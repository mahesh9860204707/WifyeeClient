package com.wifyee.greenfields.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.qrcode.DecoderActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoosePaymentWalletActivity extends AppCompatActivity implements View.OnClickListener
{
    @BindView(R.id.et_mobile_number)
    EditText etMobileNumber;

    @BindView(R.id.qr_code)
    Button qrCodeButton;

    @BindView(R.id.continue_btn)
    Button continueButton;

    private Toolbar mToolbar;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment_wallet);
        ButterKnife.bind(this);
        continueButton.setOnClickListener(this);
        qrCodeButton.setOnClickListener(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        back= (ImageButton) findViewById(R.id.toolbar_back);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

        if(v==qrCodeButton) {
            startActivity(new Intent(this, DecoderActivity.class));
        }
        if(v==continueButton)
        {
            if(etMobileNumber.getText().toString()==null||etMobileNumber.getText().toString().length()==0)
            {
                Toast.makeText(this,"Please fill mobile number",Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, ClientToMerchantPaymnetActivity.class).putExtra("mobileNumber",etMobileNumber.getText().toString()));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
    }
}
