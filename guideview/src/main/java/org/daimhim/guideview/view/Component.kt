package org.daimhim.guideview.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * * 遮罩系统中相对于目标区域而绘制一些图片或者文字等view需要实现的接口. <br></br>
 *  我还有一个想法 把这个变成View 当作MaskView的特殊子View，在虚拟一个高亮View，就可以直接在xml代码中编写遮罩，可惜我没时间
 *
 * * anchor 相对目标View的锚点
 * fitPosition 相对目标View的对齐
 * xOffset 相对目标View的X轴位移，在计算锚点和对齐之后。
 * yOffset 相对目标View的Y轴位移，在计算锚点和对齐之后。
 *
 * Created by binIoter
 */
abstract class Component(val anchorId: Int = -1,var anchor: Int,var fitPosition: Int,var xOffset: Int = 0,var yOffset: Int = 0) {
    /**
     * 需要显示的view
     *
     * @param inflater use to inflate xml resource file
     * @return the component view
     */
    abstract fun getView(inflater: LayoutInflater): View


    companion object {
        /**
         * 相对位置
         */
        const val FIT_START = MaskView.LayoutParams.PARENT_START
        const val FIT_END = MaskView.LayoutParams.PARENT_END
        const val FIT_CENTER = MaskView.LayoutParams.PARENT_CENTER

        /**
         * 高亮View为锚点
         */
        const val ANCHOR_LEFT = MaskView.LayoutParams.ANCHOR_LEFT
        const val ANCHOR_RIGHT = MaskView.LayoutParams.ANCHOR_RIGHT
        const val ANCHOR_BOTTOM = MaskView.LayoutParams.ANCHOR_BOTTOM
        const val ANCHOR_TOP = MaskView.LayoutParams.ANCHOR_TOP
        const val ANCHOR_OVER = MaskView.LayoutParams.ANCHOR_OVER

        /**
         * 以父类View为锚点 位置使用 Gravity
         */
        const val ANCHOR_PARENT = MaskView.LayoutParams.ANCHOR_IN_PARENT
    }
}