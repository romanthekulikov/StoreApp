package com.example.storeapp

import android.app.Application
import com.example.data.database.AppDatabase
import com.example.storeapp.injection.AppComponent
import com.example.storeapp.injection.DaggerAppComponent
import com.google.firebase.FirebaseApp

class StoreApp : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        appComponent = DaggerAppComponent.builder().build()
        AppDatabase.initDb(applicationContext)
        FirebaseApp.initializeApp(applicationContext)
        super.onCreate()
    }
}