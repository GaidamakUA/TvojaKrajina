package com.blogspot.androidgaidamak.tvojakrajina;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.blogspot.androidgaidamak.tvojakrajina.coloringview.UkraineMapView;
import com.blogspot.androidgaidamak.tvojakrajina.databinding.ActivityOblastsBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OblastsActivity extends AppCompatActivity {
    public static final String KEY_COMMON_PREFERENCES = "COMMON_SETTINGS";
    public static final String KEY_ENABLED_OBLASTS = "ENABLED_OBLASTS";

    private ActivityOblastsBinding binding;
    private SharedPreferences preferences;
    private BindingRecyclerViewAdapter<EnablableOblast> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(KEY_COMMON_PREFERENCES, 0);
        setTitle(R.string.oblast_activity_title);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_oblasts);
        initAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.confirm_menu, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Set<String> enabledOblasts = new HashSet<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            EnablableOblast item = adapter.getItem(i);
            if (item.isEnabled) {
                enabledOblasts.add(String.valueOf(item.oblast.ordinal()));
            } else {
                enabledOblasts.remove(String.valueOf(item.oblast.ordinal()));
            }
        }
        preferences.edit().putStringSet(KEY_ENABLED_OBLASTS, enabledOblasts).apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.confirm) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initAdapter() {
        adapter = new BindingRecyclerViewAdapter<>(R.layout.oblast_list_item, BR.oblast, this::itemClicked);
        binding.list.setAdapter(adapter);
        List<EnablableOblast> items = createItems();
        adapter.setItems(items);
    }

    private void itemClicked(EnablableOblast item) {
        item.setEnabled(!item.isEnabled());
        adapter.notifyDataSetChanged();
    }

    private List<EnablableOblast> createItems() {
        Set<String> enabledItems = preferences.getStringSet(KEY_ENABLED_OBLASTS, new HashSet<>());
        List<EnablableOblast> items = new ArrayList<>();
        for (UkraineMapView.Oblast oblast : UkraineMapView.Oblast.values()) {
            boolean enabled = enabledItems.contains(String.valueOf(oblast.ordinal()));
            items.add(new EnablableOblast(oblast, enabled));
        }
        return items;
    }

    public static class EnablableOblast {
        private UkraineMapView.Oblast oblast;
        private boolean isEnabled;

        public EnablableOblast(UkraineMapView.Oblast oblast, boolean isEnabled) {
            this.oblast = oblast;
            this.isEnabled = isEnabled;
        }

        @StringRes
        public int getOblastName() {
            return oblast.getOblastName();
        }

        public boolean isEnabled() {
            return isEnabled;
        }

        public void setEnabled(boolean enabled) {
            isEnabled = enabled;
        }
    }
}
