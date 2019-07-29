package com.wifyee.greenfields.activity;

import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
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
import com.wifyee.greenfields.adapters.MyVoucherListAdapter;
import com.wifyee.greenfields.adapters.VoucherListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.MyVoucherModel;
import com.wifyee.greenfields.models.VoucherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MyVoucherList extends AppCompatActivity {

    private RecyclerView recyclerView;
    Toolbar mToolbar;
    SpinKitView progressBar;
    private List<MyVoucherModel> list = new ArrayList<>();
    private MyVoucherModel[] voucher;
    private MyVoucherListAdapter voucherAdapter;
    TextView txtNofound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_voucher_list);

        recyclerView =  findViewById(R.id.recycler_view);
        progressBar =  findViewById(R.id.progressbar);
        txtNofound =  findViewById(R.id.txt_no_found);

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
        txtNofound.setTypeface(Fonts.getSemiBold(this));

        LinearLayoutManager ll = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(ll);
        voucherAdapter = new MyVoucherListAdapter(this,list);
        recyclerView.setAdapter(voucherAdapter);
        voucherAdapter.notifyDataSetChanged();

        callMyVoucherList();
    }

    private void callMyVoucherList() {
        progressBar.setVisibility(View.VISIBLE);
        if (list != null) {
            list.clear();
        }
        JSONObject json = new JSONObject();
        try {
            json.put(ResponseAttributeConstants.CLIENTID, LocalPreferenceUtility.getUserCode(MyVoucherList.this));
            //json.put(ResponseAttributeConstants.CLIENTID, "3496");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING +
                NetworkConstant.PARAM_GET_CLIENT_VOUCHER_LIST)
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
                                if(jsonArray.length()>0) {
                                    txtNofound.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    voucher = new MyVoucherModel[jsonArray.length()];
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        voucher[i] = new MyVoucherModel(
                                                jsonObject.getString("id"),
                                                jsonObject.getString("voucher_id"),
                                                jsonObject.getString("voucher_no"),
                                                jsonObject.getString("voucher_name"),
                                                jsonObject.getString("voucher_details"),
                                                jsonObject.getString("voucher_actual_price"),
                                                jsonObject.getString("voucher_balance_amt"),
                                                jsonObject.getString("voucher_quantity"),
                                                jsonObject.getString("valid_from"),
                                                jsonObject.getString("valid_upto"),
                                                jsonObject.getString("image_path"),
                                                jsonObject.getString("voucher_expired"),
                                                jsonObject.getString("mer_id"),
                                                jsonObject.getString("mer_company"),
                                                jsonObject.getString("merchant_type_id"),
                                                jsonObject.getString("mer_current_status")
                                        );
                                        list.add(voucher[i]);
                                    }
                                    recyclerView.getRecycledViewPool().clear();
                                    voucherAdapter.notifyDataSetChanged();
                                }else {
                                    txtNofound.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                            } else {
                                String msg = object.getString(ResponseAttributeConstants.MSG);
                                Toast.makeText(MyVoucherList.this, msg, Toast.LENGTH_SHORT).show();
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
