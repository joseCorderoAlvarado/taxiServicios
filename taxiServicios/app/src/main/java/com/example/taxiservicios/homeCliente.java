package com.example.taxiservicios;
import android.content.Context;
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
import java.util.HashMap;
import java.util.Map;
public class homeCliente extends Fragment {
    RecyclerView recyclerPersonajes; //ok
    RecyclerView.Adapter adapter;
    ArrayList<modeloCliente> listaPersonaje;
    String correo;
    TextView tvservicios,tvserviciogratis,tvconfirmado;
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
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
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
                cargarservicios("http://pruebataxi.laviveshop.com/app/cantidadservicioscliente.php",correo);
                listaPersonaje= new ArrayList<>();
                llenarLista("http://pruebataxi.laviveshop.com/app/consultarServiciosClienteHome.php",correo);
                handler.postDelayed(this, PERIODO);
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
                                        "Descripción del Taxi: \n" + jsonObject.getString("descripcionVehiculo"));
                            } else {
                                System.out.println("Refreshed token: " + statusServicio);
                                modelo =new modeloCliente(
                                        jsonObject.getString("identificador"),
                                        "Fecha: "+jsonObject.getString("fecha")+"\n\n"+"Hora: "+jsonObject.getString("hora"),
                                        "Destino: "+jsonObject.getString("direccion"),
                                        "Servicio:"+statusServicio,
                                        jsonObject.getString("evaluacion")
                                );
                            }
                            listaPersonaje.add(modelo);
                        }
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        recyclerPersonajes.setLayoutManager(manager);
                        recyclerPersonajes.setHasFixedSize(true);
                        AdaptadorCliente adapter=new AdaptadorCliente(listaPersonaje);
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
                                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
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
                        tvserviciogratis.setText(gratis);
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



}
