package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hje.jan.hencoderplus.R
import com.hje.jan.hencoderplus.extension.dp
import com.hje.jan.hencoderplus.extension.getBitmap

class CircleImage : View {

    companion object {
        val RADIUS = 100.dp()
        val RIM_WIDTH = 10.dp()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private var bitmap: Bitmap = getBitmap(context, R.drawable.xfermode, 300.dp())

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = RIM_WIDTH
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL
        canvas?.let {
            paint.xfermode = null
            val saveCount = it.saveLayer(
                width / 2 - RADIUS, height / 2 - RADIUS,
                width / 2 + RADIUS, height / 2 + RADIUS, paint
            )
            canvas.drawCircle(
                width / 2.toFloat(), height / 2.toFloat(), RADIUS, paint
            )
            paint.xfermode = xfermode
            canvas?.drawBitmap(
                bitmap,
                width / 2 - RADIUS, height / 2 - RADIUS, paint
            )
            it.restoreToCount(saveCount)
            paint.style = Paint.Style.STROKE
            paint.flags = Paint.ANTI_ALIAS_FLAG
            /*it.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), RADIUS, paint)*/
        }
    }
}