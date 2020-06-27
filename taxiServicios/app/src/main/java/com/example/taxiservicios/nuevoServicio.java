package com.example.taxiservicios;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class nuevoServicio extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    DatePicker fecha;

    TimePicker hora;
    Spinner sOrigen, sDestino;
    EditText txtOrigen, txtDestino, txtcomentarios;
    Button btnNuevo;
    String correo, valors1, valors2, fechac, horac, origen, destino, comentarios2;
    Double latitudorigen, longitudorigen, latituddestino, longituddestino;
    List<String> direccion1obt = new ArrayList<String>();
    List<String> direccion2obt = new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_nuevoservicio, container, false);
        fecha = view.findViewById(R.id.dpfecha);
        fecha.setMinDate(System.currentTimeMillis() - 1000);
        hora = view.findViewById(R.id.tphora);
        sOrigen = view.findViewById(R.id.spinnerpartida);
        sDestino = view.findViewById(R.id.spinnerdestino);
        txtOrigen = view.findViewById(R.id.txtOrigen);
        txtDestino = view.findViewById(R.id.txtDestino);
        txtcomentarios = view.findViewById(R.id.txtComentarios);
        btnNuevo = view.findViewById(R.id.btnmodificar);
        fecha.setMinDate(System.currentTimeMillis() - 1000);
        Places.initialize(getContext(), "AIzaSyC9fjPKZnzHxEQTPf97KLzprLcoss5DGcE");
        PlacesClient placesClient = Places.createClient(getContext());
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autcomplete_fragment);
        // autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(21.5039, -104.895),
                new LatLng(21.5039, -104.895)));
        autocompleteFragment.setCountries("MX");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
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
                Log.i(TAG, "un error a ocurrido" + status);
            }
        });
        AutocompleteSupportFragment autocompleteFragment2 = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autcomplete_fragment2);
        // autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment2.setLocationBias(RectangularBounds.newInstance(
                new LatLng(21.5039, -104.895),
                new LatLng(21.5039, -104.895)));
        autocompleteFragment2.setCountries("MX");
        autocompleteFragment2.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "*Place2 (pick-up) latitude: " + place.getLatLng().latitude + " longitude: " + place.getLatLng().longitude);
                //       Log.i(TAG,"Place"+ place.getAddress()+","+place.getLatLng()+","+place.getTypes()+","+place.getLatLng());
                txtDestino.setText(place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "un error a ocurrido" + status);
            }
        });
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo = preferences.getString("correo", null);
        cargardireccion1("http://pruebataxi.laviveshop.com/app/consultardireccion1.php", correo);
        cargardireccion2("http://pruebataxi.laviveshop.com/app/consultardireccion2.php", correo);
        final String URL_spnuevoservicio = "http://pruebataxi.laviveshop.com/app/spregistrarservicio.php";
        final String URL_nuevoservicio = "http://pruebataxi.laviveshop.com/app/agendarservicio.php";
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // La App esta en ejecución
                Calendar rightNow = Calendar.getInstance();
                int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
                int currentDay = rightNow.get(Calendar.DAY_OF_MONTH);
                int currentMonth = rightNow.get(Calendar.MONTH) ;
                int currentYear = rightNow.get(Calendar.YEAR);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(hora.getHour()>currentHourIn24Format || fecha.getDayOfMonth()>currentDay || fecha.getMonth()>currentMonth || fecha.getYear()>currentYear ){
                        verificarServicio(URL_nuevoservicio,view);
                    }else{
                        hora.setHour(currentHourIn24Format);
                        Toast.makeText(getActivity().getBaseContext(), "Necesitas pedir el taxi con minimo una hora de anticipacion", Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    Log.d("currentMonth",currentMonth + "");
                    Log.d("fecha get month",fecha.getMonth() + "");
                    if(hora.getCurrentHour()>currentHourIn24Format||fecha.getDayOfMonth()>currentDay || fecha.getMonth()>currentMonth || fecha.getYear()>currentYear ){
                        verificarServicio(URL_nuevoservicio,view);
                    } else{
                        hora.setCurrentHour(currentHourIn24Format);
                        Toast.makeText(getActivity().getBaseContext(), "Necesitas pedir el taxi con minimo una hora de anticipacion", Toast.LENGTH_SHORT).show();
                    }
                }
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
                Toast.makeText(getActivity().getBaseContext(),"Servicio Creado con exito!!",Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
    private  void spnuevoservicio(String URL, final String sd1, final String sd2,
                                  final String fecha, final String hora, final String comentario, final String correov)
    {

        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"Servicio Creado con exito!!",Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
}
