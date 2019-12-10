package com.example.prikkie.RoomShoppingList;

import android.media.Image;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shoppinglistitem_table")
public class ShoppingListItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private double price;

    private String imageUrl;

    private Boolean isChecked;

    public int getId() {
        return id;
    }

    public ShoppingListItem(String title, double price, String imageUrl, Boolean isChecked) {
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isChecked = isChecked;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void  setIsChecked(Boolean bool){
        this.isChecked = bool;
    }

    public Boolean getIsChecked() {return isChecked;}

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }


}


