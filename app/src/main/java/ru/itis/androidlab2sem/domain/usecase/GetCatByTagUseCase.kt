package ru.itis.androidlab2sem.domain.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import jakarta.inject.Inject
import ru.itis.androidlab2sem.domain.cache.CatCache
import ru.itis.androidlab2sem.domain.model.CatResult
import ru.itis.androidlab2sem.domain.repository.CatRepository
import java.io.IOException

class GetCatByTagUseCase @Inject constructor(
    private val repository: CatRepository,
    private val cache: CatCache
) {
    suspend operator fun invoke(tag: String): CatResult {
        return try {
            val cached = cache.get(tag)
            if (cached != null) {
                CatResult.Success(cached, fromCache = true)
            } else {
                val response = repository.getCatByTag(tag)
                val bitmap = BitmapFactory.decodeStream(response.byteStream())!!
                cache.put(tag, bitmap)
                CatResult.Success(bitmap, fromCache = false)
            }
        } catch (e: Exception) {
            CatResult.Error(e)
        }
    }
}