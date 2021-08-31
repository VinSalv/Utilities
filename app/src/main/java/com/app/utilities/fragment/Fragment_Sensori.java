package com.app.utilities.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.utilities.R;
import com.app.utilities.utility.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class Fragment_Sensori extends Fragment implements SensorEventListener {
    private final Utils utils = new Utils();
    TextView xValue, yValue, zValue, xGyroValue, yGyroValue, zGyroValue, xMagnoValue, yMagnoValue, zMagnoValue;
    Spinner speedAcc, speedGir, speedMag;
    SensorManager sensorManager;
    Sensor mAccel, mGyro, mMagno;
    LinearLayout sensorsLayout;
    int accSimpRate, girSimpRate, magSimpRate;
    Button otherSensors;
    private Thread thread;
    private boolean plotData = true;
    private LineChart chartAceleX, chartAceleY, chartAceleZ, chartGirX, chartGirY, chartGirZ, chartMagnetX, chartMagnetY, chartMagnetZ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__sensori, container, false);

        speedAcc = view.findViewById(R.id.speedAcc);
        speedGir = view.findViewById(R.id.speedGir);
        speedMag = view.findViewById(R.id.speedMag);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.speed));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speedAcc.setAdapter(arrayAdapter);
        speedGir.setAdapter(arrayAdapter);
        speedMag.setAdapter(arrayAdapter);

        sensorsLayout = view.findViewById(R.id.sensorsLayout);

        xValue = view.findViewById(R.id.xValue);
        yValue = view.findViewById(R.id.yValue);
        zValue = view.findViewById(R.id.zValue);
        xGyroValue = view.findViewById(R.id.xGyroValue);
        yGyroValue = view.findViewById(R.id.yGyroValue);
        zGyroValue = view.findViewById(R.id.zGyroValue);
        xMagnoValue = view.findViewById(R.id.xMagnoValue);
        yMagnoValue = view.findViewById(R.id.yMagnoValue);
        zMagnoValue = view.findViewById(R.id.zMagnoValue);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        chartAceleX = (LineChart) view.findViewById(R.id.chartAceleX);
        chartAceleY = (LineChart) view.findViewById(R.id.chartAceleY);
        chartAceleZ = (LineChart) view.findViewById(R.id.chartAceleZ);

        chartGirX = (LineChart) view.findViewById(R.id.chartGirX);
        chartGirY = (LineChart) view.findViewById(R.id.chartGirY);
        chartGirZ = (LineChart) view.findViewById(R.id.chartGirZ);

        chartMagnetX = (LineChart) view.findViewById(R.id.chartMagnetX);
        chartMagnetY = (LineChart) view.findViewById(R.id.chartMagnetY);
        chartMagnetZ = (LineChart) view.findViewById(R.id.chartMagnetZ);

        accSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
        girSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
        magSimpRate = SensorManager.SENSOR_DELAY_NORMAL;

        if (mAccel != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mAccel, accSimpRate);
            config(chartAceleX, "accelerometro");
            config(chartAceleY, "accelerometro");
            config(chartAceleZ, "accelerometro");

        } else {
            xValue.setText(R.string.AccelerometerNotSupported);
            sensorsLayout.removeView(chartAceleX);
            sensorsLayout.removeView(chartAceleY);
            sensorsLayout.removeView(chartAceleZ);
            sensorsLayout.removeView(speedAcc);
        }
        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyro != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mGyro, girSimpRate);
            config(chartGirX, "giroscopio");
            config(chartGirY, "giroscopio");
            config(chartGirZ, "giroscopio");
        } else {
            xGyroValue.setText(R.string.GyroscopeNotSupported);
            sensorsLayout.removeView(chartGirX);
            sensorsLayout.removeView(chartGirY);
            sensorsLayout.removeView(chartGirZ);
            sensorsLayout.removeView(speedGir);
        }
        mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagno != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mMagno, magSimpRate);
            config(chartMagnetX, "magnetometro");
            config(chartMagnetY, "magnetometro");
            config(chartMagnetZ, "magnetometro");
        } else {
            xMagnoValue.setText(R.string.MagnetometerNotSupported);
            sensorsLayout.removeView(chartMagnetX);
            sensorsLayout.removeView(chartMagnetY);
            sensorsLayout.removeView(chartMagnetZ);
            sensorsLayout.removeView(speedMag);
        }

        speedAcc.setSelection(3);
        speedAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        accSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    case 1:
                        accSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case 2:
                        accSimpRate = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case 3:
                    default:
                        accSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                    case 4:
                        accSimpRate = 400000;
                        break;
                    case 5:
                        accSimpRate = 800000;
                        break;
                    case 6:
                        accSimpRate = 1000000;
                        break;
                }
                if (mAccel != null) {
                    sensorManager.unregisterListener(Fragment_Sensori.this, mAccel);
                    sensorManager.registerListener(Fragment_Sensori.this, mAccel, accSimpRate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        speedGir.setSelection(3);
        speedGir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        girSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    case 1:
                        girSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case 2:
                        girSimpRate = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case 3:
                    default:
                        girSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                    case 4:
                        girSimpRate = 400000;
                        break;
                    case 5:
                        girSimpRate = 800000;
                        break;
                    case 6:
                        girSimpRate = 1000000;
                        break;
                }
                if (mGyro != null) {
                    sensorManager.unregisterListener(Fragment_Sensori.this, mGyro);
                    sensorManager.registerListener(Fragment_Sensori.this, mGyro, girSimpRate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        speedMag.setSelection(3);
        speedMag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        magSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    case 1:
                        magSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case 2:
                        magSimpRate = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case 3:
                    default:
                        magSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                    case 4:
                        magSimpRate = 400000;
                        break;
                    case 5:
                        magSimpRate = 800000;
                        break;
                    case 6:
                        magSimpRate = 1000000;
                        break;
                }
                if (mMagno != null) {
                    sensorManager.unregisterListener(Fragment_Sensori.this, mMagno);
                    sensorManager.registerListener(Fragment_Sensori.this, mMagno, magSimpRate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        feedMultiple();
        otherSensors = view.findViewById(R.id.otherSensors);
        otherSensors.setOnClickListener(v -> utils.goToAltriSensoriActivity(requireActivity()));
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xValue.setText(Html.fromHtml("<b>X: </b>" + utils.roundAvoid(event.values[0], 2)));
            yValue.setText(Html.fromHtml("<b>Y: </b>" + utils.roundAvoid(event.values[1], 2)));
            zValue.setText(Html.fromHtml("<b>Z: </b>" + utils.roundAvoid(event.values[2], 2)));
            if (plotData) {
                addEntry(event, "accelerometro");
                plotData = false;
            }
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            xGyroValue.setText(Html.fromHtml("<b>X: </b>" + utils.roundAvoid(event.values[0], 2)));
            yGyroValue.setText(Html.fromHtml("<b>Y: </b>" + utils.roundAvoid(event.values[1], 2)));
            zGyroValue.setText(Html.fromHtml("<b>Z: </b>" + utils.roundAvoid(event.values[2], 2)));
            if (plotData) {
                addEntry(event, "giroscopio");
                plotData = false;
            }
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            xMagnoValue.setText(Html.fromHtml("<b>X: </b>" + utils.roundAvoid(event.values[0], 2)));
            yMagnoValue.setText(Html.fromHtml("<b>Y: </b>" + utils.roundAvoid(event.values[1], 2)));
            zMagnoValue.setText(Html.fromHtml("<b>Z: </b>" + utils.roundAvoid(event.values[2], 2)));
            if (plotData) {
                addEntry(event, "magnetometro");
                plotData = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void config(LineChart chart, String sensor) {
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);
        chart.setBackgroundColor(Color.WHITE);
        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);
        XAxis xl = chart.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(false);
        xl.setEnabled(true);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(false);
        //noinspection CommentedOutCode
        switch (sensor) {
            case "accelerometro":
            case "giroscopio":
                leftAxis.setAxisMaximum(10f);
                leftAxis.setAxisMinimum(-10f);
                break;
                /*case "magnetometro":
                leftAxis.setAxisMaximum(80);
                leftAxis.setAxisMinimum(-80);
                break;*/
        }
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getXAxis().setDrawGridLines(true);
        chart.setDrawBorders(false);
    }

    private void addEntry(SensorEvent event, String sensor) {
        LineData data;
        switch (sensor) {
            case "accelerometro":
                data = chartAceleX.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("accel. x");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0]), 0);
                    data.notifyDataChanged();
                    chartAceleX.notifyDataSetChanged();
                    chartAceleX.setVisibleXRangeMaximum(10);
                    chartAceleX.moveViewToX(data.getEntryCount());
                }
                data = chartAceleY.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("accel. y");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[1]), 0);
                    data.notifyDataChanged();
                    chartAceleY.notifyDataSetChanged();
                    chartAceleY.setVisibleXRangeMaximum(10);
                    chartAceleY.moveViewToX(data.getEntryCount());
                }
                data = chartAceleZ.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("accel. z");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[2]), 0);
                    data.notifyDataChanged();
                    chartAceleZ.notifyDataSetChanged();
                    chartAceleZ.setVisibleXRangeMaximum(10);
                    chartAceleZ.moveViewToX(data.getEntryCount());
                }
                break;
            case "giroscopio":
                data = chartGirX.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("giro. x");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0]), 0);
                    data.notifyDataChanged();
                    chartGirX.notifyDataSetChanged();
                    chartGirX.setVisibleXRangeMaximum(10);
                    chartGirX.moveViewToX(data.getEntryCount());
                }
                data = chartGirY.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("giro. y");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[1]), 0);
                    data.notifyDataChanged();
                    chartGirY.notifyDataSetChanged();
                    chartGirY.setVisibleXRangeMaximum(10);
                    chartGirY.moveViewToX(data.getEntryCount());
                }
                data = chartGirZ.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("giro. z");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[2]), 0);
                    data.notifyDataChanged();
                    chartGirZ.notifyDataSetChanged();
                    chartGirZ.setVisibleXRangeMaximum(10);
                    chartGirZ.moveViewToX(data.getEntryCount());
                }
                break;
            case "magnetometro":
                data = chartMagnetX.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet(" magn. x");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0]), 0);
                    data.notifyDataChanged();
                    chartMagnetX.notifyDataSetChanged();
                    chartMagnetX.setVisibleXRangeMaximum(10);
                    chartMagnetX.moveViewToX(data.getEntryCount());
                }
                data = chartMagnetY.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("magn. y");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[1]), 0);
                    data.notifyDataChanged();
                    chartMagnetY.notifyDataSetChanged();
                    chartMagnetY.setVisibleXRangeMaximum(10);
                    chartMagnetY.moveViewToX(data.getEntryCount());
                }
                data = chartMagnetZ.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("magn. z");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[2]), 0);
                    data.notifyDataChanged();
                    chartMagnetZ.notifyDataSetChanged();
                    chartMagnetZ.setVisibleXRangeMaximum(10);
                    chartMagnetZ.moveViewToX(data.getEntryCount());
                }
                break;
        }

    }

    @NonNull
    private LineDataSet createSet(String data) {
        LineDataSet set = new LineDataSet(null, data);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.GREEN);
        set.setHighlightEnabled(false);
        set.setDrawValues(true);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    private void feedMultiple() {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(() -> {
            while (true) {
                plotData = true;
                try {
                    //noinspection BusyWait
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (thread != null) {
            thread.interrupt();
        }
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAccel != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mAccel, accSimpRate);
        } else {
            xValue.setText(R.string.AccelerometerNotSupported);
        }
        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyro != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mGyro, girSimpRate);
        } else {
            xGyroValue.setText(R.string.GyroscopeNotSupported);
        }
        mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagno != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mMagno, magSimpRate);
        } else {
            xMagnoValue.setText(R.string.MagnetometerNotSupported);
        }
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        thread.interrupt();
        super.onDestroy();
    }
}

