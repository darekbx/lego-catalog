package com.legocatalog.ui.model

import com.legocatalog.data.remote.model.LegoSet

data class SetInfo(
        val number: String = "",
        val name: String = "",
        val year: String = "",
        var themeId: Int = 0,
        val partsCount: String = "",
        val imageUrl: String = ""
) {

    fun toMap() = HashMap<String, Any>()
            .apply {
                put("number", number)
                put("name", name)
                put("year", year)
                put("themeId", themeId)
                put("partsCount", partsCount)
                put("imageUrl", imageUrl)
            }

    companion object {

        fun fromMap(map: Map<String, Any>) = SetInfo(
                map["number"] as String,
                map["name"] as String,
                map["year"] as String,
                map["themeId"] as Int,
                map["partsCount"] as String,
                map["imageUrl"] as String
        )
    }

    enum class Theme {
        DUPLO,
        CITY,
        TECHNIC
    }
}