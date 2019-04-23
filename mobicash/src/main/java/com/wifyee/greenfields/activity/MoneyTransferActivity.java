package com.wifyee.greenfields.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.fragments.MoneyTransferFragment;

public class MoneyTransferActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer);

        if (savedInstanceState == null) {

            Fragment fragment = new MoneyTransferFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, MoneyTransferFragment.TAG)
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

}