package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.hje.jan.hencoderplus.extension.dp
import kotlin.math.cos
import kotlin.math.sin

class DashBoard : View {

    companion object {
        const val TAG = "DashBoard"
        val RADIUS = 150.dp()
        const val ANGLE = 120
        const val DASH_COUNT = 20
        val POINTER_LENGTH = 100.dp()
    }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val dash = Path()
    var effect: PathDashPathEffect
    private var currentIndex = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        paint.style = Paint.Style.STROKE
        //小方块的宽度近似为弧长,实际上略有差别,
        dash.addRect(0.dp(), 0.dp(), 2.dp(), 16.dp(), Path.Direction.CW)
        //预留一个宽度,不然最后一个刻度画不上
        val interval =
            (Math.toRadians((360f - ANGLE).toDouble()) * RADIUS - 2.dp()) / (DASH_COUNT - 1)
        effect =
            PathDashPathEffect(dash, interval.toFloat(), 0.dp(), PathDashPathEffect.Style.ROTATE)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.strokeWidth = 4.dp()
        paint.color = Color.parseColor("#E31722")
        canvas?.drawArc(
            width / 2 - RADIUS,
            height / 2 - RADIUS,
            width / 2 + RADIUS,
            height / 2 + RADIUS,
            (90 + ANGLE / 2).toFloat(),
            (360 - ANGLE).toFloat(),
            false,
            paint
        )
        paint.pathEffect = effect
        canvas?.drawArc(
            width / 2 - RADIUS,
            height / 2 - RADIUS,
            width / 2 + RADIUS,
            height / 2 + RADIUS,
            (90 + ANGLE / 2).toFloat(),
            (360 - ANGLE).toFloat(),
            false,
            paint
        )
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = Color.BLACK
        paint.pathEffect = null
        canvas?.drawPoint((width / 2).toFloat(), (height / 2).toFloat(), paint)
        canvas?.drawLine(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (cos(Math.toRadians(getCurrentAngle(currentIndex))) * POINTER_LENGTH + width / 2).toFloat(),
            (sin(Math.toRadians(getCurrentAngle(currentIndex))) * POINTER_LENGTH + height / 2).toFloat(),
            paint
        )
        paint.strokeWidth = 8.dp()
        canvas?.drawPoint((width / 2).toFloat(), (height / 2).toFloat(), paint)
    }

    private fun getCurrentAngle(index: Int): Double {
        val angle = (90 + ANGLE / 2 + index * (360 - ANGLE) / (DASH_COUNT - 1)).toDouble()
        Log.d(TAG, angle.toString())
        return angle
    }

    fun setCurrentIndex(currentIndex: Int) {
        this.currentIndex = currentIndex
        invalidate()
    }
}