package com.gaincity.gc_smart_life_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.android.user.api.IRegisterCallback;
import com.thingclips.smart.sdk.api.IResultCallback;
import com.thingclips.smart.android.user.api.ILoginCallback;
import com.thingclips.smart.android.user.api.ILogoutCallback;
import com.thingclips.smart.bizbundle.initializer.BizBundleInitializer;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if user is logged in
        if (ThingHomeSdk.getUserInstance().isLogin()) {

            // Navigate to HomeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Close MainActivity to prevent going back
        } else {

            // User is not logged in, show the main activity
            setContentView(R.layout.activity_main);
            setupUI();
        }


    }

    private void setupUI() {
        Button toLoginPageButton = findViewById(R.id.toLoginPageButton);
        Button toRegisterPageButton = findViewById(R.id.toRegisterPageButton);

        toLoginPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                Intent intent = new Intent(MainActivity.this, User_LoginActivity.class);
                startActivity(intent);

            }
        });
        toRegisterPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                Intent intent = new Intent(MainActivity.this, User_RegisterActivity.class);
                startActivity(intent);

            }
        });
    }




}
