package com.mohsen.nobka.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NobkaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //disable dark model
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}