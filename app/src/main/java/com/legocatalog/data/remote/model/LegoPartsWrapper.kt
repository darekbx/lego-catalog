package com.legocatalog.data.remote.model

data class LegoPartsWrapper(
        val count: Int = 0,
        val next: String = "",
        val previous: String = "",
        val results: List<LegoSetPart>? = null
)