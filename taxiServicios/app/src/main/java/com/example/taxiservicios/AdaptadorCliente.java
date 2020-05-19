package com.example.taxiservicios;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import static androidx.recyclerview.widget.RecyclerView.*;
public class AdaptadorCliente extends RecyclerView.Adapter<AdaptadorCliente.ViewHolder> implements  View.OnClickListener{
    private List<modeloCliente> userModelList;
    private View.OnClickListener listener;
    public AdaptadorCliente(List<modeloCliente> userModelList) {
        this.userModelList = userModelList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listacliente,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtfechahora.setText(userModelList.get(position).getFechahora());
        holder.txtdireccion.setText(userModelList.get(position).getDireccion());
        holder.txtstatus.setText(userModelList.get(position).getStatus());
       if(userModelList.get(position).getStatus().equals("Status del servicio:abierta"))
       {
           holder.txt_descripcion_taxi.setVisibility(View.GONE);
           holder.txt_no_taxi.setVisibility(View.GONE);
           holder.txtstatus.setTextColor(Color.rgb(0,143,57));
       }
       else if(userModelList.get(position).getStatus().equals("Status del servicio:realizada"))
           {
               holder.txt_descripcion_taxi.setVisibility(View.GONE);
               holder.txt_no_taxi.setVisibility(View.GONE);
               holder.txtstatus.setTextColor(Color.BLACK);
           }
       else if(userModelList.get(position).getStatus().equals("Status del servicio:Confirmada"))
       {
           holder.txtstatus.setTextColor(Color.BLUE);

           holder.txt_descripcion_taxi.setVisibility(View.VISIBLE);
           holder.txt_no_taxi.setVisibility(View.VISIBLE);

           holder.txt_no_taxi.setText(userModelList.get(position).getNotaxi());
           holder.txt_descripcion_taxi.setText(userModelList.get(position).getDescripcionVehiculo());


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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtidentificador,txtfechahora,txtdireccion,txtstatus,txt_no_taxi, txt_descripcion_taxi;
        public ViewHolder(View v) {
            super(v);
            txtdireccion= (TextView) itemView.findViewById(R.id.direccion);
            txtfechahora=(TextView) itemView.findViewById(R.id.fechahora);
            txtstatus=(TextView) itemView.findViewById(R.id.status);
            txt_no_taxi=(TextView) itemView.findViewById(R.id.noTaxi);
            txt_descripcion_taxi=(TextView) itemView.findViewById(R.id.descripcionVehiculo);

        }
    }
}
