package com.nttn.productivity_app.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nttn.productivity_app.R;
import com.nttn.productivity_app.model.Habit;
import com.nttn.productivity_app.model.HabitAndroidViewModel;
import com.nttn.productivity_app.ui.habit.HabitRecyclerViewAdapter;
import com.nttn.productivity_app.ui.habit.HabitViewModel;
import com.nttn.productivity_app.ui.habit.OnHabitClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

public class HomeFragment extends Fragment implements OnHabitClickListener {

    private static final String TAG = "HOME_FRAGMENT";

    private RecyclerView habitRecyclerView;
    private HabitRecyclerViewAdapter habitRecyclerViewAdapter;
    private HabitAndroidViewModel habitAndroidViewModel;
    private HabitViewModel habitViewModel;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        habitRecyclerView = root.findViewById(R.id.habit_recycler_view);
        habitRecyclerView.setHasFixedSize(true);
        habitRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        habitViewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);

        habitAndroidViewModel = new ViewModelProvider.AndroidViewModelFactory(
                requireActivity().getApplication()
        ).create(HabitAndroidViewModel.class);

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
            if (which == 0) {   /* reset progress */
                Date currentProgress = habit.getStartedAt();
                habit.setStartedAt(new Date());
                HabitAndroidViewModel.updateHabit(habit);
                Snackbar
                        .make(requireActivity().findViewById(R.id.fab_add_habit),
                                R.string.habit_progress_reset_notification, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo_action_text, v_ -> {
                            habit.setStartedAt(currentProgress);
                            HabitAndroidViewModel.updateHabit(habit);
                        })
                        .show();
            } else if (which == 1) {    /* edit habit */
                habitViewModel.setHabit(habit);
                habitViewModel.setEditing(true);
                habitViewModel.getHabitBottomSheetFragment().show(
                        requireActivity().getSupportFragmentManager(),
                        habitViewModel.getHabitBottomSheetFragment().getTag()
                );
            } else if (which == 2) {    /* delete habit */
                HabitAndroidViewModel.deleteHabit(habit);
                Snackbar
                        .make(requireActivity().findViewById(R.id.fab_add_habit),
                                R.string.habit_deleted_notification, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo_action_text, v_ -> HabitAndroidViewModel.insertHabit(habit))
                        .show();
            }
        });
        alertBuilder.create().show();
    }
}