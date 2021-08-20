package com.example.utilities;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.utilities.Utility.Preferences;
import com.example.utilities.Utility.Utils;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    static Preferences pref = new Preferences();
    static Utils utils = new Utils();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, pref);
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

        SwitchPreferenceCompat pred;
        ListPreference list_themes;

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            pred = findPreference("pred");
            list_themes = findPreference("list_themes");
            Objects.requireNonNull(pred).setOnPreferenceChangeListener((preference, newValue) -> {
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:

                    case Configuration.UI_MODE_NIGHT_NO:
                        list_themes.setValueIndex(0);
                        break;

                    case Configuration.UI_MODE_NIGHT_YES:
                        list_themes.setValueIndex(1);
                        break;
                }
                if ((Boolean) newValue) {
                    utils.saveData(requireActivity(), pref, pred, list_themes);
                    utils.goToMainActivity(requireActivity());
                } else utils.saveData(requireActivity(), pref, pred, list_themes);
                return true;
            });

            Objects.requireNonNull(list_themes).setOnPreferenceChangeListener((preference, newValue) -> {
                utils.saveData(requireActivity(), pref, pred, list_themes);
                //utils.goToMainActivity(requireActivity());
                Log.d("TEMA",(String)newValue);
                if (((String) newValue).equals("light_theme")) utils.changeTheme(requireActivity(),0);
                else utils.changeTheme(requireActivity(),0);
                utils.goToMainActivity(requireActivity());
                return true;
            });
        }

        @Override
        public void onStart() {
            super.onStart();
            pref = utils.loadData(requireActivity(), pref);
            if (!pred.isChecked())
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:

                    case Configuration.UI_MODE_NIGHT_NO:
                        list_themes.setValueIndex(0);
                        break;

                    case Configuration.UI_MODE_NIGHT_YES:
                        list_themes.setValueIndex(1);
                        break;
                }
        }

        @Override
        public void onPause() {
            super.onPause();
            pref = utils.loadData(requireActivity(), pref);
            if (!pred.isChecked())
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:

                    case Configuration.UI_MODE_NIGHT_NO:
                        list_themes.setValueIndex(0);
                        break;

                    case Configuration.UI_MODE_NIGHT_YES:
                        list_themes.setValueIndex(1);
                        break;
                }
        }

        @Override
        public void onResume() {
            super.onResume();
            pref = utils.loadData(requireActivity(), pref);
            if (!pred.isChecked())
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:

                    case Configuration.UI_MODE_NIGHT_NO:
                        list_themes.setValueIndex(0);
                        break;

                    case Configuration.UI_MODE_NIGHT_YES:
                        list_themes.setValueIndex(1);
                        break;
                }
        }

        @Override
        public void onStop() {
            super.onStop();
            pref = utils.loadData(requireActivity(), pref);
            if (!pred.isChecked())
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:

                    case Configuration.UI_MODE_NIGHT_NO:
                        list_themes.setValueIndex(0);
                        break;

                    case Configuration.UI_MODE_NIGHT_YES:
                        list_themes.setValueIndex(1);
                        break;
                }
        }
    }
}