package com.lionel.operational;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.buttonLogin);

        // Set click listener for login button
        buttonLogin.setOnClickListener(loginClickListener);
    }

    private View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Perform login validation
            if (validateLogin()) {
                // Create Intent to start MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                // Optionally, pass data to MainActivity using Intent extras
                startActivity(intent);
                // Optionally, finish the LoginActivity to prevent navigating back to it
                finish();
            }
        }
    };

    private boolean validateLogin() {
        //set seeion is login true
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();

        // Perform login validation, return true if valid, false otherwise
        return true; // Placeholder, replace with actual validation logic
    }
}