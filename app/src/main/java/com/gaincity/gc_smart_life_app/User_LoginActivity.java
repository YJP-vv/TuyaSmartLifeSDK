package com.gaincity.gc_smart_life_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.StateListDrawableCompat;

import com.gaincity.gc_smart_life_app.databinding.ActivityResetPasswordBinding;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.android.user.api.ILoginCallback;
import com.hbb20.CountryCodePicker;

public class User_LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    CountryCodePicker codePicker;
    private String countryCode; // Default country code



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);


//        StateListDrawableCompat ActivityUser_LoginBinding;
//        binding = ActivityUser_LoginBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
//
//        com.gaincity.gc_smart_life_app.databinding.ActivityUser_LoginBinding binding = com.gaincity.gc_smart_life_app.databinding.ActivityUser_LoginBinding.inflate(getLayoutInflater());
//        setSupportActionBar(binding.loginToolbar);

        codePicker=findViewById(R.id.login_country_code);

        countryCode=codePicker.getSelectedCountryCode();
        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.loginPasswordInput);
        Button loginButton = findViewById(R.id.loginButton);
        TextView resetPassword = findViewById(R.id.reset_password);

        // Login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(User_LoginActivity.this, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(email, password);
            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password) {
        // Login user with email and password
        ThingHomeSdk.getUserInstance().loginWithEmail(countryCode, email, password, new ILoginCallback() {
            @Override
            public void onSuccess(com.thingclips.smart.android.user.bean.User user) {
                Toast.makeText(User_LoginActivity.this, "Login successful: " + user.getUsername(), Toast.LENGTH_SHORT).show();
                // Navigate to the main application page if needed
                Intent intent = new Intent(User_LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(User_LoginActivity.this, "Login failed: " + error+countryCode+email+password, Toast.LENGTH_LONG).show();
                Log.e("LoginError", "Code: " + code + ", Error: " + error+ ", data: "+countryCode+email+password);
            }
        });
    }
}
