package com.binioter.guideview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class CircleShape : IShape {
    override fun drawShape(canvas: Canvas, rect: Rect, padding: Rect, corner: Int, paint: Paint) {

        canvas.drawCircle(rect.centerX().toFloat(), rect.centerY().toFloat(),
                (rect.width() / 2).toFloat() + padding.width(), paint)
    }
}