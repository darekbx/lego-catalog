package com.legocatalog.model

data class LegoPartsWrapper(
        val count: Int = 0,
        val next: String = "",
        val previous: String = "",
        val results: List<LegoSetPart>? = null
)