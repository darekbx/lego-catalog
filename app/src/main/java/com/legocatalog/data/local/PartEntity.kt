package com.legocatalog.data.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.legocatalog.data.remote.model.LegoSetPart
import com.legocatalog.ui.model.Part

@Entity(tableName = "part")
data class PartEntity(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "set_num") var setNumber: String? = null,
        @ColumnInfo(name = "quantity") var quantity: Int = 0,
        @ColumnInfo(name = "is_spare") var isSpare: Boolean = false,
        @ColumnInfo(name = "num_sets") var numSets: Int = 0,
        @ColumnInfo(name = "element_id") var elementId: String? = null,

        @ColumnInfo(name = "color_name") var colorName: String? = "",
        @ColumnInfo(name = "color_rgb") var colorRgb: String? = "",
        @ColumnInfo(name = "is_trans") var isTransparent: Boolean = false,

        @ColumnInfo(name = "name") var name: String? = null,
        @ColumnInfo(name = "part_num") var partNumber: String? = null,
        @ColumnInfo(name = "part_img_url") var partImgUrl: String? = null
) {

    companion object {
        fun mapLegoPartToEntity(legoSetPart: LegoSetPart): PartEntity {
            with(legoSetPart) {
                return PartEntity(
                        null,
                        setNumber,
                        quantity,
                        isSpare,
                        numSets,
                        elementId,
                        color?.name,
                        color?.rgb,
                        color?.isTransparent ?: false,
                        part?.name,
                        part?.partNumber,
                        part?.partImageUrl
                )
            }
        }

        fun mapToPart(partEntity: PartEntity): Part {
            with(partEntity) {
                return Part(
                        quantity,
                        numSets
                        , colorName,
                        name,
                        partNumber,
                        partImgUrl
                )
            }
        }
    }
}