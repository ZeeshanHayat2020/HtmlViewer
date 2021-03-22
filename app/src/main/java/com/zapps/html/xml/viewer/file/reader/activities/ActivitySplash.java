package com.zapps.html.xml.viewer.file.reader.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.zapps.html.xml.viewer.file.reader.R;
import com.zapps.appsFlowModule.activities.ActivityIntroSLides;
import com.zapps.appsFlowModule.activities.ActivityLanguage;
import com.zapps.appsFlowModule.activities.ActivityPrivacyPolicy;
import com.zapps.appsFlowModule.prefrences.MyPreferences;
import com.zapps.html.xml.viewer.file.reader.databinding.ActivitySplashBinding;
import com.zapps.html.xml.viewer.file.reader.appUtils.AppOpenManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;

public class ActivitySplash extends ActivityBase {

    private ActivitySplashBinding activitySplashBinding;
    private Handler launchHandler;
    private Runnable launchRunnable;
    private MyPreferences myPreferences;
    private boolean isAdLeftApp = false;

    int currentApiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        MobileAds.initialize(this, getResources().getString(R.string.app_Id));
        reqNewInterstitial(this);
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initViews();
    }

    private void hideSystemUI() {
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isAdLeftApp = false;
        if (haveNetworkConnection()) {
            showInterstitial();
        } else {
            launchWithDelay();
        }
    }

    private void initViews() {
        myPreferences = new MyPreferences(this);
    }

    private void showInterstitial() {
        launchRunnable = new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded() && !isAdLeftApp) {
                    mInterstitialAd.show();
                    AppOpenManager.isInterstitialShowing = true;
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            AppOpenManager.isInterstitialShowing = false;
                            launchLanguageActivity();
                        }
                    });
                    reqNewInterstitial(ActivitySplash.this);
                } else {
                    launchLanguageActivity();
                }
            }
        };
        launchHandler = new Handler(getMainLooper());
        launchHandler.postDelayed(launchRunnable, 6000);
    }

    private void launchWithDelay() {
        launchRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAdLeftApp) {
                    launchLanguageActivity();
                }
            }
        };
        launchHandler = new Handler();
        launchHandler.postDelayed(launchRunnable, 4000);
    }

    private void launchLanguageActivity() {
        final Intent intent;
        if (!myPreferences.isLanguageSelected()) {
            intent = new Intent(ActivitySplash.this, ActivityLanguage.class);
        } else {
            if (myPreferences.isFirstTimeLaunch()) {
                intent = new Intent(ActivitySplash.this, ActivityIntroSLides.class);
            } else {
                if (myPreferences.isPrivacyPolicyAccepted()) {
                    intent = new Intent(ActivitySplash.this, ActivityMain.class);
                } else {
                    intent = new Intent(ActivitySplash.this, ActivityPrivacyPolicy.class);
                }
            }
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAdLeftApp = true;
        launchHandler.removeCallbacks(launchRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAdLeftApp = true;
        launchHandler.removeCallbacks(launchRunnable);
    }
}