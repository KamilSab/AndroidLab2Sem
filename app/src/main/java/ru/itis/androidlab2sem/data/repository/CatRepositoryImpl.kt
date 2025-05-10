package ru.itis.androidlab2sem.data.repository

import jakarta.inject.Inject
import okhttp3.ResponseBody
import retrofit2.Response
import ru.itis.androidlab2sem.data.api.CatApiService
import ru.itis.androidlab2sem.domain.repository.CatRepository
import java.io.IOException

class CatRepositoryImpl @Inject constructor(
    private val catApiService: CatApiService
) : CatRepository {
    override suspend fun getRandomCat(): Response<ResponseBody> {
        return catApiService.getRandomCat()
    }

    override suspend fun getCatByTag(tag: String): ResponseBody {
        val response = catApiService.getCatByTag(tag)
        if (!response.isSuccessful) throw IOException("Tag not found")
        return response.body() ?: throw IOException("Empty response")
    }
}