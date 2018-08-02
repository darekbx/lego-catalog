package com.legocatalog.data.local


import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded

data class SetCountEntity(
        @Embedded var set: SetEntity? = null,
        @ColumnInfo(name = "count") var count: Int = 0)