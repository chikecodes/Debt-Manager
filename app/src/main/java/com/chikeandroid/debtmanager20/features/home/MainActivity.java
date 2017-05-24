package com.chikeandroid.debtmanager20.features.home;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.test.espresso.IdlingResource;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.features.addeditdebt.AddEditDebtActivity;
import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.databinding.ActivityMainBinding;
import com.chikeandroid.debtmanager20.event.MainViewPagerSwipeEvent;
import com.chikeandroid.debtmanager20.features.home.adapter.HomeFragmentPagerAdapter;
import com.chikeandroid.debtmanager20.util.EspressoIdlingResource;
import com.chikeandroid.debtmanager20.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;


public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    public static final String EXTRA_DEBT_TYPE = "com.chikeandroid.debtmanager20.features.home.debt_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = binding.toolbarMainIncluded.toolbarMain;
        setSupportActionBar(toolbar);

        mViewPager = binding.viewPagerMain;
        mViewPager.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = binding.tabLayoutMain;
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = binding.fabMain;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddEditDebtActivity.start(MainActivity.this, AddEditDebtActivity.REQUEST_ADD_DEBT);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            // optional
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                EventBus.getDefault().post(new MainViewPagerSwipeEvent("Swiped"));
            } });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (AddEditDebtActivity.REQUEST_ADD_DEBT == requestCode && Activity.RESULT_OK == resultCode) {

            ViewUtil.showToast(this, getString(R.string.msg_debt_save_success));

            if (data != null && data.getIntExtra(EXTRA_DEBT_TYPE, -1) == Debt.DEBT_TYPE_IOWE) {
                mViewPager.setCurrentItem(1, true);
            }else if (data != null && data.getIntExtra(EXTRA_DEBT_TYPE, -1) == Debt.DEBT_TYPE_OWED) {
                mViewPager.setCurrentItem(0, true);
            }
        }
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
