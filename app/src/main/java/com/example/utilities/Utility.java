package com.example.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.utilities.ui.main.PageFragment;

public class Utility {
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
}
