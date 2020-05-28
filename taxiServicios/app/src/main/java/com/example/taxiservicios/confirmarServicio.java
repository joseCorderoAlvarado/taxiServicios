package com.example.taxiservicios;

import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;

public class confirmarServicio extends Fragment {
    Spinner spNumeroTaxi;
    String valors1,desvehiculo;
    EditText txtDescripcionVehiculo;
    TextView tvinformacion;
    Button btnconfirmarservicio;
    List<String> listadotaxisobt =  new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_confirmarservicio, container, false);
        Bundle datosRecuperados = getArguments();
        final String idrecuperado = datosRecuperados.getString("identificador");
        spNumeroTaxi=view.findViewById(R.id.spNumeroTaxi);
        txtDescripcionVehiculo=view.findViewById(R.id.txtDescripcionVehiculo);
        tvinformacion=view.findViewById(R.id.tvinformacion);
        btnconfirmarservicio=view.findViewById(R.id.btnconfirmarservicio);
        cargarlistaTaxis("http://pruebataxi.laviveshop.com/app/consultalistataxis.php");
        cargarconfirmado("http://pruebataxi.laviveshop.com/app/consultarConfirmadoA.php",idrecuperado);
        btnconfirmarservicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Confirmar servicio");
                alertDialogBuilder
                        .setMessage("¿Estas seguro de asignar este taxi al servicio?")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                valors1 = spNumeroTaxi.getSelectedItem().toString();
                desvehiculo = txtDescripcionVehiculo.getText().toString();
                if(desvehiculo.isEmpty())
                {
                    confirmar("http://pruebataxi.laviveshop.com/app/actualizarestadoconfirmado.php", idrecuperado, valors1, "");
                    homeAdministrador confirmarservicio = new homeAdministrador();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, confirmarservicio).addToBackStack(null).commit();
                }
                else {
                    confirmar("http://pruebataxi.laviveshop.com/app/actualizarestadoconfirmado.php", idrecuperado, valors1, desvehiculo);
                    homeAdministrador confirmarservicio = new homeAdministrador();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, confirmarservicio).addToBackStack(null).commit();
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
    private void cargarlistaTaxis(String URL)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject valores = new JSONObject(response);
                    JSONArray jsonArray=valores.getJSONArray("taxis");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String taxides=jsonObject.getString("destaxi");
                        listadotaxisobt.add(taxides);
                    }
                    spNumeroTaxi.setAdapter(new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_dropdown_item,listadotaxisobt));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
    private  void cargarconfirmado(String URL, final String idrecuperadox)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject valores = new JSONObject(response);
                    JSONArray jsonArray=valores.getJSONArray("confirmado");
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String fecha = jsonObject.getString("fecha");
                        String hora = jsonObject.getString("hora");
                        String nombre=jsonObject.getString("nombre");
                        String telefono=jsonObject.getString("telefono");
                        String origen = jsonObject.getString("origen");
                        String destino = jsonObject.getString("destino");
                        String referencia=jsonObject.getString("referencia");
                        String costo=jsonObject.getString("costo");
                        Log.d("costo",costo);
                        Log.d("destino",destino);
                            if (referencia.equals(""))
                            {
                                if (costo.equals(""))
                                {
                                    tvinformacion.setText("      Datos del Servicio  \nFecha: " + fecha +
                                            "\n\nHora: " + hora +"\n\nOrigen:\n"+origen+
                                            "\n\nDestino:\n" + destino+
                                            "\n\nEl cliente es:\n"+nombre+
                                            "\n\nTelefono:\n"
                                            +telefono);
                                }else {
                                    tvinformacion.setText("      Datos del Servicio  \nFecha: " + fecha +
                                            "\n\nHora: " + hora +"\n\nServicio:"+costo +"\n\nOrigen:\n" + origen +
                                            "\n\nDestino:\n" + destino +
                                            "\n\nEl cliente es:\n" + nombre +
                                            "\n\nTelefono:\n"
                                            + telefono);
                                }
                            }
                            else
                                {
                                    if (costo.equals("")) {
                                        tvinformacion.setText("     Datos del Servicio  \nfecha del servicio: " + fecha +
                                                "\n\nHora: " + hora + "\n\nOrigen:\n" + origen +
                                                "\n\nDestino:\n" + destino +
                                                "\n\nEl cliente:\n" + nombre +
                                                "\n\nTelefono:\n"
                                                + telefono + "\n\nComentario:\n" + referencia);
                                    }
                                    else
                                        {
                                            tvinformacion.setText("     Datos del Servicio  \nfecha del servicio: " + fecha +
                                                    "\n\nHora: " + hora + "\n\nOrigen:\n" + origen +
                                                    "\n\nDestino:\n" + destino +
                                                    "\n\nEl cliente:\n" + nombre +
                                                    "\n\nTelefono:\n"
                                                    + telefono + "\n\nComentario:\n" + referencia+"\n\nServicio:"+costo);
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
                parametros.put("idservicio",idrecuperadox);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
    private  void confirmar(String URL, final String identificador , final String sptaxi, final String  descripcionvehiculo)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"Servicio Confirmado con exito!!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),"Error al actualizar el servicio",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("idservicio",identificador.toString());
                parametros.put("taxi",sptaxi.toString());
                parametros.put("descripcionvehiculo",descripcionvehiculo);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }

}
