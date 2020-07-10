package app.proyecto.taxiservicios;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taxiservicios.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class avisoprivacidad extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.fragment_avisoprivacidad,container,false);
        return view;
    }
}
