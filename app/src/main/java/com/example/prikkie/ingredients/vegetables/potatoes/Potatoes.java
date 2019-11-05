package com.example.prikkie.ingredients.vegetables.potatoes;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class Potatoes extends IngredientClassificationComponent {
    public void Potatoes(){
        English = "Potatoes";
        Dutch = "Aardappelen";

        Add(new Potato());
        Add(new Taro());
        Add(new SweetPotato());
        Add(new Yam());
        Add(new Konjac());
    }
}
