package ru.itis.androidlab2sem.presentation.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun GraphScreen() {
    var pointCount by remember { mutableStateOf("") }
    var values by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var points by remember { mutableStateOf<List<Float>?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = pointCount,
            onValueChange = { pointCount = it },
            label = { Text("Number of points") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = values,
            onValueChange = { values = it },
            label = { Text("Values (comma separated)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Text(error!!, color = Color.Red)
        }

        Button(
            onClick = {
                try {
                    val count = pointCount.toInt()
                    val valuesList = values.split(",").map { it.trim().toFloat() }

                    if (valuesList.size != count) {
                        error = "Number of values must match point count"
                        return@Button
                    }

                    if (valuesList.any { it < 0 }) {
                        error = "Values must be non-negative"
                        return@Button
                    }

                    error = null
                    points = valuesList
                } catch (e: Exception) {
                    error = "Invalid input"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Draw Graph")
        }

        Spacer(modifier = Modifier.height(16.dp))

        points?.let { drawGraph(it) }
    }
}

@Composable
private fun drawGraph(points: List<Float>) {
    val color = Color.Blue
    val graphHeight = 300.dp
    val graphWidth = LocalConfiguration.current.screenWidthDp.dp - 32.dp

    Canvas(
        modifier = Modifier
            .height(graphHeight)
            .width(graphWidth)
    ) {
        val maxValue = points.maxOrNull() ?: 1f
        val stepX = size.width / (points.size - 1)
        val pointsNormalized = points.mapIndexed { index, value ->
            Offset(
                x = index * stepX,
                y = size.height - (value / maxValue * size.height)
            )
        }

        drawLine(
            color = Color.Black,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = 2f
        )

        drawLine(
            color = Color.Black,
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            strokeWidth = 2f
        )

        val path = Path().apply {
            moveTo(0f, size.height)
            pointsNormalized.forEach { lineTo(it.x, it.y) }
            lineTo(pointsNormalized.last().x, size.height)
            close()
        }

        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(color.copy(alpha = 0.3f), Color.Transparent)
            )
        )

        if (pointsNormalized.size > 1) {
            for (i in 0 until pointsNormalized.size - 1) {
                drawLine(
                    color = color,
                    start = pointsNormalized[i],
                    end = pointsNormalized[i + 1],
                    strokeWidth = 3f
                )
            }
        }

        pointsNormalized.forEach { point ->
            drawCircle(
                color = color,
                radius = 5f,
                center = point
            )
        }
    }
}