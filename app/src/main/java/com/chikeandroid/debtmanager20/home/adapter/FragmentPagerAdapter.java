package com.chikeandroid.debtmanager20.home.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.chikeandroid.debtmanager20.iowe.IOweFragment;
import com.chikeandroid.debtmanager20.oweme.OweMeDebtsFragment;
import com.chikeandroid.debtmanager20.people.PeopleFragment;

/**
 * Created by Chike on 3/14/2017.
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] {"Owe Me", "I Owe", "People"};
    private Context mContext;

    public FragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = OweMeDebtsFragment.newInstance();
                break;
            case 1:
                fragment = IOweFragment.newInstance();
                break;
            case 2:
                fragment = PeopleFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
