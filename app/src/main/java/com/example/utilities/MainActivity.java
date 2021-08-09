package com.example.utilities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.utilities.databinding.ActivityMainBinding;
import com.example.utilities.ui.main.SampleFragmentPagerAdapter;
import com.example.utilities.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    // configure icons
    private final int[] imageResId = {
            R.drawable.icona_di_prova,
            R.drawable.icona_di_prova,
            R.drawable.icona_di_prova};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.utilities.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));
        // Give the TabLayout the ViewPager
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        // setup TabLayout first
        for (int i = 0; i < imageResId.length; i++) {
            Objects.requireNonNull(tabs.getTabAt(i)).setIcon(imageResId[i]);
        }

        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}