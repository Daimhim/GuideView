package com.binioter.guideview.guide

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.binioter.guideview.view.MaskView

/**
 * @date: 2020/5/25
 * @author: Zhangx
 * @remark: 这个人很懒，什么都没有留下
 */
class DialogGuideImpl : AbsGuide() {
    private var mDialog : Dialog? = null
    override fun show(context: Context, window: Window, overlay: ViewGroup) {
        val onCreateView = onCreateView(context, overlay)
        mDialog = Dialog(context)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog!!.setContentView(onCreateView)
        mDialog!!.window?.let {lWindow->
            val lAttributes = lWindow.attributes
            lWindow.addFlags(window.attributes.flags)
            lAttributes.width = WindowManager.LayoutParams.MATCH_PARENT
            lAttributes.height = WindowManager.LayoutParams.MATCH_PARENT
            lWindow.attributes = lAttributes
            lWindow.decorView.setPadding(0, 0, 0, 0)
//                lWindow.getDecorView().setMinimumWidth((int) mMaskView.getMOverlayRect().right);
//                lWindow.getDecorView().setMinimumHeight((int) mMaskView.getMOverlayRect().bottom);
            //                lWindow.getDecorView().setMinimumWidth((int) mMaskView.getMOverlayRect().right);
//                lWindow.getDecorView().setMinimumHeight((int) mMaskView.getMOverlayRect().bottom);
            lWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            lWindow.decorView.setBackgroundColor(Color.TRANSPARENT)
        }
        mDialog!!.show()
    }

    override fun dismiss() {
        mDialog?.let {
            it.dismiss()
        }
    }

    override fun onCreateView(context: Context, overlay: ViewGroup): MaskView {
        val createView = super.onCreateView(context, overlay)
        val rect = Rect()
        overlay.getWindowVisibleDisplayFrame(rect)
        createView.mOverlayRect.set(rect)
        return createView
    }
}