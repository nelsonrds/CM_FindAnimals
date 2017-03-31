package com.example.helder.client.Fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.helder.client.R;

/**
 * Created by Nelson on 24/03/2017.
 */

public class animalFragment extends android.support.v4.app.Fragment {

    ListView lista;
    Cursor c;
    SimpleCursorAdapter adapter;

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

        lista = (ListView)view.findViewById(R.id.animalList);


        return view;
    }
}
