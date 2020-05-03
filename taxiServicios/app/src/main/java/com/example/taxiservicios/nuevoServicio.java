package com.example.taxiservicios;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class nuevoServicio extends Fragment {
DatePicker fecha;
TimePicker hora;
Spinner sOrigen, sDestino;
EditText txtOrigen, txtDestino,txtcomentarios;
Button btnNuevo;
String correo,valors1,valors2,fechac,horac,origen,destino,comentarios2;
List<String> direccion1obt =  new ArrayList<String>();
List<String> direccion2obt =  new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_nuevoservicio,container,false);
        fecha=view.findViewById(R.id.dpfecha);
        hora=view.findViewById(R.id.tphora);
        sOrigen=view.findViewById(R.id.spinnerpartida);
        sDestino=view.findViewById(R.id.spinnerdestino);
        txtOrigen=view.findViewById(R.id.txtOrigen);
        txtDestino=view.findViewById(R.id.txtDestino);
        txtcomentarios=view.findViewById(R.id.txtComentarios);
        btnNuevo=view.findViewById(R.id.btnnuevo);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo=preferences.getString("correo",null);
        cargardireccion1("http://pruebataxi.laviveshop.com/app/consultardireccion1.php",correo);
        cargardireccion2("http://pruebataxi.laviveshop.com/app/consultardireccion2.php",correo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               valors1 =sOrigen.getSelectedItem().toString();
               valors2=sDestino.getSelectedItem().toString();
               fechac=""+fecha.getYear()+"-"+fecha.getMonth()+"-"+fecha.getDayOfMonth()+"";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    horac=""+hora.getHour()+":"+hora.getMinute();
                }
            origen=txtOrigen.getText().toString();
            destino=txtDestino.getText().toString();
            comentarios2=txtcomentarios.getText().toString();
            if(origen.isEmpty() && destino.isEmpty())
            {
                spnuevoservicio("http://pruebataxi.laviveshop.com/app/spregistrarservicio.php",valors1,valors2,fechac,horac,comentarios2,correo);
            }
            else if(origen.isEmpty() && !destino.isEmpty()){
                Toast.makeText(getActivity().getBaseContext(),"necesitas ingresar la direccion de destino",Toast.LENGTH_SHORT).show();
            }
            else if(destino.isEmpty() && !origen.isEmpty())
            {
                Toast.makeText(getActivity().getBaseContext(),"necesitas ingresar la direccion donde te recogeran",Toast.LENGTH_SHORT).show();
            }
            else
                {
                  nuevoservicio(  "http://pruebataxi.laviveshop.com/app/agendarservicio.php",valors1,valors2,origen,destino,fechac,horac,comentarios2,correo);
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
