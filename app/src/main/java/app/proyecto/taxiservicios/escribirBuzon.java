package app.proyecto.taxiservicios;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.taxiservicios.R;

import java.util.HashMap;
import java.util.Map;

public class escribirBuzon extends Fragment {

    EditText notaBuzon;
    Button mandar;
    String correo;
    String calificacion,comentario, comentarioCarro;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.fragment_buzon,container,false);
        Bundle datosRecuperados = getArguments();
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo=preferences.getString("correo",null);




        notaBuzon=view.findViewById(R.id.txtMensaje);


        mandar=view.findViewById(R.id.btnBuzon);






        mandar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                comentario=notaBuzon.getText().toString();




                mandarMensaje("http://pruebataxi.laviveshop.com/app/enviarBuzon.php",correo,
                                       comentario);
                                escribirBuzon home = new escribirBuzon();
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,home).addToBackStack(null).commit();
            }
        });
        return view;
    }

    private  void mandarMensaje(String URL, final String correo,
                                   final String nota
                                  )
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"Mensaje enviado con exito!!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),"Error al enviar mensaje",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",correo);
                parametros.put("mensaje",nota);

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
}
