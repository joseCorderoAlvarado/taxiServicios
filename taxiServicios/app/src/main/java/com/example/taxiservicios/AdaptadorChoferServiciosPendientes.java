package com.example.taxiservicios;

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


import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.OnClickListener;

public class AdaptadorChoferServiciosPendientes extends Adapter<AdaptadorChoferServiciosPendientes.ViewHolder> implements  OnClickListener{
    private List<modeloChoferServiciosPendientes> userModelList;
    private OnClickListener listener;
    public AdaptadorChoferServiciosPendientes(List<modeloChoferServiciosPendientes> userModelList) {
        this.userModelList = userModelList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listachofer,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        holder.txtCliente.setText(userModelList.get(position).getCliente());
        holder.txtEvaluacion.setVisibility(View.GONE);
        holder.txtFechaHora.setText(userModelList.get(position).getFecha_hora());
        holder.btnCalificarCliente.setVisibility(View.GONE);
        holder.txt_evaluacion.setVisibility(View.GONE);

        //cancelada
        if(userModelList.get(position).getStatus_idstatus().equals("2")) {
            holder.txtNota.setTextColor(Color.RED);
            holder.txtNota.setText("El usuario cancelo el servicio");

        }
        else if(userModelList.get(position).getStatus_idstatus().equals("3")) {





            if(userModelList.get(position).getEvaluacionCliente().equals("0")){
                holder.btnCalificarCliente.setVisibility(View.VISIBLE);
                holder.btnCalificarCliente.setText("Calificar servicio");
                holder.btnCalificarCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id=userModelList.get(position).getIdentificador();
                        Bundle datosAEnviar = new Bundle();
                        datosAEnviar.putString("identificador",id);
                        evaluarCliente evaluarcliente = new evaluarCliente();
                        evaluarcliente.setArguments(datosAEnviar);
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,evaluarcliente).addToBackStack(null).commit();


                    }
                });
            } else{
                holder.txt_evaluacion.setText("Ya realizaste la evaluación");
                holder.txt_evaluacion.setVisibility(View.VISIBLE);
            }

        }



        else{
            holder.txtNota.setTextColor(Color.rgb(0,0,0));
            holder.txtEvaluacion.setVisibility(View.VISIBLE);

            holder.txtEvaluacion.setText(userModelList.get(position).getEvaluacion());
            holder.txtNota.setText(userModelList.get(position).getNota());


        }

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
        TextView txtCliente,txtEvaluacion,txtNota,txtFechaHora,txt_evaluacion;
        Button btnCalificarCliente;
        public ViewHolder(View v) {
            super(v);
            txtCliente= (TextView) itemView.findViewById(R.id.cliente);
            txtEvaluacion= (TextView) itemView.findViewById(R.id.evaluacion);
            txtNota=(TextView) itemView.findViewById(R.id.nota);
            txtFechaHora=(TextView) itemView.findViewById(R.id.fechahora3);
            txt_evaluacion=(TextView) itemView.findViewById(R.id.notaBoton);
            btnCalificarCliente=(Button) itemView.findViewById(R.id.btnFinalizar);
        }
    }
}
