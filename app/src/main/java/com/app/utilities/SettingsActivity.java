package com.app.utilities;

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

import com.app.utilities.utility.Preferences;
import com.app.utilities.utility.Utils;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    static final Utils utils = new Utils();
    static Preferences pref;
    protected Configuration mPrevConfig;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferences());
        if (!pref.getPredBool()) {
            if (pref.getThemeText().equals("LightTheme") || pref.getThemeText().equals("LightThemeSelected"))
                utils.changeThemeSelected(this, 0);
            else
                utils.changeThemeSelected(this, 1);
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                case Configuration.UI_MODE_NIGHT_NO:
                    utils.changeTheme(this, 0);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    utils.changeTheme(this, 1);
                    break;
            }
        }
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
            utils.refreshActivity(this);
        }
    }

    protected boolean isNightConfigChanged(Configuration newConfig) {
        return (newConfig.diff(mPrevConfig) & ActivityInfo.CONFIG_UI_MODE) != 0 && isOnDarkMode(newConfig) != isOnDarkMode(mPrevConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        utils.goToMainActivity(this);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        SwitchPreferenceCompat pred;
        ListPreference list_themes;

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            pref = utils.loadData(requireActivity(), new Preferences());
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            pred = findPreference("pred");
            list_themes = findPreference("list_themes");
            if (!pref.getPredBool()) {
                switch (pref.getThemeText()) {
                    case "LightThemeSelected":
                    case "LightTheme":
                        list_themes.setValueIndex(0);
                        break;
                    case "DarkThemeSelected":
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
                    utils.refreshActivity(requireActivity());
                } else
                    utils.saveData(requireActivity(), pref, (Boolean) newValue, list_themes.getValue());
                return true;
            });
            Objects.requireNonNull(list_themes).setOnPreferenceChangeListener((preference, newValue) -> {
                utils.saveData(requireActivity(), pref, pred.isChecked(), newValue.toString());
                utils.refreshActivity(requireActivity());
                return true;
            });
        }
    }
}