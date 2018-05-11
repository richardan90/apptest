package com.example.richardantonios.testapp.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.richardantonios.testapp.MainActivity;
import com.example.richardantonios.testapp.R;
import com.example.richardantonios.testapp.utils.MyPreference;


public class TimerFragment extends BaseFragment {

    private TextView logindate;
    private Chronometer chronometer;
    private int hours_elapsed;
    private int minutes_elapsed;
    private int seconds_elapsed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        if(getActivity() != null) {
            // Set title bar
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Timer Fragment");
            String date = MyPreference.getInstance(getActivity().getApplicationContext()).getStringData("logged_in_date");
            logindate = rootView.findViewById(R.id.logindate);
            logindate.setText(date);
        }

        getElapsedTime();

        chronometer = rootView.findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime() - (hours_elapsed * 3600000 + minutes_elapsed * 60000 + seconds_elapsed * 1000));
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();

                int h = (int)(time /3600000);
                hours_elapsed = h;
                int m = (int)(time - h*3600000)/60000;
                minutes_elapsed = m;
                int s = (int)(time - h*3600000- m*60000)/1000 ;
                seconds_elapsed = s;

                String hh = h < 10 ? "0"+h+"h": h+"h";
                String mm = m < 10 ? "0"+m+"m": m+"m";
                String ss = s < 10 ? "0"+s+"s": s+"s";
                String elapsedtime = hh+":"+mm+":"+ss;
                cArg.setText(elapsedtime);
            }
        });
        chronometer.start();

        return rootView;
    }

    @Override
    public void onPause() {
        saveTime();
        super.onPause();
    }

    private void saveTime()
    {
        if(getActivity() != null) {
            MyPreference.getInstance(getActivity().getApplicationContext()).saveIntData("hours_elapsed", hours_elapsed);
            MyPreference.getInstance(getActivity().getApplicationContext()).saveIntData("minutes_elapsed", minutes_elapsed);
            MyPreference.getInstance(getActivity().getApplicationContext()).saveIntData("seconds_elapsed", seconds_elapsed);
        }
    }

    public void getElapsedTime() {
        if(getActivity() != null) {
            hours_elapsed = MyPreference.getInstance(getActivity().getApplicationContext()).getIntData("hours_elapsed");
            minutes_elapsed = MyPreference.getInstance(getActivity().getApplicationContext()).getIntData("minutes_elapsed");
            seconds_elapsed = MyPreference.getInstance(getActivity().getApplicationContext()).getIntData("seconds_elapsed");
        }
    }
}
