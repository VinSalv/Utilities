package com.example.utilities.bussola;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.utilities.utility.Utils;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class Compass implements SensorEventListener {

    final Utils utils = new Utils();
    private final SensorManager sensorManager;
    private final Sensor gsensor;
    private final Sensor msensor;
    private final float[] mGravity = new float[3];
    private final float[] mGeomagnetic = new float[3];
    private final float[] R = new float[9];
    private final float[] I = new float[9];
    private CompassListener listener;
    private float azimuthFix;

    public Compass(Context context) {
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (gsensor == null) {
            TabLayout tabs = ((Activity) context).findViewById(com.example.utilities.R.id.tabs);
            Objects.requireNonNull(tabs.getTabAt(2)).select();
            utils.notifyUser(context, "Il tuo dispositivo non dispone di un accellerometro");
        }
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (msensor == null) {
            TabLayout tabs = ((Activity) context).findViewById(com.example.utilities.R.id.tabs);
            Objects.requireNonNull(tabs.getTabAt(2)).select();
            utils.notifyUser(context, "Il tuo dispositivo non dispone di un accellerometro");
        }
    }

    public void start() {
        if (gsensor != null)
            sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_GAME);
        if (msensor != null)
            sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void setAzimuthFix(float fix) {
        azimuthFix = fix;
    }

    public void resetAzimuthFix() {
        setAzimuthFix(0);
    }

    public void setListener(CompassListener l) {
        listener = l;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * event.values[2];
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * event.values[2];
            }
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimuth = (float) Math.toDegrees(orientation[0]);
                azimuth = (azimuth + azimuthFix + 360) % 360;
                if (listener != null) {
                    listener.onNewAzimuth(azimuth);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public interface CompassListener {
        void onNewAzimuth(float azimuth);
    }
}






