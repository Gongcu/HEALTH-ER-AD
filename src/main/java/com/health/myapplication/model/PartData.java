package com.health.myapplication.model;

public class PartData implements Comparable<PartData>{
    private String partName;
    private int resId;
    public PartData(){}

    public PartData(String part_name, int resId) {
        this.partName = part_name;
        this.resId = resId;
    }

    public String getPartName(){
        return partName;
    }
    public int getPartActivity(){
        if(partName.equals("무분할"))
            return 1;
        else{
            String str = partName.substring(0,1);
            return Integer.parseInt(str);
        }
    }
    public void setPartName(String name){
        partName=name;
    }
    public int getResId(){
        return resId;
    }
    public void setResId(int id){
        resId=id;
    }

    @Override
    public int compareTo(PartData s) {
        if (this.getPartActivity()< s.getPartActivity()) {
            return -1;
        } else if (this.getPartActivity() > s.getPartActivity()) {
            return 1;
        }
        return 0;
    }
}
