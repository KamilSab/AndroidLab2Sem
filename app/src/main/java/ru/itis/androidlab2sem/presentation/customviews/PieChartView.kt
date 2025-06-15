package ru.itis.androidlab2sem.presentation.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.math.*

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val sectorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 36f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val rectF = RectF()
    private val touchPath = Path()
    private val sectorPaths = mutableListOf<Path>()
    private val touchRegion = Region()
    private val sectorRegions = mutableListOf<Region>()

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var selectedIndex = -1
    private var lastTouchAngle = -1f

    private val gapAngle = 2f
    private val highlightAlpha = 200

    private var sectors: List<Pair<Int, Float>> = emptyList()
    private val defaultColors = listOf(
        Color.parseColor("#FF6B6B"),
        Color.parseColor("#4ECDC4"),
        Color.parseColor("#45B7D1"),
        Color.parseColor("#FFA07A"),
        Color.parseColor("#98D8C8"),
        Color.parseColor("#D4A5A5"),
        Color.parseColor("#A4C5E0"),
        Color.parseColor("#C4A4DB"),
        Color.parseColor("#FFD166")
    )

    fun setData(data: List<Pair<Int, Float>>) {
        val sum = data.sumOf { it.second.toDouble() }
        require(abs(sum - 100.0) < 0.01) {
            "Сумма должна быть 100%, текущая сумма: $sum"
        }

        require(data.all { it.second > 0 }) {
            "Все должны быть положительными."
        }

        this.sectors = data
        sectorPaths.clear()
        sectorRegions.clear()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = min(w, h) / 2f * 0.85f
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }

    override fun onDraw(canvas: Canvas) {
        if (sectors.isEmpty()) return

        var startAngle = -90f
        val totalGapAngle = sectors.size * gapAngle
        val totalSweepAngle = 360f - totalGapAngle
        sectorPaths.clear()
        sectorRegions.clear()

        sectors.forEachIndexed { index, (_, percentage) ->
            val sweepAngle = percentage * totalSweepAngle / 100f
            val color = defaultColors[index % defaultColors.size]

            val path = Path().apply {
                moveTo(centerX, centerY)
                arcTo(rectF, startAngle, sweepAngle)
                lineTo(centerX, centerY)
                close()
            }
            sectorPaths.add(path)

            val region = Region()
            region.setPath(path, Region(
                (centerX - radius).toInt(),
                (centerY - radius).toInt(),
                (centerX + radius).toInt(),
                (centerY + radius).toInt()
            ))
            sectorRegions.add(region)

            sectorPaint.color = if (index == selectedIndex) {
                adjustColorAlpha(color, highlightAlpha)
            } else {
                color
            }
            canvas.drawPath(path, sectorPaint)

            if (percentage >= 5f) {
                drawPercentage(canvas, startAngle, sweepAngle, percentage.toInt().toString())
            }

            startAngle += sweepAngle + gapAngle
        }
    }

    private fun drawPercentage(canvas: Canvas, startAngle: Float, sweepAngle: Float, text: String) {
        val middleAngle = startAngle + sweepAngle / 2
        val textRadius = radius * 0.6f
        val x = centerX + textRadius * cos(Math.toRadians(middleAngle.toDouble())).toFloat()
        val y = centerY + textRadius * sin(Math.toRadians(middleAngle.toDouble())).toFloat() + textPaint.textSize / 3
        canvas.drawText("$text%", x, y, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val newSelectedIndex = findSelectedSector(event.x, event.y)
                if (newSelectedIndex != selectedIndex) {
                    selectedIndex = newSelectedIndex
                    invalidate()
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (selectedIndex != -1) {
                    showSelection(sectors[selectedIndex].first.toString())
                }
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                selectedIndex = -1
                invalidate()
                return true
            }
        }
        return false
    }

    private fun findSelectedSector(x: Float, y: Float): Int {
        val distance = sqrt((x - centerX).pow(2) + (y - centerY).pow(2))
        if (distance > radius) {
            return -1
        }

        for (i in sectorRegions.indices) {
            if (sectorRegions[i].contains(x.toInt(), y.toInt())) {
                return i
            }
        }

        return -1
    }

    private fun showSelection(key: String) {
        Toast.makeText(context, "Выбран: $key", Toast.LENGTH_SHORT).show()
    }

    private fun adjustColorAlpha(color: Int, alpha: Int): Int {
        return Color.argb(
            alpha,
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }

    private fun Math.toRadians(degrees: Double): Double = degrees * PI / 180
}