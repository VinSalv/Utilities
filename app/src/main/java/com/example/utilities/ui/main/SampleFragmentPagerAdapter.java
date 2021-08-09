package com.example.utilities.ui.main;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private final String[] tabTitles = new String[]{"Tab1", "Tab2", "Tab3"};

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
