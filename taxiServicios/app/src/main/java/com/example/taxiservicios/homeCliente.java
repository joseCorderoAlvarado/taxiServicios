package com.example.taxiservicios;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
TextView tvservicios,tvserviciogratis,tvconfirmado;
String correo;
Button btnNuevoServicio;
CardView card1;
    private Handler handler;
    private Runnable runnable;
    // TODO: Rename and change types and number of parameters
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homecliente, container, false);
       tvservicios =view.findViewById(R.id.tvservicios);
       tvserviciogratis=view.findViewById(R.id.serviciogratis);
       tvconfirmado=view.findViewById(R.id.tvconfirmado);
       btnNuevoServicio=view.findViewById(R.id.btnnuevoservicio);
       card1=view.findViewById(R.id.card1);
       SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
       correo=preferences.getString("correo",null);
       cargarservicios("http://pruebataxi.laviveshop.com/app/cantidadservicioscliente.php",correo);
       btnNuevoServicio.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               nuevoServicio servicio = new nuevoServicio();
               AppCompatActivity activity = (AppCompatActivity) v.getContext();
               activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, servicio).addToBackStack(null).commit();
           }
       });
        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run(){
                cargarconfirmado("http://pruebataxi.laviveshop.com/app/ultimoconfirmado.php",correo);
                handler.postDelayed(this, 12000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
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
    private  void cargarconfirmado(String URL, final String correov)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject valores = new JSONObject(response);
                    JSONArray jsonArray=valores.getJSONArray("confirmado");
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String noservicios = jsonObject.getString("id");
                        String fecha = jsonObject.getString("fecha");
                        String hora = jsonObject.getString("hora");
                        String destino = jsonObject.getString("destino");
                        String referencia=jsonObject.getString("referencia");
                        String taxi= jsonObject.getString("taxi");
                        String descripcion= jsonObject.getString("descripcion");
                        if (destino.equals("null")) {
                            tvconfirmado.setText("");
                        } else {
                            if (referencia.equals(""))
                            {
                                if(descripcion.equals(""))
                                {
                                    card1.setVisibility(View.VISIBLE);
                                    tvconfirmado.setText("  Ultimo Servicio Confirmado  \nFech:" + fecha +
                                            "\nHora: " + hora + "\nDestino\n" +
                                            destino+"\n\nTaxi:\n"
                                            +taxi);
                                }
                                else
                                    {
                                        card1.setVisibility(View.VISIBLE);
                                        tvconfirmado.setText("  Ultimo Servicio Confirmado  \nFech:" + fecha +
                                                "\nHora: " + hora + "\nDestino\n" +
                                                destino+"\n\nTaxi:\n"
                                                +taxi+"\n\nDescripción del taxi:\n"+descripcion);
                                    }
                            }else
                                {
                                    if (descripcion.equals("")){
                                    //    card1.setVisibility(View.VISIBLE);
                                        tvconfirmado.setText("  Ultimo Servicio Confirmado  \nFecha:" + fecha +
                                                "\nHora: " + hora + "\nDestino\n" +
                                                destino+"\n\nReferencia del viaje:\n"+referencia+"\n\nTaxi:\n"
                                                +taxi);
                                    }
                                    else
                                        {
                                      //      card1.setVisibility(View.VISIBLE);
                                            tvconfirmado.setText("  Ultimo Servicio Confirmado  \nFecha:" + fecha +
                                                    "\nHora: " + hora + "\nDestino\n" +
                                                    destino+"\n\nReferencia del viaje:\n"+referencia+"\n\nTaxi:\n"
                                                    +taxi+"\n\nDescripción del taxi:"+descripcion);
                                        }
                                }
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
                parametros.put("correo",correov);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
}
