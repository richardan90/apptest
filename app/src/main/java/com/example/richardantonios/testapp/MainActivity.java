package com.example.richardantonios.testapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.richardantonios.testapp.fragments.ListViewFragment;
import com.example.richardantonios.testapp.fragments.TimerFragment;
import com.example.richardantonios.testapp.utils.MyPreference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean logout;
    private boolean leaveapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // save a new login date only if the user has no previous login date and has selected the remember me checkbox
        if(MyPreference.getInstance(getApplicationContext()).getStringData("logged_in_date").equals(""))
        {
            saveLoggedInDate();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPushNotification();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set desired fragment for the first time
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // The id specified here identifies which ViewGroup to append the Fragment to.
        ft.add(R.id.fragment_placeHolder, new TimerFragment()); // Timer is the default fragment
        ft.commit();
        fragment_state = true;
    }

    private void saveLoggedInDate()
    {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String logged_in_date = formatter.format(now);
        MyPreference.getInstance(getApplicationContext()).saveStringData("logged_in_date", logged_in_date);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(!fragment_state){
            replaceFragment(R.id.fragment_placeHolder, new TimerFragment());
        }
        else showLeaveAppDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            sendPushNotification();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.timer) {
            if(!fragment_state)
                replaceFragment(R.id.fragment_placeHolder, new TimerFragment());
        } else if (id == R.id.listview) {
            replaceFragment(R.id.fragment_placeHolder, new ListViewFragment());
        } else if (id == R.id.logout) {
            showLogoutDialog();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void showLogoutDialog()
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MainActivity.this);
        }
        builder.setTitle("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout = true;
                        handleLogout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showLeaveAppDialog()
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MainActivity.this);
        }
        builder.setTitle("Are you sure you want to leave the App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        leaveapp = true;
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void handleLogout()
    {
        // remove logged in date and flag
        MyPreference.getInstance(getApplicationContext()).saveBooleanData("logged_in", false);
        MyPreference.getInstance(getApplicationContext()).saveStringData("logged_in_date", "");

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    private void sendPushNotification()
    {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "channel name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Description");
            if(mNotificationManager != null)
                mNotificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.drawable.hiapphere_com_micron) // notification icon
                .setContentTitle("Hello World!") // title for notification
                .setContentText("This is a push notification.")// message for notification
                .setAutoCancel(true); // clear notification after click
        // Uncomment to enable action on notification click
        /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);*/
        if(mNotificationManager != null)
            mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    protected void onDestroy() {
        if(logout)
        {
            // remove time values
            MyPreference.getInstance(getApplicationContext()).saveIntData("hours_elapsed", 0);
            MyPreference.getInstance(getApplicationContext()).saveIntData("minutes_elapsed", 0);
            MyPreference.getInstance(getApplicationContext()).saveIntData("seconds_elapsed", 0);
        }

        if(leaveapp)
        {
            if(!MyPreference.getInstance(getApplicationContext()).getBooleanData("logged_in"))
            {
                MyPreference.getInstance(getApplicationContext()).saveStringData("logged_in_date", "");
                MyPreference.getInstance(getApplicationContext()).saveIntData("hours_elapsed", 0);
                MyPreference.getInstance(getApplicationContext()).saveIntData("minutes_elapsed", 0);
                MyPreference.getInstance(getApplicationContext()).saveIntData("seconds_elapsed", 0);
            }
        }
        super.onDestroy();
    }
}
