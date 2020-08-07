package app.proyecto.taxiservicios;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.taxiservicios.AdaptadorCliente;
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

public class nuevoServicio extends Fragment {

    //Estos textos hacen referencias a los spinners y el ese
TextView texto1,texto2,texto3;



DatePicker fecha;
TimePicker hora;
Spinner sOrigen, sDestino;
EditText txtOrigen, txtDestino,txtcomentarios;
Button btnNuevo,btn1,btn2;
String correo,valors1,valors2,fechac,horac,origen,destino,comentarios2;
Double latitudorigen,longitudorigen,latituddestino,longituddestino;
Dialog myDialog;
    Spinner x;
List<String> direccion1obt =  new ArrayList<String>();
List<String> direccion2obt =  new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.fragment_nuevoservicio,container,false);
        fecha=view.findViewById(R.id.dpfecha);
        fecha.setMinDate(System.currentTimeMillis() - 1000);
        hora=view.findViewById(R.id.tphora);
        btn1=view.findViewById(R.id.btn1);
        myDialog = new Dialog(getActivity().getBaseContext());
        x = myDialog.findViewById(R.id.spinner1);
        btn2=view.findViewById(R.id.btn2);
        sOrigen=view.findViewById(R.id.spinnerpartida);
        sDestino=view.findViewById(R.id.spinnerdestino);
        txtOrigen=view.findViewById(R.id.txtOrigen);
        txtDestino=view.findViewById(R.id.txtDestino);
        txtcomentarios=view.findViewById(R.id.txtComentarios);
        btnNuevo=view.findViewById(R.id.btnmodificar);
        fecha.setMinDate(System.currentTimeMillis() - 1000);

        texto1=view.findViewById(R.id.textView14);
        texto2=view.findViewById(R.id.textView12);
        texto3=view.findViewById(R.id.textView13);
        Bundle datosRecuperados = getArguments();
        Bundle datosRecuperados2 = getArguments();
        if(datosRecuperados!=null)
        {
            final String direccionorigen = datosRecuperados.getString("direccionorigen");
            txtOrigen.setText(direccionorigen);

        }
        else
        {
        }
        if(datosRecuperados2!=null)
        {
            final String direcciondestino = datosRecuperados.getString("direcciondestino");
            txtDestino.setText(direcciondestino);
        }
        else
            {

            }

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

        //Ocultamos los spinners y los textos de arriba
        cargardireccion1("http://pruebataxi.laviveshop.com/app/consultardireccion1.php",correo);
        cargardireccion2("http://pruebataxi.laviveshop.com/app/consultardireccion2.php",correo);
        sOrigen.setVisibility(View.GONE);
        sDestino.setVisibility(View.GONE);
        texto1.setVisibility(View.GONE);
        texto2.setVisibility(View.GONE);
        btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);
        texto3.setText("Elegir ubicaciónes Guardadas");
        texto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.direcciones,null);
                final Spinner mSpinner =(Spinner) mView.findViewById(R.id.spinner1);
                Button btno=(Button) mView.findViewById(R.id.btnx);
                Button btnd=(Button) mView.findViewById(R.id.btny);
                cargardireccionada("http://pruebataxi.laviveshop.com/app/consultardireccion1.php",correo,mSpinner);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                btno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        valors1 = mSpinner.getSelectedItem().toString();
                        if (valors1.equals("seleccione una direccion"))
                        {
                            Toast.makeText(getActivity().getBaseContext(), "Ingresa una direccion valida", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            txtOrigen.setText(valors1);
                            dialog.dismiss();
                        }
                    }
                });
                btnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        valors2 = mSpinner.getSelectedItem().toString();
                        if (valors2.equals("seleccione una direccion"))
                        {
                            Toast.makeText(getActivity().getBaseContext(), "Ingresa una direccion valida", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            txtDestino.setText(valors2);
                            dialog.dismiss();
                        }
                    }
                });

            }
        });
        //fin
         final String URL_spnuevoservicio="http://pruebataxi.laviveshop.com/app/spregistrarservicio.php";
        final String URL_nuevoservicio="http://pruebataxi.laviveshop.com/app/agendarservicio.php";
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valors1 = sOrigen.getSelectedItem().toString();
                if(valors1.equals("seleccione una direccion"))
                {
                    Toast.makeText(getActivity().getBaseContext(), "selecciona una direccion de origen", Toast.LENGTH_SHORT).show();
                }else {
                    txtOrigen.setText(valors1);
                }

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valors2=sOrigen.getSelectedItem().toString();
                if (valors2.equals("seleccione una direccion"))
                {
                    Toast.makeText(getActivity().getBaseContext(), "selecciona una direccion de destino", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        txtDestino.setText(valors2);
                    }

            }
        });
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
                        verificarServicio(URL_nuevoservicio,view);
                    }else{


                        hora.setHour(currentHourIn24Format);
                        Toast.makeText(getActivity().getBaseContext(), "Necesitas pedir el taxi con minimo  10 minutos de anticipacion", Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                  //  Log.d("currentMonth",currentMonth + "");
                   // Log.d("fecha get month",fecha.getMonth() + "");
                    int axuliarMinutos = hora.getCurrentMinute()-currentMinute;
                    if(axuliarMinutos>10 || hora.getCurrentHour()>currentHourIn24Format||fecha.getDayOfMonth()>currentDay || fecha.getMonth()>currentMonth || fecha.getYear()>currentYear ){
                        verificarServicio(URL_nuevoservicio,view);
                    } else{
                        hora.setCurrentHour(currentHourIn24Format);
                        Toast.makeText(getActivity().getBaseContext(), "Necesitas pedir el taxi con minimo 10 minutos de anticipacion", Toast.LENGTH_SHORT).show();

                    }
                }

                }
        });
        return view;
    }
    private void cargardireccionada(String URL, final String Correv,final Spinner x)
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
                    x.setAdapter(new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_dropdown_item,direccion1obt));

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
    private void verificarServicio(final String URL, final View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Crear Servicio");
        alertDialogBuilder
                .setMessage("¿Estás seguro de agendar este servicio?")
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
                                    spnuevoservicio("http://pruebataxi.laviveshop.com/app/spregistrarservicio.php", valors1, valors2, fechac, horac, comentarios2, correo);
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
                            nuevoservicio(URL, origen, destino, origen, destino, fechac, horac, comentarios2, correo);
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
    private  void nuevoservicio(String URL, final String sd1, final String sd2, final String d1, final String d2,
                                final String fecha, final String hora, final String comentario, final String correov)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"tu servicio de taxi se creo con exito!!",Toast.LENGTH_SHORT).show();
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
                parametros.put("d1",d1);
                parametros.put("d2",d2);
                parametros.put("referencia",comentario);
                parametros.put("correo",correov);
                return parametros;
            }
        };
        //Ocho segundos de espera y lo demas default
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//espera del ese
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
    private  void spnuevoservicio(String URL, final String sd1, final String sd2,
                                  final String fecha, final String hora, final String comentario, final String correov)
    {

        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"tu servicio de taxi se creo con exito!!",Toast.LENGTH_SHORT).show();
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
                return parametros;
            }
        };
        //Ocho segundos de espera y lo demas default
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//espera del ese
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
}
