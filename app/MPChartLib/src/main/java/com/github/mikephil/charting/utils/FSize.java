package com.github.mikephil.charting.utils;

import java.util.List;

public final class FSize extends ObjectPool.Poolable {
    private static ObjectPool<FSize> pool;

    static {
        pool = ObjectPool.create(256, new FSize(0, 0));
        pool.setReplenishPercentage(0.5f);
    }

    public float width;
    public float height;

    public FSize() {
    }

    public FSize(final float width, final float height) {
        this.width = width;
        this.height = height;
    }

    public static FSize getInstance(final float width, final float height) {
        FSize result = pool.get();
        result.width = width;
        result.height = height;
        return result;
    }

    public static void recycleInstance(FSize instance) {
        pool.recycle(instance);
    }

    public static void recycleInstances(List<FSize> instances) {
        pool.recycle(instances);
    }

    protected ObjectPool.Poolable instantiate() {
        return new FSize(0, 0);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof FSize) {
            final FSize other = (FSize) obj;
            return width == other.width && height == other.height;
        }
        return false;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(width) ^ Float.floatToIntBits(height);
    }
}
