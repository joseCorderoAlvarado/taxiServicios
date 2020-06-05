package com.example.taxiservicios;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Calendar;

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
                setAlarm();
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



    public void setAlarm() {
        //Una alarma
        Calendar calendar = Calendar.getInstance();



        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 4);
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 4);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        System.out.println("El calendario: " + calendar.toString());
        Calendar cur = Calendar.getInstance();
        //long sss=1591324200000l;
        //calendar.setTimeInMillis(sss);
        //System.out.println("sss2: " + calendar.getTime());


        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(getBaseContext(), servicioAlarmas.class);
        int ALARM1_ID = 10000;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
      //  System.out.println("En milisegundos: " + calendar.getTimeInMillis());


         calendar = Calendar.getInstance();



        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 4);
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        System.out.println("El calendario: " + calendar.toString());
         cur = Calendar.getInstance();
        //long sss=1591324200000l;
        //calendar.setTimeInMillis(sss);
        //System.out.println("sss2: " + calendar.getTime());


        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        myIntent = new Intent(getBaseContext(), servicioAlarmas.class);
        ALARM1_ID = 20000;
         pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        //  System.out.println("En milisegundos: " + calendar.getTimeInMillis());



    }


}
