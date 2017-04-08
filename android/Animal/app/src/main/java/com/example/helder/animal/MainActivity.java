package com.example.helder.animal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener listener;

    private double latitudeNow;
    private double longitudeNow;

    private EditText idAnimal;
    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvMsg;

    private Button btnLocalizar;
    private Button btnParar;

    final String urlInicial = "http://eurogather.net:3000/api/";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        idAnimal = (EditText) findViewById(R.id.etAnimalID);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        tvMsg.setVisibility(View.INVISIBLE);
        btnLocalizar = (Button) findViewById(R.id.btnLocalizar);
        btnParar = (Button) findViewById(R.id.btnStop);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                tvLatitude.setText(location.getLatitude()+"");
                tvLongitude.setText(location.getLongitude()+"");
                receiveCoordinates(location.getLongitude(),location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configure_button();
    }

    private void receiveCoordinates(final double longitude, double latitude) {
        longitudeNow = longitude;
        latitudeNow = latitude;

        String url = urlInicial + "updateAnimalLocation/"+idAnimal.getText().toString();
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                Log.d("Response", response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude", Double.toString(latitudeNow));
                params.put("longitude", Double.toString(longitudeNow));

                return params;
            }

        };
        MySingleton.getInstance(this).addToRequestQeueu(putRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    public void botaoStart(View v) {

        String animalAux = idAnimal.getText().toString();
        if (!animalAux.matches("")) {

            tvMsg.setText("Checking ID...");
            tvMsg.setVisibility(View.VISIBLE);

            idAnimal.setFocusable(false);
            idAnimal.setEnabled(false);
            btnLocalizar.setEnabled(false);



            checkAnimalExists(animalAux);
        } else {
            Toast.makeText(this,"ID not defined",Toast.LENGTH_LONG).show();
        }

    }

    private void checkAnimalExists(String animalID) {

        String url = urlInicial + "animalExists/"+animalID;
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            successCheckAnimalExists(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR|CheckAnimal",error.toString());
                    }
                }
        );
        MySingleton.getInstance(this).addToRequestQeueu(getRequest);
    }

    private void successCheckAnimalExists (JSONObject response) throws JSONException {
        String result = response.getString("exists");
        Boolean auxResult;

        if (result.equalsIgnoreCase("true") || result.equalsIgnoreCase("false")) {
            auxResult = Boolean.valueOf(result);

            if (auxResult) {
                animalChecked();
            } else {
                idAnimal.setFocusable(true);
                idAnimal.setEnabled(true);
                tvMsg.setText("Animal not Found");
                btnLocalizar.setEnabled(true);
            }
        }

    }

    private void animalChecked() {
        tvMsg.setText("Animal Found, sending coordinates");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
        }

        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }


    public void botaoStop(View v) {
        locationManager.removeUpdates(listener);
        //locationManager = null;

        idAnimal.setFocusable(true);
        idAnimal.setEnabled(true);
        tvMsg.setText("Animal not Found");
        btnLocalizar.setEnabled(true);
        tvMsg.setVisibility(View.INVISIBLE);
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
        }
    }
}

