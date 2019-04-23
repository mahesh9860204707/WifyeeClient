package com.wifyee.greenfields.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientToMerchantPaymnetActivity extends AppCompatActivity implements View.OnClickListener {

    private String qrValue="";
    private String merchantName="";
    private String merchantId="";
    @BindView(R.id.merchant_name)
    TextView merchantValueName;
    @BindView(R.id.continue_btn)
    Button continueButton;
    @BindView(R.id.et_add_money)
    EditText et_amount;
    private String addAmount;
    private String mobileNumber="";

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        ButterKnife.bind(this);
        mContext = this;
        continueButton.setOnClickListener(this);
        qrValue=getIntent().getStringExtra("QrCodeValue");
        mobileNumber=getIntent().getStringExtra("mobileNumber");
//        Log.e("Getting Value",qrValue);
        callApi(this,qrValue,mobileNumber);

    }
    //Call Api Client Wallet To Merchant Wallet Balance Transfer
    private void callApi(final Context context, String qrValue ,String mobile)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try
        {
            if(qrValue!= null) {
                jsonObject.put("qrCode", qrValue);
            }
            else {
                jsonObject.put("mobileNumber", mobile);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if(qrValue!= null) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.MERCHANT_PROFILE_POINT, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Getting Response", response.toString());
                    try {
                        if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                            String merchantId = response.getString("merchantId");
                            String merchantName = response.getString("merchantName");
                            bindUi(merchantId, merchantName);
                        } else {
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
                    Toast.makeText(context, "response Error", Toast.LENGTH_SHORT).show();
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
        else
        {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.MERCHANT_PROFILE_MOBILE_POINT, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Getting Response", response.toString());
                    try {
                        if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                            String merchantId = response.getString("merchantId");
                            String merchantName = response.getString("merchantName");
                            bindUi(merchantId, merchantName);
                        } else {
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
                    Toast.makeText(context, "response Error", Toast.LENGTH_SHORT).show();
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
    }

    private void bindUi(String merchantID, String merName) {

        merchantName=merName;
        merchantId=merchantID;
        merchantValueName.setText(merchantName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
    }

    @Override
    public void onClick(View v) {
        if(v==continueButton)
        {
            addAmount =et_amount.getText().toString();
            if(addAmount.length()==0)
            {
                Toast.makeText(this,"'Please fill Money",Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent=new Intent(this,PayUBaseActivity.class);
            intent.putExtra(PaymentConstants.STRING_EXTRA, addAmount);
            intent.putExtra(NetworkConstant.MERCHANT_ID, merchantId);
            intent.putExtra("merchantPay","ClientToMerchantPay");
            startActivity(intent);
        }
    }
}
