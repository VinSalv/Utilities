package com.app.utilities.utilitiesActivity.bussola.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.app.utilities.R;

public enum SensorAccuracy {
    NO_CONTACT(R.string.sensor_accuracy_no_contact, R.drawable.ic_sensor_no_contact),
    UNRELIABLE(R.string.sensor_accuracy_unreliable, R.drawable.ic_sensor_unreliable),
    LOW(R.string.sensor_accuracy_low, R.drawable.ic_sensor_low),
    MEDIUM(R.string.sensor_accuracy_medium, R.drawable.ic_sensor_medium),
    HIGH(R.string.sensor_accuracy_high, R.drawable.ic_sensor_high);
    private final int textResourceId;
    private final int iconResourceId;

    SensorAccuracy(@StringRes int textResourceId, @DrawableRes int iconResourceId) {
        this.textResourceId = textResourceId;
        this.iconResourceId = iconResourceId;
    }

    public final int getTextResourceId() {
        return this.textResourceId;
    }

    public final int getIconResourceId() {
        return this.iconResourceId;
    }
}
