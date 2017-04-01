package com.example.helder.client.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.helder.client.DataBase.Animal;
import com.example.helder.client.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Nelson on 31/03/2017.
 */

public class MyAnimalListAdapter extends ArrayAdapter<Animal> {
    private Context mContext;
    private ArrayList<Animal> animals;

    public MyAnimalListAdapter(Context context, ArrayList<Animal> list){
        super(context, 0, list);
        mContext = context;
        this.animals = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Animal anim = this.animals.get(position);

        convertView = LayoutInflater.from(this.mContext).inflate(R.layout.listanimals_row, null);

        CheckBox check1 = (CheckBox)convertView.findViewById(R.id.checkBox);
        check1.setChecked(anim.getChecked());

        TextView txv1 = (TextView)convertView.findViewById(R.id.animalName);
        txv1.setText(anim.getAnimalName());

        TextView txv2 = (TextView)convertView.findViewById(R.id.ownerAnimal);
        txv2.setText(anim.getOwnerId());

        return convertView;
    }
}
