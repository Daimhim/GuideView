package org.daimhim.guideview.guide

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import org.daimhim.guideview.util.DimenUtil
import org.daimhim.guideview.view.GuideLayout

class GuideLayoutGuideImpl : AbsGuide() {
    private var mDialog : Dialog? = null
    private val TAG = GuideLayoutGuideImpl::class.java.simpleName
    override fun onCreateView(context: Context, window: Window, overlay: ViewGroup): ViewGroup {
        val mFullingLayoutId = configuration.mFullingLayoutId
        val inflate : ViewGroup
        if (mFullingLayoutId != -1) {
            inflate = LayoutInflater.from(context).inflate(mFullingLayoutId, overlay,false) as ViewGroup
        }else{
            inflate = GuideLayout(context)
        }
        if (inflate is GuideLayout){
            inflate.setOriginalLayout(overlay)
        }
        mDialog = Dialog(context)
        mDialog!!.setContentView(inflate)
        mDialog!!.window?.let {lWindow->
            val lAttributes = lWindow.attributes
            lAttributes.width = WindowManager.LayoutParams.MATCH_PARENT
            lAttributes.height = WindowManager.LayoutParams.MATCH_PARENT
            lAttributes.flags = window.attributes.flags
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

    override fun show(context: Context?, window: Window?, overlay: ViewGroup?) {
        super.show(context, window, overlay)
        mDialog?.show()
    }
    override fun dismiss() {
        mDialog?.dismiss()
    }
}