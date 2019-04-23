package com.wifyee.greenfields.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;

public class SplashActivity extends BaseActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if (LocalPreferenceUtility.getUserMobileNumber(getApplicationContext()).isEmpty()
                        || LocalPreferenceUtility.getUserPassCode(getApplicationContext()).isEmpty()
                        || LocalPreferenceUtility.getDeviceMacID(getApplicationContext()).isEmpty()) {
                    // start login or sign up activity
                    startActivity(IntentFactory.createUserLoginActivity(getApplicationContext()));
                } else {
                    //start dashboard
                    startActivity(IntentFactory.createDashboardActivity(getApplicationContext()));
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
