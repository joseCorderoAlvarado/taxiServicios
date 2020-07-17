package app.proyecto.taxiservicios;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.taxiservicios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
public class datosCliente extends Fragment {
    EditText txtNombre,txtTelefono,txtContraseña;
    Button btnmodificar;
    String correo, nombre,telefon,contrasena;
    String URL_datosCliente="http://pruebataxi.laviveshop.com/app/consultardatoscliente.php";
    //String URL_datosCliente="http://192.168.1.105/Taxis-Pruebas/consultardatoscliente.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.fragment_datoscliente,container,false);
        txtNombre=view.findViewById(R.id.txtNombreC);
        txtContraseña=view.findViewById(R.id.txtContrasenaC);
        txtTelefono=view.findViewById(R.id.txtTelefonoC);
        btnmodificar=view.findViewById(R.id.btnModificarC);
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        correo=preferences.getString("correo",null);
        cargardatos(URL_datosCliente,correo);
        btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
// Configura el titulo.
                alertDialogBuilder.setTitle("Modificar Datos");
// Configura el mensaje.
                alertDialogBuilder
                        .setMessage("¿Estas seguro de modificar tus datos?")
                        .setCancelable(false)
                        .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                telefon=txtTelefono.getText().toString();
                                contrasena=txtContraseña.getText().toString();
                                nombre=txtNombre.getText().toString();
                                modificardatos("http://pruebataxi.laviveshop.com/app/actualizardatoscliente.php",nombre,telefon,contrasena,correo);
                                homeCliente home = new homeCliente();
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, home).addToBackStack(null).commit();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        });
        return  view;
    }
    private  void cargardatos(String URL, final String correoobt)
    {
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
                        String telefono =jsonObject.getString("telefono");
                        txtTelefono.setText(telefono);
                        txtNombre.setText(nombre);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",correoobt);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
    private  void  modificardatos(String URL, final String nombreobt, final String telefonoobt, final String contrasenaobt, final String correoobt)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getBaseContext(),"Datos actualizados con exito!!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(),"Error al modificar los datos",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("nombre",nombreobt.toString());
                parametros.put("telefono",telefonoobt.toString());
                parametros.put("contrasena",contrasenaobt.toString());
                parametros.put("correo",correoobt.toString());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getBaseContext());
        requestQueue.add(stringRequest);
    }
}