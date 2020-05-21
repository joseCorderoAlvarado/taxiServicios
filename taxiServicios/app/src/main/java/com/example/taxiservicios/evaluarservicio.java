package com.example.taxiservicios;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class evaluarservicio extends Fragment {
    RatingBar estrellas;
    EditText nota2;
    Button evaluar;
    String calificacion,comentario;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.fragment_evaluarservicio,container,false);
        Bundle datosRecuperados = getArguments();
        final String idrecuperado = datosRecuperados.getString("identificador");
        estrellas=view.findViewById(R.id.rbestrellas);
        nota2=view.findViewById(R.id.txtmejora);
        evaluar=view.findViewById(R.id.btnEvaluarServicio);
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
                                evaluarservicio("http://pruebataxi.laviveshop.com/app/evaluarservicio.php",idrecuperado,calificacion,comentario);
                                homeCliente home = new homeCliente();
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, home).addToBackStack(null).commit();
            }
        });
        return view;
    }

    private  void evaluarservicio(String URL, final String identificador, final String calificacion, final String nota)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"Servicio evaluado con exito!!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),"Error al evaluar el servicio",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("id",identificador);
                parametros.put("estrellas",calificacion);
                parametros.put("nota",nota);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
}
