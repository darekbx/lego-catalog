package com.legocatalog.di

import android.content.Context
import com.legocatalog.LegoCatalogApp
import com.legocatalog.data.repository.Repository
import com.legocatalog.data.remote.firebase.FirebaseAuthentication
import com.legocatalog.data.remote.firebase.FirebaseDatabase
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

    @Provides
    fun provideRepository(firebaseDatabase: FirebaseDatabase) = Repository(firebaseDatabase)
}