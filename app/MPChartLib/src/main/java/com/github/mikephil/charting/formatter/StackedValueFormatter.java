package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class StackedValueFormatter implements IValueFormatter {
    private boolean mDrawWholeStack;
    private String mAppendix;
    private DecimalFormat mFormat;

    public StackedValueFormatter(boolean drawWholeStack, String appendix, int decimals) {
        this.mDrawWholeStack = drawWholeStack;
        this.mAppendix = appendix;
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < decimals; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }
        this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if (!mDrawWholeStack && entry instanceof BarEntry) {
            BarEntry barEntry = (BarEntry) entry;
            float[] vals = barEntry.getYVals();
            if (vals != null) {

                if (vals[vals.length - 1] == value) {

                    return mFormat.format(barEntry.getY()) + mAppendix;
                } else {
                    return "";
                }
            }
        }

        return mFormat.format(value) + mAppendix;
    }
}
