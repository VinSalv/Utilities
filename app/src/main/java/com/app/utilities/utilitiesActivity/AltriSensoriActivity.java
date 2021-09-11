package com.app.utilities.utilitiesActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.utilities.R;
import com.app.utilities.settings.Preferenze;
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

public class AltriSensoriActivity extends AppCompatActivity implements SensorEventListener {
    private final Utils utils = new Utils();
    @SuppressWarnings("unused")
    protected Configuration mPrevConfig;
    TextView light, pressure, temperature, humidity;
    Preferenze pref;
    Spinner speedLig, speedPre, speedTem, speedHum;
    SensorManager sensorManager;
    Sensor mLight, mPressure, mTemperature, mHumidity;
    LinearLayout otherSensorsLayout;
    int ligSimpRate, preSimpRate, temSimpRate, humSimpRate;
    ArrayList<String> arrayListLight, arrayListPressure, arrayListTemperature, arrayListHumidty;
    ImageButton recOtherSensorsButton, pauseOtherSensorsButton, stopOtherSensorsButton;
    CheckBox allOtherSensorsCheckBox, ligCheckBox, presCheckBox, tempCheckBox, humCheckBox;
    Boolean bLig, bPres, bTemp, bHum, bRecOrPause;
    String fileName;
    ImageButton directory;
    private Thread thread;
    private boolean plotData = true;
    private LineChart chartLight, chartPressure, chartTemperature, chartHumidity;

    public static boolean isOnDarkMode(@NonNull Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

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
        setContentView(R.layout.activity_altri_sensori);

        directory = findViewById(R.id.direcotry);
        directory.setOnClickListener(v -> utils.openFolderDownload(this));

        arrayListLight = new ArrayList<>();
        arrayListPressure = new ArrayList<>();
        arrayListTemperature = new ArrayList<>();
        arrayListHumidty = new ArrayList<>();

        bLig = false;
        bPres = false;
        bTemp = false;
        bHum = false;
        bRecOrPause = false;

        recOtherSensorsButton = findViewById(R.id.recOtherSensorsButton);
        pauseOtherSensorsButton = findViewById(R.id.pauseOtherSensorsButton);
        stopOtherSensorsButton = findViewById(R.id.stopOtherSensorsButton);

        allOtherSensorsCheckBox = findViewById(R.id.allOtherSensorsCheckBox);
        ligCheckBox = findViewById(R.id.ligCheckBox);
        presCheckBox = findViewById(R.id.presCheckBox);
        tempCheckBox = findViewById(R.id.tempCheckBox);
        humCheckBox = findViewById(R.id.humCheckBox);

        pauseOtherSensorsButton.setEnabled(false);
        stopOtherSensorsButton.setEnabled(false);

        speedLig = findViewById(R.id.speedLig);
        speedPre = findViewById(R.id.speedPre);
        speedTem = findViewById(R.id.speedTem);
        speedHum = findViewById(R.id.speedHum);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.speed));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speedLig.setAdapter(arrayAdapter);
        speedPre.setAdapter(arrayAdapter);
        speedTem.setAdapter(arrayAdapter);
        speedHum.setAdapter(arrayAdapter);

        otherSensorsLayout = findViewById(R.id.otherSensorsLayout);

        light = findViewById(R.id.light);
        pressure = findViewById(R.id.pressure);
        temperature = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        chartLight = findViewById(R.id.chartLight);
        chartPressure = findViewById(R.id.chartPressure);
        chartTemperature = findViewById(R.id.chartTemperature);
        chartHumidity = findViewById(R.id.chartHumidity);

        ligSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
        preSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
        temSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
        humSimpRate = SensorManager.SENSOR_DELAY_NORMAL;

        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            sensorManager.registerListener(this, mLight, ligSimpRate);
            config(chartLight, "luminosità");
        } else {
            light.setText(R.string.BrightnessNotSupported);
            otherSensorsLayout.removeView(chartLight);
            otherSensorsLayout.removeView(speedLig);
            ligCheckBox.setEnabled(false);
        }
        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mPressure != null) {
            sensorManager.registerListener(this, mPressure, preSimpRate);
            config(chartPressure, "peressione");
        } else {
            pressure.setText(R.string.PressureNotSupported);
            otherSensorsLayout.removeView(chartPressure);
            otherSensorsLayout.removeView(speedPre);
            presCheckBox.setEnabled(false);
        }
        mTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (mTemperature != null) {
            sensorManager.registerListener(this, mTemperature, temSimpRate);
            config(chartTemperature, "temperatura");
        } else {
            temperature.setText(R.string.TemperatureNotSupported);
            otherSensorsLayout.removeView(chartTemperature);
            otherSensorsLayout.removeView(speedTem);
            tempCheckBox.setEnabled(false);
        }
        mHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (mHumidity != null) {
            sensorManager.registerListener(this, mHumidity, humSimpRate);
            config(chartHumidity, "umidità");
        } else {
            humidity.setText(R.string.HumidityNotSupported);
            otherSensorsLayout.removeView(chartHumidity);
            otherSensorsLayout.removeView(speedHum);
            humCheckBox.setEnabled(false);
        }
        allOtherSensorsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        if (mLight != null)
                            ligCheckBox.setChecked(true);
                        if (mPressure != null)
                            presCheckBox.setChecked(true);
                        if (mTemperature != null)
                            tempCheckBox.setChecked(true);
                        if (mHumidity != null)
                            humCheckBox.setChecked(true);
                    }
                    if ((ligCheckBox.isChecked() ^ (mLight == null)) && (presCheckBox.isChecked() ^ (mPressure == null)) && (tempCheckBox.isChecked() ^ (mTemperature == null)) && (tempCheckBox.isChecked() ^ (mHumidity == null)))
                        allOtherSensorsCheckBox.setChecked(true);
                }
        );
        ligCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isChecked) {
                        allOtherSensorsCheckBox.setChecked(false);
                    } else {
                        bLig = true;
                    }
                    if ((ligCheckBox.isChecked() ^ (mLight == null)) && (presCheckBox.isChecked() ^ (mPressure == null)) && (tempCheckBox.isChecked() ^ (mTemperature == null)) && (tempCheckBox.isChecked() ^ (mHumidity == null)))
                        allOtherSensorsCheckBox.setChecked(true);
                }
        );
        presCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isChecked) {
                        allOtherSensorsCheckBox.setChecked(false);
                    } else {
                        bPres = true;
                    }
                    if ((ligCheckBox.isChecked() ^ (mLight == null)) && (presCheckBox.isChecked() ^ (mPressure == null)) && (tempCheckBox.isChecked() ^ (mTemperature == null)) && (tempCheckBox.isChecked() ^ (mHumidity == null)))
                        allOtherSensorsCheckBox.setChecked(true);
                }
        );
        tempCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isChecked) {
                        allOtherSensorsCheckBox.setChecked(false);
                    } else {
                        bTemp = true;
                    }
                    if ((ligCheckBox.isChecked() ^ (mLight == null)) && (presCheckBox.isChecked() ^ (mPressure == null)) && (tempCheckBox.isChecked() ^ (mTemperature == null)) && (tempCheckBox.isChecked() ^ (mHumidity == null)))
                        allOtherSensorsCheckBox.setChecked(true);
                }
        );
        humCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isChecked) {
                        allOtherSensorsCheckBox.setChecked(false);
                    } else {
                        bHum = true;
                    }
                    if (ligCheckBox.isChecked() && presCheckBox.isChecked() && tempCheckBox.isChecked() && tempCheckBox.isChecked())
                        allOtherSensorsCheckBox.setChecked(true);
                }
        );
        speedLig.setSelection(3);
        speedLig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sensorManager.unregisterListener(AltriSensoriActivity.this, mLight);
                switch (position) {
                    case 0:
                        ligSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        sensorManager.registerListener(AltriSensoriActivity.this, mLight, ligSimpRate);
                        break;
                    case 1:
                        ligSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        sensorManager.registerListener(AltriSensoriActivity.this, mLight, ligSimpRate);
                        break;
                    case 2:
                        ligSimpRate = SensorManager.SENSOR_DELAY_UI;
                        sensorManager.registerListener(AltriSensoriActivity.this, mLight, ligSimpRate);
                        break;
                    case 3:
                    default:
                        ligSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        sensorManager.registerListener(AltriSensoriActivity.this, mLight, ligSimpRate);
                        break;
                    case 4:
                        sensorManager.unregisterListener(AltriSensoriActivity.this, mLight);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        speedPre.setSelection(3);
        speedPre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sensorManager.unregisterListener(AltriSensoriActivity.this, mPressure);
                switch (position) {
                    case 0:
                        preSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        sensorManager.registerListener(AltriSensoriActivity.this, mPressure, preSimpRate);
                        break;
                    case 1:
                        preSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        sensorManager.registerListener(AltriSensoriActivity.this, mPressure, preSimpRate);
                        break;
                    case 2:
                        preSimpRate = SensorManager.SENSOR_DELAY_UI;
                        sensorManager.registerListener(AltriSensoriActivity.this, mPressure, preSimpRate);
                        break;
                    case 3:
                    default:
                        preSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        sensorManager.registerListener(AltriSensoriActivity.this, mPressure, preSimpRate);
                        break;
                    case 4:
                        sensorManager.unregisterListener(AltriSensoriActivity.this, mPressure);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        speedTem.setSelection(3);
        speedTem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sensorManager.unregisterListener(AltriSensoriActivity.this, mTemperature);
                switch (position) {
                    case 0:
                        temSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        sensorManager.registerListener(AltriSensoriActivity.this, mTemperature, temSimpRate);
                        break;
                    case 1:
                        temSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        sensorManager.registerListener(AltriSensoriActivity.this, mTemperature, temSimpRate);
                        break;
                    case 2:
                        temSimpRate = SensorManager.SENSOR_DELAY_UI;
                        sensorManager.registerListener(AltriSensoriActivity.this, mTemperature, temSimpRate);
                        break;
                    case 3:
                    default:
                        temSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        sensorManager.registerListener(AltriSensoriActivity.this, mTemperature, temSimpRate);
                        break;
                    case 4:
                        sensorManager.unregisterListener(AltriSensoriActivity.this, mTemperature);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        speedHum.setSelection(3);
        speedHum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sensorManager.unregisterListener(AltriSensoriActivity.this, mHumidity);
                switch (position) {
                    case 0:
                        humSimpRate = SensorManager.SENSOR_DELAY_FASTEST;
                        sensorManager.registerListener(AltriSensoriActivity.this, mHumidity, humSimpRate);
                        break;
                    case 1:
                        humSimpRate = SensorManager.SENSOR_DELAY_GAME;
                        sensorManager.registerListener(AltriSensoriActivity.this, mHumidity, humSimpRate);
                        break;
                    case 2:
                        humSimpRate = SensorManager.SENSOR_DELAY_UI;
                        sensorManager.registerListener(AltriSensoriActivity.this, mHumidity, humSimpRate);
                        break;
                    case 3:
                    default:
                        humSimpRate = SensorManager.SENSOR_DELAY_NORMAL;
                        sensorManager.registerListener(AltriSensoriActivity.this, mHumidity, humSimpRate);
                        break;
                    case 4:
                        sensorManager.unregisterListener(AltriSensoriActivity.this, mHumidity);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        feedMultiple();
        recOtherSensorsButton.setOnClickListener(v -> {
            if (ligCheckBox.isChecked() || presCheckBox.isChecked() || tempCheckBox.isChecked() || humCheckBox.isChecked()) {
                bRecOrPause = true;
                allOtherSensorsCheckBox.setEnabled(false);
                ligCheckBox.setEnabled(false);
                presCheckBox.setEnabled(false);
                tempCheckBox.setEnabled(false);
                humCheckBox.setEnabled(false);
                recOtherSensorsButton.setEnabled(false);
                pauseOtherSensorsButton.setEnabled(true);
                stopOtherSensorsButton.setEnabled(true);
                recOtherSensorsButton.setImageResource(R.drawable.ic_rec_press);
                pauseOtherSensorsButton.setImageResource(R.drawable.ic_pause);
                stopOtherSensorsButton.setImageResource(R.drawable.ic_stop);
                bLig = ligCheckBox.isChecked();
                bPres = presCheckBox.isChecked();
                bTemp = tempCheckBox.isChecked();
                bHum = humCheckBox.isChecked();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
                fileName = (sdf.format(Calendar.getInstance().getTime()) + ".csv");
            } else {
                utils.notifyUserShortWay(this, "Seleziona almeno un sensore!!");
            }
        });
        pauseOtherSensorsButton.setOnClickListener(v -> {
            bRecOrPause = true;
            allOtherSensorsCheckBox.setEnabled(false);
            ligCheckBox.setEnabled(false);
            presCheckBox.setEnabled(false);
            tempCheckBox.setEnabled(false);
            humCheckBox.setEnabled(false);
            recOtherSensorsButton.setEnabled(true);
            pauseOtherSensorsButton.setEnabled(false);
            stopOtherSensorsButton.setEnabled(true);
            recOtherSensorsButton.setImageResource(R.drawable.ic_rec);
            pauseOtherSensorsButton.setImageResource(R.drawable.ic_pause_press);
            stopOtherSensorsButton.setImageResource(R.drawable.ic_stop);
            bLig = false;
            bPres = false;
            bTemp = false;
            bHum = false;
        });
        stopOtherSensorsButton.setOnClickListener(v -> {
            bRecOrPause = false;
            allOtherSensorsCheckBox.setEnabled(true);
            ligCheckBox.setEnabled(true);
            presCheckBox.setEnabled(true);
            tempCheckBox.setEnabled(true);
            humCheckBox.setEnabled(true);
            recOtherSensorsButton.setEnabled(true);
            pauseOtherSensorsButton.setEnabled(false);
            stopOtherSensorsButton.setEnabled(false);
            recOtherSensorsButton.setImageResource(R.drawable.ic_rec);
            pauseOtherSensorsButton.setImageResource(R.drawable.ic_pause);
            stopOtherSensorsButton.setImageResource(R.drawable.ic_stop);
            bLig = ligCheckBox.isChecked();
            bPres = presCheckBox.isChecked();
            bTemp = tempCheckBox.isChecked();
            bHum = humCheckBox.isChecked();
            int count;
            if (ligCheckBox.isChecked()) {
                String csv = Environment.getExternalStorageDirectory() + "/Download/" + "SensoreLuminosità" + fileName;
                CSVWriter writer;
                try {
                    writer = new CSVWriter(new FileWriter(csv));
                    List<String[]> data = new ArrayList<>();
                    count = 1;
                    data.add(new String[]{"lx"});
                    while (count != arrayListLight.size() + 1) {
                        data.add(new String[]{'"' + arrayListLight.get(count - 1) + '"'});
                        count++;
                    }
                    Objects.requireNonNull(writer).writeAll(data);
                    writer.close();
                    utils.notifyUserShortWay(this, "File CSV relativo sensore della luminosità realizzato con successo");
                } catch (IOException e) {
                    e.printStackTrace();
                    utils.notifyUser(this, "Non è stato possibile realizzare il CSV relativo al sensore della luminosità" + e);
                }
            }
            if (presCheckBox.isChecked()) {
                String csv = Environment.getExternalStorageDirectory() + "/Download/" + "SensorePressione" + fileName;
                CSVWriter writer;
                try {
                    writer = new CSVWriter(new FileWriter(csv));
                    List<String[]> data = new ArrayList<>();
                    count = 1;
                    data.add(new String[]{"hPa(mbar)"});
                    while (count != arrayListPressure.size() + 1) {
                        data.add(new String[]{'"' + arrayListPressure.get(count - 1) + '"'});
                        count++;
                    }
                    Objects.requireNonNull(writer).writeAll(data);
                    writer.close();
                    utils.notifyUserShortWay(this, "File CSV relativo al sensore della pressione realizzato con successo");
                } catch (IOException e) {
                    e.printStackTrace();
                    utils.notifyUser(this, "Non è stato possibile realizzare il CSV relativo al sensore della pressione");
                }
            }
            if (tempCheckBox.isChecked()) {
                String csv = Environment.getExternalStorageDirectory() + "/Download/" + "SensoreTemperatura" + fileName;
                CSVWriter writer;
                try {
                    writer = new CSVWriter(new FileWriter(csv));
                    List<String[]> data = new ArrayList<>();
                    count = 1;
                    data.add(new String[]{"C°"});
                    while (count != arrayListTemperature.size() + 1) {
                        data.add(new String[]{'"' + arrayListTemperature.get(count - 1) + '"'});
                        count++;
                    }
                    Objects.requireNonNull(writer).writeAll(data);
                    writer.close();
                    utils.notifyUserShortWay(this, "File CSV relativo al sensore della temperatura realizzato con successo");
                } catch (IOException e) {
                    e.printStackTrace();
                    utils.notifyUser(this, "Non è stato possibile realizzare il CSV relativo al sensore della temperatura");
                }
            }
            if (humCheckBox.isChecked()) {
                String csv = Environment.getExternalStorageDirectory() + "/Download/" + "SensoreUmidità" + fileName;
                CSVWriter writer;
                try {
                    writer = new CSVWriter(new FileWriter(csv));
                    List<String[]> data = new ArrayList<>();
                    count = 1;
                    data.add(new String[]{"%"});
                    while (count != arrayListHumidty.size() + 1) {
                        data.add(new String[]{'"' + arrayListHumidty.get(count - 1) + '"'});
                        count++;
                    }
                    Objects.requireNonNull(writer).writeAll(data);
                    writer.close();
                    utils.notifyUserShortWay(this, "File CSV relativo al sensore di umidità realizzato con successo");
                } catch (IOException e) {
                    e.printStackTrace();
                    utils.notifyUser(this, "Non è stato possibile realizzare il CSV relativo al sensore di umidità");
                }
            }
            arrayListLight.clear();
            arrayListPressure.clear();
            arrayListTemperature.clear();
            arrayListHumidty.clear();
        });
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());
        mPrevConfig = new Configuration(getResources().getConfiguration());
    }

    @Override
    public void onBackPressed() {
        utils.goToMainActivity2(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            light.setText(Html.fromHtml("<b>Luminosità: </b>" + utils.roundAvoid(event.values[0], 2) + " [lx]"));
            if (plotData) {
                addEntry(event, "luminosità");
                plotData = false;
            }
        } else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            pressure.setText(Html.fromHtml("<b>Pressione: </b>" + utils.roundAvoid(event.values[0], 2) + " [hPa(mbar)]"));
            if (plotData) {
                addEntry(event, "pressione");
                plotData = false;
            }
        } else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            temperature.setText(Html.fromHtml("<b>Temperatura: </b>" + utils.roundAvoid(event.values[0], 2) + " [°C]"));
            if (plotData) {
                addEntry(event, "temperatura");
                plotData = false;
            }
        } else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            humidity.setText(Html.fromHtml("<b>Umidità: </b>" + utils.roundAvoid(event.values[0], 2) + " [%]"));
            if (plotData) {
                addEntry(event, "umidità");
                plotData = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @SuppressWarnings("CommentedOutCode")
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
        switch (sensor) {
            case "luminosità":
                leftAxis.setAxisMinimum(0f);
                break;
            /*case "pressione":
                leftAxis.setAxisMaximum(mPressure.getMaximumRange());
                leftAxis.setAxisMinimum(-mPressure.getMaximumRange());
                break;
            case "temperatura":
                leftAxis.setAxisMaximum(mTemperature.getMaximumRange());
                leftAxis.setAxisMinimum(-mTemperature.getMaximumRange());
                break;
             */
            case "umnidità":
                //noinspection DuplicateBranchesInSwitch
                leftAxis.setAxisMinimum(0f);
                break;
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
            case "luminosità":
                data = chartLight.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("Lum.");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0]), 0);
                    data.notifyDataChanged();
                    chartLight.notifyDataSetChanged();
                    chartLight.setVisibleXRangeMaximum(10);
                    chartLight.moveViewToX(data.getEntryCount());
                    if (bLig) arrayListLight.add(String.valueOf(event.values[0]));

                }
                break;
            case "pressione":
                data = chartPressure.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("Pres.");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0]), 0);
                    data.notifyDataChanged();
                    chartPressure.notifyDataSetChanged();
                    chartPressure.setVisibleXRangeMaximum(10);
                    chartPressure.moveViewToX(data.getEntryCount());
                    if (bPres) arrayListPressure.add(String.valueOf(event.values[0]));

                }
                break;
            case "temperatura":
                data = chartTemperature.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("Temp.");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0] + 10), 0);
                    data.notifyDataChanged();
                    chartTemperature.notifyDataSetChanged();
                    chartTemperature.setVisibleXRangeMaximum(10);
                    chartTemperature.moveViewToX(data.getEntryCount());
                    if (bTemp) arrayListTemperature.add(String.valueOf(event.values[0]));
                }
                break;
            case "umidità":
                data = chartHumidity.getData();
                if (data != null) {
                    ILineDataSet set = data.getDataSetByIndex(0);
                    if (set == null) {
                        set = createSet("Umid.");
                        data.addDataSet(set);
                    }
                    data.addEntry(new Entry(set.getEntryCount(), event.values[0]), 0);
                    data.notifyDataChanged();
                    chartHumidity.notifyDataSetChanged();
                    chartHumidity.setVisibleXRangeMaximum(10);
                    chartHumidity.moveViewToX(data.getEntryCount());
                    if (bHum) arrayListHumidty.add(String.valueOf(event.values[0]));
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
        if (bRecOrPause)
            stopOtherSensorsButton.performClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            sensorManager.registerListener(this, mLight, ligSimpRate);
        } else {
            light.setText(R.string.BrightnessNotSupported);
        }
        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mPressure != null) {
            sensorManager.registerListener(this, mPressure, preSimpRate);
        } else {
            pressure.setText(R.string.PressureNotSupported);
        }
        mTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (mTemperature != null) {
            sensorManager.registerListener(this, mTemperature, temSimpRate);
        } else {
            temperature.setText(R.string.TemperatureNotSupported);
        }
        mHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (mHumidity != null) {
            sensorManager.registerListener(this, mHumidity, humSimpRate);
        } else {
            humidity.setText(R.string.HumidityNotSupported);
        }
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        if (bRecOrPause)
            stopOtherSensorsButton.performClick();
        thread.interrupt();
        super.onDestroy();
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

}