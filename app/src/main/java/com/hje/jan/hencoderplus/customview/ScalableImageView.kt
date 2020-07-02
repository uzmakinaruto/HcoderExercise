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
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import com.hje.jan.hencoderplus.R
import com.hje.jan.hencoderplus.extension.dp
import com.hje.jan.hencoderplus.extension.getBitmap
import kotlin.math.max
import kotlin.math.min

class ScalableImageView : View, Runnable {
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
    private var scaleGestureDetector: ScaleGestureDetector
    private var originOffsetX = 0f
    private var originOffsetY = 0f
    private var scroller: OverScroller
    private var downX = 0f
    private var downY = 0f

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        bitmap = getBitmap(context!!, R.drawable.chihuo, 300.dp())
        scroller = OverScroller(context, null)
        scaleGestureDetector =
            ScaleGestureDetector(context, object : ScaleGestureDetector.OnScaleGestureListener {
                var beginScale = 1f
                override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                    beginScale = currentScale
                    return true
                }

                override fun onScaleEnd(detector: ScaleGestureDetector?) {
                    offsetX = 0f
                    offsetY = 0f
                    invalidate()
                }

                override fun onScale(detector: ScaleGestureDetector?): Boolean {
                    currentScale = beginScale * (detector?.scaleFactor ?: 1f)
                    if (currentScale > fitHeightScale) currentScale = fitHeightScale
                    else if (currentScale < 1f) currentScale = 1f
                    invalidate()
                    return false
                }

            })
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
                    if(scroller.isFinished){
                        getScaleAnimator()?.start()
                    }
                    Log.d(
                        TAG,
                        "onDoubleTap x:${e?.x} y:${e?.y} fitWidthScale${fitWidthScale} fitHeightScale${fitHeightScale}"
                    )

                    if (currentScale == fitWidthScale) {
                        e?.let {
                            if (it.y > (height - bitmap.height * fitWidthScale) / 2
                                && it.y < (height + bitmap.height * fitWidthScale) / 2
                            ) {
                                downX = it.x
                                downY = it.y
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
                    if(currentScale == fitHeightScale){
                        scroller.fling(
                            offsetX.toInt(),
                            offsetY.toInt(),
                            velocityX.toInt(),
                            velocityY.toInt(),
                            (-(bitmap.width * fitHeightScale - width) / 2).toInt(),
                            ((bitmap.width * fitHeightScale - width) / 2).toInt(),
                            (-(bitmap.height * fitHeightScale - height) / 2).toInt(),
                            ((bitmap.height * fitHeightScale - height) / 2).toInt()
                            , 100.dp().toInt(), 100.dp().toInt()
                        )
                        postOnAnimation(this@ScalableImageView)
                    }
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            })
    }


    fun getScaleAnimator(): ObjectAnimator? {
        if (currentScale >= 1 && currentScale < fitWidthScale) {
            return ObjectAnimator.ofFloat(
                this,
                "currentScale",
                currentScale,
                fitWidthScale
            )
        } else if (currentScale >= fitWidthScale && currentScale < fitHeightScale) {
            return ObjectAnimator.ofFloat(
                this,
                "currentScale",
                currentScale,
                fitHeightScale
            )
        } else {
            offsetX = 0f
            offsetY = 0f
            return ObjectAnimator.ofFloat(
                this,
                "currentScale",
                fitHeightScale,
                1f
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = scaleGestureDetector.onTouchEvent(event)
        if (scaleGestureDetector.isInProgress) {
            return result
        }
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
        canvas?.translate(offsetX, offsetY)
        canvas?.scale(currentScale, currentScale, (width / 2).toFloat(), (height / 2).toFloat())
        canvas?.drawBitmap(bitmap, originOffsetX, originOffsetY, paint)
    }

    fun setCurrentScale(currentScale: Float) {
        this.currentScale = currentScale
        if (currentScale >= fitWidthScale && currentScale < fitHeightScale) {
            offsetX =
                (downX - width / 2) - (downX - width / 2) * currentScale / fitWidthScale
            offsetY =
                (downY - height / 2) - (downY - height / 2) * currentScale / fitWidthScale

        }
        invalidate()
    }

    override fun run() {
        if (scroller.computeScrollOffset()) {
            offsetX = scroller.currX.toFloat()
            offsetY = scroller.currY.toFloat()
            invalidate()
            postOnAnimation(this)
        }
    }
}