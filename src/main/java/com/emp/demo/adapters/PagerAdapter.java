package com.emp.demo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.emp.demo.fragments.ChannelsFragment;
import com.emp.demo.fragments.FeaturedFragment;
import com.emp.demo.fragments.SeriesFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FeaturedFragment tab1 = new FeaturedFragment();
                return tab1;
            case 1:
               ChannelsFragment tab2 = new ChannelsFragment();
              return tab2;
            case 2:
                SeriesFragment tab3 = new SeriesFragment();
               return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}