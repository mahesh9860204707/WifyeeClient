package com.wifyee.greenfields.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.requests.ClientResetPassCodeRequest;
import com.wifyee.greenfields.models.response.ClientResetPassCodeResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.security.NoSuchAlgorithmException;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ChangePasswordSettingActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.edit_text_existing_pass_code)
    EditText mExistingPassCode;

    @BindView(R.id.edit_text_new_pass_code)
    EditText mNewPassCode;

    @BindView(R.id.edit_text_re_enter_passcode)
    EditText mReenterNewPassCode;

    @BindString(R.string.passcode_error_message)
    String mPasscodeErrorMessage;

    @BindString(R.string.new_passcode_error_message)
    String mNewPasscodeErrorMessage;

    @BindString(R.string.reenter_passcode_error_message)
    String mReenterNewPasscodeErrorMessage;

    @BindString(R.string.passcode_not_matching_error_message)
    String mPasscodeNotMatchingErrorMessage;

    @BindString(R.string.empty_clientid_or_passcode_or_hash_error_message)
    String mEmptyClientIdOrPasscodeOrHashErrorMessage;

    @BindString(R.string.successful)
    String mSuccessfulTitle;

    @BindString(R.string.dialog_error)
    String mDialogErrorTitle;

    @BindString(R.string.passcode_changed_successfully_sent)
    String mPasscodeSuccessfullyChanged;

    @BindString(R.string.reset_passcode_failed)
    String mResetPasswordFailed;

    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_CLIENT_RESET_PASSCODE_SUCCESS,
            NetworkConstant.STATUS_CLIENT_RESET_PASSCODE_FAIL
    };


    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_setting);
        ButterKnife.bind(this);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        toolBarTitle.setTypeface(Fonts.getSemiBold(this));
        TextInputLayout tilExistPwd   = findViewById(R.id.til_exist_pwd);
        TextInputLayout til_new_password   = findViewById(R.id.til_new_password);
        TextInputLayout til_re_enter   = findViewById(R.id.til_re_enter);
        Button btn_confirm_reset_passcode   = findViewById(R.id.btn_confirm_reset_passcode);
        tilExistPwd.setTypeface(Fonts.getRegular(this));
        til_new_password.setTypeface(Fonts.getRegular(this));
        til_re_enter.setTypeface(Fonts.getRegular(this));
        mExistingPassCode.setTypeface(Fonts.getSemiBold(this));
        mNewPassCode.setTypeface(Fonts.getSemiBold(this));
        mReenterNewPassCode.setTypeface(Fonts.getSemiBold(this));
        btn_confirm_reset_passcode.setTypeface(Fonts.getSemiBold(this));

        if (mToolbar != null) {
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
        }

        mExistingPassCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mExistingPassCode.getText().toString().length()==4){
                    mExistingPassCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_correct, 0);
                }else {
                    mExistingPassCode.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mNewPassCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mNewPassCode.getText().toString().length()==4){
                    mNewPassCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_correct, 0);
                }else {
                    mNewPassCode.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mReenterNewPassCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mReenterNewPassCode.getText().toString().length()==4){
                    mReenterNewPassCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_correct, 0);
                }else {
                    mReenterNewPassCode.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(resetPasscodeReceiver);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(resetPasscodeReceiver, new IntentFilter(action));
        }

    }

    @OnClick(R.id.btn_confirm_reset_passcode)
    void resetPasscode() {
        if (validate()) {
            ClientResetPassCodeRequest mClientResetPassCodeRequest = new ClientResetPassCodeRequest();
            mClientResetPassCodeRequest.clientId = LocalPreferenceUtility.getUserCode(getApplicationContext());
            mClientResetPassCodeRequest.pinCode = mExistingPassCode.getText().toString();
            mClientResetPassCodeRequest.newPinCode = mNewPassCode.getText().toString();
            try {
                mClientResetPassCodeRequest.hash = MobicashUtils.getSha1(LocalPreferenceUtility.getUserCode(getApplicationContext()) + MobicashUtils.md5(mExistingPassCode.getText().toString()));
            } catch (NoSuchAlgorithmException e) {
                Timber.e("NoSuchAlgorithmException catched." + e.getMessage());
            }
            MobicashIntentService.startActionClientResetPasscode(mContext, mClientResetPassCodeRequest);
        }
    }

    private boolean validate() {
        String regex = "\\d+";
        String existingPasscode = mExistingPassCode.getText().toString();
        String newPassCode = mNewPassCode.getText().toString();
        String reenterNewPassCode = mReenterNewPassCode.getText().toString();

        if (existingPasscode.length() == 0 || newPassCode.length() == 0 || reenterNewPassCode.length() == 0) {
            showDialog(getString(R.string.Invalid_alert_title), getString(R.string.all_fields_required), false);
            return false;
        }
        if (existingPasscode.length() != 4 || !existingPasscode.matches(regex)) {
            Timber.d(" passcode are invalid");
            showDialog(mDialogErrorTitle, mPasscodeErrorMessage, false);
            return false;
        }

        if (newPassCode.length() != 4 || !newPassCode.matches(regex)) {
            Timber.d("new passcode are invalid");
            showDialog(mDialogErrorTitle, mNewPasscodeErrorMessage, false);
            return false;
        }

        if (reenterNewPassCode.length() != 4 || !reenterNewPassCode.matches(regex)) {
            Timber.d(" Reentered passcode are invalid");
            showDialog(mDialogErrorTitle, mReenterNewPasscodeErrorMessage, false);
            return false;
        }

        if (!reenterNewPassCode.equals(newPassCode)) {
            Timber.d(" New password and reentered password do not matching");
            showDialog(mDialogErrorTitle, mPasscodeNotMatchingErrorMessage, false);
            return false;
        }

        if (LocalPreferenceUtility.getUserCode(getApplicationContext()) == null || LocalPreferenceUtility.getUserCode(getApplicationContext()).length() == 0) {
            Timber.d(" Client id is null");
            showDialog(mDialogErrorTitle, mEmptyClientIdOrPasscodeOrHashErrorMessage, false);
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

    /**
     * Handling broadcast event for user login .
     */
    private BroadcastReceiver resetPasscodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            cancelProgressDialog();
            if (action.equals(NetworkConstant.STATUS_CLIENT_RESET_PASSCODE_SUCCESS)) {

                ClientResetPassCodeResponse mClientResetPassCodeResponse = (ClientResetPassCodeResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (mClientResetPassCodeResponse != null && mClientResetPassCodeResponse.resetStatus != null) {
                    Timber.d("STATUS_CLIENT_RESET_PASSCODE_SUCCESS = > ClientResetPassCodeResponse  ==>" + new Gson().toJson(mClientResetPassCodeResponse));

                    if (mClientResetPassCodeResponse.resetStatus.equalsIgnoreCase(ResponseAttributeConstants.SUCCESS)) {
                        showDialog(mSuccessfulTitle, mPasscodeSuccessfullyChanged, true);
                    } else {
                        showDialog(mDialogErrorTitle, mResetPasswordFailed, true);
                    }
                } else {
                    Timber.d("STATUS_CLIENT_RESET_PASSCODE_SUCCESS = > ClientResetPassCodeResponse  is null");
                }

            } else if (action.equals(NetworkConstant.STATUS_CLIENT_RESET_PASSCODE_FAIL)) {

                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);

                if (failureResponse != null && failureResponse.msg != null) {
                    Timber.d("STATUS_CLIENT_RESET_PASSCODE_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    showErrorDialog(failureResponse.msg);
                } else {
                    String errorMessage = getString(R.string.error_message);
                    showErrorDialog(errorMessage);
                }
            }
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}
