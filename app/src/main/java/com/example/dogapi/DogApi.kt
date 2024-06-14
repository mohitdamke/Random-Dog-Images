package com.example.dogapi

import retrofit2.Response
import retrofit2.http.GET

interface DogApi {

    @GET("api/breeds/image/random")
    suspend fun getDogResult(): Response<allRandomDog>

    companion object{
        const val BASE_URL = "https://dog.ceo/"
    }
}