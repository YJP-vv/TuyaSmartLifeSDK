package com.gaincity.gc_smart_life_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.hbb20.CountryCodePicker;
import com.thingclips.smart.android.user.api.IResetPasswordCallback;
import com.thingclips.smart.sdk.api.IResultCallback;
import com.gaincity.gc_smart_life_app.databinding.ActivityResetPasswordBinding;
import com.thingclips.smart.home.sdk.ThingHomeSdk;


public class ResetPasswordActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityResetPasswordBinding binding;
    CountryCodePicker codePicker;
    private String countryCode;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.resetPasswordToolbar);

        Toolbar resetPasswordToolbar;
        MaterialToolbar resetPWDToolbar = findViewById(R.id.resetPasswordToolbar);
        resetPWDToolbar.setNavigationOnClickListener(v -> {
            // Navigate back to the previous activity
            finish();
        });

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_reset_password);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        resetPWDToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_new_24);
        resetPWDToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        findViewById(R.id.verifyButton).setBackgroundColor(ContextCompat.getColor(this, R.drawable.bg_text_bts));
        findViewById(R.id.verifyButton).setBackgroundResource(R.drawable.bg_text_bts);

        codePicker=findViewById(R.id.country_code);

        countryCode=codePicker.getSelectedCountryCode();

        EditText email= findViewById(R.id.email);
        EditText verificationCode= findViewById(R.id.verification_code);
        EditText resetPasswordInput_1= findViewById(R.id.resetPasswordInput_1);
        EditText resetPasswordInput_2= findViewById(R.id.resetPasswordInput_2);
        Button verifyButton = findViewById(R.id.verifyButton);
        Button reset_password = findViewById(R.id.reset_password);


        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAccount=email.getText().toString().trim();
                if (emailAccount.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter the Email.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailAccount).matches()) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter a valid Email address.", Toast.LENGTH_LONG).show();
                    return;
                }
                getVerificationCode(emailAccount,countryCode);
            }
        });

        reset_password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String emailAccount=email.getText().toString().trim();
                String verifyCode=verificationCode.getText().toString().trim();
                String password1=resetPasswordInput_1.getText().toString().trim();
                String password2=resetPasswordInput_2.getText().toString().trim();
                if (verifyCode.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter the verification code.", Toast.LENGTH_LONG).show();
                    return;
                } else if (password1.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter both passwords.", Toast.LENGTH_LONG).show();
                    return;
                }else if (!password1.equals(password2)) {
                    Toast.makeText(ResetPasswordActivity.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
                    return;
                }
                resetPassword(emailAccount,countryCode,verifyCode,password1);
            }
        });

    }



    private void getVerificationCode(String email, String countryCode) {
        ThingHomeSdk.getUserInstance().sendVerifyCodeWithUserName(email, "", countryCode, 3, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                Toast.makeText(ResetPasswordActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(ResetPasswordActivity.this, "Verification code returned successfully.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resetPassword(String email, String countryCode, String verifyCode, String password) {
        ThingHomeSdk.getUserInstance().resetEmailPassword(countryCode, email, verifyCode, password, new IResetPasswordCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(ResetPasswordActivity.this, "Password reset successfully.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ResetPasswordActivity.this, User_LoginActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onError(String code, String error) {
                Toast.makeText(ResetPasswordActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_reset_password);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}