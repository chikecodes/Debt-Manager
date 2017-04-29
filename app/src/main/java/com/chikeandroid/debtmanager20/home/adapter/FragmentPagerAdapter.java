package com.chikeandroid.debtmanager20.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.chikeandroid.debtmanager20.iowe.IOweFragment;
import com.chikeandroid.debtmanager20.oweme.OweMeDebtsFragment;
import com.chikeandroid.debtmanager20.people.PeopleFragment;

/**
 * Created by Chike on 3/14/2017.
 * PagerAdapter for MainActivity
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private final String[] mTabTitles = new String[] {"Owe Me", "I Owe", "People"};

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
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
            default:
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }
}
