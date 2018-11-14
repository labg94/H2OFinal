package com.example.luisa.h2o.api;

import com.example.luisa.h2o.model.Solicitud;
import com.example.luisa.h2o.model.UsuarioRespuesta;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UsuarioServicio {

    @GET("list")
    Call<UsuarioRespuesta> obtenerUsuarios(@Header("Authorization") String authHeader);


    @GET("asignarJ")
    Call<UsuarioRespuesta>  agregarSolicitud(@Header("Authorization") String authHeader, @Body Solicitud solicitud);
}
