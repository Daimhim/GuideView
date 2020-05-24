package com.binioter.guideview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF

class RoundShape : IShape {

    override fun drawShape(canvas: Canvas, rect: Rect, padding: Rect, corner: Int, paint: Paint) {
        rect.left - padding.left
        rect.top - padding.top
        rect.right + padding.right
        rect.bottom + padding.bottom
        canvas.drawRoundRect(RectF(rect), corner.toFloat(), corner.toFloat(), paint)
    }
}