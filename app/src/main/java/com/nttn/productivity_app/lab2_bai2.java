package com.nttn.productivity_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class lab2_bai2 extends Activity {
/*    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab2_bai2);
        final ListView listview = findViewById(R.id.listview);
        final EditText editText = findViewById(R.id.et_name);
        final TextView posval = findViewById(R.id.tv_pos_val);
        final Button submitButton = findViewById(R.id.bt_submit);
        final ArrayList<String> items = new ArrayList<String>();
        items.add("Tèo");
        items.add("Tí");
        items.add("Bin");
        items.add("Bo" );
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listview.setAdapter(adapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = editText.getText().toString();
                if (text.isEmpty()) return;
                editText.setText("");
                items.add(text);
                adapter.notifyDataSetChanged();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String text = "position: %d, value: %s";
                posval.setText(String.format(text, position, "value"));
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = position;
                if(index >= items.size()) return false;
                items.remove(index);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }*/
}
