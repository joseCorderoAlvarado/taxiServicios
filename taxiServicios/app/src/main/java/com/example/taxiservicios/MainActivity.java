package com.example.taxiservicios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
   EditText txtcorreo,txtcontrasena;
   Button btningresar;
   String correo,contrasena;
   TextView registro;
   String tokenFinal;
   //String URL_validarUsuario="http://192.168.1.105/Taxis-Pruebas/validar_usuario.php";

   //URLs de los servidores
 String URL_validarUsuario="http://pruebataxi.laviveshop.com/app/validar_usuario.php";

    final String URL_registrar_token="http://pruebataxi.laviveshop.com/app/registrar_token.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtcorreo=findViewById(R.id.txtcorreo);
        txtcontrasena=findViewById(R.id.txtcontrasena);
        btningresar=findViewById(R.id.btnregistro);
        registro=findViewById(R.id.viewiniciarsesion);
        recuperarpreferencias();


        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo=txtcorreo.getText().toString();
                contrasena=txtcontrasena.getText().toString();
                if(!correo.isEmpty() && !contrasena.isEmpty()){
                    validarUsuario(URL_validarUsuario);
                }
                else
                    {
                        Toast.makeText(MainActivity.this,"No se permiten campos vacios",Toast.LENGTH_SHORT).show();
                    }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                        //    Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toas
                       // Log.d(TAG, msg);
                       // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        tokenFinal=token.toString();
                    }
                });
        System.out.println("Device token: " +  FirebaseInstanceId.getInstance().getInstanceId());


        //El boton para registrar
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),registro.class);
                startActivity(intent);
            }
        });
    }





    //El meotodo del Login
  private  void validarUsuario(String URL)
  {
      StringRequest stringRequest =new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
           if(!response.isEmpty())
           {
               try {
                   JSONObject valores =new JSONObject(response);
                   int valorLlave = valores.getInt("tipoUsuario_idtipoUsuario");

                   //Condeicion para los usuarios tipos
                   if(valorLlave==2)
                   {
                       guardarpreferencias();
                       Intent intent =new Intent(getApplicationContext(),inicioCliente.class);
                       startActivity(intent);
                       finish();
                   }
                   else if(valorLlave==1)
                       {


                           registrarToken(URL_registrar_token,tokenFinal,correo);
                           guardarpreferencias2();
                           Intent intent =new Intent(getApplicationContext(),inicioAdministrador.class);
                           startActivity(intent);
                           finish();
                       }
                   else if(valorLlave==3)
                   {
                       guardarpreferencias3();
                       Intent intent =new Intent(getApplicationContext(),inicioChofer.class);
                       startActivity(intent);
                       finish();
                   }


               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }
           else
               {
                   Toast.makeText(MainActivity.this,"Usuario o contraseña incorrectas",Toast.LENGTH_SHORT).show();
               }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
          }
      }){
          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
              Map<String,String> parametros = new HashMap<String, String>();
              parametros.put("correo",correo);
              parametros.put("contrasena",contrasena);

              return parametros;
          }
      };
      RequestQueue requestQueue= Volley.newRequestQueue(this);
      requestQueue.add(stringRequest);
  }



    private void registrarToken(String URL, final String tokenDevice, final String correo){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("tokenDispositivo",tokenDevice);
                parametros.put("correo",correo);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


  private  void guardarpreferencias()
  {
      SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
      SharedPreferences.Editor editor =preferences.edit();
      editor.putString("correo",correo);
      editor.putString("contrasena",contrasena);
      editor.putInt("tipo",2);
      editor.putBoolean("sesion",true);
      editor.commit();
  }
    private  void guardarpreferencias2()
    {
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =preferences.edit();
        editor.putString("correo",correo);
        editor.putString("contrasena",contrasena);
        editor.putInt("tipo",1);
        editor.putBoolean("sesion",true);
        editor.commit();
    }

    //Cuando el tipo de usuario es 3
    private  void guardarpreferencias3()
    {
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =preferences.edit();
        editor.putString("correo",correo);
        editor.putString("contrasena",contrasena);
        editor.putInt("tipo",3);
        editor.putBoolean("sesion",true);
        editor.commit();

    }


  private void recuperarpreferencias()
  {
      SharedPreferences preferences= getSharedPreferences("preferenciasLogin",Context.MODE_PRIVATE);
      txtcorreo.setText(preferences.getString("correo",""));
      txtcontrasena.setText(preferences.getString("contrasena",""));
  }





}
