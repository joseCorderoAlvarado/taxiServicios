package com.example.taxiservicios;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
public class AdaptadorAdministrador extends RecyclerView.Adapter<AdaptadorAdministrador.ViewHolder> implements  View.OnClickListener {
    private List<modeloAdministrador> userModelList;
    private View.OnClickListener listener;
    private Context context;
    public AdaptadorAdministrador(List<modeloAdministrador> userModelList,Context context   ) {
        this.userModelList = userModelList;
        this.context = context;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listaadministrador,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (userModelList.get(position).getReferencia().equals("Comentarios:\n"))
        {
            holder.txtfechahora.setText(userModelList.get(position).getFechahora());
            holder.txtdireccion.setText(userModelList.get(position).getDireccion());
       //
            holder.txtstatus.setText(userModelList.get(position).getStatus());
            holder.txtCliente.setText(userModelList.get(position).getNombre());
            if(userModelList.get(position).getCosto().equals("Servicio gratis"))
            {
                holder.txtcostoso.setText(userModelList.get(position).getCosto());
            }
            else
                {
                    holder.txtcostoso.setText("Costo: $"+userModelList.get(position).getCosto());
                }
        }
        else
            {
                holder.txtfechahora.setText(userModelList.get(position).getFechahora());
                holder.txtdireccion.setText(userModelList.get(position).getDireccion());
                holder.txtreferencia.setText(userModelList.get(position).getReferencia());
                holder.txtstatus.setText(userModelList.get(position).getStatus());
                holder.txtCliente.setText(userModelList.get(position).getNombre());
                if(userModelList.get(position).getCosto().equals("Servicio gratis"))
                {
                    holder.txtcostoso.setText(userModelList.get(position).getCosto());
                }
                else
                {
                    holder.txtcostoso.setText("Costo: $"+userModelList.get(position).getCosto());
                }
            }


        holder.txtVehiculo.setVisibility(View.GONE);
        holder.btnComando.setVisibility(View.GONE);

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


        if(userModelList.get(position).getStatus().equals("Servicio:abierta"))
        {
            holder.txtstatus.setTextColor(Color.rgb(0,143,57));
            holder.btnComando.setVisibility(View.VISIBLE);
            holder.btnComando.setText("Confirmar servicio");
            holder.btnComando.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id=userModelList.get(position).getIdentificador();
                    Bundle datosAEnviar = new Bundle();
                    datosAEnviar.putString("identificador",id);
                    confirmarServicio confirmarServicio = new confirmarServicio();
                    confirmarServicio.setArguments(datosAEnviar);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,confirmarServicio).addToBackStack(null).commit();
                }
            });



        }
        else if(userModelList.get(position).getStatus().equals("Servicio:realizada"))
        {
            holder.txtstatus.setTextColor(Color.BLACK);
        }
        else if(userModelList.get(position).getStatus().equals("Servicio:Confirmada"))
        {
            holder.txtVehiculo.setVisibility(View.VISIBLE);
            holder.txtVehiculo.setText(userModelList.get(position).getVehiculoCompleto());
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


    //Las asociaciones coj la listaadminsitrador xml
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtidentificador,txtfechahora,txtdireccion,
                txtstatus,txtreferencia,txtCliente,txtVehiculo,txtcostoso,txtTelefono;
        Button btnComando,btnCall;
        public ViewHolder(View v) {
            super(v);
            txtdireccion= (TextView) itemView.findViewById(R.id.direccion2);
            txtreferencia=(TextView) itemView.findViewById(R.id.referencia2);
            txtfechahora=(TextView) itemView.findViewById(R.id.fechahora2);
            txtstatus=(TextView) itemView.findViewById(R.id.status2);
            txtCliente=(TextView) itemView.findViewById(R.id.cliente);
            txtVehiculo=(TextView) itemView.findViewById(R.id.vehiculo);
            txtcostoso=(TextView) itemView.findViewById(R.id.gratis);
            btnComando=(Button) itemView.findViewById(R.id.btnEfectoAdmin);

            txtTelefono=(TextView) itemView.findViewById(R.id.telefono);
            btnCall= (Button)itemView.findViewById(R.id.botonllamar);
        }
    }
}
