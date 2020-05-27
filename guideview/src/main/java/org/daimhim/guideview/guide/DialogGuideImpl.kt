package org.daimhim.guideview.guide

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

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
        mDialog!!.setContentView(onCreateView)
        mDialog!!.window?.let {lWindow->
            val lAttributes = lWindow.attributes
            lAttributes.width = WindowManager.LayoutParams.MATCH_PARENT
            lAttributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            lWindow.attributes = lAttributes
//            lWindow.decorView.setPadding(0, 0, 0, 0)
//                lWindow.getDecorView().setMinimumWidth((int) mMaskView.getMOverlayRect().right);
//                lWindow.getDecorView().setMinimumHeight((int) mMaskView.getMOverlayRect().bottom);
            //                lWindow.getDecorView().setMinimumWidth((int) mMaskView.getMOverlayRect().right);
//                lWindow.getDecorView().setMinimumHeight((int) mMaskView.getMOverlayRect().bottom);
            lWindow.setBackgroundDrawable(ColorDrawable(Color.BLUE))
        }
        mDialog!!.show()
        mDialog?.setOnCancelListener {

        }
    }

    override fun dismiss() {
        mDialog?.let {
            it.dismiss()
        }
    }
}