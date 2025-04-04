package com.nttn.productivity_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
abstract class Employee {
    private int id;
    private String name;
    Employee(int id, String name)  {
        this.id = id;
        this.name = name;
    }
    abstract int tinhLuong();

    @Override
    public String toString() {
        return id + "-" + name;
    }
}

class EmployeeFullTime extends Employee {
    EmployeeFullTime(int id, String name) {
        super(id, name);
    }

    @Override
    int tinhLuong() {
        return 200;
    }

    @Override
    public String toString() {
        return super.toString() + "-->" + "FullTime=" + tinhLuong();
    }
}


class EmployeePartTime extends Employee {
    EmployeePartTime(int id, String name) {
        super(id, name);
    }

    @Override
    int tinhLuong() {
        return 100;
    }

    @Override
    public String toString() {
        return super.toString() + "-->" + "PartTime=" + tinhLuong();
    }
}

public class lab2_bai3 extends Activity {
/*    boolean isChinhThuc = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab2_bai3);
        final ListView listview = findViewById(R.id.listview);
        final TextView posval = findViewById(R.id.tv_pos_val);
        final Button submitButton = findViewById(R.id.bt_submit);

        // 5 components:
        final EditText manhanvien = findViewById(R.id.et_code);
        final EditText tenhanvien = findViewById(R.id.et_name);
        final RadioGroup loainhanvien = findViewById(R.id.radio_group);
        loainhanvien.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println("group: " + group.toString() + "checked id: " + checkedId);
                if(checkedId == R.id.radio_type1) {
                    isChinhThuc = true;
                } else if (checkedId == R.id.radio_type2) {
                    isChinhThuc = false;
                }
            }
        });

        final ArrayList<Employee> items = new ArrayList<Employee>();
        final ArrayAdapter<Employee> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listview.setAdapter(adapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int manv = Integer.parseInt(manhanvien.getText().toString());
                final String tennv = tenhanvien.getText().toString();
                Employee em = null;
                if(isChinhThuc) {
                    em = new EmployeeFullTime(manv, tennv);
                } else {
                    em = new EmployeePartTime(manv, tennv);
                }
                items.add(em);
                adapter.notifyDataSetChanged();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String text = "position: %d, value: %s";
                final String value = parent.getItemAtPosition(position).toString();
                posval.setText(String.format(text, position, value));
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
