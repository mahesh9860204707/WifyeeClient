package com.wifyee.greenfields.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    ImageView imageView;
    ObjectAnimator objectanimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.scooter);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int windowWidth = size.x;
        //int windowHeight = size.y;
        objectanimator = ObjectAnimator.ofFloat(imageView,"x",windowWidth);
        objectanimator.setDuration(4000);
        objectanimator.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LocalPreferenceUtility.getUserMobileNumber(getApplicationContext()).isEmpty()
                        || LocalPreferenceUtility.getUserPassCode(getApplicationContext()).isEmpty()
                        || LocalPreferenceUtility.getDeviceMacID(getApplicationContext()).isEmpty()) {
                    // start login or sign up activity
                    startActivity(IntentFactory.createUserLoginActivity(getApplicationContext()));
                    /*Intent intent = new Intent(getApplicationContext(),Aaa.class);
                    startActivity(intent);*/
                } else {
                    /*Toast.makeText(getApplicationContext(),
                            "mob "+LocalPreferenceUtility.getUserMobileNumber(getApplicationContext())+
                                    "pwd "+LocalPreferenceUtility.getUserPassCode(getApplicationContext())+
                                    "mac "+LocalPreferenceUtility.getDeviceMacID(getApplicationContext()),Toast.LENGTH_LONG).show();*/
                    //start dashboard
                    startActivity(IntentFactory.createDashboardActivity(getApplicationContext()));
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
