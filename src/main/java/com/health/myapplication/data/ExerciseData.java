package com.health.myapplication.data;

public class ExerciseData {
    String name;
    String desc;
    String tip;

    int set;
    int rep;

    public ExerciseData(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
    public ExerciseData(String name, int set, int rep) {
        this.name = name;
        this.set=set;
        this.rep=rep;
    }
    public ExerciseData(String name, String desc, String tip) {
        this.name = name;
        this.desc = desc;
        this.tip = tip;
    }
    public ExerciseData(String name, String desc, String tip, int set, int rep) {
        this.name = name;
        this.desc = desc;
        this.tip = tip;
        this.set=set;
        this.rep=rep;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public int getRep() {
        return rep;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }
}
