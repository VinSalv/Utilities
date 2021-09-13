package com.app.utilities.settings;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.app.utilities.R;
import com.app.utilities.utilitiesActivity.scf.PreferencesSCF;
import com.app.utilities.utility.Utils;

import java.util.Objects;

public class PreferenzeSCF extends AppCompatActivity {
    static final Utils utils = new Utils();
    static Preferenze pref;
    static PreferencesSCF prefSCF;
    protected Configuration mPrevConfig;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferenze());
        prefSCF = utils.loadDataSCF(this, new PreferencesSCF());
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
        setContentView(R.layout.preferenze_dado_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.pref_dice, new SettingsFragment())
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

    public static class SettingsFragment extends PreferenceFragmentCompat {
        SwitchPreferenceCompat vibration_button_scf;
        SwitchPreferenceCompat shake_scf;
        SwitchPreferenceCompat vibration_shake_scf;

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_scf, rootKey);
            vibration_button_scf = findPreference("vibration_button_scf");
            shake_scf = findPreference("shake_scf");
            vibration_shake_scf = findPreference("vibration_shake_scf");
            vibration_button_scf.setChecked(prefSCF.getVibrationButtonSCFBool());
            shake_scf.setChecked(prefSCF.getShakeSCFBool());
            vibration_shake_scf.setChecked(prefSCF.getVibrationShakeSCFBool());
            if (!shake_scf.isChecked())
                vibration_shake_scf.setChecked(false);
            Objects.requireNonNull(vibration_button_scf).setOnPreferenceChangeListener((preference, newValue) -> {
                utils.saveDataSCF(requireActivity(), prefSCF, (Boolean) newValue, shake_scf.isChecked(), vibration_shake_scf.isChecked());
                return true;
            });
            Objects.requireNonNull(shake_scf).setOnPreferenceChangeListener((preference, newValue) -> {
                if (!(Boolean) newValue)
                    vibration_shake_scf.setChecked(false);
                utils.saveDataSCF(requireActivity(), prefSCF, vibration_button_scf.isChecked(), (Boolean) newValue, vibration_shake_scf.isChecked());
                return true;
            });
            Objects.requireNonNull(vibration_shake_scf).setOnPreferenceChangeListener((preference, newValue) -> {
                utils.saveDataSCF(requireActivity(), prefSCF, vibration_button_scf.isChecked(), shake_scf.isChecked(), (Boolean) newValue);
                return true;
            });
        }
    }
}