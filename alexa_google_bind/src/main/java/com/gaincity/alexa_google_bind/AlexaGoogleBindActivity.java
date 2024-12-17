package com.gaincity.alexa_google_bind;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.gaincity.alexa_google_bind.databinding.ActivityAlexaGoogleBindBinding;

import com.thingclips.smart.android.common.utils.L;
import com.thingclips.smart.api.MicroContext;
import com.thingclips.smart.api.router.UrlBuilder;
import com.thingclips.smart.api.router.UrlRouter;
import com.thingclips.smart.api.service.MicroServiceManager;
import com.thingclips.smart.bind.ThingSocialLoginBindManager;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.home.sdk.callback.IThingResultCallback;
import com.thingclips.smart.social.auth.manager.api.AuthorityBean;
import com.thingclips.smart.social.auth.manager.api.ResultCallback;
import com.thingclips.smart.social.auth.manager.api.SocialAuthManagerClient;

import java.util.ArrayList;

public class AlexaGoogleBindActivity extends AppCompatActivity {

    private ActivityAlexaGoogleBindBinding binding;
    private BindAdapter bindAdapter;
    private String TAG = "AlexaGoogleBindActivity";
    private static final String KEY_ACTION = "action";
    private static final String AUTHORITY_BEAN = "authority_bean";
    private static final String GO_TO_DE_AUTHORIZA = "gotoDeAuthorize";
    private static final int REQUEST_REFRESH_MANAGER_AUTHORIZATION = 151;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlexaGoogleBindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        initView();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.bt_alexa_bind).setOnClickListener(v -> {
            AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
            long homeId = 0;
            if (null != familyService) {
                homeId = familyService.getCurrentHomeId();
            }

            ThingSocialLoginBindManager.Companion.getInstance().alexaBind(AlexaGoogleBindActivity.this, String.valueOf(homeId), new IThingResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    L.d(TAG, "alexa bind onSuccess : " + result);
                    Toast.makeText(AlexaGoogleBindActivity.this, "alexa bind onSuccess.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String errorCode, String errorMessage) {
                    L.d(TAG, "alexa bind onError : errorCode: " + errorCode + " errorMessage: " + errorMessage);
                    Toast.makeText(AlexaGoogleBindActivity.this, "alexa bind onError."+ errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        findViewById(R.id.bt_google_bind).setOnClickListener(v -> {
            AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
            long homeId = 0;
            if (null != familyService) {
                homeId = familyService.getCurrentHomeId();
            }

            ThingSocialLoginBindManager.Companion.getInstance().googleBind(AlexaGoogleBindActivity.this, String.valueOf(homeId), new IThingResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    L.d(TAG, "google bind onSuccess : " + result);
                    Toast.makeText(AlexaGoogleBindActivity.this, "google bind successful.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String errorCode, String errorMessage) {
                    L.d(TAG, "google bind onError : errorCode: " + errorCode + " errorMessage: " + errorMessage);
                    Toast.makeText(AlexaGoogleBindActivity.this, "google bind onError."+ errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        findViewById(R.id.bt_get_bind_list).setOnClickListener(v -> getBindSkillList());
    }

    private void initView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_bind_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        bindAdapter = new BindAdapter();
        mRecyclerView.setAdapter(bindAdapter);
        bindAdapter.setOnItemClickListener((authorityBean, var2) -> {
            try {
                String route = "SocialAuthManagerAppAction";
                UrlBuilder mBuilder = UrlRouter.makeBuilder(AlexaGoogleBindActivity.this, route);
                Bundle newBundle = new Bundle();
                newBundle.putString(KEY_ACTION, GO_TO_DE_AUTHORIZA);
                newBundle.putParcelable(AUTHORITY_BEAN, authorityBean);
                mBuilder.setRequestCode(REQUEST_REFRESH_MANAGER_AUTHORIZATION);
                mBuilder.putExtras(newBundle);
                UrlRouter.execute(mBuilder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void onItemClick(Context context, AuthorityBean authorityBean) {
        UrlBuilder mBuilder = UrlRouter.makeBuilder(context, "SocialAuthManagerAppAction");
        Bundle newBundle = new Bundle();
        newBundle.putString("action", "gotoDeAuthorize");
        newBundle.putParcelable("authority_bean", authorityBean);
        mBuilder.setRequestCode(REQUEST_REFRESH_MANAGER_AUTHORIZATION);
        mBuilder.putExtras(newBundle);
        UrlRouter.execute(mBuilder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_REFRESH_MANAGER_AUTHORIZATION:
                if (RESULT_OK == resultCode) {
                    getBindSkillList();
                }
                break;
            default:
                break;
        }
    }

    public void getBindSkillList() {
        SocialAuthManagerClient.INSTANCE.getInstance(MicroContext.getApplication())
                .getAuthorityPlatforms(new ResultCallback<ArrayList<AuthorityBean>>() {
                    @Override
                    public void onSuccess(ArrayList<AuthorityBean> authorityBeans) {
                        bindAdapter.setData(authorityBeans);
                    }

                    @Override
                    public void onFailure(@Nullable String s, @Nullable String s1) {
                        bindAdapter.setData(null);
                    }
                });
    }
}




//package com.gaincity.alexa_google_bind;
//
//import android.os.Bundle;
//
//import com.google.android.material.snackbar.Snackbar;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.view.View;
//
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//
//import com.gaincity.alexa_google_bind.databinding.ActivityAlexaGoogleBindBinding;
//
//public class AlexaGoogleBindActivity extends AppCompatActivity {
//
//    private AppBarConfiguration appBarConfiguration;
//    private ActivityAlexaGoogleBindBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityAlexaGoogleBindBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.toolbar);
//
////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_alexa_google_bind);
////        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
////        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_alexa_google_bind);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//}
