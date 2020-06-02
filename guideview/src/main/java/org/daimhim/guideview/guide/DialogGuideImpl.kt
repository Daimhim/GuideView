package org.daimhim.guideview.guide

import android.app.Activity
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
    override fun show(activity: Activity, overlay: ViewGroup?) {
        super.show(activity, overlay)
    }

    override fun onBindView(pViewGroup: ViewGroup) {
        super.onBindView(pViewGroup)
        mDialog = Dialog(pViewGroup.context)
        mDialog!!.setContentView(pViewGroup)
//        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
//            lWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            lWindow.setBackgroundDrawableResource(android.R.color.transparent); //设置window背景透明
        }
        mDialog!!.show()
    }


    override fun dismiss() {
        mDialog?.let {
            it.dismiss()
        }
    }
}