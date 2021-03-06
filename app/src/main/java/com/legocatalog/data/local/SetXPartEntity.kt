package com.legocatalog.data.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "setxpart")
data class SetXPartEntity(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "set_id") var setId: Int = 0,
        @ColumnInfo(name = "element_id") var elementId: String? = null,
        @ColumnInfo(name = "quantity") var quantity: Int = 0
) {

}