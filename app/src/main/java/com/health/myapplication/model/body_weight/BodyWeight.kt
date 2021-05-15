package com.health.myapplication.model.body_weight

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weightTable")
data class BodyWeight(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name ="_id") var id: Int?,
        @ColumnInfo(name ="weight" )
        var bodyweight: Double,
        @ColumnInfo(name ="date", defaultValue = "(datetime('now','localtime'))")
        var date: String?
) {
    constructor(weight: Double,date: String) : this(null,weight,date) {}
}