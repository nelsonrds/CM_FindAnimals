package com.example.helder.client.Fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helder.client.R;
import com.google.android.gms.drive.internal.StringListResponse;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.helder.client.MainActivity;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by Nelson on 24/03/2017.
 */

public class mapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    ArrayList<LatLng> pontos;
    GoogleMap nMap;
    ImageButton btclear;
    ImageButton btcheck;

    ArrayList<LatLng> dots;

    public mapFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static mapFragment newInstance() {
        mapFragment fragment = new mapFragment();
        return fragment;
    }
    private static View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
            }
        try {
            view = inflater.inflate(R.layout.map_fragment, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        //Botoes
        btclear = (ImageButton)view.findViewById(R.id.btclear);
        btclear.setVisibility(View.GONE);
        btclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Limpar", Toast.LENGTH_SHORT).show();
            }
        });

        btcheck = (ImageButton)view.findViewById(R.id.btcheck);
        btcheck.setVisibility(View.GONE);
        btcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Aceitar", Toast.LENGTH_SHORT).show();
            }
        });



        SupportMapFragment mMap = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapa));
        mMap.getMapAsync(this);

        pontos = new ArrayList<>();

        dots = new ArrayList<>();

        LatLng novo = new LatLng(41.69430348, -8.84685147);
        LatLng novo2 = new LatLng(41.69430348, -8.84685147);
        LatLng novo3 = new LatLng(41.69430883, -8.84684781);
        LatLng novo4 = new LatLng(41.69431187, -8.84684867);

        dots.add(novo);
        dots.add(novo2);
        dots.add(novo3);
        dots.add(novo4);


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // mapa
        nMap = googleMap;

        nMap.addMarker(new MarkerOptions()
                .position(new LatLng(41.1, -8.2))
                .title("Marker"));
        //Toast.makeText(, "onMapReady", Toast.LENGTH_SHORT).show();

        for(int i = 0 ; i < 4; i ++){
            nMap.addMarker(new MarkerOptions().position(dots.get(i)));
        }

        LatLng ltn = new LatLng(41.6, -8.84);

        CameraPosition cm = new CameraPosition.Builder()
                .zoom(15)
                .target(ltn)
                .build();

        nMap.animateCamera(CameraUpdateFactory.newCameraPosition(cm));

        //eventos de click
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);

    }

    @Override
    public void onMapClick(LatLng latLng) {

        nMap.addMarker(new MarkerOptions().position(latLng));

        //Botoes
        btclear.setVisibility(View.VISIBLE);
        btcheck.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        pontos.add(latLng);
        nMap.addMarker(new MarkerOptions().position(latLng));
        if(pontos.size() == 4){
            PolygonOptions polyOp = new PolygonOptions();
            polyOp.addAll(pontos);
            polyOp.strokeColor(Color.RED);
            polyOp.fillColor(Color.TRANSPARENT);
            nMap.addPolygon(polyOp);
            pontos = new ArrayList<>();
        }
    }

    public void cancelPoly(View view){

    }

}