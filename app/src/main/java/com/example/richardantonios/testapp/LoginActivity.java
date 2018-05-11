package com.example.richardantonios.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.richardantonios.testapp.utils.MyPreference;

public class LoginActivity extends BaseActivity {

    private EditText username;
    private EditText password;
    private CheckBox rememberme;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rememberme = findViewById(R.id.rememberme);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernametxt = username.getText().toString();
                String passwordtxt = password.getText().toString();

                handleLogin(usernametxt, passwordtxt, rememberme.isChecked());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MyPreference.getInstance(getApplicationContext()).getBooleanData("logged_in"))
        {
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
