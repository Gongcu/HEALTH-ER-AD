package com.health.myapplication.entity.one_rm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "caldateTable")
data class OneRmDate(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name ="_id") var id: Int?,
        @ColumnInfo(name ="date", defaultValue = "(datetime('now'))") var date: String?
) {
    constructor(date: String) : this(null,date) {}


}