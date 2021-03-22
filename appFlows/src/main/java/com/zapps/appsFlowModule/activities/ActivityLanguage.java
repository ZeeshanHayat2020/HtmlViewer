package com.zapps.appsFlowModule.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;


import com.zapps.appsFlowModule.R;
import com.zapps.appsFlowModule.contstants.Constant;
import com.zapps.appsFlowModule.databinding.ActivityLanguageBinding;
import com.zapps.appsFlowModule.databinding.LayoutCustomToolbarBinding;
import com.zapps.appsFlowModule.prefrences.MyPreferences;
import com.zapps.appsFlowModule.utilsFlows.LanguageManager;

import java.util.Locale;


public class ActivityLanguage extends ActivityAppsFlowBase implements View.OnClickListener {
    private MyPreferences myPreferences;
    private ActivityLanguageBinding activityMyLanguageBinding;
    private LayoutCustomToolbarBinding toolbarBinding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setStatusBarGradient(this);
        activityMyLanguageBinding = DataBindingUtil.setContentView(this, R.layout.activity_language);
        toolbarBinding = activityMyLanguageBinding.toolBar;
        iniViews();
    }


    public void setStatusBarGradient(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
            window.setNavigationBarColor(this.getResources().getColor(R.color.white));
        }
    }

    private void iniViews() {
        myPreferences = new MyPreferences(this);
        toolbarBinding.llCustomToolBarTvTitle.setText("Select Language");
        activityMyLanguageBinding.acLanguageBtnEnglish.setOnClickListener(this);
        activityMyLanguageBinding.acLanguageBtnArabic.setOnClickListener(this);
        activityMyLanguageBinding.acLanguageBtnChina.setOnClickListener(this);
        activityMyLanguageBinding.acLanguageBtnGerman.setOnClickListener(this);
        activityMyLanguageBinding.acLanguageBtnIndonesian.setOnClickListener(this);
        activityMyLanguageBinding.acLanguageBtnItalian.setOnClickListener(this);
        activityMyLanguageBinding.acLanguageBtnPolish.setOnClickListener(this);
        activityMyLanguageBinding.acLanguageBtnPortuguese.setOnClickListener(this);
        activityMyLanguageBinding.acLanguageBtnRussian.setOnClickListener(this);
        activityMyLanguageBinding.acLanguageBtnSpanish.setOnClickListener(this);

        Locale locale = LanguageManager.getLocale(this.getResources());
        switch (locale.getLanguage()) {
            case LanguageManager.LANGUAGE_KEY_ENGLISH: {
                activityMyLanguageBinding.acLanguageBtnEnglish.setPressed(true);
            }
            break;
            case LanguageManager.LANGUAGE_KEY_ARABIC: {

                activityMyLanguageBinding.acLanguageBtnArabic.setPressed(true);
            }
            break;
            case LanguageManager.LANGUAGE_KEY_CHINES: {

                activityMyLanguageBinding.acLanguageBtnChina.setPressed(true);
            }
            break;
            case LanguageManager.LANGUAGE_KEY_GERMAN: {

                activityMyLanguageBinding.acLanguageBtnGerman.setPressed(true);
            }
            break;
            case LanguageManager.LANGUAGE_KEY_INDONESIAN: {

                activityMyLanguageBinding.acLanguageBtnIndonesian.setPressed(true);
            }
            break;
            case LanguageManager.LANGUAGE_KEY_ITALIAN: {

                activityMyLanguageBinding.acLanguageBtnItalian.setPressed(true);
            }
            break;
            case LanguageManager.LANGUAGE_KEY_POLISH: {

                activityMyLanguageBinding.acLanguageBtnPolish.setPressed(true);
            }
            break;
            case LanguageManager.LANGUAGE_KEY_PORTUGUESE: {

                activityMyLanguageBinding.acLanguageBtnPortuguese.setPressed(true);
            }
            break;
            case LanguageManager.LANGUAGE_KEY_RUSSIA: {

                activityMyLanguageBinding.acLanguageBtnRussian.setPressed(true);
            }
            break;
            case LanguageManager.LANGUAGE_KEY_SPANISH: {

                activityMyLanguageBinding.acLanguageBtnSpanish.setPressed(true);
            }
            break;
            default:
                activityMyLanguageBinding.acLanguageBtnEnglish.setPressed(true);

        }
    }

    private void setLanguage(boolean isSelected) {
        myPreferences.setLanguageSelected(isSelected);
        startIntroSlidesActivity();
    }

    private void setNewLocal(String languageKey) {
        LanguageManager.setNewLocale(this, languageKey);
    }

    private void startIntroSlidesActivity() {
        Intent intent = null;
        if (myPreferences.isFirstTimeLaunch()) {
            intent = new Intent(ActivityLanguage.this, ActivityIntroSLides.class);
        } else {
            if (myPreferences.isPrivacyPolicyAccepted()) {
                try {
                    intent = new Intent(ActivityLanguage.this, Class.forName(Constant.ACTIVITY_MAIN));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                intent = new Intent(ActivityLanguage.this, ActivityPrivacyPolicy.class);
            }
        }
        startActivity(intent);
        finish();

    }

    private void reFreshButtons() {
        for (int i = 0; i < activityMyLanguageBinding.acLanguageButtonContainer.getChildCount(); i++) {
            AppCompatButton tempButton = (AppCompatButton) activityMyLanguageBinding.acLanguageButtonContainer.getChildAt(i);
            if (tempButton.isPressed()) {
                tempButton.setPressed(false);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.isPressed()) {
            view.setPressed(false);
            view.performClick();
        } else {
            reFreshButtons();
            int id = view.getId();
            if (id == R.id.acLanguage_btnEnglish) {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_ENGLISH);
            } else if (id == R.id.acLanguage_btnArabic) {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_ARABIC);
            } else if (id == R.id.acLanguage_btnChina) {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_CHINES);
            } else if (id == R.id.acLanguage_btnGerman) {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_GERMAN);
            } else if (id == R.id.acLanguage_btnIndonesian) {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_INDONESIAN);
            } else if (id == R.id.acLanguage_btnItalian) {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_ITALIAN);
            } else if (id == R.id.acLanguage_btnPolish) {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_POLISH);
            } else if (id == R.id.acLanguage_btnPortuguese) {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_PORTUGUESE);
            } else if (id == R.id.acLanguage_btnRussian) {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_RUSSIA);
            } else if (id == R.id.acLanguage_btnSpanish) {
                setLanguage(true);
                setNewLocal(LanguageManager.LANGUAGE_KEY_SPANISH);
            }
        }
    }
}