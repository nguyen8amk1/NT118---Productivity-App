package com.nttn.productivity_app.ui.habit;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.nttn.productivity_app.model.Habit;
import com.nttn.productivity_app.model.HabitAndroidViewModel;
import com.nttn.productivity_app.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.nttn.productivity_app.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     HabitBottomSheetFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */

public class HabitBottomSheetFragment extends BottomSheetDialogFragment {

    private HabitViewModel habitViewModel;

    private EditText editTextHabit;
    private ImageButton buttonSaveHabit;
    private ImageButton buttonDiscard;

    private DatePicker datePicker;
    private TimePicker timePicker;
    private List<View> groupEditStartedAt;

    Calendar calendar = Calendar.getInstance();
    private Date dateStartedAt;

    public HabitBottomSheetFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View root = inflater.inflate(R.layout.fragment_habit_bottom_sheet, container, false);
        editTextHabit = root.findViewById(R.id.edit_text_habit_bottom_sheet);
        buttonSaveHabit = root.findViewById(R.id.button_save_bottom_sheet);
        buttonDiscard = root.findViewById(R.id.button_discard_bottom_sheet);
        datePicker = root.findViewById(R.id.date_picker);
        timePicker = root.findViewById(R.id.time_picker);
        groupEditStartedAt = Arrays.asList(datePicker, timePicker);

        habitViewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);

        return root;
    }

    private void reset() {
        for (View view : groupEditStartedAt) {
            view.setVisibility(View.VISIBLE);
        }
        dateStartedAt = new Date();
        editTextHabit.setText("");
        editTextHabit.setError(null);
        habitViewModel.setHabit(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (habitViewModel.getSelectedHabit().getValue() != null) {
            Habit editingHabit = habitViewModel.getSelectedHabit().getValue();
            editTextHabit.setText(editingHabit.getTitle());
            for (View view : groupEditStartedAt) {
                view.setVisibility(View.GONE);
            }
        } else {
            reset();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonSaveHabit.setOnClickListener(v -> {
            String title = editTextHabit.getText().toString().trim();
            if (!TextUtils.isEmpty(title)) {
                if (!habitViewModel.isEditing()) {
                    Habit newHabit = new Habit(title, dateStartedAt);
                    HabitAndroidViewModel.insertHabit(newHabit);
                    Snackbar
                            .make(requireActivity().findViewById(R.id.fab_add_habit),
                                    R.string.habit_added_notification, Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo_action_text, v_ -> HabitAndroidViewModel.deleteLastAddedHabit())
                            .show();
                } else {
                    Habit editingHabit = habitViewModel.getSelectedHabit().getValue();
                    Objects.requireNonNull(editingHabit).setTitle(title);
                    HabitAndroidViewModel.updateHabit(editingHabit);
                    Snackbar
                            .make(requireActivity().findViewById(R.id.fab_add_habit),
                                    R.string.habit_updated_notification, Snackbar.LENGTH_LONG)
                            .show();
                }
                Utils.hideSoftKeyboard(v);
                if (this.isVisible()) {
                    this.dismiss();
                }
                reset();
            } else {
                editTextHabit.requestFocus();
            }
        });

        buttonDiscard.setOnClickListener(v -> {
            this.dismiss();
            reset();
        });

        datePicker.setOnDateChangedListener((view_, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
        });

        timePicker.setOnTimeChangedListener((view_, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            dateStartedAt = calendar.getTime();
        });
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        reset();
    }
}
