package com.wifyee.greenfields.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.constants.WifiConstant;
import com.wifyee.greenfields.mapper.ModelMapper;
import com.wifyee.greenfields.models.requests.SignupRequest;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.OTP_Response;
import com.wifyee.greenfields.models.response.SignupResponse;
import com.wifyee.greenfields.services.MobicashIntentService;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import timber.log.Timber;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SignUpActivity extends BaseActivity  {

    @BindView(R.id.tv_first_name)
    EditText mFirstName;

    @BindView(R.id.tv_last_name)
    EditText mLastName;

    @BindView(R.id.tv_phone_number)
    EditText mPhoneNumber;

   /* @BindView(R.id.tv_customer_dob)
    EditText mDateOfBirth;*/

    @BindView(R.id.tv_customer_address)
    EditText mAddress;

    @BindView(R.id.otp_layout)
    LinearLayout otp_layout;

    @BindView(R.id.tv_otp)
    EditText tv_otp;

    @BindView(R.id.resend_otp)
    TextView resend_otp;

    @BindView(R.id.submit_otp)
    Button btn_otp;

    @BindView(R.id.radio_grp)
    RadioGroup mRadio_grp;

    @BindView(R.id.male)
    RadioButton mMale;

    @BindView(R.id.female)
    RadioButton mFemale;

    @BindView(R.id.tv_password)
    EditText mPassword;

    @BindView(R.id.tv_pin_code)
    EditText mPincode;

    @BindView(R.id.btn_sign_up)
    Button mSignupButton;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.progressbar)
    SpinKitView loading;

    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;

    //@BindView(R.id.toolbar_back)
    //ImageButton back;

    // Validation error messages

    @BindString(R.string.firstname_error_message)
    String mFirstNameErrorMessage;

    @BindString(R.string.lastname_error_message)
    String mLastNameErrorMessage;

    @BindString(R.string.phone_number_error_message)
    String mPhoneNumberErrorMessage;

    @BindString(R.string.email_error_message)
    String mEmailErrorMessage;

    @BindString(R.string.passcode_error_message)
    String mPasscodeErrorMessage;

    @BindString(R.string.pincode_error_message)
    String mPincodeErrorMessage;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private LocationTrack locationTrack;

    private Context mContext = null;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String[] genderArray={"Select Gender","Male","Female"};
    private String gender="";
    private String selectedItem="";
    private ProgressDialog progressDialog = null;
    private OTP_Response mSendOtpData;

    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList= {
            NetworkConstant.STATUS_USER_SIGNUP_FAIL,
            NetworkConstant.STATUS_USER_SIGNUP_SUCCESS,
            NetworkConstant.STATUS_USER_OTP_FAIL,
            NetworkConstant.STATUS_USER_OTP_SUCCESS,
            NetworkConstant.STATUS_USER_CONFIRMATION_OTP_SUCCESS,
            NetworkConstant.STATUS_USER_CONFIRMATION_OTP_FAIL

    };

    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_new);
        ButterKnife.bind(this);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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

            /*back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });*/
        }

        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        TextView txtGender = findViewById(R.id.txt_gender);
        TextInputLayout tilFname = findViewById(R.id.til_fname);
        TextInputLayout tilLname = findViewById(R.id.til_lname);
        TextInputLayout tilMobile = findViewById(R.id.til_mobile);
        TextInputLayout tilPassword = findViewById(R.id.til_password);
        TextInputLayout tilZipcode = findViewById(R.id.til_pincode);

        txtGender.setTypeface(Fonts.getSemiBold(this));
        mFirstName.setTypeface(Fonts.getSemiBold(this));
        mLastName.setTypeface(Fonts.getSemiBold(this));
        mPhoneNumber.setTypeface(Fonts.getSemiBold(this));
        mPassword.setTypeface(Fonts.getSemiBold(this));
        mPincode.setTypeface(Fonts.getSemiBold(this));
        tilMobile.setTypeface(Fonts.getRegular(this));
        tilPassword.setTypeface(Fonts.getRegular(this));
        tilFname.setTypeface(Fonts.getRegular(this));
        tilLname.setTypeface(Fonts.getRegular(this));
        tilZipcode.setTypeface(Fonts.getRegular(this));
        mMale.setTypeface(Fonts.getRegular(this));
        mFemale.setTypeface(Fonts.getRegular(this));
        mSignupButton.setTypeface(Fonts.getSemiBold(this));
        toolBarTitle.setTypeface(Fonts.getSemiBold(this));

        //   mSignupButton.setOnClickListener(this);

        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mFirstName.getText().toString().length()>1){
                    mFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_correct, 0);
                }else {
                    mFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mLastName.getText().toString().length()>1){
                    mLastName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_correct, 0);
                }else {
                    mLastName.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mPhoneNumber.getText().toString().length()==10){
                    mPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_correct, 0);
                }else {
                    mPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mPincode.getText().toString().length()==6){
                    mPincode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_correct, 0);
                }else {
                    mPincode.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    if (MobicashUtils.isNetworkAvailable(SignUpActivity.this)) {
                        Timber.d(" Sending API request for Signup...");
                        //showProgressDialog();
                        loading.setVisibility(View.VISIBLE);
                        mainLayout.setAlpha(0.5f);
                        mSignupButton.setEnabled(false);
                        mSignupButton.setAlpha(0.6f);
                        SignupRequest mSignupRequest = new SignupRequest();
                        mSignupRequest.clientmobile = mPhoneNumber.getText().toString().trim();
                        mSignupRequest.firstname = mFirstName.getText().toString().trim();
                        mSignupRequest.lastname = mLastName.getText().toString().trim();
                        mSignupRequest.pincode = mPassword.getText().toString().trim();
                        mSignupRequest.customerTitle=gender;
                        // mSignupRequest.customerDOB=mDateOfBirth.getText().toString().trim();
                        mSignupRequest.custAddess = mAddress.getText().toString().trim();
                        mSignupRequest.email = mPhoneNumber.getText().toString().trim()+"@wifyee.com";
                        try {
                            mSignupRequest.hash = MobicashUtils.getSha1(mPhoneNumber.getText().toString() + mFirstName.getText().toString() + mLastName.getText().toString() + mPassword.getText().toString()+mPhoneNumber.getText().toString().trim()+"@wifyee.com");
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        MobicashIntentService.startActionUserSignUp(mContext,mSignupRequest);
                    } else {
                        showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.network_not_available));
                    }

                    //startWifyeeRegistration(SignUpActivity.this);
                }
            }
        });

        otp_layout.setVisibility(View.GONE);
        /*resend_otp.setEnabled(false);
        tv_otp.setEnabled(false);
        btn_otp.setEnabled(false);*/
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clientMobile=LocalPreferenceUtility.getUserMobileNumber(SignUpActivity.this);
                if (clientMobile.length()==0||clientMobile.equals("")) {
                    Toast.makeText(getApplicationContext(),"Mobile Number not found .Please try after some time.",Toast.LENGTH_SHORT).show();
                } else {
                    //showProgressDialog();
                    loading.setVisibility(View.VISIBLE);
                    mainLayout.setAlpha(0.5f);
                    MobicashIntentService.startActionCallOTPDetails(mContext,clientMobile);
                }
            }
        });

        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String otpString = tv_otp.getText().toString();
                    if (otpString.equals("") || otpString.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Fill OTP", Toast.LENGTH_SHORT).show();
                    } else if(mSendOtpData!=null) {
                        //showProgressDialog();
                        loading.setVisibility(View.VISIBLE);
                        mainLayout.setAlpha(0.5f);
                        OTP_Response mOTOtp_response=new OTP_Response();
                        mOTOtp_response.timefrom = mSendOtpData.timefrom;
                        mOTOtp_response.code = otpString;
                        mOTOtp_response.mobile = mSendOtpData.mobile;
                        mOTOtp_response.responseCode = LocalPreferenceUtility.getUserCode(SignUpActivity.this);
                        mOTOtp_response.userId = LocalPreferenceUtility.getUserCode(SignUpActivity.this);
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

        mRadio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.female:
                        gender="Mr.";
                        break;
                    case R.id.male:
                        gender="Mr.";
                        break;

                }
            }
        });

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        locationTrack = new LocationTrack(SignUpActivity.this);


        if (locationTrack.canGetLocation()) {

            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(SignUpActivity.this, Locale.getDefault());
            try {
                if(latitude!=0.0||longitude!=0.0) {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null) {
                        String address = addresses.get(0).getAddressLine(0);
                        mAddress.setText(address);
                    }
                }
               /* String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();*/// Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {

            locationTrack.showSettingsAlert();
        }
    }


    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
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

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(SignUpActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

    //Set Date Picker
    private void showDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        Date chosenDate = cal.getTime();
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                        String datetime = format.format(chosenDate);
                        //mDateOfBirth.setText(datetime);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //Formatted Date from String
    public String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){
        Date parsed = null;
        String outputDate = "";
        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());
        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {
            // LOG(TAG, "ParseException - dateFormat");
        }
        return outputDate;
    }

   /* @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_sign_up:
                try {
                    if (validate()) {
                        if (MobicashUtils.isNetworkAvailable(this)) {
                            Timber.d(" Sending API request for Signup...");
                            showProgressDialog();
                            SignupRequest mSignupRequest = new SignupRequest();
                            mSignupRequest.clientmobile = mPhoneNumber.getText().toString().trim();
                            mSignupRequest.firstname = mFirstName.getText().toString().trim();
                            mSignupRequest.lastname = mLastName.getText().toString().trim();
                            mSignupRequest.pincode = mPassword.getText().toString().trim();
                            mSignupRequest.customerTitle=gender;
                            // mSignupRequest.customerDOB=mDateOfBirth.getText().toString().trim();
                            mSignupRequest.custAddess=mAddress.getText().toString().trim();
                            mSignupRequest.email =mPhoneNumber.getText().toString().trim()+"@wifyee.com";
                            mSignupRequest.hash = MobicashUtils.getSha1(mPhoneNumber.getText().toString() + mFirstName.getText().toString() + mLastName.getText().toString() + mPassword.getText().toString()+mPhoneNumber.getText().toString().trim()+"@wifyee.com");
                            MobicashIntentService.startActionUserSignUp(mContext,mSignupRequest);
                        } else {
                            showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.network_not_available));
                        }

                        //startWifyeeRegistration(SignUpActivity.this);
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Timber.e("catched  NoSuchAlgorithmException. Message : " + e.getMessage());
                } catch (Exception e) {
                    Timber.e("catched  Exception Message : " + e.getMessage());
                }
                break;
        }
    }*/


    private boolean validate() {
        if (mFirstName.getText().toString().trim().length() == 0) {
            showDialog(mFirstNameErrorMessage);
            return false;
        }else {
            Pattern ps = Pattern.compile("^\\w+(\\s\\w+)*$");
            Matcher ms = ps.matcher(mFirstName.getText().toString());
            boolean bs = ms.matches();
            if (bs == false) {
                showDialog("Please enter only charecter or remove White Space");
            }
        }
        if (mLastName.getText().toString().trim().length() == 0) {
            showDialog(mLastNameErrorMessage);
            return false;
        }else {
            Pattern ps = Pattern.compile("^\\w+(\\s\\w+)*$");
            Matcher ms = ps.matcher(mLastName.getText().toString());
            boolean bs = ms.matches();
            if (bs == false) {
                showDialog("Please enter only character or remove White Space");
            }
        }

        if (mPhoneNumber.getText().toString().trim().length() != 10) {
            showDialog(mPhoneNumberErrorMessage);
            return false;
        }
        if (mPassword.getText().toString().trim().length() == 0
                || mPassword.getText().toString().length() != 4) {
            showDialog(mPasscodeErrorMessage);
            return false;
        }

        if (mPincode.getText().toString().trim().length() == 0
                || mPincode.getText().toString().length() != 6) {
            showDialog(mPincodeErrorMessage);
            return false;
        }

        return true;
    }


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
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
                    // connectWifi(strWiFyeeSSID,activity);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    startActivity(IntentFactory.createUserLoginActivity(getApplicationContext()));
                    layout.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(signupStatusReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(signupStatusReceiver, new IntentFilter(action));
        }

    }
    /**
     * Handling broadcast event for user Signup .
     */
    private BroadcastReceiver signupStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //cancelProgressDialog();
            loading.setVisibility(View.GONE);
            mainLayout.setAlpha(1f);
            mSignupButton.setEnabled(true);
            mSignupButton.setAlpha(1f);
            try {
                if (action.equals(NetworkConstant.STATUS_USER_SIGNUP_SUCCESS)) {
                   final SignupResponse signupResponse = (SignupResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    Timber.d("STATUS_USER_SIGNUP_SUCCESS = > signupResponse  ==>" + new Gson().toJson(signupResponse));

                    FirebaseMessaging.getInstance().subscribeToTopic(mPincode.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                            }else {
                                Log.i(TAG,"Successfully subscribeToTopic "+mPincode.getText().toString());
                            }
                        }
                    });
                    //showProgressDialog();
                    loading.setVisibility(View.VISIBLE);
                    mainLayout.setAlpha(0.5f);
                    mSignupButton.setEnabled(false);
                    mSignupButton.setAlpha(0.6f);
                    startWifyeeRegistrationRequest(signupResponse);
                    startWifyeeRegistration(SignUpActivity.this);
                    MobicashIntentService.startActionCallOTPDetails(mContext,signupResponse.clientMobile);
                   /* runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callSendApi(SignUpActivity.this,signupResponse.clientMobile);
                        }
                    });*/

                    saveDataToLocalPreferences(signupResponse);
                    //onSuccess("Msg","Successfuly Register.Please Confirm Your Otp Number.");

                    //saveDataToLocalPreferences(signupResponse);
                    mSignupButton.setEnabled(false);

                } else if (action.equals(NetworkConstant.STATUS_USER_SIGNUP_FAIL)) {
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_USER_SIGNUP_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        showErrorDialog(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showErrorDialog(errorMessage);
                    }
                    mSignupButton.setEnabled(true);
                }
                if (action.equals(NetworkConstant.STATUS_USER_OTP_SUCCESS)) {
                    //cancelProgressDialog();
                    loading.setVisibility(View.GONE);
                    mainLayout.setAlpha(1f);
                    mSignupButton.setEnabled(false);
                    mSignupButton.setAlpha(0.6f);
                    OTP_Response otp_response = (OTP_Response) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    mSendOtpData = new OTP_Response();

                    mSendOtpData = otp_response;
                    if(otp_response!=null) {
                        pDialog = new SweetAlertDialog(SignUpActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setTitleText("Done");
                        pDialog.setContentText("You are successfully register. Please Confirm Your Otp Number");
                        pDialog.show();
                        pDialog.setCancelable(false);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                pDialog.dismiss();
                                onStop();
                                Intent otp = new Intent(SignUpActivity.this, OTPVerified.class);
                                otp.putExtra("mobile_no", mSendOtpData.mobile);
                                otp.putExtra("timefrom", mSendOtpData.timefrom);
                                otp.putExtra("userId", mSendOtpData.userId);
                                startActivity(otp);
                                finish();
                                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                        });
                    }
                    /*if(otp_response!=null)
                    {
//                        mSendOtpData = otp_response;
//                        Intent otp = new Intent(SignUpActivity.this,OTPVerified.class);
//                        otp.putExtra("mobile_no",mSendOtpData.mobile);
//                        otp.putExtra("timefrom",mSendOtpData.timefrom);
//                        otp.putExtra("userId",mSendOtpData.userId);
//                        startActivity(otp);
//                        finish();
//                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//                        String code=otp_response.code;
//                        String timeFrom=otp_response.timefrom;
//                        String mobile=otp_response.mobile;
//                       //otp_layout.setVisibility(View.VISIBLE);
//                        resend_otp.setEnabled(true);
//                        tv_otp.setEnabled(true);
//                        btn_otp.setEnabled(true);
//                        //showOTPDialog(code,timeFrom,mobile);
                    }*/
                    else{
                        mSignupButton.setEnabled(true);
                        mSignupButton.setAlpha(1f);
                        otp_layout.setVisibility(View.GONE);
                    }

                } else if (action.equals(NetworkConstant.STATUS_USER_OTP_FAIL)){
                    //cancelProgressDialog();
                    loading.setVisibility(View.GONE);
                    mainLayout.setAlpha(1f);
                    mSignupButton.setEnabled(true);
                    mSignupButton.setAlpha(1f);
                    //  mSignupButton.setEnabled(true);
                    Toast.makeText(getApplicationContext(),"Some Error Occured in Otp .Please try after some time",Toast.LENGTH_SHORT).show();
                    otp_layout.setVisibility(View.GONE);

                }

                //otp
                if (action.equals(NetworkConstant.STATUS_USER_CONFIRMATION_OTP_SUCCESS)) {
                    //cancelProgressDialog();
                    loading.setVisibility(View.GONE);
                    mainLayout.setAlpha(1f);
                    mSignupButton.setEnabled(true);
                    mSignupButton.setAlpha(1f);
                    showSuccessDialog(SignUpActivity.this);

                } else if (action.equals(NetworkConstant.STATUS_USER_CONFIRMATION_OTP_FAIL)){
                    //cancelProgressDialog();
                    loading.setVisibility(View.GONE);
                    mainLayout.setAlpha(1f);
                    mSignupButton.setEnabled(true);
                    mSignupButton.setAlpha(1f);
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_USER_SIGNUP_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                        System.out.println("failureResponse"+failureResponse);
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        showErrorDialog(failureResponse.msg);
                    } else {
                        // String errorMessage = getString(R.string.error_message);
                        String errorMessage ="Check Your Internet Connection.";
                        showErrorDialog(errorMessage);
                    }
                    // Toast.makeText(getApplicationContext(),"Some Error Occured in Confirmation the  Otp .Please try after some time",Toast.LENGTH_SHORT).show();

                }
                if (intent.getAction().equalsIgnoreCase("otp")) {

                    final String message = intent.getStringExtra("message");
                  /*  Toast.mak
                    your_edittext.setText(message);*/
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Timber.e(" Exception caught in signupStatusReceiver " + e.getMessage());
            }
        }
    };

    private void onSuccess(String title, String statusMessage) {
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
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

   /* //generate Otp API
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
                        onSuccess(getString(R.string.signup_status_title), getString(R.string.registration_success),code,
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
    }*/

    /**
     * put details on local preference
     * @param signupResponse
     */
    private void saveDataToLocalPreferences(SignupResponse signupResponse) {
        LocalPreferenceUtility.putUserEmail(getApplicationContext(),mPhoneNumber.getText().toString().trim()+"@wifyee.com");
        LocalPreferenceUtility.putUserFirstName(getApplicationContext(),mFirstName.getText().toString().trim());
        LocalPreferenceUtility.putUserMobileNumber(getApplicationContext(),mPhoneNumber.getText().toString().trim());
        LocalPreferenceUtility.putUserLastName(getApplicationContext(),mLastName.getText().toString().trim());
        LocalPreferenceUtility.putUserPassCode(getApplicationContext(),mPassword.getText().toString().trim());
        LocalPreferenceUtility.putUserCode(getApplicationContext(),signupResponse.clientId);
        Log.e("client id","client Id "+signupResponse.clientId);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            LocalBroadcastManager.getInstance(SignUpActivity.this).unregisterReceiver(signupStatusReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    /**
     * start wifyee registration request after successfully SignUp
     */
    private void startWifyeeRegistrationRequest(SignupResponse mLoginResponse){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(WifiConstant.WIFYEE_REG_USER_NAME, mLoginResponse.clientId);
            jsonObject.put(WifiConstant.WIFYEE_REG_PASSWORD, mPassword.getText().toString().trim());
            jsonObject.put(WifiConstant.WIFYEE_REG_IP,  MobicashUtils.getIPAddress(this));
            jsonObject.put(WifiConstant.WIFYEE_REG_MAC, MobicashUtils.getMacAddress(this));
            jsonObject.put(WifiConstant.WIFYEE_REG_RES_URL, WifiConstant.WIFYEE_DEFAULT_URL);
            /*jsonObject.put(WifiConstant.WIFYEE_REG_RES_LONGITUDE, longitude);
            jsonObject.put(WifiConstant.WIFYEE_REG_RES_LATITUDE, latitude);*/
            jsonObject.put(WifiConstant.WIFYEE_REG_USER_FIRST_NAME,mFirstName.getText().toString().trim());
            jsonObject.put(WifiConstant.WIFYEE_REG_USER_LAST_NAME,mLastName.getText().toString().trim());
            jsonObject.put(WifiConstant.WIFYEE_REG_USER_EMAIL,mPhoneNumber.getText().toString().trim()+"@wifyee.com");
            jsonObject.put(WifiConstant.WIFYEE_REG_USER_MOBILE_PHONE,mPhoneNumber.getText().toString().trim());

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        MobicashIntentService.startActionWifyeeUserRegistration(this,jsonObject.toString());
    }

    /*private void startWifyeeReg(Context activity)
    {
        AndroidNetworking.get(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_GET_CLIENT_PROFILE_INFO_END_POINT)
                .addQueryParameter(NetworkConstant.PARAM_CLIENT_ID, mGetClientProfileInfoRequest.clientId)
                .addQueryParameter(NetworkConstant.PARAM_HASH, mGetClientProfileInfoRequest.hash)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Get Client Profile Info API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                GetClientProfileInfoResponse mGetClientProfileInfoResponse = ModelMapper.transformJSONObjectTomGetClientProfileInfoResponse(response);
                                Timber.d("Got Success GetClientProfileInfoResponse...");

                                Timber.d("handleActionGetClientProfileInfo = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionGetClientProfileInfo = > GetClientProfileInfoResponse mGetClientProfileInfoResponse ==>" + new Gson().toJson(mGetClientProfileInfoResponse));


                            } else {
                                Timber.d("Got failure in GetClientProfileInfoResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionGetClientProfileInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Get Client Profile Info API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionGetClientProfileInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                    }
                });
    }*/

    //Register in Wifyee Soft
    private void startWifyeeRegistration(final Context activity) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_password", mPassword.getText().toString().trim());
            jsonObject.put("req_repassword", mPassword.getText().toString().trim());
            jsonObject.put("req_planid", "14");
            jsonObject.put("req_locationid", "5506");
            jsonObject.put("req_firstname",mFirstName.getText().toString().trim());
            jsonObject.put("req_lastname",mLastName.getText().toString().trim());
            jsonObject.put("req_username",mPhoneNumber.getText().toString().trim());
            jsonObject.put("req_usermac", MobicashUtils.getMacAddress(this));
            jsonObject.put("callback", "65464812313265");
            jsonObject.put("req_customercode", "95629568");
            jsonObject.put("req_action","create");
            LocalPreferenceUtility.putUserEmail(this,(mPhoneNumber.getText().toString().trim()+"@wifyee.com"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        AndroidNetworking.post(NetworkConstant.WIFYEE_HOTSPOT_REGISTRATION)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("AfterReg1",response.toString());
                        try {
                            if (response != null) {
                                Timber.d("Got Success GetRegisterInfoResponse...");
                                Timber.d("handleActionGetRegisterInfo = > JSONObject response ==>" + new Gson().toJson(response));
                                //cancelProgressDialog();
                                loading.setVisibility(View.GONE);
                                mainLayout.setAlpha(1f);
                            } else {
                                Timber.d("Got failure in GetRegisterInfoResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionGetRegisterInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                            }
                        } catch (Exception e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                            //cancelProgressDialog();
                            loading.setVisibility(View.GONE);
                            mainLayout.setAlpha(1f);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Get Register Info API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        //cancelProgressDialog();
                        loading.setVisibility(View.GONE);
                        mainLayout.setAlpha(1f);
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionGetRegisterInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                    }
                });
    }
}
