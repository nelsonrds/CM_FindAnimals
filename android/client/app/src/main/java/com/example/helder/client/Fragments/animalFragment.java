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
import com.google.android.gms.maps.MapView;

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
        View view = inflater.inflate(R.layout.animal_fragment, container, false);

        /*if(view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.animal_fragment, container, false);
        } catch (InflateException e) {
             //map is already there, just return view as it is
        }*/

        lista = new ArrayList<>();

        loc = new ArrayList<>();

        lv = (ListView)view.findViewById(R.id.animalList);

        getAnimalsWS();



        return view;
    }



    private void getAnimalsWS(){
        final String url = "http://eurogather.net:3000/api/animals";

        //Toast.makeText(getContext(), "getting data...", Toast.LENGTH_SHORT).show();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // display response
                lista.clear();
                loc.clear();

                try{
                    //Toast.makeText(getContext(), ""+response.getString(an), Toast.LENGTH_LONG).show();
                    JSONArray ja = response.getJSONArray("animals");
                    for(int i = 0 ; i < ja.length(); i++){
                        JSONObject jobj = ja.getJSONObject(i);

                        Animal aux = new Animal();
                        aux.setAnimalName(jobj.getString("nome"));
                        aux.setOwnerId(jobj.getString("owner"));
                        aux.setChecked(true);

                        JSONArray jaLocation = jobj.getJSONArray("location");
                        for(int j = 0; j < jaLocation.length(); j++){
                            JSONObject jobjLoc = jaLocation.getJSONObject(j);
                            Location auxLocation = new Location(jobjLoc.getString("latitude"),jobjLoc.getString("longitude"));
                            loc.add(auxLocation);
                        }

                        aux.setAnimalLocation(loc);
                        lista.add(aux);
                    }

                    adapter = new MyAnimalListAdapter(getContext(), lista);

                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Animal animal = (Animal)parent.getItemAtPosition(position);
                            Toast.makeText(getContext(), animal.getAnimalName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch(JSONException ex){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Singleton.getInstance(getContext()).addToRequestQeueu(getRequest);
    }


}
