package com.app.utilities.utilitiesActivity.bussola;

import static android.hardware.SensorManager.SENSOR_DELAY_FASTEST;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.app.utilities.R;
import com.app.utilities.settings.Preferenze;
import com.app.utilities.utilitiesActivity.bussola.model.Azimuth;
import com.app.utilities.utilitiesActivity.bussola.model.MathUtils;
import com.app.utilities.utilitiesActivity.bussola.model.RotationVector;
import com.app.utilities.utilitiesActivity.bussola.model.SensorAccuracy;
import com.app.utilities.utilitiesActivity.bussola.view.CompassView;
import com.app.utilities.utility.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

@SuppressWarnings("ALL")
public final class BussolaActivity extends AppCompatActivity implements SensorEventListener {
    private final Utils utils = new Utils();
    protected Configuration mPrevConfig;
    Preferenze pref;
    private CompassView compassView;
    private SensorManager sensorManager;
    private Menu optionsMenu;
    private SensorAccuracy sensorAccuracy;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferenze());
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
        this.setContentView(R.layout.activity_bussola);
        this.compassView = findViewById(R.id.compass);
        @SuppressLint("WrongConstant") Object var2 = this.getSystemService("sensor");
        if (var2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type android.hardware.SensorManager");
        } else {
            this.sensorManager = (SensorManager) var2;
        }
        ImageButton accuracy = findViewById(R.id.accuracy);
        accuracy.setOnClickListener(view -> this.showSensorStatusPopup());
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());
        mPrevConfig = new Configuration(getResources().getConfiguration());
    }

    protected void onResume() {
        super.onResume();
        SensorManager var10000 = this.sensorManager;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sensorManager");
        }
        Sensor rotationVectorSensor = var10000.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (rotationVectorSensor == null) {
            this.showSensorErrorDialog();
        } else {
            var10000 = this.sensorManager;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sensorManager");
            }
            var10000.registerListener(this, rotationVectorSensor, SENSOR_DELAY_FASTEST);
        }
    }

    private void showSensorErrorDialog() {
        (new MaterialAlertDialogBuilder(this)).setMessage(R.string.sensor_error_message).setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false).show();
    }

    protected void onPause() {
        super.onPause();
        SensorManager var10000 = this.sensorManager;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sensorManager");
        }
        var10000.unregisterListener(this);
    }

    private void showSensorStatusPopup() {
        View sensorAccuracyView = this.getLayoutInflater().inflate(R.layout.sensor_alert_dialog_view, null);
        ((AppCompatImageView) sensorAccuracyView.findViewById(R.id.sensor_accuracy_image)).setImageResource(this.sensorAccuracy.getIconResourceId());
        ((AppCompatTextView) sensorAccuracyView.findViewById(R.id.sensor_accuracy_text)).setText(this.sensorAccuracy.getTextResourceId());
        (new MaterialAlertDialogBuilder(this)).
                setTitle(R.string.sensor_status).
                setView(sensorAccuracyView).
                setNeutralButton(R.string.ok, (dialog, i) -> dialog.dismiss()).show();
    }

    public void onAccuracyChanged(@NotNull Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            this.updateSensorAccuracy(accuracy);
        }
    }

    private void updateSensorAccuracy(int accuracy) {
        this.sensorAccuracy = this.adaptAccuracy(accuracy);
    }

    private SensorAccuracy adaptAccuracy(int accuracy) {
        SensorAccuracy var10000;
        switch (accuracy) {
            case 0:
                var10000 = SensorAccuracy.UNRELIABLE;
                break;
            case 1:
                var10000 = SensorAccuracy.LOW;
                break;
            case 2:
                var10000 = SensorAccuracy.MEDIUM;
                break;
            case 3:
                var10000 = SensorAccuracy.HIGH;
                break;
            default:
                var10000 = SensorAccuracy.NO_CONTACT;
        }
        return var10000;
    }

    public void onSensorChanged(@NotNull SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            this.updateCompass(event);
        }
    }

    private void updateCompass(SensorEvent event) {
        RotationVector rotationVector = new RotationVector(event.values[0], event.values[1], event.values[2]);
        Azimuth azimuth = MathUtils.calculateAzimuth(rotationVector);
        this.compassView.setAzimuth(azimuth);
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
}