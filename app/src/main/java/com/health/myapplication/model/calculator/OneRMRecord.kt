package com.health.myapplication.model.calculator

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "onermTable"
)
data class OneRMRecord(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") var id: Int?,
        @ColumnInfo(name ="exercise") var exercise: String,
        @ColumnInfo(name = "weight") var weight: Double,
        @ColumnInfo(name = "dateId") var dateId: Int?
) {
    constructor(exercise: String, weight: Double) : this(null, exercise,weight,null) {}
    constructor(exercise: String, weight: Double, dateId: Int) : this(null, exercise,weight,dateId) {}
    companion object {
        val NULL : OneRMRecord = OneRMRecord("NULL",0.0)
    }
}