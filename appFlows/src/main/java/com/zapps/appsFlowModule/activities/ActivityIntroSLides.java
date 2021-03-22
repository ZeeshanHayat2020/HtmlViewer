package com.zapps.appsFlowModule.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.zapps.appsFlowModule.R;
import com.zapps.appsFlowModule.adapters.AdapterPagerIntroSlides;
import com.zapps.appsFlowModule.contstants.Constant;
import com.zapps.appsFlowModule.databinding.ActivityIntroSlidesBinding;
import com.zapps.appsFlowModule.prefrences.MyPreferences;


public class ActivityIntroSLides extends ActivityAppsFlowBase {


    private ActivityIntroSlidesBinding binding;
    private PreferenceManager preferenceManager;
    private TextView[] dots;
    private int[] screensList;
    private AdapterPagerIntroSlides pagerAdapter;
    private MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_slides);
        myPreferences = new MyPreferences(this);
        initViews();
        setUpViewPager();
    }

    public void setStatusBarGradient(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
            window.setNavigationBarColor(this.getResources().getColor(R.color.white));
        }
    }

    private void initViews() {
        binding.acIntroSlideViewPager.setOnPageChangeListener(viewPagerPageChangeListener);
        binding.acIntroSlidesBtnNext.setOnClickListener(onButtonsClicked);
        binding.acIntroSlidesBtnSkip.setOnClickListener(onButtonsClicked);
        binding.tabLayout.setupWithViewPager(binding.acIntroSlideViewPager, true);

    }

    private void setUpViewPager() {
        screensList = new int[]{
                R.layout.layout_intro_one,
                R.layout.layout_intro_two
        };
        pagerAdapter = new AdapterPagerIntroSlides(this, screensList);
        binding.acIntroSlideViewPager.setAdapter(pagerAdapter);
    }

    private void launchPrivacyPolicyScreen(boolean isFirstLaunch) {
        myPreferences.setFirstTimeLaunch(isFirstLaunch);
        Intent intent = null;
        if (myPreferences.isPrivacyPolicyAccepted()) {
            try {
                intent = new Intent(ActivityIntroSLides.this, Class.forName(Constant.ACTIVITY_MAIN));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            intent = new Intent(ActivityIntroSLides.this, ActivityPrivacyPolicy.class);
        }
        startActivity(intent);
        finish();
    }

    private int getItem(int i) {
        return binding.acIntroSlideViewPager.getCurrentItem() + i;
    }

    View.OnClickListener onButtonsClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.acIntroSlides_btnNext) {
                int current = getItem(+1);
                if (current < screensList.length) {
                    binding.acIntroSlideViewPager.setCurrentItem(current);
                } else {
                    launchPrivacyPolicyScreen(false);
                }
            } else if (id == R.id.acIntroSlides_btnSkip) {
                launchPrivacyPolicyScreen(true);
            }
        }
    };

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (position == screensList.length - 1) {
                binding.acIntroSlidesBtnSkip.setVisibility(View.INVISIBLE);
            } else {
                binding.acIntroSlidesBtnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


}