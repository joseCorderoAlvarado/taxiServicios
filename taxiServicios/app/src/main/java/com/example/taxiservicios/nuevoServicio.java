package com.example.taxiservicios;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import static android.app.Activity.RESULT_OK;

public class nuevoServicio extends Fragment {
DatePicker fecha;
TimePicker hora;
Spinner sOrigen, sDestino;
EditText txtOrigen, txtDestino,txtcomentarios;
Button btnNuevo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_nuevoservicio,container,false);
        fecha=view.findViewById(R.id.dpfecha);
        hora=view.findViewById(R.id.tphora);
        sOrigen=view.findViewById(R.id.spinnerpartida);
        sDestino=view.findViewById(R.id.spinnerdestino);
        txtOrigen=view.findViewById(R.id.txtOrigen);
        txtDestino=view.findViewById(R.id.txtDestino);
        txtcomentarios=view.findViewById(R.id.txtComentarios);
        btnNuevo=view.findViewById(R.id.btnnuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}
