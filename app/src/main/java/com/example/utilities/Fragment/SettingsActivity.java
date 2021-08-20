package com.example.utilities.Fragment;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.utilities.R;
import com.example.utilities.Utility.Preferences;
import com.example.utilities.Utility.Utils;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    static Preferences pref;
    static Utils utils = new Utils();
    protected Configuration mPrevConfig;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferences());
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
        mPrevConfig = new Configuration(getResources().getConfiguration());

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configurationChanged(newConfig);
        mPrevConfig = new Configuration(newConfig);
    }

    protected void configurationChanged(Configuration newConfig) {
        if (isNightConfigChanged(newConfig) && pref.getPredBool()) {
            utils.goToMainActivity(this);
        }
    }

    protected boolean isNightConfigChanged(Configuration newConfig) {
        return (newConfig.diff(mPrevConfig) & ActivityInfo.CONFIG_UI_MODE) != 0 && isOnDarkMode(newConfig) != isOnDarkMode(mPrevConfig);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        SwitchPreferenceCompat pred;
        ListPreference list_themes;

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            pref = utils.loadData(requireActivity(), new Preferences());
            pred = findPreference("pred");
            list_themes = findPreference("list_themes");
            if (!pref.getPredBool()) {
                switch (pref.getThemeText()) {
                    case "LightTheme":
                        list_themes.setValueIndex(0);
                        break;
                    case "DarkTheme":
                        list_themes.setValueIndex(1);
                        break;
                }
            } else {
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
                    utils.saveData(requireActivity(), pref, (Boolean) newValue, list_themes.getValue());
                    utils.goToMainActivity(requireActivity());
                } else
                    utils.saveData(requireActivity(), pref, (Boolean) newValue, list_themes.getValue());
                return true;
            });

            Objects.requireNonNull(list_themes).setOnPreferenceChangeListener((preference, newValue) -> {
                utils.saveData(requireActivity(), pref, pred.isChecked(), newValue.toString());
                utils.goToMainActivity(requireActivity());
                return true;
            });
        }
    }
}