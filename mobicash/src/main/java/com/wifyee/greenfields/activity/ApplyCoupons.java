package com.wifyee.greenfields.activity;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;

public class ApplyCoupons extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_coupons);

        mToolbar = findViewById(R.id.toolbar);
        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        TextView txtAvailCoupons = findViewById(R.id.txt_avail_coupons);
        coupon = findViewById(R.id.coupon);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.secondaryPrimary), PorterDuff.Mode.SRC_ATOP);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }

        toolBarTitle.setTypeface(Fonts.getSemiBold(this));
        txtAvailCoupons.setTypeface(Fonts.getSemiBold(this));
        coupon.setTypeface(Fonts.getSemiBold(this));



    }
}
