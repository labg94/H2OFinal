package com.example.luisa.h2o.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.luisa.h2o.Fragments.ActualFragment;
import com.example.luisa.h2o.Fragments.EmptyRequest;
import com.example.luisa.h2o.Fragments.HistoryFragment;
import com.example.luisa.h2o.Fragments.ProfileFragment;
import com.example.luisa.h2o.Fragments.RequestFragment;
import com.example.luisa.h2o.LoginActivity;
import com.example.luisa.h2o.MainActivity;
import com.example.luisa.h2o.R;
import com.example.luisa.h2o.model.Solicitud;

public class TabAdapter extends FragmentStatePagerAdapter {

    Context context;
    String[] tabs;


    public TabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.tabs = this.context.getResources().getStringArray(R.array.tabs);
    }

    @Override
    public Fragment getItem(int position) {

        boolean actual = false;


        for (Solicitud s:
                LoginActivity.usuarioActual.getSolicitudList()) {
                if(s.getState().equalsIgnoreCase("actual")){
                    actual= true;
                    break;
                }
        }

        switch (position){
            case 0: return  new HistoryFragment();
            case 1: return new ProfileFragment();
            case 2: return new ActualFragment();
            case 3: if (actual){
                return new EmptyRequest();
            }else{
                return new RequestFragment();
            }

            default: return new ProfileFragment();
        }
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
