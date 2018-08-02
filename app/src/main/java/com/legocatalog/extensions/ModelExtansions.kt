package com.legocatalog.extensions

import com.legocatalog.data.local.PartEntity
import com.legocatalog.data.local.PartQuantityEntity
import com.legocatalog.data.local.SetCountEntity
import com.legocatalog.data.local.SetEntity
import com.legocatalog.data.remote.model.LegoSet
import com.legocatalog.data.remote.model.LegoSetPart
import com.legocatalog.ui.model.Part
import com.legocatalog.ui.model.SetInfo

fun LegoSet.toSetEntity() = SetEntity(null, name, number, partsCount, themeId, year, imageUrl)
fun LegoSet.toSetInfo() = SetInfo(-1, number, name, year, themeId, partsCount, imageUrl)

fun SetCountEntity.toSetInfo() = SetInfo(set?.id?.toInt() ?: 0, set?.number, set?.name,
        set?.year, set?.themeId ?: -1, set?.partsCount, set?.imageUrl, count)

fun SetEntity.toSetInfo() = SetInfo(id?.toInt() ?: 0, number, name, year, themeId ?: -1, partsCount, imageUrl)

fun SetInfo.toMap() = HashMap<String, Any>()
        .apply {
            put("number", number ?: "")
            put("name", name ?: "")
            put("year", year ?: "")
            put("themeId", themeId)
            put("partsCount", partsCount ?: 0)
            put("imageUrl", imageUrl ?: "")
        }

fun SetInfo.fromMap(map: Map<String, Any>) = SetInfo(
        -1,
        map["number"] as String,
        map["name"] as String,
        map["year"] as String,
        map["themeId"] as Int,
        map["partsCount"] as String,
        map["imageUrl"] as String
)

fun LegoSetPart.toPartEntity() = PartEntity(
        null,
        elementId,
        numSets,
        part?.name,
        part?.partNumber,
        part?.partImageUrl,
        color?.name,
        color?.rgb,
        color?.isTransparent ?: false
)

fun PartQuantityEntity.toPart() = Part(
        quantity,
        partEntity?.numSets ?: 0,
        partEntity?.colorName,
        partEntity?.name,
        partEntity?.partNumber,
        partEntity?.partImgUrl
)