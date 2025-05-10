package ru.itis.androidlab2sem.domain.repository

import okhttp3.ResponseBody
import retrofit2.Response

interface CatRepository {
    suspend fun getRandomCat(): Response<ResponseBody>
    suspend fun getCatByTag(tag: String): ResponseBody
}