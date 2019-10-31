package com.example.prikkie.ingredients.sugarSources;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class SugarSources extends IngredientClassificationComponent {
    public void SugarSources(){
        English = "Sugar Sources";
        Dutch = "Suikerbronnen";

        Add(new SugarCane());
        Add(new SugarBeet());
    }
}
