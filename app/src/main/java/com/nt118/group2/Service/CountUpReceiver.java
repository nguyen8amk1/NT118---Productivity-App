package com.nt118.group2.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.nt118.group2.Library.Action;
import com.nt118.group2.Library.Key;

public class CountUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(@NonNull Context context, Intent intent) {
        int action = intent.getIntExtra(Key.SEND_ACTION, Action.NONE);
        Intent service = new Intent(context, CountUpService.class);
        service.putExtra(Key.SEND_ACTION, action);
        context.startService(service);
    }
}
