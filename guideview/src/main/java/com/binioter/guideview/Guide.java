package com.binioter.guideview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


/**
 * 遮罩系统的封装 <br>
 * * 外部需要调用{@link GuideBuilder}来创建该实例，实例创建后调用
 * * {@link #show(Activity)} 控制显示； 调用 {@link #dismiss()}让遮罩系统消失。 <br>
 * <p>
 * Created by binIoter
 */

public class Guide implements View.OnKeyListener, View.OnTouchListener {

    Guide() {

    }

    /**
     * 滑动临界值
     */
    private static final int SLIDE_THRESHOLD = 30;
    protected Configuration mConfiguration;
    protected MaskView mMaskView;
    protected Component[] mComponents;
    protected SparseArray<HighlightArea> mHighlightAreas;
    protected GuideBuilder.OnVisibilityChangedListener mOnVisibilityChangedListener;
    protected GuideBuilder.OnSlideListener mOnSlideListener;

    void setConfiguration(Configuration configuration) {
        mConfiguration = configuration;
    }

    void setComponents(Component[] components) {
        mComponents = components;
    }

    void setTargetViews(SparseArray<HighlightArea> highlightAreas) {
        mHighlightAreas = highlightAreas;
    }

    void setCallback(GuideBuilder.OnVisibilityChangedListener listener) {
        this.mOnVisibilityChangedListener = listener;
    }

    public void setOnSlideListener(GuideBuilder.OnSlideListener onSlideListener) {
        this.mOnSlideListener = onSlideListener;
    }

    /**
     * 显示遮罩
     *
     * @param activity 目标Activity
     */
    public void show(Activity activity) {
        show(activity, (ViewGroup) activity.getWindow().getDecorView());
    }
    /**
     * 显示遮罩
     *
     * @param dialog 目标Activity
     */
    public void show(Dialog dialog) {
        show(dialog.getContext(),dialog.getWindow(), (ViewGroup) dialog.getWindow().getDecorView());
    }
    /**
     * 显示遮罩
     *
     * @param activity 目标Activity
     * @param overlay  遮罩层view
     */
    public void show(Activity activity, ViewGroup overlay) {
        show(activity,activity.getWindow(), overlay);
    }


    public void show(Context context,Window window,ViewGroup overlay){
        mMaskView = onCreateView(context, overlay);
        switch (mConfiguration.mShowMode){
            case MaskView.VIEW_SHOW:{
                overlay.addView(mMaskView);
                break;
            }
            case MaskView.DIALOG_SHOW:{
                Dialog lDialog = new Dialog(context);
                lDialog.setContentView(mMaskView);
                Window lWindow = lDialog.getWindow();
                WindowManager.LayoutParams lAttributes = lWindow.getAttributes();
                lAttributes.width = WindowManager.LayoutParams.MATCH_PARENT;
                lAttributes.height = WindowManager.LayoutParams.MATCH_PARENT;
                lWindow.setAttributes(lAttributes);
                lWindow.getDecorView().setPadding(0,0,0,0);
//                lWindow.getDecorView().setMinimumWidth((int) mMaskView.getMOverlayRect().right);
//                lWindow.getDecorView().setMinimumHeight((int) mMaskView.getMOverlayRect().bottom);
                lWindow.setBackgroundDrawableResource(android.R.color.transparent);
                lDialog.show();
                break;
            }
            case MaskView.DIALOG_FRAGMENT_SHOW:{
                break;
            }
        }
    }

    public void clear() {
        if (mMaskView == null) {
            return;
        }
        final ViewGroup vp = (ViewGroup) mMaskView.getParent();
        if (vp == null) {
            return;
        }
        vp.removeView(mMaskView);
        onDestroy();
    }

    /**
     * 隐藏该遮罩并回收资源相关
     */
    public void dismiss() {
        if (mMaskView == null) {
            return;
        }
        final ViewGroup vp = (ViewGroup) mMaskView.getParent();
        if (vp == null) {
            return;
        }
        if (mConfiguration.mExitAnimationId != -1) {
            // mMaskView may leak if context is null
            Context context = mMaskView.getContext();
            assert context != null;

            Animation anim = AnimationUtils.loadAnimation(context, mConfiguration.mExitAnimationId);
            assert anim != null;
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    vp.removeView(mMaskView);
                    if (mOnVisibilityChangedListener != null) {
                        mOnVisibilityChangedListener.onDismiss();
                    }
                    onDestroy();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mMaskView.startAnimation(anim);
        } else {
            vp.removeView(mMaskView);
            if (mOnVisibilityChangedListener != null) {
                mOnVisibilityChangedListener.onDismiss();
            }
            onDestroy();
        }
    }

    /**
     * 根据locInwindow定位后，是否需要判断loc值非0
     */
    public void setShouldCheckLocInWindow(boolean set) {
//        mShouldCheckLocInWindow = set;/**/
    }

    protected MaskView onCreateView(Context context, ViewGroup overlay) {
        MaskView maskView = new MaskView(context);
        maskView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        maskView.setOnKeyListener(this);
        maskView.setMTargetRects(mHighlightAreas);
        maskView.setFullingColor(context.getResources().getColor(mConfiguration.mFullingColorId));
        maskView.setFullingAlpha(mConfiguration.mAlpha);
        if (mConfiguration.mOutsideTouchable) {
            maskView.setClickable(false);
        } else {
            maskView.setOnTouchListener(this);
        }

        // Adds the components to the mask view.
        for (Component c : mComponents) {
            maskView.addView(Common.componentToView(LayoutInflater.from(context), c));
        }

        return maskView;
    }

    private void onDestroy() {
        mConfiguration = null;
        mComponents = null;
        mOnVisibilityChangedListener = null;
        mOnSlideListener = null;
        mMaskView.removeAllViews();
        mMaskView = null;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mConfiguration != null && mConfiguration.mAutoDismiss) {
                dismiss();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    float startY = -1f;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //处理焦点
        if (mConfiguration.focusClick){
            for (int i = 0; i < mHighlightAreas.size(); i++) {
                if (isTouchPointInView(mHighlightAreas.valueAt(i).getView(),motionEvent.getRawX(),motionEvent.getRawY())){
                    mHighlightAreas.valueAt(i).getView().dispatchTouchEvent(motionEvent);
                }
            }
            return true;
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            startY = motionEvent.getY();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (startY - motionEvent.getY() > DimenUtil.dp2px(view.getContext(), SLIDE_THRESHOLD)) {
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlideListener(GuideBuilder.SlideState.UP);
                }
            } else if (motionEvent.getY() - startY > DimenUtil.dp2px(view.getContext(), SLIDE_THRESHOLD)) {
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlideListener(GuideBuilder.SlideState.DOWN);
                }
            }
            if (mConfiguration != null && mConfiguration.mAutoDismiss) {
                dismiss();
            }
        }
        return true;
    }

    private boolean isTouchPointInView(View targetView, float xAxis, float yAxis) {
        if (targetView== null) {
            return false;
        }
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + targetView.getMeasuredWidth();
        int bottom = top + targetView.getMeasuredHeight();
        if (yAxis >= top && yAxis <= bottom && xAxis >= left
                && xAxis <= right) {
            return true;
        }
        return false;
    }
}
