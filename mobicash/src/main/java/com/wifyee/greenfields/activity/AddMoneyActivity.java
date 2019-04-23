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
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.models.requests.AddMoneyWallet;

import java.security.NoSuchAlgorithmException;

public class AddMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_add_money;
    private Button btncontinue;
    private String money="";
    private String clientReciever="";
    private String pincode="";
    private String hash="";
    private AddMoneyWallet addMoneyWallet;
    private String addMoneyKey="addMoneyKey";
    private Toolbar mToolbar;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        back= (ImageButton) findViewById(R.id.toolbar_back);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            back.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }
        bindView();
        setTitle("Add Money");
    }

    private void bindView() {
        et_add_money=(EditText)findViewById(R.id.et_add_money);
        btncontinue=(Button) findViewById(R.id.continue_btn);
        btncontinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v==btncontinue) {
            money=et_add_money.getText().toString();
            if(money.equals("")||money.length()==0)
            {
                Toast.makeText(AddMoneyActivity.this,"Fill Money",Toast.LENGTH_SHORT).show();
                return;
            }
            addMoneyWallet=new AddMoneyWallet();
            clientReciever= LocalPreferenceUtility.getUserMobileNumber(this);
            pincode= LocalPreferenceUtility.getUserPassCode(this);
            StringBuilder sb = new StringBuilder(clientReciever);
            sb.append(money);
            sb.append(MobicashUtils.md5(pincode));
            addMoneyWallet.clientreceiver=clientReciever;
            addMoneyWallet.amount=money;
            addMoneyWallet.pincode=pincode;
            try {
                String hashValue="";
                hashValue = MobicashUtils.getSha1(sb.toString());
                addMoneyWallet.hash=hashValue;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            Intent intent=new Intent(this,PayUBaseActivity.class);
            intent.putExtra(PaymentConstants.STRING_EXTRA, money);
            intent.putExtra("addMoneyKey",addMoneyKey);
            intent.putExtra(NetworkConstant.EXTRA_DATA_WALLET, addMoneyWallet);
            intent.putExtra("description","");
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}
