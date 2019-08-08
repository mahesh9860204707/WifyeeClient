package com.wifyee.greenfields.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.dairyorder.DairyNetworkConstant;
import com.wifyee.greenfields.dairyorder.DairyProductIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class PayCredit extends AppCompatActivity {

    TextView totalCredit;
    EditText amount;
    Toolbar mToolbar;
    String totalAmt,mcId;
    private int paymentSelectedIndex = 0;
    private RadioGroup paymentGroup;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_credit);

        mToolbar = findViewById(R.id.mtoolbar);
        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        totalCredit = findViewById(R.id.total_credit);
        amount = findViewById(R.id.amount);
        TextView txtTotalCredit = findViewById(R.id.txt_total_credit);
        TextView txtSignRupee = findViewById(R.id.sign_rupee);
        TextView txtPayment = findViewById(R.id.txt_payment);
        RadioButton wallet = findViewById(R.id.rb_wallet);
        RadioButton onlinePayment = findViewById(R.id.rb_netbanking);
        paymentGroup = findViewById(R.id.payment_group);
        Button pay =  findViewById(R.id.pay);

        totalAmt = getIntent().getStringExtra("due_amt");
        mcId = getIntent().getStringExtra("mc_id");

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
        //txtSignRupee.setTypeface(Fonts.getSemiBold(this));
        amount.setTypeface(Fonts.getRegular(this));
        txtPayment.setTypeface(Fonts.getRegular(this));
        wallet.setTypeface(Fonts.getRegular(this));
        onlinePayment.setTypeface(Fonts.getRegular(this));
        pay.setTypeface(Fonts.getSemiBold(this));

        int walletAmount = !LocalPreferenceUtility.getWalletBalance(this).equals("")
                ?Integer.parseInt(LocalPreferenceUtility.getWalletBalance(this)) : 0;
        wallet.setText("Wallet ( ₹"+walletAmount+" )");

        totalCredit.setText("₹"+totalAmt);

        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_wallet) {
                    paymentSelectedIndex = 0;
                } else if(checkedId == R.id.rb_netbanking) {
                    paymentSelectedIndex = 1;
                }
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate(view)){
                    if(paymentSelectedIndex == 0){
                        payWallet(amount.getText().toString().trim());
                    }else {
                        Intent i = new Intent(PayCredit.this,PayCreditWebViewActivity.class);
                        i.putExtra("mc_id",mcId);
                        i.putExtra("amount",amount.getText().toString().trim());
                        startActivity(i);
                        finish();
                    }
                }
            }
        });
    }

    private boolean isValidate(View view){
        if (amount.getText().toString().trim().isEmpty()){
            Snackbar.make(view,"Please enter valid amount",Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!amount.getText().toString().trim().isEmpty()) {
            int value = Integer.parseInt(amount.getText().toString());
            if (value <= 0) {
                Snackbar.make(view, "Please enter valid amount", Snackbar.LENGTH_SHORT).show();
                return false;
            }else if (value > Integer.parseInt(totalAmt)){
                Snackbar.make(view, "Amount is exceeding to due amount", Snackbar.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }
    }


    private void payWallet(String amount){
        pDialog = new SweetAlertDialog(PayCredit.this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Please wait...");
        pDialog.show();
        pDialog.setCancelable(false);

        JSONObject json = new JSONObject();
        try {
            json.put("merchant_credits_id",mcId);
            json.put("clientPhone",LocalPreferenceUtility.getUserMobileNumber(PayCredit.this));
            json.put("paymentAmt",amount);
            json.put("paymentMode","wallet");
            json.put("pincode", MobicashUtils.md5(LocalPreferenceUtility.getUserPassCode(PayCredit.this)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("POST_ORDER_JSON",json.toString());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING+NetworkConstant.CREDIT_WALLET_PAYMENT)
                .addJSONObjectBody(json)
                .setOkHttpClient(okHttpClient)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called credit wallet payment");
                        Log.e("creditWalletRes",response.toString());
                        try {
                            /*DairyProductIntentService.startActionGetWalletBalance(PayCredit.this,
                                    LocalPreferenceUtility.getUserMobileNumber(PayCredit.this));*/
                            JSONObject devResObj = response.getJSONObject(ResponseAttributeConstants.DEV_RESPONSE);
                            LocalPreferenceUtility.saveWalletBalance(PayCredit.this,
                                    String.valueOf(devResObj.getInt(ResponseAttributeConstants.CLIENT_NEW_BALANCE)));
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                pDialog.setTitleText("Success!")
                                        .setContentText(response.getString(ResponseAttributeConstants.MSG))
                                        .setConfirmText("OK")
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        onResume();
                                        finish();
                                    }
                                });
//
                            } else {
                                JSONObject object = response.getJSONObject(ResponseAttributeConstants.DEV_RESPONSE);
                                pDialog.setTitleText("Failed!")
                                        .setContentText(object.getString(ResponseAttributeConstants.MSG))
                                        .setConfirmText("OK")
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("ErrorAddOrder",error.toString());
                        Timber.e("called onError of User dairy order API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        pDialog.setTitleText("Error!")
                                .setContentText(error.toString())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                });
    }
}
