package com.wifyee.greenfields.activity;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.adapters.VoucherListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.foodorder.GPSTracker;
import com.wifyee.greenfields.models.VoucherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class VoucherList extends AppCompatActivity {

    private List<VoucherModel> list = new ArrayList<>();
    private VoucherModel[] voucher;
    private VoucherListAdapter voucherAdapter;
    private RecyclerView recyclerViewVouchers;
    int flag =1; //for item layout selection
    Toolbar mToolbar;
    SpinKitView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_list);

        recyclerViewVouchers =  findViewById(R.id.recycler_view_vouchers);
        progressBar =  findViewById(R.id.progressbar);

        mToolbar = findViewById(R.id.toolbar);
        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);

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

        LinearLayoutManager ll = new LinearLayoutManager(this);
        recyclerViewVouchers.setLayoutManager(ll);
        voucherAdapter = new VoucherListAdapter(this,list,flag);
        recyclerViewVouchers.setAdapter(voucherAdapter);
        voucherAdapter.notifyDataSetChanged();

        callVoucherList();
    }

    private void callVoucherList() {
        progressBar.setVisibility(View.VISIBLE);
        if (list != null) {
            list.clear();
        }
        JSONObject json = new JSONObject();
        try {
            json.put("pincode", LocalPreferenceUtility.getCurrentPincode(VoucherList.this));
            //json.put("limit","4");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("json",json.toString());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING +
                NetworkConstant.PARAM_VOUCHER_LIST_BY_PINCODE)
                .addJSONObjectBody(json)
                .setOkHttpClient(okHttpClient)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject object) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("--RESPONSE--", object.toString());
                        try {
                            if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                JSONArray jsonArray = object.getJSONArray(ResponseAttributeConstants.OFFERS_DATA);

                                voucher = new VoucherModel[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    voucher[i] = new VoucherModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("voucher_no"),
                                            jsonObject.getString("voucher_name"),
                                            jsonObject.getString("voucher_details"),
                                            jsonObject.getString("voucher_amount"),
                                            jsonObject.getString("discount_amount"),
                                            jsonObject.getString("valid_from"),
                                            jsonObject.getString("valid_upto"),
                                            jsonObject.getString("image_path")
                                    );
                                    list.add(voucher[i]);
                                }
                                recyclerViewVouchers.getRecycledViewPool().clear();
                                voucherAdapter.notifyDataSetChanged();
                            } else {
                                String msg = object.getString(ResponseAttributeConstants.MSG);
                                Toast.makeText(VoucherList.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressBar.setVisibility(View.GONE);
                        // handle error
                        Timber.e("called onError of User Invoice API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                    }
                });
    }

}
