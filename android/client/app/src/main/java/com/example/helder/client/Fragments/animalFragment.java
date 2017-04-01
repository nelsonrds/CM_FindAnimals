package com.example.helder.client.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.helder.client.Adapter.MyAnimalListAdapter;
import com.example.helder.client.DataBase.Animal;
import com.example.helder.client.DataBase.Location;
import com.example.helder.client.R;
import com.example.helder.client.Utils;
import com.example.helder.client.WebServices.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nelson on 24/03/2017.
 */

public class animalFragment extends android.support.v4.app.Fragment {


    MyAnimalListAdapter adapter;
    public static ArrayList<Animal> lista;
    ArrayList<Location> loc;
    public ListView lv;


    private static View view;

    public animalFragment(){
    }

    public static animalFragment newInstance() {
        animalFragment fragment = new animalFragment();
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
            view = inflater.inflate(R.layout.animal_fragment, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        lista = new ArrayList<>();

        loc = new ArrayList<>();

        lv = (ListView)view.findViewById(R.id.animalList);


        adapter = new MyAnimalListAdapter(this.getContext(), lista);

        getAnimalsWS();

        return view;
    }

    private void getAnimalsWS(){
        String url = "http://eurogather.net:3000/api/animals";

        Toast.makeText(getContext(), "getting data...", Toast.LENGTH_SHORT).show();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // display response
                Log.d("Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        Singleton.getInstance(getContext()).addToRequestQeueu(getRequest);
    }


}
