package com.app.utilities.utilitiesActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.utilities.R;
import com.app.utilities.utility.Preferences;
import com.app.utilities.utility.Utils;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ARMeasureActivity extends AppCompatActivity {

    private static final double MIN_OPENGL_VERSION = 3.0;
    private final DecimalFormat form_numbers = new DecimalFormat("#0.00 m");
    private final ArrayList<String> arl_saved = new ArrayList<>();
    private final Utils utils = new Utils();
    protected Configuration mPrevConfig;
    List<AnchorNode> anchorNodes = new ArrayList<>();
    Preferences pref;
    private float upDistance = 0f;
    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private AnchorNode myanchornode;
    private Anchor anchor1 = null, anchor2 = null;
    private HitResult myhit;
    private TextView text;
    private SeekBar sk_height_control;
    private boolean measure_height = false;
    private float fl_measurement = 0.0f;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferences());
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

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_armeasure);


        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        text = findViewById(R.id.text);

        sk_height_control = findViewById(R.id.sk_height_control);
        Button btn_height = findViewById(R.id.btn_height);
        Button btn_save = findViewById(R.id.btn_save);
        Button btn_width = findViewById(R.id.btn_width);
        ImageButton btn_share = findViewById(R.id.btn_share);

        sk_height_control.setEnabled(false);

        btn_width.setOnClickListener(view -> {
            resetLayout();
            measure_height = false;
            text.setText("Clicca sugli estremi che vuoi misurare");
        });

        btn_height.setOnClickListener(view -> {
            resetLayout();
            measure_height = true;
            text.setText("Fare clic sulla base dell'oggetto che si vuole misurare");
        });

        btn_save.setOnClickListener(view -> {
            if (fl_measurement != 0.0f)
                saveDialog();
            else
                Toast.makeText(ARMeasureActivity.this, "Effettuare una misurazione prima di salvare", Toast.LENGTH_SHORT).show();
        });

        btn_share.setOnClickListener(view -> {
            if (arl_saved.size() > 0) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                StringBuilder shareBody = new StringBuilder();
                for (String measurement : arl_saved)
                    shareBody.append(measurement).append("\n");
                shareBody = new StringBuilder(shareBody.toString().trim());
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "AR Measurements");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody.toString());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            } else
                Toast.makeText(ARMeasureActivity.this, "Salvare le misure prima di condividere", Toast.LENGTH_SHORT).show();
        });


        sk_height_control.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                upDistance = progress;
                fl_measurement = progress / 100f;
                text.setText("Altezza: " + form_numbers.format(fl_measurement));
                myanchornode.setLocalScale(new Vector3(1f, progress / 10f, 1f));
                //ascend(myanchornode, upDistance);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
            ModelRenderable.builder()
                    .setSource(this, R.raw.cubito3)
                    .build()
                    .thenAccept(renderable -> andyRenderable = renderable)
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Impossibile renderizzare", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });
        }

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }
                    myhit = hitResult;

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();

                    AnchorNode anchorNode = new AnchorNode(anchor);


                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    if (!measure_height) {
                        if (anchor2 != null) {
                            emptyAnchors();
                        }
                        if (anchor1 == null) {
                            anchor1 = anchor;
                        } else {
                            anchor2 = anchor;
                            fl_measurement = getMetersBetweenAnchors(anchor1, anchor2);
                            text.setText("Larghezza: " + form_numbers.format(fl_measurement));

                        }
                    } else {
                        emptyAnchors();
                        anchor1 = anchor;
                        text.setText("Muovi la barra sopra i pulsanti finché il cubo raggiunge la base superiore");
                        sk_height_control.setEnabled(true);
                    }

                    myanchornode = anchorNode;
                    anchorNodes.add(anchorNode);

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();
                    andy.getScaleController().setEnabled(false);
                });
        ImageButton question = findViewById(R.id.question);
        question.setOnClickListener(view -> {
            Toast toast = Toast.makeText(this, "1) Ispeziona l'area con il cellulare finchè la manina non scompare;\n\n2) Scegli quale misura adoperare;\n\n3) Piazza i cubi sui puntini bianchi che appaiono dopo aver selezionato la misura;\n\n4) Salva tutte le misure che desideri cliccando sul pulsante \"SALVA PER CONDIVIDERE\";\n\n5) Clicca sull'icona in alto a destra per usare le tue misure.\n\nP.S. Nel caso dell'altezza non sarà necessario il secondo cubo per delimitare l'estremità.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());
        mPrevConfig = new Configuration(getResources().getConfiguration());
    }

    /**
     * Function to raise an object perpendicular to the ArPlane a specific distance
     *
     * @param an anchor belonging to the object that should be raised
     * @param up distance in centimeters the object should be raised vertically
     */
    private void ascend(AnchorNode an, float up) {
        Anchor anchor = myhit.getTrackable().createAnchor(
                myhit.getHitPose().compose(Pose.makeTranslation(0, up / 100f, 0)));

        an.setAnchor(anchor);
    }

    /**
     * Function to return the distance in meters between two objects placed in ArPlane
     *
     * @param anchor1 first object's anchor
     * @param anchor2 second object's anchor
     * @return the distance between the two anchors in meters
     */
    private float getMetersBetweenAnchors(Anchor anchor1, Anchor anchor2) {
        float[] distance_vector = anchor1.getPose().inverse()
                .compose(anchor2.getPose()).getTranslation();
        float totalDistanceSquared = 0;
        for (int i = 0; i < 3; ++i)
            totalDistanceSquared += distance_vector[i] * distance_vector[i];
        return (float) Math.sqrt(totalDistanceSquared);
    }

    /**
     * Check whether the device supports the tools required to use the measurement tools
     *
     * @param activity
     * @return boolean determining whether the device is supported or not
     */
    private boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Toast.makeText(activity, "Sceneform richiede Android N o successivo", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Toast.makeText(activity, "Sceneform richiede OpenGL ES 3.0 o successivo", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    private void saveDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ARMeasureActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_save, null);

        EditText et_measure = mView.findViewById(R.id.et_measure);
        mBuilder.setTitle("Titolo della misura");

        mBuilder.setPositiveButton("Ok", (dialogInterface, i) -> {
            if (et_measure.length() != 0) {
                arl_saved.add(et_measure.getText() + ": " + form_numbers.format(fl_measurement));
                dialogInterface.dismiss();
            } else
                Toast.makeText(ARMeasureActivity.this, "Il titolo non può essere vuoto", Toast.LENGTH_SHORT).show();
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

    /**
     * Set layout to its initial state
     */
    private void resetLayout() {
        sk_height_control.setProgress(10);
        sk_height_control.setEnabled(false);
        measure_height = false;
        emptyAnchors();
    }

    private void emptyAnchors() {
        anchor1 = null;
        anchor2 = null;
        for (AnchorNode n : anchorNodes) {
            arFragment.getArSceneView().getScene().removeChild(n);
            Objects.requireNonNull(n.getAnchor()).detach();
            n.setParent(null);
        }
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
