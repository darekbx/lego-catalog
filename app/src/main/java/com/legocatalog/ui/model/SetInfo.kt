package com.legocatalog.ui.model

data class SetInfo(
        val id: Int = 0,
        val number: String? = "",
        val name: String? = "",
        val year: String? = "",
        var themeId: Int = 0,
        val partsCount: String? = "",
        val imageUrl: String? = "",
        val setsCount: Int = 0
) {

    val moreThanOne = setsCount > 1

    enum class Theme {
        DUPLO,
        CITY,
        TECHNIC
    }
}