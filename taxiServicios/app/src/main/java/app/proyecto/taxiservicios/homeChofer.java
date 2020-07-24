package app.proyecto.taxiservicios;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.taxiservicios.R;
import android.location.LocationListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class homeChofer extends Fragment {
    // TODO: Rename and change types of parameters
    RecyclerView recyclerPersonajes; //ok
    RecyclerView.Adapter adapter;
    TextView txtTitulo;
    ArrayList<modeloChofer> listaPersonaje;
    String correo;
    private Handler handler;
    private Runnable runnable;
    private RequestQueue requestQueue;
    private Runnable runnable2;
    public static final long PERIODO = 10000; // 60 segundos (60 * 1000 millisegundos)
    Geocoder geocoder;
    List<Address> direccion;
    private LocationManager locManager;
    private Location loc;
    double Latusuer;
    double Longuser;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 507;
    //String URL_consultarServiciosChoferPendientes="http://192.168.1.105/Taxis-Pruebas/consultarHistorialChofer.php.php";
    String URL_consultarServiciosAsignados="http://pruebataxi.laviveshop.com/app/consultarServiciosChofer.php";


    // TODO: Rename and change types and number of parameters
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servicios_pendientes_chofer, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
       getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        txtTitulo = (TextView)view.findViewById(R.id.titulo);
        txtTitulo.setText("Servicios Asignados");
        correo=preferences.getString("correo",null);
        recyclerPersonajes= (RecyclerView) view.findViewById(R.id.datosServiciosPendientesChofer);
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
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        LocationListener locationListener= new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                Latusuer = location.getLongitude();
                                Longuser = location.getLatitude();
                            }
                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                            }
                            @Override
                            public void onProviderEnabled(String provider) {
                            }
                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                        };
                        locManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
                        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10,locationListener);
                        loc = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);


                        if (loc!=null) {
                             Latusuer = loc.getLatitude();
                             Longuser = loc.getLongitude();
                            Log.d("x",""+Latusuer);
                            geocoder = new Geocoder(getContext(), Locale.getDefault());
                            try {
                                direccion = geocoder.getFromLocation(Latusuer, Longuser, 1);
                                String address = direccion.get(0).getAddressLine(0);
                                Log.d("address", address + "" + correo);
                                envioubicacion("http://pruebataxi.laviveshop.com/app/envioubicacion.php", address, correo);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Log.d("address",  correo);
                        }
                    }else{
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
                        return;
                    }
                }else{
                    // No se necesita requerir permiso, OS menor a 6.0.
                    LocationListener locationListener= new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Latusuer = location.getLongitude();
                            Longuser = location.getLatitude();
                        }
                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }
                        @Override
                        public void onProviderEnabled(String provider) {
                        }
                        @Override
                        public void onProviderDisabled(String provider) {
                        }
                    };
                    locManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10,locationListener);
                    loc = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);


                    if (loc!=null) {
                        Latusuer = loc.getLatitude();
                        Longuser = loc.getLongitude();
                        Log.d("x",""+Latusuer);
                        geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            direccion = geocoder.getFromLocation(Latusuer, Longuser, 1);
                            String address = direccion.get(0).getAddressLine(0);
                            envioubicacion("http://pruebataxi.laviveshop.com/app/envioubicacion.php", address, correo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Log.d("address",  correo);
                    }
                }
                handler.postDelayed(this, 500);
            }
        };

        runnable2= new Runnable() {
            @Override
            public void run() {
                listaPersonaje= new ArrayList<>();
                llenarLista("http://pruebataxi.laviveshop.com/app/asignacioncarro.php",correo);
                handler.postDelayed(runnable2, 6000);
            }
        };
        handler.postDelayed(runnable, 500);
        handler.postDelayed(runnable2, 500);
    }
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(runnable2);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // El usuario acepto los permisos.
                    Toast.makeText(getActivity(), "Gracias, aceptaste los permisos requeridos para el correcto funcionamiento de esta aplicación.", Toast.LENGTH_SHORT).show();
                }else{
                    // Permiso denegado.
                    Toast.makeText(getActivity(), "No se aceptó permisos", Toast.LENGTH_SHORT).show();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
    private  void envioubicacion(String URL, final String ubicacion, final String correox)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("ubicacion",ubicacion);
                parametros.put("correo",correox);
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }

    private void llenarLista(String URL,final String correov) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("hola",response);
                if(!response.isEmpty())
                {
                    try {
                        JSONObject servicios = new JSONObject(response);
                        JSONArray jsonArray=servicios.getJSONArray("servicios");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            modeloChofer modelo =new modeloChofer(
                                    "Cliente: " + jsonObject.getString("cliente")  + "\n",
                                    "Recoger el dia: " + jsonObject.getString("fecha") + " a las: " + jsonObject.getString("hora") + " horas " + "\n",
                                    "Donde se recogera: "+ jsonObject.getString("recoger") + "\n",
                                    "Donde se dirige: "+ jsonObject.getString("llevar") + "\n",
                                    "Télefono: "+ jsonObject.getString("telefono") + "\n",
                                    jsonObject.getString("idservicio"),
                                    "Costo aprox del servicio: $"+jsonObject.getString("costo")+"\n",
                                    jsonObject.getString("status"),
                                    correov
                            );

                            listaPersonaje.add(modelo);

                        }
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        recyclerPersonajes.setLayoutManager(manager);
                        recyclerPersonajes.setHasFixedSize(true);
                        AdaptadorChofer adapter=new AdaptadorChofer(listaPersonaje,getContext());
                        recyclerPersonajes.setAdapter(adapter);

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
        requestQueue = Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
 }
