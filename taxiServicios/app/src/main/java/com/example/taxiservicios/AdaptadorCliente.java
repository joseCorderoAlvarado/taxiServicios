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
           holder.txtstatus.setTextColor(Color.rgb(0,143,57));
       }
       else if(userModelList.get(position).getStatus().equals("Status del servicio:realizada"))
           {
               holder.txtstatus.setTextColor(Color.BLACK);
           }
       else if(userModelList.get(position).getStatus().equals("Status del servicio:Confirmada"))
       {
           holder.txtstatus.setTextColor(Color.BLUE);
       }
       else
           {
               holder.txtstatus.setTextColor(Color.RED);
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
        TextView txtidentificador,txtfechahora,txtdireccion,txtstatus;
        public ViewHolder(View v) {
            super(v);
            txtdireccion= (TextView) itemView.findViewById(R.id.direccion);
            txtfechahora=(TextView) itemView.findViewById(R.id.fechahora);
            txtstatus=(TextView) itemView.findViewById(R.id.status);
        }
    }
}
