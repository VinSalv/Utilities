package com.example.utilities.Bussola;

import android.content.Context;

import com.example.utilities.R;

public class SOTWFormatter {
    private static final int[] sides = {0, 45, 90, 135, 180, 225, 270, 315, 360};
    private static String[] names = null;

    public SOTWFormatter(Context context) {
        initLocalizedNames(context);
    }

    private static int findClosestIndex(int target) {
        // Doing binary search
        int i = 0, j = sides.length, mid = 0;
        while (i < j) {
            mid = (i + j) / 2;
            /* If target is less than array element,
               then search in left */
            if (target < sides[mid]) {
                // If target is greater than previous
                // to mid, return closest of two
                if (mid > 0 && target > sides[mid - 1]) {
                    return getClosest(mid - 1, mid, target);
                }
                /* Repeat for left half */
                j = mid;
            } else {
                if (mid < sides.length - 1 && target < sides[mid + 1]) {
                    return getClosest(mid, mid + 1, target);
                }
                i = mid + 1; // update i
            }
        }
        // Only single element left after search
        return mid;
    }

    private static int getClosest(int index1, int index2, int target) {
        if (target - sides[index1] >= sides[index2] - target) {
            return index2;
        }
        return index1;
    }

    public String format(float azimuth) {
        int iAzimuth = (int) azimuth;
        int index = findClosestIndex(iAzimuth);
        return iAzimuth + "° " + names[index];
    }

    private void initLocalizedNames(Context context) {
        // it will be localized version of
        // {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"}
        // yes, N is twice, for 0 and for 360

        if (names == null) {
            names = new String[]{
                    context.getString(R.string.sotw_north),
                    context.getString(R.string.sotw_northeast),
                    context.getString(R.string.sotw_east),
                    context.getString(R.string.sotw_southeast),
                    context.getString(R.string.sotw_south),
                    context.getString(R.string.sotw_southwest),
                    context.getString(R.string.sotw_west),
                    context.getString(R.string.sotw_northwest),
                    context.getString(R.string.sotw_north)
            };
        }
    }
}