package com.example.richardantonios.testapp.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class StickyService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if(!MyPreference.getInstance(getApplicationContext()).getBooleanData("logged_in"))
        {
            MyPreference.getInstance(getApplicationContext()).saveStringData("logged_in_date", "");
            MyPreference.getInstance(getApplicationContext()).saveIntData("hours_elapsed", 0);
            MyPreference.getInstance(getApplicationContext()).saveIntData("minutes_elapsed", 0);
            MyPreference.getInstance(getApplicationContext()).saveIntData("seconds_elapsed", 0);
        }
    }
}