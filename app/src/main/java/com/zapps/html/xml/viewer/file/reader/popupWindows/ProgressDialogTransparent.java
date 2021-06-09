package com.zapps.html.xml.viewer.file.reader.popupWindows;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.zapps.html.xml.viewer.file.reader.R;
import com.zapps.html.xml.viewer.file.reader.databinding.LayoutProgressDailogTransparentBinding;

public class ProgressDialogTransparent extends Dialog {

    private String TAG = "DialogAPKManagerItemClick";
    private Context context;
    private Activity activity;
    private LayoutProgressDailogTransparentBinding binding;

    public ProgressDialogTransparent(@NonNull Context context, int themeResId, Activity activity) {
        super(context, themeResId);
        this.activity = activity;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_progress_dailog_transparent, null, false);
        setContentView(binding.getRoot());
        setCancelable(true);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.getRoot().setOnClickListener(view -> dismiss());
    }

}
