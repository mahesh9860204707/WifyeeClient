package com.wifyee.greenfields.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.Profile;
//import com.facebook.login.LoginResult;
import com.github.ybq.android.spinkit.SpinKitView;
import com.goodiebag.pinview.Pinview;
import com.google.gson.Gson;
import com.wifyee.greenfields.Intents.IntentFactory;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.GPSTracker;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.constants.WifiConstant;
import com.wifyee.greenfields.models.requests.GetClientProfileInfoRequest;
import com.wifyee.greenfields.models.requests.LoginRequest;
import com.wifyee.greenfields.models.requests.MacAddressUpdate;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.GetClientProfileInfoResponse;
import com.wifyee.greenfields.models.response.GetProfileInfo;
import com.wifyee.greenfields.models.response.LoginResponse;
import com.wifyee.greenfields.models.response.MacUpdateAddressResponse;
import com.wifyee.greenfields.models.response.OTP_Response;
import com.wifyee.greenfields.services.MobicashIntentService;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import timber.log.Timber;

public class SignInBaseActivity extends BaseActivity implements View.OnClickListener,LocationListener {


    @BindView(R.id.tv_phone_number)
    EditText mClientMobileNo;

    @BindView(R.id.tv_password)
    EditText mClientPasscode;

    @BindView(R.id.btn_login)
    Button mLoginButton;

    @BindView(R.id.tv_sign_up)
    LinearLayout mSignUpTextView;

    @BindView(R.id.tv_send_passcode)
    TextView mSendPassword;

    @BindView(R.id.progressbar)
    SpinKitView loading;

    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;

    /*@BindView(R.id.tv_otp_verify)
    TextView mOtpVerify;*/

    /*@BindView(R.id.update_device)
    TextView updateDevice;*/

    /*@BindView(R.id.otp_layout)
    LinearLayout otp_layout;*/

    /*@BindView(R.id.tv_otp)
    EditText tv_otp;*/

    /*@BindView(R.id.resend_otp)
    TextView resend_otp;*/

    /*@BindView(R.id.submit_otp)
    Button btn_otp;*/

  //  @BindView(R.id.fb_login_button)
  //  Button fbLoginButton;

  //  @BindView(R.id.gmail_login_button)
  //  Button gmailLoginButton;

    // Validation error messages
    @BindString(R.string.phone_number_error_message)
    String mPhoneNumberErrorMessage;


    @BindString(R.string.passcode_error_message)
    String mPasscodeErrorMessage;

    TextInputLayout tilMobile,tilPassword;
    // GPSTracker class
    GPSTracker gps;
    //private GoogleHelper mGoogle;
    //private FacebookHelper mFacebook;
    private Context mContext = null;
    private LocationManager locationManager;
    private String mprovider;
    private Double latitude,longitude;
    private LoginRequest mLoginRequest;
    String strWiFyeeNamesecond="Wifyee";
    private String firstName="";
    private String clientEmail="";
    private String lastName="";
    private OTP_Response mSendOtpData;
    private String mobileNumber="";
    private EditText mobileRecharge;
    SweetAlertDialog pDialog;
    Dialog layoutOtp;
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_USER_LOGIN_FAIL,
            NetworkConstant.STATUS_USER_LOGIN_SUCCESS,
            NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_SUCCESS,
            NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_FAIL,
            NetworkConstant.STATUS_USER_OTP_FAIL,
            NetworkConstant.STATUS_USER_OTP_SUCCESS,
            NetworkConstant.STATUS_USER_CONFIRMATION_OTP_SUCCESS,
            NetworkConstant.STATUS_USER_CONFIRMATION_OTP_FAIL,
            NetworkConstant.STATUS_MAC_ADDRESS_UPDATE_FAIL,
            NetworkConstant.STATUS_MAC_ADDRESS_UPDATE_SUCCESS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_new);
       // FacebookSdk.sdkInitialize(getApplicationContext());
      //  mGoogle = new GoogleHelper(this, this, null);
      //  mFacebook = new FacebookHelper(this);
        ButterKnife.bind(this);
        mContext = this;
        mLoginButton.setOnClickListener(this);
        mSignUpTextView.setOnClickListener(this);
        mSendPassword.setOnClickListener(this);
        //mOtpVerify.setOnClickListener(this);
       // fbLoginButton.setOnClickListener(this);
      //  gmailLoginButton.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 15000, 1, this);
            if (location != null)
                onLocationChanged(location);
              //  Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
        }

        ImageView icSetting = findViewById(R.id.ic_setting);
        TextView favName = findViewById(R.id.com_name);
        TextView txtNewUser = findViewById(R.id.txt_new_user);
        TextView txtSignUp = findViewById(R.id.txt_signup);
        tilMobile   = findViewById(R.id.til_mobile);
        tilPassword = findViewById(R.id.til_password);
        TextView txtLogin = findViewById(R.id.txt_login);

        favName.setTypeface(Fonts.getSemiBold(this));
        mClientMobileNo.setTypeface(Fonts.getSemiBold(this));
        mClientPasscode.setTypeface(Fonts.getSemiBold(this));
        txtLogin.setTypeface(Fonts.getSemiBold(this));
        txtNewUser.setTypeface(Fonts.getSemiBold(this));
        txtSignUp.setTypeface(Fonts.getSemiBold(this));
        mSendPassword.setTypeface(Fonts.getSemiBold(this));
        tilMobile.setTypeface(Fonts.getRegular(this));
        tilPassword.setTypeface(Fonts.getRegular(this));
        mLoginButton.setTypeface(Fonts.getSemiBold(this));

        mClientMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mClientMobileNo.getText().toString().length()==10){
                    mClientMobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_correct, 0);
                }else {
                    mClientMobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        icSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.inflate(R.menu.setting_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                popupMobileNo(1);
                                //popupOtpFullscreen(SignInBaseActivity.this,mobileNumber);
                                break;

                            case R.id.menu2:
                                popupMobileNo(0);
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

/*
        updateDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupUpdateMac(SignInBaseActivity.this);
            }
        });*/

        //otp_layout.setVisibility(View.INVISIBLE);


        /*resend_otp.setEnabled(false);
        tv_otp.setEnabled(false);
        btn_otp.setEnabled(false);*/

        /*resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clientMobile=LocalPreferenceUtility.getUserMobileNumber(SignInBaseActivity.this);
                if (clientMobile.length()==0||clientMobile.equals("")) {
                    Toast.makeText(getApplicationContext(),"Mobile Number not found .Please try after some time.",Toast.LENGTH_SHORT).show();
                } else {
                    showProgressDialog();
                    MobicashIntentService.startActionCallOTPDetails(mContext,clientMobile);
                }
            }
        });*/

        /*btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String otpString = tv_otp.getText().toString();
                    if (otpString.equals("") || otpString.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Fill OTP", Toast.LENGTH_SHORT).show();
                    } else if(mSendOtpData!=null) {
                        showProgressDialog();
                        OTP_Response mOTOtp_response=new OTP_Response();
                        mOTOtp_response.timefrom = mSendOtpData.timefrom;
                        mOTOtp_response.code = otpString;
                        mOTOtp_response.mobile = mSendOtpData.mobile;
                        mOTOtp_response.userId = mSendOtpData.userId;
                        System.out.println("Confirm otp request"+mOTOtp_response);
                        MobicashIntentService.startActionUserConfirmationOtp(getApplicationContext(),mOTOtp_response);
                        //callAuthenticationApi(getApplicationContext(),mobile,otpString,timeString);
                        //   layout.dismiss();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });*/
    }

    //Show Popup Mac device ID
    private void showPopupUpdateMac(final Activity activity) {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        layout.setContentView(R.layout.showpopup_fill_mobile_mac);
        // Set dialog title
        layout.setTitle("Fill Mobile");
        Button updateMobile = (Button) layout.findViewById(R.id.update_mobile);
        Button noUpdate = (Button) layout.findViewById(R.id.no_update);
        mobileRecharge=(EditText) layout.findViewById(R.id.mobile_number_recharge);
        layout.show();
        noUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.dismiss();
            }
        });
        updateMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mobileNumber=mobileRecharge.getText().toString();
                    if(mobileNumber.equals("")||mobileNumber.length()==0)
                    {
                        Toast.makeText(activity, "Fill Mobile Number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    MacAddressUpdate macAddressUpdate = new MacAddressUpdate();
                    StringBuilder builder = new StringBuilder(MobicashUtils.getMacAddress(activity));
                    builder.append(mobileNumber);
                    try {
                        String hash = "";
                        hash = MobicashUtils.getSha1(builder.toString());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    macAddressUpdate.hash = MobicashUtils.getSha1(builder.toString());
                    macAddressUpdate.mobileNumbers = mobileNumber;

                    //Call Api for Mac Update
                    showProgressDialog();
                    MobicashIntentService.startActionMacUpdateRequest(getApplicationContext(),macAddressUpdate);
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
    }

    /*private void popupOtpFullscreen(final Activity activity,String mobileNo){
        //layoutOtp = new Dialog(activity,android.R.style.Theme_Holo_NoActionBar);
        //layoutOtp = new Dialog(activity,android.R.style.Theme_Holo_NoActionBar_Fullscreen);
        layoutOtp = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        layoutOtp.setContentView(R.layout.popup_otp);
        layoutOtp.setCancelable(false);
        ImageView close = layoutOtp.findViewById(R.id.ib_close);
        TextView txt = layoutOtp.findViewById(R.id.txt);
        TextView txtMobileNo = layoutOtp.findViewById(R.id.txt_mobile_no);
        TextView txtDontReceive = layoutOtp.findViewById(R.id.txt_dont_receive);
        TextView txtResend = layoutOtp.findViewById(R.id.txt_resend);
        Button verifyOtp = layoutOtp.findViewById(R.id.verify_otp);
        final Pinview pin = layoutOtp.findViewById(R.id.pinview);

        txt.setTypeface(Fonts.getRegular(activity));
        txtMobileNo.setTypeface(Fonts.getSemiBold(activity));
        txtDontReceive.setTypeface(Fonts.getRegular(activity));
        txtResend.setTypeface(Fonts.getSemiBold(activity));
        verifyOtp.setTypeface(Fonts.getSemiBold(activity));

        txtMobileNo.setText("+91-"+mobileNo);

        //pin.getValue();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutOtp.dismiss();
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String otpString = pin.getValue();
                    if (otpString.equals("") || otpString.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                    } else if(mSendOtpData!=null) {
                        showProgressDialog();
                        OTP_Response mOTOtp_response=new OTP_Response();
                        mOTOtp_response.timefrom = mSendOtpData.timefrom;
                        mOTOtp_response.code = otpString;
                        mOTOtp_response.mobile = mSendOtpData.mobile;
                        mOTOtp_response.userId = mSendOtpData.userId;
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

        layoutOtp.show();
    }*/

    // 0 for update device , 1 for verify otp

    private void popupMobileNo(final int option){
        final Dialog layout = new Dialog(SignInBaseActivity.this);
        layout.setContentView(R.layout.popup_update_device);
        TextView tv = layout.findViewById(R.id.tv);
        final EditText mobileNo =  layout.findViewById(R.id.tv_phone_number);
        TextInputLayout til =  layout.findViewById(R.id.til_mobile);
        Button cancel =  layout.findViewById(R.id.cancel_btn);
        Button ok =  layout.findViewById(R.id.confirm_btn);

        tv.setTypeface(Fonts.getRegular(SignInBaseActivity.this));
        til.setTypeface(Fonts.getRegular(SignInBaseActivity.this));
        mobileNo.setTypeface(Fonts.getSemiBold(SignInBaseActivity.this));
        cancel.setTypeface(Fonts.getSemiBold(SignInBaseActivity.this));
        ok.setTypeface(Fonts.getSemiBold(SignInBaseActivity.this));

        mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mobileNo.getText().toString().length()==10){
                    mobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_correct, 0);
                }else {
                    mobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mobileNumber = mobileNo.getText().toString();
                    if(mobileNo.length()!=10)
                    {
                        Toast.makeText(SignInBaseActivity.this, "Please enter valid mobile no.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //checking condition
                    if(option==0) {
                        MacAddressUpdate macAddressUpdate = new MacAddressUpdate();
                        StringBuilder builder = new StringBuilder(MobicashUtils.getMacAddress(SignInBaseActivity.this));
                        builder.append(mobileNumber);

                        macAddressUpdate.hash = MobicashUtils.getSha1(builder.toString());
                        macAddressUpdate.mobileNumbers = mobileNumber;

                        //Call Api for Mac Update
                        //showProgressDialog();
                        loading.setVisibility(View.VISIBLE);
                        mainLayout.setAlpha(0.5f);
                        MobicashIntentService.startActionMacUpdateRequest(mContext, macAddressUpdate);
                        layout.dismiss();

                    }else {
                        //showProgressDialog();
                        loading.setVisibility(View.VISIBLE);
                        mainLayout.setAlpha(0.5f);
                        MobicashIntentService.startActionCallOTPDetails(mContext,mobileNumber);
                        layout.dismiss();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        layout.show();
    }

    private void showPopup_FOROTP(final Activity activity) {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        layout.setContentView(R.layout.showpopup_fill_mobile_mac);
        // Set dialog title
        layout.setTitle("Fill Mobile");
        Button updateMobile = (Button) layout.findViewById(R.id.update_mobile);
        Button noUpdate = (Button) layout.findViewById(R.id.no_update);
        mobileRecharge=(EditText) layout.findViewById(R.id.mobile_number_recharge);
        layout.show();
        noUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.dismiss();
            }
        });
        updateMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mobileNumber = mobileRecharge.getText().toString();
                    if(mobileNumber.equals("")||mobileNumber.length()==0)
                    {
                        Toast.makeText(activity, "Fill Mobile Number", Toast.LENGTH_SHORT).show();
                        return;
                    }else{

                        showProgressDialog();
                        MobicashIntentService.startActionCallOTPDetails(mContext,mobileNumber);
                        layout.dismiss();
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
    }

    //Call Api Update for Mac Address
  /*  private void callApiForMacUpdate(final Activity context,String mobileNumber, String macAddress)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",mobileNumber);
            jsonObject.put("mac",macAddress);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.UPDATE_MAC_DEVICE_ID,jsonObject,new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        cancelProgressDialog();

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
    }*/

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
       // Toast.makeText(SignInBaseActivity.this,latitude+"Latitude"+longitude+"Longitude",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_sign_up:
                startActivity(IntentFactory.createUserEnrollmentActivity(getApplicationContext()));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                break;
            case R.id.btn_login:
                try {
                    if (validateSignInFields()) {
                        if (MobicashUtils.isNetworkAvailable(this)) {
                            Timber.d(" Sending API request...");
                            //showProgressDialog();
                            loading.setVisibility(View.VISIBLE);
                            mainLayout.setAlpha(0.5f);
                            mLoginRequest = new LoginRequest();
                            mLoginRequest.clientmobile = mClientMobileNo.getText().toString();
                            LocalPreferenceUtility.putUserMobileNumber(this,mLoginRequest.clientmobile);
                            mLoginRequest.pincode = MobicashUtils.md5(mClientPasscode.getText().toString());
                            mLoginRequest.hash = MobicashUtils.getSha1(mClientMobileNo.getText().toString() + MobicashUtils.md5(mClientPasscode.getText().toString()));
                            //callApiGettingStatus();
                            MobicashIntentService.startActionUserLogin(mContext, mLoginRequest);
                        } else {
                            showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.network_not_available));
                        }

                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Timber.e("catched  NoSuchAlgorithmException. Message : " + e.getMessage());
                } catch (Exception e) {
                    Timber.e("catched  Exception Message : " + e.getMessage());
                }

                break;
            case R.id.tv_send_passcode:
                startActivity(IntentFactory.createSendPasscodeActivity(getApplicationContext()));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                break;

            /*case R.id.tv_otp_verify:
               showPopup_FOROTP(SignInBaseActivity.this);
                break;*/


        }

    }

    /**
     * show text validation message.
     *
     * @param title
     * @param verificationFailedMessageString
     */
    private void showAlert(String title, String verificationFailedMessageString) {
        String message = verificationFailedMessageString;
        AlertDialog.Builder alertDialogBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(this);
        }
        alertDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * show validation dialog
     */
    private void showDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setTitle(getString(R.string.dialog_error))
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * user input validation
     *
     * @return
     */
    public boolean validateSignInFields() {
        String regex = "\\d+";
        String phoneNumber = mClientMobileNo.getText().toString();
        String passCode = mClientPasscode.getText().toString();
        //if (phoneNumber.length() != 10 && phoneNumber.matches(regex)) {
        if (phoneNumber.length() != 10) {
            Timber.d(" phone number are invalid");
            showDialog(mPhoneNumberErrorMessage);
            return false;
        }
        if (passCode.length() != 4) {
            Timber.d(" password are invalid");
            showDialog(mPasscodeErrorMessage);
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginStatusReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(loginStatusReceiver, new IntentFilter(action));
        }



        /*if (layoutOtp!=null && layoutOtp.isShowing() ){
            layoutOtp.dismiss();
        }*/



    }
    /*@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebook.onActivityResult(requestCode, resultCode, data);
        mGoogle.onActivityResult(requestCode, resultCode, data);
    }*/
    /**
     * Handling broadcast event for user login .
     */
    private BroadcastReceiver loginStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //cancelProgressDialog();
            loading.setVisibility(View.GONE);
            mainLayout.setAlpha(1f);
            try {
                if (action.equals(NetworkConstant.STATUS_USER_LOGIN_SUCCESS)) {
                    LoginResponse mLoginResponse = (LoginResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    Timber.d("STATUS_USER_LOGIN_SUCCESS = > LoginResponse  ==>" + new Gson().toJson(mLoginResponse));
                    saveClientDetailsOnLocalReference(mLoginResponse);
                    MobicashIntentService.startActionGetClientProfileInfo(getApplicationContext(), getClientProfileInfoRequest());
                    startWifyeeRegistrationRequest(mLoginResponse);
                    startActivity(IntentFactory.createDashboardActivity(getApplicationContext()));
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    finish();
                } else if (action.equals(NetworkConstant.STATUS_USER_LOGIN_FAIL)) {
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_USER_SIGNUP_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        //showErrorDialog(failureResponse.msg);
                        showErrorAlertbox(SignInBaseActivity.this,failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showErrorAlertbox(SignInBaseActivity.this,errorMessage);
                        //showErrorDialog(errorMessage);
                    }
                }
                if (action.equals(NetworkConstant.STATUS_MAC_ADDRESS_UPDATE_SUCCESS)) {
                    MacUpdateAddressResponse mMacUpdateAddressResponse = (MacUpdateAddressResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if(mMacUpdateAddressResponse!=null)
                    {
                        //ALertbox here
                        Log.e("calling","calling1");
                        Log.e("msg",mMacUpdateAddressResponse.msg);
                        showSuccessAlertbox(SignInBaseActivity.this,mMacUpdateAddressResponse.msg);
                        //showSuccessDialogMsg(SignInBaseActivity.this,mMacUpdateAddressResponse.msg);
                    }
                    Timber.d("STATUS_USER_LOGIN_SUCCESS = > LoginResponse  ==>" + new Gson().toJson(mMacUpdateAddressResponse));

                } else if (action.equals(NetworkConstant.STATUS_MAC_ADDRESS_UPDATE_FAIL)) {
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_USER_SIGNUP_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        showErrorAlertbox(SignInBaseActivity.this,failureResponse.msg);
                        //showErrorDialog(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showErrorAlertbox(SignInBaseActivity.this,errorMessage);
                        //showErrorDialog(errorMessage);
                    }
                }
                if (action.equals(NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_SUCCESS)) {

                    GetClientProfileInfoResponse getClientProfileInfoResponse = (GetClientProfileInfoResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (getClientProfileInfoResponse != null
                            && getClientProfileInfoResponse.clientProfileInfo.mobicashClientProfile != null) {
                        GetProfileInfo getProfileInfo = getClientProfileInfoResponse.clientProfileInfo.mobicashClientProfile;
                        if (getProfileInfo != null && getProfileInfo.clientWalletBalance != null
                                && getProfileInfo.clientApprovedCreditLimit != null
                                && getProfileInfo.clientProfileImage != null
                                &&getProfileInfo.client_firstname!= null&&getProfileInfo.client_lastname!=null&&
                                getProfileInfo.client_email!=null)
                            firstName=getProfileInfo.client_firstname;
                        LocalPreferenceUtility.putUserFirstName(context,firstName);
                        lastName=getProfileInfo.client_lastname;
                        LocalPreferenceUtility.putUserFirstName(context,lastName);
                        clientEmail=getProfileInfo.client_email;
                        LocalPreferenceUtility.putUserFirstName(context,clientEmail);
                    }
                    }
                    if (action.equals(NetworkConstant.STATUS_USER_OTP_SUCCESS)) {
                    //mLoginButton.setEnabled(false);
                    OTP_Response otp_response = (OTP_Response) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    mSendOtpData =new OTP_Response();
                    if(otp_response!=null)
                    {
                        mSendOtpData = otp_response;
                        Intent otp = new Intent(SignInBaseActivity.this,OTPVerified.class);
                        otp.putExtra("mobile_no",mSendOtpData.mobile);
                        otp.putExtra("timefrom",mSendOtpData.timefrom);
                        otp.putExtra("userId",mSendOtpData.userId);
                        startActivity(otp);
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                        //popupOtpFullscreen(SignInBaseActivity.this,mobileNumber);

                      /*  String code=otp_response.code;
                        String timeFrom=otp_response.timefrom;
                        String mobile=otp_response.mobile;*/

                        //otp_layout.setVisibility(View.VISIBLE);

                        /*resend_otp.setEnabled(true);
                        tv_otp.setEnabled(true);
                        btn_otp.setEnabled(true);*/
                        //showOTPDialog(code,timeFrom,mobile);
                    }
                    else{
                        mLoginButton.setEnabled(true);
                        //otp_layout.setVisibility(View.INVISIBLE);
                    }

                } else if (action.equals(NetworkConstant.STATUS_USER_OTP_FAIL)){
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_USER_SIGNUP_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        //showErrorDialog(failureResponse.msg);
                        showErrorAlertbox(SignInBaseActivity.this,failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        //showErrorDialog(errorMessage);
                        showErrorAlertbox(SignInBaseActivity.this,errorMessage);
                    }
                    //  mSignupButton.setEnabled(true);
                    /*Toast.makeText(getApplicationContext(),"Some Error Occured in Otp .Please try after some time",Toast.LENGTH_SHORT).show();
                    otp_layout.setVisibility(View.INVISIBLE);*/

                }
                //OTP
                if (action.equals(NetworkConstant.STATUS_USER_CONFIRMATION_OTP_SUCCESS)) {

                    showSuccessAlertbox(SignInBaseActivity.this,"OTP verified successfully.");
                    //showSuccessDialog(SignInBaseActivity.this);

                } else if (action.equals(NetworkConstant.STATUS_USER_CONFIRMATION_OTP_FAIL)){
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_USER_SIGNUP_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                        System.out.println("failureResponse"+failureResponse);
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        showErrorAlertbox(SignInBaseActivity.this,failureResponse.msg);
                    } else {
                        // String errorMessage = getString(R.string.error_message);
                        String errorMessage ="Check Your Internet Connection.";
                        showErrorAlertbox(SignInBaseActivity.this,errorMessage);
                    }
                    // Toast.makeText(getApplicationContext(),"Some Error Occured in Confirmation the  Otp .Please try after some time",Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                Timber.e(" Exception caught in loginStatusReceiver " + e.getMessage());
            }
        }
    };

    private void showSuccessDialog(final Activity activity) {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_success_signup);
        // Set dialog title
        layout.setTitle("OTP Verified Successfully.");
        Button yes = (Button) layout.findViewById(R.id.yes);
        layout.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   //otp_layout.setVisibility(View.INVISIBLE);
                   mLoginButton.setEnabled(true);
                    layout.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    private void showSuccessAlertbox(Activity activity,String msg){
        pDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("Done");
        pDialog.setContentText(msg);
        pDialog.show();
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
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

    private void showSuccessDialogMsg(final Activity activity,String msg) {
        final Dialog layouts = new Dialog(activity);
        layouts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layouts.setContentView(R.layout.showpopup_success_signup);
        // Set dialog title
        layouts.setTitle(msg);
        Button yes = (Button) layouts.findViewById(R.id.yes);
        layouts.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    layouts.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    private GetClientProfileInfoRequest getClientProfileInfoRequest() {
        GetClientProfileInfoRequest getClientProfileInfoRequest = null;
        getClientProfileInfoRequest = new GetClientProfileInfoRequest();
        getClientProfileInfoRequest.clientId = LocalPreferenceUtility.getUserCode(getApplicationContext());
        getClientProfileInfoRequest.pincode = LocalPreferenceUtility.getUserPassCode(getApplicationContext());
        try {
            getClientProfileInfoRequest.hash = MobicashUtils.getSha1(getClientProfileInfoRequest.clientId
                    + MobicashUtils.md5(getClientProfileInfoRequest.pincode));
        } catch (Exception e) {
        }

        return getClientProfileInfoRequest;
    }
    /**
     * save details on local preference
     */
    private void saveClientDetailsOnLocalReference(LoginResponse mLoginResponse) {
        LocalPreferenceUtility.putUserMobileNumber(getApplicationContext(), mLoginResponse.clientMobile);
        LocalPreferenceUtility.putUserCode(getApplicationContext(), mLoginResponse.clientId);
        LocalPreferenceUtility.putUserPassCode(getApplicationContext(), mClientPasscode.getText().toString());
        LocalPreferenceUtility.putDeviceMacID(getApplicationContext(),MobicashUtils.getMacAddress(this));
        //Update Password on Wifyee server
//        showProgressDialog();
        updatePasswordOnWifyee(this,mLoginResponse.clientMobile,mClientPasscode.getText().toString());
    }
   //Update Password on Wifyee Server

    private void updatePasswordOnWifyee(final Context context, String clientMobile, String password)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_customercode","95629568");
            jsonObject.put("req_action","UPDATE");
            jsonObject.put("req_username",clientMobile+"@wifyee");
            jsonObject.put("req_password",password);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.UPDATE_PASSWORD_WIYEE_SERVER ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    loading.setVisibility(View.GONE);
                    mainLayout.setAlpha(1f);
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        //cancelProgressDialog();
                        Toast.makeText(context, "Successfully Updated Password", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //cancelProgressDialog();
                        Toast.makeText(context, "Issue in Update Password", Toast.LENGTH_SHORT).show();
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
                loading.setVisibility(View.GONE);
                mainLayout.setAlpha(1f);
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
     * start wifyee registration request after successfully login
     */
    private void startWifyeeRegistrationRequest(LoginResponse mLoginResponse){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(WifiConstant.WIFYEE_REG_USER_NAME, mLoginResponse.clientId);
            jsonObject.put(WifiConstant.WIFYEE_REG_PASSWORD, mLoginResponse.passCode);
            jsonObject.put(WifiConstant.WIFYEE_REG_IP,  MobicashUtils.getIPAddress(this));
            jsonObject.put(WifiConstant.WIFYEE_REG_MAC, MobicashUtils.getMacAddress(this));
            jsonObject.put(WifiConstant.WIFYEE_REG_RES_URL, WifiConstant.WIFYEE_DEFAULT_URL);
            /*jsonObject.put(WifiConstant.WIFYEE_REG_RES_LONGITUDE, longitude);
            jsonObject.put(WifiConstant.WIFYEE_REG_RES_LATITUDE, latitude);*/
            jsonObject.put(WifiConstant.WIFYEE_REG_USER_FIRST_NAME,LocalPreferenceUtility.getUserFirstsName(this));
            jsonObject.put(WifiConstant.WIFYEE_REG_USER_LAST_NAME,LocalPreferenceUtility.getUserLastName(this));
            jsonObject.put(WifiConstant.WIFYEE_REG_USER_EMAIL,LocalPreferenceUtility.getUserEmail(this));
            jsonObject.put(WifiConstant.WIFYEE_REG_USER_MOBILE_PHONE,LocalPreferenceUtility.getUserMobileNumber(this));

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        MobicashIntentService.startActionWifyeeUserRegistration(this,jsonObject.toString());
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }
    }


   /* @Override
    public void onGoogleAuthSignIn(String authToken, String userId, String displayName, Uri photoUri, String givenName, String Email)
    {
        Log.e("Auth Token",String.format(Locale.US, "User id:%s\n\nAuthToken:%s", userId, authToken));
        if(displayName!=null) {

        }
        else {
            overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
            startActivity(new Intent(this,MobicashDashBoardActivity.class));
        }

    }

    @Override
    public void onGoogleAuthSignInFailed(String errorMessage) {

        Toast.makeText(this,"Error Sign Via Google" +errorMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoogleAuthSignOut() {

    }*/

   /* @Override public void onFbSignInFail(String errorMessage) {
        Toast.makeText(this,"Error Sign in Via facebook" +errorMessage,Toast.LENGTH_SHORT).show();

    }

    @Override public void onFbSignInSuccess(LoginResult loginResults, String userId) {
        setFacebookdata(loginResults);
        Log.e("Auth Token",String.format(Locale.US, "User id:%s\n\nAuthToken:%s", userId, loginResults.getAccessToken().getToken()));
       // finish();
       // overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
       // startActivity(new Intent(this,MobicashDashBoardActivity.class));
    }

   //set Facebook data
    private void setFacebookdata(LoginResult authToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                authToken.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response",response.toString());
                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String gender = response.getJSONObject().getString("gender");



                            Profile profile = Profile.getCurrentProfile();
                            String id = profile.getId();
                            String link = profile.getLinkUri().toString();
                            Log.i("Link",link);
                            if (Profile.getCurrentProfile()!=null)
                            {
                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                            }

                            Log.i("Login" + "Email", email);
                            Log.i("Login"+ "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);
                            Log.i("Login" + "Gender", gender);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override public void onFBSignOut() {
        Log.e("Signed out of Facebook","Signed out of Facebook");

    }
*/
}
