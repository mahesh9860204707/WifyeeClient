package com.wifyee.greenfields.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.requests.ClientSendPassCodeRequest;
import com.wifyee.greenfields.models.response.ClientSendPassCodeResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class AuthenticateOTPActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_back)
    ImageButton back;

    @BindView(R.id.edit_text_mobile_number)
    EditText mMobileNumber;

    @BindString(R.string.dialog_error)
    String mDialogErrorTitle;

    @BindString(R.string.successful)
    String mSuccessfulTitle;

    @BindString(R.string.phone_number_error_message)
    String mPhoneNumberErrorMessage;

    @BindString(R.string.passcode_successfully_sent)
    String mPasscodeSuccessfullySent;

    @BindString(R.string.send_passcode_failed)
    String mSendPasswordFailed;


    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_CLIENT_SEND_PASSCODE_SUCCESS,
            NetworkConstant.STATUS_CLIENT_SEND_PASSCODE_FAIL
    };


    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_otp);
        ButterKnife.bind(this);
        mContext = this;
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
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
    }

    @OnClick(R.id.send_button)
    void sendPasscode() {
        if (validateMobileNo()) {
            showProgressDialog();
            callSendApi(this,mMobileNumber.getText().toString());
        }
    }

    public boolean validateMobileNo() {
        String regex = "\\d+";
        String phoneNumber = mMobileNumber.getText().toString();
        if (phoneNumber.length() != 10 && phoneNumber.matches(regex)) {
            Timber.d(" phone number are invalid");
            showDialog(mDialogErrorTitle, mPhoneNumberErrorMessage, false);
            return false;
        }
        return true;
    }

    /**
     * show validation dialog
     */
    private void showDialog(String title, String message, final boolean finishAfterOkPressed) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (finishAfterOkPressed) {
                            finish();
                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //generate Otp API
    private void callSendApi(final Context context,String clientMobile)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",clientMobile);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.SEND_OTP_AUTHENTICATION,jsonObject,new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        cancelProgressDialog();
                        String code=response.getString("code");
                        String timeFrom=response.getString("timeFrom");
                        String mobile=response.getString("mobile");
                        onSuccess(getString(R.string.otp_status_title), getString(R.string.otp_success),code,
                                timeFrom,mobile);
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


    /**
     * show success message alert
     *
     * @param statusMessage
     */
    private void onSuccess(String title, String statusMessage,final String code,final String timeString,final String mobile) {
        AlertDialog.Builder alertDialogBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(this);
        }
        alertDialogBuilder
                .setTitle(title)
                .setMessage(statusMessage)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                        startActivity(new Intent(AuthenticateOTPActivity.this,SignUpOTPActivity.class).putExtra("code",code).putExtra("timeFormString",timeString).putExtra("mobile",mobile));
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

}
