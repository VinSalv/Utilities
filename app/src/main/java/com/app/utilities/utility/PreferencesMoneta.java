package com.app.utilities.utility;

public class PreferencesMoneta {
    final String sharedPrefs = "preferenceMoneta";
    final String vibrationButtonMoneta = "vibration_button_moneta";
    final String shakeMoneta = "shake_moneta";
    final String vibrationShakeMoneta = "vibration_shake_moneta";
    Boolean vibrationButtonMonetaBool;
    Boolean shakeMonetaBool;
    Boolean vibrationShakeMonetaBool;

    public PreferencesMoneta() {
        this.vibrationButtonMonetaBool = null;
        this.shakeMonetaBool = null;
        this.vibrationShakeMonetaBool = null;
    }

    public String getSharedPrefs() {
        return sharedPrefs;
    }

    public String getVibrationButtonMoneta() {
        return vibrationButtonMoneta;
    }

    public String getShakeMoneta() {
        return shakeMoneta;
    }

    public String getVibrationShakeMoneta() {
        return vibrationShakeMoneta;
    }

    public Boolean getVibrationButtonMonetaBool() {
        return vibrationButtonMonetaBool;
    }

    public void setVibrationButtonMonetaBool(Boolean vibrationButtonMonetaBool) {
        this.vibrationButtonMonetaBool = vibrationButtonMonetaBool;
    }

    public Boolean getShakeMonetaBool() {
        return shakeMonetaBool;
    }

    public void setShakeMonetaBool(Boolean shakeMonetaBool) {
        this.shakeMonetaBool = shakeMonetaBool;
    }

    public Boolean getVibrationShakeMonetaBool() {
        return vibrationShakeMonetaBool;
    }

    public void setVibrationShakeMonetaBool(Boolean vibrationShakeMonetaBool) {
        this.vibrationShakeMonetaBool = vibrationShakeMonetaBool;
    }
}
