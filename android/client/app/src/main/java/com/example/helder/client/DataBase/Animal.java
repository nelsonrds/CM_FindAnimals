package com.example.helder.client.DataBase;

import java.util.ArrayList;

/**
 * Created by Nelson on 31/03/2017.
 */

public class Animal {
    private long animalId;
    private String animalName;
    private String ownerId;
    private Boolean treated;
    private ArrayList<Location> animalLocation;
    private Boolean isChecked;


    public Animal(){
        animalLocation = new ArrayList<>();
        isChecked = true;
    }

    public Animal(long id, String name, String owner, Boolean tretd, ArrayList<Location> location, Boolean isCheck){
        animalId = id;
        animalName = name;
        ownerId = owner;
        treated = tretd;
        isChecked = isCheck;
        if(location.isEmpty()){
            animalLocation = new ArrayList<>();
        }else{
            animalLocation = location;
        }
    }

    public long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(long animalId) {
        this.animalId = animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getTreated() {
        return treated;
    }

    public void setTreated(Boolean treated) {
        this.treated = treated;
    }

    public ArrayList<Location> getAnimalLocation() {
        return animalLocation;
    }

    public void setAnimalLocation(ArrayList<Location> animalLocation) {
        this.animalLocation = animalLocation;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
