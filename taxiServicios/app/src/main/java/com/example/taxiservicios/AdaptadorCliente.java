package com.example.taxiservicios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import static androidx.recyclerview.widget.RecyclerView.*;
public class AdaptadorCliente extends RecyclerView.Adapter<AdaptadorCliente.ViewHolder> {
    private List<modeloCliente> userModelList;
    public AdaptadorCliente(List<modeloCliente> userModelList) {
        this.userModelList = userModelList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listacliente,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtNombre.setText(userModelList.get(position).getNombre());
        holder.txtInformacion.setText(userModelList.get(position).getInfo());
    }
    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre,txtInformacion;
        public ViewHolder(View v) {
            super(v);
            txtNombre= (TextView) itemView.findViewById(R.id.idNombre);
            txtInformacion= (TextView) itemView.findViewById(R.id.idInfo);
        }
    }
}
