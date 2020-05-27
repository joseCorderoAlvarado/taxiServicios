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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listachoferserviciospendientes,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCliente.setText(userModelList.get(position).getCliente());
        holder.txtFechaHora.setText(userModelList.get(position).getFechahora());
        holder.txtRecoger.setText(userModelList.get(position).getDireccionRecoger());
        holder.txtLlevar.setText(userModelList.get(position).getDireccionLlevar());
        holder.txtTelefono.setText(userModelList.get(position).getTelefono());

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
        TextView txtCliente,txtFechaHora,txtRecoger,txtLlevar,txtTelefono;
        public ViewHolder(View v) {
            super(v);
            txtCliente= (TextView) itemView.findViewById(R.id.cliente);
            txtFechaHora= (TextView) itemView.findViewById(R.id.fechahora4);
            txtRecoger=(TextView) itemView.findViewById(R.id.direccionRecoger);
            txtLlevar=(TextView) itemView.findViewById(R.id.direccionLlevar);
            txtTelefono=(TextView) itemView.findViewById(R.id.telefono);
        }
    }
}
