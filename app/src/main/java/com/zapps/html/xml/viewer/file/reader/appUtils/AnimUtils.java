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

    public static void animCircularRevealToShowLayout(View view) {


        if (view.isAttachedToWindow()) {

            int x = view.getRight();
            int y = view.getBottom();

            int startRadius = 0;
            int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
            view.setVisibility(View.VISIBLE);
            anim.start();

        } else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    v.removeOnAttachStateChangeListener(this);

                    int x = view.getRight();
                    int y = view.getBottom();

                    int startRadius = 0;
                    int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());

                    Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
                    view.setVisibility(View.VISIBLE);
                    anim.start();

                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                }
            });
        }
    }

    public static void animCircularRevealToHideLayout(View view) {
        int x = view.getRight();
        int y = view.getBottom();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, endRadius, startRadius);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        anim.start();
    }

    public static void animOpenMainMenu(View view) {
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, -view.getLayoutParams().height, 0);
        translateYAnimator.setDuration(300);
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -view.getLayoutParams().width, 0);
        translateXAnimator.setDuration(300);
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 0.5f, 1f);
        fadeAnim.setDuration(200);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(translateYAnimator, translateXAnimator, fadeAnim);
        set.start();
    }

    public static void animCloseMainMenu(View view) {
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, -view.getLayoutParams().height);
        translateYAnimator.setDuration(200);
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, -view.getLayoutParams().width);
        translateXAnimator.setDuration(200);
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0.5f);
        fadeAnim.setDuration(300);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(translateXAnimator, translateYAnimator, fadeAnim);
        set.start();
    }

}
