package ru.itis.androidlab2sem.domain.cache

import android.graphics.Bitmap
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject

@ViewModelScoped
class CatCache @Inject constructor(
    private val cacheDurationMinutes: Long
) {
    private val cache = mutableMapOf<String, CacheEntry>()

    data class CacheEntry(
        val bitmap: Bitmap,
        val timestamp: Long,
        var requestsBetween: Int = 0
    )

    fun get(key: String): Bitmap? {
        return cache[key]?.let { entry ->
            val isExpired = System.currentTimeMillis() - entry.timestamp > cacheDurationMinutes * 60 * 1000
            when {
                isExpired -> null
                entry.requestsBetween >= 3 -> null
                else -> entry.bitmap
            }
        }
    }

    fun put(key: String, bitmap: Bitmap) {
        cache.values.forEach { it.requestsBetween++ }
        cache[key] = CacheEntry(
            bitmap = bitmap,
            timestamp = System.currentTimeMillis(),
            requestsBetween = 0
        )
    }

    fun clear() {
        cache.clear()
    }
}
