package org.daimhim.guideview.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import org.daimhim.guideview.view.GuideView
import org.daimhim.guideview.view.HighlightArea

interface IShape {
    fun drawShape(canvas: Canvas, highlightArea: HighlightArea, paint: Paint)
    fun drawShape(canvas: Canvas, view: GuideView, paint: Paint){

    }
}