package com.nttn.productivity_app.habits;

import android.os.Bundle;
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

public class Habits extends Fragment  {
    private static final String TAG = "HOME_FRAGMENT";
    private RecyclerView habitRecyclerView;
//    private HabitRecyclerViewAdapter habitRecyclerViewAdapter;
    private HabitAndroidViewModel habitAndroidViewModel;
    private HabitViewModel habitViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.habit_lists, container, false);

//        habitRecyclerView = root.findViewById(R.id.habit_recycler_view);
//        habitRecyclerView.setHasFixedSize(true);
//        habitRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
//
        habitViewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);
//
//        habitAndroidViewModel = new ViewModelProvider.AndroidViewModelFactory(
//                requireActivity().getApplication()
//        ).create(HabitAndroidViewModel.class);
//
//        habitAndroidViewModel
//                .getAllHabits()
//                .observe(requireActivity(), habits -> {
//                            habitRecyclerViewAdapter = new HabitRecyclerViewAdapter(habits, this);
//                            habitRecyclerView.setAdapter(habitRecyclerViewAdapter);
//                        }
//                );

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
