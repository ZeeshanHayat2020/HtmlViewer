package com.zapps.appsFlowModule.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;

import androidx.appcompat.app.AppCompatActivity;

import com.zapps.appsFlowModule.utilsFlows.LanguageManager;


public class ActivityAppsFlowBase extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageManager.setLocale(base));
    }



}
