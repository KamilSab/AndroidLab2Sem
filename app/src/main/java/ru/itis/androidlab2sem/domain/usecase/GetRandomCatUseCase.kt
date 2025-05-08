package ru.itis.androidlab2sem.domain.usecase

import jakarta.inject.Inject
import okhttp3.ResponseBody
import ru.itis.androidlab2sem.domain.repository.CatRepository
import java.io.IOException

class GetRandomCatUseCase @Inject constructor(
    private val catRepository: CatRepository
) {
    suspend operator fun invoke(): ResponseBody {
        val response = catRepository.getRandomCat()
        if (!response.isSuccessful || response.body() == null) {
            throw IOException("Server error: ${response.code()}")
        }
        return response.body()!!
    }
}
