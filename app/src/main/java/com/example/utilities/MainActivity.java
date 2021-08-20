package com.example.utilities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.utilities.Fragment.ui.SectionsPagerAdapter;
import com.example.utilities.Utility.Preferences;
import com.example.utilities.Utility.Utils;
import com.example.utilities.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private final Utils utils = new Utils();
    Preferences pref = new Preferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, pref);
        if (!pref.getPredBool())
            if (pref.getThemeText().equals("light_theme")) utils.changeTheme(this, 0);
            else utils.changeTheme(this, 1);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> utils.goToInfoActivity(MainActivity.this));
    }

    @Override
    public void onStart() {
        super.onStart();
        pref = utils.loadData(this, pref);
        if (!pref.getPredBool())
            if (pref.getThemeText().equals("light_theme")) utils.changeTheme(this, 0);
            else utils.changeTheme(this, 1);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> utils.goToInfoActivity(MainActivity.this));
    }

    @Override
    public void onPause() {
        super.onPause();
        pref = utils.loadData(this, pref);
    }

    @Override
    public void onResume() {
        super.onResume();
        pref = utils.loadData(this, pref);
    }

    @Override
    public void onStop() {
        super.onStop();
        pref = utils.loadData(this, pref);
    }
}