package com.legocatalog.di

import android.content.Context
import com.legocatalog.LegoCatalogApp
import com.legocatalog.firebase.FirebaseAuthentication
import com.legocatalog.firebase.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: LegoCatalogApp) {

    @Provides
    @Singleton
    fun provideApplication(): LegoCatalogApp = application

    @Provides
    fun provideContext(): Context = application

    @Provides
    fun provideFirebaseAuthentication() = FirebaseAuthentication()

    @Provides
    fun provideFirebaseDatabase() = FirebaseDatabase()
}