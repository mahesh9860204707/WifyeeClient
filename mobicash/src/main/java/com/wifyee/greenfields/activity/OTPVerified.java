package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.goodiebag.pinview.Pinview;
import com.google.gson.Gson;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.GetClientProfileInfoResponse;
import com.wifyee.greenfields.models.response.GetProfileInfo;
import com.wifyee.greenfields.models.response.LoginResponse;
import com.wifyee.greenfields.models.response.MacUpdateAddressResponse;
import com.wifyee.greenfields.models.response.OTP_Response;
import com.wifyee.greenfields.services.MobicashIntentService;

import cn.pedant.SweetAlert.SweetAlertDialog;
import timber.log.Timber;

public class OTPVerified extends AppCompatActivity {

    private SpinKitView loading;
    private String mobilNo,userId,timefrom;
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_USER_CONFIRMATION_OTP_SUCCESS,
            NetworkConstant.STATUS_USER_CONFIRMATION_OTP_FAIL,
            NetworkConstant.STATUS_USER_OTP_FAIL,
            NetworkConstant.STATUS_USER_OTP_SUCCESS,
    };

    SweetAlertDialog pDialog;
    LinearLayout llResendOtp;
    TextView txtResend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverified);

        TextView txt = findViewById(R.id.txt);
        TextView txtMobileNo = findViewById(R.id.txt_mobile_no);
        TextView txtDontReceive = findViewById(R.id.txt_dont_receive);
        txtResend = findViewById(R.id.txt_resend);
        Button verifyOtp = findViewById(R.id.verify_otp);
        final Pinview pin = findViewById(R.id.pinview);
        loading = findViewById(R.id.progressbar);
        llResendOtp = findViewById(R.id.ll_resend_otp);

        txt.setTypeface(Fonts.getRegular(this));
        txtMobileNo.setTypeface(Fonts.getSemiBold(this));
        txtDontReceive.setTypeface(Fonts.getRegular(this));
        txtResend.setTypeface(Fonts.getSemiBold(this));
        verifyOtp.setTypeface(Fonts.getSemiBold(this));

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        toolBarTitle.setTypeface(Fonts.getSemiBold(this));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.secondaryPrimary), PorterDuff.Mode.SRC_ATOP);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

        mobilNo = getIntent().getStringExtra("mobile_no");
        userId = getIntent().getStringExtra("userId");
        timefrom = getIntent().getStringExtra("timefrom");

        txtMobileNo.setText("+91-"+mobilNo);

        TimeFun();

        llResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobilNo.length()==0||mobilNo.equals("")) {
                    Toast.makeText(getApplicationContext(),"Mobile Number not found .Please try after some time.",Toast.LENGTH_SHORT).show();
                } else {
                    loading.setVisibility(View.VISIBLE);
                    MobicashIntentService.startActionCallOTPDetails(OTPVerified.this,mobilNo,"");
                }
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String otpString = pin.getValue();
                    if (otpString.equals("") || otpString.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                    } else if(userId!=null) {
                        loading.setVisibility(View.VISIBLE);
                        OTP_Response mOTOtp_response=new OTP_Response();
                        mOTOtp_response.timefrom = timefrom;
                        mOTOtp_response.code = otpString;
                        mOTOtp_response.mobile = mobilNo;
                        mOTOtp_response.userId = userId;
                        System.out.println("Confirm otp request"+mOTOtp_response);
                        MobicashIntentService.startActionUserConfirmationOtp(getApplicationContext(),mOTOtp_response);
                        //callAuthenticationApi(getApplicationContext(),mobile,otpString,timeString);
                        //   layout.dismiss();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginStatusReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(loginStatusReceiver, new IntentFilter(action));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }
    }

    private BroadcastReceiver loginStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            loading.setVisibility(View.GONE);
            try {
                if (action.equals(NetworkConstant.STATUS_USER_CONFIRMATION_OTP_SUCCESS)) {

                    showSuccessAlertbox(OTPVerified.this);

                } else if (action.equals(NetworkConstant.STATUS_USER_CONFIRMATION_OTP_FAIL)){
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_USER_SIGNUP_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                        System.out.println("failureResponse"+failureResponse);
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        showErrorAlertbox(OTPVerified.this,failureResponse.msg);
                    } else {
                        // String errorMessage = getString(R.string.error_message);
                        String errorMessage ="Check Your Internet Connection.";
                        showErrorAlertbox(OTPVerified.this,errorMessage);
                    }
                }

                //Resend OTP
                if (action.equals(NetworkConstant.STATUS_USER_OTP_SUCCESS)) {
                    //mLoginButton.setEnabled(false);
                    OTP_Response otp_response = (OTP_Response) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if(otp_response!=null)
                    {
                        mobilNo = otp_response.mobile;
                        timefrom = otp_response.timefrom;
                        userId = otp_response.userId;
                        Toast.makeText(OTPVerified.this,"OTP has sent successfully",Toast.LENGTH_SHORT).show();
                        TimeFun();
                    }
                    else{

                    }

                } else if (action.equals(NetworkConstant.STATUS_USER_OTP_FAIL)){
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_USER_SIGNUP_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        //showErrorDialog(failureResponse.msg);
                        showErrorAlertbox(OTPVerified.this,failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showErrorAlertbox(OTPVerified.this,errorMessage);
                    }
                }


            } catch (Exception e) {
                Timber.e(" Exception caught in loginStatusReceiver " + e.getMessage());
            }
        }
    };

    private void showSuccessAlertbox(Activity activity){
        pDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("Done");
        pDialog.setContentText("OTP verified successfully.");
        pDialog.show();
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                onStop();
                OTPVerified.this.finish();
            }
        });
    }

    private void showErrorAlertbox(Activity activity,String msg){
        pDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText("Error");
        pDialog.setContentText(msg);
        pDialog.show();
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismiss();
            }
        });
    }

    private void TimeFun(){
        new CountDownTimer(100000, 1000){
            public void onTick(long millisUntilFinished){
                txtResend.setText("Resend OTP ("+(millisUntilFinished / 1000)+")");
                llResendOtp.setEnabled(false);
                txtResend.setAlpha(0.6f);
            }
            public  void onFinish(){
                txtResend.setText("Resend OTP");
                llResendOtp.setEnabled(true);
                txtResend.setAlpha(1f);
            }
        }.start();
    }
}
