package com.legocatalog.data.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.legocatalog.data.remote.model.LegoSet
import com.legocatalog.ui.model.SetInfo

@Entity(tableName = "set")
data class SetEntity(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "name") var name: String? = null,
        @ColumnInfo(name = "number") var number: String? = null,
        @ColumnInfo(name = "parts_count") var partsCount: String? = null,
        @ColumnInfo(name = "theme_id") var themeId: Int = 0,
        @ColumnInfo(name = "year") var year: String? = null,
        @ColumnInfo(name = "image_url") var imageUrl: String? = null
) {

    companion object {
        fun mapLegoSetToEntity(legoSet: LegoSet): SetEntity {
            with(legoSet) {
                return SetEntity(
                        null,
                        name,
                        number,
                        partsCount,
                        themeId,
                        year,
                        imageUrl
                )
            }
        }

        fun fromLegoSet(setInfo: LegoSet) =
                SetInfo(setInfo.number, setInfo.name, setInfo.year,
                        setInfo.themeId, setInfo.partsCount, setInfo.imageUrl)
    }
}