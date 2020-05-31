package org.daimhim.guideview.guide

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
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
        mDialog!!.setContentView(inflate,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
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
        val measuredWidth = window.decorView.measuredWidth
        val measuredHeight = window.decorView.measuredHeight
        Log.i(TAG, "measuredWidth:$measuredWidth measuredHeight:$measuredHeight")
        val displayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displayMetrics)
        Log.i(TAG, "displayMetrics.widthPixels:${displayMetrics.widthPixels} displayMetrics.heightPixels:${displayMetrics.heightPixels}")
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