package com.binioter.guideview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

interface IShape {
    fun drawShape(canvas: Canvas,rect: Rect,padding: Rect,corner:Int,paint: Paint)
}