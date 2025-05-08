package ru.itis.androidlab2sem.presentation.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.itis.androidlab2sem.domain.usecase.GetRandomCatUseCase
import android.graphics.BitmapFactory
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.itis.androidlab2sem.domain.usecase.GetCatByTagUseCase

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRandomCatUseCase: GetRandomCatUseCase,
    private val getCatByTagUseCase: GetCatByTagUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state: StateFlow<MainState> = _state

    fun loadRandomCat() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            kotlin.runCatching {
                getRandomCatUseCase().use { responseBody ->
                    BitmapFactory.decodeStream(responseBody.byteStream())
                }
            }.fold(
                onSuccess = { bitmap ->
                    _state.value = if (bitmap != null) {
                        MainState.Success(bitmap)
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
            runCatching {
                getCatByTagUseCase(tag)
            }.fold(
                onSuccess = { bitmap ->
                    _state.value = MainState.Success(bitmap)
                },
                onFailure = { exception ->
                    _state.value = MainState.Error(exception.message ?: "Tag error")
                }
            )
        }
    }
}