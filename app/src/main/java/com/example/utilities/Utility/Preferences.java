package com.example.utilities.Utility;

public class Preferences {
    String sharedPrefs = "sharedPrefs";
    String pred = "pred";
    String theme = "theme";
    Boolean predBool;
    String themeText;

    public Preferences() {
        this.predBool = null;
        this.themeText = null;
    }

    public String getSharedPrefs() {
        return sharedPrefs;
    }


    public String getPred() {
        return pred;
    }


    public String getTheme() {
        return theme;
    }


    public Boolean getPredBool() {
        return predBool;
    }

    public void setPredBool(Boolean predBool) {
        this.predBool = predBool;
    }

    public String getThemeText() {
        return themeText;
    }

    public void setThemeText(String themeText) {
        this.themeText = themeText;
    }

}
