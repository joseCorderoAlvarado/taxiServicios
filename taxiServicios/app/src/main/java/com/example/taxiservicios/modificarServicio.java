package com.example.taxiservicios;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
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

import java.util.HashMap;
import java.util.Map;

public class modificarServicio extends Fragment {
    DatePicker fecham;
    TimePicker horam;
    EditText txtOrigenm, txtDestinom,txtcomentariosm;
    Button btnModificar, btnEliminar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_modificarservicio, container, false);
        Bundle datosRecuperados = getArguments();
        String idrecuperado = datosRecuperados.getString("identificador");
        fecham=view.findViewById(R.id.dpfecham);
        horam=view.findViewById(R.id.tphoram);
        txtOrigenm=view.findViewById(R.id.txtOrigenm);
        txtDestinom=view.findViewById(R.id.txtDestinom);
        txtcomentariosm=view.findViewById(R.id.txtComentariosm);
        btnModificar=view.findViewById(R.id.btnmodificar);
        btnEliminar=view.findViewById(R.id.btnEliminar);
        cargardatos("http://pruebataxi.laviveshop.com/app/consultarservicioabierto.php",idrecuperado);
        return view;
    }
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
                        txtOrigenm.setText(origenobt);
                        txtDestinom.setText(destinoobt);
                        txtcomentariosm.setText(comentariosobt);
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
}
