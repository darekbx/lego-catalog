package com.legocatalog.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface LegoDao {

    @Query("SELECT * FROM part WHERE set_num = :setNumber")
    fun fetch(setNumber: String): LiveData<List<PartEntity>>

    @Insert
    fun add(partEntity: PartEntity): Long
}