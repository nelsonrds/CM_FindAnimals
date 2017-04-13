package com.example.helder.client.Fragments;

import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.helder.client.DataBase.Animal;
import com.example.helder.client.DataBase.Location;
import com.example.helder.client.R;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import static com.example.helder.client.Fragments.animalFragment.lista;

/**
 * Created by Nelson on 24/03/2017.
 */

public class routeFragment extends android.support.v4.app.Fragment {

    private static View view;

    public routeFragment(){
    }

    public static routeFragment newInstance() {
        routeFragment fragment = new routeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.route_fragment, container, false);
        } catch (InflateException e) {

        }

        //Toast.makeText(getContext(), ""+lista.size() ,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), ""+lista.get(0).getAnimalLocation().size() ,Toast.LENGTH_SHORT).show();




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        for(int i = 0; i < lista.size(); i++){
            Toast.makeText(getContext(), "nome:21321 " + lista.get(i).getAnimalName() + " estado: " + lista.get(i).getChecked() ,Toast.LENGTH_SHORT).show();
        }
    }
}
