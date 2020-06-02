package org.daimhim.guideview.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import org.daimhim.guideview.view.HighlightArea

class RoundShapeImpl : IShape {



    override fun drawShape(canvas: Canvas, highlightArea: HighlightArea, paint: Paint) {
        val rect = Rect(highlightArea.rect)
        rect.left - highlightArea.padding.left
        rect.top - highlightArea.padding.top
        rect.right + highlightArea.padding.right
        rect.bottom + highlightArea.padding.bottom
        canvas.drawRoundRect(RectF(rect), highlightArea.corner.toFloat(), highlightArea.corner.toFloat(), paint)
    }
}