package app.proyecto.taxiservicios;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.taxiservicios.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorCliente extends RecyclerView.Adapter<AdaptadorCliente.ViewHolder> implements  View.OnClickListener{
    private List<modeloCliente> userModelList;
    private View.OnClickListener listener;
    private Context context;

    public AdaptadorCliente(List<modeloCliente> userModelList,Context context ) {
        this.userModelList = userModelList;
        this.context = context;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listacliente,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtfechahora.setText(userModelList.get(position).getFechahora());
        holder.txtdireccion.setText(userModelList.get(position).getDireccion());
        holder.txtstatus.setText(userModelList.get(position).getStatus());

        holder.btnComandoCliente.setVisibility(View.GONE);
        holder.txt_descripcion_taxi.setVisibility(View.GONE);
        holder.txt_no_taxi.setVisibility(View.GONE);
        holder.txt_evaluacion.setVisibility(View.GONE);
        //Abierta
       if(userModelList.get(position).getStatus().equals("Servicio:abierta"))
       {

           holder.txtstatus.setTextColor(Color.rgb(0,143,57));
           holder.btnComandoCliente.setVisibility(View.VISIBLE);
           holder.btnComandoCliente.setText("Modificar servicio");
           holder.txtcosto.setVisibility(View.VISIBLE);
           if(userModelList.get(position).getCosto().equals("Costo aprox del servicio: $"))
           {
               holder.txtcosto.setText("");
               holder.txtcosto.setTextColor(Color.rgb(229,190,1));
           }
           else
               {
                   if(userModelList.get(position).getCosto().equals("Costo aprox del servicio: $Servicio gratis"))
                   {
                       holder.txtcosto.setText("Servicio gratis");
                       holder.txtcosto.setTextColor(Color.rgb(229,190,1));
                   }
                   else {
                       holder.txtcosto.setText(userModelList.get(position).getCosto());
                       holder.txtcosto.setTextColor(Color.rgb(229,190,1));
                   }
               }
           holder.btnComandoCliente.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String id=userModelList.get(position).getIdentificador();
                   Bundle datosAEnviar = new Bundle();
                   datosAEnviar.putString("identificador",id);
                   modificarServicio modificarservicio = new modificarServicio();
                   modificarservicio.setArguments(datosAEnviar);
                   AppCompatActivity activity = (AppCompatActivity) context;
                   activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,modificarservicio).addToBackStack(null).commit();
               }
           });

       }
       else if(userModelList.get(position).getStatus().equals("Servicio:realizada")
       )
           {
               holder.txtstatus.setTextColor(Color.BLACK);
               if(userModelList.get(position).getEvaluacion().equals("0")){
               holder.btnComandoCliente.setVisibility(View.VISIBLE);
               holder.txtcosto.setVisibility(View.VISIBLE);
               if (userModelList.get(position).getCosto().equals("Costo aprox del servicio: $"))
               {
                   holder.txtcosto.setText("");
                   holder.txtcosto.setTextColor(Color.rgb(229,190,1));
               }
               else
                   {
                       if(userModelList.get(position).getCosto().equals("Costo aprox del servicio: $Servicio gratis"))
                       {
                           holder.txtcosto.setText("Servicio gratis");
                           holder.txtcosto.setTextColor(Color.rgb(229,190,1));
                       }
                       else {
                           holder.txtcosto.setText(userModelList.get(position).getCosto());
                           holder.txtcosto.setTextColor(Color.rgb(229,190,1));
                       }
               }
               holder.btnComandoCliente.setText("Calificar servicio");
               holder.btnComandoCliente.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       String id=userModelList.get(position).getIdentificador();
                       Bundle datosAEnviar = new Bundle();
                       datosAEnviar.putString("identificador",id);
                       evaluarservicio evaluarservicio = new evaluarservicio();
                       evaluarservicio.setArguments(datosAEnviar);
                       AppCompatActivity activity = (AppCompatActivity) view.getContext();
                       activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,evaluarservicio).addToBackStack(null).commit();


                   }
               });
               } else{
                   holder.txt_evaluacion.setText("Ya realizaste la evaluación");
                   holder.txt_evaluacion.setVisibility(View.VISIBLE);
               }


           }
       else if(userModelList.get(position).getStatus().equals("Servicio:Confirmada"))
       {
           holder.txtstatus.setTextColor(Color.BLUE);
           holder.txt_descripcion_taxi.setVisibility(View.VISIBLE);
           holder.txt_no_taxi.setVisibility(View.VISIBLE);
           holder.txtcosto.setVisibility(View.VISIBLE);
           holder.txt_no_taxi.setText(userModelList.get(position).getNotaxi());
           holder.txt_descripcion_taxi.setText(userModelList.get(position).getDescripcionVehiculo());
           holder.btnComandoCliente.setVisibility(View.VISIBLE);
               holder.txtcosto.setText(userModelList.get(position).getCosto());
               if (userModelList.get(position).getCosto().equals("\nCosto aproximado del servicio: $Servicio gratis"))
               {
                   holder.txtcosto.setText("Servicio gratis");
                   holder.txtcosto.setTextColor(Color.rgb(229,190,1));
               }
               else
                   {
                       holder.txtcosto.setText(userModelList.get(position).getCosto());
                       holder.txtcosto.setTextColor(Color.rgb(229,190,1));
                   }


               /////////////////////////////
           holder.btnComandoCliente.setText("Cancelar  servicio");
           holder.btnComandoCliente.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String id=userModelList.get(position).getIdentificador();
                   Log.d("iddelboton",id);
                   eliminarservicio("http://pruebataxi.laviveshop.com/app/eliminarservicio.php",id);
                   homeCliente homeCliente = new homeCliente();
                   AppCompatActivity activity = (AppCompatActivity) view.getContext();
                   activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,homeCliente).addToBackStack(null).commit();
               }
           });
               ////////////////////////////////////////////////////
       }
       else
           {
               holder.txtstatus.setTextColor(Color.RED);
               holder.txt_descripcion_taxi.setVisibility(View.GONE);
               holder.txt_no_taxi.setVisibility(View.GONE);
           }
    }
    private  void eliminarservicio(String URL, final String identificador)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"¡Servicio cancelado con exito!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error al eliminar el servicio",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("id",identificador);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this.context);
        requestQueue.add(stringRequest);
    }
    @Override
    public int getItemCount() {
        return userModelList.size();
    }
    public  void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }
    @Override
    public void onClick(View v) {
        if(listener!=null)
        {
            listener.onClick(v);
        }
    }


    //Asociacion con los elementos de la interfaz lista cliente txt
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtidentificador,txtfechahora,txtdireccion,txtstatus,txt_no_taxi, txt_descripcion_taxi, txt_evaluacion, txtcosto;
                Button btnComandoCliente;
        public ViewHolder(View v) {
            super(v);
            txtdireccion= (TextView) itemView.findViewById(R.id.direccion);
            txtfechahora=(TextView) itemView.findViewById(R.id.fechahora);
            txtstatus=(TextView) itemView.findViewById(R.id.status);
            txt_no_taxi=(TextView) itemView.findViewById(R.id.noTaxi);
            txt_descripcion_taxi=(TextView) itemView.findViewById(R.id.descripcionVehiculo);
            txt_evaluacion=(TextView) itemView.findViewById(R.id.evaluacion);
            txtcosto=(TextView) itemView.findViewById(R.id.costo);
            btnComandoCliente=(Button) itemView.findViewById(R.id.btnEfectoCliente);
        }
    }
}
