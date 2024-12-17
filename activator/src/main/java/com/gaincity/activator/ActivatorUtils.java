package com.gaincity.activator;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.thingclips.smart.activator.plug.mesosphere.api.IThingDeviceActiveListener;
import com.thingclips.smart.activator.plug.mesosphere.ThingDeviceActivatorManager;
import com.thingclips.smart.activator.scan.qrcode.ScanManager;

import java.util.List;

public class ActivatorUtils {

    // Utility method to start device activation
    public static void addDeviceActivation(Activity activity) {
        // Start the activation action
        ThingDeviceActivatorManager.INSTANCE.startDeviceActiveAction(activity);

        // Add a listener to handle activation events
        ThingDeviceActivatorManager.INSTANCE.addListener(new IThingDeviceActiveListener() {
            @Override
            public void onDevicesAdd(List<String> list) {
                for (String id : list) {
                    Toast.makeText(activity, "Device added successfully, ID: " + id, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onRoomDataUpdate() {
                Toast.makeText(activity, "Please refresh room data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOpenDevicePanel(String deviceId) {
                Toast.makeText(activity, "You can open the panel of the device: " + deviceId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void scanActivation(Activity activity) {
        ScanManager.INSTANCE.openScan(activity);
    }
}

