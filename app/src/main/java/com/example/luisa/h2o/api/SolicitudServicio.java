package com.example.luisa.h2o.api;

import com.example.luisa.h2o.model.Solicitud;
import com.example.luisa.h2o.model.SolicitudRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface SolicitudServicio {
    @GET("solicitudUsuario")
    Call<SolicitudRespuesta> obtenerDisponibles(@Header("Authorization") String authHeader);



}
