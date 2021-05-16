package com.health.myapplication.entity.custom_program

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "program")
data class CustomProgram(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name ="_id") var id: Int?,
        @ColumnInfo(name ="activity" ) var activity: Int
) {
    constructor(activity: Int) : this(null,activity) {}
}