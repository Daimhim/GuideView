package org.daimhim.guideview.guide

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*

/**
 * @date: 2020/5/25
 * @author: Zhangx
 * @remark: 这个人很懒，什么都没有留下
 */
class DialogGuideImpl : AbsGuide() {
    private var mDialog : GuideDialog? = null

    override fun onCreateView(context: Context?, window: Window?, overlay: ViewGroup?): ViewGroup {
        val inflate = super.onCreateView(context, window, overlay)
        mDialog = GuideDialog(context!!)
        mDialog!!.setContentView(inflate)
        mDialog!!.window?.let {lWindow->
            val lAttributes = lWindow.attributes
            lAttributes.width = WindowManager.LayoutParams.MATCH_PARENT
            lAttributes.height = WindowManager.LayoutParams.MATCH_PARENT
            lAttributes.flags = window?.attributes?.flags!!
            lWindow.attributes = lAttributes
            lWindow.decorView.setPadding(0, 0, 0, 0)
            lWindow.decorView.setBackgroundColor(Color.TRANSPARENT)
            lWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        mDialog?.window?.decorView?.post {
            var parent = inflate.parent
            while (parent != null && parent is ViewGroup) {
                if (parent.paddingTop > 0) {
                    parent.setPadding(0, 0, 0, 0)
                }
                parent = parent.parent
            }
        }
        return inflate
    }

    override fun onBindView(pViewGroup: ViewGroup?) {
        mDialog?.onTouchListener = AbsGuideTouchListener(configuration,mHighlightAreas)
        mDialog?.setCancelable(configuration.mCancelable)
    }

    override fun show(context: Context?, window: Window?, overlay: ViewGroup?) {
        super.show(context, window, overlay)
        mDialog?.show()
    }


    override fun dismiss() {
        mDialog?.dismiss()
    }

    class GuideDialog(context: Context) : Dialog(context) {
        var onTouchListener: View.OnTouchListener? = null
        override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
            return onTouchListener?.onTouch(window.decorView,ev)?:super.dispatchTouchEvent(ev)
        }


    }
}