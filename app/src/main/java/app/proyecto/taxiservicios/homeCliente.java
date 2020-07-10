package app.proyecto.taxiservicios;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.taxiservicios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
public class homeCliente extends Fragment {
    RecyclerView recyclerPersonajes; //ok
    RecyclerView.Adapter adapter;
    ArrayList<modeloCliente> listaPersonaje;
    String correo;
    TextView tvservicios,tvserviciogratis,tvconfirmado,tvsgratisobt;
    Button btnNuevoServicio;
    private Handler handler;
    private Runnable runnable;





    public static final long PERIODO = 30000; // 60 segundos (60 * 1000 millisegundos)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historialservicioclientehome, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo=preferences.getString("correo",null);
        tvservicios =view.findViewById(R.id.tvservicios);
        tvserviciogratis=view.findViewById(R.id.serviciogratis);
        tvconfirmado=view.findViewById(R.id.tvconfirmado);
        btnNuevoServicio=view.findViewById(R.id.btnnuevoservicio);
        btnNuevoServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevoServicio servicio = new nuevoServicio();
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, servicio).addToBackStack(null).commit();
            }
        });


        recyclerPersonajes= (RecyclerView) view.findViewById(R.id.datosh);
        recyclerPersonajes.setHasFixedSize(true);
        recyclerPersonajes.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run(){


                final String URL_alarmasDia="http://pruebataxi.laviveshop.com/app/consultarServiciosDelDia.php";



                cargarservicios("http://pruebataxi.laviveshop.com/app/cantidadservicioscliente.php",correo);
                listaPersonaje= new ArrayList<>();
                llenarLista("http://pruebataxi.laviveshop.com/app/consultarServiciosClienteHome.php",correo);
                handler.postDelayed(this, PERIODO);
                //Lo dejo por si se ocupara a futuro,pero no lo mando a llamar
                System.out.println("El correo alarma es: " + correo);
                llenarListaAlarmas(URL_alarmasDia,correo,getContext());

            }
        };
        handler.postDelayed(runnable, 1000);
    }
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private void llenarLista(String URL, final String correov) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty())
                {
                    try {
                        JSONObject servicios = new JSONObject(response);
                        JSONArray jsonArray=servicios.getJSONArray("servicios");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            modeloCliente modelo;
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String statusServicio =jsonObject.getString("status");
                            if (statusServicio.contains("Confirmada")) {
                                modelo =new modeloCliente(
                                        jsonObject.getString("identificador"),
                                        "Fecha: "+jsonObject.getString("fecha")+"\n\n"+"Hora: "+jsonObject.getString("hora"),
                                        "Destino: "+jsonObject.getString("direccion"),
                                        "Servicio:"+ statusServicio,
                                        "Taxi: \n" + jsonObject.getString("vehiculoCompleto"),
                                        "Descripción del Taxi: \n" + jsonObject.getString("descripcionVehiculo"),
                                        "\nCosto aproximado del servicio: $"+jsonObject.getString("costo"));
                            } else {
                                System.out.println("Refreshed token: " + statusServicio);
                                modelo =new modeloCliente(
                                        jsonObject.getString("identificador"),
                                        "Fecha: "+jsonObject.getString("fecha")+"\n\n"+"Hora: "+jsonObject.getString("hora"),
                                        "Destino: "+jsonObject.getString("direccion"),
                                        "Servicio:"+statusServicio,
                                        jsonObject.getString("evaluacion"),
                                        ""
                                );
                            }
                            listaPersonaje.add(modelo);
                        }
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        recyclerPersonajes.setLayoutManager(manager);
                        recyclerPersonajes.setHasFixedSize(true);
                        AdaptadorCliente adapter=new AdaptadorCliente(listaPersonaje,getContext());
                        recyclerPersonajes.setAdapter(adapter);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String lista=listaPersonaje.get(recyclerPersonajes.getChildAdapterPosition(v)).getStatus();
                                String id=listaPersonaje.get(recyclerPersonajes.getChildAdapterPosition(v)).getIdentificador();
                                if(lista.equals("Servicio:abierta")){
                                    Bundle datosAEnviar = new Bundle();
                                    datosAEnviar.putString("identificador",id);
                                    modificarServicio modificarservicio = new modificarServicio();
                                    modificarservicio.setArguments(datosAEnviar);
                                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,modificarservicio).addToBackStack(null).commit();
                                }
                                else if (lista.equals("Servicio:Confirmada"))
                                {
                                    Bundle datosAEnviar = new Bundle();
                                    datosAEnviar.putString("identificador",id);
                                    verAutomovil verAutomovil = new verAutomovil();
                                    verAutomovil.setArguments(datosAEnviar);
                                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,verAutomovil).addToBackStack(null).commit();
                                }
                                else if (lista.equals("Servicio:realizada"))
                                {
                                    Bundle datosAEnviar = new Bundle();
                                    datosAEnviar.putString("identificador",id);
                                    evaluarservicio evaluarservicio = new evaluarservicio();
                                    evaluarservicio.setArguments(datosAEnviar);
                                    AppCompatActivity activity = (AppCompatActivity) getContext();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,evaluarservicio).addToBackStack(null).commit();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",correov);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }






    private  void cargarservicios(String URL, final String correov)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject valores = new JSONObject(response);
                    JSONArray jsonArray=valores.getJSONArray("cantidad");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String noservicios =jsonObject.getString("cuenta");
                        String gratis=jsonObject.getString("value");
                        String sgratis= jsonObject.getString("gratis");
                        tvserviciogratis.setText(gratis+"\n\n Has Obtenido:"+sgratis +" Servicios gratis");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",correov);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }



    private void llenarListaAlarmas(String URL, final String correov, final Context contex) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    try {
                        JSONObject servicios = new JSONObject(response);
                        JSONArray jsonArray = servicios.getJSONArray("servicios");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //Obtenemos los valores
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String fechaServicio = jsonObject.getString("fecha");
                            String horaServicio = jsonObject.getString("hora");


                            String[] separar = fechaServicio.split("-");
                            String year = separar[0]; // el año
                            String month = separar[1]; // el mes
                            String day = separar[2]; // el dia

                            String[] separar2 = horaServicio.split(":");
                            String hour = separar2[0]; // la hora
                            String minute = separar2[1]; // el minuto



                            Calendar calendar = Calendar.getInstance();


                            calendar.set(Calendar.YEAR, Integer.parseInt(year));
                            calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);

                            System.out.println("El calendario: " + calendar.toString());
                            Calendar cur = Calendar.getInstance();
                            //long sss=1591324200000l;

                            System.out.println("Fecha Actual: " + cur.toString());
                            int anticipacionNotificacion = (20 * 60 * 1000);//Minutos*segundos*milisegundos

                            long milisegundosCalendario=calendar.getTimeInMillis()-anticipacionNotificacion;
                            long milisegundosActuales=cur.getTimeInMillis();

                            if (milisegundosCalendario>milisegundosActuales) {
                              //  calendar.add(Calendar.DATE, 1);





                            System.out.println("sss2: " +  calendar.getTimeInMillis());
                            calendar.setTimeInMillis(calendar.getTimeInMillis());
                            System.out.println("sss5: " + calendar.getTime());

                            Intent myIntent = new Intent(contex, servicioAlarmas.class);
                            int ALARM1_ID = i+1;
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                    contex, ALARM1_ID, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) contex.getSystemService(Context.ALARM_SERVICE);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, milisegundosCalendario ,  pendingIntent);
                            System.out.println("Alarma: " + ALARM1_ID);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contex,error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",correov);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(contex);
        requestQueue.add(stringRequest);
    }



}
