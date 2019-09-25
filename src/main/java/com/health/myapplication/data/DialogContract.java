package com.health.myapplication.data;

import android.provider.BaseColumns;

public class DialogContract {
    private int click;

    public DialogContract(int id) {
        this.click = id;
    }
    private DialogContract(){}
    //BaseColumn 인터페이스는 자동으로 _ID 고유 기본기 생성
    public static final class Entry implements BaseColumns {
        public static final String TABLE_NAME="alert";
        public static final String COLUMN_CLICK="state";
    }

    public long getId() {
        return click;
    }

    public void setId(int id) {
        this.click = id;
    }

}
