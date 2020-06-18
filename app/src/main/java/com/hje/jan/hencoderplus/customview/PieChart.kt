package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.hje.jan.hencoderplus.extension.dp
import kotlin.math.cos
import kotlin.math.sin

class PieChart : View {
    companion object {
        const val TAG = "PieChart"
        val RADIUS = 150.dp()
        val angles = listOf(30f, 120f, 60f, 90f, 60f)
        val colors = listOf("#37E2E3", "#28E32C", "#E39533", "#E31D3C", "#B52CE3")
        val MOVE_DISTANCE = 50.dp()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var pickIndex = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var startAngle = 0.0
        var transX = 0f
        var transY = 0f
        for (index in angles.indices) {
            paint.color = Color.parseColor(colors[index])
            Log.d(TAG, "transX${transX}")
            Log.d(TAG, "transY${transY}")
            if (index == pickIndex) {
                transX =
                    cos(Math.toRadians(startAngle + angles[index] / 2)).toFloat() * MOVE_DISTANCE
                transY =
                    sin(Math.toRadians(startAngle + angles[index] / 2)).toFloat() * MOVE_DISTANCE
                canvas?.translate(transX, transY)
            }
            canvas?.drawArc(
                width / 2 - RADIUS,
                height / 2 - RADIUS,
                width / 2 + RADIUS,
                height / 2 + RADIUS,
                startAngle.toFloat(),
                angles[index],
                true,
                paint
            )
            if (index == pickIndex) {
                canvas?.translate(-transX, -transY)
            }
            startAngle += angles[index]
        }
    }

    fun pickIndex(pickIndex: Int) {
        this.pickIndex = pickIndex
    }
}