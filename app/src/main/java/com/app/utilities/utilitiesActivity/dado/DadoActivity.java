package com.app.utilities.utilitiesActivity.dado;

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

public class DadoActivity extends AppCompatActivity implements Shake.Callback {
    final int[] diceArray = {
            R.drawable.dice_1,
            R.drawable.dice_2,
            R.drawable.dice_3,
            R.drawable.dice_4,
            R.drawable.dice_5,
            R.drawable.dice_6
    };
    final TypedValue typedValue = new TypedValue();
    private final Utils utils = new Utils();
    protected Configuration mPrevConfig;
    Button oneDice;
    Button twoDices;
    Button rollButton;
    LinearLayout diceLayout;
    TextView leftDiceNumber;
    TextView rightDiceNumber;
    TextView diceNumber;
    int height;
    int width;
    @SuppressWarnings("unused")
    int colorAccent;
    Preferenze pref;
    PreferencesDado prefDado;
    boolean one = true;
    ImageView dice;
    ImageView leftDice;
    ImageView rightDice;
    Vibrator vibrator;
    boolean b = true;
    boolean b2 = false;
    private Shake shake = null;

    public static boolean isOnDarkMode(Configuration configuration) {
        return (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = utils.loadData(this, new Preferenze());
        prefDado = utils.loadDataDado(this, new PreferencesDado());
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
        setContentView(R.layout.activity_dado);
        Toolbar myToolbarDice = findViewById(R.id.toolbarDice);
        setSupportActionBar(myToolbarDice);
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(android.R.attr.colorAccent, typedValue, true);
        @SuppressLint("Recycle") TypedArray arr = obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.colorAccent});
        colorAccent = arr.getColor(0, -1);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        shake = new Shake(this, 2.5d, 0, this);
        rollButton = findViewById(R.id.roll);
        oneDice = findViewById(R.id.oneDice);
        oneDice.setOnClickListener(this::OneDiceButton);
        twoDices = findViewById(R.id.twoDices);
        twoDices.setOnClickListener(this::TwoDicesButton);
        diceLayout = findViewById(R.id.diceLayout);
        ViewTreeObserver observer = diceLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                init();
                diceLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        diceNumber = findViewById(R.id.diceNumber);
        leftDiceNumber = findViewById(R.id.leftDiceNumber);
        rightDiceNumber = findViewById(R.id.rightDiceNumber);
        dice = new ImageView(this);
        leftDice = new ImageView(this);
        rightDice = new ImageView(this);
        OneDiceButton(this.getWindow().getDecorView().findViewById(android.R.id.content));
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
                    view.setBackgroundColor(getColor(colorSecondaryVariant));
                    break;
                case "DarkThemeSelected":
                case "DarkTheme":
                    view.setBackgroundColor(getColor(R.color.dark_gray));
                    break;
            }
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                case Configuration.UI_MODE_NIGHT_NO:
                    view.setBackgroundColor(getColor(colorSecondaryVariant));
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    view.setBackgroundColor(getColor(R.color.dark_gray));
                    break;
            }
        }
    }

    protected void init() {
        height = diceLayout.getHeight();
        width = diceLayout.getWidth();
    }

    @SuppressLint("SetTextI18n")
    public void OneDiceButton(View view) {
        if (prefDado.getVibrationButtonsDiceBool() && b & b2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(300);
            }
        b2 = true;
        one = true;
        diceLayout.removeAllViews();
        diceNumber.setText("0");
        leftDiceNumber.setVisibility(View.INVISIBLE);
        rightDiceNumber.setVisibility(View.INVISIBLE);
        ImageView dice_empty = new ImageView(this);
        dice_empty.setImageResource(R.drawable.dice_empty);
        diceLayout.addView(dice_empty);
        dice_empty.getLayoutParams().width = width;
        dice_empty.getLayoutParams().height = height;
        rollButton.setOnClickListener(v -> {
            if (prefDado.getVibrationButtonDiceBool() && b)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    vibrator.vibrate(300);
                }
            diceLayout.removeAllViews();
            Random RandomGenerator = new Random();
            int nbre = RandomGenerator.nextInt(6);
            diceNumber.setText(Integer.toString(nbre + 1));
            dice.setImageResource(diceArray[nbre]);
            diceLayout.addView(dice);
            dice.getLayoutParams().width = width;
            dice.getLayoutParams().height = height;
        });
    }

    @SuppressLint("SetTextI18n")
    public void TwoDicesButton(View view) {
        if (prefDado.getVibrationButtonsDiceBool() && b)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(300);
            }
        one = false;
        diceLayout.removeAllViews();
        diceNumber.setText("0");
        leftDiceNumber.setText("0");
        rightDiceNumber.setText("0");
        leftDiceNumber.setVisibility(View.VISIBLE);
        rightDiceNumber.setVisibility(View.VISIBLE);
        ImageView dice_empty = new ImageView(this);
        dice_empty.setImageResource(R.drawable.dice_empty);
        diceLayout.addView(dice_empty);
        dice_empty.getLayoutParams().width = width / 2;
        dice_empty.getLayoutParams().height = height;
        ImageView dice_empty2 = new ImageView(this);
        dice_empty2.setImageResource(R.drawable.dice_empty);
        diceLayout.addView(dice_empty2);
        dice_empty2.getLayoutParams().width = width / 2;
        dice_empty2.getLayoutParams().height = height;
        rollButton.setOnClickListener(v -> {
            if (prefDado.getVibrationButtonDiceBool() && b)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    vibrator.vibrate(300);
                }
            diceLayout.removeAllViews();
            Random RandomGenerator = new Random();
            int nbre = RandomGenerator.nextInt(6);
            int first = nbre;
            leftDiceNumber.setText(Integer.toString(first + 1));
            leftDice.setImageResource(diceArray[nbre]);
            diceLayout.addView(leftDice);
            leftDice.getLayoutParams().width = width / 2;
            leftDice.getLayoutParams().height = height;
            nbre = RandomGenerator.nextInt(6);
            rightDiceNumber.setText(Integer.toString(nbre + 1));
            rightDice.setImageResource(diceArray[nbre]);
            diceLayout.addView(rightDice);
            rightDice.getLayoutParams().width = width / 2;
            rightDice.getLayoutParams().height = height;
            diceNumber.setText((first + 1) + " + " + (nbre + 1) + " = " + (first + 1 + nbre + 1));
        });
    }

    public void onDestroy() {
        super.onDestroy();
        shake.close();
    }

    @SuppressLint("SetTextI18n")
    public void shakingStarted() {
        if (prefDado.getShakeDiceBool() && b)
            if (one) {
                if (prefDado.getVibrationShakeDiceBool() && b)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(300);
                    }
                diceLayout.removeAllViews();
                Random RandomGenerator = new Random();
                int nbre = RandomGenerator.nextInt(6);
                diceNumber.setText(Integer.toString(nbre + 1));
                dice.setImageResource(diceArray[nbre]);
                diceLayout.addView(dice);
                dice.getLayoutParams().width = width;
                dice.getLayoutParams().height = height;
            } else {
                if (prefDado.getVibrationShakeDiceBool() && b)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(300);
                    }
                diceLayout.removeAllViews();
                Random RandomGenerator = new Random();
                int nbre = RandomGenerator.nextInt(6);
                int first = nbre;
                leftDiceNumber.setText(Integer.toString(first + 1));
                leftDice.setImageResource(diceArray[nbre]);
                diceLayout.addView(leftDice);
                leftDice.getLayoutParams().width = width / 2;
                leftDice.getLayoutParams().height = height;
                nbre = RandomGenerator.nextInt(6);
                rightDiceNumber.setText(Integer.toString(nbre + 1));
                rightDice.setImageResource(diceArray[nbre]);
                diceLayout.addView(rightDice);
                rightDice.getLayoutParams().width = width / 2;
                rightDice.getLayoutParams().height = height;
                diceNumber.setText((first + 1) + " + " + (nbre + 1) + " = " + (first + 1 + nbre + 1));

            }
    }

    public void shakingStopped() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_options_dice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.preference_dice) {
            b = false;
            utils.goToPrefderenzeDado(this);
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
        prefDado = utils.loadDataDado(this, new PreferencesDado());
        b = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefDado = utils.loadDataDado(this, new PreferencesDado());
        b = true;
    }
}