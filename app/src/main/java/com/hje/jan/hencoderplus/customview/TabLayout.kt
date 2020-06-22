package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import kotlin.math.max

class TabLayout : ViewGroup {

    private val childBounds = mutableListOf<Rect>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthUsed = 0
        var heightUsed = 0
        var maxLineHeight = 0
        var maxLineWidth = 0
        val parentWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val parentWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            val lp = child.layoutParams as MarginLayoutParams
            var childBound: Rect
            if (childBounds.size > index) {
                childBound = childBounds[index]
            } else {
                childBound = Rect()
                childBounds.add(childBound)
            }
            measureChildWithMargins(
                child,
                widthMeasureSpec,
                0,
                heightMeasureSpec,
                heightUsed
            )
            //MeasureSpec.UNSPECIFIED是滑动控件的情况
            if (parentWidthMode != MeasureSpec.UNSPECIFIED && widthUsed + child.measuredWidth + lp.leftMargin > parentWidthSize) {
                //换行
                widthUsed = 0
                heightUsed += maxLineHeight
                maxLineHeight = 0
            }
            childBound.set(
                widthUsed + lp.leftMargin,
                heightUsed + lp.topMargin,
                widthUsed + lp.leftMargin + child.measuredWidth,
                heightUsed + lp.topMargin + child.measuredHeight
            )
            widthUsed += child.measuredWidth + lp.leftMargin + lp.rightMargin
            maxLineHeight =
                max(maxLineHeight, child.measuredHeight + lp.topMargin + lp.bottomMargin)
            maxLineWidth = max(maxLineWidth, widthUsed)
        }
        setMeasuredDimension(maxLineWidth, heightUsed + maxLineHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            val childBound = childBounds[index]
            child.layout(childBound.left, childBound.top, childBound.right, childBound.bottom)
        }
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }


    //不需要实现 直接使用measureChildWithMargins
    fun getChildWidthSpec(index: Int, widthMeasureSpec: Int, widthUsed: Int): Int {
        val parentWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val parentWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val child = getChildAt(index)
        val lp = child.layoutParams as MarginLayoutParams
        var childWidthSpecSize = lp.width
        var childWidthSpecMode = 0
        val padding = child.paddingLeft + child.paddingRight
        +lp.leftMargin + lp.rightMargin
        if (childWidthSpecSize == LayoutParams.MATCH_PARENT) {
            when (parentWidthMode) {
                MeasureSpec.EXACTLY -> {
                    childWidthSpecSize =
                        parentWidthSize - widthUsed - padding
                    childWidthSpecMode = MeasureSpec.EXACTLY
                }
                MeasureSpec.AT_MOST -> {
                    childWidthSpecSize = parentWidthSize - widthUsed - padding
                    childWidthSpecMode = MeasureSpec.AT_MOST
                }
                MeasureSpec.UNSPECIFIED -> {
                    childWidthSpecSize = 0
                    childWidthSpecMode = MeasureSpec.UNSPECIFIED
                }
            }
        } else if (childWidthSpecSize == LayoutParams.WRAP_CONTENT) {
            when (parentWidthMode) {
                MeasureSpec.EXACTLY -> {
                    childWidthSpecSize = parentWidthSize - widthUsed - padding
                    childWidthSpecMode = MeasureSpec.AT_MOST
                }
                MeasureSpec.AT_MOST -> {
                    childWidthSpecSize = parentWidthSize - widthUsed - padding
                    childWidthSpecMode = MeasureSpec.AT_MOST
                }
                MeasureSpec.UNSPECIFIED -> {
                    childWidthSpecSize = 0
                    childWidthSpecMode = MeasureSpec.UNSPECIFIED
                }
            }
        } else if (childWidthSpecSize > 0) {
            when (parentWidthMode) {
                MeasureSpec.EXACTLY -> {
                    childWidthSpecMode = MeasureSpec.EXACTLY
                }
                MeasureSpec.AT_MOST -> {
                    childWidthSpecMode = MeasureSpec.EXACTLY
                }
                MeasureSpec.UNSPECIFIED -> {
                    childWidthSpecMode = MeasureSpec.EXACTLY
                }
            }
        }
        return MeasureSpec.makeMeasureSpec(childWidthSpecSize, childWidthSpecMode)
    }

    fun getChildHeightSpec(index: Int, heightMeasureSpec: Int, heightUsed: Int): Int {
        val parentHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        val parentHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        val child = getChildAt(index)
        val lp = child.layoutParams as MarginLayoutParams
        var childHeightSpecSize = lp.height
        var childHeightSpecMode = 0
        val padding = child.paddingTop + child.paddingBottom
        +lp.leftMargin + lp.rightMargin
        if (childHeightSpecSize == LayoutParams.MATCH_PARENT) {
            when (parentHeightMode) {
                MeasureSpec.EXACTLY -> {
                    childHeightSpecSize =
                        parentHeightSize - heightUsed - padding
                    childHeightSpecMode = MeasureSpec.EXACTLY
                }
                MeasureSpec.AT_MOST -> {
                    childHeightSpecSize = parentHeightSize - heightUsed - padding
                    childHeightSpecMode = MeasureSpec.AT_MOST
                }
                MeasureSpec.UNSPECIFIED -> {
                    childHeightSpecSize = 0
                    childHeightSpecMode = MeasureSpec.UNSPECIFIED
                }
            }
        } else if (childHeightSpecSize == LayoutParams.WRAP_CONTENT) {
            when (parentHeightMode) {
                MeasureSpec.EXACTLY -> {
                    childHeightSpecSize = parentHeightSize - heightUsed - padding
                    childHeightSpecMode = MeasureSpec.AT_MOST
                }
                MeasureSpec.AT_MOST -> {
                    childHeightSpecSize = parentHeightSize - heightUsed - padding
                    childHeightSpecMode = MeasureSpec.AT_MOST
                }
                MeasureSpec.UNSPECIFIED -> {
                    childHeightSpecSize = 0
                    childHeightSpecMode = MeasureSpec.UNSPECIFIED
                }
            }
        } else if (childHeightSpecSize > 0) {
            when (parentHeightMode) {
                MeasureSpec.EXACTLY -> {
                    childHeightSpecMode = MeasureSpec.EXACTLY
                }
                MeasureSpec.AT_MOST -> {
                    childHeightSpecMode = MeasureSpec.EXACTLY
                }
                MeasureSpec.UNSPECIFIED -> {
                    childHeightSpecMode = MeasureSpec.EXACTLY
                }
            }
        }
        return MeasureSpec.makeMeasureSpec(childHeightSpecSize, childHeightSpecMode)
    }
}