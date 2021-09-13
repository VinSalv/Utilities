package com.app.utilities.utilitiesActivity.bussola.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public final class Azimuth {
    private final float degrees;
    @NotNull
    private final CardinalDirection cardinalDirection;

    public Azimuth(float _degrees) {
        this.degrees = this.normalizeAngle(_degrees);
        float var2 = this.degrees;
        this.cardinalDirection = access$contains(access$until(22.5F, 67.5F), var2) ? CardinalDirection.NORTHEAST : (access$contains(access$until(67.5F, 112.5F), var2) ? CardinalDirection.EAST : (access$contains(access$until(112.5F, 157.5F), var2) ? CardinalDirection.SOUTHEAST : (access$contains(access$until(157.5F, 202.5F), var2) ? CardinalDirection.SOUTH : (access$contains(access$until(202.5F, 247.5F), var2) ? CardinalDirection.SOUTHWEST : (access$contains(access$until(247.5F, 292.5F), var2) ? CardinalDirection.WEST : (access$contains(access$until(292.5F, 337.5F), var2) ? CardinalDirection.NORTHWEST : CardinalDirection.NORTH))))));
    }

    @NotNull
    public final Azimuth plus(float degrees) {
        return new Azimuth(this.degrees + degrees);
    }

    public final float getDegrees() {
        return this.degrees;
    }

    @NotNull
    public final CardinalDirection getCardinalDirection() {
        return this.cardinalDirection;
    }

    private float normalizeAngle(float angleInDegrees) {
        return (angleInDegrees + 360.0F) % 360.0F;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!Intrinsics.areEqual(this.getClass(), other != null ? other.getClass() : null)) {
            return false;
        } else if (other == null) {
            throw new NullPointerException("null cannot be cast to non-null type com.bobek.compass.model.Azimuth");
        } else {
            return this.degrees == ((Azimuth) other).degrees;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int hashCode() {
        return Float.hashCode(this.degrees);
    }

    @NotNull
    public String toString() {
        return "Azimuth(degrees=" + this.degrees + ')';
    }

    private boolean contains(SemiClosedFloatRange $this$contains, float value) {
        return $this$contains.getFromInclusive() <= value && value < $this$contains.getToExclusive();
    }

    private SemiClosedFloatRange until(float $this$until, float to) {
        return new SemiClosedFloatRange($this$until, to);
    }

    public final boolean access$contains(SemiClosedFloatRange $this$access_u24contains, float value) {
        return contains($this$access_u24contains, value);
    }

    public final SemiClosedFloatRange access$until(float $this$access_u24until, float to) {
        return until($this$access_u24until, to);
    }

    static final class SemiClosedFloatRange {
        private final float fromInclusive;
        private final float toExclusive;

        public SemiClosedFloatRange(float fromInclusive, float toExclusive) {
            this.fromInclusive = fromInclusive;
            this.toExclusive = toExclusive;
        }

        public final float getFromInclusive() {
            return this.fromInclusive;
        }

        public final float getToExclusive() {
            return this.toExclusive;
        }

        @NotNull
        public String toString() {
            return "SemiClosedFloatRange(fromInclusive=" + this.fromInclusive + ", toExclusive=" + this.toExclusive + ")";
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public int hashCode() {
            return Float.hashCode(this.fromInclusive) * 31 + Float.hashCode(this.toExclusive);
        }

        public boolean equals(@Nullable Object var1) {
            if (this != var1) {
                if (var1 instanceof SemiClosedFloatRange) {
                    SemiClosedFloatRange var2 = (SemiClosedFloatRange) var1;
                    return Float.compare(this.fromInclusive, var2.fromInclusive) == 0 && Float.compare(this.toExclusive, var2.toExclusive) == 0;
                }
                return false;
            } else {
                return true;
            }
        }
    }
}
