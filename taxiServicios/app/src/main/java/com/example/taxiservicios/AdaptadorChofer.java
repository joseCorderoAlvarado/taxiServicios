package com.example.taxiservicios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.OnClickListener;

public class AdaptadorChofer extends Adapter<AdaptadorChofer.ViewHolder> implements  OnClickListener{
    private List<modeloChofer> userModelList;
    private OnClickListener listener;
    public AdaptadorChofer(List<modeloChofer> userModelList) {
        this.userModelList = userModelList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listachofer,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCliente.setText(userModelList.get(position).getCliente());
        holder.txtEvaluacion.setText(userModelList.get(position).getEvaluacion());
        holder.txtNota.setText(userModelList.get(position).getNota());
        holder.txtFechaHora.setText(userModelList.get(position).getFecha_hora());
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
        TextView txtCliente,txtEvaluacion,txtNota,txtFechaHora;
        public ViewHolder(View v) {
            super(v);
            txtCliente= (TextView) itemView.findViewById(R.id.cliente);
            txtEvaluacion= (TextView) itemView.findViewById(R.id.evaluacion);
            txtNota=(TextView) itemView.findViewById(R.id.nota);
            txtFechaHora=(TextView) itemView.findViewById(R.id.fechahora3);
        }
    }
}
