package com.hje.jan.hencoderplus.customview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.hje.jan.hencoderplus.R
import com.hje.jan.hencoderplus.extension.dp

class MaterialEditText : androidx.appcompat.widget.AppCompatEditText {
    companion object {
        val TEXT_SIZE = 12.dp()
        val TEXT_PADDING_LEFT = 4.dp()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var backgroundBound: Rect = Rect()
    var isUseFloatingText = true
    var minCharCount = 0
    var maxCharCount = 0
    private var isFloatingTextShown = false
    private var floatingTextFaction = 0f
    private var animator: ObjectAnimator? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.MaterialEditText)!!
        isUseFloatingText =
            typedArray.getBoolean(R.styleable.MaterialEditText_isUseFloatingText, true)
        maxCharCount = typedArray.getInteger(R.styleable.MaterialEditText_maxCharCount, 0)
        minCharCount = typedArray.getInteger(R.styleable.MaterialEditText_minCharCount, 0)
        typedArray.recycle()
        paint.textSize = TEXT_SIZE
        animator = ObjectAnimator.ofFloat(this, "floatingTextFaction", 0f, 1f)
        paint.color = resources.getColor(R.color.colorAccent, null)
        background.getPadding(backgroundBound)
        Log.d("MaterialEditText", backgroundBound.top.toString())
        addTextChangedListener(onTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int ->
            if (isUseFloatingText) {
                if (!TextUtils.isEmpty(charSequence) && !isFloatingTextShown) {
                    isFloatingTextShown = true
                    setPadding(
                        backgroundBound.left,
                        (backgroundBound.top + paint.fontSpacing).toInt(),
                        backgroundBound.right,
                        backgroundBound.bottom
                    )
                    animator?.start()
                } else if (TextUtils.isEmpty(charSequence) && isFloatingTextShown) {
                    isFloatingTextShown = false
                    setPadding(
                        backgroundBound.left,
                        backgroundBound.top,
                        backgroundBound.right,
                        backgroundBound.bottom
                    )
                    animator?.reverse()
                }
            }
        })
    }

    /*override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        if (maxCharCount > 0 || minCharCount > 0) {
            height = (height + paint.fontSpacing).toInt()
        }
        //setMeasuredDimension(width, height)
    }*/


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        Log.d("MaterialEditText", "paint.fontSpacing:${paint.fontSpacing} TEXT_SIZE:${TEXT_SIZE}")
        super.onDraw(canvas)
        paint.alpha = (0xFF * floatingTextFaction).toInt()
        if (isUseFloatingText && !TextUtils.isEmpty(hint)) {
            canvas?.drawText(
                hint.toString(),
                TEXT_PADDING_LEFT,
                paint.fontSpacing + textSize * (1 - floatingTextFaction),
                paint
            )
        }

    }

    fun setFloatingTextFaction(floatingTextFaction: Float) {
        this.floatingTextFaction = floatingTextFaction
        invalidate()
    }

    fun getFloatingTextFaction() = floatingTextFaction
}