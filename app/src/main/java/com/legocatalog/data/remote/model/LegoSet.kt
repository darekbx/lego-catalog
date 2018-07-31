package com.legocatalog.data.remote.model

import com.google.gson.annotations.SerializedName

data class LegoSet(
        @SerializedName("set_num") val number: String? = "",
        val name: String? = "",
        val year: String? = "",
        var themeId: Int = 0,
        @SerializedName("num_parts") val partsCount: String? = "",
        @SerializedName("set_img_url") val imageUrl: String? = ""
)