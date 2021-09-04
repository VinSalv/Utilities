package com.app.utilities.utilitiesActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Html;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.utilities.R;
import com.app.utilities.utility.Preferences;
import com.app.utilities.utility.PreferencesSCF;
import com.app.utilities.utility.Shake;
import com.app.utilities.utility.Utils;

import java.util.Random;

public class SCFActivity extends AppCompatActivity implements Shake.Callback {
    final int[] redArray = {
            R.drawable.paper_red,
            R.drawable.rock_red,
            R.drawable.scissors_red
    };
    final int[] blueArray = {
            R.drawable.paper_blue,
            R.drawable.rock_blue,
            R.drawable.scissors_blue
    };
    final TypedValue typedValue = new TypedValue();
    private final Utils utils = new Utils();
    protected Configuration mPrevConfig;
    Button goButton;
    LinearLayout scfLayout;
    TextView redPlayerEditText;
    TextView bluePlayerEditText;
    TextView scfWinner;
    int height;
    int width;
    @SuppressWarnings("unused")
    int colorAccent;
    Preferences pref;
    PreferencesSCF prefSCF;
    Vibrator vibrator;
    Boolean b = true;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferences());
        prefSCF = utils.loadDataSCF(this, new PreferencesSCF());
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
        setContentView(R.layout.activity_scf);
        Toolbar myToolbarSCF = findViewById(R.id.toolbarSCF);
        setSupportActionBar(myToolbarSCF);
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(android.R.attr.colorAccent, typedValue, true);
        @SuppressLint("Recycle") TypedArray arr = obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.colorAccent});
        colorAccent = arr.getColor(0, -1);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        @SuppressWarnings("unused") Shake shake = new Shake(this, 2.5d, 0, this);
        goButton = findViewById(R.id.go);
        scfLayout = findViewById(R.id.scfLayout);
        ViewTreeObserver observer = scfLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                init();
                scfLayout.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });
        scfWinner = findViewById(R.id.scfWinner);
        redPlayerEditText = findViewById(R.id.testaPlayerEditText);
        bluePlayerEditText = findViewById(R.id.crocePlayerEditText);
        goButton.setOnClickListener(this::goButton);
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());
        mPrevConfig = new Configuration(getResources().getConfiguration());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        pref = utils.loadData(this, new Preferences());
        getTheme().resolveAttribute(R.attr.colorSecondaryVariant, typedValue, true);
        int colorSecondaryVariant = typedValue.resourceId;
        if (!pref.getPredBool()) {
            switch (pref.getThemeText()) {
                case "LightThemeSelected":
                case "LightTheme":
                    view.setBackgroundColor(this.getColor(colorSecondaryVariant));
                    break;
                case "DarkThemeSelected":
                case "DarkTheme":
                    view.setBackgroundColor(this.getColor(R.color.dark_gray));
                    break;
            }
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                case Configuration.UI_MODE_NIGHT_NO:
                    view.setBackgroundColor(this.getColor(colorSecondaryVariant));
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    view.setBackgroundColor(this.getColor(R.color.dark_gray));
                    break;
            }
        }
    }

    protected void init() {
        height = scfLayout.getHeight();
        width = scfLayout.getWidth();
    }

    @SuppressLint("SetTextI18n")
    public void goButton(View view) {
        if (prefSCF.getVibrationButtonSCFBool() && b)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(300);
            }
        scfLayout.removeAllViews();
        scfWinner.setText(getString(R.string.scfWinner));
        if ((redPlayerEditText.getText().length() == 0) && (bluePlayerEditText.getText().length() == 0)) {
            utils.notifyUserShortWay(this, "Inserisci i nomi dei gicoatori!!");
            return;
        } else if (redPlayerEditText.getText().length() == 0) {
            utils.notifyUserShortWay(this, "Inserisci il nome del giocatore rosso!!");
            return;
        } else if (bluePlayerEditText.getText().length() == 0) {
            utils.notifyUserShortWay(this, "Inserisci il nome del giocatore blu!!");
            return;
        }
        final ImageView redPlayerImage = new ImageView(this);
        final ImageView bluePlayerImage = new ImageView(this);
        Random RandomGenerator = new Random();
        int nred = RandomGenerator.nextInt(3);
        redPlayerImage.setImageResource(redArray[nred]);
        scfLayout.addView(redPlayerImage);
        redPlayerImage.getLayoutParams().width = width / 2;
        redPlayerImage.getLayoutParams().height = height;
        int nblue = RandomGenerator.nextInt(3);
        bluePlayerImage.setImageResource(blueArray[nblue]);
        scfLayout.addView(bluePlayerImage);
        bluePlayerImage.getLayoutParams().width = width / 2;
        bluePlayerImage.getLayoutParams().height = height;
        String text = "Il vincitore è: ";
        if (nred == 0 && nblue == 1) {
            scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else if (nred == 0 && nblue == 2) {
            scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else if (nred == 1 && nblue == 0) {
            scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else if (nred == 1 && nblue == 2) {
            scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else if (nred == 2 && nblue == 0) {
            scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else if (nred == 2 && nblue == 1) {
            scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        }
        if (nblue == 0 && nred == 1) {
            scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else if (nblue == 0 && nred == 2) {
            scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else if (nblue == 1 && nred == 0) {
            scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else if (nblue == 1 && nred == 2) {
            scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else if (nblue == 2 && nred == 0) {
            scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else if (nblue == 2 && nred == 1) {
            scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else {
            scfWinner.setText("Pareggio");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void shakingStarted() {
        if (prefSCF.getShakeSCFBool() && b) {
            if (prefSCF.getVibrationShakeSCFBool() && b)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    vibrator.vibrate(300);
                }
            scfLayout.removeAllViews();
            scfWinner.setText(getString(R.string.scfWinner));
            if ((redPlayerEditText.getText().length() == 0) && (bluePlayerEditText.getText().length() == 0)) {
                utils.notifyUserShortWay(this, "Inserisci i nomi dei gicoatori!!");
                return;
            } else if (redPlayerEditText.getText().length() == 0) {
                utils.notifyUserShortWay(this, "Inserisci il nome del giocatore rosso!!");
                return;
            } else if (bluePlayerEditText.getText().length() == 0) {
                utils.notifyUserShortWay(this, "Inserisci il nome del giocatore blu!!");
                return;
            }
            final ImageView redPlayerImage = new ImageView(this);
            final ImageView bluePlayerImage = new ImageView(this);
            Random RandomGenerator = new Random();
            int nred = RandomGenerator.nextInt(3);
            redPlayerImage.setImageResource(redArray[nred]);
            scfLayout.addView(redPlayerImage);
            redPlayerImage.getLayoutParams().width = width / 2;
            redPlayerImage.getLayoutParams().height = height;
            int nblue = RandomGenerator.nextInt(3);
            bluePlayerImage.setImageResource(blueArray[nblue]);
            scfLayout.addView(bluePlayerImage);
            bluePlayerImage.getLayoutParams().width = width / 2;
            bluePlayerImage.getLayoutParams().height = height;
            String text = "Il vincitore è: ";
            if (nred == 0 && nblue == 1) {
                scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else if (nred == 0 && nblue == 2) {
                scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else if (nred == 1 && nblue == 0) {
                scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else if (nred == 1 && nblue == 2) {
                scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else if (nred == 2 && nblue == 0) {
                scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else if (nred == 2 && nblue == 1) {
                scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            }
            if (nblue == 0 && nred == 1) {
                scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else if (nblue == 0 && nred == 2) {
                scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else if (nblue == 1 && nred == 0) {
                scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else if (nblue == 1 && nred == 2) {
                scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else if (nblue == 2 && nred == 0) {
                scfWinner.setText(Html.fromHtml(text + "<font color='blue'>" + bluePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else if (nblue == 2 && nred == 1) {
                scfWinner.setText(Html.fromHtml(text + "<font color='red'>" + redPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else {
                scfWinner.setText("Pareggio");
            }
        }
    }

    @Override
    public void shakingStopped() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_options_scf, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.preference_scf) {
            b = false;
            utils.goToPrefderenzeSCF(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onStart() {
        super.onStart();
        prefSCF = utils.loadDataSCF(this, new PreferencesSCF());
        b = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefSCF = utils.loadDataSCF(this, new PreferencesSCF());
        b = true;
    }
}