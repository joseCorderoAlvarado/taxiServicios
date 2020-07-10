package app.proyecto.taxiservicios;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiservicios.R;

import java.util.List;

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
               }
               else
                   {
                       if(userModelList.get(position).getCosto().equals("Costo aprox del servicio: $Servicio gratis"))
                       {
                           holder.txtcosto.setText("Servicio gratis");
                       }
                       else {
                           holder.txtcosto.setText(userModelList.get(position).getCosto());
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
               holder.txtcosto.setText(userModelList.get(position).getCosto());
               if (userModelList.get(position).getCosto().equals("\nCosto aproximado del servicio: $Servicio gratis"))
               {
                   holder.txtcosto.setText("Servicio gratis");
               }
               else
                   {
                       holder.txtcosto.setText(userModelList.get(position).getCosto());
                   }
       }
       else
           {
               holder.txtstatus.setTextColor(Color.RED);
               holder.txt_descripcion_taxi.setVisibility(View.GONE);
               holder.txt_no_taxi.setVisibility(View.GONE);
           }
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
