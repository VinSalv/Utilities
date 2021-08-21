package com.example.utilities.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.utilities.R;
import com.example.utilities.utility.Preferences;
import com.example.utilities.utility.Utils;

public class Fragment_Sensori extends Fragment implements SensorEventListener {
    final TypedValue typedValue = new TypedValue();
    private final Utils utils = new Utils();
    protected Configuration mPrevConfig;
    TextView xValue, yValue, zValue, xGyroValue, yGyroValue, zGyroValue, xMagnoValue, yMagnoValue, zMagnoValue, light, pressure, temperature, humidity;
    Preferences pref;
    int color;
    int colorSecondaryVariant;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(requireActivity(), new Preferences());
        mPrevConfig = new Configuration(getResources().getConfiguration());

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xValue.setText(Html.fromHtml("<b>X: </b>" + utils.roundAvoid(event.values[0], 2)));
            yValue.setText(Html.fromHtml("<b>Y: </b>" + utils.roundAvoid(event.values[1], 2)));
            zValue.setText(Html.fromHtml("<b>Z: </b>" + utils.roundAvoid(event.values[2], 2)));
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            xGyroValue.setText(Html.fromHtml("<b>X: </b>" + utils.roundAvoid(event.values[0], 2)));
            yGyroValue.setText(Html.fromHtml("<b>Y: </b>" + utils.roundAvoid(event.values[1], 2)));
            zGyroValue.setText(Html.fromHtml("<b>Z: </b>" + utils.roundAvoid(event.values[2], 2)));
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            xMagnoValue.setText(Html.fromHtml("<b>X: </b>" + utils.roundAvoid(event.values[0], 2)));
            yMagnoValue.setText(Html.fromHtml("<b>Y: </b>" + utils.roundAvoid(event.values[1], 2)));
            zMagnoValue.setText(Html.fromHtml("<b>Z: </b>" + utils.roundAvoid(event.values[2], 2)));
        } else if (sensor.getType() == Sensor.TYPE_LIGHT) {
            light.setText(Html.fromHtml("<b>Luminosità: </b>" + utils.roundAvoid(event.values[0], 2) + " [lx]"));
        } else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            pressure.setText(Html.fromHtml("<b>Pressione: </b>" + utils.roundAvoid(event.values[0], 2) + " [hPa oppure mbar]"));
        } else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            temperature.setText(Html.fromHtml("<b>Temperatura: </b>" + utils.roundAvoid(event.values[0], 2) + " [°C]"));
        } else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            humidity.setText(Html.fromHtml("<b>Umidità: </b>" + utils.roundAvoid(event.values[0], 2) + " [%]"));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__sensori, container, false);
        xValue = view.findViewById(R.id.xValue);
        yValue = view.findViewById(R.id.yValue);
        zValue = view.findViewById(R.id.zValue);
        xGyroValue = view.findViewById(R.id.xGyroValue);
        yGyroValue = view.findViewById(R.id.yGyroValue);
        zGyroValue = view.findViewById(R.id.zGyroValue);
        xMagnoValue = view.findViewById(R.id.xMagnoValue);
        yMagnoValue = view.findViewById(R.id.yMagnoValue);
        zMagnoValue = view.findViewById(R.id.zMagnoValue);
        light = view.findViewById(R.id.light);
        pressure = view.findViewById(R.id.pressure);
        temperature = view.findViewById(R.id.temperature);
        humidity = view.findViewById(R.id.humidity);
        SensorManager sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(Fragment_Sensori.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            xValue.setText(R.string.AccelerometerNotSupported);
        }
        Sensor mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyro != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            xGyroValue.setText(R.string.GyroscopeNotSupported);
        }
        Sensor mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagno != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mMagno, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            xMagnoValue.setText(R.string.MagnetometerNotSupported);
        }
        Sensor mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mLight, SensorManager.SENSOR_DELAY_GAME);
        } else {
            light.setText(R.string.BrightnessNotSupported);
        }
        Sensor mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mPressure != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mPressure, SensorManager.SENSOR_DELAY_GAME);
        } else {
            pressure.setText(R.string.PressureNotSupported);
        }
        Sensor mTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (mTemperature != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mTemperature, SensorManager.SENSOR_DELAY_GAME);
        } else {
            temperature.setText(R.string.TemperatureNotSupported);
        }
        Sensor mHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (mHumidity != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mHumidity, SensorManager.SENSOR_DELAY_GAME);
        } else {
            humidity.setText(R.string.HumidityNotSupported);
        }
        pref = utils.loadData(requireActivity(), new Preferences());
        requireActivity().getTheme().resolveAttribute(R.attr.color, typedValue, true);
        color = typedValue.resourceId;
        requireActivity().getTheme().resolveAttribute(R.attr.colorSecondaryVariant, typedValue, true);
        colorSecondaryVariant = typedValue.resourceId;
        if (!pref.getPredBool()) {
            switch (pref.getThemeText()) {
                case "LightThemeSelected":
                case "LightTheme":
                    view.setBackgroundColor(requireActivity().getColor(colorSecondaryVariant));
                    break;
                case "DarkThemeSelected":
                case "DarkTheme":
                    view.setBackgroundColor(requireActivity().getColor(color));
                    break;
            }
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                case Configuration.UI_MODE_NIGHT_NO:
                    view.setBackgroundColor(requireActivity().getColor(colorSecondaryVariant));
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    view.setBackgroundColor(requireActivity().getColor(color));
                    break;
            }
        }
        return view;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configurationChanged(newConfig);
        mPrevConfig = new Configuration(newConfig);
    }

    protected void configurationChanged(Configuration newConfig) {
        if (isNightConfigChanged(newConfig) && pref.getPredBool()) {
            utils.goToMainActivity(requireActivity());
        }
    }

    protected boolean isNightConfigChanged(Configuration newConfig) {
        return (newConfig.diff(mPrevConfig) & ActivityInfo.CONFIG_UI_MODE) != 0 && isOnDarkMode(newConfig) != isOnDarkMode(mPrevConfig);
    }
}