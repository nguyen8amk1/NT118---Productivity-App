package com.nt118.group2.View.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nt118.group2.R;

import java.util.Calendar;

public class CalendarViewHolder extends RecyclerView.ViewHolder{

    public final TextView dayOfMonth;
    public final LinearLayout cl_cell_calendar;
    public int _month;
    public int _year;
    public int _day;
    public CalendarViewHolder(@NonNull View itemView) {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        cl_cell_calendar = itemView.findViewById(R.id.cell_calendar);
        cl_cell_calendar.setBackgroundColor(Color.parseColor("#f0ebe5"));
    }


    public void IsCurrentDay(int month, int year, Context context){
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int m = cal.get(Calendar.MONTH);
        int y = cal.get(Calendar.YEAR);
        _month = m;
        _year =y;
        _day =day;
        if(dayOfMonth.getText() !="")
        {
            if(Integer.parseInt(dayOfMonth.getText().toString()) == day && (m)==month && y==year)
            {
                dayOfMonth.setTextColor(Color.BLUE);
                TextView textView = itemView.findViewById(R.id.cellDayText);
                textView.setTextSize(25);
            }
        }
    }

}
