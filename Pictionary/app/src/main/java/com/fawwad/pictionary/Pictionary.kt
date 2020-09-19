package com.fawwad.pictionary

import android.app.Application

class Pictionary : Application() {

    companion object {
        var instance: Pictionary? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}