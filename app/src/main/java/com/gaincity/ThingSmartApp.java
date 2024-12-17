package com.gaincity;
import android.app.Application;

import com.gaincity.gc_smart_life_app.User_LoginActivity;
import com.thingclips.smart.api.service.RedirectService;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.home.sdk.ThingHomeSdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.thingclips.smart.thingpackconfig.PackConfig;
import com.thingclips.smart.bizbundle.initializer.BizBundleInitializer;
import com.thingclips.smart.api.router.UrlBuilder;
import com.thingclips.smart.api.service.RouteEventListener;
import com.thingclips.smart.api.service.ServiceEventListener;
import com.thingclips.smart.sdk.api.INeedLoginListener;


import androidx.multidex.MultiDex;

import com.thingclips.smart.api.MicroContext;
import com.thingclips.smart.api.router.UrlBuilder;
import com.thingclips.smart.api.service.RedirectService;
import com.thingclips.smart.api.service.RouteEventListener;
import com.thingclips.smart.api.service.ServiceEventListener;
import com.thingclips.smart.bizbundle.initializer.BizBundleInitializer;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.thingpackconfig.PackConfig;



public class ThingSmartApp extends Application {
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        ThingHomeSdk.init(this);
//
//    }
private static final String TAG = "ThingSmartApp";
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            // Retrieve the meta-data values from the AndroidManifest.xml
            ApplicationInfo appInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String appKey = appInfo.metaData.getString("THING_SMART_APPKEY");
            String appSecret = appInfo.metaData.getString("THING_SMART_SECRET");

            // Log the keys for debugging purposes (optional)
            Log.d(TAG, "AppKey: " + appKey);
            Log.d(TAG, "AppSecret: " + appSecret);

            // Initialize the ThingHomeSdk with the retrieved keys
            ThingHomeSdk.init(this, appKey, appSecret);
            Log.d(TAG, "this: " + this);

            // Initialize BizBundle
            BizBundleInitializer.init(this, new RouteEventListener() {
                @Override
                public void onFaild(int errorCode, UrlBuilder urlBuilder) {
                    Log.e(TAG, "Unimplemented route: " + urlBuilder.target + ", Params: " + urlBuilder.params);
                }
            }, new ServiceEventListener() {
                @Override
                public void onFaild(String serviceName) {
                    Log.e(TAG, "Unimplemented service: " + serviceName);
                }
            });

            // Register family service (if required for your app)
            BizBundleInitializer.registerService(AbsBizBundleFamilyService.class, new BizBundleFamilyServiceImpl());


            // Set up a URL interceptor for custom route handling
            RedirectService redirectService = MicroContext.getServiceManager()
                    .findServiceByInterface(RedirectService.class.getName());
            if (redirectService != null) {
                redirectService.registerUrlInterceptor(new RedirectService.UrlInterceptor() {
                    @Override
                    public void forUrlBuilder(UrlBuilder urlBuilder, RedirectService.InterceptorCallback interceptorCallback) {
                        if ("panelAction".equals(urlBuilder.target) && "gotoPanelMore".equals(urlBuilder.params.getString("action"))) {
                            interceptorCallback.interceptor("interceptor");
                            Log.e(TAG, "Intercepted URL: " + urlBuilder.params);
                        } else {
                            interceptorCallback.onContinue(urlBuilder);
                        }
                    }
                });
            } else {
                Log.w(TAG, "RedirectService is not available.");
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

//        ThingHomeSdk.setOnNeedLoginListener(context -> {
//            Toast.makeText(ThingSmartApp.this, "Login status expired, please login again", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context, User_LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
//            context.startActivity(intent);
//        });
        ThingHomeSdk.setOnNeedLoginListener(new INeedLoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                Toast.makeText(ThingSmartApp.this, "Login status expired, please login again", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, User_LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // Enable multidex for large applications
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        // Clean up resources when the application is terminated
        ThingHomeSdk.onDestroy();
    }
}

