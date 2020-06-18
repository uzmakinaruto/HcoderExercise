package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.hje.jan.hencoderplus.R
import com.hje.jan.hencoderplus.extension.dp
import com.hje.jan.hencoderplus.extension.getBitmap

class ImageText : View {

    companion object {
        const val MESSAGE = "BBABBABBABBABBABBABBABBABBABBABBABBA凯尔特·阿尔斯特传说中的战士，女王。" +
                "异境·魔境「影之国」的女王兼守门人，" +
                "枪术与卢恩魔术的天才。她关闭了「影之国」大门，" +
                "并以其自身的强大力量支配着其中的无数亡灵。" +
                "收年轻的库·丘林（之后的阿尔斯特英雄）为徒，" +
                "毫无保留地传授技能并引导他，甚至赠与爱用的魔枪。" +
                "后来还教导了他的儿子康莱。BBABBABBABBABBABBABBABBABBABBABBABBABBABBA"
        val PADDING = 8.dp()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap: Bitmap = getBitmap(context, R.drawable.shijian, 100.dp())
    val measureWidth = listOf<Float>()

    /*val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)*/
    /*lateinit var staticLayout: StaticLayout*/
    /**
     * left top right bottom
     * */
    var bitmapBorder = arrayOf(0f, 0f, 0f, 0f)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        paint.textSize = 20.dp()
        /*textPaint.textSize = 30.dp()*/
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmapBorder[0] = ((width - bitmap.width) / 2).toFloat()
        bitmapBorder[1] = 200f
        bitmapBorder[2] = ((width + bitmap.width) / 2).toFloat()
        bitmapBorder[3] = 200f + bitmap.height
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)/*
        staticLayout =
            StaticLayout.Builder.obtain(MESSAGE, 0, MESSAGE.length, textPaint, width).build()
        staticLayout.draw(canvas)*/
        canvas?.drawBitmap(bitmap, ((width - bitmap.width) / 2).toFloat(), 200f, paint)
        //canvas?.drawText(MESSAGE, 0f, 100f, paint)
        var isFinish = false
        var startIndex = 0
        var maxWidth: Float
        var writeCount: Int
        var textStartX = PADDING
        var textStartY = 100f
        var isBeforeBitmap = true
        while (!isFinish) {
            //判断是否被图片挡住
            if (textStartY < bitmapBorder[1] || textStartY > bitmapBorder[3] + paint.fontSpacing) {
                //在图片的上面或者下面
                maxWidth = width - 2 * PADDING
                textStartX = PADDING
            } else {
                //会被图片挡住
                maxWidth = (width - bitmap.width) / 2 - 2 * PADDING
                if (isBeforeBitmap) {
                    textStartX = PADDING
                    isBeforeBitmap = false
                } else {
                    isBeforeBitmap = true
                    textStartX = (width + (bitmap.width)) / 2 + PADDING
                }
            }
            writeCount = paint.breakText(
                MESSAGE, startIndex, MESSAGE.length, true,
                maxWidth, null
            )
            canvas?.drawText(
                MESSAGE,
                startIndex,
                startIndex + writeCount,
                textStartX,
                textStartY,
                paint
            )
            startIndex += writeCount
            if (isBeforeBitmap)
                textStartY += paint.fontSpacing
            if (startIndex >= MESSAGE.length) break
        }
    }
}