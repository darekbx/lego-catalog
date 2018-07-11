package com.legocatalog.remote.rebrickable

import com.legocatalog.BuildConfig
import com.legocatalog.model.Set
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface RebrickableService {

    @GET("/api/v3/lego/sets/{set_num}/")
    fun setByNumber(@Path("set_num") setNumber: String): Call<Set>
}