package com.legocatalog.repository.remote.rebrickable

import com.legocatalog.model.LegoPartsWrapper
import com.legocatalog.model.LegoSet
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RebrickableService {

    @GET("/api/v3/lego/sets/{set_num}/")
    fun setByNumber(@Path("set_num") setNumber: String): Call<LegoSet>

    @GET("/api/v3/lego/sets/{set_num}/parts/")
    fun setParts(@Path("set_num") setNumber: String): Call<LegoPartsWrapper>
}