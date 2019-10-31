package com.example.prikkie.ingredients.legumes;

import com.example.prikkie.ingredients.IngredientClassificationComponent;
import com.example.prikkie.ingredients.legumes.beans.Beans;

public class Legumes extends IngredientClassificationComponent {
    public void Legumes(){
        English = "Legumes";
        Dutch = "peulvruchten";

        Add(new SoyBean());
        Add(new Beans());
        Add(new Peanut());
        Add(new Peas());
        Add(new BroadBean());
    }
}
