package com.eesl.myapplication

import android.app.Application
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential

class MainApplication() : Application() {

    companion object {
        lateinit var googleCreds : GoogleAccountCredential

    }
    override fun onCreate() {
        super.onCreate()

    }
}