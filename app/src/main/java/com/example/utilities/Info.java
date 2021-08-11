package com.example.utilities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Info extends AppCompatActivity {
    private final Utility util = new Utility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> util.goToMainActivity(Info.this));
    }
}