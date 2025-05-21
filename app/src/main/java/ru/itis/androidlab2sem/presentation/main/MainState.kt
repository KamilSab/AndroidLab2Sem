package ru.itis.androidlab2sem.presentation.main

import android.graphics.Bitmap

sealed class MainState {
    object Loading : MainState()
    data class Success(
        val bitmap: Bitmap,
        val source: String
    ) : MainState()
    data class Error(val message: String) : MainState()
}