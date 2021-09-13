package com.app.utilities.settings;

public class Preferenze {
    final String sharedPrefs = "sharedPrefs";
    final String pred = "pred";
    final String theme = "theme";
    Boolean predBool;
    String themeText;

    public Preferenze() {
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
