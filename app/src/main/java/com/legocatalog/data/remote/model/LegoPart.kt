package com.legocatalog.data.remote.model

import com.google.gson.annotations.SerializedName

data class LegoPart(
        @SerializedName("part_num") val partNumber: String = "",
        val name: String = "",
        @SerializedName("part_cat_id") val partCatalogId: Int = 0,
        @SerializedName("part_img_url") val partImageUrl: String = "",
        @SerializedName("part_url") val partUrl: String = ""
        ) {

}