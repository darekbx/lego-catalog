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
) {

    fun toMap() = HashMap<String, Any>()
            .apply {
                put("id", id)
                put("inv_part_id", inventoryPartId)
                put("set_num", setNumber)
                put("quantity", quantity)
                put("is_spare", isSpare)
                put("element_id", elementId)
                put("num_sets", numSets)
            }

    companion object {
        fun fromMap(map: Map<String, Any>) = LegoSetPart(
                map["id"] as String,
                map["inv_part_id"] as String,
                map["set_num"] as String,
                map["quantity"] as Int,
                map["is_spare"] as Boolean,
                map["element_id"] as String,
                map["num_sets"] as Int
        )
    }
}