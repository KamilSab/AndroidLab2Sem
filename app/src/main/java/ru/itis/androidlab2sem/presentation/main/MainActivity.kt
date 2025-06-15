package ru.itis.androidlab2sem.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import ru.itis.androidlab2sem.R
import ru.itis.androidlab2sem.databinding.ActivityMainBinding
import ru.itis.androidlab2sem.presentation.customviews.PieChartView

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testData1 = listOf(1 to 25f, 2 to 42f, 3 to 33f)
        val testData2 = listOf(1 to 50f, 2 to 50f)
        val testData3 = listOf(1 to 10f, 2 to 20f, 3 to 30f, 4 to 40f)
        val testData4 = listOf(1 to 60f, 2 to 30f, 3 to 10f)

        findViewById<PieChartView>(R.id.pieChart).apply {
            setData(testData3)
        }
    }
}