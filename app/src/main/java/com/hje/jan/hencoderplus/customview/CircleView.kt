package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.hje.jan.hencoderplus.extension.dp

class CircleView : View {
    companion object {
        val DEFAULT_RADIUS = 100.dp()
        val PADDING = 10.dp()
    }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //view根据自己特性得到的尺寸
        var mWidth = ((DEFAULT_RADIUS + PADDING) * 2 + paddingLeft + paddingRight).toInt()
        var mHeight = ((DEFAULT_RADIUS + PADDING) * 2 + paddingTop + paddingBottom).toInt()
        //这两句相当于下面一段代码 系统已经实现
        mWidth = resolveSizeAndState(mWidth, widthMeasureSpec,0)
        mHeight = resolveSizeAndState(mHeight, heightMeasureSpec,0)
        /*val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        when (widthMode) {
            MeasureSpec.EXACTLY ->
                mWidth = widthSize
            MeasureSpec.AT_MOST -> {
                if (mWidth > widthSize)
                    mWidth = widthSize
            }
            MeasureSpec.UNSPECIFIED -> {

            }
        }
        when (heightMode) {
            MeasureSpec.EXACTLY ->
                mHeight = heightSize
            MeasureSpec.AT_MOST -> {
                if (mHeight > heightSize) {
                    mHeight = heightSize
                }
            }
            MeasureSpec.UNSPECIFIED -> {

            }
        }*/
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(
            (measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(),
            measuredWidth / 2 - PADDING, paint
        )
    }
}