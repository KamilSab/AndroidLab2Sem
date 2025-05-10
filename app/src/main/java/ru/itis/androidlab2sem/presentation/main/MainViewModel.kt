package ru.itis.androidlab2sem.presentation.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.itis.androidlab2sem.domain.usecase.GetRandomCatUseCase
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import ru.itis.androidlab2sem.domain.model.CatResult
import ru.itis.androidlab2sem.domain.usecase.GetCatByTagUseCase

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRandomCatUseCase: GetRandomCatUseCase,
    private val getCatByTagUseCase: GetCatByTagUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state: StateFlow<MainState> = _state

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun loadRandomCat() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            runCatching {
                getRandomCatUseCase().use { responseBody ->
                    BitmapFactory.decodeStream(responseBody.byteStream())
                }
            }.fold(
                onSuccess = { bitmap ->
                    _state.value = if (bitmap != null) {
                        MainState.Success(bitmap, source = "API")
                    } else {
                        MainState.Error("Failed to decode image")
                    }
                },
                onFailure = { e ->
                    _state.value = MainState.Error(e.message ?: "Unknown error")
                }
            )
        }
    }

    fun loadCatByTag(tag: String) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            when (val result = getCatByTagUseCase(tag)) {
                is CatResult.Success -> {
                    _state.value = MainState.Success(
                        bitmap = result.bitmap,
                        source = if (result.fromCache) "Cache" else "API"
                    )
                    showToast("Данные из: ${if (result.fromCache) "кэша" else "API"}")
                }
                is CatResult.Error -> {
                    _state.value = MainState.Error(result.exception.message ?: "Ошибка")
                }
            }
        }
    }
}