package com.app.utilities.utility;

public class PreferencesSCF {
    final String sharedPrefs = "preferenceDado";
    final String vibrationButtonSCF = "vibration_button_scf";
    final String shakeSCF = "shake_scf";
    final String vibrationShakeSCF = "vibration_shake_scf";
    Boolean vibrationButtonSCFBool;
    Boolean shakeSCFBool;
    Boolean vibrationShakeSCFBool;

    public PreferencesSCF() {
        this.vibrationButtonSCFBool = null;
        this.shakeSCFBool = null;
        this.vibrationShakeSCFBool = null;
    }

    public String getSharedPrefs() {
        return sharedPrefs;
    }

    public String getVibrationButtonSCF() {
        return vibrationButtonSCF;
    }

    public String getShakeSCF() {
        return shakeSCF;
    }

    public String getVibrationShakeSCF() {
        return vibrationShakeSCF;
    }

    public Boolean getVibrationButtonSCFBool() {
        return vibrationButtonSCFBool;
    }

    public void setVibrationButtonSCFBool(Boolean vibrationButtonSCFBool) {
        this.vibrationButtonSCFBool = vibrationButtonSCFBool;
    }

    public Boolean getShakeSCFBool() {
        return shakeSCFBool;
    }

    public void setShakeSCFBool(Boolean shakeSCFBool) {
        this.shakeSCFBool = shakeSCFBool;
    }

    public Boolean getVibrationShakeSCFBool() {
        return vibrationShakeSCFBool;
    }

    public void setVibrationShakeSCFBool(Boolean vibrationShakeSCFBool) {
        this.vibrationShakeSCFBool = vibrationShakeSCFBool;
    }
}
