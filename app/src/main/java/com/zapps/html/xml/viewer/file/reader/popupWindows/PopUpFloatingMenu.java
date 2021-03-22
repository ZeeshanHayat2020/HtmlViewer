package com.zapps.html.xml.viewer.file.reader.popupWindows;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.databinding.DataBindingUtil;

import com.zapps.html.xml.viewer.file.reader.R;
import com.zapps.html.xml.viewer.file.reader.activities.ActivityFilesContainer;
import com.zapps.html.xml.viewer.file.reader.databinding.LayoutPopupFloatingMenuBinding;
import com.zapps.html.xml.viewer.file.reader.interfaces.MyOnClickListener;
import com.zapps.html.xml.viewer.file.reader.models.ModelFiles;

import java.util.List;

public class PopUpFloatingMenu implements MyOnClickListener {

    private String TAG = "PopUpWidowHomeMenu";

    private LayoutPopupFloatingMenuBinding binding;
    private View popupView;
    private PopupWindow popupWindow;
    private Context context;
    private ActivityFilesContainer activityFilesContainer;
    private List<ModelFiles> filesList;

    private ObjectAnimator animFadeInWindow;
    private ObjectAnimator animFadeOutWindow;

    private ObjectAnimator animRotateRight;
    private ObjectAnimator animRotateLeft;

    private Animation animShowFab1;
    private Animation animShowFab2;
    private Animation animShowFab3;

    private Animation animHideFab1;
    private Animation animHideFab2;
    private Animation animHideFab3;


    public void initFloatingMenu(Context context) {
        this.context = context;
        activityFilesContainer = (ActivityFilesContainer) context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_popup_floating_menu, null, false);
        binding.setClickHandler(this::onClick);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false;
        popupWindow = new PopupWindow(binding.getRoot().getRootView(), width, height, focusable);
        animFadeInWindow = animFadeInToShowWindow(binding.getRoot());
        animFadeOutWindow = animFadeOutToHideWindow(binding.getRoot());
        animRotateRight = animRotateRight(binding.btnCrossMenu);
        animRotateLeft = animRotateLeft(binding.btnCrossMenu);
        animShowFab1 = AnimationUtils.loadAnimation(context, R.anim.anim_show_fab_1);
        animShowFab2 = AnimationUtils.loadAnimation(context, R.anim.anim_show_fab_2);
        animShowFab3 = AnimationUtils.loadAnimation(context, R.anim.anim_show_fab_3);

        animHideFab1 = AnimationUtils.loadAnimation(context, R.anim.anim_hide_fab_1);
        animHideFab2 = AnimationUtils.loadAnimation(context, R.anim.anim_hide_fab_2);
        animHideFab3 = AnimationUtils.loadAnimation(context, R.anim.anim_hide_fab_3);

    }

    public void showFloatingMenu(View view, List<ModelFiles> filesList) {
        this.filesList = filesList;
        if (!popupWindow.isShowing()) {
            popupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
            animFadeInWindow.start();
            animRotateRight.start();
            binding.btnFab1.startAnimation(animShowFab1);
            binding.btnFab2.startAnimation(animShowFab2);
            binding.btnFab3.startAnimation(animShowFab3);
        }
    }

    public void hideFloatingMenu() {
        if (popupWindow.isShowing()) {
            binding.btnFab1.startAnimation(animHideFab1);
            binding.btnFab2.startAnimation(animHideFab2);
            binding.btnFab3.startAnimation(animHideFab3);
            animRotateLeft.start();
            animFadeOutWindow.start();
        }
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCrossMenu:

            case R.id.rootView: {
                hideFloatingMenu();
            }
            break;
            case R.id.btnFab1: {

                activityFilesContainer.dialogRenameFile(filesList);
                hideFloatingMenu();
            }
            break;
            case R.id.btnFab2: {

                activityFilesContainer.deleteFileDialog(filesList);
                hideFloatingMenu();
            }
            break;
            case R.id.btnFab3: {
                activityFilesContainer.shareFileS(filesList);
                hideFloatingMenu();
            }
            break;

        }
    }

    public ObjectAnimator animFadeInToShowWindow(View view) {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f);
        fadeAnim.setDuration(150);
        fadeAnim.setInterpolator(new DecelerateInterpolator());
        return fadeAnim;
    }

    public ObjectAnimator animFadeOutToHideWindow(View view) {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f);
        fadeAnim.setDuration(600);
        fadeAnim.setInterpolator(new AccelerateInterpolator());
        fadeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                popupWindow.dismiss();
            }
        });
        return fadeAnim;
    }

    public ObjectAnimator animRotateRight(View view) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, View.ROTATION, 0, 45);
        rotate.setDuration(150);
        rotate.setInterpolator(new DecelerateInterpolator());
        return rotate;
    }

    public ObjectAnimator animRotateLeft(View view) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, View.ROTATION, 45, 0);
        rotate.setDuration(500);
        rotate.setInterpolator(new AccelerateInterpolator());
        return rotate;
    }

    public AnimatorSet animShowMenuBtn(View view, int duration) {

        ObjectAnimator ty = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, -100);
        ty.setDuration(duration);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f);
        alpha.setDuration(duration);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ty, alpha);
        set.setInterpolator(new LinearInterpolator());
        return set;
    }

    public AnimatorSet animHideMenuBtn(View view, int duration) {
        ObjectAnimator ty = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, -100, 0);
        ty.setDuration(duration);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0);
        alpha.setDuration(duration);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha);
        return set;
    }

}
