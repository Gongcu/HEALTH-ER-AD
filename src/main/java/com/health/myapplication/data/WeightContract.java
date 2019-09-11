package com.health.myapplication.data;

import android.provider.BaseColumns;

public class WeightContract implements Comparable<WeightContract>{
    private String date;
    private double weight;

    @Override
    public int compareTo(WeightContract o) {
        if (Integer.parseInt(this.date) < Integer.parseInt(o.getDate())) {
            return -1;
        } else if (Integer.parseInt(this.date) == Integer.parseInt(o.date)) {
            return 0;
        } else {
            return 1;
        }
    }

    public WeightContract(String date, double weight) {
        this.date = date;
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    private WeightContract(){}
    //BaseColumn 인터페이스는 자동으로 _ID 고유 기본기 생성
    public static final class WeightEntry implements BaseColumns {
        public static final String TABLE_NAME="weightTable";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_DATE = "date";
    }
}
