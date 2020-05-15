package com.example.taxiservicios;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
        final View view =inflater.inflate(R.layout.fragment_nuevoservicio,container,false);
        fecha=view.findViewById(R.id.dpfecha);
        hora=view.findViewById(R.id.tphora);
        sOrigen=view.findViewById(R.id.spinnerpartida);
        sDestino=view.findViewById(R.id.spinnerdestino);
        txtOrigen=view.findViewById(R.id.txtOrigen);
        txtDestino=view.findViewById(R.id.txtDestino);
        txtcomentarios=view.findViewById(R.id.txtComentarios);
        btnNuevo=view.findViewById(R.id.btnmodificar);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo=preferences.getString("correo",null);
       cargardireccion1("http://pruebataxi.laviveshop.com/app/consultardireccion1.php",correo);
        cargardireccion2("http://pruebataxi.laviveshop.com/app/consultardireccion2.php",correo);

        //cargardireccion1("http://192.168.1.105/Taxis-Pruebas/consultardireccion1.php",correo);
        //cargardireccion2("http://192.168.1.105/Taxis-Pruebas/consultardireccion2.php",correo);

         final String URL_spnuevoservicio="http://pruebataxi.laviveshop.com/app/spregistrarservicio.php";
        //final String URL_spnuevoservicio="http://192.168.1.105/Taxis-Pruebas/spregistrarservicio.php";

        final String URL_nuevoservicio="http://pruebataxi.laviveshop.com/app/agendarservicio.php";
        //final String URL_nuevoservicio="http://192.168.1.105/Taxis-Pruebas/agendarservicio.php";



        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
// Configura el titulo.
                alertDialogBuilder.setTitle("Mi Dialogo");
// Configura el mensaje.
                alertDialogBuilder
                        .setMessage("¿Estás seguro de agendar este servicio?")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                valors1 = sOrigen.getSelectedItem().toString();
                                valors2 = sDestino.getSelectedItem().toString();
                                fechac = "" + fecha.getYear() + "-" + fecha.getMonth() + "-" + fecha.getDayOfMonth() + "";
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    horac = "" + hora.getHour() + ":" + hora.getMinute();
                                }
                                else{
                                    horac = " ";
                                }
                                origen = txtOrigen.getText().toString();
                                destino = txtDestino.getText().toString();
                                comentarios2 = txtcomentarios.getText().toString();
                                if (origen.isEmpty() && destino.isEmpty()) {
                                    spnuevoservicio(URL_spnuevoservicio, valors1, valors2, fechac, horac, comentarios2, correo);
                                    homeCliente modificarservicio = new homeCliente();
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,modificarservicio).addToBackStack(null).commit();
                                }
                                else if (origen.isEmpty() && !destino.isEmpty()) {
                                    Toast.makeText(getActivity().getBaseContext(), "necesitas ingresar la direccion de destino", Toast.LENGTH_SHORT).show();
                                } else if (destino.isEmpty() && !origen.isEmpty()) {
                                    Toast.makeText(getActivity().getBaseContext(), "necesitas ingresar la direccion donde te recogeran", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    nuevoservicio(URL_nuevoservicio, valors1, valors2, origen, destino, fechac, horac, comentarios2, correo);
                                    homeCliente modificarservicio = new homeCliente();
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,modificarservicio).addToBackStack(null).commit();
                                }
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        }).create().show();
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
                direccion1obt.add("seleccione una direccion");
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
                    direccion2obt.add("seleccione una direccion");
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


    //sss
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
