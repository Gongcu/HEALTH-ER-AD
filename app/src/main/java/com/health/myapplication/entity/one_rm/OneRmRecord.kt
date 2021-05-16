package com.health.myapplication.entity.one_rm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "onermTable"
)
data class OneRmRecord(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") var id: Int?,
        @ColumnInfo(name ="exercise") var exercise: String,
        @ColumnInfo(name = "weight") var weight: Double,
        @ColumnInfo(name = "dateId") var dateId: Int?
) {
    constructor(exercise: String, weight: Double) : this(null, exercise,weight,null) {}
    constructor(exercise: String, weight: Double, dateId: Int) : this(null, exercise,weight,dateId) {}
    companion object {
        val NULL : OneRmRecord = OneRmRecord("NULL",0.0)
    }
}