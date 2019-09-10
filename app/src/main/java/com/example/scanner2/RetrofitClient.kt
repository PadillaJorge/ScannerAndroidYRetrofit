package com.example.appscanner

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.google.gson.Gson
import com.google.gson.GsonBuilder


object RetrofitClient {
 val Base_URL = "https://intense-falls-32376.herokuapp.com/api/"
    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()

                .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()


    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Base_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).
                client(okHttpClient)



            .build()

        retrofit.create(Api::class.java)
    }
}