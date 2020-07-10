package app.proyecto.taxiservicios;

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
import com.example.taxiservicios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class verBuzon extends Fragment {
    // TODO: Rename and change types of parameters
    RecyclerView recyclerPersonajes; //ok
    RecyclerView.Adapter adapter;
    TextView txtTitulo;
    ArrayList<modeloBuzon> listaPersonaje;
    String correo;


    String URL_consultarServiciosAsignados="http://pruebataxi.laviveshop.com/app/consultarBuzon.php";


    // TODO: Rename and change types and number of parameters
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buzon_admin, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);

        txtTitulo = (TextView)view.findViewById(R.id.titulo);
        txtTitulo.setText("Buzon de sugerencias");
        correo=preferences.getString("correo",null);
        recyclerPersonajes= (RecyclerView) view.findViewById(R.id.datosBuzon);
        recyclerPersonajes.setHasFixedSize(true);
        recyclerPersonajes.setLayoutManager(new LinearLayoutManager(getContext()));
        listaPersonaje= new ArrayList<>();
        llenarLista(URL_consultarServiciosAsignados);
        return view;
    }
    private void llenarLista(String URL) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty())
                {
                    try {
                        JSONObject servicios = new JSONObject(response);
                        JSONArray jsonArray=servicios.getJSONArray("datos");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            modeloBuzon modelo =new modeloBuzon(
                                    "Correo: " + jsonObject.getString("correo")  + "\n",
                                    "Nombre:"+jsonObject.getString("nombre")+"\n",
                                    "Mensaje: "+ jsonObject.getString("mensaje") + "\n",
                                    "Fecha: "+ jsonObject.getString("fecha") + "\n"
                            );
                            listaPersonaje.add(modelo);

                        }
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        recyclerPersonajes.setLayoutManager(manager);
                        recyclerPersonajes.setHasFixedSize(true);
                        AdaptadorBuzon adapter=new AdaptadorBuzon(listaPersonaje,getContext());
                        recyclerPersonajes.setAdapter(adapter);

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

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
 }
