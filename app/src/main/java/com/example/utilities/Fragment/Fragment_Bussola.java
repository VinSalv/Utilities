package com.example.utilities.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.utilities.Bussola.Compass;
import com.example.utilities.Bussola.SOTWFormatter;
import com.example.utilities.R;

public class Fragment_Bussola extends Fragment {
    private Compass compass;
    private ImageView arrowView;
    private TextView sotwLabel;
    private float currentAzimuth;
    private SOTWFormatter sotwFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__bussola, container, false);
        sotwFormatter = new SOTWFormatter(getActivity());
        arrowView = view.findViewById(R.id.main_image_hands);
        sotwLabel = view.findViewById(R.id.sotw_label);
        setupCompass();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        compass.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        compass.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        compass.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        compass.stop();
    }

    private void setupCompass() {
        compass = new Compass(requireActivity());
        Compass.CompassListener cl = getCompassListener();
        compass.setListener(cl);
    }

    private void adjustArrow(float azimuth) {
        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = azimuth;
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        arrowView.startAnimation(an);
    }

    private void adjustSotwLabel(float azimuth) {
        sotwLabel.setText(sotwFormatter.format(azimuth));
    }

    private Compass.CompassListener getCompassListener() {
        return azimuth -> requireActivity().runOnUiThread(() -> {
            adjustArrow(azimuth);
            adjustSotwLabel(azimuth);
        });
    }
}