package com.example.taxiservicios;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class homeCliente extends Fragment {
    // TODO: Rename and change types of parameters
    RecyclerView recyclerPersonajes; //ok
    RecyclerView.Adapter adapter;
    ArrayList<modeloCliente> listaPersonaje;
    String correo;
    // TODO: Rename and change types and number of parameters
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homecliente, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo=preferences.getString("correo",null);
        recyclerPersonajes= (RecyclerView) view.findViewById(R.id.datos);
        recyclerPersonajes.setHasFixedSize(true);
        recyclerPersonajes.setLayoutManager(new LinearLayoutManager(getContext()));
        listaPersonaje= new ArrayList<>();
        llenarLista("http://pruebataxi.laviveshop.com/app/consultarServiciosCliente.php",correo);
        return view;
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
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            modeloCliente modelo =new modeloCliente(
                                    jsonObject.getString("identificador"),
                                    "Fecha del servicio: "+jsonObject.getString("fecha")+"\n"+"Pasaran por ti a las: "+jsonObject.getString("hora"),
                                    "Direccion de destino: "+jsonObject.getString("direccion"),
                                    "Status del servicio:"+jsonObject.getString("status"));
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
                             if(lista.equals("Status del servicio:abierta")){
                                 Bundle datosAEnviar = new Bundle();
                                 datosAEnviar.putString("identificador",id);
                                 modificarServicio modificarservicio = new modificarServicio();
                                 modificarservicio.setArguments(datosAEnviar);
                                 AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                 activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,modificarservicio).addToBackStack(null).commit();
                             }
                             else if (lista.equals("Status del servicio:Confirmada"))
                                 {
                                     Bundle datosAEnviar = new Bundle();
                                     datosAEnviar.putString("identificador",id);
                                     verAutomovil verAutomovil = new verAutomovil();
                                     verAutomovil.setArguments(datosAEnviar);
                                     AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                     activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,verAutomovil).addToBackStack(null).commit();
                                 }
                             else if (lista.equals("Status del servicio:realizada"))
                             {
                                 Toast.makeText(getActivity(),lista,Toast.LENGTH_SHORT).show();
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
}
