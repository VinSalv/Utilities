package com.app.utilities.utility;

public class PreferencesDado {
    final String sharedPrefs = "preferenceDado";
    final String vibrationButtonsDice = "vibration_buttons_dice";
    final String vibrationButtonDice = "vibration_button_dice";
    final String shakeDice = "shake_dice";
    final String vibrationShakeDice = "vibration_shake_dice";
    Boolean vibrationButtonsDiceBool;
    Boolean vibrationButtonDiceBool;
    Boolean shakeDiceBool;
    Boolean vibrationShakeDiceBool;

    public PreferencesDado() {
        this.vibrationButtonsDiceBool = null;
        this.vibrationButtonDiceBool = null;
        this.shakeDiceBool = null;
        this.vibrationShakeDiceBool = null;
    }

    public String getVibrationButtonsDice() {
        return vibrationButtonsDice;
    }

    public Boolean getVibrationButtonsDiceBool() {
        return vibrationButtonsDiceBool;
    }

    public void setVibrationButtonsDiceBool(Boolean vibrationButtonsDiceBool) {
        this.vibrationButtonsDiceBool = vibrationButtonsDiceBool;
    }

    public String getVibrationButtonDice() {
        return vibrationButtonDice;
    }

    public String getShakeDice() {
        return shakeDice;
    }

    public String getVibrationShakeDice() {
        return vibrationShakeDice;
    }

    public String getSharedPrefs() {
        return sharedPrefs;
    }

    public Boolean getVibrationButtonDiceBool() {
        return vibrationButtonDiceBool;
    }

    public void setVibrationButtonDiceBool(Boolean vibrationButtonDiceBool) {
        this.vibrationButtonDiceBool = vibrationButtonDiceBool;
    }

    public Boolean getShakeDiceBool() {
        return shakeDiceBool;
    }

    public void setShakeDiceBool(Boolean shakeDiceBool) {
        this.shakeDiceBool = shakeDiceBool;
    }

    public Boolean getVibrationShakeDiceBool() {
        return vibrationShakeDiceBool;
    }

    public void setVibrationShakeDiceBool(Boolean vibrationShakeDiceBool) {
        this.vibrationShakeDiceBool = vibrationShakeDiceBool;
    }
}
