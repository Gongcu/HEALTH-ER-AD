package com.health.myapplication.model.etc

import android.os.Parcel
import android.os.Parcelable

data class ExerciseModel (
    val name: String,
    val desc: String,
    val tip: String,
    var set: Int,
    var rep: Int,
    var imageR: Int,
    var imageF: Int
): BaseModel(name,imageR), Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    constructor(name: String, desc: String, tip: String, imageR: Int, imageF: Int) : this(name,desc,tip,0,0, imageR, imageF) {}


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(desc)
        dest.writeString(tip)
        dest.writeInt(set)
        dest.writeInt(rep)
        dest.writeInt(imageR)
        dest.writeInt(imageF)
    }

    companion object CREATOR : Parcelable.Creator<ExerciseModel> {
        override fun createFromParcel(parcel: Parcel): ExerciseModel {
            return ExerciseModel(parcel)
        }

        override fun newArray(size: Int): Array<ExerciseModel?> {
            return arrayOfNulls(size)
        }
    }
}