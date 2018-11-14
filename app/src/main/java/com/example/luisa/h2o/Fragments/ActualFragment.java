package com.example.luisa.h2o.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luisa.h2o.LoginActivity;
import com.example.luisa.h2o.R;
import com.example.luisa.h2o.model.Solicitud;

public class ActualFragment extends Fragment {
    public static  Solicitud solicitud = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        for (Solicitud s:
             LoginActivity.usuarioActual.getSolicitudList()) {
            if(s.getState().equalsIgnoreCase("actual")){
                solicitud = s;
                break;
            }
        }

        int actualLayout;
        View view;
        if (solicitud == null) {
            actualLayout = R.layout.actual_empty_fragment;
            view = inflater.inflate(actualLayout, container, false);
        } else {
            actualLayout = R.layout.actual_fragment;
            view = inflater.inflate(actualLayout, container, false);
            TextView problem =  view.findViewById(R.id.af_problem);
            TextView client = view.findViewById(R.id.af_client);
            TextView urgency = view.findViewById(R.id.af_urgency);
            TextView place = view.findViewById(R.id.af_place);
            TextView phone = view.findViewById(R.id.af_phone);
            problem.setText(solicitud.getProblem());
            client.setText(solicitud.getName());
            urgency.setText("Urgencia " +solicitud.getUrgency());
            place.setText(solicitud.getPlace());
            phone.setText(solicitud.getPhone());
        }

        return view;
    }

}
