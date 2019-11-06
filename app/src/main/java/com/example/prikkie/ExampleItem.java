package com.example.prikkie;

import android.widget.CheckBox;

public class ExampleItem {
    private String mImageResource;
    private String mText1;
    private String mText2;
    private CheckBox mCheckbox;

    public ExampleItem(String imageResource, String text1, String text2, CheckBox checkBox) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        mCheckbox = checkBox;
    }

    public void changeText1(String text){
        mText1 = text;
    }

    public  void  flipCheckBox(){
        
    }

    public String getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }
}
