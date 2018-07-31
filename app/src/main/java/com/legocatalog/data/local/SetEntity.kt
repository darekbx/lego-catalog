package com.legocatalog.data.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "set")
data class SetEntity(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "name") var name: String? = null,
        @ColumnInfo(name = "number") var number: String? = null,
        @ColumnInfo(name = "parts_count") var partsCount: String? = null,
        @ColumnInfo(name = "theme_id") var themeId: Int = 0,
        @ColumnInfo(name = "year") var year: String? = null,
        @ColumnInfo(name = "image_url") var imageUrl: String? = null
)