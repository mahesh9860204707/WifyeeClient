package com.wifyee.greenfields.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.qrcode.DecoderActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoosePaymentWalletActivity extends AppCompatActivity implements View.OnClickListener
{
    @BindView(R.id.et_mobile_number)
    EditText etMobileNumber;

    @BindView(R.id.qr_code)
    ImageView qrCodeButton;

    @BindView(R.id.continue_btn)
    RelativeLayout continueButton;

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
        TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        TextView txtContinue = findViewById(R.id.txt_continue);
        TextInputLayout tilMobileNo = findViewById(R.id.til_mobile_no);

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
        toolbarTitle.setTypeface(Fonts.getSemiBold(this));
        txtContinue.setTypeface(Fonts.getSemiBold(this));
        etMobileNumber.setTypeface(Fonts.getSemiBold(this));
        tilMobileNo.setTypeface(Fonts.getRegular(this));

        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etMobileNumber.getText().toString().length()==10){
                    etMobileNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_correct, 0);
                }else {
                    etMobileNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        if(v==qrCodeButton) {
            startActivity(new Intent(this, DecoderActivity.class));
        }
        if(v==continueButton)
        {
            if(etMobileNumber.getText().toString().length()!=10)
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
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

}
