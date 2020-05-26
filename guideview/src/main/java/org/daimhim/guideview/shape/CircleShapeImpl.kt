package org.daimhim.guideview.shape

import android.graphics.Canvas
import android.graphics.Paint
import org.daimhim.guideview.view.HighlightArea

class CircleShapeImpl : IShape {

    override fun drawShape(canvas: Canvas, highlightArea: HighlightArea, paint: Paint) {
        canvas.drawCircle(highlightArea.rect.centerX().toFloat(),
                highlightArea.rect.centerY().toFloat(),
                (highlightArea.rect.width() / 2).toFloat() + highlightArea.padding.width(), paint)
    }
}