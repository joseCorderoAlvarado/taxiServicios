package com.example.taxiservicios;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class codigoinvitacion extends Fragment {
    TextView txtcodigo;
    Button btncompartir;
    String correo;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_codigoinvitacion, container, false);
        txtcodigo=view.findViewById(R.id.codigo);
        btncompartir=view.findViewById(R.id.btncompatir);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo=preferences.getString("correo",null);
        cargardatos("http://pruebataxi.laviveshop.com/app/consultarci.php",correo);
        btncompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Usa mi codigo de invitación\n"+txtcodigo.getText().toString()+"\n y comienza a usar taxiServicios\n descargala aqui:https://play.google.com/store/apps/details?id=app.proyecto.taxiservicios");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
        return view;
    }
public  void  cargardatos(String URL, final String correov)
{
    StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject valores = new JSONObject(response);
                JSONArray jsonArray=valores.getJSONArray("datos");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String ciobt =jsonObject.getString("ci");
                    txtcodigo.setText(ciobt);
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
