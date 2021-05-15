package com.health.myapplication.etc

import android.content.Context
import android.widget.ArrayAdapter

class HintSpinnerAdapter(val mContext: Context, val resource: Int, val list: List<String>): ArrayAdapter<String>(mContext, resource, list) {
    override fun getCount(): Int {
        // don't display last item. It is used as hint.
        val count = super.getCount()
        return if (count > 0) count - 1 else count
    }
}