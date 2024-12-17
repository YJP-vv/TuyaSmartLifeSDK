package com.gaincity.gc_smart_life_app;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gaincity.gc_smart_life_app.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);
//        Log.d("HomeActivity", "NavHostFragment ID: " + R.id.nav_host_fragment_content_home_page);
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
////        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
////        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        Log.d("HomeActivity", "navController : " + navController);
//        // Define top-level destinations
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.btm_nav_home, R.id.btm_nav_scene, R.id.btm_nav_mall, R.id.btm_nav_me)
//                .build();
//        Log.d("HomeActivity", "appBarConfiguration : " + appBarConfiguration);
//        Log.d("HomeActivity", "binding.bottomNavigationView : " + binding.bottomNavigationView);
//        // Link NavController to ActionBar and BottomNavigationView
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);




        // Get NavHostFragment instead of NavController directly
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_home_page);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // Setup ActionBar and BottomNavigationView with NavController
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.btm_nav_home, R.id.btm_nav_scene, R.id.btm_nav_mall, R.id.btm_nav_me)
                    .build();

//            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

            // Customize Toolbar behavior
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                // Remove title for all destinations
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayShowTitleEnabled(false); // Set empty title
                }
            });
        } else {
            Log.e("HomeActivity", "NavHostFragment is null!");
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}