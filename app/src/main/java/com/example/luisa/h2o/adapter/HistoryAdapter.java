package com.example.luisa.h2o.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.luisa.h2o.HistoryShower;
import com.example.luisa.h2o.R;
import com.example.luisa.h2o.RequestShower;
import com.example.luisa.h2o.model.Solicitud;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class HistoryAdapter extends ArrayAdapter<Solicitud> {

    public HistoryAdapter(@NonNull Context context, List<Solicitud> solicitudList) {
        super(context,R.layout.request_view ,solicitudList);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.request_view,null);

        // ITEM SELECCIONADO DE LOS TRABAJOS
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),HistoryShower.class);
                intent.putExtra("idRequest",getItem(position).getId());
                startActivity(getContext(),intent,null);
            }
        });
        TextView problem = item.findViewById(R.id.request_problem);
        TextView place = item.findViewById(R.id.request_place);
        TextView urgency = item.findViewById(R.id.request_urgency);

        problem.setText(getItem(position).getProblem());
//        problem.setTextColor(getContext().getColor(R.color.colorAccent));
        place.setText(getItem(position).getPlace());

        int urg= Integer.parseInt(getItem(position).getUrgency());

//        switch (urg){
//            case 0:case 1:case 2: case 3:
//                urgency.setTextColor(Color.RED);
//                break;
//            case 4:case 5:case 6:case 7:
//                urgency.setTextColor(Color.rgb(120,0,120));
//                break;
//            case 8:case 9:case 10:
//                urgency.setTextColor(Color.BLUE);
//                break;
//        }

//        if ( urg >4) {
//
//        }else{
//            urgency.setTextColor(Color.RED);
//        }
        urgency.setTextColor(getContext().getColor(R.color.colorAccent));
        urgency.setText("Terminado");


        return item;
    }
}
