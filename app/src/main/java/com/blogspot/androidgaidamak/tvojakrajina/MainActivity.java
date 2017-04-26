package com.blogspot.androidgaidamak.tvojakrajina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.blogspot.androidgaidamak.tvojakrajina.coloringview.UkraineMapView;
import com.blogspot.androidgaidamak.tvojakrajina.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import static com.blogspot.androidgaidamak.tvojakrajina.OblastsActivity.KEY_COMMON_PREFERENCES;
import static com.blogspot.androidgaidamak.tvojakrajina.OblastsActivity.KEY_ENABLED_OBLASTS;

public class MainActivity extends AppCompatActivity {

    public static final String AUTHORITY = "com.blogspot.androidgaidamak.tvojakrajina.fileprovider";
    private ActivityMainBinding binding;
    private SharedPreferences preferences;
    private int enabledOblastsCount;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
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
        enabledOblastsCount = enabledItems.size();
        setTitle(getString(R.string.main_activity_title_template, enabledOblastsCount));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            shareMap();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void openTvojaKrajinaSite() {
        Uri webPage = Uri.parse("https://vk.com/tvojakrajina");
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void shareMap() {
        File file = createOblastImageFile();
        Uri imageUri = FileProvider.getUriForFile(MainActivity.this, AUTHORITY, file);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.share_social_title, enabledOblastsCount));
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_social_title, enabledOblastsCount));
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_social_text, enabledOblastsCount));
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
    }

    private File createOblastImageFile() {
        String cacheDir = getCacheDir().toString();
        OutputStream outStream = null;
        File directory = new File(cacheDir, "/colored_map");
        if (!directory.exists()) {
            directory.mkdir();
        }
        String filename = "/tvoja_krajina.png";
        File file = new File(directory, filename);

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            Bitmap bitmap = binding.mapView.createBitmap();

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
//            e.printStackTrace();
        }
        return file;
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
