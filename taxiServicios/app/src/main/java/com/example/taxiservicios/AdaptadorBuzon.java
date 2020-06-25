package com.example.taxiservicios;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.OnClickListener;

public class AdaptadorBuzon extends Adapter<AdaptadorBuzon.ViewHolder> implements  OnClickListener{
    private Context context;
    private List<modeloBuzon> userModelList;
    private OnClickListener listener;
        public AdaptadorBuzon(List<modeloBuzon> userModelList, Context context)
    {
        this.userModelList = userModelList;
        this.context = context;
    }



    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_buzon,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
        holder.txtCorreo.setText(userModelList.get(position).getCorreo());
        holder.txtMensaje.setText(userModelList.get(position).getMensaje());
        holder.txtFechaBuzon.setText(userModelList.get(position).getFechaMensaje());
        holder.txtNombre.setText(userModelList.get(position).getNombre());



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
        TextView txtCorreo, txtMensaje,txtFechaBuzon,txtNombre;

        public ViewHolder(View v) {
            super(v);
            txtCorreo= (TextView) itemView.findViewById(R.id.correo);
            txtMensaje= (TextView) itemView.findViewById(R.id.mensaje);
            txtFechaBuzon=(TextView) itemView.findViewById(R.id.fechaBuzon);
            txtNombre=(TextView) itemView.findViewById(R.id.nombre);
        }
    }



}
