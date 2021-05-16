package com.health.myapplication.entity.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dateTable")
data class RecordDate(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name ="_id") var id: Int?,
        @ColumnInfo(name ="date", defaultValue = "(datetime('now'))") var date: String?
) {
    constructor(date: String) : this(null,date) {}
}