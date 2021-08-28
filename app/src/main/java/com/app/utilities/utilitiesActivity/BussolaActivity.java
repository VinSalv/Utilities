package com.app.utilities.utilitiesActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.utilities.R;
import com.app.utilities.bussola.Compass;
import com.app.utilities.bussola.SOTWFormatter;
import com.app.utilities.utility.Preferences;
import com.app.utilities.utility.Utils;

public class BussolaActivity extends AppCompatActivity {
    private final Utils utils = new Utils();
    @SuppressWarnings("unused")
    protected Configuration mPrevConfig;
    @SuppressWarnings("unused")
    Preferences pref;
    private Compass compass;
    private ImageView arrowView;
    private TextView sotwLabel;
    private float currentAzimuth;
    private SOTWFormatter sotwFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferences());
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
        setContentView(R.layout.activity_bussola);
        pref = utils.loadData(this, new Preferences());
        mPrevConfig = new Configuration(getResources().getConfiguration());
        sotwFormatter = new SOTWFormatter(this);
        arrowView = findViewById(R.id.main_image_hands);
        sotwLabel = findViewById(R.id.sotw_label);
        setupCompass();
    }

    private void setupCompass() {
        compass = new Compass(this);
        Compass.CompassListener cl = getCompassListener();
        compass.setListener(cl);
    }

    private void adjustArrow(float azimuth) {
        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = azimuth;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        arrowView.startAnimation(an);
    }

    private void adjustSotwLabel(float azimuth) {
        sotwLabel.setText(sotwFormatter.format(azimuth));
    }

    private Compass.CompassListener getCompassListener() {
        return azimuth -> this.runOnUiThread(() -> {
            adjustArrow(azimuth);
            adjustSotwLabel(azimuth);

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        compass.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        compass.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        compass.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        compass.stop();
    }


}