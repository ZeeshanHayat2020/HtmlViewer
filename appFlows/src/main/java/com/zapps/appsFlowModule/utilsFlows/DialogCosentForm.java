package com.zapps.appsFlowModule.utilsFlows;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.zapps.appsFlowModule.R;
import com.zapps.appsFlowModule.contstants.Constant;
import com.zapps.appsFlowModule.databinding.LayoutDialogConsentFormBinding;
import com.zapps.appsFlowModule.interfaces.MyOnClickListener;
import com.zapps.appsFlowModule.prefrences.MyPreferences;

public class DialogCosentForm extends Dialog implements MyOnClickListener {


    private String TAG = "DialogAPKManagerItemClick";
    private Context context;
    private Activity activity;
    private LayoutDialogConsentFormBinding binding;
    private MyPreferences myPreferences;


    public DialogCosentForm(@NonNull Context context, int themeResId, Activity activity, MyPreferences myPreferences) {
        super(context, themeResId);
        this.activity = activity;
        this.context = context;
        this.myPreferences = myPreferences;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_consent_form);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_dialog_consent_form, null, false);
        setContentView(binding.getRoot());
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        binding.setClickHandler(this::onClick);

    }

    private void setFormAcceptance(boolean isAccept) {
        myPreferences.setPrivacyPolicyAcceptance(isAccept);
        myPreferences.setConcernFormAcceptance(isAccept);
        if (isAccept) {
            Intent intent = null;
            try {
                intent = new Intent(context, Class.forName(Constant.ACTIVITY_MAIN));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            context.startActivity(intent);
            activity.finish();

        } else {
            activity.finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnConserContinue) {
            setFormAcceptance(true);
        } else if (id == R.id.btnConserExit) {
            setFormAcceptance(false);
        } else if (id == R.id.btnConserPrivacy) {
            this.dismiss();
        }
    }
}
