package com.app.utilities.fragment.ui;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.utilities.R;
import com.app.utilities.fragment.Fragment_Misurazione;
import com.app.utilities.fragment.Fragment_Sensori;
import com.app.utilities.fragment.Fragment_Sorte;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    @SuppressWarnings("unused")
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Fragment_Misurazione();
                break;
            case 1:
                fragment = new Fragment_Sorte();
                break;
            case 2:
                fragment = new Fragment_Sensori();
                break;
        }
        return Objects.requireNonNull(fragment);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}