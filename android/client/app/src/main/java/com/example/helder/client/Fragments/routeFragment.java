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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import static com.example.helder.client.Fragments.animalFragment.lista;

/**
 * Created by Nelson on 24/03/2017.
 */

public class routeFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    private static View view;
    GoogleMap nMap;

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


        SupportMapFragment mMap = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapa2));
        mMap.getMapAsync(this);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        for(int i = 0; i < lista.size(); i++){

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;

        LatLng ltn = new LatLng(41.69621, -8.8430194);

        CameraPosition cm = new CameraPosition.Builder()
                .zoom(15)
                .target(ltn)
                .build();

        nMap.animateCamera(CameraUpdateFactory.newCameraPosition(cm));
    }
}
