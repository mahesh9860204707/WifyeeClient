package com.wifyee.greenfields.activity;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;

public class PayCredit extends AppCompatActivity {

    TextView totalCredit,paid,amount;
    Toolbar mToolbar;
    String totalAmt,paidAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_credit);

        mToolbar = findViewById(R.id.mtoolbar);
        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        totalCredit = findViewById(R.id.total_credit);
        paid = findViewById(R.id.paid);
        amount = findViewById(R.id.amount);
        TextView txtTotalCredit = findViewById(R.id.txt_total_credit);
        TextView txtPaid = findViewById(R.id.txt_paid);
        TextView txtSignRupee = findViewById(R.id.sign_rupee);
        TextView txtPayment = findViewById(R.id.txt_payment);
        RadioButton wallet = findViewById(R.id.rb_wallet);
        RadioButton onlinePayment = findViewById(R.id.rb_netbanking);
        Button pay =  findViewById(R.id.pay);

        totalAmt = getIntent().getStringExtra("total_amt");
        paidAmt = getIntent().getStringExtra("paid_amt");

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
        txtTotalCredit.setTypeface(Fonts.getRegular(this));
        totalCredit.setTypeface(Fonts.getRegular(this));
        txtPaid.setTypeface(Fonts.getRegular(this));
        paid.setTypeface(Fonts.getRegular(this));
        //txtSignRupee.setTypeface(Fonts.getSemiBold(this));
        amount.setTypeface(Fonts.getRegular(this));
        txtPayment.setTypeface(Fonts.getRegular(this));
        wallet.setTypeface(Fonts.getRegular(this));
        onlinePayment.setTypeface(Fonts.getRegular(this));
        pay.setTypeface(Fonts.getSemiBold(this));

        totalCredit.setText("₹"+totalAmt);
        paid.setText("₹"+paidAmt);

    }
}
