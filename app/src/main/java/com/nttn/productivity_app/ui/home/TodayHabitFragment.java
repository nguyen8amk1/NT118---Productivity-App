package com.nttn.productivity_app.ui.home;

import android.app.AlertDialog;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nttn.productivity_app.R;
import com.nttn.productivity_app.data.HabitRepository_InMemory;
import com.nttn.productivity_app.data.iHabitRepository;
import com.nttn.productivity_app.model.Habit;
import com.nttn.productivity_app.model.HabitAndroidViewModel;
import com.nttn.productivity_app.ui.habit.HabitRecyclerViewAdapter;
import com.nttn.productivity_app.ui.habit.HabitViewModel;
import com.nttn.productivity_app.ui.habit.OnHabitClickListener;
import com.google.android.material.snackbar.Snackbar;

public class TodayHabitFragment extends Fragment implements OnHabitClickListener {

    private static final String TAG = "HOME_FRAGMENT";

    private RecyclerView habitRecyclerView;
    private HabitRecyclerViewAdapter habitRecyclerViewAdapter;
    private HabitAndroidViewModel habitAndroidViewModel;
    private HabitViewModel habitViewModel;
    private iHabitRepository habitRepository = HabitRepository_InMemory.getInstance();


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_habitslist, container, false);
        habitRecyclerView = root.findViewById(R.id.habit_recycler_view);
        habitRecyclerView.setHasFixedSize(true);
        habitRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        habitViewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);
//        habitAndroidViewModel = new ViewModelProvider.AndroidViewModelFactory(
//                requireActivity().getApplication()
//        ).create(HabitAndroidViewModel.class);
        habitAndroidViewModel = new HabitAndroidViewModel((Application) requireActivity().getApplicationContext(), (iHabitRepository) habitRepository);

        {
            // final List<Habit> habits = new ArrayList<>();
            //        habits.add(new Habit("today's hello world 1", new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime()));
            //        habits.add(new Habit("today's hello world 2", new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime()));
            //        habits.add(new Habit("today's hello world 3", new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime()));
            //        habits.add(new Habit("today's hello world 4", new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime()));
            //        habits.add(new Habit("today's hello world 5", new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime()));
            //        habitRecyclerViewAdapter = new HabitRecyclerViewAdapter(habits, this);
            //        habitRecyclerView.setAdapter(habitRecyclerViewAdapter);
        }

        habitAndroidViewModel
                .getAllHabits()
                .observe(requireActivity(), habits -> {
                            habitRecyclerViewAdapter = new HabitRecyclerViewAdapter(habits, this);
                            habitRecyclerView.setAdapter(habitRecyclerViewAdapter);
                        }
                );

        return root;
    }

    @Override
    public void onHabitClick(Habit habit) {
        Log.d(TAG,"Clicked: " + habit);
    }

    @Override
    public void onHabitLongClick(Habit habit) {
        Log.d(TAG,"Long Clicked: " + habit);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setItems(R.array.habit_popup_menu_items, (dialog, which) -> {
            if (which == 0) {    /* edit habit, this is pure ui */
                habitViewModel.setHabit(habit);
                habitViewModel.setEditing(true);
                habitViewModel.getHabitBottomSheetFragment().show(
                        requireActivity().getSupportFragmentManager(),
                        habitViewModel.getHabitBottomSheetFragment().getTag()
                );
            } else if (which == 1) {    /* delete habit */
                habitRepository.deleteHabit(habit);
                Snackbar.make(requireActivity().findViewById(R.id.fab_add_habit),
                                R.string.habit_deleted_notification, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo_action_text, v_ -> habitRepository.insertHabit(habit))
                        .show();
            }
        });
        alertBuilder.create().show();
    }
}