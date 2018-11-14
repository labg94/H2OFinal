package com.example.luisa.h2o;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.luisa.h2o.model.Solicitud;

public class HistoryShower extends AppCompatActivity {

    private String name;
    private String problem;
    private String urgency;
    private String place;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_shower);

        id = getIntent().getIntExtra("idRequest",0);

        for (Solicitud s:
                LoginActivity.usuarioActual.getSolicitudList()) {
            if(s.getId() == id){
                name = s.getName();
                problem= s.getProblem();
                urgency= s.getUrgency();
                place = s.getPlace();
            }
        }

        TextView tvName= findViewById(R.id.rs_name);
        TextView tvProblem= findViewById(R.id.rs_problem);
        TextView tvUrgency= findViewById(R.id.rs_urgency);
        TextView tvPlace= findViewById(R.id.rs_place);

        tvName.setText(name);
        tvPlace.setText(place);
        tvProblem.setText(problem);
        tvUrgency.setText("Urgencia "+urgency);


    }
}
