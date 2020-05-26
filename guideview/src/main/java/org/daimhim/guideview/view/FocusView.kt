package org.daimhim.guideview.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * @date: 2020/5/26
 * @author: Zhangx
 * @remark: 这个人很懒，什么都没有留下
 */
class FocusView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    override fun isFocused(): Boolean {
        return return true
    }
}