package com.example.prikkie;

import android.widget.CheckBox;

public class ExampleItem {
    private String mImageResource;
    private String mText1;
    private String mText2;
    private Boolean isChecked;

    public ExampleItem(String imageResource, String text1, String text2, Boolean isChecked) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        this.isChecked = isChecked;
    }

    public void changeText1(String text){
        mText1 = text;
    }

    public  void  flipCheckBox(){
        isChecked = !isChecked;
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

    public Boolean getChecked(){
        return isChecked;
    }
}
