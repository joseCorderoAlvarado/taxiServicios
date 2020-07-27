package app.proyecto.taxiservicios;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.taxiservicios.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class inicioChofer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String NombreMenu, correo;
    String URL_CargarDatos="http://pruebataxi.laviveshop.com/app/consultar_datos_chofer.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        setContentView(R.layout.activity_inicio_chofer);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer3);









        navigationView =findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //Para cambiar el mensaje en el texto del header del navigationView al nombre del Chofer
        SharedPreferences preferences = this.getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo=preferences.getString("correo",null);

        cargarNombreChoferMenu(URL_CargarDatos,correo,this );

        //Fin


        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new homeChofer());
        fragmentTransaction.commit();

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId()==R.id.Inicio)
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new homeChofer());

            fragmentTransaction.commit();
        }
        if(menuItem.getItemId()==R.id.Pendientes)
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new serviciosPendientesChofer());
            fragmentTransaction.commit();
        }

        if(menuItem.getItemId()==R.id.Datos)
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new datosChofer());
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId()==R.id.buzon)
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new escribirBuzon());
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId()==R.id.avisoprivacidad)
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction= fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new avisoprivacidad());
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId()==R.id.Cerrar)
        {
            SharedPreferences preferences= getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
            final String URL_registrar_token="http://pruebataxi.laviveshop.com/app/eliminar_token.php";
            final String correo=preferences.getString("correo",null);
            //   System.out.println("El correo es: " + correo);
            eliminar(URL_registrar_token,correo);

            preferences.edit().clear().commit();
            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }

        return true;
    }


    private void eliminar(String URL, final String correo){
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
                parametros.put("correo",correo);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private  void cargarNombreChoferMenu(String URL, final String correoobt, final Context context)
    {
          //System.out.println("El correo es: " + correoobt);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject valores = new JSONObject(response);
                    JSONArray jsonArray=valores.getJSONArray("datos");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nombre =jsonObject.getString("nombre");

                        //La magia
                        View header = navigationView.getHeaderView(0);
                        TextView textoMenu = (TextView) header.findViewById(R.id.textLeyenda);
                        textoMenu.setText("Chofer: " + nombre);
                        //Fin




                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",correoobt);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }




}
