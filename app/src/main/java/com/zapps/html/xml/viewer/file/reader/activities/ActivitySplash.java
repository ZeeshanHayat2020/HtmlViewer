package com.zapps.html.xml.viewer.file.reader.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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

    private static final String TAG = "ActivitySplash";
    private ActivitySplashBinding activitySplashBinding;
    private Handler launchHandler;
    private Runnable launchRunnable;
    private MyPreferences myPreferences;
    int currentApiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        MobileAds.initialize(this, getResources().getString(R.string.app_Id));
        reqNewInterstitial(this);
        initInterstitialListeners();
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (haveNetworkConnection()) {
            showInterstitial();
        } else {
            launchWithDelay();
        }
    }

    private void initViews() {
        AppOpenManager.isInterstitialShowing = true;
        myPreferences = new MyPreferences(this);
    }

    private void showInterstitial() {
        launchRunnable = () -> {
            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else if (mFacebookIntersAd != null && mFacebookIntersAd.isAdLoaded() && !mFacebookIntersAd.isAdInvalidated()) {
                mFacebookIntersAd.show();
            } else {
                launchLanguageActivity();
            }

        };
        launchHandler = new Handler(getMainLooper());
        launchHandler.postDelayed(launchRunnable, 8000);
    }

    private void initInterstitialListeners() {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d(TAG, "onAdFailedToLoad: " + loadAdError.getResponseInfo());
                reqNewFacebookInterstitial(intiFacebookAdListeners(), 2);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                launchLanguageActivity();
                reqNewInterstitial(ActivitySplash.this);
                Log.d(TAG, "onAdClosed: ");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.d(TAG, "onAdImpression: ");
            }
        });
    }

    private InterstitialAdListener intiFacebookAdListeners() {
        return new InterstitialAdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                launchLanguageActivity();
            }
        };
    }

    private void launchWithDelay() {
        launchRunnable = this::launchLanguageActivity;
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
        launchHandler.removeCallbacks(launchRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        launchHandler.removeCallbacks(launchRunnable);
        AppOpenManager.isInterstitialShowing = false;
    }
}