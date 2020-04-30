package com.example.taxiservicios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
   EditText txtcorreo,txtcontrasena;
   Button btningresar;
   String correo,contrasena;
   TextView registro;
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
                    validarUsuario("http://pruebataxi.laviveshop.com/app/validar_usuario.php");
                }
                else
                    {
                        Toast.makeText(MainActivity.this,"No se permiten campos vacios",Toast.LENGTH_SHORT).show();
                    }
            }
        });
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),registro.class);
                startActivity(intent);
            }
        });
    }
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
                   if(valorLlave==2)
                   {
                       guardarpreferencias();
                       Intent intent =new Intent(getApplicationContext(),inicioCliente.class);
                       startActivity(intent);
                       finish();
                   }
                   else
                       {
                           guardarpreferencias2();
                           Intent intent =new Intent(getApplicationContext(),inicioAdministrador.class);
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
  private void recuperarpreferencias()
  {
      SharedPreferences preferences= getSharedPreferences("preferenciasLogin",Context.MODE_PRIVATE);
      txtcorreo.setText(preferences.getString("correo",""));
      txtcontrasena.setText(preferences.getString("contrasena",""));
  }
}
