package com.binioter.guideview.view

import android.content.Context
import android.graphics.*
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.binioter.guideview.util.Common

class MaskView
    : ViewGroup {
    /**
     * 蒙层区域
     */
    val mOverlayRect = RectF()

    /**
     * 高亮区域
     */
    var mTargetRects = SparseArray<HighlightArea>()


    /**
     * 挖空画笔
     */
    private lateinit var mEraser: Paint
    /**
     * 橡皮擦Bitmap
     */
    private lateinit var mEraserBitmap: Bitmap
    /**
     * 橡皮擦Cavas
     */
    private lateinit var mEraserCanvas: Canvas

    /**
     * 蒙层背景画笔
     */
    private var mFullingPaint: Paint = Paint()

    private val mStyle = ROUNDRECT

    private val temRectF = RectF()

    constructor(context: Context?):this(context,null,0)
    constructor(context: Context?, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        //自我绘制
        setWillNotDraw(false)
    }

    private fun initPaintAndCanvas() {
        mEraserBitmap = Bitmap.createBitmap(mOverlayRect.right.toInt(), mOverlayRect.bottom.toInt(), Bitmap.Config.ARGB_8888)
        mEraserCanvas = Canvas(mEraserBitmap)
        mEraser = Paint()
        mEraser.color = Color.WHITE
        //图形重叠时的处理方式，擦除效果
        mEraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        //位图抗锯齿设置
        mEraser.flags = Paint.ANTI_ALIAS_FLAG
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(w, h)
        var childAt : View
        for (index in 0 until childCount){
            childAt = getChildAt(index)
            measureChild(childAt,widthMeasureSpec,heightMeasureSpec)
        }
        var valueAt: HighlightArea
        for (i in 0 until mTargetRects.size()) {
            valueAt = mTargetRects.valueAt(i)
            valueAt.rect.set(Common.getViewAbsRect(valueAt.view, mOverlayRect.left.toInt(), mOverlayRect.top.toInt()))
        }
    }

    override fun onLayout(changed: Boolean,left:Int, top:Int, right:Int, bottom:Int) {
        var childAt : View
        var lp : LayoutParams
        var highlightArea : HighlightArea?
        var width : Int
        var height : Int
        for (index in 0 until childCount){
            //初始化
            temRectF.set(0F,0F,0F,0F)
            childAt = getChildAt(index)
            width = childAt.measuredWidth;
            height = childAt.measuredHeight;
            lp = childAt.layoutParams as LayoutParams
            //如果指定了锚点 就找到它并赋值
            highlightArea = mTargetRects.get(lp.id)
            highlightArea?.let {
                temRectF.set(it.rect)
            }
            var height1 = temRectF.height()
            var width1 = temRectF.width()
            var childLeft = 0F
            var childTop = 0F
            //垂直
            if (lp.targetAnchor == LayoutParams.ANCHOR_LEFT
                    || lp.targetAnchor == LayoutParams.ANCHOR_RIGHT){
                when (lp.targetParentPosition) {
                    LayoutParams.PARENT_START -> {
                        childTop = temRectF.top + lp.offsetY
                    }
                    LayoutParams.PARENT_CENTER -> {
                        childTop = temRectF.bottom - (temRectF.height()/2)  - (height/2) + lp.offsetY
                    }
                    LayoutParams.PARENT_END -> {
                        childTop = (temRectF.bottom - height) + lp.offsetY
                    }
                }

            }else if (lp.targetAnchor == LayoutParams.ANCHOR_TOP
                    || lp.targetAnchor == LayoutParams.ANCHOR_BOTTOM){
                //水平
                when (lp.targetParentPosition) {
                    LayoutParams.PARENT_START -> {
                        childLeft = temRectF.left + lp.offsetX
                    }
                    LayoutParams.PARENT_CENTER -> {
                        childLeft = temRectF.right - (temRectF.width()/2)  - (width/2) + lp.offsetX
                    }
                    LayoutParams.PARENT_END -> {
                        childLeft = temRectF.right + lp.offsetX
                    }
                }
            }
            //垂直
            when(lp.targetAnchor){
                LayoutParams.ANCHOR_LEFT ->{
                    childLeft = (temRectF.left - width) + lp.offsetX
                }
                LayoutParams.ANCHOR_RIGHT ->{
                    childLeft = (temRectF.right) + lp.offsetX
                }
                LayoutParams.ANCHOR_TOP ->{
                   childTop = (temRectF.top - height) + lp.offsetY
                }
                LayoutParams.ANCHOR_BOTTOM ->{
                    childTop = (temRectF.bottom) + lp.offsetY
                }
                LayoutParams.ANCHOR_OVER ->{

                }
                LayoutParams.ANCHOR_IN_PARENT ->{
                    val DEFAULT_CHILD_GRAVITY = Gravity.TOP or Gravity.START
                    var gravity: Int = lp.targetParentPosition
                    if (gravity == -1) {
                        gravity = DEFAULT_CHILD_GRAVITY
                    }

                    val layoutDirection = layoutDirection
                    val absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection)
                    val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK

                    childLeft = when (absoluteGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
                        Gravity.CENTER_HORIZONTAL -> (left + (left - right - width) / 2 + lp.offsetX).toFloat()
                        Gravity.RIGHT -> (right - width + lp.offsetX).toFloat()
                        Gravity.LEFT -> (left + lp.offsetX).toFloat()
                        else -> (left + lp.offsetX).toFloat()
                    }
                    childTop = when (verticalGravity) {
                        Gravity.TOP -> (top + lp.offsetY).toFloat()
                        Gravity.CENTER_VERTICAL -> (top + (bottom - top - height) / 2 + lp.offsetY).toFloat()
                        Gravity.BOTTOM -> (bottom - height - lp.offsetY).toFloat()
                        else -> (top + lp.offsetY).toFloat()
                    }
                }
            }
            childTop += mOverlayRect.top
            childAt.layout(childLeft.toInt(), childTop.toInt(), childLeft.toInt() + width, childTop.toInt() + height)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initPaintAndCanvas()
        mEraserBitmap.eraseColor(Color.TRANSPARENT)
        mEraserCanvas.drawColor(mFullingPaint.color)
        var valueAt: HighlightArea
        for (i in 0 until mTargetRects.size()) {
            valueAt = mTargetRects.valueAt(i)
            valueAt.shape.drawShape(mEraserCanvas,valueAt,mEraser)
        }
        canvas.drawBitmap(mEraserBitmap,mOverlayRect.left,mOverlayRect.top,null);
    }
    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        try {
            clearFocus()
            mEraserCanvas.setBitmap(null)
//            mEraserBitmap = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
//    override fun dispatchDraw(canvas: Canvas) {
//        val drawingTime = drawingTime
//        try {
//            var child: View?
//            for (i in 0 until childCount) {
//                child = getChildAt(i)
//                drawChild(canvas, child, drawingTime)
//            }
//        } catch (e: NullPointerException) {
//        }
//    }
    fun setFullingAlpha(alpha: Int) {
        mFullingPaint.alpha = alpha
    }

    fun setFullingColor(@ColorInt color: Int) {
        mFullingPaint.color = color
    }

    companion object{
        /**
         * 圆角矩形&矩形
         */
        const val ROUNDRECT = 0
        /**
         * 圆形
         */
        const val CIRCLE = 1
        /**
         * 自定义形状
         */
        const val CUSTOM = 2

        /**
         * 以普通View展示
         */
        const val VIEW_SHOW = 0x01
        /**
         * 以Dialog展示
         */
        const val DIALOG_SHOW = 0x02
        /**
         * 以DialogFragment展示
         */
        const val DIALOG_FRAGMENT_SHOW = 0x03
    }


    open class LayoutParams : ViewGroup.LayoutParams {
        var targetAnchor = ANCHOR_BOTTOM
        var targetParentPosition = PARENT_CENTER
        var offsetX = 0
        var offsetY = 0
        var id = -1

        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs) {}
        constructor(width: Int, height: Int) : super(width, height) {}
        constructor(source: ViewGroup.LayoutParams?) : super(source) {}

        companion object {
            const val ANCHOR_LEFT = 0x01
            const val ANCHOR_TOP = 0x02
            const val ANCHOR_RIGHT = 0x03
            const val ANCHOR_BOTTOM = 0x04
            const val ANCHOR_OVER = 0x05
            const val ANCHOR_IN_PARENT = 0x06
            const val PARENT_START = 0x10
            const val PARENT_CENTER = 0x20
            const val PARENT_END = 0x30
        }
    }
}