package ru.itis.androidlab2sem.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CatItemShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = shimmerBrush(
                        targetValue = 1300f,
                        showShimmer = true
                    )
                )
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(24.dp)
                .background(
                    brush = shimmerBrush(
                        targetValue = 1300f,
                        showShimmer = true
                    )
                )
        )
    }
}

private fun shimmerBrush(showShimmer: Boolean, targetValue: Float): Brush {
    return if (showShimmer) {
        Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f)
            ),
            start = Offset.Zero,
            end = Offset(x = targetValue, y = targetValue)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent)
        )
    }
}
