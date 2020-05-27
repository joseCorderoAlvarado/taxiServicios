package com.example.taxiservicios;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
public class homeChofer extends Fragment {
    // TODO: Rename and change types of parameters
    RecyclerView recyclerPersonajes; //ok
    RecyclerView.Adapter adapter;
    TextView txtTitulo;
    ArrayList<modeloChofer> listaPersonaje;
    String correo;
    //String URL_consultarServiciosChoferPendientes="http://192.168.1.105/Taxis-Pruebas/consultarHistorialChofer.php.php";


    String URL_consultarServiciosChoferPendientes="http://pruebataxi.laviveshop.com/app/consultarServiciosChofer.php";


    // TODO: Rename and change types and number of parameters
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servicios_pendientes_chofer, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);

        txtTitulo = (TextView)view.findViewById(R.id.titulo);
        txtTitulo.setText("Servicios Realizados");
        correo=preferences.getString("correo",null);
        recyclerPersonajes= (RecyclerView) view.findViewById(R.id.datosServiciosPendientesChofer);
        recyclerPersonajes.setHasFixedSize(true);
        recyclerPersonajes.setLayoutManager(new LinearLayoutManager(getContext()));
        listaPersonaje= new ArrayList<>();
        llenarLista(URL_consultarServiciosChoferPendientes,correo);
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
                            modeloChofer modelo =new modeloChofer(
                                    "Cliente: " + jsonObject.getString("cliente")  + "\n",
                                    "Recoger el dia: " + jsonObject.getString("fecha") + " a las: " + jsonObject.getString("hora") + " horas " + "\n",
                                    "Donde se recogera: "+ jsonObject.getString("recoger") + "\n",
                                    "Donde se dirige: "+ jsonObject.getString("llevar") + "\n",
                                    "Télefono: "+ jsonObject.getString("telefono") + "\n"
                            );
                            listaPersonaje.add(modelo);

                        }
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        recyclerPersonajes.setLayoutManager(manager);
                        recyclerPersonajes.setHasFixedSize(true);
                        AdaptadorChofer adapter=new AdaptadorChofer(listaPersonaje);
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
