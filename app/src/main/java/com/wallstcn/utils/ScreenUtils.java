package com.wallstcn.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by _GZL on 2016/3/4.
 */
public class ScreenUtils {

    public static int dp2px(Context ctxt, int dp) {
        final float scale = ctxt.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
