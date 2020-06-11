package org.daimhim.guideview.guide

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.util.SparseArray
import android.view.*
import org.daimhim.guideview.Configuration
import org.daimhim.guideview.util.DimenUtil
import org.daimhim.guideview.view.HighlightArea

/**
 * @date: 2020/5/25
 * @author: Zhangx
 * @remark: 这个人很懒，什么都没有留下
 */
class DialogGuideImpl : AbsGuide() {
    private var mDialog : GuideDialog? = null

    override fun onCreateView(context: Context?, window: Window, overlay: ViewGroup?): ViewGroup {
        val inflate = super.onCreateView(context, window, overlay)
        mDialog = GuideDialog(window,context!!)
        mDialog!!.setContentView(inflate)
        mDialog?.let {
            it.mConfiguration = configuration
            it.mHighlightAreas = mHighlightAreas
            it.setCancelable(configuration.mCancelable)
        }
        mDialog!!.window?.let {lWindow->
            val lAttributes = lWindow.attributes
            lAttributes.width = WindowManager.LayoutParams.MATCH_PARENT
            lAttributes.height = WindowManager.LayoutParams.MATCH_PARENT
            lAttributes.flags = window.attributes?.flags!!
            lAttributes.dimAmount = 0F
            lWindow.attributes = lAttributes
            lWindow.decorView.setPadding(0, 0, 0, 0)
            lWindow.decorView.setBackgroundColor(Color.TRANSPARENT)
            lWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        mDialog?.window?.decorView?.let {
            if (it is ViewGroup){
                (it as ViewGroup).removeAllViews()
                if (inflate.parent is ViewGroup){
                    (inflate.parent as ViewGroup).removeView(inflate)
                }
                (it as ViewGroup).addView(inflate)
            }
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

    class GuideDialog(private val backdropWindow:Window, context: Context) : Dialog(context) {
        var onTouchListener: View.OnTouchListener? = null
        lateinit var mConfiguration: Configuration
        lateinit var mHighlightAreas: SparseArray<HighlightArea>
        private var distributionCompleted = false

        override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
            Log.i("onTouch", ev.toString())
            //默认拦截
            var result = super.dispatchTouchEvent(ev)
            if (result){
                return result
            }
            //焦点之外都可以点击
            if (mConfiguration.mOutsideTouchable) {
                backdropWindow.superDispatchTouchEvent(ev)
                result = true
            }
            //仅处理焦点
            if (mConfiguration.focusClick) {
                //DOWN 事件，寻找处理的View
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    var currentView: View? = null
                    //遍历所有的允许处理事件的View 是否处理
                    for (i in 0 until mHighlightAreas.size()) {
                        if (!mHighlightAreas.valueAt(i).isEnable) {
                            continue
                        }
                        currentView = mHighlightAreas.valueAt(i).view
                        //判断当前触摸是否在View之内
                        if (DimenUtil.isTouchPointInView(currentView, ev.rawX, ev.rawY)) {
                            backdropWindow.superDispatchTouchEvent(ev)
                            result = true
                            distributionCompleted = true
                            break
                        }
                    }
                }else if (distributionCompleted && ev.action == MotionEvent.ACTION_UP){
                    backdropWindow.superDispatchTouchEvent(ev)
                    distributionCompleted = false
                }else if (distributionCompleted){
                    backdropWindow.superDispatchTouchEvent(ev)
                }
            }
            return result
        }

    }
}