package com.lionel.operational;

import static com.lionel.operational.model.Constant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.lionel.operational.model.AccountModel;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;
    private TextView labelEmailError;
    private TextView labelPasswordError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.buttonLogin);
        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        labelEmailError = findViewById(R.id.labelEmailError);
        labelPasswordError = findViewById(R.id.labelPasswordError);

        // Set click listener for login button
        buttonLogin.setOnClickListener(loginClickListener);
    }

    private View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Perform login validation
            if(validateLogin()){
                // If login is valid, post login
                postLogin();
            }
        }
    };

    private boolean validateLogin() {
        boolean isValid = true;
        if (inputEmail.getText().toString().isEmpty()) {
            labelEmailError.setText(getText(R.string.email_required));
            labelEmailError.setVisibility(View.VISIBLE);
            isValid = false;
        }else{
            labelEmailError.setText("");
            labelEmailError.setVisibility(View.GONE);
        }
        if (inputPassword.getText().toString().isEmpty()) {
            labelPasswordError.setText(getText(R.string.password_required));
            labelPasswordError.setVisibility(View.VISIBLE);
            isValid = false;
        }else{
            labelPasswordError.setText("");
            labelPasswordError.setVisibility(View.GONE);
        }
        return isValid;
    }

    private void postLogin() {
        //post api login
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<ApiResponse<AccountModel>> call = apiService.login(inputEmail.getText().toString(), inputPassword.getText().toString());

        call.enqueue(new Callback<ApiResponse<AccountModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<AccountModel>> call, Response<ApiResponse<AccountModel>> response) {
                if (response.isSuccessful()) {
                    if(response.body() != null) {
                        AccountModel accountModel = response.body().getData();
                        Log.d("LoginActivity", "onResponse: " + accountModel.getName());
                        //set seeion is login true
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("userData", accountModel.toJson());
                        editor.apply();
                        // Create Intent to start MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        // Optionally, pass data to MainActivity using Intent extras
                        startActivity(intent);
                        // Optionally, finish the LoginActivity to prevent navigating back to it
                        finish();
                    }
                } else {
                    Log.d("LoginActivity", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AccountModel>> call, Throwable t) {
                Log.d("LoginActivity", "onFailure: " + t.getMessage());
            }
        });
    }
}