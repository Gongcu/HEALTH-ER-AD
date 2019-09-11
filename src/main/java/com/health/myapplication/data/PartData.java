package com.health.myapplication.data;

public class PartData implements Comparable<PartData>{
    private String part_name;
    private int resId;
    public PartData(){}

    public PartData(String part_name, int resId) {
        this.part_name = part_name;
        this.resId = resId;
    }

    public String getPartName(){
        return part_name;
    }
    public int getPartActivity(){
        if(part_name.equals("무분할"))
            return 1;
        else{
            String str = part_name.substring(0,1);
            return Integer.parseInt(str);
        }
    }
    public void setPartName(String name){
        part_name=name;
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
