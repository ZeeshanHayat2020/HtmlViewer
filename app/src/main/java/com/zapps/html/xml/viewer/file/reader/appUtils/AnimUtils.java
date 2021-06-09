package com.zapps.html.xml.viewer.file.reader.appUtils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.zapps.html.xml.viewer.file.reader.R;


public class AnimUtils {


    public static void animShakeTheView(View view) {
        Animation shake = AnimationUtils.loadAnimation(view.getContext(), R.anim.shake_animation);
        view.startAnimation(shake);
    }

    public static void animCircularShow(View view) {
        ObjectAnimator dy = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0, 1f);
        dy.setDuration(300);
        ObjectAnimator dx = ObjectAnimator.ofFloat(view, View.SCALE_X, 0, 1f);
        dx.setDuration(300);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, View.ROTATION, 0, 360);
        rotate.setDuration(300);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f);
        alpha.setDuration(400);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateInterpolator());
        set.playTogether(dy, dx, rotate, alpha);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        set.start();
    }

    public static void animCircularHide(View view) {
        ObjectAnimator dy = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0);
        dy.setDuration(300);
        ObjectAnimator dx = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 0);
        dx.setDuration(300);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, View.ROTATION, 360, 0);
        rotate.setDuration(300);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0);
        alpha.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(dy, dx, rotate, alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
            }
        });
        set.start();
    }


}
