package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class AxisRenderer extends Renderer {
    protected AxisBase mAxis;
    protected Transformer mTrans;
    protected Paint mGridPaint;
    protected Paint mAxisLabelPaint;
    protected Paint mAxisLinePaint;
    protected Paint mLimitLinePaint;

    public AxisRenderer(ViewPortHandler viewPortHandler, Transformer trans, AxisBase axis) {
        super(viewPortHandler);
        this.mTrans = trans;
        this.mAxis = axis;
        if (mViewPortHandler != null) {
            mAxisLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mGridPaint = new Paint();
            mGridPaint.setColor(Color.GRAY);
            mGridPaint.setStrokeWidth(1f);
            mGridPaint.setStyle(Style.STROKE);
            mGridPaint.setAlpha(90);
            mAxisLinePaint = new Paint();
            mAxisLinePaint.setColor(Color.BLACK);
            mAxisLinePaint.setStrokeWidth(1f);
            mAxisLinePaint.setStyle(Style.STROKE);
            mLimitLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLimitLinePaint.setStyle(Paint.Style.STROKE);
        }
    }

    public Paint getPaintAxisLabels() {
        return mAxisLabelPaint;
    }

    public Paint getPaintGrid() {
        return mGridPaint;
    }

    public Paint getPaintAxisLine() {
        return mAxisLinePaint;
    }

    public Transformer getTransformer() {
        return mTrans;
    }

    public void computeAxis(float min, float max, boolean inverted) {
        if (mViewPortHandler != null && mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutY()) {
            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom());
            if (!inverted) {
                min = (float) p2.y;
                max = (float) p1.y;
            } else {
                min = (float) p1.y;
                max = (float) p2.y;
            }
            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }
        computeAxisValues(min, max);
    }

    protected void computeAxisValues(float min, float max) {
        float yMin = min;
        float yMax = max;
        int labelCount = mAxis.getLabelCount();
        double range = Math.abs(yMax - yMin);
        if (labelCount == 0 || range <= 0 || Double.isInfinite(range)) {
            mAxis.mEntries = new float[]{};
            mAxis.mCenteredEntries = new float[]{};
            mAxis.mEntryCount = 0;
            return;
        }
        double rawInterval = range intervalMagnitude);
        if (intervalSigDigit > 5) {
            interval = Math.floor(10.0 * intervalMagnitude) == 0.0
                    ? interval
                    : Math.floor(10.0 * intervalMagnitude);
        }
        int n = mAxis.isCenterAxisLabelsEnabled() ? 1 : 0;
        if (mAxis.isForceLabelsEnabled()) {
            interval = (float) range interval) *interval;
            if (mAxis.isCenterAxisLabelsEnabled()) {
                first -= interval;
            }
            double last = interval == 0.0 ? 0.0 : Utils.nextUp(Math.floor(yMax / interval) * interval);
            double f;
            int i;
            if (interval != 0.0 && last != first) {
                for (f = first; f <= last; f += interval) {
                    ++n;
                }
            } else if (last == first && n == 0) {
                n = 1;
            }
            mAxis.mEntryCount = n;
            if (mAxis.mEntries.length < n) {
                mAxis.mEntries = new float[n];
            }
            for (f = first, i = 0; i < n; f += interval, ++i) {
                if (f == 0.0)
                    f = 0.0;
                mAxis.mEntries[i] = (float) f;
            }
        }
        if (interval < 1) {
            mAxis.mDecimals = (int) Math.ceil(-Math.log10(interval));
        } else {
            mAxis.mDecimals = 0;
        }
        if (mAxis.isCenterAxisLabelsEnabled()) {
            if (mAxis.mCenteredEntries.length < n) {
                mAxis.mCenteredEntries = new float[n];
            }
            float offset = (float) interval / 2f;
            for (int i = 0; i < n; i++) {
                mAxis.mCenteredEntries[i] = mAxis.mEntries[i] + offset;
            }
        }
    }

    public abstract void renderAxisLabels(Canvas c);

    public abstract void renderGridLines(Canvas c);

    public abstract void renderAxisLine(Canvas c);

    public abstract void renderLimitLines(Canvas c);
}
