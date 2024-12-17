package com.gaincity.family;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gaincity.family.databinding.ActivityFamilyManageBinding;


import com.thingclips.smart.api.router.UrlRouter;
import com.thingclips.smart.family.main.listener.HomeInviteListener;
import com.thingclips.smart.home.sdk.ThingHomeSdk;

public class FamilyManageActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityFamilyManageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFamilyManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_family_manage);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //family manage
        initializeInvitationListener();

        binding.btnFamilyStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UrlRouter.execute(UrlRouter.makeBuilder(FamilyManageActivity.this, "family_manage"));
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

    private final HomeInviteListener homeInviteListener = new HomeInviteListener() {
        @Override
        public void onHomeInvite(long homeId, String homeName) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FamilyManageActivity.this);
            builder.setTitle(getString(R.string.family_go_to_family_manage_page))
                    .setMessage(String.format(getString(R.string.family_invitation_message), homeName))
                    .setPositiveButton(getString(R.string.family_accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // accept invitation
                        }
                    })
                    .setNegativeButton(getString(R.string.family_reject), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // reject invitation
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    };
    private void initializeInvitationListener() {
        ThingHomeSdk.getHomeManagerInstance().registerThingHomeChangeListener(homeInviteListener);
    }

    @Override
    protected void onDestroy() {
        ThingHomeSdk.getHomeManagerInstance().unRegisterThingHomeChangeListener(homeInviteListener);
        super.onDestroy();
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_family_manage);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}