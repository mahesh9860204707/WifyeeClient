package com.wifyee.greenfields.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.requests.ClientSendPassCodeRequest;
import com.wifyee.greenfields.models.response.ClientSendPassCodeResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SendPassCodeActivity extends BaseActivity {

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
        setContentView(R.layout.activity_reset_pass_code);
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(sendPasscodeReceiver);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(sendPasscodeReceiver, new IntentFilter(action));
        }
    }

    @OnClick(R.id.send_button)
    void sendPasscode() {
        if (validateMobileNo()) {
            ClientSendPassCodeRequest mClientSendPassCodeRequest = new ClientSendPassCodeRequest();
            mClientSendPassCodeRequest.clientmobile = mMobileNumber.getText().toString();
            MobicashIntentService.startActionClientSendPasscode(mContext, mClientSendPassCodeRequest);
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

    /**
     * Handling broadcast event for user login .
     */
    private BroadcastReceiver sendPasscodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            cancelProgressDialog();
            if (action.equals(NetworkConstant.STATUS_CLIENT_SEND_PASSCODE_SUCCESS)) {

                ClientSendPassCodeResponse mClientSendPassCodeResponse = (ClientSendPassCodeResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (mClientSendPassCodeResponse != null && mClientSendPassCodeResponse.sendStatus != null) {
                    Timber.d("STATUS_CLIENT_SEND_PASSCODE_SUCCESS = > ClientSendPassCodeResponse  ==>" + new Gson().toJson(mClientSendPassCodeResponse));

                    if (mClientSendPassCodeResponse.sendStatus.equalsIgnoreCase(ResponseAttributeConstants.SUCCESS)) {
                        showDialog(mSuccessfulTitle, mPasscodeSuccessfullySent, true);
                    } else {
                        showDialog(mDialogErrorTitle, mSendPasswordFailed, true);
                    }
                } else {
                    Timber.d("STATUS_CLIENT_SEND_PASSCODE_SUCCESS = > ClientSendPassCodeResponse  is null");
                }

            } else if (action.equals(NetworkConstant.STATUS_CLIENT_SEND_PASSCODE_FAIL)) {

                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);

                if (failureResponse != null && failureResponse.msg != null) {
                    Timber.d("STATUS_CLIENT_SEND_PASSCODE_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
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
