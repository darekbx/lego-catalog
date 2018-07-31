package com.legocatalog.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface LegoDao {

    @Query("""
        SELECT *
        FROM `set`
        WHERE theme_id = :themeId
        """)
    fun fetchSets(themeId: Int): LiveData<List<SetEntity>>

    @Query("""
        SELECT p.*, sxp.quantity AS quantity
        FROM setxpart AS sxp
        INNER JOIN part AS p ON p.element_id = sxp.element_id
        WHERE sxp.set_id = :setId
        """)
    fun fetchParts(setId: Int): LiveData<List<PartQuantityEntity>>

    @Query("""
        SELECT *
        FROM `set`
        WHERE id = :setId
        """)
    fun fetchSet(setId: Int): LiveData<SetEntity>

    @Query("""
        SELECT COUNT(id)
        FROM part
        WHERE element_id = :elementId
        """)
    fun countByElementId(elementId: String): Long

    @Insert
    fun add(partEntity: PartEntity): Long

    @Insert
    fun add(setEntity: SetEntity): Long

    @Insert
    fun add(setXPartEntity: SetXPartEntity): Long
}