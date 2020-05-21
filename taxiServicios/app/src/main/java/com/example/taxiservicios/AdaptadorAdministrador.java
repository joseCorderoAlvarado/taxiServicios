package com.example.taxiservicios;
import android.graphics.Color;
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
        if (userModelList.get(position).getReferencia().equals("Comentarios:\n"))
        {
            holder.txtfechahora.setText(userModelList.get(position).getFechahora());
            holder.txtdireccion.setText(userModelList.get(position).getDireccion());
       //     holder.txtreferencia.setText(userModelList.get(position).getReferencia());
            holder.txtstatus.setText(userModelList.get(position).getStatus());
        }
        else
            {
                holder.txtfechahora.setText(userModelList.get(position).getFechahora());
                holder.txtdireccion.setText(userModelList.get(position).getDireccion());
                holder.txtreferencia.setText(userModelList.get(position).getReferencia());
                holder.txtstatus.setText(userModelList.get(position).getStatus());
            }
        if(userModelList.get(position).getStatus().equals("Servicio:abierta"))
        {
            holder.txtstatus.setTextColor(Color.rgb(0,143,57));
        }
        else if(userModelList.get(position).getStatus().equals("Servicio:realizada"))
        {
            holder.txtstatus.setTextColor(Color.BLACK);
        }
        else if(userModelList.get(position).getStatus().equals("Servicio:Confirmada"))
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
        TextView txtidentificador,txtfechahora,txtdireccion,txtstatus,txtreferencia;
        public ViewHolder(View v) {
            super(v);
            txtdireccion= (TextView) itemView.findViewById(R.id.direccion2);
            txtreferencia=(TextView) itemView.findViewById(R.id.referencia2);
            txtfechahora=(TextView) itemView.findViewById(R.id.fechahora2);
            txtstatus=(TextView) itemView.findViewById(R.id.status2);
        }
    }
}
