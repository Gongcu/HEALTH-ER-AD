package com.health.myapplication.listener

interface RecordDialogListener {
    fun onPositiveClicked(time: String, name: String, set: Int, rep: Int, weight: Double) //for training data
}