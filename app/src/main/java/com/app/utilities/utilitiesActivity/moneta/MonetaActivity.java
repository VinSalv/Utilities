package com.app.utilities.utilitiesActivity.moneta;

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
import com.app.utilities.settings.Preferenze;
import com.app.utilities.utility.Shake;
import com.app.utilities.utility.Utils;

import java.util.Random;

public class MonetaActivity extends AppCompatActivity implements Shake.Callback {
    final int[] monetaArray = {
            R.drawable.testa,
            R.drawable.croce,
    };
    final TypedValue typedValue = new TypedValue();
    private final Utils utils = new Utils();
    protected Configuration mPrevConfig;
    Button goMonetaButton;
    LinearLayout monetaLayout;
    TextView testaPlayerEditText;
    TextView crocePlayerEditText;
    TextView croceWinner;
    int height;
    int width;
    @SuppressWarnings("unused")
    int colorAccent;
    Preferenze pref;
    PreferencesMoneta prefMoneta;
    Vibrator vibrator;
    Boolean b = true;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferenze());
        prefMoneta = utils.loadDataMoneta(this, new PreferencesMoneta());
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
        setContentView(R.layout.activity_moneta);
        Toolbar myToolbarMoneta = findViewById(R.id.toolbarMoneta);
        setSupportActionBar(myToolbarMoneta);
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(android.R.attr.colorAccent, typedValue, true);
        @SuppressLint("Recycle") TypedArray arr = obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.colorAccent});
        colorAccent = arr.getColor(0, -1);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        @SuppressWarnings("unused") Shake shake = new Shake(this, 2.5d, 0, this);
        goMonetaButton = findViewById(R.id.goMoneta);
        monetaLayout = findViewById(R.id.monetaLayout);
        ViewTreeObserver observer = monetaLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                init();
                monetaLayout.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });
        croceWinner = findViewById(R.id.monetaWinner);
        testaPlayerEditText = findViewById(R.id.testaPlayerEditText);
        crocePlayerEditText = findViewById(R.id.crocePlayerEditText);
        goMonetaButton.setOnClickListener(this::goButton);
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());
        mPrevConfig = new Configuration(getResources().getConfiguration());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        pref = utils.loadData(this, new Preferenze());
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
        height = monetaLayout.getHeight();
        width = monetaLayout.getWidth();
    }

    @SuppressLint("SetTextI18n")
    public void goButton(View view) {
        if (prefMoneta.getVibrationButtonMonetaBool() && b) {
            vibrator.cancel();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(300);
            }
        }
        monetaLayout.removeAllViews();
        croceWinner.setText(getString(R.string.monetaWinner));
        if ((testaPlayerEditText.getText().length() == 0) && (crocePlayerEditText.getText().length() == 0)) {
            utils.notifyUserShortWay(this, "Inserisci i nomi dei gicoatori!!");
            return;
        } else if (testaPlayerEditText.getText().length() == 0) {
            utils.notifyUserShortWay(this, "Inserisci il nome del giocatore testa!!");
            return;
        } else if (crocePlayerEditText.getText().length() == 0) {
            utils.notifyUserShortWay(this, "Inserisci il nome del giocatore croce!!");
            return;
        }
        final ImageView testaPlayerImage = new ImageView(this);
        Random RandomGenerator = new Random();
        int nmoneta = RandomGenerator.nextInt(2);
        testaPlayerImage.setImageResource(monetaArray[nmoneta]);
        monetaLayout.addView(testaPlayerImage);
        testaPlayerImage.getLayoutParams().width = width / 2;
        testaPlayerImage.getLayoutParams().height = height;
        String text = "Il vincitore è: ";
        if (nmoneta == 0) {
            croceWinner.setText(Html.fromHtml(text + "<font color='red'>" + testaPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        } else {
            croceWinner.setText(Html.fromHtml(text + "<font color='blue'>" + crocePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void shakingStarted() {
        if (prefMoneta.getShakeMonetaBool() && b) {
            if (prefMoneta.getVibrationShakeMonetaBool() && b) {
                vibrator.cancel();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(300);
                }
            }
            monetaLayout.removeAllViews();
            croceWinner.setText(getString(R.string.monetaWinner));
            if ((testaPlayerEditText.getText().length() == 0) && (crocePlayerEditText.getText().length() == 0)) {
                utils.notifyUserShortWay(this, "Inserisci i nomi dei gicoatori!!");
                return;
            } else if (testaPlayerEditText.getText().length() == 0) {
                utils.notifyUserShortWay(this, "Inserisci il nome del giocatore testa!!");
                return;
            } else if (crocePlayerEditText.getText().length() == 0) {
                utils.notifyUserShortWay(this, "Inserisci il nome del giocatore croce!!");
                return;
            }
            final ImageView testaPlayerImage = new ImageView(this);
            Random RandomGenerator = new Random();
            int nmoneta = RandomGenerator.nextInt(2);
            testaPlayerImage.setImageResource(monetaArray[nmoneta]);
            monetaLayout.addView(testaPlayerImage);
            testaPlayerImage.getLayoutParams().width = width / 2;
            testaPlayerImage.getLayoutParams().height = height;
            String text = "Il vincitore è: ";
            if (nmoneta == 0) {
                croceWinner.setText(Html.fromHtml(text + "<font color='red'>" + testaPlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            } else {
                croceWinner.setText(Html.fromHtml(text + "<font color='blue'>" + crocePlayerEditText.getText() + "</font>"), TextView.BufferType.SPANNABLE);
            }
        }
    }

    @Override
    public void shakingStopped() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_options_moneta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.preference_moneta) {
            b = false;
            utils.goToPrefderenzeMoneta(this);
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
        prefMoneta = utils.loadDataMoneta(this, new PreferencesMoneta());
        b = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefMoneta = utils.loadDataMoneta(this, new PreferencesMoneta());
        b = true;
    }
}