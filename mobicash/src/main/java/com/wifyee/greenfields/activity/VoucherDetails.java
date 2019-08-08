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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class VoucherDetails extends AppCompatActivity {

    private int paymentSelectedIndex = 0;
    private RadioGroup paymentGroup;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_details);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        ImageView imageView = findViewById(R.id.image);
        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        TextView voucherName = findViewById(R.id.voucher_name);
        TextView voucherAmt = findViewById(R.id.voucher_amount);
        TextView validity = findViewById(R.id.txt_validity);
        TextView voucherDetails = findViewById(R.id.voucher_details);
        final Button buy_now = findViewById(R.id.buy_now);
        TextView minus = findViewById(R.id.minus);
        TextView plus = findViewById(R.id.plus);
        TextView txt_voucher_worth = findViewById(R.id.txt_voucher_worth);
        final TextView tv_quantityNumber = findViewById(R.id.tv_quantityNumber);
        TextView txtPayment = findViewById(R.id.txt_payment);
        RadioButton wallet = findViewById(R.id.rb_wallet);
        RadioButton onlinePayment = findViewById(R.id.rb_netbanking);
        paymentGroup = findViewById(R.id.payment_group);
        TextView txt_terms_condition = findViewById(R.id.txt_terms_condition);
        TextView first_tc = findViewById(R.id.first_tc);
        TextView second_tc = findViewById(R.id.second_tc);
        TextView third_tc = findViewById(R.id.third_tc);

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

        String vName = getIntent().getStringExtra("v_name");
        final String vAmt = getIntent().getStringExtra("v_amt");
        String vUrl = getIntent().getStringExtra("v_url");
        String vValidity = getIntent().getStringExtra("v_validity");
        String vDetails = getIntent().getStringExtra("v_details");
        final String vId = getIntent().getStringExtra("v_id");
        final String vNo = getIntent().getStringExtra("v_no");

        toolBarTitle.setText(vName);
        voucherName.setText(vName);
        voucherAmt.setText("₹".concat(vAmt));
        validity.setText("Validity : ".concat(vValidity));
        voucherDetails.setText(vDetails);
        buy_now.setText("BUY NOW (₹".concat(vAmt).concat(")"));
        first_tc.setText("\u25CF ".concat(vName).concat(" is applicable on selected items only."));
        second_tc.setText("\u25CF No two or more vouchers can be clubbed together for a single transaction.");
        third_tc.setText("\u25CF "+vName+" is applicable only in the area with zipcode: "+LocalPreferenceUtility.getCurrentPincode(this));

        toolBarTitle.setTypeface(Fonts.getSemiBold(this));
        voucherName.setTypeface(Fonts.getSemiBold(this));
        txt_voucher_worth.setTypeface(Fonts.getRegular(this));
        voucherDetails.setTypeface(Fonts.getRegular(this));
        voucherAmt.setTypeface(Fonts.getSemiBold(this));
        validity.setTypeface(Fonts.getRegular(this));
        minus.setTypeface(Fonts.getSemiBold(this));
        plus.setTypeface(Fonts.getSemiBold(this));
        tv_quantityNumber.setTypeface(Fonts.getSemiBold(this));
        buy_now.setTypeface(Fonts.getSemiBold(this));
        txtPayment.setTypeface(Fonts.getRegular(this));
        wallet.setTypeface(Fonts.getRegular(this));
        txt_terms_condition.setTypeface(Fonts.getRegular(this));
        first_tc.setTypeface(Fonts.getRegular(this));
        second_tc.setTypeface(Fonts.getRegular(this));
        third_tc.setTypeface(Fonts.getRegular(this));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.oriz_icon)
                .error(R.drawable.oriz_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(this).load(vUrl)
                .apply(options)
                .into(imageView);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(tv_quantityNumber.getText().toString()) + 1;
                int amt = Integer.parseInt(vAmt);
                if (value >= 1) {
                    //checkItemQuantity(myViewHolder,place,place.getItemId(),value);
                    tv_quantityNumber.setText(String.valueOf(value));
                    int sumPrice = amt * value;
                    buy_now.setText("BUY NOW (₹".concat(String.valueOf(sumPrice)).concat(")"));
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(tv_quantityNumber.getText().toString()) - 1;
                int amt = Integer.parseInt(vAmt);
                if (value >= 1) {
                    tv_quantityNumber.setText(String.valueOf(value));
                    int sumPrice = amt * value;
                    buy_now.setText("BUY NOW (₹".concat(String.valueOf(sumPrice)).concat(")"));
                }
            }
        });

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

        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String totalAmount = buy_now.getText().toString()
                        .replace("BUY NOW (₹","").replace(")","");
                    if(paymentSelectedIndex == 0){
                        payWallet(totalAmount,vId,vNo, tv_quantityNumber.getText().toString(),vAmt);
                    }else {
                        Intent i = new Intent(VoucherDetails.this,VoucherPurchaseWebViewActivity.class);
                        i.putExtra("ord_amt",totalAmount);
                        i.putExtra("v_id",vId);
                        i.putExtra("v_no",vNo);
                        i.putExtra("v_qty",tv_quantityNumber.getText().toString());
                        i.putExtra("v_amt",vAmt);
                        startActivity(i);
                        finish();
                    }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }
    }


    private void payWallet(String ord_amt,String v_id,String v_no,String v_qty,String v_amt){
        pDialog = new SweetAlertDialog(VoucherDetails.this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Please wait...");
        pDialog.show();
        pDialog.setCancelable(false);

        JSONObject json = new JSONObject();
        try {
            json.put("order_type","voucher_order");
            json.put("client_id",LocalPreferenceUtility.getUserCode(VoucherDetails.this));
            json.put("order_amt",ord_amt);
            json.put("voucher_id",v_id);
            json.put("voucher_no",v_no);
            json.put("voucher_quantity",v_qty);
            json.put("voucher_amt",v_amt);
            json.put("payment_mode","wallet");
            json.put("order_on", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date()));
            json.put("mobile",LocalPreferenceUtility.getUserMobileNumber(VoucherDetails.this));
            json.put("pincode", MobicashUtils.md5(LocalPreferenceUtility.getUserPassCode(VoucherDetails.this)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("POST_BUY_JSON",json.toString());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PURCHASE_VOUCHER_WALLET_PAYMENT)
                .addJSONObjectBody(json)
                .setOkHttpClient(okHttpClient)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called buy voucher wallet payment");
                        Log.e("buyVoucherRes",response.toString());
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                LocalPreferenceUtility.saveWalletBalance(VoucherDetails.this,
                                        String.valueOf(response.getInt("new_wallet_bal")));
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

                            } else {
                                pDialog.setTitleText("Failed!")
                                        .setContentText(response.getString(ResponseAttributeConstants.MSG))
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
