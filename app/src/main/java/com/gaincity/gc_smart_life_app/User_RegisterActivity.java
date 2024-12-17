package com.gaincity.gc_smart_life_app;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.smart.android.user.api.ILoginCallback;
import com.thingclips.smart.android.user.api.ILogoutCallback;
import com.thingclips.smart.android.user.api.IRegisterCallback;
import com.thingclips.smart.bizbundle.initializer.BizBundleInitializer;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.sdk.api.IResultCallback;

import com.hbb20.CountryCodePicker;


public class User_RegisterActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText verificationCodeInput;
    private Button actionButton;
    private Button verifyButton;
    private EditText registerPasswordInput_1;
    private EditText registerPasswordInput_2;
    private String email;
    private String countryCode; // User's country code
    private String password = "your_password"; // Example password
    private String verificationCode;
    CountryCodePicker codePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);

        codePicker=findViewById(R.id.country_code);

        countryCode=codePicker.getSelectedCountryCode();

        // getting the country name
//        String country_name=codePicker.getSelectedCountryName();
        // getting the country name code
//        String country_namecode=codePicker.getSelectedCountryNameCode();

        // Initialize views
        emailInput = findViewById(R.id.registerEmailInput);
        verificationCodeInput = findViewById(R.id.verificationCodeInput);
        actionButton = findViewById(R.id.actionButton);
        verifyButton = findViewById(R.id.verifyButton);
        registerPasswordInput_1 = findViewById(R.id.registerPasswordInput_1);
        registerPasswordInput_2 = findViewById(R.id.registerPasswordInput_2);
        // Initially disable the verification code input field
        verificationCodeInput.setEnabled(false);

        // Action button click listener (Send verification code)
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailInput.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(User_RegisterActivity.this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendVerificationCode();
            }
        });

        // Verify button click listener (Register user)
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationCode = verificationCodeInput.getText().toString();
                if (verificationCode.isEmpty()) {
                    Toast.makeText(User_RegisterActivity.this, "Please enter the verification code.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (registerPasswordInput_1.getText().toString().isEmpty() || registerPasswordInput_2.getText().toString().isEmpty()) {
                    Toast.makeText(User_RegisterActivity.this, "Please enter both passwords.", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!registerPasswordInput_1.getText().toString().equals(registerPasswordInput_2.getText().toString())) {
                    Toast.makeText(User_RegisterActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser();
            }
        });


    }

    private void sendVerificationCode() {
        // Send the verification code to the email
        ThingHomeSdk.getUserInstance().sendVerifyCodeWithUserName(email, "", countryCode, 1, new IResultCallback() {
            @Override
            public void onSuccess() {
                // Enable the verification code input field after sending the code
                verificationCodeInput.setEnabled(true);
                Toast.makeText(User_RegisterActivity.this, "Verification code sent successfully." + countryCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(User_RegisterActivity.this, "Error sending verification code: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser() {
        // Register the user with email and verification code
        ThingHomeSdk.getUserInstance().registerAccountWithEmail(countryCode, email, registerPasswordInput_1.getText().toString(), verificationCode, new IRegisterCallback() {
            @Override
            public void onSuccess(com.thingclips.smart.android.user.bean.User user) {
                Toast.makeText(User_RegisterActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                // Navigate to LoginActivity
                Intent intent = new Intent(User_RegisterActivity.this, User_LoginActivity.class);
                startActivity(intent);

            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(User_RegisterActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        // Login user with email and password
        ThingHomeSdk.getUserInstance().loginWithEmail(countryCode, email, password, new ILoginCallback() {
            @Override
            public void onSuccess(com.thingclips.smart.android.user.bean.User user) {
                Toast.makeText(User_RegisterActivity.this, "Login successful: " + user.getUsername(), Toast.LENGTH_SHORT).show();

                // Call BizBundleInitializer.onLogin after successful login
                BizBundleInitializer.onLogin();
                Log.i("MainActivity", "BizBundle login initialized.");
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(User_RegisterActivity.this, "Login failed: " + error, Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Login error: " + code + ", " + error);
            }
        });
    }

    private void logoutUser() {
        // Logout user
        ThingHomeSdk.getUserInstance().logout(new ILogoutCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(User_RegisterActivity.this, "Logout successful.", Toast.LENGTH_SHORT).show();

                // Call BizBundleInitializer.onLogout after successful logout
                BizBundleInitializer.onLogout(User_RegisterActivity.this);
                Log.i("MainActivity", "BizBundle logout initialized.");
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                Toast.makeText(User_RegisterActivity.this, "Logout failed: " + errorMsg, Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Logout error: " + errorCode + ", " + errorMsg);
            }
        });
    }
}
