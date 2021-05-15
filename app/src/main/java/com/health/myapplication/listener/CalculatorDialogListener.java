package com.health.myapplication.listener;

public interface CalculatorDialogListener {
    public void onPositiveClicked();
    public void onPositiveClicked(String time,String name, double weight);
    public void onNegativeClicked();
}
