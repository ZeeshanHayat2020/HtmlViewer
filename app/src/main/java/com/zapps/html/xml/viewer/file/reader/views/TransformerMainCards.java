package com.zapps.html.xml.viewer.file.reader.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.zapps.html.xml.viewer.file.reader.R;
import com.zapps.html.xml.viewer.file.reader.adapters.AdapterMainCardMainCardsPager;
import com.zapps.html.xml.viewer.file.reader.interfaces.InterfaceAdapterMainCard;
import com.zapps.html.xml.viewer.file.reader.interfaces.OnClickMainCardFilesListener;

public class TransformerMainCards implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    private Context context;
    private Activity activity;
    private ViewPager viewPager;
    private AdapterMainCardMainCardsPager cardAdapter;
    private float lastOffset;
    private boolean scalingEnabled;
    private String TAG = "Transformer";
    private InterstitialAd mInterstitialAd;
    private OnClickMainCardFilesListener onClickMainCardFilesListener;

    public TransformerMainCards(Context context, Activity activity, ViewPager viewPager, AdapterMainCardMainCardsPager adapter) {
        this.context = context;
        this.activity = activity;
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        cardAdapter = adapter;
        reqNewInterstitial(context);
    }

    public void setOnClickMainCardFilesListener(OnClickMainCardFilesListener onClickMainCardFilesListener) {
        this.onClickMainCardFilesListener = onClickMainCardFilesListener;
    }

    public void enableScaling(boolean enable) {
        if (scalingEnabled && !enable) {
            // shrink main card
            CardView currentCard = cardAdapter.getCardViewAt(viewPager.getCurrentItem());
            if (currentCard != null) {
                currentCard.animate().scaleY(1);
                currentCard.animate().scaleX(1);
                Log.d(TAG, "enableScaling: shrink Main Card Called.");
            }
        } else if (!scalingEnabled && enable) {
            // grow main card
            CardView currentCard = cardAdapter.getCardViewAt(viewPager.getCurrentItem());
            if (currentCard != null) {
                //enlarge the current item
                currentCard.animate().scaleY(1.1f);
                currentCard.animate().scaleX(1.1f);
                Log.d(TAG, "enableScaling: Enlarge Main Card Called.");
            }
        }
        scalingEnabled = enable;
    }

    @Override
    public void transformPage(View page, float position) {
        @SuppressLint("RtlGetPadding") int pageWidth = viewPager.getMeasuredWidth() - viewPager.getPaddingLeft() - viewPager.getPaddingRight();
        int pageHeight = viewPager.getHeight();
        @SuppressLint("RtlGetPadding") int paddingLeft = viewPager.getPaddingLeft();
        float transformPos = (float) (page.getLeft() - (viewPager.getScrollX() + paddingLeft)) / pageWidth;

        final float normalizedposition = Math.abs(Math.abs(transformPos) - 1);
        page.setAlpha(normalizedposition + 0.3f);
        int max = -pageHeight / 12;
        if (transformPos < -1) {
            page.setTranslationY(0);

        } else if (transformPos <= 1) {
            page.setTranslationY(max * (1 - Math.abs(transformPos)));
        } else {
            page.setTranslationY(0);
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int realCurrentPosition;
        int nextPosition;
        float realOffset;
        boolean goingLeft = lastOffset > positionOffset;
        if (goingLeft) {
            realCurrentPosition = position + 1;
            nextPosition = position;
            realOffset = 1 - positionOffset;
        } else {
            nextPosition = position + 1;
            realCurrentPosition = position;
            realOffset = positionOffset;
        }
        if (nextPosition > cardAdapter.getCount() - 1
                || realCurrentPosition > cardAdapter.getCount() - 1) {
            return;
        }
        CardView currentCard = cardAdapter.getCardViewAt(realCurrentPosition);
        if (currentCard != null) {
            if (scalingEnabled) {
                currentCard.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
                currentCard.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
            }

            currentCard.setCardElevation((InterfaceAdapterMainCard.BASE_ELEVATION + InterfaceAdapterMainCard.BASE_ELEVATION
                    * (InterfaceAdapterMainCard.MAX_ELEVATION_FACTOR - 1) * (1 - realOffset)));

            for (int c = 0; c < currentCard.getChildCount(); c++) {
                RelativeLayout rootViewFiles = (RelativeLayout) currentCard.getChildAt(c);
                rootViewFiles.setOnClickListener(view -> {
                    TextView childTextView = (TextView) (rootViewFiles.findViewById(R.id.itemCards_tv));
                    String fileName = (String) (childTextView.getText());
                    if (onClickMainCardFilesListener != null) {
                        onClickMainCardFilesListener.onClick(view, fileName);
                    }
                });
            }
        }
        CardView nextCard = cardAdapter.getCardViewAt(nextPosition);
        if (nextCard != null) {
            if (scalingEnabled) {
                nextCard.setScaleX((float) (1 + 0.1 * (realOffset)));
                nextCard.setScaleY((float) (1 + 0.1 * (realOffset)));
            }
            nextCard.setCardElevation((InterfaceAdapterMainCard.BASE_ELEVATION + InterfaceAdapterMainCard.BASE_ELEVATION
                    * (InterfaceAdapterMainCard.MAX_ELEVATION_FACTOR - 1) * (realOffset)));

        }
        lastOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void intentToNext(Activity activity) {
        final Intent intent = new Intent(context, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    context.startActivity(intent);
                }
            });
            reqNewInterstitial(context);
        } else {
            context.startActivity(intent);
        }

    }


/*
    public void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    Constant.REQUEST_PERMISSION_STORAGE);
            return;
        }
    }

    public void checkPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_PHONE_STATE
                    },
                    Constant.REQUEST_PERMISSIONS_PHONE_STATE);
            return;
        }
    }

    public void checkRecordingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.RECORD_AUDIO
                    },
                    Constant.REQUEST_PERMISSIONS_RECORD_AUDIO);
            return;
        }
    }

    public boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else
                return true;
        } else return true;
    }

    public boolean hasPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else
                return true;
        } else return true;
    }

    public boolean hasRecordAudioPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else
                return true;
        } else return true;
    }
*/


    public void reqNewInterstitial(Context context) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.interstitial_Id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

}