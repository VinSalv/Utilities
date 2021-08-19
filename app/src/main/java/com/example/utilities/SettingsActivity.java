package com.example.utilities;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.utilities.Utility.Utils;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    static Utils utils = new Utils();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat pred = findPreference("pred");
            ListPreference prefs = findPreference("list_themes");

            Objects.requireNonNull(pred).setOnPreferenceChangeListener((preference, newValue) -> {
                utils.goToMainActivity(requireActivity());

                return true;
            });

            Objects.requireNonNull(prefs).setOnPreferenceChangeListener((preference, newValue) -> {
                utils.goToMainActivity(requireActivity());
                return true;
            });
        }


    }
}