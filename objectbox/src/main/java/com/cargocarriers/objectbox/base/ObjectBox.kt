package com.cargocarriers.objectbox.base

import android.content.Context
import com.cargocarriers.objectbox.entities.trip.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox {
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
    }
}