package com.example.luisa.h2o;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.RequestManager;
import com.example.luisa.h2o.api.UsuarioServicio;
import com.example.luisa.h2o.model.Solicitud;
import com.example.luisa.h2o.model.Usuario;
import com.example.luisa.h2o.model.UsuarioRespuesta;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestShower extends AppCompatActivity {
    private static final String TAG_ACTUAL = "Asginacion";
    Retrofit retrofit;
    private String name;
    private String problem;
    private String urgency;
    private String place;
    private int id;
    private  Solicitud solicitud= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_shower);

        retrofit = new Retrofit.Builder().baseUrl("https://h2oservicetest.herokuapp.com/usuario/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        id = getIntent().getIntExtra("idRequest",0);

        for (Solicitud s:
             MainActivity.listaSolicitud) {
            if(s.getId() == id){
             name = s.getName();
             problem= s.getProblem();
             urgency= s.getUrgency();
             place = s.getPlace();
             solicitud = s;
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

    public void requestSelected(View view) {


        Intent intent = new Intent(this, MainActivity.class);
        if (solicitud != null) {
            solicitud.setState("actual");
            LoginActivity.usuarioActual.getSolicitudList().add(solicitud);
            agregarSolicitud(LoginActivity.usuarioActual,solicitud);
        }
        intent.putExtra("idRequest", id);
        intent.putExtra("nameRequest", name);
//        intent.putExtra("problem",problem);
//        intent.putExtra("urgency",urgency);
//        intent.putExtra("place",place);
        startActivity(intent);
    }


    public void agregarSolicitud(Usuario usuario,Solicitud solicitud){

        RequestQueue queue = Volley.newRequestQueue(this);
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

    public void cancel(View view) {
        onBackPressed();
    }

//    public void goToMapsRequest(View view) {
//        Intent intent = new Intent(this,MapsActivity.class);
//        startActivity(intent);
//    }
}
