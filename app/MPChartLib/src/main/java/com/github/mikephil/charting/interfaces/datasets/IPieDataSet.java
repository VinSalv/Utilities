package com.github.mikephil.charting.interfaces.datasets;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

public interface IPieDataSet extends IDataSet<PieEntry> {
    float getSliceSpace();

    boolean isAutomaticallyDisableSliceSpacingEnabled();

    float getSelectionShift();

    PieDataSet.ValuePosition getXValuePosition();

    PieDataSet.ValuePosition getYValuePosition();

    int getValueLineColor();

    boolean isUseValueColorForLineEnabled();

    float getValueLineWidth();

    float getValueLinePart1OffsetPercentage();

    float getValueLinePart1Length();

    float getValueLinePart2Length();

    boolean isValueLineVariableLength();

    @Nullable
    Integer getHighlightColor();
}
