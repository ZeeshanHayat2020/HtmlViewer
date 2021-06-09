package com.zapps.html.xml.viewer.file.reader.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.zapps.docsReaderModule.utils.TaskRetrievePath;
import com.zapps.html.xml.viewer.file.reader.R;
import com.zapps.docsReaderModule.constant.DocsReaderConstant;
import com.zapps.appsFlowModule.contstants.Constant;
import com.zapps.html.xml.viewer.file.reader.adapters.AdapterMainCardMainCardsPager;
import com.zapps.html.xml.viewer.file.reader.appUtils.MyUtils;
import com.zapps.html.xml.viewer.file.reader.databinding.ActivityMainBinding;
import com.zapps.html.xml.viewer.file.reader.interfaces.OnClickMainCardFilesListener;
import com.zapps.docsReaderModule.officereader.ActivityDocumentReader;
import com.zapps.html.xml.viewer.file.reader.appUtils.FileUtils;
import com.zapps.html.xml.viewer.file.reader.models.ModelMainCards;
import com.zapps.html.xml.viewer.file.reader.popupWindows.PopUpWidowMainMenu;
import com.zapps.html.xml.viewer.file.reader.appUtils.AppOpenManager;
import com.zapps.html.xml.viewer.file.reader.popupWindows.ProgressDialogTransparent;
import com.zapps.html.xml.viewer.file.reader.views.FragmentMainCards;
import com.zapps.html.xml.viewer.file.reader.views.TransformerMainCards;
import com.google.android.gms.ads.AdListener;
import com.google.android.material.snackbar.Snackbar;

public class ActivityMain extends ActivityBase implements OnClickMainCardFilesListener {


    private String TAG = this.getClass().getName();
    ActivityMainBinding binding;
    private AdapterMainCardMainCardsPager pagerAdapter;
    private PopUpWidowMainMenu widowMainMenu;
    private String[] itemNames;
    private Intent intent = null;
    private ProgressDialogTransparent transparentBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        reqNewInterstitial(ActivityMain.this);
        initInterstitialListeners();
        if (!hasStoragePermission()) {
            permissionDialog(getString(R.string.msgDialogPermission));
        }
        setUpCardsViewPager();
        widowMainMenu = new PopUpWidowMainMenu(ActivityMain.this, mInterstitialAd);
        binding.btnMenu.setOnClickListener(onClickMenuButton);
        binding.btnAttachFile.setOnClickListener(onClickMenuButton);
        transparentBar = new ProgressDialogTransparent(this, 0, this);
        setUpInAppUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (haveNetworkConnection()) {
            checkForUpdate();
        }
    }

    @Override
    public void onBackPressed() {
        if (widowMainMenu != null && widowMainMenu.getPopupWindow() != null) {
            if (widowMainMenu.getPopupWindow().isShowing()) {
                widowMainMenu.hideMenu();
            } else {
                dialogExitApp();
            }
        } else {
            dialogExitApp();
        }
    }

    private void setUpCardsViewPager() {
        pagerAdapter = new AdapterMainCardMainCardsPager(getSupportFragmentManager());
        itemNames = new String[]{
                getString(R.string.htmlFiles),
                getString(R.string.xmlFiles),
                getString(R.string.recentFiles)
        };
        for (int i = 0; i < itemNames.length; i++) {
            pagerAdapter.addCardFragment(new FragmentMainCards(), new ModelMainCards(itemNames[i]), i);
        }
        float pagesMargin = this.getResources().getDimension(R.dimen._20sdp);
        TransformerMainCards pageTransformerMainCards = new TransformerMainCards(ActivityMain.this, ActivityMain.this, binding.viewPager, pagerAdapter);
        pageTransformerMainCards.enableScaling(true);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setPageTransformer(true, pageTransformerMainCards);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.setPageMargin((int) -pagesMargin);

        binding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pageTransformerMainCards.setOnClickMainCardFilesListener(this::onClick);

    }

    private void intentOnItemClick2(String buttonName) {
        intent = new Intent(ActivityMain.this, ActivityFilesContainer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (buttonName.equals(itemNames[0])) {
            intent.putExtra(Constant.KEY_INTENT_ACTIVITY_CALL, String.valueOf(ActivityCall.HTML_FILE));
        } else if (buttonName.equals(itemNames[1])) {
            intent.putExtra(Constant.KEY_INTENT_ACTIVITY_CALL, String.valueOf(ActivityCall.XML_FILES));
        } else if (buttonName.equals(itemNames[2])) {
            intent.putExtra(Constant.KEY_INTENT_ACTIVITY_CALL, String.valueOf(ActivityCall.RECENT_FILES));
        }
        showInterstitialAd(intent);
  /*      if (mInterstitialAd != null) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                AppOpenManager.isInterstitialShowing = true;
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        reqNewInterstitial(ActivityMain.this);
                        AppOpenManager.isInterstitialShowing = false;
                        if (!buttonName.equals("")) {
                            startActivity(intent);
                        }
                    }
                });
            } else {
                if (!buttonName.equals("")) {
                    startActivity(intent);
                }
            }
        }*/
    }

    private View.OnClickListener onClickMenuButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnAttachFile: {
                    attachFile();
                }
                break;
                case R.id.btnMenu: {
                    widowMainMenu.showMenu(view);
                }
                break;
            }
        }
    };

    private void initInterstitialListeners() {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                reqNewFacebookInterstitial(intiFacebookAdListeners(), 0);

            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                AppOpenManager.isInterstitialShowing = false;
                reqNewInterstitial(ActivityMain.this);
                initInterstitialListeners();
                if (intent != null)
                    startActivity(intent);
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                AppOpenManager.isInterstitialShowing = true;
            }
        });
    }

    private InterstitialAdListener intiFacebookAdListeners() {
        return new InterstitialAdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "onError: FaceBook Error");
                reqNewInterstitial(ActivityMain.this);
                initInterstitialListeners();
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
                AppOpenManager.isInterstitialShowing = true;
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                AppOpenManager.isInterstitialShowing = false;
                reqNewFacebookInterstitial(intiFacebookAdListeners(), 1);
                if (intent != null) {
                    startActivity(intent);
                }
            }
        };
    }

    private void showInterstitialAd(Intent intent) {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else if (mFacebookIntersAd != null && mFacebookIntersAd.isAdLoaded() && !mFacebookIntersAd.isAdInvalidated()) {
            mFacebookIntersAd.show();
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQUEST_PERMISSION_STORAGE: {
                if (grantResults.length > 0) {
                    boolean writExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writExternalStorage && readExternalStorage) {

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) && shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                permissionDialog(getString(R.string.msgDialogPermission));
                            } else {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, Constant.REQUEST_INTENT_TO_PERMISSION_SETTING);
                            }
                        }
                    }
                }
            }
            break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CODE_FOR_IN_APP_UPDATE: {
                if (resultCode == RESULT_CANCELED) {
                    this.finish();
                } else if (resultCode == RESULT_OK) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar
                            .make(parentLayout, "Application updated successfully.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
            break;
            case Constant.REQUEST_CODE_ATTACH_FILE: {
                if (RESULT_OK == resultCode) {
               /*     Uri uri = data.getData();
                    String filePath = "";
                    filePath = FileUtils.getPath(ActivityMain.this, uri);
                    if (!filePath.equals("")) {
                        final Intent intent = new Intent(ActivityMain.this, ActivityDocumentReader.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(DocsReaderConstant.KEY_INTENT_SELECTED_FILE_PATH, filePath);
                        startActivity(intent);
                    }
*/
                    String filePath = "";
                    Uri tempUri = data.getData();
                    if (tempUri != null) {
                        TaskRetrievePath taskRetrievePath = new TaskRetrievePath(ActivityMain.this, tempUri);
                        Thread thread = new Thread(taskRetrievePath);
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        filePath = taskRetrievePath.getFilePath();
                        final Intent intent = new Intent(ActivityMain.this, ActivityDocumentReader.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (filePath == null) {
                            filePath = FileUtils.getPath(ActivityMain.this, tempUri);
                        } else if (filePath.equals("")) {
                            filePath = FileUtils.getPath(ActivityMain.this, tempUri);
                        } else if (TextUtils.isEmpty(filePath)) {
                            filePath = FileUtils.getPath(ActivityMain.this, tempUri);
                        }
                        intent.putExtra(DocsReaderConstant.KEY_INTENT_SELECTED_FILE_PATH, filePath);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Sorry, Can't open file.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case Constant.REQUEST_INTENT_TO_PERMISSION_SETTING: {
                if (!hasStoragePermission()) {
                    permissionDialog(getString(R.string.msgDialogPermission));
                }
            }
            break;
        }

    }

    @Override
    public void onClick(View v, String fileName) {
        if (hasStoragePermission()) {
            transparentBar.show();
            intentOnItemClick2(fileName);
        } else {
            permissionDialog(getString(R.string.msgDialogPermission));
        }
    }

    @Override
    protected void onDestroy() {
        if (mFacebookIntersAd != null) {
            mFacebookIntersAd.destroy();
        }
        if (mInterstitialAd != null) {
            mInterstitialAd = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (transparentBar != null && transparentBar.isShowing()) {
            transparentBar.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        transparentBar.dismiss();
        if (intent != null) {
            intent = null;
        }
    }

}