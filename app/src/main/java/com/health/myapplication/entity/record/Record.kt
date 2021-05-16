package com.health.myapplication.entity.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exerciseNote")
data class Record(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name ="_id") var id: Int?,
        @ColumnInfo(name ="exercisename") var exercisename: String,
        @ColumnInfo(name ="rep") var rep: Int,
        @ColumnInfo(name ="settime") var set: Int,
        @ColumnInfo(name ="weight") var weight: Double,
        @ColumnInfo(name ="dateid") var dateid: Int?
) {
    constructor(exercisename: String, rep: Int, set: Int,weight: Double) : this(null,exercisename,rep,set,weight,null) {}
}