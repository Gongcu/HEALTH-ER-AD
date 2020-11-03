package com.health.myapplication.model

class ExerciseModel (
    val name: String,
    val desc: String,
    val tip: String,
    var set: Int,
    var rep: Int,
    var imageR: Int,
    var imageF: Int
):BasicModel(name,imageR)
{
    constructor(name: String, desc: String, tip: String, imageR: Int, imageF: Int) : this(name,desc,tip,0,0, imageR, imageF) {}
}