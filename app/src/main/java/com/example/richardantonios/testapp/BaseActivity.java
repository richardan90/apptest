package com.example.richardantonios.testapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.richardantonios.testapp.fragments.TimerFragment;
import com.example.richardantonios.testapp.sqlite.SampleSQLiteDBHelper;
import com.example.richardantonios.testapp.utils.MyPreference;

import es.dmoral.toasty.Toasty;

public abstract class BaseActivity extends AppCompatActivity {

    public boolean fragment_state;

    void handleLogin(String username, String password, boolean rememberme)
    {
        if (checkUserCredentials(username, password)) {
            MyPreference.getInstance(getApplicationContext()).saveBooleanData("logged_in", rememberme);
            showSuccessToastMessage("Logged in Successfully");
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }
        else showErrorToastMessage("Wrong Credentials!");
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment The fragment to be added.
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        fragment_state = fragment instanceof TimerFragment;

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    boolean checkUserCredentials(String username, String password)
    {
        SQLiteDatabase sqLiteDatabase = new SampleSQLiteDBHelper(this).getReadableDatabase();

        String[] projection = {
                SampleSQLiteDBHelper.USER_NAME,
                SampleSQLiteDBHelper.USER_PASSWORD
        };

        String selection = SampleSQLiteDBHelper.USER_NAME + " = ? AND " + SampleSQLiteDBHelper.USER_PASSWORD + " = ?";

        String[] selectionArgs = {username, password};

        Cursor cursor = sqLiteDatabase.query(
                SampleSQLiteDBHelper.TABLE_USER_INFO,   // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );

        if (cursor.moveToFirst()) {
            cursor.close();
            sqLiteDatabase.close();
            return true;
        }

        cursor.close();
        sqLiteDatabase.close();

        return false;
    }

    /**
     * Shows a {@link android.widget.Toast} success message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showSuccessToastMessage(String message) {
        Toasty.success(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows a {@link android.widget.Toast} error message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showErrorToastMessage(String message) {
        Toasty.error(this, message, Toast.LENGTH_LONG).show();
    }

}
