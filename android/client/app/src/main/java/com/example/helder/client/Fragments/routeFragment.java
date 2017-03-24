package com.example.helder.client.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helder.client.R;

/**
 * Created by Nelson on 24/03/2017.
 */

public class routeFragment extends android.support.v4.app.Fragment {

    public routeFragment(){
    }

    public static routeFragment newInstance() {
        routeFragment fragment = new routeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.route_fragment, container, false);

        return rootView;
    }
}
