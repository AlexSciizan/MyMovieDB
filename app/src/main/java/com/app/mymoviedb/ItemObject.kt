package com.app.mymoviedb

import android.graphics.Bitmap

class ItemObject(val bitmap: Bitmap, private val isiList: Array<String>) {

    fun getItem(i: Int): String {
        return isiList[i]
    }
}