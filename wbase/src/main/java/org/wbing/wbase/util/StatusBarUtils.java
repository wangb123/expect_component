package org.wbing.wbase.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

/**
 * @author 王冰
 * @date 2018/6/20
 */
public class StatusBarUtils {
    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getHeight(Context context) {
        final int heightResId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        return heightResId > 0 ? context.getResources().getDimensionPixelSize(heightResId) : 0;
    }

    /**
     * 给activity设置状态栏是否透明
     * 实际上是把DecorView的顶部移除SystemWindow的边距，导致其上移
     *
     * @param activity    界面
     * @param translucent 是否透明
     */
    public static void setTranslucent(final Activity activity, final boolean translucent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        setStyle(activity,translucent);
        if (translucent) {
            decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                setBackgroundColor(activity, Color.TRANSPARENT, false);
                 WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
                return defaultInsets.replaceSystemWindowInsets(
                        defaultInsets.getSystemWindowInsetLeft(),
                        0,
                        defaultInsets.getSystemWindowInsetRight(),
                        defaultInsets.getSystemWindowInsetBottom());
            });
        } else {
             decorView.setOnApplyWindowInsetsListener(null);
        }

        ViewCompat.requestApplyInsets(decorView);
    }

    /**
     * 给activity设置状态栏的背景颜色
     *
     * @param activity 界面
     * @param color    颜色值
     * @param animated 是否使用动画
     */
    public static void setBackgroundColor(final Activity activity, final int color, final boolean animated) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        activity.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (animated) {
            int curColor = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                curColor = activity.getWindow().getStatusBarColor();
            }
            ValueAnimator colorAnimation =
                    ValueAnimator.ofObject(new ArgbEvaluator(), curColor, color);
            colorAnimation.addUpdateListener(
                    animator -> activity
                            .getWindow()
                            .setStatusBarColor((Integer) animator.getAnimatedValue()));
            colorAnimation.setDuration(300).setStartDelay(0);
            colorAnimation.start();
        } else {
            activity.getWindow().setStatusBarColor(color);
        }
    }


    /**
     * 设置状态栏文字颜色
     *
     * @param activity 界面
     * @param dark     true黑色，false白色
     */
    public static void setStyle(Activity activity, final boolean dark) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(dark ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : 0);
    }
}
