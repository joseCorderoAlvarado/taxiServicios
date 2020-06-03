package com.example.taxiservicios;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


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
        Button btnfinalizar, btnCall;
        public ViewHolder(View v) {
            super(v);
            txtCliente= (TextView) itemView.findViewById(R.id.cliente);
            txtFechaHora= (TextView) itemView.findViewById(R.id.fechahora4);
            txtRecoger=(TextView) itemView.findViewById(R.id.direccionRecoger);
            txtLlevar=(TextView) itemView.findViewById(R.id.direccionLlevar);
            txtTelefono=(TextView) itemView.findViewById(R.id.telefono);
            btnCall= (Button)itemView.findViewById(R.id.botonllamar);
        }
    }
}
