package com.example.utilities.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.utilities.R;
import com.example.utilities.Utility.Utils;

public class Fragment_Sensori extends Fragment implements SensorEventListener {
    TextView xValue, yValue, zValue, xGyroValue, yGyroValue, zGyroValue, xMagnoValue, yMagnoValue, zMagnoValue, light, pressure, temperature, humidity;
    Utils util = new Utils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xValue.setText(Html.fromHtml("<b>X: </b>" + util.roundAvoid(event.values[0], 2)));
            yValue.setText(Html.fromHtml("<b>Y: </b>" + util.roundAvoid(event.values[1], 2)));
            zValue.setText(Html.fromHtml("<b>Z: </b>" + util.roundAvoid(event.values[2], 2)));
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            xGyroValue.setText(Html.fromHtml("<b>X: </b>" + util.roundAvoid(event.values[0], 2)));
            yGyroValue.setText(Html.fromHtml("<b>Y: </b>" + util.roundAvoid(event.values[1], 2)));
            zGyroValue.setText(Html.fromHtml("<b>Z: </b>" + util.roundAvoid(event.values[2], 2)));
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            xMagnoValue.setText(Html.fromHtml("<b>X: </b>" + util.roundAvoid(event.values[0], 2)));
            yMagnoValue.setText(Html.fromHtml("<b>Y: </b>" + util.roundAvoid(event.values[1], 2)));
            zMagnoValue.setText(Html.fromHtml("<b>Z: </b>" + util.roundAvoid(event.values[2], 2)));
        } else if (sensor.getType() == Sensor.TYPE_LIGHT) {
            light.setText(Html.fromHtml("<b>Luminosità: </b>" + util.roundAvoid(event.values[0], 2) + " [lx]"));
        } else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            pressure.setText(Html.fromHtml("<b>Pressione: </b>" + util.roundAvoid(event.values[0], 2) + " [hPa oppure mbar]"));
        } else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            temperature.setText(Html.fromHtml("<b>Temperatura: </b>" + util.roundAvoid(event.values[0], 2) + " [°C]"));
        } else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            humidity.setText(Html.fromHtml("<b>Umidità: </b>" + util.roundAvoid(event.values[0], 2) + " [%]"));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__sensori, container, false);

        xValue = (TextView) view.findViewById(R.id.xValue);
        yValue = (TextView) view.findViewById(R.id.yValue);
        zValue = (TextView) view.findViewById(R.id.zValue);

        xGyroValue = (TextView) view.findViewById(R.id.xGyroValue);
        yGyroValue = (TextView) view.findViewById(R.id.yGyroValue);
        zGyroValue = (TextView) view.findViewById(R.id.zGyroValue);

        xMagnoValue = (TextView) view.findViewById(R.id.xMagnoValue);
        yMagnoValue = (TextView) view.findViewById(R.id.yMagnoValue);
        zMagnoValue = (TextView) view.findViewById(R.id.zMagnoValue);

        light = (TextView) view.findViewById(R.id.light);
        pressure = (TextView) view.findViewById(R.id.pressure);
        temperature = (TextView) view.findViewById(R.id.temperature);
        humidity = (TextView) view.findViewById(R.id.humidity);

        SensorManager sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(Fragment_Sensori.this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            xValue.setText(R.string.AccelerometerNotSupported);
        }

        Sensor mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyro != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mGyro, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            xGyroValue.setText(R.string.GyroscopeNotSupported);
        }

        Sensor mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagno != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mMagno, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            xMagnoValue.setText(R.string.MagnetometerNotSupported);
        }

        Sensor mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            light.setText(R.string.BrightnessNotSupported);
        }

        Sensor mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mPressure != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mPressure, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            pressure.setText(R.string.PressureNotSupported);
        }

        Sensor mTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (mTemperature != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mTemperature, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            temperature.setText(R.string.TemperatureNotSupported);
        }

        Sensor mHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (mHumidity != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mHumidity, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            humidity.setText(R.string.HumidityNotSupported);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}