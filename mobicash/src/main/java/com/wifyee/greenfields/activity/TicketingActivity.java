package com.wifyee.greenfields.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.fragments.TicketingFragment;

public class TicketingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing);


        if (savedInstanceState == null) {

            Fragment fragment = new TicketingFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, TicketingFragment.TAG)
                    .commit();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

}
