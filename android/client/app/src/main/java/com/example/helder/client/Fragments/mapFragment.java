package com.example.helder.client.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.helder.client.R;
import com.example.helder.client.WebServices.Singleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.helder.client.MainActivity.UserID;

/**
 * Created by Nelson on 24/03/2017.
 */

public class mapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    ArrayList<LatLng> pontos;
    GoogleMap nMap;
    ImageButton btclear;
    ImageButton btcheck;

    ArrayList<LatLng> dots;

    Boolean checkIfNew;
    Boolean existFence;

    Circle circle;
    ArrayList<Circle> cir;

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
                pontos.clear();


            }
        });

        btcheck = (ImageButton)view.findViewById(R.id.btcheck);
        btcheck.setVisibility(View.GONE);
        btcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Aceitar", Toast.LENGTH_SHORT).show();
                btclear.setVisibility(View.GONE);
                btcheck.setVisibility(View.GONE);
                checkIfNew = false;
                sendFenceWS();
                Toast.makeText(getContext(),pontos.toString(), Toast.LENGTH_LONG).show();
                PolygonOptions polyOp = new PolygonOptions();
                polyOp.addAll(pontos);
                polyOp.strokeColor(Color.RED);
                polyOp.fillColor(Color.TRANSPARENT);
                nMap.addPolygon(polyOp);
                pontos.clear();
                for(int i = 0; i < cir.size() ; i++){
                    cir.get(i).remove();
                }
            }
        });



        SupportMapFragment mMap = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapa));
        mMap.getMapAsync(this);

        pontos = new ArrayList<>();

        dots = new ArrayList<>();

        cir = new ArrayList<>();

        checkIfNew = false;

        existFence = false;




        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // mapa
        nMap = googleMap;

        LatLng ltn = new LatLng(41.69621, -8.8430194);

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
        if(!existFence){
            if(checkIfNew){
                pontos.add(latLng);
                circle = nMap.addCircle(new CircleOptions()
                        .center(new LatLng(latLng.latitude, latLng.longitude))
                        .radius(10)
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE));
                cir.add(circle);
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if(!existFence){
            checkIfNew = true;
            pontos.add(latLng);
            btclear.setVisibility(View.VISIBLE);
            btcheck.setVisibility(View.VISIBLE);
            circle = nMap.addCircle(new CircleOptions()
                    .center(new LatLng(latLng.latitude, latLng.longitude))
                    .radius(10)
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE));
            cir.add(circle);
        }
    }

    private void getFenceWS(){
        String url = "";

//        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, )
    }

    private void getFenceLocal(){

    }

    private void sendFenceWS(){
        String url = "http://d3167cc6.ngrok.io/api/user/addFence";

        JSONArray jsonObjectMembers = new JSONArray();
        for (int i = 0; i < pontos.size(); i++) {
            try {
                JSONObject jo = new JSONObject();
                jo.put("latitude", String.valueOf(pontos.get(i).latitude));
                jo.put("longitude", String.valueOf(pontos.get(i).longitude));

                jsonObjectMembers.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONArray jsonObjectMembers2 = new JSONArray();
        for (int i = 0; i < pontos.size(); i++) {
            try {
                JSONObject jo2 = new JSONObject();
                jo2.put(String.valueOf(pontos.get(i).latitude), String.valueOf(pontos.get(i).longitude));
                jsonObjectMembers2.put(jo2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject JO = new JSONObject();
        try{
            JO.put("idUser", UserID);
            JO.put("coordenadas", jsonObjectMembers);
            JO.put("coordenadasXY", jsonObjectMembers2);
        }catch(Exception io){

        }


        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.POST, url, JO,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                    }
                }
        );/*{

            @Override
            protected Map<String, String> getParams()
            {

                JSONArray jsonObjectMembers = new JSONArray();
                for (int i = 0; i < pontos.size(); i++) {
                    try {
                        JSONObject jo = new JSONObject();
                        jo.put("latitude", String.valueOf(pontos.get(i).latitude));
                        jo.put("longitude", String.valueOf(pontos.get(i).longitude));

                        jsonObjectMembers.put(jo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONArray jsonObjectMembers2 = new JSONArray();
                for (int i = 0; i < pontos.size(); i++) {
                    try {
                        JSONObject jo2 = new JSONObject();
                        jo2.put(String.valueOf(pontos.get(i).latitude), String.valueOf(pontos.get(i).longitude));
                        jsonObjectMembers2.put(jo2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Map<String, String>  params = new HashMap<String, String>();
                params.put("idUser", UserID);
                params.put("coordenadas", jsonObjectMembers.toString());
                params.put("coordenadasXY", jsonObjectMembers2.toString());

                JSONObject JO = new JSONObject();
                try{
                    JO.put("idUser", UserID);
                    JO.put("coordenadas", jsonObjectMembers);
                    JO.put("coordenadasXY", jsonObjectMembers2);
                }catch(Exception io){

                }



                return params;
            }
        };*/
        Singleton.getInstance(getContext()).addToRequestQeueu(putRequest);
    }

}