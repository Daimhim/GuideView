package org.daimhim.guideview.guide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.daimhim.guideview.util.Common;
import org.daimhim.guideview.view.Component;
import org.daimhim.guideview.Configuration;
import org.daimhim.guideview.util.DimenUtil;
import org.daimhim.guideview.GuideBuilder;
import org.daimhim.guideview.view.FocusView;
import org.daimhim.guideview.view.HighlightArea;
import org.daimhim.guideview.view.MaskView;


/**
 * 遮罩系统的封装 <br>
 * * 外部需要调用{@link GuideBuilder}来创建该实例，实例创建后调用
 * * {@link #show(Activity)} 控制显示； 调用 {@link #dismiss()}让遮罩系统消失。 <br>
 * <p>
 * Created by binIoter
 */

public abstract class AbsGuide implements View.OnKeyListener, View.OnTouchListener {

    AbsGuide() {

    }

    /**
     * 滑动临界值
     */
    private static final int SLIDE_THRESHOLD = 30;
    protected Configuration mConfiguration;
    protected Component[] mComponents;
    protected SparseArray<HighlightArea> mHighlightAreas;
    protected GuideBuilder.OnVisibilityChangedListener mOnVisibilityChangedListener;
    protected GuideBuilder.OnSlideListener mOnSlideListener;

    public void setConfiguration(Configuration configuration) {
        mConfiguration = configuration;
    }

    public void setComponents(Component[] components) {
        mComponents = components;
    }

    public void setTargetViews(SparseArray<HighlightArea> highlightAreas) {
        mHighlightAreas = highlightAreas;
    }

    public void setCallback(GuideBuilder.OnVisibilityChangedListener listener) {
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


    public abstract void show(Context context,Window window,ViewGroup overlay);

    /**
     * 隐藏该遮罩并回收资源相关
     */
    public abstract void dismiss();

    /**
     * 根据locInwindow定位后，是否需要判断loc值非0
     */
    public void setShouldCheckLocInWindow(boolean set) {
//        mShouldCheckLocInWindow = set;/**/
    }


    protected MaskView onCreateView(Context context, ViewGroup overlay) {
        MaskView maskView = new MaskView(context);
        Rect rect = new Rect();
        overlay.getWindowVisibleDisplayFrame(rect);
        maskView.getMOverlayRect().set(rect);
        maskView.setFocusableInTouchMode(true);
        maskView.setFocusable(true);
        maskView.requestFocus();
        maskView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        maskView.setOnKeyListener(this);
        maskView.setMTargetRects(mHighlightAreas);
        maskView.setFullingColor(context.getResources().getColor(mConfiguration.getMFullingColorId()));
        maskView.setFullingAlpha(mConfiguration.getMAlpha());
        if (mConfiguration.getMOutsideTouchable()) {
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

    protected void onDestroy() {
        mConfiguration = null;
        mComponents = null;
        mOnVisibilityChangedListener = null;
        mOnSlideListener = null;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.i("onKey",event.toString());
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mConfiguration != null && mConfiguration.getMAutoDismiss()) {
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
        Log.i("onTouch",motionEvent.toString());
        //处理焦点
        if (mConfiguration.getFocusClick()){
            //默认拦截
            boolean result = true;
            //DOWN 事件，寻找处理的View
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                View currentView = null;
                //遍历所有的允许处理事件的View 是否处理
                for (int i = 0; i < mHighlightAreas.size(); i++) {
                    if (!mHighlightAreas.valueAt(i).isEnable()){
                        continue;
                    }
                    currentView = mHighlightAreas.valueAt(i).getView();
                    //判断当前触摸是否在View之内
                    if (isTouchPointInView(currentView, motionEvent.getRawX(), motionEvent.getRawY())) {
                        result = false;
                        break;
                    }
                }
            }
            return result;
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
            if (mConfiguration != null && mConfiguration.getMAutoDismiss()) {
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
