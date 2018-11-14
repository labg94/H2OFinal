package com.example.luisa.h2o.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.luisa.h2o.LoginActivity;
import com.example.luisa.h2o.R;
import com.example.luisa.h2o.model.Usuario;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment,container,false);

        TextView rut = view.findViewById(R.id.user_id);
        TextView name = view.findViewById(R.id.user_name);
        TextView mail = view.findViewById(R.id.user_mail);
        TextView phone = view.findViewById(R.id.user_phone);
        RatingBar score = view.findViewById(R.id.ratingBar);


        Usuario usuario = LoginActivity.usuarioActual;

        rut.setText(usuario.getRut());
        name.setText(usuario.getNombre());
        mail.setText(usuario.getMail());
        phone.setText(usuario.getPhone());
        score.setRating(usuario.getScore());

        return view;
    }

}
