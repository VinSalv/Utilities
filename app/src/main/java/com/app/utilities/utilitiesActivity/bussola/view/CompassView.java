package com.app.utilities.utilitiesActivity.bussola.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.app.utilities.R;
import com.app.utilities.utilitiesActivity.bussola.model.Azimuth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CompassView extends ConstraintLayout {
    ConstraintSet constraintSet = null;
    private AppCompatImageView compassRoseImage;
    private AppCompatTextView statusDegreesText;
    private AppCompatTextView statusCardinalDirectionText;
    private AppCompatTextView cardinalDirectionNorthText;
    private AppCompatTextView cardinalDirectionEastText;
    private AppCompatTextView cardinalDirectionSouthText;
    private AppCompatTextView cardinalDirectionWestText;
    private AppCompatTextView degree0Text;
    private AppCompatTextView degree30Text;
    private AppCompatTextView degree60Text;
    private AppCompatTextView degree90Text;
    private AppCompatTextView degree120Text;
    private AppCompatTextView degree150Text;
    private AppCompatTextView degree180Text;
    private AppCompatTextView degree210Text;
    private AppCompatTextView degree240Text;
    private AppCompatTextView degree270Text;
    private AppCompatTextView degree300Text;
    private AppCompatTextView degree330Text;
    @NotNull
    private Azimuth azimuth;

    public CompassView(@NotNull Context context, @NotNull AttributeSet attributeSet) {
        super(context, attributeSet);
        this.azimuth = new Azimuth(0.0F);
        ConstraintLayout.inflate(context, R.layout.compass_view, this);
    }

    public final void setAzimuth(@NotNull Azimuth var1) {
        this.azimuth = var1;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        compassRoseImage = findViewById(R.id.compass_rose_image);
        statusDegreesText = findViewById(R.id.status_degrees_text);
        statusCardinalDirectionText = findViewById(R.id.status_cardinal_direction_text);
        cardinalDirectionNorthText = findViewById(R.id.cardinal_direction_north_text);
        cardinalDirectionEastText = findViewById(R.id.cardinal_direction_east_text);
        cardinalDirectionSouthText = findViewById(R.id.cardinal_direction_south_text);
        cardinalDirectionWestText = findViewById(R.id.cardinal_direction_west_text);
        degree0Text = findViewById(R.id.degree_0_text);
        degree30Text = findViewById(R.id.degree_30_text);
        degree60Text = findViewById(R.id.degree_60_text);
        degree90Text = findViewById(R.id.degree_90_text);
        degree120Text = findViewById(R.id.degree_120_text);
        degree150Text = findViewById(R.id.degree_150_text);
        degree180Text = findViewById(R.id.degree_180_text);
        degree210Text = findViewById(R.id.degree_210_text);
        degree240Text = findViewById(R.id.degree_240_text);
        degree270Text = findViewById(R.id.degree_270_text);
        degree300Text = findViewById(R.id.degree_300_text);
        degree330Text = findViewById(R.id.degree_330_text);
    }

    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        this.updateStatusTextSize((float) width * 0.08F);
        this.updateCardinalDirectionTextSize((float) width * 0.08F);
        this.updateDegreeTextSize((float) width * 0.03F);
    }

    private void updateStatusTextSize(float textSize) {
        this.statusDegreesText.setTextSize(0, textSize);
    }

    private void updateCardinalDirectionTextSize(float textSize) {
        this.cardinalDirectionNorthText.setTextSize(0, textSize);
        this.cardinalDirectionEastText.setTextSize(0, textSize);
        this.cardinalDirectionSouthText.setTextSize(0, textSize);
        this.cardinalDirectionWestText.setTextSize(0, textSize);
    }

    private void updateDegreeTextSize(float textSize) {
        this.degree0Text.setTextSize(0, textSize);
        this.degree30Text.setTextSize(0, textSize);
        this.degree60Text.setTextSize(0, textSize);
        this.degree90Text.setTextSize(0, textSize);
        this.degree120Text.setTextSize(0, textSize);
        this.degree150Text.setTextSize(0, textSize);
        this.degree180Text.setTextSize(0, textSize);
        this.degree210Text.setTextSize(0, textSize);
        this.degree240Text.setTextSize(0, textSize);
        this.degree270Text.setTextSize(0, textSize);
        this.degree300Text.setTextSize(0, textSize);
        this.degree330Text.setTextSize(0, textSize);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void dispatchDraw(@Nullable Canvas canvas) {
        this.updateViews(this.azimuth.plus(this.displayRotation()));
        super.dispatchDraw(canvas);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private float displayRotation() {
        Display var10000 = this.getDisplay();
        Integer var1 = var10000 != null ? var10000.getRotation() : null;
        byte var2 = 1;
        float var3;
        if (var1 != null) {
            if (var1 == var2) {
                var3 = 90.0F;
                return var3;
            }
        }
        var2 = 2;
        if (var1 != null) {
            if (var1 == var2) {
                var3 = 180.0F;
                return var3;
            }
        }
        var2 = 3;
        if (var1 != null) {
            if (var1 == var2) {
                var3 = 270.0F;
                return var3;
            }
        }
        var3 = 0.0F;
        return var3;
    }

    private void updateViews(Azimuth azimuth) {
        this.updateStatusDegreesText(azimuth);
        this.updateStatusDirectionText(azimuth);
        float rotation = -azimuth.getDegrees();
        this.rotateCompassRoseImage(rotation);
        this.rotateCompassRoseTexts(rotation);
    }

    @SuppressLint("SetTextI18n")
    private void updateStatusDegreesText(Azimuth azimuth) {
        statusDegreesText.setText((Math.round(azimuth.getDegrees())) + "°");
    }

    private void updateStatusDirectionText(Azimuth azimuth) {
        this.statusCardinalDirectionText.setText(this.getContext().getString(azimuth.getCardinalDirection().getLabelResourceId()));
    }

    private void rotateCompassRoseImage(float rotation) {
        this.compassRoseImage.setRotation(rotation);
    }

    private void rotateCompassRoseTexts(float rotation) {
        constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        rotateCardinalDirectionTexts(constraintSet, rotation);
        rotateDegreeTexts(constraintSet, rotation);
        constraintSet.applyTo(this);
    }

    private void rotateCardinalDirectionTexts(ConstraintSet constraintSet, float rotation) {
        int radius = calculateTextRadius(0.23F);
        constraintSet.constrainCircle(R.id.cardinal_direction_north_text, R.id.compass_rose_image, radius, rotation);
        constraintSet.constrainCircle(R.id.cardinal_direction_east_text, R.id.compass_rose_image, radius, rotation + (float) 90);
        constraintSet.constrainCircle(R.id.cardinal_direction_south_text, R.id.compass_rose_image, radius, rotation + (float) 180);
        constraintSet.constrainCircle(R.id.cardinal_direction_west_text, R.id.compass_rose_image, radius, rotation + (float) 270);
    }

    private void rotateDegreeTexts(ConstraintSet constraintSet, float rotation) {
        int radius = calculateTextRadius(0.08F);
        constraintSet.constrainCircle(R.id.degree_0_text, R.id.compass_rose_image, radius, rotation);
        constraintSet.constrainCircle(R.id.degree_30_text, R.id.compass_rose_image, radius, rotation + (float) 30);
        constraintSet.constrainCircle(R.id.degree_60_text, R.id.compass_rose_image, radius, rotation + (float) 60);
        constraintSet.constrainCircle(R.id.degree_90_text, R.id.compass_rose_image, radius, rotation + (float) 90);
        constraintSet.constrainCircle(R.id.degree_120_text, R.id.compass_rose_image, radius, rotation + (float) 120);
        constraintSet.constrainCircle(R.id.degree_150_text, R.id.compass_rose_image, radius, rotation + (float) 150);
        constraintSet.constrainCircle(R.id.degree_180_text, R.id.compass_rose_image, radius, rotation + (float) 180);
        constraintSet.constrainCircle(R.id.degree_210_text, R.id.compass_rose_image, radius, rotation + (float) 210);
        constraintSet.constrainCircle(R.id.degree_240_text, R.id.compass_rose_image, radius, rotation + (float) 240);
        constraintSet.constrainCircle(R.id.degree_270_text, R.id.compass_rose_image, radius, rotation + (float) 270);
        constraintSet.constrainCircle(R.id.degree_300_text, R.id.compass_rose_image, radius, rotation + (float) 300);
        constraintSet.constrainCircle(R.id.degree_330_text, R.id.compass_rose_image, radius, rotation + (float) 330);
    }

    private int calculateTextRadius(float ratio) {
        return this.getWidth() / 2 - (int) ((float) this.getWidth() * ratio);
    }
}
