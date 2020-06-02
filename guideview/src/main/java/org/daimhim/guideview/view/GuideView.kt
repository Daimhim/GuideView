package org.daimhim.guideview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.binioter.guideview.R
import org.daimhim.guideview.shape.CircleShapeImpl
import org.daimhim.guideview.shape.IShape
import org.daimhim.guideview.shape.RoundShapeImpl

/**
 * @date: 2020/5/26
 * @author: Zhangx
 * @remark: 这个人很懒，什么都没有留下
 */
class GuideView : View {
    var guideBindId = -1
    private var guideShape = 0
    var guideCorner = 0F
    private var bindViewWidth = 0
    private var bindViewHeight = 0
    private lateinit var mIShape:IShape
    constructor(context: Context):this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet? = null):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):super(context, attrs, defStyleAttr){
        attrs?.let {
            val a = context.obtainStyledAttributes(
                    attrs, R.styleable.GuideView, defStyleAttr, 0)
            guideBindId = a.getResourceId(R.styleable.GuideView_guide_bindId,-1)
            guideShape = a.getInt(R.styleable.GuideView_guide_shape,0)
            guideCorner = a.getDimension(R.styleable.GuideView_guide_corner,0F)
            mIShape = when(guideShape){
                0->{
                    RoundShapeImpl()
                }
                1->{
                    CircleShapeImpl()
                }
                else->{
                    RoundShapeImpl()
                }
            }
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width: Int = bindViewWidth
        var height: Int = bindViewHeight
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize
        }
        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize
        }
        width += paddingLeft + paddingRight
        height += paddingTop + paddingBottom
        setMeasuredDimension(width, height)
    }

    public fun setBindViewSize(widthMeasure: Int, heightMeasure : Int){
        bindViewWidth = widthMeasure
        bindViewHeight = heightMeasure
    }

    fun onDrawParent(canvas: Canvas,eraser: Paint) {
        mIShape.drawShape(canvas,this,eraser)
    }

}