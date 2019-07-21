package com.wifyee.greenfields.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.wifyee.greenfields.MobicashApplication;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.CircularNetworkImageView;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.activity.UploadPrescription;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.requests.ClientProfileUpdateRequest;
import com.wifyee.greenfields.models.requests.GetClientProfileRequest;
import com.wifyee.greenfields.models.requests.UploadClientPictureRequest;
import com.wifyee.greenfields.models.response.ClientProfileUpdateResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.GetClientProfileResponse;
import com.wifyee.greenfields.models.response.GetProfile;
import com.wifyee.greenfields.models.response.Profile;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by sumanta on 12/23/16.
 */

public class ProfileFragment extends android.support.v4.app.Fragment {


    public static String userLastName="";
    public interface ProfileFragmentListner {

        void overridePendingTransition();

        void showError(String msg);

        void showProgress();

        void cancelProgress();

    }


    @BindView(R.id.edit_text_fname)
    EditText mFirstNameEditTextView;

    @BindView(R.id.edit_text_lname)
    EditText mLastNameEditTextView;

    @BindView(R.id.edit_text_user_name)
    EditText mUserNameEditTextView;

    @BindView(R.id.btn_dob)
    Button mDateOfBirthButton;

    @BindView(R.id.edit_text_address)
    EditText mAddressEditTextView;

    @BindView(R.id.edit_text_city_town)
    EditText mCityOrTownEditTextView;

    @BindView(R.id.edit_text_postal_code)
    EditText mPostalCodeEditTextView;

    @BindView(R.id.button_country)
    Button mCountryButton;

    @BindView(R.id.btn_language)
    Button mLanguageButton;

    @BindView(R.id.edit_text_email)
    EditText mEmailEditTextView;

    @BindView(R.id.edit_text_adhaar)
    EditText mAdhaarEditTextView;

    @BindView(R.id.edit_text_pan)
    EditText mPanEditTextView;

    @BindView(R.id.user_image)
    CircularNetworkImageView mUserImageView;

    @BindView(R.id.btn_update)
    Button mUpdateButton;

    @BindString(R.string.english_code)
    String mEnglishCode;

    @BindString(R.string.hindi_code)
    String mHindiCode;

    @BindString(R.string.english)
    String mEnglishLanguage;

    @BindString(R.string.hindi)
    String mHindiLanguage;

    @BindString(R.string.india_code)
    String mIndiaCode;

    @BindString(R.string.india)
    String mIndiaCountry;
    // Validation error messages
    @BindString(R.string.empty_clientid_or_passcode_or_hash_error_message)
    String mEmptyClientIdOrPasscodeOrHashErrorMessage;

    @BindString(R.string.firstname_error_message)
    String mFirstNameErrorMessage;

    @BindString(R.string.lastname_error_message)
    String mLastNameErrorMessage;

    @BindString(R.string.username_error_message)
    String mUserNameErrorMessage;

    @BindString(R.string.language_error_message)
    String mLanguageErrorMessage;

    @BindString(R.string.email_error_message)
    String mEmailErrorMessage;

    @BindString(R.string.zipcode_error_message)
    String mZipCodeErrorMessage;

    @BindString(R.string.adhaar_number_error_message)
    String mAdhaarNoErrorMessage;

    @BindString(R.string.pan_number_error_message)
    String mPanNoErrorMessage;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;

    /**
     * List of actions supported.
     */

    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_GET_CLIENT_PROFILE_SUCCESS,
            NetworkConstant.STATUS_GET_CLIENT_PROFILE_FAIL,
            NetworkConstant.STATUS_CLIENT_PROFILE_UPDATE_SUCCESS,
            NetworkConstant.STATUS_CLIENT_PROFILE_UPDATE__FAIL,
            NetworkConstant.STATUS_UPLOAD_CLIENT_PROFILE_PICTURE_SUCCESS,
            NetworkConstant.STATUS_UPLOAD_CLIENT_PROFILE_PICTURE_FAIL
    };

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private String[] languageListArray = null;
    private String[] countryListArray = null;

    private Dialog selectLanguageDialog;
    static Button mDateOfBirthButtonRef;


    ProfileFragmentListner mProfileFragmentListner;
    private Context mContext = null;
    private boolean mUploadPictureAction;
    private SweetAlertDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        ButterKnife.bind(this, view);
        mProfileFragmentListner = (ProfileFragmentListner) getActivity();
        mDateOfBirthButtonRef = mDateOfBirthButton;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(mClientProfileReceiver, new IntentFilter(action));
        }
        languageListArray = new String[2];
        languageListArray[0] = mEnglishLanguage;
        languageListArray[1] = mHindiLanguage;

        countryListArray = new String[1];
        countryListArray[0] = mIndiaCountry;

        GetClientProfileRequest mGetClientProfileRequest = new GetClientProfileRequest();
        mGetClientProfileRequest.clientId = LocalPreferenceUtility.getUserCode(mContext);
        mGetClientProfileRequest.pincode = LocalPreferenceUtility.getUserPassCode(mContext);
        try {
            mGetClientProfileRequest.hash = MobicashUtils.getSha1(mGetClientProfileRequest.clientId + MobicashUtils.md5(mGetClientProfileRequest.pincode));
        } catch (NoSuchAlgorithmException e) {
            Timber.e("NoSuchAlgorithmException Occurred . Message : " + e.getMessage());
        }

        MobicashIntentService.startActionGetClientProfile(mContext, mGetClientProfileRequest);
        mProfileFragmentListner.showProgress();
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                if (selectedImage != null) {
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String
                    mUserImageView.setImageBitmap(BitmapFactory
                            .decodeFile(filePath));

                    if (filePath != null) {
                        Timber.e("filePath is :" + filePath);
                        uploadProfilePictureToServer(filePath);
                    } else {
                        Timber.e("filePath is null");
                    }

                } else {
                    Timber.e("Selected profile picture is null");
                }
            } catch (Exception e) {
                Timber.e("Exception occured while selecting picture from gallary" + e.getMessage());
            }
        }
    }

    public void uploadProfilePictureToServer(String filePath) {
        if (filePath != null) {
            UploadClientPictureRequest mUploadClientPictureRequest = new UploadClientPictureRequest();
            mUploadClientPictureRequest.clientId = LocalPreferenceUtility.getUserCode(mContext);
            mUploadClientPictureRequest.pincode = LocalPreferenceUtility.getUserPassCode(mContext);
            mUploadClientPictureRequest.picture = new File(filePath);
            try {
                mUploadClientPictureRequest.hash = MobicashUtils.getSha1(LocalPreferenceUtility.getUserCode(mContext) + MobicashUtils.md5(LocalPreferenceUtility.getUserPassCode(mContext)));
            } catch (NoSuchAlgorithmException e) {
                Timber.e("NoSuchAlgorithmException catched." + e.getMessage());
            }
            pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Please wait...");
            pDialog.show();
            pDialog.setCancelable(false);
            MobicashIntentService.startActionUploadClientProfilePicture(mContext, mUploadClientPictureRequest);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mClientProfileReceiver);
        if ( pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }
    }

    @OnClick(R.id.btn_language)
    public void selectLanguage() {
        selectLanguageDialog = onCreateDialogForLanguageSelection();
        selectLanguageDialog.show();

    }

    @OnClick(R.id.button_country)
    public void selectCountry() {
        selectLanguageDialog = onCreateDialogForCountrySelection();
        selectLanguageDialog.show();

    }

    @OnClick(R.id.btn_dob)
    public void selectBirthDate() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datepicker");

    }

    @OnClick(R.id.user_image)
    public void selectPicture() {
        showFileChooser();

    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int day) {
            String years = "" + year;
            String months = "" + (monthOfYear + 1);
            String days = "" + day;
            if (monthOfYear >= 0 && monthOfYear < 9) {
                months = "0" + (monthOfYear + 1);
            }
            if (day > 0 && day < 10) {
                days = "0" + day;

            }

            mDateOfBirthButtonRef.setText(years + "-" + months + "-" + days);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = null;
            datePickerDialog = new DatePickerDialog(getContext(), this, year, month, day);

            return datePickerDialog;
        }

    }


    @OnClick(R.id.btn_update)
    public void updateProfile() {
        //Toast.makeText(getContext(),"BtnCLicked",Toast.LENGTH_SHORT).show();
        ClientProfileUpdateRequest mClientProfileUpdateRequest = new ClientProfileUpdateRequest();
        mClientProfileUpdateRequest.clientId = LocalPreferenceUtility.getUserCode(mContext);
        mClientProfileUpdateRequest.pincode = MobicashUtils.md5(LocalPreferenceUtility.getUserPassCode(mContext));
        mClientProfileUpdateRequest.firstname = mFirstNameEditTextView.getText().toString();
        mClientProfileUpdateRequest.lastname = mLastNameEditTextView.getText().toString();
        mClientProfileUpdateRequest.username = mUserNameEditTextView.getText().toString();
        mClientProfileUpdateRequest.address = mAddressEditTextView.getText().toString();
        mClientProfileUpdateRequest.zipcode = mPostalCodeEditTextView.getText().toString();
        mClientProfileUpdateRequest.city = mCityOrTownEditTextView.getText().toString();
        mClientProfileUpdateRequest.country = mCountryButton.getText().toString();
        mClientProfileUpdateRequest.language = mLanguageButton.getText().toString();
        mClientProfileUpdateRequest.email = mEmailEditTextView.getText().toString();
        mClientProfileUpdateRequest.dob = mDateOfBirthButton.getText().toString();
        mClientProfileUpdateRequest.adhaar = mAdhaarEditTextView.getText().toString();
        mClientProfileUpdateRequest.pan = mPanEditTextView.getText().toString();

        try {
            mClientProfileUpdateRequest.hash = MobicashUtils.getSha1(mClientProfileUpdateRequest.clientId + mClientProfileUpdateRequest.pincode);
        } catch (NoSuchAlgorithmException e) {
            Timber.e("NoSuchAlgorithmException Occurred . Message : " + e.getMessage());
        }

        if (validateProfileUpdateData(mClientProfileUpdateRequest)) {
            Timber.e("Validation is successfull ");
            pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Please wait...");
            pDialog.show();
            pDialog.setCancelable(false);
            MobicashIntentService.startActionClientProfileUpdate(mContext, mClientProfileUpdateRequest);
            //mProfileFragmentListner.showProgress();
        }

    }

    public boolean validateProfileUpdateData(ClientProfileUpdateRequest mClientProfileUpdateRequest) {

        String numberRegex = "\\d+";

        if (mClientProfileUpdateRequest.clientId.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.clientId is null");
            mProfileFragmentListner.showError(mEmptyClientIdOrPasscodeOrHashErrorMessage);
            return false;
        }

        if (mClientProfileUpdateRequest.pincode.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.pincode is null");
            mProfileFragmentListner.showError(mEmptyClientIdOrPasscodeOrHashErrorMessage);
            return false;
        }

        if (mClientProfileUpdateRequest.hash.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.hash is null");
            mProfileFragmentListner.showError(mEmptyClientIdOrPasscodeOrHashErrorMessage);
            return false;
        }

        if (mClientProfileUpdateRequest.firstname.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.firstname is empty");
            mProfileFragmentListner.showError(mFirstNameErrorMessage);
            return false;
        }

        if (mClientProfileUpdateRequest.lastname.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.lastname is empty");
            mProfileFragmentListner.showError(mLastNameErrorMessage);
            return false;
        }

        if (mClientProfileUpdateRequest.username.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.username is empty");
            mProfileFragmentListner.showError(mUserNameErrorMessage);
            return false;
        }


        if (mClientProfileUpdateRequest.language.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.language is empty");
            mProfileFragmentListner.showError(mLanguageErrorMessage);
            return false;
        } else {
            if (mClientProfileUpdateRequest.language.equalsIgnoreCase(mEnglishLanguage)) {
                mClientProfileUpdateRequest.language = mEnglishCode;
            } else if (mClientProfileUpdateRequest.language.equalsIgnoreCase(mHindiLanguage)) {
                mClientProfileUpdateRequest.language = mHindiCode;
            } else {
                mClientProfileUpdateRequest.language = mEnglishCode;
            }
        }

        if (mClientProfileUpdateRequest.email.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.email is empty");
            mProfileFragmentListner.showError(mEmailErrorMessage);
            return false;
        } else if (!validateEmail(mClientProfileUpdateRequest.email)) {
            Timber.e("mClientProfileUpdateRequest.email is invalid");
            mProfileFragmentListner.showError(mEmailErrorMessage);
            return false;
        }

        //////////// Optional

        if (mClientProfileUpdateRequest.address.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.address is empty");
        }

        if (mClientProfileUpdateRequest.city.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.city is empty");
        }

        if (mClientProfileUpdateRequest.country.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.country is empty");
        } else {
            if (mClientProfileUpdateRequest.country.equalsIgnoreCase(mIndiaCountry)) {
                mClientProfileUpdateRequest.country = mIndiaCode;
            } else {
                mClientProfileUpdateRequest.country = mIndiaCode;
            }
        }

        if (mClientProfileUpdateRequest.dob.length() == 0) {
            Timber.e("mClientProfileUpdateRequest.dob is empty");
        }

        if (mClientProfileUpdateRequest.zipcode.length() != 0) {
            if (!mClientProfileUpdateRequest.zipcode.matches(numberRegex) || mClientProfileUpdateRequest.zipcode.length() != 6) {
                Timber.e("mClientProfileUpdateRequest.zipcode is invalid");
                mProfileFragmentListner.showError(mZipCodeErrorMessage);
                return false;
            }
        } else {
            Timber.e("mClientProfileUpdateRequest.zipcode is empty");
        }

        if (mClientProfileUpdateRequest.adhaar.length() != 0)
        {
            if (mClientProfileUpdateRequest.adhaar.length() != 12) {
                Timber.e("mClientProfileUpdateRequest.adhaar is invalid");
                mProfileFragmentListner.showError(mAdhaarNoErrorMessage);
                return false;
            }
        } else {
            Timber.e("mClientProfileUpdateRequest.adhaar is empty");
        }

        if (mClientProfileUpdateRequest.pan != null) {
            if (mClientProfileUpdateRequest.pan.length() != 10) {
                Timber.e("mClientProfileUpdateRequest.pan is invalid");
                mProfileFragmentListner.showError(mPanNoErrorMessage);
                return false;
            }
        } else {
            Timber.e("mClientProfileUpdateRequest.pan is empty");
        }

        return true;
    }


    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public Dialog onCreateDialogForLanguageSelection() {

        AlertDialog.Builder alertDialogBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            alertDialogBuilder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(mContext);
        }

        alertDialogBuilder.setTitle(getString(R.string.language_dialog_title))
                .setSingleChoiceItems(languageListArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedLanguageIndex = 0;
                        selectedLanguageIndex = which;
                        if (selectedLanguageIndex == 0) {
                            mLanguageButton.setText(mEnglishLanguage);
                        } else if (selectedLanguageIndex == 1) {
                            mLanguageButton.setText(mHindiLanguage);
                        }

                        dialog.dismiss();
                    }
                });

        return alertDialogBuilder.create();
    }

    public Dialog onCreateDialogForCountrySelection() {

        AlertDialog.Builder alertDialogBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            alertDialogBuilder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(mContext);
        }

        alertDialogBuilder.setTitle(getString(R.string.country_dialog_title))
                .setSingleChoiceItems(countryListArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedCountryIndex = 0;

                        selectedCountryIndex = which;
                        if (selectedCountryIndex == 0) {
                            mCountryButton.setText(mIndiaCountry);
                        }

                        dialog.dismiss();
                    }
                });

        return alertDialogBuilder.create();
    }


    /**
     * Handling broadcast event for user login .
     */
    private BroadcastReceiver mClientProfileReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            mProfileFragmentListner.cancelProgress();
            try {
                switch (action) {
                    case NetworkConstant.STATUS_GET_CLIENT_PROFILE_SUCCESS:
                        GetClientProfileResponse mGetClientProfileResponse = (GetClientProfileResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                        Timber.d("STATUS_GET_CLIENT_PROFILE_SUCCESS = > GetClientProfileResponse  ==>" + new Gson().toJson(mGetClientProfileResponse));

                        if (mGetClientProfileResponse != null && mGetClientProfileResponse.clientProfile != null && mGetClientProfileResponse.clientProfile.mobicashClientProfile != null) {
                            GetProfile mGetProfile = mGetClientProfileResponse.clientProfile.mobicashClientProfile;

                            if (mGetProfile.clientFirstName != null) {
                                mFirstNameEditTextView.setText(mGetProfile.clientFirstName);
                            } else {
                                mFirstNameEditTextView.setText("");
                            }

                            if (mGetProfile.clientLastName != null) {
                                mLastNameEditTextView.setText(mGetProfile.clientLastName);
                                userLastName =mGetProfile.clientLastName;
                            } else {
                                mLastNameEditTextView.setText("");
                            }

                            if (mGetProfile.clientUserName != null) {
                                mUserNameEditTextView.setText(mGetProfile.clientUserName);
                            } else {
                                mUserNameEditTextView.setText("");
                            }

                            if (mGetProfile.clientAddress != null) {
                                mAddressEditTextView.setText(mGetProfile.clientAddress);
                            } else {
                                mAddressEditTextView.setText("");
                            }

                            if (mGetProfile.clientCity != null) {
                                mCityOrTownEditTextView.setText(mGetProfile.clientCity);
                            } else {
                                mCityOrTownEditTextView.setText("");
                            }

                            if (mGetProfile.clientZipcode != null) {
                                mPostalCodeEditTextView.setText(mGetProfile.clientZipcode);
                                LocalPreferenceUtility.putPinCode(mContext,mGetProfile.clientZipcode);
                            } else {
                                mPostalCodeEditTextView.setText("");
                            }

                            if (mGetProfile.clientCountry != null) {
                                if (mGetProfile.clientCountry.equalsIgnoreCase(mIndiaCode)) {
                                    mCountryButton.setText(mIndiaCountry);
                                }
                            } else {
                                mCountryButton.setText("");
                            }

                            if (mGetProfile.clientLanguage != null) {
                                if (mGetProfile.clientLanguage.equalsIgnoreCase(mEnglishCode)) {
                                    mLanguageButton.setText(mEnglishLanguage);
                                } else if (mGetProfile.clientLanguage.equalsIgnoreCase(mHindiCode)) {
                                    mLanguageButton.setText(mHindiLanguage);
                                }
                            } else {
                                mLanguageButton.setText("");
                            }

                            if (mGetProfile.clientEmail != null) {
                                mEmailEditTextView.setText(mGetProfile.clientEmail);
                            } else {
                                mEmailEditTextView.setText("");
                            }

                            if (mGetProfile.clientAdhaar != null) {
                                mAdhaarEditTextView.setText(mGetProfile.clientAdhaar);
                            } else {
                                mAdhaarEditTextView.setText("");
                            }

                            if (mGetProfile.clientPan != null) {
                                mPanEditTextView.setText(mGetProfile.clientPan);
                            } else {
                                mPanEditTextView.setText("");
                            }

                            if (mGetProfile.clientDob != null) {
                                mDateOfBirthButton.setText(mGetProfile.clientDob);
                            } else {
                                mDateOfBirthButton.setText("");
                            }

                            if (mGetProfile.profilePicture != null) {
                                mUserImageView.setImageUrl(mGetProfile.profilePicture, MobicashApplication.getInstance().getImageLoader());
                            } else {
                                mUserImageView.setDefaultImageResId(R.mipmap.ic_user_default);
                            }

                        } else {
                            Timber.d("STATUS_GET_CLIENT_PROFILE_SUCCESS = > GetClientProfileResponse is null");
                        }
                        break;

                    case NetworkConstant.STATUS_GET_CLIENT_PROFILE_FAIL:

                        FailureResponse getClientProfileFailureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                        if (getClientProfileFailureResponse != null) {
                            Timber.d("STATUS_GET_CLIENT_PROFILE_FAIL = > failureResponse  ==>" + new Gson().toJson(getClientProfileFailureResponse));
                        }
                        if (getClientProfileFailureResponse != null && getClientProfileFailureResponse.msg != null) {
                            mProfileFragmentListner.showError(getClientProfileFailureResponse.msg);
                        } else {
                            String errorMessage = getString(R.string.error_message);
                            mProfileFragmentListner.showError(errorMessage);
                        }
                        break;

                    case NetworkConstant.STATUS_CLIENT_PROFILE_UPDATE_SUCCESS:

                        ClientProfileUpdateResponse mClientProfileUpdateResponse = (ClientProfileUpdateResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                        Timber.d("STATUS_CLIENT_PROFILE_UPDATE_SUCCESS = > ClientProfileUpdateResponse  ==>" + new Gson().toJson(mClientProfileUpdateResponse));
                        if (mClientProfileUpdateResponse != null && mClientProfileUpdateResponse.clientProfile != null && mClientProfileUpdateResponse.clientProfile.mobicashClientProfile != null) {
                            Profile mProfile = mClientProfileUpdateResponse.clientProfile.mobicashClientProfile;


                            if (mProfile.clientFirstName != null) {
                                mFirstNameEditTextView.setText(mProfile.clientFirstName);
                            } else {
                                mFirstNameEditTextView.setText("");
                            }

                            if (mProfile.clientLastName != null) {
                                mLastNameEditTextView.setText(mProfile.clientLastName);
                            } else {
                                mLastNameEditTextView.setText("");
                            }

                            if (mProfile.clientUserName != null) {
                                mUserNameEditTextView.setText(mProfile.clientUserName);
                            } else {
                                mUserNameEditTextView.setText("");
                            }

                            if (mProfile.clientAddress != null) {
                                mAddressEditTextView.setText(mProfile.clientAddress);
                            } else {
                                mAddressEditTextView.setText("");
                            }

                            if (mProfile.clientCity != null) {
                                mCityOrTownEditTextView.setText(mProfile.clientCity);
                            } else {
                                mCityOrTownEditTextView.setText("");
                            }

                            if (mProfile.clientZipcode != null) {
                                mPostalCodeEditTextView.setText(mProfile.clientZipcode);
                                FirebaseMessaging.getInstance().subscribeToTopic(mPostalCodeEditTextView.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!task.isSuccessful()) {
                                                    Log.w("ProfileFragment", "getInstanceId failed", task.getException());
                                                }else {
                                                    Log.i("ProfileFragment","Successfully subscribeToTopic "+mPostalCodeEditTextView.getText().toString());
                                                }
                                            }
                                        });
                            } else {
                                mPostalCodeEditTextView.setText("");
                            }

                            if (mProfile.clientCountry != null) {
                                if (mProfile.clientCountry.equalsIgnoreCase(mIndiaCode)) {
                                    mCountryButton.setText(mIndiaCountry);
                                }
                            } else {
                                mCountryButton.setText("");
                            }

                            if (mProfile.clientLanguage != null) {
                                if (mProfile.clientLanguage.equalsIgnoreCase(mEnglishCode)) {
                                    mLanguageButton.setText(mEnglishLanguage);
                                } else if (mProfile.clientLanguage.equalsIgnoreCase(mHindiCode)) {
                                    mLanguageButton.setText(mHindiLanguage);
                                }
                            } else {
                                mLanguageButton.setText("");
                            }

                            if (mProfile.clientEmail != null) {
                                mEmailEditTextView.setText(mProfile.clientEmail);
                            } else {
                                mEmailEditTextView.setText("");
                            }

                            if (mProfile.clientAdhaar != null) {
                                mAdhaarEditTextView.setText(mProfile.clientAdhaar);
                            } else {
                                mAdhaarEditTextView.setText("");
                            }

                            if (mProfile.clientPan != null) {
                                mPanEditTextView.setText(mProfile.clientPan);
                            } else {
                                mPanEditTextView.setText("");
                            }

                            if (mProfile.clientDob != null) {
                                mDateOfBirthButton.setText(mProfile.clientDob);
                            } else {
                                mDateOfBirthButton.setText("");
                            }


                            if (mProfile.profilePicture != null) {
                                mUserImageView.setImageUrl(mProfile.profilePicture, MobicashApplication.getInstance().getImageLoader());
                            } else {
                                mUserImageView.setDefaultImageResId(R.mipmap.ic_user_default);
                            }

                            pDialog.setTitleText("Success!")
                                    .setConfirmText("OK")
                                    .setContentText("Profile updated successfully")
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    onPause();
                                    //onResume();
                                }
                            });

                        } else {
                            Timber.d("STATUS_CLIENT_PROFILE_UPDATE_SUCCESS = > GetClientProfileResponse is null");
                        }

                        break;
                    case NetworkConstant.STATUS_CLIENT_PROFILE_UPDATE__FAIL:

                        FailureResponse profileUpdateFailureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                        if (profileUpdateFailureResponse != null) {
                            Timber.d("STATUS_CLIENT_PROFILE_UPDATE__FAIL = > failureResponse  ==>" + new Gson().toJson(profileUpdateFailureResponse));
                        }
                        if (profileUpdateFailureResponse != null && profileUpdateFailureResponse.msg != null) {
                            mProfileFragmentListner.showError(profileUpdateFailureResponse.msg);
                        } else {
                            pDialog.setTitleText("Failed! Please Try Again.")
                                    .setConfirmText("OK")
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            //String errorMessage = getString(R.string.error_message);
                            //mProfileFragmentListner.showError(errorMessage);
                        }
                        break;
                    case NetworkConstant.STATUS_UPLOAD_CLIENT_PROFILE_PICTURE_SUCCESS:
                        mUploadPictureAction = false;
                        pDialog.setTitleText("Success!")
                                .setContentText("Profile picture uploaded successfully")
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                onPause();
                                //onResume();
                            }
                        });
                        //mProfileFragmentListner.showError("Profile Picture Uploaded successfully");
                        break;
                    case NetworkConstant.STATUS_UPLOAD_CLIENT_PROFILE_PICTURE_FAIL:
                        mUploadPictureAction = false;
                        pDialog.setTitleText("Failed! Please Try Again.")
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        //mProfileFragmentListner.showError("profile Picture Uploaded failed");
                        break;
                }

            } catch (Exception e) {
                Timber.e(" Exception caught in loginStatusReceiver " + e.getMessage());
            }
        }
    };


}
