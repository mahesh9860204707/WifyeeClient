package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.requests.SendMoney;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.SendMoneyResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.security.NoSuchAlgorithmException;

import timber.log.Timber;


public class SendMoneyActivity extends BaseActivity {

    private EditText et_Mobile_Number;
    private EditText et_Money;
    private Button submit_Continue;
    private String senderMobile="";
    private String recieverMobile="";
    private String amount="";
    private String hashValue="";
    private SendMoney sendMoney;
    private Toolbar mToolbar;
    private ImageButton back;
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_SEND_MONEY_FAIL,
            NetworkConstant.STATUS_SEND_MONEY_SUCCESS,
            NetworkConstant.STATUS_SEND_CLIENT_WALLET_MONEY_FAIL,
            NetworkConstant.STATUS_SEND_MONEY_CLIENT_WALLET_SUCCESS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
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
        submit_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                recieverMobile = et_Mobile_Number.getText().toString();
                amount = et_Money.getText().toString();
                if (amount.equals("") || amount == null) {
                    Toast.makeText(SendMoneyActivity.this, "Fill Send Money", Toast.LENGTH_SHORT).show();
                    return;
                } else if (recieverMobile.equals("") || recieverMobile == null) {
                    Toast.makeText(SendMoneyActivity.this, "Fill Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(senderMobile);
                stringBuilder.append(recieverMobile);
                stringBuilder.append(amount);
                sendMoney=new SendMoney();
                sendMoney.setAmount(amount);
                sendMoney.setClientreceiver(recieverMobile);
                sendMoney.setClientsender(senderMobile);
                try {
                    hashValue = MobicashUtils.getSha1(stringBuilder.toString());
                    sendMoney.setHash(hashValue);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                showProgressDialog();
                MobicashIntentService.startActionSendMoney(SendMoneyActivity.this,sendMoney);
            }
        });
    }

    private void showUI() {
        et_Money=(EditText)findViewById(R.id.et_send_money);
        et_Mobile_Number=(EditText)findViewById(R.id.et_mobile_number);
        submit_Continue=(Button) findViewById(R.id.submit_button);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(sendMoneyStatusReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(sendMoneyStatusReceiver, new IntentFilter(action));
        }
    }
    /**
     * Handling broadcast event for user send Money .
     */
    private BroadcastReceiver sendMoneyStatusReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            try {
                if (action.equals(NetworkConstant.STATUS_SEND_MONEY_SUCCESS)) {
                    SendMoneyResponse msendMoneyResponse = (SendMoneyResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if(msendMoneyResponse.receiverType.equals("ExistingClient")) {
                        callClientToClientWalletApi(SendMoneyActivity.this,msendMoneyResponse);
                    }
                    else {
                        cancelProgressDialog();
                        showSuccessDialog(SendMoneyActivity.this);
                    }
                } else if (action.equals(NetworkConstant.STATUS_SEND_MONEY_FAIL)) {
                    cancelProgressDialog();
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_SEND_MONEY_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        showErrorDialog(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showErrorDialog(errorMessage);
                    }

                }
                if(action.equals(NetworkConstant.STATUS_SEND_MONEY_CLIENT_WALLET_SUCCESS)) {
                    cancelProgressDialog();
                    showSuccessDialog(SendMoneyActivity.this);
                }
                else if(action.equals(NetworkConstant.STATUS_SEND_CLIENT_WALLET_MONEY_FAIL))
                {
                    cancelProgressDialog();
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_SEND_MONEY_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {

                        showErrorDialog(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showErrorDialog(errorMessage);
                    }
                }
            } catch (Exception e) {
                Timber.e(" Exception caught in sendMoneyStatusReceiver " + e.getMessage());
            }
        }
    };

    private void callClientToClientWalletApi(final Context context,SendMoneyResponse msendMoneyResponse) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(msendMoneyResponse.senderMobile);
        stringBuilder.append(msendMoneyResponse.receiverMobile);
        stringBuilder.append(msendMoneyResponse.amount);
        sendMoney = new SendMoney();
        sendMoney.setClientsender(msendMoneyResponse.senderMobile);
        sendMoney.setClientreceiver(msendMoneyResponse.receiverMobile);
        sendMoney.setAmount(msendMoneyResponse.amount);
        try {
            hashValue = MobicashUtils.getSha1(stringBuilder.toString());
            sendMoney.setHash(hashValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        MobicashIntentService.startActionSendMoneyToClient(this,sendMoney);
        cancelProgressDialog();
    }


    //SuccesFully Transferred
    private void showSuccessDialog(Activity activity)
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
                    finish();
                    layout.dismiss();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
