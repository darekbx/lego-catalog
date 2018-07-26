package com.legocatalog.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(PartEntity::class), version = 1)
abstract class LegoDatabase: RoomDatabase() {

    companion object {
        val DB_NAME = "lego_catalog"
    }

    abstract fun getDao(): LegoDao
}