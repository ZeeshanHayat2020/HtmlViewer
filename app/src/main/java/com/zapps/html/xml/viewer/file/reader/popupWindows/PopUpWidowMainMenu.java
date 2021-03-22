package com.zapps.html.xml.viewer.file.reader.popupWindows;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.databinding.DataBindingUtil;

import com.zapps.html.xml.viewer.file.reader.R;
import com.zapps.html.xml.viewer.file.reader.databinding.LayoutPopupMenuMainBinding;
import com.zapps.appsFlowModule.activities.ActivityLanguage;
import com.zapps.appsFlowModule.activities.ActivityPrivacyPolicy;

import com.zapps.html.xml.viewer.file.reader.activities.ActivityMain;
import com.zapps.html.xml.viewer.file.reader.interfaces.MyOnClickListener;
import com.zapps.html.xml.viewer.file.reader.appUtils.AnimUtils;
import com.zapps.html.xml.viewer.file.reader.appUtils.AppOpenManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;


public class PopUpWidowMainMenu implements MyOnClickListener {
    private String TAG = "PopUpWidowHomeMenu";
    private LayoutPopupMenuMainBinding binding;
    private Context context;
    private PopupWindow popupWindow;
    private ObjectAnimator animFadeInWindow;
    private ObjectAnimator animFadeOutWindow;
    private InterstitialAd interstitialAd;

    public PopUpWidowMainMenu(Context context, InterstitialAd interstitialAd) {
        this.context = context;
        this.interstitialAd = interstitialAd;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_popup_menu_main, null, false);
        binding.setClickHandler(this::onClick);
        animFadeInWindow = animFadeInToShowWindow(binding.getRoot());
        animFadeOutWindow = animFadeOutToHideWindow(binding.getRoot());
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false;
        popupWindow = new PopupWindow(binding.getRoot(), width, height, focusable);
    }


    public void showMenu(View view) {
        if (!popupWindow.isShowing()) {
            popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.TOP, 0, 0);
            animFadeInWindow.start();
            AnimUtils.animOpenMainMenu(binding.rootView);
        }
    }

    public void hideMenu() {
        if (popupWindow.isShowing()) {
            AnimUtils.animCloseMainMenu(binding.rootView);
            animFadeOutWindow.start();
        }
    }


    public ObjectAnimator animFadeInToShowWindow(View view) {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f);
        fadeAnim.setDuration(200);
        fadeAnim.setInterpolator(new DecelerateInterpolator());
        return fadeAnim;
    }

    public ObjectAnimator animFadeOutToHideWindow(View view) {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f);
        fadeAnim.setDuration(200);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.parentView: {
                hideMenu();
            }
            break;
            case R.id.menuMainBtnShare: {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + context.getPackageName());
                context.startActivity(Intent.createChooser(intent, "Share via"));
            }
            break;
            case R.id.menuMainBtnRate: {
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));

                } catch (ActivityNotFoundException unused2) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));

                }
            }
            break;
            case R.id.menuMainBtnMore: {
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Z Apps Lab")));

                } catch (ActivityNotFoundException unused) {
                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/developer?id=NJSmartBytes")));

                }
            }
            break;
            case R.id.menuMainBtnLanguage: {

                Intent intent = new Intent(context, ActivityLanguage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                if (interstitialAd != null && interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    AppOpenManager.isInterstitialShowing = true;
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            context.startActivity(intent);
                            ((ActivityMain) context).finish();
                            AppOpenManager.isInterstitialShowing = false;
                        }
                    });
                } else {
                    context.startActivity(intent);
                    ((ActivityMain) context).finish();
                }
            }
            break;
            case R.id.menuMainBtnPrivacy: {
                Intent intent = new Intent(context, ActivityPrivacyPolicy.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
                ((ActivityMain) context).finish();
            }
            break;


        }
    }
}
