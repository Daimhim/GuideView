package org.daimhim.guideview.view

import android.graphics.Rect
import android.view.View
import org.daimhim.guideview.shape.IShape

class HighlightArea  {

    /**
     * 高亮的View
     */
    val view:View

    /**
     * 圆角
     */
    var corner:Int = 0

    /**
     * 样式
     */
    var style:Int = 0

    /**
     * panding
     */
    var padding: Rect = Rect()

    /**
     * 高亮View的坐标
     */
    var rect:Rect = Rect()

    /**
     * 要绘制的形状
     */
    var shape : IShape;

    /**
     * 是否开启高亮的触摸事件
     */
    var isEnable = true

    constructor(view: View,corner:Int,padding: Rect,style:Int,shape : IShape):this(view, corner, padding, style, shape, true)
    constructor(view: View, corner:Int, padding: Rect, style:Int, shape : IShape, isEnable: Boolean){
        this.view = view
        this.corner = corner
        this.padding = padding
        this.style = style
        this.shape = shape
        this.isEnable = isEnable
    }

}