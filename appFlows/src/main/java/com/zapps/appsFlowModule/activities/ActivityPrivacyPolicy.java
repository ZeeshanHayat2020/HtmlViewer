package com.zapps.appsFlowModule.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;

import com.zapps.appsFlowModule.R;
import com.zapps.appsFlowModule.contstants.Constant;
import com.zapps.appsFlowModule.databinding.ActivityPrivacyPolicyBinding;
import com.zapps.appsFlowModule.interfaces.MyOnClickListener;
import com.zapps.appsFlowModule.prefrences.MyPreferences;
import com.zapps.appsFlowModule.utilsFlows.DialogCosentForm;


public class ActivityPrivacyPolicy extends ActivityAppsFlowBase implements MyOnClickListener {

    private ActivityPrivacyPolicyBinding privacyPolicyBinding;
    private MyPreferences myPreferences;
    DialogCosentForm dialogCosentForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this);
        privacyPolicyBinding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        myPreferences = new MyPreferences(this);
        privacyPolicyBinding.setClickHandler(this);
        initViews();
    }

    public static void setStatusBarGradient(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
            window.setNavigationBarColor(activity.getResources().getColor(R.color.white));
        }
    }

    private void initViews() {
        dialogCosentForm = new DialogCosentForm(this, 0, this, myPreferences);
        dialogCosentForm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCosentForm.setCancelable(false);
        if (!myPreferences.isConcernFormAccepted()) {
            dialogCosentForm.show();
        }

        setPrivacyPolicyLink();
        privacyPolicyBinding.checkboxConfirmPrivacy.setOnCheckedChangeListener(onCheckBoxChangeListener);
        enableAcceptBtn();

    }

    private void setPrivacyPolicyLink() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            privacyPolicyBinding.tvLink.setText(Html.fromHtml(getString(R.string.privacy_policy_linkText), Html.FROM_HTML_MODE_LEGACY));
        } else {
            privacyPolicyBinding.tvLink.setText(Html.fromHtml(getString(R.string.privacy_policy_linkText)));
        }
        privacyPolicyBinding.tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.privacy_policy_URL);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    private void startMainActivity() {
        Intent intent = null;
        try {
            intent = new Intent(ActivityPrivacyPolicy.this, Class.forName(Constant.ACTIVITY_MAIN));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(intent);
        this.finish();
    }

    private void setAcceptance(boolean isAccept) {
        myPreferences.setPrivacyPolicyAcceptance(isAccept);
        myPreferences.setConcernFormAcceptance(isAccept);
        if (isAccept) {
            startMainActivity();
        } else {
            this.finish();
        }
    }

    private void enableAcceptBtn() {
        privacyPolicyBinding.btnPrivacyAccept.setEnabled(privacyPolicyBinding.checkboxConfirmPrivacy.isChecked());
        if (privacyPolicyBinding.btnPrivacyAccept.isEnabled()) {
            privacyPolicyBinding.btnPrivacyAccept.setAlpha(1f);
        } else {
            privacyPolicyBinding.btnPrivacyAccept.setAlpha(0.5f);
        }
    }

    CompoundButton.OnCheckedChangeListener onCheckBoxChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            enableAcceptBtn();
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPrivacyAccept) {
            setAcceptance(true);
        } else if (v.getId() == R.id.btnPrivacyDecline) {
            setAcceptance(false);
        }

    }


}