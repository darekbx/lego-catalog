package com.legocatalog.ui.model

data class Part(
        var quantity: Int = 0,
        var numSets: Int = 0,
        var colorName: String? = "",
        var name: String? = null,
        var partNumber: String? = null,
        var partImgUrl: String? = null
) 