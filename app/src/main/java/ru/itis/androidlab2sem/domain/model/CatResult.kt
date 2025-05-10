package ru.itis.androidlab2sem.domain.model

import android.graphics.Bitmap

sealed class CatResult {
    data class Success(
        val bitmap: Bitmap,
        val fromCache: Boolean
    ) : CatResult()

    data class Error(
        val exception: Throwable
    ) : CatResult()
}