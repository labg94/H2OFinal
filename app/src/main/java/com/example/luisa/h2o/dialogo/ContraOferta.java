package com.example.luisa.h2o.dialogo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luisa.h2o.LoginActivity;
import com.example.luisa.h2o.MainActivity;
import com.example.luisa.h2o.R;
import com.example.luisa.h2o.RequestShower;
import com.example.luisa.h2o.model.Solicitud;
import com.example.luisa.h2o.model.Usuario;

import java.util.HashMap;
import java.util.Map;

public class ContraOferta extends DialogFragment {

    private Solicitud solicitud= null;
    private int id;
    private static final String TAG_ACTUAL = "Asginacion";


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogo = inflater.inflate(R.layout.contraoferta_dialogo, null);

        builder.setView(dialogo);


        Bundle bundle = getArguments();

        id = bundle.getInt("id");

        Button aceptar = dialogo.findViewById(R.id.btn_aceptar_oferta);
        Button cancelar = dialogo.findViewById(R.id.btn_cancelar_oferta);

        for (Solicitud s:
                MainActivity.listaSolicitud) {
            if(s.getId() == id){
                solicitud = s;
            }
        }


        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSelected(v);
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }



    public void agregarSolicitud(Usuario usuario, Solicitud solicitud){

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://h2oservicetest.herokuapp.com/usuario/asignar?solicitud="+solicitud.getId()+"&usuario="+usuario.getId();

        Log.i(TAG_ACTUAL, "url: "+ url);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG_ACTUAL, "Todo bien" + response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG_ACTUAL, "Error: "+ error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Basic YWRtaW46MTIz");
                return params;
            }
        };
        queue.add(stringRequest);
    }


    public void requestSelected(View view) {


        Intent intent = new Intent(getContext(), MainActivity.class);
        if (solicitud != null) {
            solicitud.setState("actual");
            LoginActivity.usuarioActual.getSolicitudList().add(solicitud);
            agregarSolicitud(LoginActivity.usuarioActual,solicitud);
        }
        intent.putExtra("idRequest", id);
        intent.putExtra("nameRequest", solicitud.getName());
//        intent.putExtra("problem",problem);
//        intent.putExtra("urgency",urgency);
//        intent.putExtra("place",place);
        startActivity(intent);
    }

}
