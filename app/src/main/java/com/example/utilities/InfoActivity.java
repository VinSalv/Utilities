package com.example.utilities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utilities.Utility.Preferences;
import com.example.utilities.Utility.Utils;

public class InfoActivity extends AppCompatActivity {
    private final Utils utils = new Utils();
    Preferences pref = new Preferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, pref);
        if (pref.getPredBool())
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_UNDEFINED:

                case Configuration.UI_MODE_NIGHT_NO:
                    utils.changeTheme(this, 0);
                    break;

                case Configuration.UI_MODE_NIGHT_YES:
                    utils.changeTheme(this, 1);
                    break;
            }
        else if (pref.getThemeText().equals("light_theme")) utils.changeTheme(this, 0);
        else utils.changeTheme(this, 1);
        setContentView(R.layout.activity_info);
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> utils.goToMainActivity(InfoActivity.this));
    }

    @Override
    public void onStart() {
        super.onStart();
        pref = utils.loadData(this, pref);
    }

    @Override
    public void onPause() {
        super.onPause();
        pref = utils.loadData(this, pref);
    }

    @Override
    public void onResume() {
        super.onResume();
        pref = utils.loadData(this, pref);
    }

    @Override
    public void onStop() {
        super.onStop();
        pref = utils.loadData(this, pref);
    }
}