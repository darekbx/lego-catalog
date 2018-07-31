package com.legocatalog.data.remote.model

import com.google.gson.annotations.SerializedName

data class LegoSetPart(
        val id: String = "",
        @SerializedName("inv_part_id") val inventoryPartId: String = "",
        @SerializedName("set_num") val setNumber: String = "",
        val quantity: Int = 0,
        @SerializedName("is_spare") val isSpare: Boolean = false,
        @SerializedName("element_id") val elementId: String = "",
        @SerializedName("num_sets") val numSets: Int = 0,
        val part: LegoPart? = null,
        val color: LegoColor? = null
)