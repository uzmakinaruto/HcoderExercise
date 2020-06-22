package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import com.hje.jan.hencoderplus.extension.dp
import kotlin.random.Random

class ColorTextView : androidx.appcompat.widget.AppCompatTextView {


    companion object {
        val COLORS = listOf<Int>(
            Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA,
            Color.YELLOW
        )
        val TEXT_SIZE = listOf<Float>(
            8.dp(), 12.dp(), 16.dp(), 24.dp()
        )
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        textSize = TEXT_SIZE[Random.nextInt(TEXT_SIZE.size)]
        setBackgroundColor(COLORS[Random.nextInt(COLORS.size)])
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }
}