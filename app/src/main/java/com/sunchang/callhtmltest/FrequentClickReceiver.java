package com.sunchang.callhtmltest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class FrequentClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (MainActivity.isFirstReceived) {
            Toast.makeText(context, "点击太频繁，请稍后再试~", Toast.LENGTH_SHORT).show();
            MainActivity.isFirstReceived = false;
        }
    }
}
