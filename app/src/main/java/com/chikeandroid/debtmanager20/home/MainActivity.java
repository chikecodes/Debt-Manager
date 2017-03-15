package com.chikeandroid.debtmanager20.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.home.adapter.FragmentPagerAdapter;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_main);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        tabLayout.setupWithViewPager(viewPager);

    }
}
