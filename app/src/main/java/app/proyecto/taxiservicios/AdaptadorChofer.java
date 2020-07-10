package app.proyecto.taxiservicios;

import android.content.Context;
import android.content.Intent;


import android.net.Uri;
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
      if(userModelList.get(position).getCosto().equals("Costo aprox del servicio: $Servicio gratis\n"))
      {
          holder.txtCosto.setText("Servicio Gratis");
      }
      else {
          holder.txtCosto.setText(userModelList.get(position).getCosto());
      }
        holder.btnCall.setText("Llamar");

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telefono=userModelList.get(position).getTelefono().toString();

                StringBuilder sb = new StringBuilder(telefono);

                sb.delete(0, 8);
                sb.deleteCharAt(0);

                String result = sb.toString();
                System.out.println("El telefono es: " + result);
                Intent llamar = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", result, null));
                context.startActivity(llamar);
            }



        });



        holder.btnfinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=userModelList.get(position).getIdentificador().toString();


                finalizarruta("http://pruebataxi.laviveshop.com/app/actualizarfinalizado.php",id);
            }



        });



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
        Button btnfinalizar, btnCall;
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
        }
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
