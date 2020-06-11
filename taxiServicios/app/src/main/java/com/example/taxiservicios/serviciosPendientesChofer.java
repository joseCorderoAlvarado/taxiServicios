package com.example.taxiservicios;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class serviciosPendientesChofer extends Fragment {
    // TODO: Rename and change types of parameters
    RecyclerView recyclerPersonajes; //ok
    RecyclerView.Adapter adapter;
    ArrayList<modeloChoferServiciosPendientes> listaPersonaje;
    String correo;
    TextView txtTitulo;
    //String URL_consultarServiciosChofer="http://192.168.1.105/Taxis-Pruebas/consultarServiciosChofer.php";


    String URL_consultarServiciosChoferRealizados="http://pruebataxi.laviveshop.com/app/consultarHistorialChofer.php";


    // TODO: Rename and change types and number of parameters
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homechofer, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        txtTitulo = (TextView)view.findViewById(R.id.titulo);
        txtTitulo.setText("Servicios Realizados y Cancelados");

        correo=preferences.getString("correo",null);
        recyclerPersonajes= (RecyclerView) view.findViewById(R.id.datosChofer);
        recyclerPersonajes.setHasFixedSize(true);
        recyclerPersonajes.setLayoutManager(new LinearLayoutManager(getContext()));
        listaPersonaje= new ArrayList<>();
        llenarLista(URL_consultarServiciosChoferRealizados,correo);
        return view;
    }
    private void llenarLista(String URL,final String correov) {
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
                            modeloChoferServiciosPendientes modelo =new modeloChoferServiciosPendientes(
                                    "Cliente: " + jsonObject.getString("cliente")  + "\n",
                                    "Calificación: " + jsonObject.getString("evaluacion") + " Estrellas " + "\n",
                                    "Nota del servicio: "+ jsonObject.getString("nota") + "\n",
                                    "Fecha/Hora realizado: " + jsonObject.getString("fecha") + " a las: " + jsonObject.getString("hora") + " horas " + "\n",
                                    jsonObject.getString("status_idstatus"),
                                    jsonObject.getString("idservicios"),
                                    jsonObject.getString("evaluacionCliente")
                            ) ;
                            listaPersonaje.add(modelo);

                        }
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        recyclerPersonajes.setLayoutManager(manager);
                        recyclerPersonajes.setHasFixedSize(true);
                        AdaptadorChoferServiciosPendientes adapter=new AdaptadorChoferServiciosPendientes(listaPersonaje);
                        recyclerPersonajes.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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
