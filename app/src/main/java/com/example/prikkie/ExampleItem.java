package com.example.prikkie;

public class ExampleItem {
    private String mImageResource;
    private String mTopText;
    private String mBottomText;
    private Boolean isChecked;

    public ExampleItem(String imageResource, String text1, String text2, Boolean isChecked) {
        mImageResource = imageResource;
        mTopText = text1;
        mBottomText = text2;
        this.isChecked = isChecked;
    }

    public void changeText1(String text){
        mTopText = text;
    }

    public  void  flipCheckBox(){
        isChecked = !isChecked;
    }

    public String getImageResource() {
        return mImageResource;
    }

    public String getTopText() {
        return mTopText;
    }

    public String getBottomText() { return mBottomText; }

    public Boolean getChecked(){
        return isChecked;
    }
}
