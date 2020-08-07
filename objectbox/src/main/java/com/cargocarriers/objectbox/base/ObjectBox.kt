package com.cargocarriers.objectbox.base

import android.content.Context
import android.util.Log
import com.cargocarriers.objectbox.BuildConfig
import com.cargocarriers.objectbox.entities.trip.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser




object ObjectBox {
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()
        if (BuildConfig.DEBUG) {
            val started = AndroidObjectBrowser(boxStore).start(context)
            Log.i("ObjectBrowser", "Started: $started")
        }
    }
}