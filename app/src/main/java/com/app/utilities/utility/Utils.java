package com.app.utilities.utility;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.app.utilities.MainActivity;
import com.app.utilities.R;
import com.app.utilities.settings.PrefderenzeDado;
import com.app.utilities.settings.PrefderenzeMoneta;
import com.app.utilities.settings.PrefderenzeSCF;
import com.app.utilities.settings.SettingsActivity;
import com.app.utilities.utilitiesActivity.ARMeasureActivity;
import com.app.utilities.utilitiesActivity.AltriSensoriActivity;
import com.app.utilities.utilitiesActivity.BussolaActivity;
import com.app.utilities.utilitiesActivity.DadoActivity;
import com.app.utilities.utilitiesActivity.InfoActivity;
import com.app.utilities.utilitiesActivity.LivellaActivity;
import com.app.utilities.utilitiesActivity.MonetaActivity;
import com.app.utilities.utilitiesActivity.SCFActivity;

@SuppressWarnings("unused")
public class Utils {
    public void notifyUser(Context context, String message) {
        Toast.makeText(context,
                message,
                Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("unused")
    public void notifyUserShortWay(Context context, String message) {
        Toast.makeText(context,
                message,
                Toast.LENGTH_SHORT).show();
    }

    public void goToMainActivity(Context context) {
        ((Activity) context).finish();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void goToInfoActivity(Context context) {
        Intent intent = new Intent(context, InfoActivity.class);
        context.startActivity(intent);
    }

    public void goToSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public void goToAltriSensoriActivity(Context context) {
        Intent intent = new Intent(context, AltriSensoriActivity.class);
        context.startActivity(intent);
    }

    public void goToARMeasureActivity(Context context) {
        Intent intent = new Intent(context, ARMeasureActivity.class);
        context.startActivity(intent);
    }

    public void goToLivellaActivity(Context context) {
        Intent intent = new Intent(context, LivellaActivity.class);
        context.startActivity(intent);
    }

    public void goToBussolaActivity(Context context) {
        Intent intent = new Intent(context, BussolaActivity.class);
        context.startActivity(intent);
    }

    public void goToDadoActivity(Context context) {
        Intent intent = new Intent(context, DadoActivity.class);
        context.startActivity(intent);
    }

    public void goToPrefderenzeDado(Context context) {
        Intent intent = new Intent(context, PrefderenzeDado.class);
        context.startActivity(intent);
    }

    public void goToSCFActivity(Context context) {
        Intent intent = new Intent(context, SCFActivity.class);
        context.startActivity(intent);
    }

    public void goToPrefderenzeSCF(Context context) {
        Intent intent = new Intent(context, PrefderenzeSCF.class);
        context.startActivity(intent);
    }

    public void goToMonetaActivity(Context context) {
        Intent intent = new Intent(context, MonetaActivity.class);
        context.startActivity(intent);
    }

    public void goToPrefderenzeMoneta(Context context) {
        Intent intent = new Intent(context, PrefderenzeMoneta.class);
        context.startActivity(intent);
    }

    public void refreshActivity(Context context) {
        ((Activity) context).finish();
        context.startActivity(((Activity) context).getIntent());
    }

    public double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public void saveData(Context context, Preferences pref, Boolean pred, String theme) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(pref.getSharedPrefs(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(pref.getPred(), pred);
        editor.putString(pref.getTheme(), theme);
        editor.apply();
    }

    public void saveDataDado(Context context, PreferencesDado prefDado, Boolean vibrationButtonsDiceBool, Boolean vibrationButtonDiceBool, Boolean shakeDiceBool, Boolean vibrationShakeDiceBool) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefDado.getSharedPrefs(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(prefDado.getVibrationButtonsDice(), vibrationButtonsDiceBool);
        editor.putBoolean(prefDado.getVibrationButtonDice(), vibrationButtonDiceBool);
        editor.putBoolean(prefDado.getShakeDice(), shakeDiceBool);
        editor.putBoolean(prefDado.getVibrationShakeDice(), vibrationShakeDiceBool);
        editor.apply();
    }

    public void saveDataSCF(Context context, PreferencesSCF prefSCF, Boolean vibrationButtonSCFBool, Boolean shakeSCFBool, Boolean vibrationShakeSCFBool) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefSCF.getSharedPrefs(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(prefSCF.getVibrationButtonSCF(), vibrationButtonSCFBool);
        editor.putBoolean(prefSCF.getShakeSCF(), shakeSCFBool);
        editor.putBoolean(prefSCF.getVibrationShakeSCF(), vibrationShakeSCFBool);
        editor.apply();
    }

    public void saveDataMoneta(Context context, PreferencesMoneta prefMoneta, Boolean vibrationButtonMonetaBool, Boolean shakeMonetaBool, Boolean vibrationShakeMonetaBool) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefMoneta.getSharedPrefs(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(prefMoneta.getVibrationButtonMoneta(), vibrationButtonMonetaBool);
        editor.putBoolean(prefMoneta.getShakeMoneta(), shakeMonetaBool);
        editor.putBoolean(prefMoneta.getVibrationShakeMoneta(), vibrationShakeMonetaBool);
        editor.apply();
    }

    public Preferences loadData(Context context, Preferences pref) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(pref.getSharedPrefs(), Context.MODE_PRIVATE);
        pref.setPredBool(sharedPreferences.getBoolean(pref.getPred(), true));
        pref.setThemeText(sharedPreferences.getString(pref.getTheme(), "light_theme"));
        return pref;
    }

    public PreferencesDado loadDataDado(Context context, PreferencesDado prefDado) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefDado.getSharedPrefs(), Context.MODE_PRIVATE);
        prefDado.setVibrationButtonsDiceBool(sharedPreferences.getBoolean(prefDado.getVibrationButtonsDice(), true));
        prefDado.setVibrationButtonDiceBool(sharedPreferences.getBoolean(prefDado.getVibrationButtonDice(), true));
        prefDado.setShakeDiceBool(sharedPreferences.getBoolean(prefDado.getShakeDice(), true));
        prefDado.setVibrationShakeDiceBool(sharedPreferences.getBoolean(prefDado.getVibrationShakeDice(), true));
        return prefDado;
    }

    public PreferencesSCF loadDataSCF(Context context, PreferencesSCF prefSCF) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefSCF.getSharedPrefs(), Context.MODE_PRIVATE);
        prefSCF.setVibrationButtonSCFBool(sharedPreferences.getBoolean(prefSCF.getVibrationButtonSCF(), true));
        prefSCF.setShakeSCFBool(sharedPreferences.getBoolean(prefSCF.getShakeSCF(), true));
        prefSCF.setVibrationShakeSCFBool(sharedPreferences.getBoolean(prefSCF.getVibrationShakeSCF(), true));
        return prefSCF;
    }

    public PreferencesMoneta loadDataMoneta(Context context, PreferencesMoneta prefMoneta) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefMoneta.getSharedPrefs(), Context.MODE_PRIVATE);
        prefMoneta.setVibrationButtonMonetaBool(sharedPreferences.getBoolean(prefMoneta.getVibrationButtonMoneta(), true));
        prefMoneta.setShakeMonetaBool(sharedPreferences.getBoolean(prefMoneta.getShakeMoneta(), true));
        prefMoneta.setVibrationShakeMonetaBool(sharedPreferences.getBoolean(prefMoneta.getVibrationShakeMoneta(), true));
        return prefMoneta;
    }

    public void changeTheme(Context context, int i) {
        switch (i) {
            case 0:
                context.setTheme(R.style.LightTheme);
                break;
            case 1:
                context.setTheme(R.style.DarkTheme);
                break;
        }
    }

    public void changeThemeSelected(Context context, int i) {
        switch (i) {
            case 0:
                context.setTheme(R.style.LightThemeSelected);
                break;
            case 1:
                context.setTheme(R.style.DarkThemeSelected);
                break;
        }
    }

    public void openFolderDownload(Context context) {
        context.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
    }

}
