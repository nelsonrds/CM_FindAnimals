package com.example.helder.client.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.helder.client.DataBase.Animal;
import com.example.helder.client.R;
import com.example.helder.client.Utils;
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
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

import static com.example.helder.client.Fragments.animalFragment.lista;
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
    Circle animalCircle;
    PolygonOptions polyOp;
    ArrayList<Circle> cir;

    ArrayList<LatLng> lastPosition;

    Handler ha;

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
        setHasOptionsMenu(true);
        //Botoes
        btclear = (ImageButton)view.findViewById(R.id.btclear);
        btclear.setVisibility(View.GONE);
        btclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pontos = new ArrayList<>();

                handler();
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
                polyOp = new PolygonOptions();
                polyOp.addAll(pontos);
                polyOp.strokeColor(Color.RED);
                polyOp.fillColor(Color.TRANSPARENT);
                nMap.addPolygon(polyOp);

                pontos = new ArrayList<>();
                for(int i = 0; i < cir.size() ; i++){
                    cir.get(i).remove();
                }
                existFence = true;
                handler();
            }
        });



        SupportMapFragment mMap = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapa));
        mMap.getMapAsync(this);

        polyOp = new PolygonOptions();

        pontos = new ArrayList<>();

        dots = new ArrayList<>();

        cir = new ArrayList<>();

        lastPosition = new ArrayList<>();

        checkIfNew = false;

        existFence = false;

        getFenceWS();


        return view;
    }

    private void handler(){
        ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                calltimer();
                Toast.makeText(getContext(), "mudou posiçao", Toast.LENGTH_SHORT).show();

                ha.postDelayed(this, 5000);
            }

        }, 5000);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
                ha.removeCallbacksAndMessages(null);
            }else{
                handler();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getContext(), "Resuming", Toast.LENGTH_SHORT).show();
        handler();
    }

    @Override
    public void onPause() {
        super.onPause();
       // Toast.makeText(getContext(), "Pausing", Toast.LENGTH_SHORT).show();
        ha.removeCallbacksAndMessages(null);
    }

    private void calltimer(){
        //your function
        //limpar ponto

        nMap.clear();

        nMap.addPolygon(polyOp);


                //chamar ws
                //desenhar circulo

        for(int i = 0; i < lastPosition.size() ; i++){
            //Toast.makeText(getContext(), lastPosition.get(i).toString(), Toast.LENGTH_SHORT).show();
            animalCircle = nMap.addCircle(new CircleOptions()
                    .center(new LatLng(lastPosition.get(i).latitude, lastPosition.get(i).longitude))
                    .radius(10)
                    .strokeColor(Color.GREEN)
                    .fillColor(Color.BLACK));
        }

        getLastPositionWS();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_mapfragment, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_deletefence:
                if(existFence){
                    nMap.clear();
                    existFence = false;
                    ha.removeCallbacksAndMessages(null);
                }
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // mapa
        nMap = googleMap;

        LatLng ltn = new LatLng(41.69621, -8.8430194);

        CameraPosition cm = new CameraPosition.Builder()
                .zoom(13)
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
            ha.removeCallbacksAndMessages(null);
        }
    }

    private void getFenceWS(){
        String url = Utils.URL_PRINCIPAL + "/api/getFence/";
        String pedido = url + UserID;
        pontos.clear();


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, pedido, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                try{
                    String res = response.getString("status");
                    JSONArray js = response.getJSONArray("coordenadas");

                    for(int i = 0; i < js.length(); i++){
                        JSONObject jo = js.getJSONObject(i);
                        Double latitude = Double.valueOf(jo.getString("latitude")).doubleValue();
                        Double longitude = Double.valueOf(jo.getString("longitude")).doubleValue();

                        LatLng coord = new LatLng(latitude, longitude);

                        pontos.add(coord);
                    }
                    polyOp.addAll(pontos);
                    polyOp.strokeColor(Color.RED);
                    polyOp.fillColor(Color.TRANSPARENT);
                    nMap.addPolygon(polyOp);

                    pontos.clear();
                    for(int i = 0; i < cir.size() ; i++){
                        cir.get(i).remove();
                    }
                    existFence = true;

                }catch (JSONException ex){}
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getContext(), error + " ola", Toast.LENGTH_SHORT).show();
            }

        });
        Singleton.getInstance(getContext()).addToRequestQeueu(getRequest);
    }

    private void sendFenceWS(){
        String url = Utils.URL_PRINCIPAL + "/api/user/addFence";

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
        );
        Singleton.getInstance(getContext()).addToRequestQeueu(putRequest);
    }

    private void getLastPositionWS(){
        String url = Utils.URL_PRINCIPAL + "/api/getAnimalsFollowingLocation";

        JSONObject JO = new JSONObject();
        try{
            JO.put("idUser", UserID);
        }catch(Exception io){

        }
        lastPosition = new ArrayList<>();

        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.POST, url, JO,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        try{
                            JSONArray ja = response.getJSONArray("animals");
                            for(int i = 0; i < ja.length() ; i++){
                                JSONObject jo = ja.getJSONObject(i);

                                Animal ws = new Animal();
                                ws.setAnimalName(jo.getString("nome"));
                                ws.setAnimalId(jo.getString("id"));

                                for(int j = 0 ; j < lista.size(); j++){

                                    Animal loc = lista.get(j);

                                    //Toast.makeText(getContext(), loc.getAnimalId() + " = " + ws.getAnimalId(),Toast.LENGTH_SHORT).show();
                                    if(loc.getAnimalId().equals(ws.getAnimalId())){

                                        if(loc.getChecked()){

                                            //desenha
                                            JSONObject last = jo.getJSONObject("lastLocation");
                                            double lat = Double.valueOf(last.getString("latitude"));
                                            double lng = Double.valueOf(last.getString("longitude"));
                                            LatLng nova = new LatLng(lat,lng);
                                            lastPosition.add(nova);
                                            //Toast.makeText(getContext(), lastPosition.size() + "",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{

                                    }
                                }
                            }


                        }catch(Exception ex){
                            Toast.makeText(getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                    }
                }
        );
        Singleton.getInstance(getContext()).addToRequestQeueu(putRequest);
    }

}