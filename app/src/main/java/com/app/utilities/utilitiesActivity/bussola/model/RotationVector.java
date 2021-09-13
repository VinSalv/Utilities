package com.app.utilities.utilitiesActivity.bussola.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RotationVector {
    private final float x;
    private final float y;
    private final float z;

    public RotationVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @NotNull
    public final float[] toArray() {
        return new float[]{this.x, this.y, this.z};
    }

    @NotNull
    public String toString() {
        return "RotationVector(x=" + this.x + ", y=" + this.y + ", z=" + this.z + ")";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int hashCode() {
        return (Float.hashCode(this.x) * 31 + Float.hashCode(this.y)) * 31 + Float.hashCode(this.z);
    }

    public boolean equals(@Nullable Object var1) {
        if (this != var1) {
            if (var1 instanceof RotationVector) {
                RotationVector var2 = (RotationVector) var1;
                return Float.compare(this.x, var2.x) == 0 && Float.compare(this.y, var2.y) == 0 && Float.compare(this.z, var2.z) == 0;
            }
            return false;
        } else {
            return true;
        }
    }
}
