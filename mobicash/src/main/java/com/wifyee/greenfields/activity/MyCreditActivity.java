package com.wifyee.greenfields.activity;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.adapters.CashBackAdapter;
import com.wifyee.greenfields.adapters.MyCreditAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.foodorder.ModelMapper;
import com.wifyee.greenfields.models.CashbackModel;
import com.wifyee.greenfields.models.MyCreditModel;
import com.wifyee.greenfields.models.response.FailureResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MyCreditActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private MyCreditAdapter adapter;
    private List<MyCreditModel> creditModels = new ArrayList<>();
    MyCreditModel[] credit;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_credit);

        mToolbar = findViewById(R.id.mtoolbar);
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

        recyclerView = findViewById(R.id.recycler_view);
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyCreditAdapter(this, creditModels);
        recyclerView.setAdapter(adapter);

        dataLoad();

    }

    private void dataLoad() {
        if (creditModels != null) {
            creditModels.clear();
        }

        showProgressDialog();
        JSONObject json = new JSONObject();
        try {
            //json.put("clientId","3496");
            json.put("clientId",LocalPreferenceUtility.getUserCode(MyCreditActivity.this));
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
                NetworkConstant.PARAM_MERCHANT_CREDIT_CLIENT)
                .addJSONObjectBody(json)
                .setOkHttpClient(okHttpClient)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject object) {
                        Log.e("--RESPONSE--", object.toString());
                        cancelProgressDialog();
                        try {
                            if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                JSONArray jsonArray = object.getJSONArray(ResponseAttributeConstants.OFFERS_DATA);
                                String devResponse = object.getString(ResponseAttributeConstants.DEV_RESPONSE);
                                Log.e("dev_res",devResponse);

                                credit = new MyCreditModel[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    credit[i] = new MyCreditModel(
                                            jsonObject.getString(ResponseAttributeConstants.MER_ID),
                                            jsonObject.getString(ResponseAttributeConstants.MER_COMPANY),
                                            jsonObject.getString(ResponseAttributeConstants.TOTAL_AMOUNT),
                                            jsonObject.getString(ResponseAttributeConstants.MER_TYPE_ID),
                                            jsonObject.getString(ResponseAttributeConstants.MER_TYPE_NAME),
                                            jsonObject.getString(ResponseAttributeConstants.MER_PROFILE_IMAGE),
                                            jsonObject.getString(ResponseAttributeConstants.MER_CREDIT_ID),
                                            jsonObject.getString(ResponseAttributeConstants.MER_DIST_ZIPCODE)
                                    );
                                    creditModels.add(credit[i]);
                                }
                                recyclerView.getRecycledViewPool().clear();
                                adapter.notifyDataSetChanged();
                            } else {
                                String devResponse = object.getString(ResponseAttributeConstants.DEV_RESPONSE);
                                Log.e("dev_res",devResponse);
                                String msg = object.getString(ResponseAttributeConstants.MSG);
                                Toast.makeText(MyCreditActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                        Timber.e("called onError of User Invoice API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("UserInvoice = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                    }
                });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(MyCreditActivity.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
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
}
