package com.wifyee.greenfields.activity;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.adapters.DiscountClaimAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.dairyorder.OrderSummaryActivity;
import com.wifyee.greenfields.foodorder.AddToCartActivity;
import com.wifyee.greenfields.foodorder.ModelMapper;
import com.wifyee.greenfields.fragments.MyCartFragment;
import com.wifyee.greenfields.interfaces.OnClickListener;
import com.wifyee.greenfields.models.CashbackModel;
import com.wifyee.greenfields.models.DiscountClaimModel;
import com.wifyee.greenfields.models.response.FailureResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class DiscountClaim extends AppCompatActivity implements OnClickListener {

    Toolbar mToolbar;
    RadioGroup claimGrop;
    RadioButton rbVoucher,rbCashback;
    RecyclerView recyclerView;
    Button backToCart;
    TextView txtAvailCoupons;
    private String voucherID="",voucherNO="",voucherName="",voucherDiscountAmt="";

    private DiscountClaimAdapter adapter;
    private List<DiscountClaimModel> listArray = new ArrayList<>();
    private DiscountClaimModel[] discount;
    private ProgressDialog progressDialog;
    private int claimSelectedIndex;
    String flag,amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_claim);

        recyclerView = findViewById(R.id.recycler_view);
        backToCart = findViewById(R.id.back_to_cart);
        rbCashback = findViewById(R.id.rb_cashback);
        rbVoucher = findViewById(R.id.rb_voucher);
        claimGrop = findViewById(R.id.claim_group);
        mToolbar = findViewById(R.id.mtoolbar);
        txtAvailCoupons = findViewById(R.id.txt_avail_coupons);
        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        TextView txtClaim = findViewById(R.id.txt);

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

        txtClaim.setTypeface(Fonts.getRegular(this));
        toolBarTitle.setTypeface(Fonts.getSemiBold(this));
        txtAvailCoupons.setTypeface(Fonts.getSemiBold(this));
        rbCashback.setTypeface(Fonts.getSemiBold(this));
        rbVoucher.setTypeface(Fonts.getSemiBold(this));
        backToCart.setTypeface(Fonts.getSemiBold(this));

        flag = getIntent().getStringExtra("flag");
        amount = getIntent().getStringExtra("amount");

        rbCashback.setText("Cashback ("+amount+")");
        double amt = Double.parseDouble(amount.replace("₹",""));
        //rbCashback.setText("Cashback ("+amount+")");

        adapter = new DiscountClaimAdapter(listArray,this,this,amt);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        dataLoad();

        claimGrop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_cashback) {
                    claimSelectedIndex = 0;
                    recyclerView.setVisibility(View.GONE);
                    txtAvailCoupons.setVisibility(View.GONE);
                } else {
                    claimSelectedIndex = 1;
                    recyclerView.setVisibility(View.VISIBLE);
                    txtAvailCoupons.setVisibility(View.VISIBLE);
                }
            }
        });

        backToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (claimSelectedIndex == 1) {
                    if (!voucherID.isEmpty()) {
                        if (flag.equalsIgnoreCase("order_summary")) {
                            OrderSummaryActivity.isVoucherClaim = true;
                            OrderSummaryActivity.voucherId = voucherID;
                            OrderSummaryActivity.voucherNo = voucherNO;
                            OrderSummaryActivity.voucherName = voucherName;
                            OrderSummaryActivity.voucherDiscAmt = voucherDiscountAmt;
                            OrderSummaryActivity.claimType = "2";
                            finish();
                        }else if (flag.equalsIgnoreCase("cart")) {
                            MyCartFragment.isVoucherClaim = true;
                            MyCartFragment.voucherId = voucherID;
                            MyCartFragment.voucherNo = voucherNO;
                            MyCartFragment.voucherName = voucherName;
                            MyCartFragment.voucherDiscAmt = voucherDiscountAmt;
                            MyCartFragment.claimType = "2";
                            finish();
                        } else if(flag.equalsIgnoreCase("food_cart")){
                            AddToCartActivity.isVoucherClaim = true;
                            AddToCartActivity.voucherId = voucherID;
                            AddToCartActivity.voucherNo = voucherNO;
                            AddToCartActivity.voucherName = voucherName;
                            AddToCartActivity.voucherDiscAmt = voucherDiscountAmt;
                            AddToCartActivity.claimType = "2";
                            finish();
                        }
                    } else {
                        Toast.makeText(DiscountClaim.this, "Select at least one voucher!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (flag.equalsIgnoreCase("order_summary")) {
                        OrderSummaryActivity.isVoucherClaim = true;
                        OrderSummaryActivity.claimType = "1";
                        OrderSummaryActivity.voucherDiscAmt = amount.replace("₹","");
                        finish();
                    }else if (flag.equalsIgnoreCase("cart")) {
                        MyCartFragment.isVoucherClaim = true;
                        MyCartFragment.claimType = "1";
                        MyCartFragment.voucherDiscAmt = amount.replace("₹","");
                        finish();
                    }else if(flag.equalsIgnoreCase("food_cart")){
                        AddToCartActivity.isVoucherClaim = true;
                        AddToCartActivity.claimType = "1";
                        AddToCartActivity.voucherDiscAmt = amount.replace("₹","");
                        finish();
                    }
                }
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(DiscountClaim.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIcon(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }


    private void dataLoad() {
        if (listArray != null) {
            listArray.clear();
        }

        showProgressDialog();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.get(NetworkConstant.MOBICASH_BASE_URL_TESTING +
                NetworkConstant.PARAM_GET_VOUCHERS)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject object) {
                        Log.e("--RESPONSE--", object.toString());
                        Timber.e("called onResponse of getVoucher API.");
                        cancelProgressDialog();
                        try {
                            if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                JSONArray jsonArray = object.getJSONArray("vouchers");
                                discount = new DiscountClaimModel[jsonArray.length()];

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    discount[i] = new DiscountClaimModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("voucher_no"),
                                            jsonObject.getString("voucher_name"),
                                            jsonObject.getString("voucher_details"),
                                            jsonObject.getString("voucher_amount"),
                                            jsonObject.getString("discount_amount")
                                    );
                                    listArray.add(discount[i]);
                                }
                                recyclerView.getRecycledViewPool().clear();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(DiscountClaim.this,
                                        "Something went wrong. PLease try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            cancelProgressDialog();
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        cancelProgressDialog();
                        // handle error
                        Timber.e("called onError of getVoucher API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("UserInvoice = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                    }
                });
    }

    @Override
    public void seClickListener(String voucherId, String voucherNo, String voucherName, String voucherDiscountAmt) {
        this.voucherID = voucherId;
        this.voucherNO = voucherNo;
        this.voucherName = voucherName;
        this.voucherDiscountAmt = voucherDiscountAmt;
    }
}
