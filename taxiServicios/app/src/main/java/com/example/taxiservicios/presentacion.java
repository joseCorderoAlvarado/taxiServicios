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
                final String URL_alarmasDia="http://pruebataxi.laviveshop.com/app/consultarServiciosDelDia.php";
                final String correo=preferences.getString("correo",null);

                //Lo dejo por si se ocupara a futuro,pero no lo mando a llamar
                //llenarListaAlarmas(URL_alarmasDia,correo,getBaseContext());

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




    private void llenarListaAlarmas(String URL, final String correov, final Context contex) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    try {
                        JSONObject servicios = new JSONObject(response);
                        JSONArray jsonArray = servicios.getJSONArray("servicios");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //Obtenemos los valores
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String fechaServicio = jsonObject.getString("fecha");
                            String horaServicio = jsonObject.getString("hora");


                            String[] separar = fechaServicio.split("-");
                            String year = separar[0]; // el año
                            String month = separar[1]; // el mes
                            String day = separar[2]; // el dia

                            String[] separar2 = horaServicio.split(":");
                            String hour = separar2[0]; // la hora
                            String minute = separar2[1]; // el minuto


                            Calendar calendar = Calendar.getInstance();


                            calendar.set(Calendar.YEAR, Integer.parseInt(year));
                            calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);

                            System.out.println("El calendario: " + calendar.toString());
                            Calendar cur = Calendar.getInstance();
                            //long sss=1591324200000l;



                            if (cur.after(calendar)) {
                                calendar.add(Calendar.DATE, 1);
                            }

                            int anticipacionNotificacion = (20 * 60 * 1000);//Minutos*segundos*milisegundos

                            System.out.println("sss2: " +  calendar.getTimeInMillis());
                            calendar.setTimeInMillis(calendar.getTimeInMillis());
                            System.out.println("sss5: " + calendar.getTime());

                            Intent myIntent = new Intent(getBaseContext(), servicioAlarmas.class);
                            int ALARM1_ID = i+1;
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                    getBaseContext(), ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-anticipacionNotificacion , AlarmManager.INTERVAL_DAY, pendingIntent);
                              System.out.println("Alarma: " + ALARM1_ID);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contex,error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",correov);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(contex);
        requestQueue.add(stringRequest);
    }

}
