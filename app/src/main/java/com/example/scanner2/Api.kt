package com.example.appscanner

import com.example.scanner2.Disco
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("Nuevo")
    abstract fun NuevoDisco(@Body disc: Disco): Call<DiscoResponse>


    @GET("disco")
    fun disco(@Query ("Disc")disco:String):Call<DiscoResponse>

}

