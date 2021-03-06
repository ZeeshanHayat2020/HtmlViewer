package com.zapps.docsReaderModule.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class BaseClass extends AppCompatActivity {

    protected void intentTo(final Context context, final Activity activity) {

        Intent intent = new Intent(context, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
