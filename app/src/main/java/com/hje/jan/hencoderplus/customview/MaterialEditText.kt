package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class MaterialEditText : androidx.appcompat.widget.AppCompatEditText {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        
    }
}