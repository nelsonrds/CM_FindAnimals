package com.example.helder.client.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helder.client.R;

/**
 * Created by Nelson on 24/03/2017.
 */

public class animalFragment extends android.support.v4.app.Fragment {
    public animalFragment(){
    }

    public static animalFragment newInstance() {
        animalFragment fragment = new animalFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.animal_fragment, container, false);

        return rootView;
    }
}
