package com.chikeandroid.debtmanager20.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        TabLayout tabLayout = binding.tabLayoutMain;
        tabLayout.setupWithViewPager(viewPager);

        fab = binding.fabMain;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDebtIntent = new Intent(MainActivity.this, AddDebtActivity.class);
                startActivity(addDebtIntent);
            }
        });

    }
}
