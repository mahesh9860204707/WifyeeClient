package com.wifyee.greenfields.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wifyee.greenfields.R;

public class SuccessTransaction extends AppCompatActivity {

    private ImageView img;
    private LinearLayout ll;
    private TextView transactionStatus,transactionTime,transactionID,rechargeNo,amount;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_transaction);

        img = findViewById(R.id.img);
        ll = findViewById(R.id.ll);
        transactionStatus = findViewById(R.id.transaction_status);
        transactionTime = findViewById(R.id.transaction_date);
        transactionID = findViewById(R.id.transaction_id);
        rechargeNo = findViewById(R.id.recharge_no);
        amount = findViewById(R.id.amount);
        btnDone = findViewById(R.id.btn_done);

        String txn_status = getIntent().getStringExtra("txn_status");
        String txn_id = getIntent().getStringExtra("txn_id");
        String txn_date = getIntent().getStringExtra("txn_date");
        String number = getIntent().getStringExtra("number");
        String amt = getIntent().getStringExtra("amount");

        transactionID.setText(txn_id);
        transactionTime.setText(txn_date);
        rechargeNo.setText(number);
        amount.setText(amt);

        if (txn_status.equalsIgnoreCase("success")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.successfulStatusBar));
            }
            ll.setBackgroundColor(getResources().getColor(R.color.successful_transaction));
            transactionStatus.setText("Transaction Successful");

            Glide.with(this)
                    .asGif()
                    .load(R.raw.success)
                    //.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(img);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.unsuccessfulStatusBar));
            }
            ll.setBackgroundColor(getResources().getColor(R.color.unsuccessful_transaction));
            transactionStatus.setText("Transaction Failed");

            Glide.with(this)
                    .asGif()
                    .load(R.raw.unsuccessful)
                    //.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(img);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MobicashDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
