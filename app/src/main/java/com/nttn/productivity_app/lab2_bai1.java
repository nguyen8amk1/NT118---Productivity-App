package com.nttn.productivity_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;

public class lab2_bai1 extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab2_bai1);
        final ListView listview = findViewById(R.id.listview);
        final String[] items = { "Tèo", "Tí", "Bin", "Bo" };
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listview.setAdapter(adapter);
    }
}
