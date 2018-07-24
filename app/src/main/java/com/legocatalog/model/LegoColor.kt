package com.legocatalog.model

import com.google.gson.annotations.SerializedName

data class LegoColor(
        val name: String = "",
        val rgb: String = "",
        @SerializedName("is_trans") val isTransparent: Boolean = false
)