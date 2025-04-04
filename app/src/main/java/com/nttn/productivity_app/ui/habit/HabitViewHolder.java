package com.nttn.productivity_app.ui.habit;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nttn.productivity_app.R;
import com.nttn.productivity_app.model.Habit;
import com.nttn.productivity_app.util.ProgressTimeFormatter;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HabitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView textViewHabitTitle;
    public TextView textHabitProgressTimer;

    public CountDownTimer habitProgressTimer;

    final OnHabitClickListener onHabitClickListener;
    final HabitRecyclerViewAdapter habitRecyclerViewAdapter;

    public HabitViewHolder(@NotNull View itemView, HabitRecyclerViewAdapter habitRecyclerViewAdapter) {
        super(itemView);
        textViewHabitTitle = itemView.findViewById(R.id.text_habit_title);
        textHabitProgressTimer = itemView.findViewById(R.id.text_habit_progress_timer);
        textViewHabitTitle.setText("");
        textHabitProgressTimer.setText("");

        this.habitRecyclerViewAdapter = habitRecyclerViewAdapter;
        this.onHabitClickListener = habitRecyclerViewAdapter.getOnHabitClickListener();
        itemView.findViewById(R.id.habit_row_card_layout).setOnClickListener(this);
        itemView.findViewById(R.id.habit_row_card_layout).setOnLongClickListener(this);
    }

    public void setHabitProgressTime(long timeInMillis) {
        if (textHabitProgressTimer != null) {
            String myStr = "You have %s time left";
            String result = String.format(myStr, ProgressTimeFormatter.parse(timeInMillis));
            textHabitProgressTimer.setText(result);
        }
    }


    public void startProgressTimer(Date startedAt) {
        if (habitProgressTimer != null) {
            habitProgressTimer.cancel();
        }
        long currentTimeUpdateIntervalMillis = 3600_000;
        long countDownInterval = 1000;
        habitProgressTimer = new HabitProgressTimer(
                currentTimeUpdateIntervalMillis,
                countDownInterval,
                startedAt
        ).start();
    }

    @Override
    public void onClick(View v) {
        int itemId = v.getId();
        Habit currentHabit = habitRecyclerViewAdapter
                .getHabitList()
                .get(getAbsoluteAdapterPosition());
        System.out.println("Habit, started at: " + currentHabit.startedAt.toString() + ", ended at: " + currentHabit.endedAt.toString());

        if (itemId == R.id.habit_row_card_layout) {
            onHabitClickListener.onHabitClick(currentHabit);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int itemId = v.getId();
        Habit currentHabit = habitRecyclerViewAdapter
                .getHabitList()
                .get(getAbsoluteAdapterPosition());

        if (itemId == R.id.habit_row_card_layout) {
            onHabitClickListener.onHabitLongClick(currentHabit);
            return true;
        }
        return false;
    }

    public class HabitProgressTimer extends CountDownTimer {
        long currentTime;
        long countDownInterval;
        long startedAtTime;

        public HabitProgressTimer(long millisInFuture, long countDownInterval, Date startedAt) {
            super(millisInFuture, countDownInterval);
            this.countDownInterval = countDownInterval;

            if (startedAt == null) {
                startedAtTime = new Date().getTime();
            } else {
                startedAtTime = startedAt.getTime();
            }

            setCurrentTime();
        }

        private void setCurrentTime() {
            this.currentTime = (new Date().getTime() - startedAtTime);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Habit currentHabit = habitRecyclerViewAdapter
                    .getHabitList()
                    .get(getAbsoluteAdapterPosition());
            Date now = new Date();
            long timeLeftInMillis = currentHabit.endedAt.getTime() - now.getTime();
            currentTime = timeLeftInMillis;
            setHabitProgressTime(currentTime);
        }

        @Override
        public void onFinish() {
            setCurrentTime();
            this.start();
        }
    }
}
