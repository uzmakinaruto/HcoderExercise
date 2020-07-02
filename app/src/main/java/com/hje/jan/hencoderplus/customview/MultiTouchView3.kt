package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.hje.jan.hencoderplus.extension.dp

class MultiTouchView3 : View {
    companion object {
        const val TAG = "MultiTouchView3"
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paths = HashMap<Int, Path>()

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5.dp()
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = Color.parseColor("#D78A33")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.actionMasked
        var index = 0
        var id = 0
        val pointerCount = event?.pointerCount!!
        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val path = Path()
                index = event.actionIndex
                path.moveTo(event.getX(index), event.getY(index))
                id = event?.getPointerId(index)
                paths[id] = path
            }
            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until pointerCount) {
                    paths[i]?.lineTo(event?.getX(i), event?.getY(i))
                }
                invalidate()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                index = event.actionIndex
                id = event?.getPointerId(index)
                paths.remove(id)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                paths.clear()
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (path in paths) {
            canvas?.drawPath(path.value, paint)
        }
    }
}