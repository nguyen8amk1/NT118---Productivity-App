package com.nt118.group2.Library;

import android.os.CountDownTimer;

public abstract class CountUpTimer extends CountDownTimer {
    private static  final  long INTERVAL_MS = CalendarExtension.ONE_MINUTE;
    private static final long DURATIONMS = CalendarExtension.ONE_DAY*356;


    public CountUpTimer() {
        super(DURATIONMS, INTERVAL_MS);
    }

    public abstract void onTick(int second);
    @Override
    public void onTick(long msUntilFinished) {
        int second = (int) ((DURATIONMS - msUntilFinished)/ CalendarExtension.ONE_MINUTE);
        onTick(second);
    }

    @Override
    public void onFinish() {
        onTick(DURATIONMS / 1000);
    }
}
