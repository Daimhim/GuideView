package com.binioter.guideview.view

import android.graphics.Rect
import android.view.View
import com.binioter.guideview.shape.IShape

class HighlightArea  {


    val view:View
    var corner:Int = 0
    var style:Int = 0
    var padding: Rect = Rect()
    var rect:Rect = Rect()
    var shape : IShape;

    constructor(view: View,corner:Int,padding: Rect,style:Int,shape : IShape){
        this.view = view
        this.corner = corner
        this.padding = padding
        this.style = style
        this.shape = shape
    }

}