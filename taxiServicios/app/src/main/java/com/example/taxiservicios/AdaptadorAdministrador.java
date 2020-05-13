package com.example.taxiservicios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorAdministrador extends RecyclerView.Adapter<AdaptadorAdministrador.ViewHolder> implements  View.OnClickListener {
    private List<modeloAdministrador> userModelList;
    private View.OnClickListener listener;
    public AdaptadorAdministrador(List<modeloAdministrador> userModelList) {
        this.userModelList = userModelList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listaadministrador,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtidentificador.setText(userModelList.get(position).getIdentificador());
        holder.txtfechahora.setText(userModelList.get(position).getFechahora());
        holder.txtdireccion.setText(userModelList.get(position).getDireccion());
        holder.txtreferencia.setText(userModelList.get(position).getReferencia());
        holder.txtstatus.setText(userModelList.get(position).getStatus());
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
        TextView txtidentificador,txtfechahora,txtdireccion,txtstatus,txtreferencia;
        public ViewHolder(View v) {
            super(v);
            txtidentificador= (TextView) itemView.findViewById(R.id.identificador2);
            txtdireccion= (TextView) itemView.findViewById(R.id.direccion2);
            txtreferencia=(TextView) itemView.findViewById(R.id.referencia2);
            txtfechahora=(TextView) itemView.findViewById(R.id.fechahora2);
            txtstatus=(TextView) itemView.findViewById(R.id.status2);
        }
    }
}