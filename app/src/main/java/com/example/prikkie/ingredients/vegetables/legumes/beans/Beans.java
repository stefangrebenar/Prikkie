package com.example.prikkie.ingredients.vegetables.legumes.beans;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class Beans extends IngredientClassificationComponent {

    public Beans(){
        English = "Beans";
        Dutch = "Bonen";

        Add(new ButterBean());
        Add(new RedBean());
        Add(new Lentil());
        Add(new LimaBean());
        Add(new Pegia());
        Add(new Sultani());
        Add(new Sultapya());
        Add(new WhiteBean());
    }
}
