package com.example.luisa.h2o;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luisa.h2o.api.UsuarioServicio;
import com.example.luisa.h2o.model.Usuario;
import com.example.luisa.h2o.model.UsuarioRespuesta;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Retrofit retrofit;
    ArrayList<Usuario> listaUsuario = null;
    public static Usuario usuarioActual = null;
    private static final String TAG_USUARIOS = "USUARIOS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        retrofit = new Retrofit.Builder().baseUrl("https://h2oservicetest.herokuapp.com/usuario/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        obtenerDatos();
    }

    public void loginValidation(View view) {
        EditText editUsuario = findViewById(R.id.edit_usuario);
        EditText editPass = findViewById(R.id.edit_password);

        String usuario = editUsuario.getText().toString();
        String pass = editPass.getText().toString();
        boolean valido = false;

            for (Usuario u : listaUsuario) {
                if (u.getMail() != null) {
                    if (u.getMail().equalsIgnoreCase(usuario) && u.getPass().equals(pass)) {
                        valido = true;
                        usuarioActual = u;
                        if (u.getSolicitudList().size() == 0) {
                            Log.i(TAG_USUARIOS, "Solicitudes nulas");
                        } else {
                            Log.i(TAG_USUARIOS, "Solicitudes no  nulas" + u.getSolicitudList().get(0).getPhone());
                        }
                        break;
                    }
                }
            }


        if (valido) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Usuario y contraseña invalidos", Toast.LENGTH_SHORT).show();
        }


    }


    private void obtenerDatos() {

        UsuarioServicio servicio = retrofit.create(UsuarioServicio.class);

        String userName = "admin";
        String password = "123";

        String base = userName + ":" + password;

        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        Call<UsuarioRespuesta> usuarioRespuestaCall = servicio.obtenerUsuarios(authHeader);
        Log.i(TAG_USUARIOS, "obtenerDatos: se creo el call");
        try {
            usuarioRespuestaCall.enqueue(new Callback<UsuarioRespuesta>() {
                @Override
                public void onResponse(Call<UsuarioRespuesta> call, Response<UsuarioRespuesta> response) {
                    Log.i(TAG_USUARIOS, "onResponse: entro");
                    try {

                        if (response.isSuccessful()) {
                            UsuarioRespuesta usuarioRespuesta = response.body();


                            listaUsuario = usuarioRespuesta.getUsuarios();

                            if (listaUsuario != null) {
                                for (Usuario u : listaUsuario) {
                                    Log.i(TAG_USUARIOS, "Nombre de usuario " + u.getNombre() + " Contrasena " + u.getPass());
                                }
                            } else {
                                Log.e(TAG_USUARIOS, "onRespose: Lista nula");
                            }
                        } else {
                            Log.e(TAG_USUARIOS, "onResponse " + response.errorBody());
                        }
                    } catch (Exception e) {
                        Log.e(TAG_USUARIOS, "Se cayo por " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UsuarioRespuesta> call, Throwable t) {
                    Log.e(TAG_USUARIOS, "onFailure" + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e(TAG_USUARIOS, "Se cayo por " + e.getMessage());
        }
    }

}
