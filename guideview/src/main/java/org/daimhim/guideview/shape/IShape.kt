package org.daimhim.guideview.shape

import android.graphics.Canvas
import android.graphics.Paint
import org.daimhim.guideview.view.HighlightArea

interface IShape {
    fun drawShape(canvas: Canvas, highlightArea: HighlightArea, paint: Paint)
}