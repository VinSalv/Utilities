package com.example.utilities.Utility;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.utilities.InfoActivity;
import com.example.utilities.MainActivity;

public class Utils {
    public void notifyUser(Context context, String message) {
        Toast.makeText(context,
                message,
                Toast.LENGTH_LONG).show();
    }

    private void notifyUserShortWay(Context context, String message) {
        Toast.makeText(context,
                message,
                Toast.LENGTH_SHORT).show();
    }

    public void goToMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void goToInfoActivity(Context context) {
        Intent intent = new Intent(context, InfoActivity.class);
        context.startActivity(intent);
    }

    public double roundAvoid(double value, int places) {

        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

}
