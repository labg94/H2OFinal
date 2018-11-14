package com.example.luisa.h2o;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luisa.h2o.Fragments.ActualFragment;
import com.example.luisa.h2o.adapter.TabAdapter;
import com.example.luisa.h2o.api.SolicitudServicio;
import com.example.luisa.h2o.api.UsuarioServicio;
import com.example.luisa.h2o.model.Solicitud;
import com.example.luisa.h2o.model.SolicitudRespuesta;
import com.example.luisa.h2o.model.Usuario;
import com.example.luisa.h2o.model.UsuarioRespuesta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;

    public static ArrayList<Solicitud> listaSolicitud = new ArrayList<>();
    private final String TAG_SOLICITUDES = "Solicitudes";
    private static final int REQUEST_CODE = 1;
    private static final String[] PERMISOS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int leer = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (leer == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, PERMISOS, REQUEST_CODE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit= new Retrofit.Builder().baseUrl("https://h2oservicetest.herokuapp.com/usuario/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        obtenerSolicitudes();

        updateView();
    }

    private void updateView() {
        obtenerSolicitudes();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.view_pager);

        if (viewPager != null) {
            viewPager.setAdapter(new TabAdapter(getSupportFragmentManager(), this));
            viewPager.setCurrentItem(2);
        }

        TabLayout tabLayout = findViewById(R.id.tab_layout);

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void goToCall(View view) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" +ActualFragment.solicitud.getPhone() ));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.v("goToCall", "Pide permisos");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
        } else {
            Log.v("goToCall", "va al intent");
            startActivity(callIntent);
        }


    }

    /**
        camara no funciona aun
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = findViewById(R.id.fix_image);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,1138,640,true));
        } else {
            Toast.makeText(this, "NO se guardo la foto", Toast.LENGTH_LONG).show();
        }
    }


    public void Terminado(View view) {
        ArrayList<Solicitud> solicituds = (ArrayList<Solicitud>) LoginActivity.usuarioActual.getSolicitudList();
        for (int i = 0; i <solicituds.size()-1 ; i++) {
            if(ActualFragment.solicitud.getId() ==solicituds.get(i).getId() ){
                LoginActivity.usuarioActual.getSolicitudList().get(i).setState("Terminado");
                listaSolicitud=(ArrayList<Solicitud>) LoginActivity.usuarioActual.getSolicitudList();
                terminarSolicitud(ActualFragment.solicitud);
                ActualFragment.solicitud= null;
                break;
            }
        }

        updateView();
    }

    public void terminarSolicitud(Solicitud solicitud){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://h2oservicetest.herokuapp.com/usuario/terminado?solicitud="+solicitud.getId();

        Log.i(TAG_SOLICITUDES, "url: "+ url);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG_SOLICITUDES, "Todo bien " + response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG_SOLICITUDES, "Error: "+ error);//HOLA
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

    public void agregarImagen(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }


    private void obtenerSolicitudes(){

        SolicitudServicio servicio = retrofit.create(SolicitudServicio.class);

        String userName = "admin";
        String password = "123";

        String base = userName + ":" + password;

        String authHeader = "Basic "+ Base64.encodeToString(base.getBytes(),Base64.NO_WRAP);

        Call<SolicitudRespuesta> solicitudRespuestaCall = servicio.obtenerDisponibles(authHeader);
        Log.i(TAG_SOLICITUDES,"obtenerDatos: se creo el call");
        try {
            solicitudRespuestaCall.enqueue(new Callback<SolicitudRespuesta>() {
                @Override
                public void onResponse(Call<SolicitudRespuesta> call, Response<SolicitudRespuesta> response) {
                    Log.i(TAG_SOLICITUDES,"onResponse: entro");
                    try {

                        if (response.isSuccessful() && response.body() != null) {

                            Log.i(TAG_SOLICITUDES,"onResponse: " + response.code());

                            SolicitudRespuesta solicitudRespuesta = response.body();

                            listaSolicitud = solicitudRespuesta.getSolicituds();

                            if (listaSolicitud != null) {
                                for (Solicitud s : listaSolicitud) {
                                    Log.i(TAG_SOLICITUDES, "estado de la solicitud " + s.getState()+ " telefono " + s.getPhone());
                                }
                            } else {
                                Log.e(TAG_SOLICITUDES, "onRespose: Lista nula");
                            }
                        } else {
                            Log.e(TAG_SOLICITUDES,"onResponse " + response.errorBody());
                        }
                    } catch (Exception e) {
                        Log.e(TAG_SOLICITUDES,"Se cayo por " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SolicitudRespuesta> call, Throwable t) {
                    Log.e(TAG_SOLICITUDES, "onFailure" + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e(TAG_SOLICITUDES,"Se cayo por " + e.getMessage());
        }
    }

}
