package com.gaincity.gc_smart_life_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.thingclips.smart.bizbundle.initializer.BizBundleInitializer;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.sdk.api.IResultCallback;
import com.thingclips.smart.android.user.api.ILogoutCallback;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_me, container, false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        // Find the logout button and set a click listener
        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logoutUser());

        // Find the feedback button and set a click listener
        Button feedbackButton = view.findViewById(R.id.feedback_qa_btn);
        feedbackButton.setOnClickListener(v -> {
            openFeedbackActivity();
        });

        Button thirdPartyLink = view.findViewById(R.id.thirdPartyLink);
        thirdPartyLink.setOnClickListener(v->{
            openAlexaGoogleBindActivity();
        });

        return view;
    }

    private void logoutUser() {
        // Logout user
        ThingHomeSdk.getUserInstance().logout(new ILogoutCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Logout successful.", Toast.LENGTH_SHORT).show();

                // Call BizBundleInitializer.onLogout after successful logout
                BizBundleInitializer.onLogout(getContext());

                if (getContext() != null && getActivity() != null) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                Log.i("MainActivity", "BizBundle logout initialized.");
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                Toast.makeText(getContext(), "Logout failed: " + errorMsg, Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Logout error: " + errorCode + ", " + errorMsg);
            }
        });
    }

    private void openFeedbackActivity() {
        try {
            Toast.makeText(getContext(), "go to FeedbackActivity " , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClassName(requireContext(), "com.gaincity.feedback.FeedbackActivity");
            startActivity(intent);
        } catch (Exception e) {
            Log.e("MeFragment", "Error opening FeedbackActivity", e);
            Toast.makeText(getContext(), "Unable to open Feedback page", Toast.LENGTH_SHORT).show();
        }
    }
    private void openAlexaGoogleBindActivity() {
        try {
            Toast.makeText(getContext(), "go to Alexa Google bind activity " , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClassName(requireContext(), "com.gaincity.alexa_google_bind.AlexaGoogleBindActivity");
            startActivity(intent);
        } catch (Exception e) {
            Log.e("MeFragment", "Error opening AlexaGoogleBindActivity", e);
            Toast.makeText(getContext(), "Unable to open AlexaGoogleBindActivity page", Toast.LENGTH_SHORT).show();
        }
    }


}

