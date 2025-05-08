package ru.itis.androidlab2sem.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CatApiService {
    @GET("cat")
    suspend fun getRandomCat(): Response<ResponseBody>

    @GET("cat/{tag}")
    suspend fun getCatByTag(@Path("tag") tag: String): Response<ResponseBody>
}