package com.example.prikkie;

public class ExampleItem {
    private String mImageResource;
    private String mTopText;
    private String mBottomText;
    private Boolean isChecked;

    public ExampleItem(String imageResource, String topText, String bottomText, Boolean isChecked) {
        mImageResource = imageResource;
        mTopText = topText;
        mBottomText = bottomText;
        this.isChecked = isChecked;
    }

    public ExampleItem(String imageResource, String topText, String bottomText) {
        mImageResource = imageResource;
        mTopText = topText;
        mBottomText = bottomText;
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
