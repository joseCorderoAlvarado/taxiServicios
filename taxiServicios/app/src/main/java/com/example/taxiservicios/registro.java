package com.example.taxiservicios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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

import java.util.HashMap;
import java.util.Map;

public class registro extends AppCompatActivity {
 EditText txtNombre,txtTelefono,txtcorreo,txtContrasena;
 Button btnregistro;
 TextView iniciarsesion;
    String correo,contrasena,nombre,telefono,correov;
   // String URL_RegistroUsuario="http://192.168.1.105/Taxis-Pruebas/registro.php";
    //String URL_ValidarRegistro="http://192.168.1.105/Taxis-Pruebas/validarRegistro.php";


    String URL_RegistroUsuario="http://pruebataxi.laviveshop.com/app/registro.php";
    String URL_ValidarRegistro="http://pruebataxi.laviveshop.com/app/validarRegistro.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        txtNombre=findViewById(R.id.txtnombre);
        txtTelefono=findViewById(R.id.txtTelefono);
        txtcorreo=findViewById(R.id.txtcorreo);
        txtContrasena=findViewById(R.id.txtcontrasena);
        btnregistro=findViewById(R.id.btnregistro);
        iniciarsesion=findViewById(R.id.viewiniciarsesion);
        recuperarpreferencias();
        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo=txtcorreo.getText().toString();
                if (Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    correov = correo;
                } else {
                    Toast.makeText(registro.this,"Este no es un correo eléctronico \n ejemplo@gmail.com, Cuida que no tenga espacios en blancos",Toast.LENGTH_SHORT).show();
                    return;
                }
                contrasena=txtContrasena.getText().toString();
             nombre=txtNombre.getText().toString();
             telefono=txtTelefono.getText().toString();
             if(!correo.isEmpty() && !contrasena.isEmpty() && !nombre.isEmpty() && !telefono.isEmpty()){
                 validarRegistro(URL_ValidarRegistro);
             }
             else
                 {
                     Toast.makeText(registro.this,"No se permiten campos vacios",Toast.LENGTH_SHORT).show();
                 }
            }
        });
        iniciarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void  validarRegistro(String URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("valor", response);
             if(response.isEmpty())
             {
                 Toast.makeText(registro.this,"Este usuario ya se encuentra registrado",Toast.LENGTH_SHORT).show();
             }
             else
                 {
                     registrarUsuario(URL_RegistroUsuario);
                 }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(registro.this,error.toString(),Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void registrarUsuario(String URL)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                guardarpreferencias();
                Intent intent =new Intent(getApplicationContext(),inicioCliente.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error al registrarse",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("nombre",nombre);
                parametros.put("telefono",telefono);
                parametros.put("correo",correov);
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
        editor.putString("correo",correov);
        editor.putString("contrasena",contrasena);
        editor.putInt("tipo",2);
        editor.putBoolean("sesion",true);
        editor.commit();
    }
    private void recuperarpreferencias()
    {
        SharedPreferences preferences= getSharedPreferences("preferenciasLogin",Context.MODE_PRIVATE);
        txtcorreo.setText(preferences.getString("correo",""));
        txtContrasena.setText(preferences.getString("contrasena",""));
    }
}
