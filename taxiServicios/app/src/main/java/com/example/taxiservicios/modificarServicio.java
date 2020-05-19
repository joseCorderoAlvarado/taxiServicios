package com.example.taxiservicios;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
    String fechac,horac,origen,destino,comentarios2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_modificarservicio, container, false);
        Bundle datosRecuperados = getArguments();
        final String idrecuperado = datosRecuperados.getString("identificador");
        fecham=view.findViewById(R.id.dpfecham);
        horam=view.findViewById(R.id.tphoram);
        txtOrigenm=view.findViewById(R.id.txtOrigenm);
        txtDestinom=view.findViewById(R.id.txtDestinom);
        txtcomentariosm=view.findViewById(R.id.txtComentariosm);
        btnModificar=view.findViewById(R.id.btnmodificar);
        btnEliminar=view.findViewById(R.id.btnEliminar);
        cargardatos("http://pruebataxi.laviveshop.com/app/consultarservicioabierto.php",idrecuperado);
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Modificar Servicio");
                alertDialogBuilder
                        .setMessage("¿Estás seguro de modificar este servicio?")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                fechac = "" + fecham.getYear() + "-" + fecham.getMonth() + "-" + fecham.getDayOfMonth() + "";
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    horac = "" + horam.getHour() + ":" + horam.getMinute();
                                }
                                origen = txtOrigenm.getText().toString();
                                destino = txtDestinom.getText().toString();
                                comentarios2 = txtcomentariosm.getText().toString();
                                if(origen.isEmpty() && !destino.isEmpty())
                                {
                                    Toast.makeText(getActivity().getBaseContext(),"se debe ingresar la direccion de origen",Toast.LENGTH_SHORT).show();
                                }
                                else if(destino.isEmpty())
                                {
                                    Toast.makeText(getActivity().getBaseContext(),"se debe ingresar la direccion de tu direccion",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    modificar("http://pruebataxi.laviveshop.com/app/actualizarservicio.php", origen, destino, fechac, horac, comentarios2, idrecuperado);
                                    homeCliente home = new homeCliente();
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, home).addToBackStack(null).commit();
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
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Mi Dialogo");
                alertDialogBuilder
                        .setMessage("¿Estás seguro de Eliminar este servicio?")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                eliminarservicio("http://pruebataxi.laviveshop.com/app/eliminarservicio.php",idrecuperado);
                                homeCliente home = new homeCliente();
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, home).addToBackStack(null).commit();
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
    private  void modificar(String URL, final String d1, final String d2,
                                    final String fecha, final String hora, final String comentario, final String identificador)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"Servicio modificado con exito!!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),"Error al modificar el servicio",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("fecha",fecha.toString());
                parametros.put("hora",hora.toString());
                parametros.put("d1",d1);
                parametros.put("d2",d2);
                parametros.put("referencia",comentario);
                parametros.put("id",identificador);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
    private  void eliminarservicio(String URL, final String identificador)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"Servicio eliminado con exito!!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),"Error al eliminar el servicio",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("id",identificador);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
}
