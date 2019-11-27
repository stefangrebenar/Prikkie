package com.example.prikkie.ingredientDB;

public class Ingredient {
    public int Id;
    public String English;
    public String Dutch;
    public String Taxonomy;
    public boolean Checked;

    public Ingredient(){
    }
    public Ingredient(int id, String english, String dutch, boolean checked){
        Id = id;
        English = english;
        Dutch = dutch;
        Checked = checked;
    }

    public String GetLanguage(int i){
        switch(i) {
            case 0:
                return English;
            case 1:
                return Dutch;
        }
        return "Language not found";
    }
}
