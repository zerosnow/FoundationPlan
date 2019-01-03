package com.example.archerlei.foundationplan.util;

/**
 * Created by shinehuang on 14-6-20.
 */

import android.content.Context;

public class DensityUtil {
    private static final String TAG = "DensityUtil";

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     */
    public static int sp2px(Context context, float spValue) {
    	final float scale = context.getResources().getDisplayMetrics().scaledDensity;
    	return (int) (spValue * scale + 0.5f);
    }
}
