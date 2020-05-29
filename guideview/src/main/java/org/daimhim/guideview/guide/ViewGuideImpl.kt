package org.daimhim.guideview.guide

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import org.daimhim.guideview.view.MaskView


/**
 * @date: 2020/5/25
 * @author: Zhangx
 * @remark: 这个人很懒，什么都没有留下
 */
class ViewGuideImpl : AbsGuide() {
    private var onCreateView: View? = null
    override fun show(context: Context, window: Window, overlay: ViewGroup) {
        onCreateView = onCreateView(context, overlay)

        val dm = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(dm)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dm.heightPixels)
        (onCreateView as MaskView?)?.layoutParams = params

        if (onCreateView!!.parent == null) {
            overlay.addView(onCreateView!!)
            if (mConfiguration.mEnterAnimationId != -1) {
                val anim = AnimationUtils.loadAnimation(context, mConfiguration.mEnterAnimationId)!!
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        if (mOnVisibilityChangedListener != null) {
                            mOnVisibilityChangedListener.onShown()
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                onCreateView!!.startAnimation(anim)
            } else {
                if (mOnVisibilityChangedListener != null) {
                    mOnVisibilityChangedListener.onShown()
                }
            }
        }
    }

    override fun dismiss() {
        val vp = ((onCreateView ?: return).parent ?: return) as ViewGroup
        if (mConfiguration.mExitAnimationId != -1) {
            // mMaskView may leak if context is null
            val context: Context = onCreateView!!.context!!
            val anim = AnimationUtils.loadAnimation(context, mConfiguration.mExitAnimationId)!!
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    vp.removeView(onCreateView!!)
                    if (mOnVisibilityChangedListener != null) {
                        mOnVisibilityChangedListener.onDismiss()
                    }
                    onDestroy()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            onCreateView!!.startAnimation(anim)
        } else {
            vp.removeView(onCreateView!!)
            if (mOnVisibilityChangedListener != null) {
                mOnVisibilityChangedListener.onDismiss()
            }
            onDestroy()
        }
    }
}