package com.example.utilities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utilities.Utility.Utils;

public class InfoActivity extends AppCompatActivity {
    private final Utils util = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> util.goToMainActivity(InfoActivity.this));
    }
}