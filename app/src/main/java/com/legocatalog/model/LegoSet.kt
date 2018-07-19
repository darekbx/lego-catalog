package com.legocatalog.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LegoSet(
        @SerializedName("set_num") val number: String = "",
        val name: String = "",
        val year: String = "",
        var themeId: Int = 0,
        @SerializedName("num_parts") val partsCount: String = "",
        @SerializedName("set_img_url") val imageUrl: String = ""
): Serializable {

    fun toMap() = HashMap<String, Any>()
            .apply {
                put("set_num", number)
                put("name", name)
                put("year", year)
                put("theme_id", themeId)
                put("num_parts", partsCount)
                put("set_img_url", imageUrl)
            }

    companion object {
        fun fromMap(map: Map<String, Any>) = LegoSet(
                map["set_num"] as String,
                map["name"] as String,
                map["year"] as String,
                map["theme_id"] as Int,
                map["num_parts"] as String,
                map["set_img_url"] as String
        )
    }

    enum class Theme {
        DUPLO,
        CITY,
        TECHNIC
    }
}