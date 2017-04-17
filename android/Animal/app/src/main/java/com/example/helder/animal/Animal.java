package com.example.helder.animal;

import java.util.ArrayList;

/**
 * Created by nelso on 17/04/2017.
 */

public class Animal {
    private String animalId;
    private String animalName;

    public Animal(){

    }

    public Animal(String id, String name){
        animalId = id;
        animalName = name;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }
}
