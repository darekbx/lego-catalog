package com.legocatalog.di

import android.arch.persistence.room.Room
import android.content.Context
import com.legocatalog.data.local.LegoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesLegoDatabase(context: Context) =
            Room
                    .databaseBuilder(context.applicationContext, LegoDatabase::class.java, LegoDatabase.DB_NAME)
                    .allowMainThreadQueries()
                    .build()
}