package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.view.isInvisible
import com.hje.jan.hencoderplus.R
import com.hje.jan.hencoderplus.extension.dp
import com.hje.jan.hencoderplus.extension.getBitmap
import com.hje.jan.hencoderplus.extension.getDensity

class FlipView : View {


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getBitmap(context, R.drawable.chihuo, 450.dp())
    private val camera = Camera()
    private var rotationDegree = 0f
    private var bottomRotation = 0f
    private var topRotation = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        camera.setLocation(0f, 0f, -6 * getDensity())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.save()
        canvas?.translate((width / 2).toFloat(), (height / 2).toFloat())
        canvas?.rotate(-rotationDegree)
        canvas?.clipRect(
            -bitmap.width,
            -bitmap.height,
            bitmap.width,
            0
        )
        camera.save()
        camera.rotateX(topRotation)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas?.rotate(rotationDegree)
        canvas?.translate((-width / 2).toFloat(), (-height / 2).toFloat())
        canvas?.drawBitmap(
            bitmap,
            (width - bitmap.width) / 2.toFloat(),
            (height - bitmap.height) / 2.toFloat(),
            paint
        )
        canvas?.restore()

        canvas?.save()
        canvas?.translate((width / 2).toFloat(), (height / 2).toFloat())
        canvas?.rotate(-rotationDegree)
        canvas?.clipRect(
            -bitmap.width,
            0,
            bitmap.width,
            bitmap.height
        )
        camera.save()
        camera.rotateX(bottomRotation)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas?.rotate(rotationDegree)
        canvas?.translate((-width / 2).toFloat(), (-height / 2).toFloat())
        canvas?.drawBitmap(
            bitmap,
            (width - bitmap.width) / 2.toFloat(),
            (height - bitmap.height) / 2.toFloat(),
            paint
        )
        canvas?.restore()
    }

    fun setBottomRotation(cameraRotation: Float) {
        this.bottomRotation = cameraRotation
        invalidate()
    }

    fun setTopRotation(topRotation: Float) {
        this.topRotation = topRotation
        invalidate()
    }

    fun setRotationDegree(rotationDegree: Float) {
        this.rotationDegree = rotationDegree
        invalidate()
    }
}