package com.example.taxiservicios;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
public class homeCliente extends Fragment {
    // TODO: Rename and change types of parameters
    RecyclerView recyclerPersonajes;
    ArrayList<modeloCliente> listaPersonaje;
    // TODO: Rename and change types and number of parameters
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homecliente, container, false);
        listaPersonaje=new ArrayList<>();
        recyclerPersonajes= (RecyclerView) view.findViewById(R.id.datos);
        recyclerPersonajes.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarLista();
        AdaptadorCliente adapter=new AdaptadorCliente(listaPersonaje);
        recyclerPersonajes.setAdapter(adapter);
        return view;
    }
    private void llenarLista() {
        listaPersonaje.add(new modeloCliente("Goku","Son Gokū es el protagonista del manga y anime Dragon Ball creado por Akira Toriyama."));
        listaPersonaje.add(new modeloCliente("Gohan","Son Gohan es un personaje del manga y anime Dragon Ball creado por Akira Toriyama. Es el primer hijo de Son Gokū y Chi-Chi"));
        listaPersonaje.add(new modeloCliente("Goten","Goten es un personaje ficticio de la serie de manga y anime Dragon Ball. Segundo hijo del protagonista, Goku, y Chichi/Milk."));
        listaPersonaje.add(new modeloCliente("Krilin","Krilin es un personaje de la serie de manga y anime Dragon Ball. Es el primer rival en artes marciales de Son Gokū aunque luego se convierte en su mejor amigo."));
        listaPersonaje.add(new modeloCliente("Picoro","Piccolo es un personaje de ficción de la serie de manga y anime Dragon Ball. Su padre, Piccolo Daimaō, surgió tras separarse de Kamisama. "));
        listaPersonaje.add(new modeloCliente("Trunks","Trunks es un personaje de ficción de la serie de manga y anime Dragon Ball de Akira Toriyama. Hijo de Vegueta y Bulma"));
        listaPersonaje.add(new modeloCliente("Vegueta","Vegeta es un personaje ficticio perteneciente a la raza llamada saiyajin, del manga y anime Dragon Ball."));
    }
}
