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
import com.app.utilities.utilitiesActivity.dado.PreferencesDado;
import com.app.utilities.utility.Utils;

import java.util.Objects;

public class PreferenzeDado extends AppCompatActivity {
    static final Utils utils = new Utils();
    static Preferenze pref;
    static PreferencesDado prefDado;
    protected Configuration mPrevConfig;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferenze());
        prefDado = utils.loadDataDado(this, new PreferencesDado());
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
        SwitchPreferenceCompat vibration_buttons_dice;
        SwitchPreferenceCompat vibration_button_dice;
        SwitchPreferenceCompat shake_dice;
        SwitchPreferenceCompat vibration_shake_dice;

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_dado, rootKey);
            vibration_buttons_dice = findPreference("vibration_buttons_dice");
            vibration_button_dice = findPreference("vibration_button_dice");
            shake_dice = findPreference("shake_dice");
            vibration_shake_dice = findPreference("vibration_shake_dice");
            vibration_buttons_dice.setChecked(prefDado.getVibrationButtonsDiceBool());
            vibration_button_dice.setChecked(prefDado.getVibrationButtonDiceBool());
            shake_dice.setChecked(prefDado.getShakeDiceBool());
            vibration_shake_dice.setChecked(prefDado.getVibrationShakeDiceBool());
            if (!shake_dice.isChecked())
                vibration_shake_dice.setChecked(false);
            Objects.requireNonNull(vibration_buttons_dice).setOnPreferenceChangeListener((preference, newValue) -> {
                utils.saveDataDado(requireActivity(), prefDado, (Boolean) newValue, vibration_button_dice.isChecked(), shake_dice.isChecked(), vibration_shake_dice.isChecked());
                return true;
            });
            Objects.requireNonNull(vibration_button_dice).setOnPreferenceChangeListener((preference, newValue) -> {
                utils.saveDataDado(requireActivity(), prefDado, vibration_buttons_dice.isChecked(), (Boolean) newValue, shake_dice.isChecked(), vibration_shake_dice.isChecked());
                return true;
            });
            Objects.requireNonNull(shake_dice).setOnPreferenceChangeListener((preference, newValue) -> {
                if ((Boolean) newValue)
                    vibration_shake_dice.setChecked(false);
                utils.saveDataDado(requireActivity(), prefDado, vibration_buttons_dice.isChecked(), vibration_button_dice.isChecked(), (Boolean) newValue, vibration_shake_dice.isChecked());
                return true;
            });
            Objects.requireNonNull(vibration_shake_dice).setOnPreferenceChangeListener((preference, newValue) -> {
                utils.saveDataDado(requireActivity(), prefDado, vibration_buttons_dice.isChecked(), vibration_button_dice.isChecked(), shake_dice.isChecked(), (Boolean) newValue);
                return true;
            });
        }
    }
}