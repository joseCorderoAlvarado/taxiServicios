package com.example.taxiservicios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class presentacion extends AppCompatActivity {

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                boolean sesion=preferences.getBoolean("sesion",false);
                int tipo=preferences.getInt("tipo",3);

                if(sesion)
                {
                    if(tipo==2)
                    {
                        Intent intent =new Intent(getApplicationContext(),inicioCliente.class);
                        startActivity(intent);
                        finish();
                    }

                    else if(tipo==1)
                    {
                        Intent intent =new Intent(getApplicationContext(),inicioAdministrador.class);
                        startActivity(intent);
                        finish();
                    }

                    else if(tipo==3)
                        {
                            Intent intent =new Intent(getApplicationContext(),inicioChofer.class);
                            startActivity(intent);
                            finish();
                        }
                }else
                    {
                        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
            }
        },2000);
    }





}
