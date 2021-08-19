package com.example.utilities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.example.utilities.Fragment.ui.SectionsPagerAdapter;
import com.example.utilities.Utility.Utils;
import com.example.utilities.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private final Utils util = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String themeName = pref.getString("list_themes", "light_theme");
        if (!pref.getBoolean("pred", true)) {
            if (themeName.equals("light_theme")) {
                util.notifyUser(this, "Tema chiaro");
                changeTheme(1);
            } else if (themeName.equals("dark_theme")) {
                util.notifyUser(this, "Tema scuro");
                changeTheme(2);
            }
        } else {

            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_NO:
                    changeTheme(1);
                    break;

                case Configuration.UI_MODE_NIGHT_YES:
                    changeTheme(2);
                    break;

                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    changeTheme(1);
                    break;
            }
        }
        com.example.utilities.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> util.goToInfoActivity(MainActivity.this));
    }

    public void changeTheme(int i) {
        switch (i) {
            case 1:
                setTheme(R.style.LightTheme);
                break;
            case 2:
                setTheme(R.style.DarkTheme);
                break;
        }

    }

}