package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;

public interface IAxisValueFormatter {
    String getFormattedValue(float value, AxisBase axis);
}
