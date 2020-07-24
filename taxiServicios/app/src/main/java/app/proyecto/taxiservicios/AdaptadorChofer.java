package app.proyecto.taxiservicios;

import android.content.Context;
import android.content.Intent;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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


import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.OnClickListener;

public class AdaptadorChofer extends Adapter<AdaptadorChofer.ViewHolder> implements  OnClickListener{
    private Context context;
    private List<modeloChofer> userModelList;
    private OnClickListener listener;
        public AdaptadorChofer(List<modeloChofer> userModelList,Context context)
    {
        this.userModelList = userModelList;
        this.context = context;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listachoferserviciospendientes,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
        holder.txtCliente.setText(userModelList.get(position).getCliente());
        holder.txtFechaHora.setText(userModelList.get(position).getFechahora());
        holder.txtRecoger.setText(userModelList.get(position).getDireccionRecoger());
        holder.txtLlevar.setText(userModelList.get(position).getDireccionLlevar());
        holder.txtTelefono.setText(userModelList.get(position).getTelefono());
        if (userModelList.get(position).getCosto().equals("Costo aprox del servicio: $Servicio gratis\n")) {
            holder.txtCosto.setText("Servicio Gratis");
            holder.txtCosto.setTextColor(Color.rgb(229, 190, 1));
        } else {
            holder.txtCosto.setText(userModelList.get(position).getCosto());
            holder.txtCosto.setTextColor(Color.rgb(229, 190, 1));
        }
        holder.btnCall.setText("Llamar");
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telefono = userModelList.get(position).getTelefono().toString();
                StringBuilder sb = new StringBuilder(telefono);
                sb.delete(0, 8);
                sb.deleteCharAt(0);
                String result = sb.toString();
                System.out.println("El telefono es: " + result);
                Intent llamar = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", result, null));
                context.startActivity(llamar);
            }


        });

if(userModelList.get(position).getStatus().equals("Confirmada"))
{
    /////////////////////////esto esta bien no moverle
    holder.btnfinalizar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String id = userModelList.get(position).getIdentificador().toString();
            finalizarruta("http://pruebataxi.laviveshop.com/app/actualizarfinalizado.php", id);
        }


    });
    ////////////////////////////////////////////////////
    holder.btnCancelar.setVisibility(View.GONE);
   /* holder.btnCancelar.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            String id = userModelList.get(position).getIdentificador().toString();
            Log.d("rechazo","rechazo");
            String correochofer=userModelList.get(position).getCorreochofer();
            rechazarservicio("http://pruebataxi.laviveshop.com/app/rechazarservicio.php",id,correochofer);
        }
    });*/
}
else
    {
        holder.btnfinalizar.setText("Aceptar Servicio");
        holder.btnfinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = userModelList.get(position).getIdentificador().toString();
                String correochofer=userModelList.get(position).getCorreochofer();
                aceptarservicio("http://pruebataxi.laviveshop.com/app/aceptarservicio.php",correochofer,id);
            }


        });
        holder.btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = userModelList.get(position).getIdentificador().toString();
                String correochofer=userModelList.get(position).getCorreochofer();
                Log.d("rechazo","rechazo");
                rechazarservicio("http://pruebataxi.laviveshop.com/app/rechazarservicio.php",id,correochofer);
            }
        });
    }


    }
    @Override
    public int getItemCount() {
        return userModelList.size();
    }
    public  void setOnClickListener(OnClickListener listener)
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCliente,txtFechaHora,txtRecoger,txtLlevar,txtTelefono,txtCosto;
        Button btnfinalizar, btnCall,btnCancelar;
        public ViewHolder(View v) {
            super(v);
            txtCliente= (TextView) itemView.findViewById(R.id.cliente);
            txtFechaHora= (TextView) itemView.findViewById(R.id.fechahora4);
            txtRecoger=(TextView) itemView.findViewById(R.id.direccionRecoger);
            txtLlevar=(TextView) itemView.findViewById(R.id.direccionLlevar);
            txtTelefono=(TextView) itemView.findViewById(R.id.telefono);
            txtCosto=(TextView) itemView.findViewById(R.id.costo);
            btnCall= (Button)itemView.findViewById(R.id.botonllamar);
            btnfinalizar =(Button)itemView.findViewById(R.id.btnFinalizar);
            btnCancelar =(Button)itemView.findViewById(R.id.btnEliminar);
        }
    }

    private  void aceptarservicio(String URL,final String correo, final String id)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1"))
                {
                    Toast.makeText(context,"Servicio confirmador  con exito",Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(context,inicioChofer.class);
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context,"El servicio ya fue asignado por otro taxista",Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(context,inicioChofer.class);
                    context.startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error al actualizar el servicio",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",correo.toString());
                parametros.put("id",id.toString());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private  void rechazarservicio(String URL, final String identificador, final String correoChofer)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Servicio rechazado con exito",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(context,inicioChofer.class);
                context.startActivity(intent);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error al actualizar el servicio",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",correoChofer.toString());
                parametros.put("idservicio",identificador.toString());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private  void finalizarruta(String URL, final String identificador)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Servicio finalizado con exito!!",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(context,inicioChofer.class);
                context.startActivity(intent);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error al actualizar el servicio",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("idservicio",identificador.toString());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
