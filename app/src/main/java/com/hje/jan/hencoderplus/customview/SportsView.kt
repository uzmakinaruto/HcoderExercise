package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.hje.jan.hencoderplus.extension.dp

class SportsView : View {

    companion object {
        val RADIUS = 150.dp()
        var TEXT = "SportsView"
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textBound = Rect()
    private var fontMetrics = Paint.FontMetrics()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        paint.strokeCap = Paint.Cap.ROUND
        paint.textSize = 30.dp()
        paint.textAlign = Paint.Align.CENTER
        paint.getTextBounds(TEXT, 0, TEXT.length, textBound)
        paint.getFontMetrics(fontMetrics)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.strokeWidth = 20.dp()
        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#D5D5D7")
        canvas?.drawCircle(
            (width / 2).toFloat(), (height / 2).toFloat(),
            RADIUS, paint
        )
        paint.color = Color.parseColor("#D52AD7")
        canvas?.drawArc(
            width / 2 - RADIUS,
            height / 2 - RADIUS,
            width / 2 + RADIUS,
            height / 2 + RADIUS,
            -90f,
            225f,
            false,
            paint
        )
        paint.style = Paint.Style.FILL
        //当text不变时使用这种方式更为准确,但text变化时text高度可能会上下跳动
        canvas?.drawText(
            TEXT,
            (width / 2).toFloat(),
            (height / 2).toFloat() + (textBound.height()) / 2,
            paint
        )
        paint.color = Color.BLACK
        //当text经常变化时使用这种方式
        canvas?.drawText(
            TEXT,
            (width / 2).toFloat(),
            (height / 2).toFloat() + (fontMetrics.descent - fontMetrics.ascent) / 2,
            paint
        )
        paint.strokeWidth = 2.dp()
        canvas?.drawLine(0f, (height / 2).toFloat(), width.toFloat(), (height / 2).toFloat(), paint)
        canvas?.drawLine(
            (width / 2).toFloat(),
            0f,
            (width / 2).toFloat(),
            height.toFloat(),
            paint
        )
    }
}