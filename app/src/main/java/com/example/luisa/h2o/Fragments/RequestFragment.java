package com.example.luisa.h2o.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.Toast;

import com.example.luisa.h2o.MainActivity;
import com.example.luisa.h2o.adapter.RequestAdapter;
import com.example.luisa.h2o.model.Solicitud;

import java.util.ArrayList;

public class RequestFragment extends ListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Solicitud> solicituds= new ArrayList<>();



            for (Solicitud s:
                    MainActivity.listaSolicitud) {
                Log.i("fragment",s.getName());

                if(s.getState().equalsIgnoreCase("Disponible")){
                    solicituds.add(s);
                }
            }
            RequestAdapter adapter = new RequestAdapter(getContext(),solicituds );

            if (adapter != null) {
                setListAdapter(adapter);
            }

    }
}
