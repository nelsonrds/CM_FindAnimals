package com.example.helder.client.DataBase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by nelso on 10/04/2017.
 */

public class Fence {
    private ArrayList<LatLng> pontos;

    public Fence(){
        this.pontos = new ArrayList<>();
    }

    public Fence(ArrayList<LatLng> pontos) {
        this.pontos = new ArrayList<>();
        this.pontos = pontos;
    }

    public ArrayList<LatLng> getPontos() {
        return pontos;
    }

    public void setPontos(ArrayList<LatLng> pontos) {
        this.pontos = pontos;
    }
}
