package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import timber.log.Timber;

public class RequestCreditActivity extends BaseActivity implements View.OnClickListener
{
    private EditText mobileNumber,amount,comments;
    private Button submitBtn;
    private String hash="";
    private Toolbar mToolbar;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_credit);
        bindUI();

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }
    }
    //Bind Ui
    private void bindUI() {
        mobileNumber=(EditText)findViewById(R.id.merchant_mobile);
        amount=(EditText)findViewById(R.id.merchant_amount);
        comments=(EditText)findViewById(R.id.edit_text_comments);
        submitBtn=(Button) findViewById(R.id.submit);
        mToolbar = findViewById(R.id.toolbar);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(mobileNumber.getText().toString().length()==0||mobileNumber.getText().toString()==null)
        {
            Toast.makeText(this,"Fill Mobile Number",Toast.LENGTH_SHORT).show();
            return;
        }
        if(amount.getText().toString().length()==0||amount.getText().toString()==null)
        {
            Toast.makeText(this,"Fill Amount",Toast.LENGTH_SHORT).show();
            return;
        }
        if(comments.getText().toString().length()==0||comments.getText().toString()==null)
        {
            Toast.makeText(this,"Fill Comments",Toast.LENGTH_SHORT).show();
            return;
        }

        //Send Amount to Merchant
        sendAmountToMerchentApi(this,mobileNumber.getText().toString(),amount.getText().toString(),comments.getText().toString());
    }
    //Send Amount to Merchant Api
    private void sendAmountToMerchentApi(final Context mContext, String mobileNumber, String amount,String comment)
    {
        pDialog = new SweetAlertDialog(RequestCreditActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Please wait...");
        pDialog.show();
        pDialog.setCancelable(false);

        StringBuilder sb = new StringBuilder(LocalPreferenceUtility.getUserMobileNumber(mContext));
        sb.append(mobileNumber);
        sb.append(amount);
        sb.append(comment);
        try {
            hash = MobicashUtils.getSha1(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merchantMobile",mobileNumber);
            jsonObject.put("userMobile",LocalPreferenceUtility.getUserMobileNumber(mContext));
            jsonObject.put("amount",amount);
            jsonObject.put("comments",comment);
            jsonObject.put("hash",hash);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,NetworkConstant.USER_REQUEST_MERCHANT_CREDIT,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        //cancelProgressDialog();
                        pDialog.setTitleText("Success!")
                                .setContentText(response.getString(ResponseAttributeConstants.MSG))
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                onPause();
                            }
                        });
                        //Toast.makeText(mContext,"Credited Successfully",Toast.LENGTH_SHORT).show();
                        //showSuccessDialogCustomize(RequestCreditActivity.this);
                    }
                    else {
                        //cancelProgressDialog();
                        pDialog.setTitleText("Failed! Please Try Again.")
                                .setContentText(response.getString(ResponseAttributeConstants.MSG))
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error", String.valueOf(error));
                pDialog.setTitleText("Error!")
                        .setContentText(error.toString())
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                //Toast.makeText(mContext,"response Error",Toast.LENGTH_SHORT).show();
                //cancelProgressDialog();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }
    }

    //SuccesFully Transaction

    private void showSuccessDialogCustomize(final Activity activity)
    {
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
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    finish();
                    startActivity(new Intent(activity,MobicashDashBoardActivity.class));
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
    }
}
