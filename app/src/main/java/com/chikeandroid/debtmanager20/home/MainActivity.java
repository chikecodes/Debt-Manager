package com.chikeandroid.debtmanager20.home;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.adddebt.AddDebtActivity;
import com.chikeandroid.debtmanager20.databinding.ActivityMainBinding;
import com.chikeandroid.debtmanager20.home.adapter.FragmentPagerAdapter;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = binding.toolbarMainIncluded.toolbarMain;
        setSupportActionBar(toolbar);

        ViewPager viewPager = binding.viewPagerMain;
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));
        viewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = binding.tabLayoutMain;
        tabLayout.setupWithViewPager(viewPager);

        fab = binding.fabMain;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddDebtActivity.class);
                startActivityForResult(intent, AddDebtActivity.REQUEST_ADD_DEBT);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (AddDebtActivity.REQUEST_ADD_DEBT == requestCode && Activity.RESULT_OK == resultCode) {
            Toast.makeText(this, "Debt saved successfully", Toast.LENGTH_LONG).show();

        }
    }
}
