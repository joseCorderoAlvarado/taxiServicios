package com.example.taxiservicios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
   EditText txtcorreo,txtcontrasena;
   Button btningresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtcorreo=findViewById(R.id.txtcorreo);
        txtcontrasena=findViewById(R.id.txtcontrasena);
        btningresar=findViewById(R.id.button2);

        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarUsuario("http://pruebataxi.laviveshop.com/app/validar_usuario.php");
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
                   Log.d("Valor", String.valueOf(valorLlave));
                   if(valorLlave==2)
                   {
                       Intent intent =new Intent(getApplicationContext(),inicioCliente.class);
                       startActivity(intent);
                   }
                   else
                       {
                           Intent intent =new Intent(getApplicationContext(),inicioAdministrador.class);
                           startActivity(intent);
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
              parametros.put("correo",txtcorreo.getText().toString());
              parametros.put("contrasena",txtcontrasena.getText().toString());

              return parametros;
          }
      };
      RequestQueue requestQueue= Volley.newRequestQueue(this);
      requestQueue.add(stringRequest);
  }
}
