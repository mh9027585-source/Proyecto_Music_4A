package com.example.proyecto_grupal_music.network
import com.example.proyecto_grupal_music.model.ITunesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ITunesApi {

    @GET("search")
    fun obtenerAlbumGenero(
        @Query("term") genero: String,
        @Query("country") pais: String,
        @Query("media") media: String,
        @Query("entity") entidad: String,
        @Query("limit") limite: Int
    ): retrofit2.Call<ITunesResponse>


}