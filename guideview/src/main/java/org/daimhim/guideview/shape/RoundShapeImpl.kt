package org.daimhim.guideview.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import org.daimhim.guideview.view.GuideView
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

    override fun drawShape(canvas: Canvas, view: GuideView, paint: Paint) {
        val rect = Rect(view.left,view.top,view.right,view.bottom)
//        rect.left -= view.paddingLeft
//        rect.top -= view.paddingTop
//        rect.right += view.paddingRight
//        rect.bottom += view.paddingBottom
        canvas.drawRoundRect(RectF(rect), view.guideCorner, view.guideCorner, paint)
    }
}