package com.nttn.productivity_app;

import static com.nttn.productivity_app.CalendarUtils.daysInMonthArray;
import static com.nttn.productivity_app.CalendarUtils.monthYearFromDate;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

// FIXME: (trantr) this app need 2 fragments in order to work (to even open up)
// 1. All todo fragment
// 2. Edit todo fragment
// 3. Today's todo fragment (my part) (need to add this to nav as well)
public class AllTodoFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: implement
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}