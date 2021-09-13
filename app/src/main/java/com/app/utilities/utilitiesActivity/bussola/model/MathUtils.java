package com.app.utilities.utilitiesActivity.bussola.model;

import android.hardware.SensorManager;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.JvmStatic;

public final class MathUtils {
    @JvmStatic
    @NotNull
    public static Azimuth calculateAzimuth(@NotNull RotationVector rotationVector) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector.toArray());
        float[] orientationAnglesInRadians = SensorManager.getOrientation(rotationMatrix, new float[3]);
        float radians = orientationAnglesInRadians[0];
        float degrees = (float) Math.toDegrees(radians);
        return new Azimuth(degrees);
    }
}
