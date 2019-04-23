package com.wifyee.greenfields.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddBeneficiaryOTPActivity extends BaseActivity implements View.OnClickListener
{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_back)
    ImageButton back;
    @BindView(R.id.tv_otp)
    EditText et_otp;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.resend_otp)
    TextView resendOtp;

    private String requestString ="";
    private String beneficiaryId ="";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beneficiary_otp);
        requestString=getIntent().getStringExtra("requestCode");
        beneficiaryId=getIntent().getStringExtra("benId");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }

        ButterKnife.bind(this);
        submit.setOnClickListener(this);
        resendOtp.setOnClickListener(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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

    @Override
    public void onClick(View v)
    {
        if(v==submit) {
            String otpString = et_otp.getText().toString();

            if (otpString.equals("") || otpString.length() == 0) {
                Toast.makeText(this, "Fill OTP", Toast.LENGTH_SHORT).show();
                return;
            }
            showProgressDialog();
            callAuthenticationApi(AddBeneficiaryOTPActivity.this, requestString, otpString, beneficiaryId);
        }
       else if(v==resendOtp)
        {
            showProgressDialog();
            callSendApi(AddBeneficiaryOTPActivity.this);
        }
    }
   //Call Authentication Api
    private void callAuthenticationApi(final Context context,String requestString, String otpString, String beneficiaryId)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", LocalPreferenceUtility.getUserCode(context));
            jsonObject.put("userType","Client");
            jsonObject.put("agentCode","RS00789");
            jsonObject.put("ben_id", beneficiaryId);
            jsonObject.put("requestCode",requestString);
            jsonObject.put("otp",otpString);
            jsonObject.put("requestFor","CUSTVERIFICATION");


            StringBuilder sb = new StringBuilder(LocalPreferenceUtility.getUserCode(context));
            sb.append("Client");
            sb.append("RS00789");
            sb.append(beneficiaryId);
            sb.append(otpString);
            sb.append(requestString);
            sb.append("CUSTVERIFICATION");
            try {
                String hashValue="";
                hashValue = MobicashUtils.getSha1(sb.toString());
                jsonObject.put("hash",hashValue);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.SEND_BENEFICIARY_OTP_AUTHENTICATION_COMPLETE ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        cancelProgressDialog();
                        showSuccessDialog(AddBeneficiaryOTPActivity.this);
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

    private void showSuccessDialog(final Activity activity)
    {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_success_signup);
        // Set dialog title
        layout.setTitle("Success");
        Button yes = (Button) layout.findViewById(R.id.yes);
        layout.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   // connectWifi(strWiFyeeSSID,activity);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    startActivity(IntentFactory.createDashboardActivity(getApplicationContext()));
                    layout.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }


    //Generate OTP API
    private void callSendApi(final Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", LocalPreferenceUtility.getUserCode(context));
            jsonObject.put("userType","Client");
            jsonObject.put("agentCode","RS00789");
            jsonObject.put("customerMobile",LocalPreferenceUtility.getUserMobileNumber(context));
            jsonObject.put("requestFor","BENVERIFICATION");

            StringBuilder sb = new StringBuilder(LocalPreferenceUtility.getUserCode(context));
            sb.append("Client");
            sb.append("RS00789");
            sb.append(LocalPreferenceUtility.getUserMobileNumber(context));
            sb.append("BENVERIFICATION");
            try {
                String hashValue="";
                hashValue = MobicashUtils.getSha1(sb.toString());
                jsonObject.put("hash",hashValue);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.USER_SEND_BENEFICIARY_OTP_VERIFICATION_END_POINT ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        String  requestCode = response.getString("requestCode");
                        requestString=requestCode;
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



    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                String input=message;
                int searchCount = Integer.parseInt(input.replaceAll("\\D", ""));
                System.out.println(searchCount);
                // Message: Dear mukesh,Are you sure want to delete the receiver. Please enter the Verification Code 4827 to delete.
                //  String parseString = message.substring(99, 104);
                et_otp.setText(String.valueOf(searchCount));
            }
        }
    };

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


}
