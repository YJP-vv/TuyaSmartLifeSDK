package com.gaincity.activator;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gaincity.activator.databinding.ActivityActivatorMainBinding;
import com.thingclips.smart.activator.plug.mesosphere.api.IThingDeviceActiveListener;
import com.thingclips.smart.activator.plug.mesosphere.ThingDeviceActivatorManager;
import com.thingclips.smart.activator.scan.qrcode.ScanManager;
import com.gaincity.activator.ActivatorUtils;
import java.util.List;


public class ActivatorMainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityActivatorMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityActivatorMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //Fragment nav control
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_activator_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        Button tvEzMode = findViewById(R.id.tvEzMode);
        Button tvApMode = findViewById(R.id.tvApMode);
        Button tv_ble = findViewById(R.id.tv_ble);
        Button tv_dual_mode = findViewById(R.id.tv_dual_mode);
        Button tv_zigBee_gateway = findViewById(R.id.tv_zigBee_gateway);
        Button tv_zigBee_subDevice = findViewById(R.id.tv_zigBee_subDevice);
        Button tv_qrcode_subDevice = findViewById(R.id.tv_qrcode_subDevice);
        Button tv_qr_code = findViewById(R.id.tv_qr_code);

        Button scanButton = findViewById(R.id.scanButton);
        Button configButton = findViewById(R.id.configButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionScan(v);
            }
        });
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionConfig(v);
            }
        });


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
    }

    public void actionConfig(View view) {
        ThingDeviceActivatorManager.INSTANCE.startDeviceActiveAction(this);

        ThingDeviceActivatorManager.INSTANCE.addListener(new IThingDeviceActiveListener() {
            @Override
            public void onDevicesAdd(List<String> list) {
                for (String id : list) {
                    Toast.makeText(ActivatorMainActivity.this, "add device success, id: "+id, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onRoomDataUpdate() {
                Toast.makeText(ActivatorMainActivity.this, "please refresh room data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOpenDevicePanel(String s) {
                Toast.makeText(ActivatorMainActivity.this, "u can open the panel of the device: " + s, Toast.LENGTH_SHORT).show();
            }
        });
    };
    public void actionScan(View view){
        ScanManager.INSTANCE.openScan(this);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_activator_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}