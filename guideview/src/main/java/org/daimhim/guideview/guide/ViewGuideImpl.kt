package org.daimhim.guideview.guide

import android.content.Context
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils

/**
 * @date: 2020/5/25
 * @author: Zhangx
 * @remark: 这个人很懒，什么都没有留下
 */
class ViewGuideImpl : AbsGuide() {
    override fun onCreateView(context: Context?, window: Window?, overlay: ViewGroup?): ViewGroup {
        val onCreateView = super.onCreateView(context, window, overlay)
        onCreateView.setFocusableInTouchMode(true)
        onCreateView.setFocusable(true)
        onCreateView.requestFocus()
        return onCreateView
    }
    override fun show(context: Context, window: Window, overlay: ViewGroup) {
        super.show(context, window, overlay)
        if (contentView.parent == null) {
            overlay.addView(contentView)
            if (configuration.mEnterAnimationId != -1) {
                val anim = AnimationUtils.loadAnimation(context, configuration.mEnterAnimationId)!!
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        if (onInfoListener.mOnVisibilityChangedListener != null) {
                            onInfoListener.mOnVisibilityChangedListener.onShown()
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                contentView.startAnimation(anim)
            } else {
                if (onInfoListener.mOnVisibilityChangedListener != null) {
                    onInfoListener.mOnVisibilityChangedListener.onShown()
                }
            }
        }
    }

    override fun dismiss() {
        val vp = ((contentView ?: return).parent ?:return) as ViewGroup
        if (configuration.mExitAnimationId != -1) {
            // mMaskView may leak if context is null
            val context: Context = contentView.context!!
            val anim = AnimationUtils.loadAnimation(context, configuration.mExitAnimationId)!!
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    vp.removeView(contentView!!)
                    if (onInfoListener.mOnVisibilityChangedListener != null) {
                        onInfoListener.mOnVisibilityChangedListener.onDismiss()
                    }
                    onDestroy()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            contentView!!.startAnimation(anim)
        } else {
            vp.removeView(contentView!!)
            if (onInfoListener.mOnVisibilityChangedListener != null) {
                onInfoListener.mOnVisibilityChangedListener.onDismiss()
            }
            onDestroy()
        }
    }
}