package com.health.myapplication.listener

import com.health.myapplication.entity.one_rm.OneRmRecord

interface AdapterListener {
    fun deleteOneRMRecord(oneRMRecord: OneRmRecord)
    fun updateOneRMRecord(id: Int, weight: Double)
}