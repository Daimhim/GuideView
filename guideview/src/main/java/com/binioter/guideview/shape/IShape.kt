package com.binioter.guideview.shape

import android.graphics.Canvas
import android.graphics.Paint
import com.binioter.guideview.view.HighlightArea

interface IShape {
    fun drawShape(canvas: Canvas, highlightArea: HighlightArea, paint: Paint)
}