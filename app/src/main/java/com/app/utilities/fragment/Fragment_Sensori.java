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
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Fragment_Sensori extends Fragment implements SensorEventListener {
    private final Utils utils = new Utils();
    TextView xAccelValue, yAccelValue, zAccelValue, xGyroValue, yGyroValue, zGyroValue, xMagneValue, yMagneValue, zMagneValue;
    Spinner speedAcc, speedGyr, speedMag;
    SensorManager sensorManager;
    Sensor mAccel, mGyro, mMagne;
    LinearLayout sensorsLayout;
    int accSimpRate, gyrSimpRate, magSimpRate;
    Button otherSensorsButton;
    ImageButton recSensorSButton, pauseSensorsButton, stopSensorsButton;
    CheckBox allSensorsCheckBox, accelCheckBox, gyroCheckBox, magneCheckBox;
    ArrayList<String> arrayListAccelX, arrayListAccelY, arrayListAccelZ, arrayListGyroX, arrayListGyroY, arrayListGyroZ, arrayListMagneX, arrayListMagneY, arrayListMagneZ;
    Boolean bAccel, bGyro, bMagne, bRecOrPause;
    String fileName;
    ImageButton directory;
    private Thread thread;
    private boolean plotData = true;
    private LineChart chartAccelX, chartAccelY, chartAccelZ, chartGyroX, chartGyroY, chartGyroZ, chartMagnetX, chartMagnetY, chartMagnetZ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__sensori, container, false);

        directory = view.findViewById(R.id.direcotry);
        directory.setOnClickListener(v -> utils.openFolderDownload(requireActivity()));

        arrayListAccelX = new ArrayList<>();
        arrayListAccelY = new ArrayList<>();
        arrayListAccelZ = new ArrayList<>();
        arrayListGyroX = new ArrayList<>();
        arrayListGyroY = new ArrayList<>();
        arrayListGyroZ = new ArrayList<>();
        arrayListMagneX = new ArrayList<>();
        arrayListMagneY = new ArrayList<>();
        arrayListMagneZ = new ArrayList<>();

        bAccel = false;
        bGyro = false;
        bMagne = false;
        bRecOrPause = false;

        recSensorSButton = view.findViewById(R.id.recSensorsButton);
        pauseSensorsButton = view.findViewById(R.id.pauseSensorsButton);
        stopSensorsButton = view.findViewById(R.id.stopSensorsButton);

        allSensorsCheckBox = view.findViewById(R.id.allSensorsCheckBox);
        accelCheckBox = view.findViewById(R.id.accelCheckBox);
        gyroCheckBox = view.findViewById(R.id.gyroCheckBox);
        magneCheckBox = view.findViewById(R.id.magneCheckBox);

        pauseSensorsButton.setEnabled(false);
        stopSensorsButton.setEnabled(false);

        speedAcc = view.findViewById(R.id.speedAcc);
        speedGyr = view.findViewById(R.id.speedGir);
        speedMag = view.findViewById(R.id.speedMag);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.speed));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speedAcc.setAdapter(arrayAdapter);
        speedGyr.setAdapter(arrayAdapter);
        speedMag.setAdapter(arrayAdapter);

        sensorsLayout = view.findViewById(R.id.sensorsLayout);

        xAccelValue = view.findViewById(R.id.xValue);
        yAccelValue = view.findViewById(R.id.yValue);
        zAccelValue = view.findViewById(R.id.zValue);
        xGyroValue = view.findViewById(R.id.xGyroValue);
        yGyroValue = view.findViewById(R.id.yGyroValue);
        zGyroValue = view.findViewById(R.id.zGyroValue);
        xMagneValue = view.findViewById(R.id.xMagnoValue);
        yMagneValue = view.findViewById(R.id.yMagnoValue);
        zMagneValue = view.findViewById(R.id.zMagnoValue);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        chartAccelX = view.findViewById(R.id.chartAceleX);
        chartAccelY = view.findViewById(R.id.chartAceleY);
        chartAccelZ = view.findViewById(R.id.chartAceleZ);

        chartGyroX = view.findViewById(R.id.chartGirX);
        chartGyroY = view.findViewById(R.id.chartGirY);
        chartGyroZ = view.findViewById(R.id.chartGirZ);

        chartMagnetX = view.findViewById(R.id.chartMagnetX);
        chartMagnetY = view.findViewById(R.id.chartMagnetY);
        chartMagnetZ = view.findViewById(R.id.chartMagnetZ);

        accSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
        gyrSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
        magSimpRate = SensorManager.SENSOR_DELAY_NORMAL;

        if (mAccel != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mAccel, accSimpRate);
            config(chartAccelX, "accelerometro");
            config(chartAccelY, "accelerometro");
            config(chartAccelZ, "accelerometro");

        } else {
            xAccelValue.setText(R.string.AccelerometerNotSupported);
            sensorsLayout.removeView(chartAccelX);
            sensorsLayout.removeView(chartAccelY);
            sensorsLayout.removeView(chartAccelZ);
            sensorsLayout.removeView(speedAcc);
            accelCheckBox.setEnabled(false);
        }

        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyro != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mGyro, gyrSimpRate);
            config(chartGyroX, "giroscopio");
            config(chartGyroY, "giroscopio");
            config(chartGyroZ, "giroscopio");
        } else {
            xGyroValue.setText(R.string.GyroscopeNotSupported);
            sensorsLayout.removeView(chartGyroX);
            sensorsLayout.removeView(chartGyroY);
            sensorsLayout.removeView(chartGyroZ);
            sensorsLayout.removeView(speedGyr);
            gyroCheckBox.setEnabled(false);
        }

        mMagne = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagne != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mMagne, magSimpRate);
            config(chartMagnetX, "magnetometro");
            config(chartMagnetY, "magnetometro");
            config(chartMagnetZ, "magnetometro");
        } else {
            xMagneValue.setText(R.string.MagnetometerNotSupported);
            sensorsLayout.removeView(chartMagnetX);
            sensorsLayout.removeView(chartMagnetY);
            sensorsLayout.removeView(chartMagnetZ);
            sensorsLayout.removeView(speedMag);
            magneCheckBox.setEnabled(false);
        }
        allSensorsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        if (mAccel != null)
                            accelCheckBox.setChecked(true);
                        if (mGyro != null)
                            gyroCheckBox.setChecked(true);
                        if (mMagne != null)
                            magneCheckBox.setChecked(true);
                    }
                    if ((accelCheckBox.isChecked() ^ (mAccel == null)) && (gyroCheckBox.isChecked() ^ (mGyro == null)) && (magneCheckBox.isChecked() ^ (mMagne == null)))
                        allSensorsCheckBox.setChecked(true);
                }
        );
        accelCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isChecked) {
                        allSensorsCheckBox.setChecked(false);
                    } else {
                        bAccel = true;
                    }
                    if ((accelCheckBox.isChecked() ^ (mAccel == null)) && (gyroCheckBox.isChecked() ^ (mGyro == null)) && (magneCheckBox.isChecked() ^ (mMagne == null)))
                        allSensorsCheckBox.setChecked(true);
                }
        );
        gyroCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isChecked) {
                        allSensorsCheckBox.setChecked(false);
                    } else {
                        bGyro = true;
                    }
                    if ((accelCheckBox.isChecked() ^ (mAccel == null)) && (gyroCheckBox.isChecked() ^ (mGyro == null)) && (magneCheckBox.isChecked() ^ (mMagne == null)))
                        allSensorsCheckBox.setChecked(true);

                }
        );
        magneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isChecked) {
                        allSensorsCheckBox.setChecked(false);
                    } else {
                        bMagne = true;
                    }
                    if ((accelCheckBox.isChecked() ^ (mAccel == null)) && (gyroCheckBox.isChecked() ^ (mGyro == null)) && (magneCheckBox.isChecked() ^ (mMagne == null)))
                        allSensorsCheckBox.setChecked(true);

                }
        );

        speedAcc.setSelection(3);
        speedAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
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
        speedGyr.setSelection(3);
        speedGyr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        gyrSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    case 1:
                        gyrSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case 2:
                        gyrSimpRate = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case 3:
                    default:
                        gyrSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                    case 4:
                        gyrSimpRate = 400000;
                        break;
                    case 5:
                        gyrSimpRate = 800000;
                        break;
                    case 6:
                        gyrSimpRate = 1000000;
                        break;
                }
                if (mGyro != null) {
                    sensorManager.unregisterListener(Fragment_Sensori.this, mGyro);
                    sensorManager.registerListener(Fragment_Sensori.this, mGyro, gyrSimpRate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        speedMag.setSelection(3);
        speedMag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
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
                if (mMagne != null) {
                    sensorManager.unregisterListener(Fragment_Sensori.this, mMagne);
                    sensorManager.registerListener(Fragment_Sensori.this, mMagne, magSimpRate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        feedMultiple();
        recSensorSButton.setOnClickListener(v -> {
            if (accelCheckBox.isChecked() || gyroCheckBox.isChecked() || magneCheckBox.isChecked()) {
                bRecOrPause = true;
                allSensorsCheckBox.setEnabled(false);
                accelCheckBox.setEnabled(false);
                gyroCheckBox.setEnabled(false);
                magneCheckBox.setEnabled(false);
                recSensorSButton.setEnabled(false);
                pauseSensorsButton.setEnabled(true);
                stopSensorsButton.setEnabled(true);
                recSensorSButton.setImageResource(R.drawable.ic_rec_press);
                pauseSensorsButton.setImageResource(R.drawable.ic_pause);
                stopSensorsButton.setImageResource(R.drawable.ic_stop);
                bAccel = accelCheckBox.isChecked();
                bGyro = gyroCheckBox.isChecked();
                bMagne = magneCheckBox.isChecked();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
                fileName = (sdf.format(Calendar.getInstance().getTime()) + ".csv");
            } else {
                utils.notifyUserShortWay(requireActivity(), "Seleziona almeno un sensore!!");
            }
        });
        pauseSensorsButton.setOnClickListener(v -> {
            bRecOrPause = true;
            allSensorsCheckBox.setEnabled(false);
            accelCheckBox.setEnabled(false);
            gyroCheckBox.setEnabled(false);
            magneCheckBox.setEnabled(false);
            recSensorSButton.setEnabled(true);
            pauseSensorsButton.setEnabled(false);
            stopSensorsButton.setEnabled(true);
            recSensorSButton.setImageResource(R.drawable.ic_rec);
            pauseSensorsButton.setImageResource(R.drawable.ic_pause_press);
            stopSensorsButton.setImageResource(R.drawable.ic_stop);
            bAccel = false;
            bGyro = false;
            bMagne = false;
        });
        stopSensorsButton.setOnClickListener(v -> {
            bRecOrPause = false;
            allSensorsCheckBox.setEnabled(true);
            accelCheckBox.setEnabled(true);
            gyroCheckBox.setEnabled(true);
            magneCheckBox.setEnabled(true);
            recSensorSButton.setEnabled(true);
            pauseSensorsButton.setEnabled(false);
            stopSensorsButton.setEnabled(false);
            recSensorSButton.setImageResource(R.drawable.ic_rec);
            pauseSensorsButton.setImageResource(R.drawable.ic_pause);
            stopSensorsButton.setImageResource(R.drawable.ic_stop);
            bAccel = accelCheckBox.isChecked();
            bGyro = gyroCheckBox.isChecked();
            bMagne = magneCheckBox.isChecked();
            int count;
            if (accelCheckBox.isChecked()) {
                String csv = Environment.getExternalStorageDirectory() + "/Download/" + "Accelerometro" + fileName;
                CSVWriter writer;
                try {
                    writer = new CSVWriter(new FileWriter(csv));
                    List<String[]> data = new ArrayList<>();
                    count = 1;
                    data.add(new String[]{"x", "y", "z"});
                    while (count != arrayListAccelX.size() + 1) {
                        data.add(new String[]{'"' + arrayListAccelX.get(count - 1) + '"', '"' + arrayListAccelY.get(count - 1) + '"', '"' + arrayListAccelZ.get(count - 1) + '"'});
                        count++;
                    }
                    Objects.requireNonNull(writer).writeAll(data);
                    writer.close();
                    utils.notifyUserShortWay(requireActivity(), "File CSV relativo all'accelerometro realizzato con successo");
                } catch (IOException e) {
                    e.printStackTrace();
                    utils.notifyUser(requireActivity(), "Non è stato possibile realizzare il CSV relativo all'accelerometro");
                }
            }
            if (gyroCheckBox.isChecked()) {
                String csv = Environment.getExternalStorageDirectory() + "/Download/" + "Giroscopio" + fileName;
                CSVWriter writer;
                try {
                    writer = new CSVWriter(new FileWriter(csv));
                    List<String[]> data = new ArrayList<>();
                    count = 1;
                    data.add(new String[]{"x", "y", "z"});
                    while (count != arrayListGyroX.size() + 1) {
                        data.add(new String[]{'"' + arrayListGyroX.get(count - 1) + '"', '"' + arrayListGyroY.get(count - 1) + '"', '"' + arrayListGyroZ.get(count - 1) + '"'});
                        count++;
                    }
                    Objects.requireNonNull(writer).writeAll(data);
                    writer.close();
                    utils.notifyUserShortWay(requireActivity(), "File CSV relativo al giroscopio realizzato con successo");
                } catch (IOException e) {
                    e.printStackTrace();
                    utils.notifyUser(requireActivity(), "Non è stato possibile realizzare il CSV relativo al giroscopio");
                }
            }
            if (magneCheckBox.isChecked()) {
                String csv = Environment.getExternalStorageDirectory() + "/Download/" + "Magnetometro" + fileName;
                CSVWriter writer;
                try {
                    writer = new CSVWriter(new FileWriter(csv));
                    List<String[]> data = new ArrayList<>();
                    count = 1;
                    data.add(new String[]{"x", "y", "z"});
                    while (count != arrayListMagneX.size() + 1) {
                        data.add(new String[]{'"' + arrayListMagneX.get(count - 1) + '"', '"' + arrayListMagneY.get(count - 1) + '"', '"' + arrayListMagneZ.get(count - 1) + '"'});
                        count++;
                    }
                    Objects.requireNonNull(writer).writeAll(data);
                    writer.close();
                    utils.notifyUserShortWay(requireActivity(), "File CSV relativo al magnetometro realizzato con successo");
                } catch (IOException e) {
                    e.printStackTrace();
                    utils.notifyUser(requireActivity(), "Non è stato possibile realizzare il CSV relativo al magnetometro");
                }
            }
            arrayListAccelX.clear();
            arrayListAccelY.clear();
            arrayListAccelZ.clear();
            arrayListGyroX.clear();
            arrayListGyroY.clear();
            arrayListGyroZ.clear();
            arrayListMagneX.clear();
            arrayListMagneY.clear();
            arrayListMagneZ.clear();
        });
        otherSensorsButton = view.findViewById(R.id.otherSensorsButton);
        otherSensorsButton.setOnClickListener(v -> utils.goToAltriSensoriActivity(requireActivity()));
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccelValue.setText(Html.fromHtml("<b>X: </b>" + utils.roundAvoid(event.values[0], 2)));
            yAccelValue.setText(Html.fromHtml("<b>Y: </b>" + utils.roundAvoid(event.values[1], 2)));
            zAccelValue.setText(Html.fromHtml("<b>Z: </b>" + utils.roundAvoid(event.values[2], 2)));
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
            xMagneValue.setText(Html.fromHtml("<b>X: </b>" + utils.roundAvoid(event.values[0], 2)));
            yMagneValue.setText(Html.fromHtml("<b>Y: </b>" + utils.roundAvoid(event.values[1], 2)));
            zMagneValue.setText(Html.fromHtml("<b>Z: </b>" + utils.roundAvoid(event.values[2], 2)));
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
                data = chartAccelX.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("accel. x");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0]), 0);
                    data.notifyDataChanged();
                    chartAccelX.notifyDataSetChanged();
                    chartAccelX.setVisibleXRangeMaximum(10);
                    chartAccelX.moveViewToX(data.getEntryCount());
                    if (bAccel) arrayListAccelX.add(String.valueOf(event.values[0]));

                }
                data = chartAccelY.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("accel. y");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[1]), 0);
                    data.notifyDataChanged();
                    chartAccelY.notifyDataSetChanged();
                    chartAccelY.setVisibleXRangeMaximum(10);
                    chartAccelY.moveViewToX(data.getEntryCount());
                    if (bAccel) arrayListAccelY.add(String.valueOf(event.values[1]));
                }
                data = chartAccelZ.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("accel. z");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[2]), 0);
                    data.notifyDataChanged();
                    chartAccelZ.notifyDataSetChanged();
                    chartAccelZ.setVisibleXRangeMaximum(10);
                    chartAccelZ.moveViewToX(data.getEntryCount());
                    if (bAccel) arrayListAccelZ.add(String.valueOf(event.values[2]));
                }
                break;
            case "giroscopio":
                data = chartGyroX.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("giro. x");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0]), 0);
                    data.notifyDataChanged();
                    chartGyroX.notifyDataSetChanged();
                    chartGyroX.setVisibleXRangeMaximum(10);
                    chartGyroX.moveViewToX(data.getEntryCount());
                    if (bGyro) arrayListGyroX.add(String.valueOf(event.values[0]));
                }
                data = chartGyroY.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("giro. y");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[1]), 0);
                    data.notifyDataChanged();
                    chartGyroY.notifyDataSetChanged();
                    chartGyroY.setVisibleXRangeMaximum(10);
                    chartGyroY.moveViewToX(data.getEntryCount());
                    if (bGyro) arrayListGyroY.add(String.valueOf(event.values[1]));
                }
                data = chartGyroZ.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("giro. z");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[2]), 0);
                    data.notifyDataChanged();
                    chartGyroZ.notifyDataSetChanged();
                    chartGyroZ.setVisibleXRangeMaximum(10);
                    chartGyroZ.moveViewToX(data.getEntryCount());
                    if (bGyro) arrayListGyroZ.add(String.valueOf(event.values[2]));
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
                    if (bMagne) arrayListMagneX.add(String.valueOf(event.values[0]));
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
                    if (bMagne) arrayListMagneY.add(String.valueOf(event.values[1]));
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
                    if (bMagne) arrayListMagneZ.add(String.valueOf(event.values[2]));

                    break;
                }

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
        if (bRecOrPause)
            stopSensorsButton.performClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAccel != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mAccel, accSimpRate);
        } else {
            xAccelValue.setText(R.string.AccelerometerNotSupported);
        }
        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyro != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mGyro, gyrSimpRate);
        } else {
            xGyroValue.setText(R.string.GyroscopeNotSupported);
        }
        mMagne = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagne != null) {
            sensorManager.registerListener(Fragment_Sensori.this, mMagne, magSimpRate);
        } else {
            xMagneValue.setText(R.string.MagnetometerNotSupported);
        }
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        if (bRecOrPause)
            stopSensorsButton.performClick();
        thread.interrupt();
        super.onDestroy();
    }
}

