package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;

public class IndexAxisValueFormatter implements IAxisValueFormatter {
    private String[] mValues = new String[]{};
    private int mValueCount = 0;

    public IndexAxisValueFormatter() {
    }

    public IndexAxisValueFormatter(String[] values) {
        if (values != null)
            setValues(values);
    }

    public IndexAxisValueFormatter(Collection<String> values) {
        if (values != null)
            setValues(values.toArray(new String[values.size()]));
    }

    public String getFormattedValue(float value, AxisBase axis) {
        int index = Math.round(value);
        if (index < 0 || index >= mValueCount || index != (int) value)
            return "";
        return mValues[index];
    }

    public String[] getValues() {
        return mValues;
    }

    public void setValues(String[] values) {
        if (values == null)
            values = new String[]{};
        this.mValues = values;
        this.mValueCount = values.length;
    }
}
