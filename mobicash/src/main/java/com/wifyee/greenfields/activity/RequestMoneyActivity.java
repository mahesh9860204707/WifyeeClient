package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.requests.RequestMoney;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RequestMoneyActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_Mobile_Number;
    private EditText et_Money;
    private Button submit;
    private String senderMobile="";
    private String recieverMobile="";
    private String amount="";
    private String hashValue="";
    private RequestMoney requestMoney;
    private Toolbar mToolbar;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_money);
        senderMobile= LocalPreferenceUtility.getUserMobileNumber(this);
        showUI();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        back= (ImageButton) findViewById(R.id.toolbar_back);

        if (mToolbar != null) {
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

    private void showUI() {
        et_Money=(EditText)findViewById(R.id.et_request_money);
        et_Mobile_Number=(EditText)findViewById(R.id.et_mobile_number);
        submit=(Button) findViewById(R.id.continue_btn);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        recieverMobile = et_Mobile_Number.getText().toString();
        amount = et_Money.getText().toString();
        if (amount.equals("") || amount == null) {
            Toast.makeText(RequestMoneyActivity.this, "Fill Send Money", Toast.LENGTH_SHORT).show();
            return;
        } else if (recieverMobile.equals("") || recieverMobile == null) {
            Toast.makeText(RequestMoneyActivity.this, "Fill Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(senderMobile);
        stringBuilder.append(recieverMobile);
        stringBuilder.append(amount);
        requestMoney=new RequestMoney();
        requestMoney.setAmount(amount);
        requestMoney.setClientreceiver(recieverMobile);
        requestMoney.setClientsender(senderMobile);
        try {
            hashValue = MobicashUtils.getSha1(stringBuilder.toString());
            requestMoney.setHash(hashValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        showProgressDialog();
        callApiRequestMoney(this,requestMoney);
    }

    private void callApiRequestMoney(final Context context, RequestMoney requestMoney) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
                jsonObject.put("amount",requestMoney.getAmount());
                jsonObject.put("clientsender",requestMoney.getClientsender());
                jsonObject.put("clientreceiver",requestMoney.getClientreceiver());
                jsonObject.put("hash",requestMoney.getHash());

                Log.e("JsonObject",jsonObject.toString());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                NetworkConstant.REQUEST_MONEY_TO_CLIENT_API ,jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0)
                    {
                        Log.e("MSG",response.getString(ResponseAttributeConstants.MSG));
                        cancelProgressDialog();
                        showSuccessDialog(RequestMoneyActivity.this);
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
                Log.e("volley_request_money", String.valueOf(error));
                //Toast.makeText(context,"response Error",Toast.LENGTH_SHORT).show();
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
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setShouldCache(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        finish();
    }

    //SuccesFully Transferred
    private void showSuccessDialog(Activity activity) {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_success);
        // Set dialog title
        layout.setTitle("Success");

        Button yes = (Button) layout.findViewById(R.id.yes);
        layout.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    finish();
                    layout.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }
}
