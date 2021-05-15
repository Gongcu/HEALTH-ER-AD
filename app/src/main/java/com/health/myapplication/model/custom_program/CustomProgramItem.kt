package com.health.myapplication.model.custom_program

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.ads.mediation.MediationExtrasReceiver
import java.sql.Timestamp
import java.util.*

@Entity(tableName = "program")
data class CustomProgramItem(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name ="_id") var id: Int?,
        @ColumnInfo(name ="activity") var activity: Int,
        @ColumnInfo(name ="divide") var divide: Int,
        @ColumnInfo(name ="part") var part: String,
        @ColumnInfo(name ="exercise") var exercise: String,
        @ColumnInfo(name ="itemorder") var itemorder: Int,
        @ColumnInfo(name ="settime") var settime: Int,
        @ColumnInfo(name ="rep") var rep: Int
        ) {
    constructor(activity: Int,divide: Int,part: String, exercise: String,itemorder: Int,settime: Int,rep: Int)
            : this(null,activity,divide,part,exercise,itemorder,settime,rep) {}
}