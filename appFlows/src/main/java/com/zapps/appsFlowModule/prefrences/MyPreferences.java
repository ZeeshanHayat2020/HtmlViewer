package com.zapps.appsFlowModule.prefrences;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

public class MyPreferences {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "com.zapps.appsFlowModule.database";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String KEY_PREF_LANGUAGE_SELECTED = "KEY_PREF_LANGUAGE_SELECTED";
    private static final String KEY_PREF_PRIVACY_POLICY_ACCEPTANCE = "KEY_PREF_PRIVACEY_POLICY_ACCEPTANCE";
    private static final String KEY_PREF_CONCERN_FORM_ACCEPTANCE = "KEY_PREF_CONCERN_FORM_ACCEPTANCE";
    private static final String KEY_PREF_IN_APP_IS_ITEM_PURCHASE = "KEY_PREF_IN_APP_IS_ITEM_PURCHASE";
    private static final String KEY_PREF_ENABLE_FIREWALL = "KEY_PREF_APP_ENABLE_FIREWALL";
    private static final String KEY_PREF_ALLOW_USAGE_ACCESS = "KEY_PREF_ALLOW_USAGE_ACCESS";
    private static final String KEY_PREF_ALLOW_OVERLAY_ACCESS = "KEY_PREF_ALLOW_OVERLAY_ACCESS";

    public MyPreferences(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLanguageSelected(boolean languageSelected) {
        editor.putBoolean(KEY_PREF_LANGUAGE_SELECTED, languageSelected);
        editor.commit();
    }

    public boolean isLanguageSelected() {
        return pref.getBoolean(KEY_PREF_LANGUAGE_SELECTED, false);
    }

    public void setPrivacyPolicyAcceptance(boolean isAccepted) {
        editor.putBoolean(KEY_PREF_PRIVACY_POLICY_ACCEPTANCE, isAccepted);
        editor.commit();
    }

    public boolean isPrivacyPolicyAccepted() {
        return pref.getBoolean(KEY_PREF_PRIVACY_POLICY_ACCEPTANCE, false);
    }

    public void setConcernFormAcceptance(boolean isAccepted) {
        editor.putBoolean(KEY_PREF_CONCERN_FORM_ACCEPTANCE, isAccepted);
        editor.commit();
    }

    public boolean isConcernFormAccepted() {
        return pref.getBoolean(KEY_PREF_CONCERN_FORM_ACCEPTANCE, false);
    }

    public void setIsItemPurchased(boolean isItemPurchased) {
        editor.putBoolean(KEY_PREF_IN_APP_IS_ITEM_PURCHASE, isItemPurchased);
        editor.commit();
    }

    public boolean isItemPurchased() {
        return pref.getBoolean(KEY_PREF_IN_APP_IS_ITEM_PURCHASE, false);

    }


    public void setFireWall(boolean isEnable) {
        editor.putBoolean(KEY_PREF_ENABLE_FIREWALL, isEnable);
        editor.commit();
        editor.apply();
    }

    public boolean isFireWallEnable() {
        return pref.getBoolean(KEY_PREF_ENABLE_FIREWALL, false);
    }


    public boolean hasUsageAccessEnable() {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public boolean hasOverLayEnable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                return false;
            } else {
                return true;
            }
        } else return true;
    }
}