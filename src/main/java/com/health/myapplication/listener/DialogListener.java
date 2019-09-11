package com.health.myapplication.listener;

public interface DialogListener {
    public void onPositiveClicked();
    public void onPositiveClicked(int date,String part,String exercise);
    public void onPositiveClicked(double weight,double height); //for bodyweight
    public void onPositiveClicked(String time, String name, int set,int rep); //for training data
    public void onNegativeClicked();
}
