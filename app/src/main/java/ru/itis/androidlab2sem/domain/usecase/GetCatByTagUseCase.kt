package ru.itis.androidlab2sem.domain.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import jakarta.inject.Inject
import ru.itis.androidlab2sem.domain.repository.CatRepository
import java.io.IOException

class GetCatByTagUseCase @Inject constructor(
    private val repository: CatRepository
) {
    suspend operator fun invoke(tag: String): Bitmap {
        val response = repository.getCatByTag(tag)
        return BitmapFactory.decodeStream(response.byteStream())
            ?: throw IOException("Failed to decode bitmap")
    }
}