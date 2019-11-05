package com.example.prikkie.ingredients.vegetables;

import com.example.prikkie.ingredients.IngredientClassificationComponent;
import com.example.prikkie.ingredients.vegetables.compositeVegetables.CompositeVegetables;
import com.example.prikkie.ingredients.vegetables.cruciferousVegetables.CruciferousVegetables;
import com.example.prikkie.ingredients.vegetables.cucurbitaceousVegetables.CucurbitaceousVegetables;
import com.example.prikkie.ingredients.vegetables.legumes.Legumes;
import com.example.prikkie.ingredients.vegetables.liliaceousVegetables.LiliaceousVegetables;
import com.example.prikkie.ingredients.vegetables.potatoes.Potatoes;
import com.example.prikkie.ingredients.vegetables.solanaceousVegetables.SolanaceousVegetables;
import com.example.prikkie.ingredients.vegetables.sugarSources.SugarSources;
import com.example.prikkie.ingredients.vegetables.umbelliferousVegetables.UmbelliferousVegetables;

public class Vegetables extends IngredientClassificationComponent {
    public Vegetables(){
        English = "Vegetables";
        Dutch = "Groenten";

        //Unclassified
        Add(new BambooShoots());
        Add(new Ginger());
        Add(new Okra());

        //Classified
        Add(new Potatoes());
        Add(new SugarSources());
        Add(new CruciferousVegetables());
        Add(new CompositeVegetables());
        Add(new LiliaceousVegetables());
        Add(new UmbelliferousVegetables());
        Add(new SolanaceousVegetables());
        Add(new CucurbitaceousVegetables());
        Add(new Legumes());
    }
}
