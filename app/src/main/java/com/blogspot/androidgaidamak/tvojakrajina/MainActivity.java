package com.blogspot.androidgaidamak.tvojakrajina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blogspot.androidgaidamak.tvojakrajina.coloringview.UkraineMapView;
import com.blogspot.androidgaidamak.tvojakrajina.databinding.ActivityMainBinding;

import java.util.HashSet;
import java.util.Set;

import static com.blogspot.androidgaidamak.tvojakrajina.OblastsActivity.KEY_COMMON_PREFERENCES;
import static com.blogspot.androidgaidamak.tvojakrajina.OblastsActivity.KEY_ENABLED_OBLASTS;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(KEY_COMMON_PREFERENCES, 0);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OblastsActivity.class);
            startActivity(intent);
        });
        binding.tvojaKrajinaLogo.setOnClickListener(v -> openTvojaKrajinaSite());
    }

    private void openTvojaKrajinaSite() {
        Uri webPage = Uri.parse("https://vk.com/tvojakrajina");
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Set<String> enabledItems = preferences.getStringSet(KEY_ENABLED_OBLASTS, new HashSet<>());
        for (UkraineMapView.Oblast oblast : UkraineMapView.Oblast.values()) {
            boolean enabled = enabledItems.contains(String.valueOf(oblast.ordinal()));
            if (enabled) {
                colorOblast(oblast);
            } else {
                binding.mapView.clearOblast(oblast);
            }
        }
        int enabledOblastsCount = enabledItems.size();
        setTitle(getString(R.string.main_activity_title_template, enabledOblastsCount));
    }

    private void colorOblast(UkraineMapView.Oblast oblast) {
        @ColorInt int color = 0;
        switch (oblast) {
            case DONETSK_OBLAST:
            case IVANO_FRANKIVSK_OBLAST:
            case KHERSON_OBLAST:
            case KHMELNYTSKYI_OBLAST:
            case KYIV_OBLAST:
            case POLTAVA_OBLAST:
            case KIROVOHRAD_OBLAST:
                color = Color.RED;
                break;
            case CRIMEAN_OBLAST:
            case CHERNIHIV_OBLAST:
            case KHARKIV_OBLAST:
            case CHERNIVTSI_OBLAST:
            case LVIV_OBLAST:
            case MYKOLAIV_OBLAST:
            case RIVNE_OBLAST:
            case VINNYTSIA_OBLAST:
            case ZAPORIZHIA_OBLAST:
                color = Color.BLUE;
                break;
            case LUHANSK_OBLAST:
            case DNIPROPETROVSK_OBLAST:
            case ODESA_OBLAST:
            case CHERKASY_OBLAST:
            case SUMY_OBLAST:
            case TERNOPIL_OBLAST:
            case VOLYN_OBLAST:
            case ZAKARPATTIA_OBLAST:
            case ZHYTOMYR_OBLAST:
                color = Color.GREEN;
                break;
        }
        binding.mapView.paintOblast(oblast, color);
    }
}
