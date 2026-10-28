package com.example.proyecto_grupal_music.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //colocar la base URL
    private const val BASE_URL ="https://itunes.apple.com/"

    //construit la url completa
    val api : ITunesApi by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }
}