package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.Fill;

import java.util.List;

public interface IBarDataSet extends IBarLineScatterCandleBubbleDataSet<BarEntry> {
    List<Fill> getFills();

    Fill getFill(int index);

    boolean isStacked();

    int getStackSize();

    int getBarShadowColor();

    float getBarBorderWidth();

    int getBarBorderColor();

    int getHighLightAlpha();

    String[] getStackLabels();
}
