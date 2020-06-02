package org.daimhim.guideview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.solver.widgets.ConstraintWidget
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.binioter.guideview.R


class GuideLayout : ConstraintLayout {
    private val TAG = GuideLayout::class.java.simpleName
    private var mGuideDraw : GuideDraw
    private var mConstraintSet : ConstraintSet
    constructor(context: Context):this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet? = null):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):super(context, attrs, defStyleAttr){
        mGuideDraw = GuideDraw()
        attrs?.let {
            val a = context.obtainStyledAttributes(
                    attrs, R.styleable.GuideLayout, defStyleAttr, 0)
            getGuideDraw().fullingPaint.color = a.getColor(R.styleable.GuideLayout_guide_color,Color.BLACK)
            getGuideDraw().fullingPaint.alpha = a.getInteger(R.styleable.GuideLayout_guide_alpha,150)

            a.recycle()
        }
        //自我绘制
        setWillNotDraw(false)
        mConstraintSet = ConstraintSet()
    }
    private var mGuideViews  = mutableListOf<GuideView>()
    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (params is LayoutParams
                && params.isPreview){
            if (isInEditMode){
                setOriginalLayout(child as ViewGroup)
                super.addView(child, 0, params)
            }
            return
        }
        super.addView(child, index, params)
        if (child is GuideView){
            child.visibility = View.INVISIBLE
            mGuideViews.add(child)
        }
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mLocationArray[0] = widthMeasureSpec
        mLocationArray[1] = heightMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        getGuideDraw().overlayRect.set(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat())
        Log.i(TAG, "onMeasure measuredWidth:$measuredWidth measuredHeight:$measuredHeight")
        var guideView: View?
        mGuideViews.forEach {
            guideView = getOriginalLayout()?.findViewById<View>(it.guideBindId)
            it.setBindViewSize(guideView?.measuredWidth ?: 0, guideView?.measuredHeight ?: 0)
            it.measure(widthMeasureSpec, heightMeasureSpec)
        }
    }
    private val mLocationArray = IntArray(2)
    @SuppressLint("WrongCall")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val oMeasuredWidth = getOriginalLayout()?.measuredWidth?:0
        val oMeasuredHeight = getOriginalLayout()?.measuredHeight?:0
        if (isInEditMode) {
            getOriginalLayout()?.layout(0,0,oMeasuredWidth,oMeasuredHeight)
        }
        val array = mLocationArray.copyOf()
        var guideView: View?
        var margin = 0
        mConstraintSet.clone(this)
        val rect = Rect()
        mGuideViews.forEach {
            guideView = getOriginalLayout()?.findViewById<View>(it.guideBindId)
            guideView?.getLocationInWindow(mLocationArray)
            guideView?.getGlobalVisibleRect(rect)
            margin = mLocationArray[0]
            if (measuredWidth / 2 > margin) {
                margin -= it.paddingLeft
                mConstraintSet.connect(it.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID,
                        ConstraintSet.LEFT, margin)
            }else{
                margin = measuredWidth - margin - it.paddingRight
                mConstraintSet.connect(it.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID,
                        ConstraintSet.RIGHT, margin)
            }
            margin = mLocationArray[1]
            if (measuredHeight / 2 > margin) {
                margin -= it.paddingTop
                mConstraintSet.connect(it.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID,
                        ConstraintSet.TOP, margin)
            }else{
                margin = measuredHeight - margin - it.paddingBottom
                mConstraintSet.connect(it.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID,
                        ConstraintSet.BOTTOM, margin)
            }
        }
        mConstraintSet.applyTo(this)
        Log.i(TAG, "onLayout measuredWidth:$measuredWidth measuredHeight:$measuredHeight")
        super.onMeasure(array[0],array[1])
        super.onLayout(changed, left, top, right, bottom)
    }
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        getGuideDraw().initPaintAndCanvas()
        getGuideDraw().eraserBitmap.eraseColor(Color.TRANSPARENT)
        getGuideDraw().eraserCanvas.drawColor(getGuideDraw().fullingPaint.color)
        mGuideViews.forEach {
            it.onDrawParent(getGuideDraw().eraserCanvas, getGuideDraw().eraser)
        }
        canvas?.drawBitmap(getGuideDraw().eraserBitmap, getGuideDraw().overlayRect.left, getGuideDraw().overlayRect.top, null)
    }
    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is GuideLayout.LayoutParams
    }
    override fun generateDefaultLayoutParams(): ConstraintLayout.LayoutParams {
        return GuideLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }
    override fun generateLayoutParams(attrs: AttributeSet): ConstraintLayout.LayoutParams {
        return GuideLayout.LayoutParams(context,attrs)
    }
    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return GuideLayout.LayoutParams(p)
    }
    fun getLayoutWidget(): ConstraintWidgetContainer {
        val declaredField = ConstraintLayout::class.java.getDeclaredField("mLayoutWidget")
        declaredField.isAccessible = true
        return declaredField.get(this) as ConstraintWidgetContainer
    }
    /**
     * 被指引的布局
     */
    private var mOriginalLayout : ViewGroup? = null
    fun setOriginalLayout(viewGroup: ViewGroup){
        mOriginalLayout = viewGroup
    }
    private fun getOriginalLayout():ViewGroup?{
        return mOriginalLayout
    }
    private fun getGuideDraw():GuideDraw{
        return mGuideDraw
    }
    class GuideDraw{
        /**
         * 蒙层区域
         */
        val overlayRect = RectF()
        /**
         * 挖空画笔
         */
        lateinit var eraser: Paint
        /**
         * 橡皮擦Bitmap
         */
        lateinit var eraserBitmap: Bitmap
        /**
         * 橡皮擦Cavas
         */
        lateinit var eraserCanvas: Canvas

        /**
         * 蒙层背景画笔
         */
        var fullingPaint: Paint = Paint()
        constructor(){
            fullingPaint.color = Color.BLACK
            fullingPaint.alpha = 150
        }
        fun initPaintAndCanvas() {
            eraserBitmap = Bitmap.createBitmap(overlayRect.right.toInt(), overlayRect.bottom.toInt(), Bitmap.Config.ARGB_8888)
            eraserCanvas = Canvas(eraserBitmap)
            eraser = Paint()
            eraser.color = Color.WHITE
            //图形重叠时的处理方式，擦除效果
            eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            //位图抗锯齿设置
            eraser.flags = Paint.ANTI_ALIAS_FLAG
        }
    }
    class LayoutParams : ConstraintLayout.LayoutParams {
        var isPreview = false
        constructor(width:Int,height:Int):super(width, height)
        constructor(source:ViewGroup.LayoutParams):super(source){
            if (source is LayoutParams){
                this.isPreview = source.isPreview
            }
        }
        constructor(c:Context, attrs:AttributeSet):super(c,attrs){
            val a = c.obtainStyledAttributes(
                    attrs, R.styleable.GuideLayout_Layout)
            isPreview = a.getBoolean(R.styleable.GuideLayout_Layout_layout_preview,false)
            a.recycle()
        }
        fun setAbsoluteX(x:Int){
            getWidget().drawX = x
        }
        fun setAbsoluteY(y:Int){
            getWidget().drawY = y;
        }
        private fun getWidget(): ConstraintWidget {
            val declaredField = ConstraintLayout.LayoutParams::class.java.getDeclaredField("widget")
            declaredField.isAccessible = true
            return declaredField.get(this) as ConstraintWidget
        }
    }
}