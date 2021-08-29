package com.app.utilities.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.app.utilities.InfoActivity;
import com.app.utilities.MainActivity;
import com.app.utilities.R;
import com.app.utilities.SettingsActivity;
import com.app.utilities.utilitiesActivity.BussolaActivity;
import com.app.utilities.utilitiesActivity.DadoActivity;
import com.app.utilities.utilitiesActivity.LivellaActivity;
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

    public void goToSCFActivity(Context context) {
        Intent intent = new Intent(context, SCFActivity.class);
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

    public Preferences loadData(Context context, Preferences pref) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(pref.getSharedPrefs(), Context.MODE_PRIVATE);
        pref.setPredBool(sharedPreferences.getBoolean(pref.getPred(), true));
        pref.setThemeText(sharedPreferences.getString(pref.getTheme(), "light_theme"));
        return pref;
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

}
