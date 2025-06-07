package com.nttn.productivity_app.util;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.nttn.productivity_app.R;
import com.nttn.productivity_app.model.Habit;

public class DialogUtils {
    @NonNull
    static public AlertDialog getAlertDialog(Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Write your message here.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        return alert11;
    }

    static public AlertDialog getDeadlineDialog(Habit habit, Context context) {
        // Create a MediaPlayer to play the alarm sound
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound); // Ensure you have an alarm_sound.mp3 in res/raw

        // Inflate the custom dialog layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_deadline_met, null);

        // Set the habit information in the dialog
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        TextView descriptionTextView = dialogView.findViewById(R.id.dialog_description);
        TextView deadlineTextView = dialogView.findViewById(R.id.dialog_deadline);
        ImageView alarmImageView = dialogView.findViewById(R.id.dialog_alarm_image);

        titleTextView.setText(habit.getTitle());
        descriptionTextView.setText("Started at: " + habit.getStartedAt().toString());
        deadlineTextView.setText("Ended at: " + habit.getEndedAt().toString());
        alarmImageView.setImageResource(R.drawable.alarm_clock);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_ProductivityApp_Dialog);
        builder.setView(dialogView);
        builder.setTitle("Deadline Met!");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mediaPlayer.stop();
                dialog.dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mediaPlayer.stop();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mediaPlayer.start();
            }
        });

        return dialog;
    }
}
