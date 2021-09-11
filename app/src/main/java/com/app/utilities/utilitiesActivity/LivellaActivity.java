package com.app.utilities.utilitiesActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.app.utilities.R;
import com.app.utilities.settings.Preferenze;
import com.app.utilities.utility.Utils;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class LivellaActivity extends AppCompatActivity implements SensorEventListener {
    final Utils util = new Utils();
    final TypedValue typedValue = new TypedValue();
    private final Utils utils = new Utils();
    protected Configuration mPrevConfig;
    int microsecond;
    int color;
    int colorOnPrimary;
    int colorPrimaryVariant;
    int colorSecondaryVariant;
    Preferenze pref;
    private Sensor accelerometer;
    private SensorManager sensorManager;
    private AnimatedView animatedView = null;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferenze());
        if (!pref.getPredBool()) {
            if (pref.getThemeText().equals("LightTheme") || pref.getThemeText().equals("LightThemeSelected"))
                utils.changeThemeSelected(this, 0);
            else
                utils.changeThemeSelected(this, 1);
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                case Configuration.UI_MODE_NIGHT_NO:
                    utils.changeTheme(this, 0);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    utils.changeTheme(this, 1);
                    break;
            }
        }
        getTheme().resolveAttribute(R.attr.color, typedValue, true);
        color = typedValue.resourceId;
        getTheme().resolveAttribute(R.attr.colorOnPrimary, typedValue, true);
        colorOnPrimary = typedValue.resourceId;
        getTheme().resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true);
        colorPrimaryVariant = typedValue.resourceId;
        getTheme().resolveAttribute(R.attr.colorSecondaryVariant, typedValue, true);
        colorSecondaryVariant = typedValue.resourceId;
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        microsecond = SensorManager.SENSOR_DELAY_GAME;
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, microsecond);
        } else {
            TabLayout tabs = this.findViewById(R.id.tabs);
            Objects.requireNonNull(tabs.getTabAt(2)).select();
            util.notifyUser(this, "Il tuo dispositivo non dispone di un accellerometro");
        }
        animatedView = new AnimatedView(this);
        animatedView.setBackgroundColor(getColor(colorPrimaryVariant));
        setContentView(animatedView);
        mPrevConfig = new Configuration(getResources().getConfiguration());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            animatedView.onSensorEvent(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (accelerometer != null)
            sensorManager.registerListener(this, accelerometer, microsecond);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null)
            sensorManager.registerListener(this, accelerometer, microsecond);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configurationChanged(newConfig);
        mPrevConfig = new Configuration(newConfig);
    }

    protected void configurationChanged(Configuration newConfig) {
        if (isNightConfigChanged(newConfig) && pref.getPredBool()) {
            utils.refreshActivity(this);
        }
    }

    protected boolean isNightConfigChanged(Configuration newConfig) {
        return (newConfig.diff(mPrevConfig) & ActivityInfo.CONFIG_UI_MODE) != 0 && isOnDarkMode(newConfig) != isOnDarkMode(mPrevConfig);
    }

    public class AnimatedView extends LinearLayout {

        static final float alpha = 0.1f;
        private static final int CIRCLE_RADIOUS = 50;
        private static final int TEXT_SIZE = 60;
        private static final int BAR_THICKNESS = 110;
        private static final int STROKE = 5;
        private static final int TOLERANCE = 10;
        final float half_stroke = (float) (STROKE / 2);
        final float half_barThickness = (float) (BAR_THICKNESS / 2);
        final float half_textSize = (float) (TEXT_SIZE / 2);
        private final Paint horizBubble;
        private final Paint horizPlane;
        private final Paint vertBubble;
        private final Paint vertPlane;
        private final Paint roundBubble;
        private final Paint roundPlane;
        private final Paint border;
        private final Paint textX;
        private final Paint textY;
        private final Paint textPlane;
        Bitmap bitmap;
        BitmapShader fillBMPshader;
        float hor_start_w = 0;
        float hor_end_w = 0;
        float hor_h = 0;
        float ver_start_h = 0;
        float ver_end_h = 0;
        float ver_w = 0;
        float half_hor = 0;
        float half_ver = 0;
        float length_bar = 0;
        double x_text = 0;
        double y_text = 0;
        private double x = 0;
        private double y = 0;
        private float horizPos;
        private float vertPos;
        private int r;
        private float xRound;
        private float yRound;
        private float txt_pln_start;
        private float txt_pln_end;
        private float txt_pln_hight;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public AnimatedView(Context context) {
            super(context);

            horizBubble = new Paint();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
            fillBMPshader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            horizBubble.setShader(fillBMPshader);

            horizPlane = new Paint();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.green);
            fillBMPshader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            horizPlane.setShader(fillBMPshader);
            horizPlane.setStrokeWidth(BAR_THICKNESS);

            vertBubble = new Paint();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
            fillBMPshader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            vertBubble.setShader(fillBMPshader);

            vertPlane = new Paint();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.green);
            fillBMPshader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            vertPlane.setShader(fillBMPshader);
            vertPlane.setStrokeWidth(BAR_THICKNESS);

            roundBubble = new Paint();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
            fillBMPshader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            roundBubble.setShader(fillBMPshader);

            roundPlane = new Paint();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.green);
            fillBMPshader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            roundPlane.setShader(fillBMPshader);

            border = new Paint();
            border.setColor(getColor(R.color.black));
            border.setStrokeWidth(STROKE);
            border.setStyle(Paint.Style.STROKE);

            textPlane = new Paint();
            if (!pref.getPredBool()) {
                if (pref.getThemeText().equals("LightTheme") || pref.getThemeText().equals("LightThemeSelected"))
                    textPlane.setColor(getColor(color));
                else
                    textPlane.setColor(getColor(colorSecondaryVariant));
            } else
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:

                    case Configuration.UI_MODE_NIGHT_NO:
                        textPlane.setColor(getColor(color));
                        break;

                    case Configuration.UI_MODE_NIGHT_YES:
                        textPlane.setColor(getColor(colorSecondaryVariant));
                        break;
                }
            textPlane.setStrokeWidth(BAR_THICKNESS);
            textPlane.setStyle(Paint.Style.STROKE);

            textX = new Paint();
            textX.setColor(getColor(colorOnPrimary));
            textX.setStyle(Paint.Style.FILL_AND_STROKE);
            textX.setTextSize(TEXT_SIZE);
            textX.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            textY = new Paint();
            textY.setColor(getColor(colorOnPrimary));
            textY.setStyle(Paint.Style.FILL_AND_STROKE);
            textY.setTextSize(TEXT_SIZE);
            textY.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            ImageButton back = new ImageButton(LivellaActivity.this);

            if (!pref.getPredBool()) {
                if (pref.getThemeText().equals("LightTheme") || pref.getThemeText().equals("LightThemeSelected"))
                    back.setImageResource(R.drawable.ic_arrow_back_light);
                else
                    back.setImageResource(R.drawable.ic_arrow_back_dark);
            } else {
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    case Configuration.UI_MODE_NIGHT_NO:
                        back.setImageResource(R.drawable.ic_arrow_back_light);
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        back.setImageResource(R.drawable.ic_arrow_back_dark);
                        break;
                }
            }
            addView(back);
            back.setOnClickListener(view -> onBackPressed());
            back.setBackgroundColor(getColor(R.color.transparent));
            ((MarginLayoutParams) back.getLayoutParams()).leftMargin = 60;
            ((MarginLayoutParams) back.getLayoutParams()).topMargin = 30;
        }

        @SuppressWarnings("unused")
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            float half_width = (float) (w / 2);
            float half_height = (float) (h / 2);
            length_bar = (float) (w - ((w / 8) * 2));

            hor_start_w = (float) (w / 8);
            hor_end_w = hor_start_w + length_bar;
            hor_h = (float) (h / 5);

            ver_start_h = (float) (h / 3.5);
            ver_end_h = ver_start_h + length_bar;
            ver_w = (float) (w - (w / 6));

            r = w / 4;

            txt_pln_start = (float) (hor_start_w * 0.8);
            txt_pln_end = hor_start_w * 5;
            txt_pln_hight = (float) (ver_end_h - (TEXT_SIZE / 1.1));

            half_hor = half_width;
            half_ver = (ver_start_h + ver_end_h) / 2;
        }

        public void onSensorEvent(@NonNull SensorEvent event) {

            x = x + alpha * (event.values[0] - x);
            y = y + alpha * (event.values[1] - y);

            if (x > 10) x = 10;
            if (x < -10) x = -10;
            if (y > 10) y = 10;
            if (y < -10) y = -10;

            horizPos = (float) (util.roundAvoid(mapX(x, length_bar), 1) + (double) hor_start_w);
            vertPos = (float) (util.roundAvoid(mapY(y, length_bar), 1) + (double) ver_start_h);

            xRound = (float) (x * (r - CIRCLE_RADIOUS) / 10);
            yRound = (float) (-y * (r - CIRCLE_RADIOUS) / 10);
            float module = (float) (Math.sqrt(Math.pow(xRound, 2) + Math.pow(yRound, 2)));
            if (module > r) {
                xRound = (float) ((x * r) / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
                yRound = (float) ((-y * r) / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
            }

            x_text = util.roundAvoid((float) -(((45 - (CIRCLE_RADIOUS * 1.8125)) / 10) * x), 1);
            y_text = util.roundAvoid((float) -(((45 - (CIRCLE_RADIOUS * 1.8125)) / 10) * y), 1);

            xRound += half_hor - CIRCLE_RADIOUS - TOLERANCE;
            yRound += half_ver - CIRCLE_RADIOUS - TOLERANCE;

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawLine(hor_start_w, hor_h, hor_end_w, hor_h, horizPlane);
            canvas.drawCircle(horizPos, hor_h, CIRCLE_RADIOUS, horizBubble);
            canvas.drawRect(hor_start_w - half_stroke, hor_h - half_stroke - half_barThickness, hor_end_w + half_stroke, hor_h + half_stroke + half_barThickness, border);
            canvas.drawRect(half_hor - half_stroke - CIRCLE_RADIOUS - TOLERANCE, hor_h - half_stroke - half_barThickness, half_hor + half_stroke + CIRCLE_RADIOUS + TOLERANCE, hor_h + half_stroke + half_barThickness, border);

            canvas.drawLine(ver_w, ver_start_h, ver_w, ver_end_h, vertPlane);
            canvas.drawCircle(ver_w, vertPos, CIRCLE_RADIOUS, vertBubble);
            canvas.drawRect(ver_w - half_stroke - half_barThickness, ver_start_h - half_stroke, ver_w + half_stroke + half_barThickness, ver_end_h + half_stroke, border);
            canvas.drawRect(ver_w - half_stroke - half_barThickness, half_ver - half_stroke - CIRCLE_RADIOUS - TOLERANCE, ver_w + half_stroke + half_barThickness, half_ver + half_stroke + CIRCLE_RADIOUS + TOLERANCE, border);

            canvas.drawCircle(half_hor - CIRCLE_RADIOUS - TOLERANCE, half_ver - CIRCLE_RADIOUS - TOLERANCE, r, roundPlane);
            canvas.drawCircle(xRound, yRound, CIRCLE_RADIOUS, roundBubble);
            canvas.drawCircle(half_hor - CIRCLE_RADIOUS - TOLERANCE, half_ver - CIRCLE_RADIOUS - TOLERANCE, r + half_stroke, border);
            canvas.drawCircle(half_hor - CIRCLE_RADIOUS - TOLERANCE, half_ver - CIRCLE_RADIOUS - TOLERANCE, CIRCLE_RADIOUS + TOLERANCE + half_stroke, border);
            canvas.drawLine(half_hor - CIRCLE_RADIOUS - TOLERANCE - r - half_stroke, half_ver - CIRCLE_RADIOUS - TOLERANCE, half_hor - CIRCLE_RADIOUS - TOLERANCE + r + half_stroke, half_ver - CIRCLE_RADIOUS - TOLERANCE, border);
            canvas.drawLine(half_hor - CIRCLE_RADIOUS - TOLERANCE, half_ver - CIRCLE_RADIOUS - TOLERANCE - r - half_stroke, half_hor - CIRCLE_RADIOUS - TOLERANCE, half_ver - CIRCLE_RADIOUS - TOLERANCE + r + half_stroke, border);

            canvas.drawLine(txt_pln_start, txt_pln_hight, txt_pln_end, txt_pln_hight, textPlane);

            canvas.drawText("X: " + x_text + "°", hor_start_w, ver_end_h - half_textSize, textX);
            canvas.drawText("Y: " + y_text + "°", (hor_start_w * 3), ver_end_h - half_textSize, textY);

            invalidate();
        }

        double mapX(double x, double wd) {
            return (float) ((((wd - (CIRCLE_RADIOUS * 2)) / 20) * x) + (wd / 2));
        }

        double mapY(double y, double hg) {
            return (float) ((((hg - (CIRCLE_RADIOUS * 2)) / 20) * (-y)) + (hg / 2));
        }

    }

}