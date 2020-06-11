package org.daimhim.guideview.util;

import android.content.Context;
import android.view.View;

/**
 * Created by binIoter
 */

public class DimenUtil {
    
    /** sp转换成px */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /** px转换成sp */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /** dip转换成px */
    public static int dp2px(Context context, float dipValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (dipValue * scale + 0.5f);
    }

    /** px转换成dip */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getStateBar(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean isTouchPointInView(View targetView, float xAxis, float yAxis) {
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