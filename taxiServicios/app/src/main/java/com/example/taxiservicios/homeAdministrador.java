package com.example.taxiservicios;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class homeAdministrador extends Fragment {
    // TODO: Rename and change types of parameters
    RecyclerView recyclerPersonajes; //ok
    RecyclerView.Adapter adapter;
    ArrayList<modeloAdministrador> listaPersonaje;
    String correo;
    //String URL_sss="http://192.168.1.105/Taxis-Pruebas/consultarServiciosAdmin.php";

    public static final long PERIODO = 60000; // 60 segundos (60 * 1000 millisegundos)
    private Handler handler;
    private Runnable runnable;
    String URL_sss="http://pruebataxi.laviveshop.com/app/consultarServiciosAdmin.php";



    // TODO: Rename and change types and number of parameters
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homeadministrador, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo=preferences.getString("correo",null);
        recyclerPersonajes= (RecyclerView) view.findViewById(R.id.datosAdmin);
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
                listaPersonaje= new ArrayList<>();
                llenarLista(URL_sss);
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
    private void llenarLista(String URL) {
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
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            modeloAdministrador modelo =new modeloAdministrador(
                                    jsonObject.getString("identificador"),
                                    "Fecha: "+jsonObject.getString("fecha")+"\n"+"Hora: "+jsonObject.getString("hora")+"\n",
                                    "Destino: "+jsonObject.getString("direccion")+"\n",
                                    "Comentarios:\n"+jsonObject.getString("referencia"),
                                    "Servicio:"+jsonObject.getString("status"),
                                    "Cliente: " + jsonObject.getString("nombre"),
                                    "Vehiculo:  " + jsonObject.getString("vehiculoCompleto"),
                                    jsonObject.getString("costo")
                                    );
                            listaPersonaje.add(modelo);
                        }
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        recyclerPersonajes.setLayoutManager(manager);
                        recyclerPersonajes.setHasFixedSize(true);
                        AdaptadorAdministrador adapter=new AdaptadorAdministrador(listaPersonaje);
                        recyclerPersonajes.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String lista=listaPersonaje.get(recyclerPersonajes.getChildAdapterPosition(v)).getStatus();
                                String id=listaPersonaje.get(recyclerPersonajes.getChildAdapterPosition(v)).getIdentificador();
                                if(lista.equals("Servicio:abierta")){
                                    Bundle datosAEnviar = new Bundle();
                                    datosAEnviar.putString("identificador",id);
                                    confirmarServicio confirmarServicio = new confirmarServicio();
                                    confirmarServicio.setArguments(datosAEnviar);
                                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,confirmarServicio).addToBackStack(null).commit();
                                }
                                else if (lista.equals("S:Confirmada"))
                                {
                                    //Bundle datosAEnviar = new Bundle();
                                    //datosAEnviar.putString("identificador",id);
                                   // verAutomovil verAutomovil = new verAutomovil();
                                   // verAutomovil.setArguments(datosAEnviar);
                                   // AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                   // activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,verAutomovil).addToBackStack(null).commit();
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
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
}
