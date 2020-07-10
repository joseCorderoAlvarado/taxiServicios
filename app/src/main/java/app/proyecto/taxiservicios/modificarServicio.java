package app.proyecto.taxiservicios;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.taxiservicios.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class modificarServicio extends Fragment {
    DatePicker fecha;
    TimePicker hora;
    Spinner sOrigen, sDestino;
    EditText txtOrigen, txtDestino,txtcomentarios;
    Button btnNuevo,btnEliminar;
    String correo,valors1,valors2,fechac,horac,origen,destino,comentarios2;





    Double latitudorigen,longitudorigen,latituddestino,longituddestino;
    List<String> direccion1obt =  new ArrayList<String>();
    List<String> direccion2obt =  new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.fragment_modificarservicio,container,false);
        Bundle datosRecuperados = getArguments();
        final String idrecuperado = datosRecuperados.getString("identificador");
        System.out.println("El idrecuerado es: " + idrecuperado);
        fecha=view.findViewById(R.id.dpfecha);
        fecha.setMinDate(System.currentTimeMillis() - 1000);
        hora=view.findViewById(R.id.tphora);
        sOrigen=view.findViewById(R.id.spinnerpartida);
        sDestino=view.findViewById(R.id.spinnerdestino);
        txtOrigen=view.findViewById(R.id.txtOrigen);
        txtDestino=view.findViewById(R.id.txtDestino);
        txtcomentarios=view.findViewById(R.id.txtComentarios);
        btnNuevo=view.findViewById(R.id.btnmodificar);
        btnEliminar=view.findViewById(R.id.btncancelar);

        fecha.setMinDate(System.currentTimeMillis() - 1000);
        Places.initialize(getContext(),"AIzaSyC9fjPKZnzHxEQTPf97KLzprLcoss5DGcE");
        PlacesClient placesClient=Places.createClient(getContext());
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autcomplete_fragment);
        // autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(21.5039,-104.895 ),
                new LatLng(21.5039,-104.895)));
        autocompleteFragment.setCountries("MX");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "*Place (pick-up) latitude: " + place.getLatLng().latitude + " longitude: " + place.getLatLng().longitude);
                // Log.i(TAG,"Place"+ place.getAddress()+","+place.getAddressComponents()+","+place.getTypes()+","+place.getLatLng());
                // txtOrigen.setText(""+place.getLatLng().latitude);
                txtOrigen.setText(place.getAddress());
            }
            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG,"un error a ocurrido"+status);
            }
        });
        AutocompleteSupportFragment autocompleteFragment2 = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autcomplete_fragment2);
        // autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment2.setLocationBias(RectangularBounds.newInstance(
                new LatLng(21.5039,-104.895 ),
                new LatLng(21.5039,-104.895)));
        autocompleteFragment2.setCountries("MX");
        autocompleteFragment2.setPlaceFields(Arrays.asList(Place.Field.NAME,Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "*Place2 (pick-up) latitude: " + place.getLatLng().latitude + " longitude: " + place.getLatLng().longitude);
                //       Log.i(TAG,"Place"+ place.getAddress()+","+place.getLatLng()+","+place.getTypes()+","+place.getLatLng());
                txtDestino.setText(place.getAddress());
            }
            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG,"un error a ocurrido"+status);
            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo=preferences.getString("correo",null);
        cargardireccion1("http://pruebataxi.laviveshop.com/app/consultardireccion1.php",correo);
        cargardireccion2("http://pruebataxi.laviveshop.com/app/consultardireccion2.php",correo);
        final String URL_modificarServicio="http://pruebataxi.laviveshop.com/app/actualizarservicio.php";
        final String URL_modificarServicioX="http://pruebataxi.laviveshop.com/app/actualizarservicio.php";



        cargardatos("http://pruebataxi.laviveshop.com/app/consultarservicioabierto.php",idrecuperado);



        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar rightNow = Calendar.getInstance();
                int currentMinute =rightNow.get(Calendar.MINUTE);
                int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
                int currentDay = rightNow.get(Calendar.DAY_OF_MONTH);
                int currentMonth = rightNow.get(Calendar.MONTH) ;
                int currentYear = rightNow.get(Calendar.YEAR);



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int axuliarMinutos = hora.getMinute()-currentMinute;


                    if(axuliarMinutos>10 || hora.getHour()>currentHourIn24Format || fecha.getDayOfMonth()>currentDay || fecha.getMonth()>currentMonth || fecha.getYear()>currentYear ){
                        verificarServicio(URL_modificarServicio,view,idrecuperado);
                    }else{


                        hora.setHour(currentHourIn24Format);
                        Toast.makeText(getActivity().getBaseContext(), "Necesitas pedir el taxi con minimo una hora de anticipacion", Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    //  Log.d("currentMonth",currentMonth + "");
                    // Log.d("fecha get month",fecha.getMonth() + "");
                    int axuliarMinutos = hora.getCurrentMinute()-currentMinute;
                    if(axuliarMinutos>10 || hora.getCurrentHour()>currentHourIn24Format||fecha.getDayOfMonth()>currentDay || fecha.getMonth()>currentMonth || fecha.getYear()>currentYear ){
                        verificarServicio(URL_modificarServicio,view,idrecuperado);
                    } else{
                        hora.setCurrentHour(currentHourIn24Format);
                        Toast.makeText(getActivity().getBaseContext(), "Necesitas pedir el taxi con minimo 10 minutos de anticipacion", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });



        //Boton Eliminar
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Mi Dialogo");
                alertDialogBuilder
                        .setMessage("¿Estás seguro de Eliminar este servicio?")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                eliminarservicio("http://pruebataxi.laviveshop.com/app/eliminarservicio.php",idrecuperado);
                                homeCliente home = new homeCliente();
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, home).addToBackStack(null).commit();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        });






        return view;
    }
    private void cargardireccion1(String URL, final String Correv)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject valores = new JSONObject(response);
                    JSONArray jsonArray=valores.getJSONArray("d1");
                    direccion1obt.add("seleccione una direccion");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nombredireccion1=jsonObject.getString("direccion1");
                        Log.d("valor",nombredireccion1);
                        direccion1obt.add(nombredireccion1);
                    }
                    sOrigen.setAdapter(new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_dropdown_item,direccion1obt));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",Correv);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }

    private void cargardireccion2(String URL, final String Correv)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject valores = new JSONObject(response);
                    JSONArray jsonArray=valores.getJSONArray("d2");
                    direccion2obt.add("seleccione una direccion");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nombredireccion1=jsonObject.getString("direccion2");
                        direccion2obt.add(nombredireccion1);
                    }
                    sDestino.setAdapter(new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_dropdown_item,direccion2obt));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",Correv);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
    private void verificarServicio(final String URL, final View view, final String idrecuperado){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Modificar Servicio");
        alertDialogBuilder
                .setMessage("¿Estás seguro de modificar este servicio?")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        valors1 = sOrigen.getSelectedItem().toString();
                        valors2 = sDestino.getSelectedItem().toString();
                        fechac = "" + fecha.getYear() + "-" + (fecha.getMonth()+1) + "-" + fecha.getDayOfMonth() + "";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            String minuteZero = (hora.getMinute()>=10)? Integer.toString(hora.getMinute()):
                                    String.format("0%s", Integer.toString(hora.getMinute()));
                            horac = "" + hora.getHour() + ":"+minuteZero;
                        }

                        else{

                            String minuteZero = (hora.getCurrentMinute() >= 10) ? Integer.toString(hora.getCurrentMinute()) :
                                    String.format("0%s", Integer.toString(hora.getCurrentMinute()));
                            horac = " " + hora.getCurrentHour() + ":" + minuteZero;
                        }
                        origen = txtOrigen.getText().toString();
                        destino = txtDestino.getText().toString();
                        comentarios2 = txtcomentarios.getText().toString();
                        if (origen.isEmpty() && destino.isEmpty()) {
                            if (valors1.equals("seleccione una direccion"))
                            {
                                Toast.makeText(getActivity().getBaseContext(), "selecciona una direccion de origen", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (valors2.equals("seleccione una direccion")) {
                                    Toast.makeText(getActivity().getBaseContext(), "selecciona una direccion de destino", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("fechac",fechac);
                                    modificarServicio(URL, valors1, valors2, fechac, horac, comentarios2, correo,idrecuperado);
                                    homeCliente modificarservicio = new homeCliente();
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, modificarservicio).addToBackStack(null).commit();
                                }
                            }
                        }
                        else if (origen.isEmpty() && !destino.isEmpty()) {
                            Toast.makeText(getActivity().getBaseContext(), "necesitas ingresar la direccion de origen", Toast.LENGTH_SHORT).show();
                        } else if (destino.isEmpty() && !origen.isEmpty()) {
                            Toast.makeText(getActivity().getBaseContext(), "necesitas ingresar la direccion de destino", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("fechac",fechac);
                            modificarServicioX(URL, origen, destino, origen, destino, fechac, horac, comentarios2, correo,idrecuperado);
                            homeCliente modificarservicio = new homeCliente();
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,modificarservicio).addToBackStack(null).commit();
                        }
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                }).create().show();

    }
    private  void modificarServicioX(String URL, final String sd1, final String sd2, final String d1, final String d2,
                                final String fecha, final String hora, final String comentario, final String correov, final String id)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"Servicio Modificado con exito!!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),"Error al modificar el servicio",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("fecha",fecha.toString());
                parametros.put("hora",hora.toString());
                parametros.put("direccion",sd1);
                parametros.put("direccion2",sd2);
                parametros.put("d1",d1);
                parametros.put("d2",d2);
                parametros.put("referencia",comentario);
                parametros.put("correo",correov);
                parametros.put("id",id);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }



    private  void modificarServicio(String URL, final String sd1, final String sd2,
                                  final String fecha, final String hora, final String comentario, final String correov,final String id)
    {

        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"Servicio Modiciado con exito!!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),"Error al registrar el servicio",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("fecha",fecha.toString());
                parametros.put("hora",hora.toString());
                parametros.put("direccion",sd1);
                parametros.put("direccion2",sd2);
                parametros.put("referencia",comentario);
                parametros.put("correo",correov);
                parametros.put("id",id);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }


//Cargar Datos
    private  void cargardatos(String URL, final String idrecuperado)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject valores = new JSONObject(response);
                    JSONArray jsonArray=valores.getJSONArray("datos");
                    Log.d( "hola",jsonArray.getString(0));
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String fechaobt =jsonObject.getString("fecha");
                        String horaobt =jsonObject.getString("hora");
                        String origenobt =jsonObject.getString("origen");
                        String destinoobt =jsonObject.getString("destino");
                        String comentariosobt =jsonObject.getString("comentarios");





                        txtOrigen.setText(origenobt);
                        txtDestino.setText(destinoobt);
                        txtcomentarios.setText(comentariosobt);


                        String[] separar = fechaobt.split("-");
                        String year = separar[0]; // el año
                        String month = separar[1]; // el mes
                        String day = separar[2]; // el dia

                       fecha.init(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day),null);

                        String[] separar2 = horaobt.split(":");
                        String hour = separar2[0]; // la hora
                        String minute = separar2[1]; // el minuto

                       minute= minute.trim();
                        hour= hour.trim();



                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hora.setHour(Integer.parseInt(hour));
                            hora.setMinute(Integer.parseInt(minute));
                        } else{
                            hora.setCurrentHour(Integer.parseInt(hour));
                            hora.setCurrentMinute(Integer.parseInt(minute));
                        }


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
                parametros.put("id",idrecuperado);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }


//Metodo de Eliminar Servicio
    private  void eliminarservicio(String URL, final String identificador)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"¡Servicio cancelado con exito!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),"Error al eliminar el servicio",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("id",identificador);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }

}
