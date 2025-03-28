package com.nttn.productivity_app;

import static com.nttn.productivity_app.CalendarUtils.daysInMonthArray;
import static com.nttn.productivity_app.CalendarUtils.monthYearFromDate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import productivity_app_core.ITaskRepository;
import productivity_app_core.SqliteTaskRepository;
import productivity_app_core.Statistics;
import productivity_app_core.Task;
import productivity_app_core.TaskService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// FIXME: (trantr) this app need 2 fragments in order to work (to even open up)
// 1. All todo fragment
// 2. Edit todo fragment
// 3. Today's todo fragment (my part) (need to add this to nav as well)
public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date != null)
        {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    public void weeklyAction(View view)
    {
        // startActivity(new Intent(this, WeekViewActivity.class));
    }
}