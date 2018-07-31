package com.legocatalog

import android.app.Application
import com.facebook.stetho.Stetho
import com.legocatalog.di.AppComponent
import com.legocatalog.di.AppModule
import com.legocatalog.di.DaggerAppComponent

class LegoCatalogApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this);
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}