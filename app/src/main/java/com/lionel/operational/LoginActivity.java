package com.lionel.operational;

import static com.lionel.operational.model.Constant.PREFERENCES_KEY;
import static com.lionel.operational.model.Constant.USERDATA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.lionel.operational.model.AccountModel;
import com.lionel.operational.model.ApiClient;
import com.lionel.operational.model.ApiResponse;
import com.lionel.operational.model.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;
    private TextView labelEmailError;
    private TextView labelPasswordError;
    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.buttonLogin);
        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        labelEmailError = findViewById(R.id.labelEmailError);
        labelPasswordError = findViewById(R.id.labelPasswordError);
        tvVersion = findViewById(R.id.tvVersion);

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
        ApiService apiService = ApiClient.getInstant(getApplicationContext()).create(ApiService.class);

        Call<ApiResponse<AccountModel>> call = apiService.login(inputEmail.getText().toString(), inputPassword.getText().toString());

        call.enqueue(new Callback<ApiResponse<AccountModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<AccountModel>> call, Response<ApiResponse<AccountModel>> response) {
                if (response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.body().isSuccess()){
                            if(response.body().getData() instanceof AccountModel){
                                AccountModel accountModel = response.body().getData();
                                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(USERDATA, accountModel.toJson());
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                //show toast error
                                Toast.makeText(LoginActivity.this, "Invalid REsponse", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            //show toast error
                            Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.d("LoginActivity", "onResponse: " + response.message());
                    //show toast error
                    Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AccountModel>> call, Throwable t) {
                Log.d("LoginActivity", "onFailure: " + t.getMessage());
                //show toast error
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}