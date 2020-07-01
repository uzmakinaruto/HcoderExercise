package com.hje.jan.hencoderplus.customview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.hje.jan.hencoderplus.R
import com.hje.jan.hencoderplus.extension.dp
import com.hje.jan.hencoderplus.extension.getBitmap
import kotlin.math.max
import kotlin.math.min

class ScalableImageView : View {
    companion object {
        const val TAG = "ScalableImageView"
        const val OVER_SCALE = 2f
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bitmap: Bitmap
    private var offsetX = 0f
    private var offsetY = 0f
    private var fitWidthScale = 0f
    private var fitHeightScale = 0f
    private var currentScale = 1f
    private var gestureDetector: GestureDetectorCompat
    private var originOffsetX = 0f
    private var originOffsetY = 0f

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        bitmap = getBitmap(context!!, R.drawable.chihuo, 300.dp())
        gestureDetector =
            GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent?): Boolean {
                    Log.d(TAG, "onDown")
                    return true
                }

                override fun onScroll(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    distanceX: Float,
                    distanceY: Float
                ): Boolean {
                    if (currentScale == fitHeightScale) {
                        offsetX -= distanceX
                        offsetX = min(offsetX, (bitmap.width * currentScale - width) / 2)
                        offsetX = max(offsetX, -(bitmap.width * currentScale - width) / 2)
                        offsetY -= distanceY
                        offsetY = min(offsetY, (bitmap.height * currentScale - height) / 2)
                        offsetY = max(offsetY, -(bitmap.height * currentScale - height) / 2)
                        Log.d(
                            TAG,
                            "onScroll- offsetX:${offsetX} offsetY:${offsetY} currentScale:${currentScale}"
                        )
                        invalidate()
                    }
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }

                override fun onDoubleTap(e: MotionEvent?): Boolean {
                    getScaleAnimator()?.start()
                    Log.d(
                        TAG,
                        "onDoubleTap x:${e?.x} y:${e?.y} fitWidthScale${fitWidthScale} fitHeightScale${fitHeightScale}"
                    )

                    if (currentScale == fitWidthScale) {
                        e?.let {
                            if (it.y > (height - bitmap.height * fitWidthScale) / 2
                                && it.y < (height + bitmap.height * fitWidthScale) / 2
                            ) {
                                offsetX =
                                    (it.x - width / 2) - (it.x - width / 2) * fitHeightScale / fitWidthScale
                                offsetY =
                                    (it.y - height / 2) - (it.y - height / 2) * fitHeightScale / fitWidthScale
                            }
                        }
                    }
                    return super.onDoubleTap(e)
                }

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    Log.d(TAG, "onFling")
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            })
    }

    fun getScaleAnimator(): ObjectAnimator? {
        when (currentScale) {
            1f -> return ObjectAnimator.ofFloat(this, "currentScale", 1f, fitWidthScale)
            fitWidthScale -> return ObjectAnimator.ofFloat(
                this,
                "currentScale",
                fitWidthScale,
                fitHeightScale
            )
            fitHeightScale -> {
                offsetX = 0f
                offsetY = 0f
                return ObjectAnimator.ofFloat(
                    this,
                    "currentScale",
                    fitHeightScale,
                    1f
                )
            }

            else -> return null
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originOffsetX = ((width - bitmap.width) / 2).toFloat()
        originOffsetY = ((height - bitmap.height) / 2).toFloat()
        fitWidthScale = width * 1f / bitmap.width
        fitHeightScale = height * 1f / bitmap.height * OVER_SCALE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.save()
        canvas?.translate(offsetX, offsetY)
        canvas?.scale(currentScale, currentScale, (width / 2).toFloat(), (height / 2).toFloat())
        canvas?.drawBitmap(bitmap, originOffsetX, originOffsetY, paint)
        canvas?.restore()
    }

    fun setCurrentScale(currentScale: Float) {
        this.currentScale = currentScale
        invalidate()
    }
}