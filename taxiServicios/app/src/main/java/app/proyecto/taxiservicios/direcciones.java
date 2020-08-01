package app.proyecto.taxiservicios;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.taxiservicios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class direcciones extends Fragment {
    Spinner sOrigen;
    String correo;
    List<String> direccion1obt =  new ArrayList<String>();
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.direcciones,container,false);
        sOrigen=view.findViewById(R.id.spinner1);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo=preferences.getString("correo",null);
        cargardireccion1("http://pruebataxi.laviveshop.com/app/consultardireccion1.php",correo);
        return  view;
    }
    private void cargardireccion1(String URL, final String Correv)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
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

}
