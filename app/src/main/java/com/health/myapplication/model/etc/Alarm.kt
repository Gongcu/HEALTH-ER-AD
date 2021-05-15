package com.health.myapplication.model.etc

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")
data class Alarm(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name ="_id") var id: Int?,
        @ColumnInfo(name ="minute")
        var minute: Int,
        @ColumnInfo(name ="second")
        var second: Int
) {
    constructor(minute: Int,second: Int) : this(null,minute,second) {}
}