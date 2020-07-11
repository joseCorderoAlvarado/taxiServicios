package app.proyecto.taxiservicios;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class evaluarCliente extends Fragment {
    RatingBar estrellas;
    EditText nota2, notaCarro;
    Button evaluar;
    String calificacion,comentario, comentarioCarro;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.fragment_evaluarcliente,container,false);
        Bundle datosRecuperados = getArguments();
        final String idrecuperado = datosRecuperados.getString("identificador");
        estrellas=view.findViewById(R.id.rbestrellas);


        nota2=view.findViewById(R.id.txtmejora);


        evaluar=view.findViewById(R.id.btnEvaluar);



        LayerDrawable stars = (LayerDrawable)  estrellas.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(229,190,1), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        estrellas.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                calificacion=""+Math.round(rating)+"";
            }
        });




        evaluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                comentario=nota2.getText().toString();




                evaluarCliente("http://pruebataxi.laviveshop.com/app/evaluarcliente.php",idrecuperado,
                                        calificacion,comentario);
                                homeChofer home = new homeChofer();
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,home).addToBackStack(null).commit();
            }
        });
        return view;
    }

    private  void evaluarCliente(String URL, final String identificador,
                                  final String calificacion, final String nota
                                  )
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"Cliente evaluado con exito!!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),"Error al evaluar el cliente",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("id",identificador);
                parametros.put("estrellasCliente",calificacion);
                parametros.put("notaCliente",nota);

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
}
