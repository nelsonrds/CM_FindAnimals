package com.example.helder.client.Adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helder.client.DataBase.Animal;
import com.example.helder.client.R;
import com.google.android.gms.vision.text.Text;

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


    /*@NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Animal anim = this.animals.get(position);

        convertView = LayoutInflater.from(this.mContext).inflate(R.layout.listanimals_row, null);


        CheckBox check1 = (CheckBox)convertView.findViewById(R.id.checkBox);
        check1.setChecked(anim.getChecked());

        TextView txv1 = (TextView)convertView.findViewById(R.id.animalName);
        txv1.setText(anim.getAnimalName());

        return convertView;
    }*/
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null){

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.listanimals_row, null);

            holder = new ViewHolder();
            holder.code = (TextView)convertView.findViewById(R.id.animalName);
            holder.name = (CheckBox)convertView.findViewById(R.id.checkBox);

            convertView.setTag(holder);

            holder.name.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Animal animal = (Animal) cb.getTag();
                    animal.setChecked(cb.isChecked());
                    Toast.makeText(getContext(), "clickou: " + animal.getAnimalName() + "  " + " estado: " + animal.getChecked().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        Animal animal = animals.get(position);
        holder.code.setText(animal.getAnimalName());
        holder.name.setChecked(animal.getChecked());

        holder.name.setTag(animal);

        return convertView;
    }

    public void checkButtonClick(){

    }


    private class ViewHolder{
        TextView code;
        CheckBox name;
    }

}
