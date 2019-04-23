package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.adapters.BeneficiaryListAdapter;
import com.wifyee.greenfields.adapters.MerchantCategoryAdapter;
import com.wifyee.greenfields.adapters.MerchantListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.requests.BeneficiaryBean;
import com.wifyee.greenfields.models.response.MerchantListBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MerchantListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_back)
    ImageButton back;

    @BindView(R.id.merchant_list)
    RecyclerView merchantList;

    private MerchantListBean merchantListBean;
    private ArrayList<MerchantListBean> merchantListBeanArrayList=new ArrayList<>();
    private String category="";
    private String actualCategory="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_list);
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        category=getIntent().getStringExtra("category");

        if(category.equals("Medical")) {
            actualCategory="Pharmacy";
        }else if(category.equals("Food & Ordering")) {
            actualCategory="Restaurant";
        }else if(category.equals("Grocery")) {
            actualCategory="General Store";
        }
        else if(category.equals("Education")) {
           // actualCategory=;
        }
        //Call Api For merchant List
        showProgressDialog();
        callApiMerchantList(this,actualCategory);
        if (mToolbar!= null) {
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
   //call Api for Merchant List
    private void callApiMerchantList(final Activity context, String category)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("macAddress",macAddrr);
            jsonObject.put("merchant_type",category);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.MERCHANT_LIST ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0)
                    {
                        cancelProgressDialog();
                        JSONArray jsonArray=response.getJSONArray("data");
                        merchantListBeanArrayList.clear();
                        for(int i=0;i<jsonArray.length();i++) {
                            merchantListBean=new MerchantListBean();
                            JSONObject benJsonObject=jsonArray.getJSONObject(i);
                            String merName=benJsonObject.getString("mer_name");
                            String merID=benJsonObject.getString("mer_id");
                            String merCompany=benJsonObject.getString("mer_company");
                            String merType=benJsonObject.getString("mer_type");
                            String merProfile=benJsonObject.getString("merchant_profile_image");
                            String merAddress=benJsonObject.getString("idt_address");
                            String merEmail=benJsonObject.getString("idt_email");
                            String merCity=benJsonObject.getString("idt_city");
                            String merMobile=benJsonObject.getString("idt_contactphone");

                            merchantListBean.setMer_id(merID);
                            merchantListBean.setMer_name(merName);
                            merchantListBean.setMer_company(merCompany);
                            merchantListBean.setMer_type(merType);
                            merchantListBean.setMerchant_profile_image(merProfile);
                            merchantListBean.setIdt_address(merAddress);
                            merchantListBean.setIdt_email(merEmail);
                            merchantListBean.setIdt_city(merCity);
                            merchantListBean.setIdt_contactphone(merMobile);
                            merchantListBeanArrayList.add(merchantListBean);
                        }
                        bindMerchantList(merchantListBeanArrayList);
                    }
                    else {
                        cancelProgressDialog();
                        Toast.makeText(context, "Some Issue in Getting response", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error", String.valueOf(error));
                Toast.makeText(context,"response Error",Toast.LENGTH_SHORT).show();
                cancelProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                Log.d("params", String.valueOf(params));
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setShouldCache(false);
    }
    //Bind Merchant List
    private void bindMerchantList(ArrayList<MerchantListBean> merchantListBeanArrayList)
    {
        MerchantListAdapter adapter = new MerchantListAdapter(this,merchantListBeanArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        merchantList.setLayoutManager(linearLayoutManager);
        merchantList.setItemAnimator(new DefaultItemAnimator());
        merchantList.setAdapter(adapter);
        cancelProgressDialog();
    }


}
