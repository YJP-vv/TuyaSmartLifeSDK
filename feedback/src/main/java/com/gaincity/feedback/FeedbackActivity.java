package com.gaincity.feedback;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gaincity.feedback.databinding.ActivityFeedbackBinding;
import com.thingclips.smart.api.MicroContext;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.android.user.api.IQurryDomainCallback;
import com.thingclips.smart.feedback.api.FeedbackService;
import com.thingclips.smart.api.router.UrlRouter;

public class FeedbackActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityFeedbackBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.e("FeedbackActivity", "in the feedback page "  + ", " );
        Toast.makeText(FeedbackActivity.this, "reached FeedbackActivity ........" , Toast.LENGTH_SHORT).show();
        setSupportActionBar(binding.toolbar);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_feedback);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.feedbackServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThingHomeSdk.getUserInstance().queryDomainByBizCodeAndKey("help_center", "main_page", new IQurryDomainCallback() {
                    @Override
                    public void onSuccess(String domain) {
                        FeedbackService feedbackService = MicroContext.findServiceByInterface(FeedbackService.class.getName());
                        if (feedbackService != null) {
                            feedbackService.jumpToWebHelpPage(FeedbackActivity.this);
                        }
                    }

                    @Override
                    public void onError(String code, String error) {
                        return;
                    }
                });
            }
        });
        binding.feedbackRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThingHomeSdk.getUserInstance().queryDomainByBizCodeAndKey("help_center", "main_page", new IQurryDomainCallback() {
                    @Override
                    public void onSuccess(String domain) {
                        UrlRouter.execute(UrlRouter.makeBuilder(FeedbackActivity.this, "helpCenter"));
                    }

                    @Override
                    public void onError(String code, String error) {
                        return;
                    }
                });
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_feedback);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}