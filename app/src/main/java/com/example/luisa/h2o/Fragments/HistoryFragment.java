package com.example.luisa.h2o.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;

import com.example.luisa.h2o.HistoryShower;
import com.example.luisa.h2o.LoginActivity;
import com.example.luisa.h2o.adapter.HistoryAdapter;
import com.example.luisa.h2o.adapter.RequestAdapter;
import com.example.luisa.h2o.model.Solicitud;

import java.util.ArrayList;

public class HistoryFragment extends ListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Solicitud> solicituds= new ArrayList<>();

        for (Solicitud s:
                LoginActivity.usuarioActual.getSolicitudList()) {
            if(s.getState().equalsIgnoreCase("terminado")){
                solicituds.add(s);
            }
        }
        HistoryAdapter adapter = new HistoryAdapter(getContext(),solicituds );

        if (adapter != null) {
            setListAdapter(adapter);
        }

    }
}
