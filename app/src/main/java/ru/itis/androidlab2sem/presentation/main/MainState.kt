package ru.itis.androidlab2sem.presentation.main

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable

@Immutable
sealed class MainState {
    @Immutable
    object Loading : MainState()

    @Immutable
    data class Success(
        val bitmap: Bitmap,
        val source: String
    ) : MainState()

    @Immutable
    data class Error(val message: String) : MainState()
}