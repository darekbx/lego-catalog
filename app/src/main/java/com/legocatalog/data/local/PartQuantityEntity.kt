package com.legocatalog.data.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded

data class PartQuantityEntity(
        @Embedded var partEntity: PartEntity? = null,
        @ColumnInfo(name = "quantity") var quantity: Int = 0)