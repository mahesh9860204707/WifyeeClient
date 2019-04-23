package com.wifyee.greenfields.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.models.requests.AirtimeRequest;
import com.wifyee.greenfields.models.requests.OperatorListRequest;
import com.wifyee.greenfields.models.response.AirtimeResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.OperatorListResponse;
import com.wifyee.greenfields.models.response.operators.OperatorDetails;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PostPaidRechargeActivity extends BaseActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener  {

    public static final String TAG = PostPaidRechargeActivity.class.getSimpleName();
    private final int MINIMUM_PHONE_NUMBER_LENGHT = 4;
    private final int MAXIMUM_PHONE_NUMBER_LENGHT = 19;
    private final int MINIMUM_BALANCE = 10;
    private int paymentSelectedIndex = 0;
    private String staticOperatorCode="";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_back)
    ImageButton back;

    @BindView(R.id.button_operator)
    Button buttonOperator;


    @BindView(R.id.top_up)
    Button buttonSubmit;

    @BindView(R.id.edit_text_mobile_number)
    EditText phoneNumberEditText;

    @BindView(R.id.edit_text_amount)
    EditText rechargeAmount;

    @BindView(R.id.radio_group_payment_type)
    RadioGroup typeGroup;

    private int selectedIndex = 0;
    private ProgressDialog progressDialog = null;
    private String[] operatorListArray = null;
    private HashMap operatorCodeListMap = null;
    private Dialog selectOperatorDialog;
    private Context mContext = null;

    /**
     * List of actions supported.
     */

    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_OPERATOR_LIST_SUCCESS,
            NetworkConstant.STATUS_OPERATOR_LIST_FAIL,
            NetworkConstant.STATUS_USER_CLIENT_AIRTIME_SUCCESS,
            NetworkConstant.STATUS_USER_CLIENT_AIRTIME_FAIL
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_paid_recharge);
        ButterKnife.bind(this);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        buttonOperator.setOnClickListener(this);
        typeGroup.setOnCheckedChangeListener(this);
        buttonSubmit.setOnClickListener(this);
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

    /**
     * operator list action
     */
    private void onOperatorButtonPressed() {
        selectOperatorDialog = onCreateDialogForOperatorSelection();
        selectOperatorDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(operatorListReceiver, new IntentFilter(action));
        }
        showProgressDialog();
        MobicashIntentService.startActionOperatorList(this, getOperatorListRequest());
    }

    /**
     * get operator list request
     */

    private OperatorListRequest getOperatorListRequest() {
        OperatorListRequest OperatorListRequest = new OperatorListRequest();
        OperatorListRequest.moduleType = NetworkConstant.TYPE_AIRTIME;
        OperatorListRequest.rechargeType = NetworkConstant.TYPE_POSTPAID;
        return OperatorListRequest;
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelProgressDialog();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(operatorListReceiver);
    }
    /**
     * operator list and recharge status receiver
     */
    private BroadcastReceiver operatorListReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String actionOperatorList = intent.getAction();
              if (actionOperatorList.equals(NetworkConstant.STATUS_USER_CLIENT_AIRTIME_SUCCESS)) {
            // cancelProgressDialog();
            AirtimeResponse airtimeResponse = (AirtimeResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
            if (airtimeResponse != null && airtimeResponse.msg != null && !airtimeResponse.msg.isEmpty()) {
                onSuccess(getString(R.string.recharge_status_title), airtimeResponse);
            }
        }
        else if (actionOperatorList.equals(NetworkConstant.STATUS_USER_CLIENT_AIRTIME_FAIL)) {
            cancelProgressDialog();
            FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
            if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                showErrorDialog(failureResponse.msg);
            }
        }
            if (actionOperatorList.equals(NetworkConstant.STATUS_OPERATOR_LIST_SUCCESS)) {
                OperatorListResponse operatorListResponse = (OperatorListResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (operatorListResponse != null && operatorListResponse.operatorList.airtimeList.postpaidList != null) {
                    List<OperatorDetails> operatorList = operatorListResponse.operatorList.airtimeList.postpaidList.operatorDetailsList;
                    cancelProgressDialog();
                    if (operatorList != null) {
                        operatorListArray = new String[operatorList.size()];
                        operatorCodeListMap = new HashMap(operatorList.size());
                        int j = 0;
                        for (OperatorDetails item : operatorList) {
                            operatorListArray[j] = item.operatorName;
                            operatorCodeListMap.put(item.operatorName, item.operatorCode);
                            j++;
                        }
                        Arrays.sort(operatorListArray);
                        cancelProgressDialog();
                    } else {
                        cancelProgressDialog();
                    }
                } else {
                    cancelProgressDialog();
                }
            } else if (actionOperatorList.equals(NetworkConstant.STATUS_OPERATOR_LIST_FAIL)) {
                cancelProgressDialog();
            }

        }
    };

    /**
     * show success message alert
     *
     * @param airtimeResponse
     */
    private void onSuccess(String title, AirtimeResponse airtimeResponse) {

        if (airtimeResponse.responseCode.equalsIgnoreCase("000")) {
            // custom dialog
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custome_dialog_layout);
            dialog.setTitle(title);

            // recharge status
            if (airtimeResponse.rechargeStatus != null) {
                ((TextView) dialog.findViewById(R.id.tv_recharge_status_value)).setText(airtimeResponse.rechargeStatus);
            }
            // reference id
            if (airtimeResponse.referenceID != null) {
                ((TextView) dialog.findViewById(R.id.tv_reference_id_value)).setText(airtimeResponse.referenceID);
            }
            // to do recharge id
            if (airtimeResponse.rechargeID != null) {
                ((TextView) dialog.findViewById(R.id.tv_recharge_id_value)).setText(airtimeResponse.rechargeID);
            }
            //recharge_transaction_date
            if (airtimeResponse.txnDate != null) {
                ((TextView) dialog.findViewById(R.id.tv_recharge_transaction_date_value)).setText(airtimeResponse.txnDate);
            }
            //recharge_number
            if (airtimeResponse.number != null) {
                ((TextView) dialog.findViewById(R.id.tv_recharge_number_value)).setText(airtimeResponse.number);
            }
            //recharge_amount
            if (airtimeResponse.amount != null) {
                ((TextView) dialog.findViewById(R.id.tv_recharge_amount_value)).setText(airtimeResponse.amount);
            }
            Button okButton = (Button) dialog.findViewById(R.id.ok);
            // if button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });

            dialog.show();
        } else {
            if (airtimeResponse.msg != null) {
                showErrorDialog(airtimeResponse.msg);
            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_operator:
                if (MobicashUtils.isNetworkAvailable(this) && operatorListArray != null) {
                    onOperatorButtonPressed();
                } else {
                    showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.network_not_available));
                }
                break;
            case R.id.top_up:
                if (onValidate()) {
                    if ((MobicashUtils.isNetworkAvailable(this))) {
                        onTopUpPressed();
                    } else {
                        showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.network_not_available));
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * start action
     */
    private void onTopUpPressed() {
        if(paymentSelectedIndex == 0) {
            if (operatorCodeListMap != null && operatorCodeListMap.containsKey(buttonOperator.getText().toString())) {
                String operatorCode = (String) operatorCodeListMap.get(buttonOperator.getText().toString());
                Timber.i("Operator code is:", operatorCode);
                showProgressDialog();
                MobicashIntentService.startActionAirtimeRecharge(getApplicationContext(), getAirtimeRequest(operatorCode));
            }
        }else{
            Intent paymentIntent = IntentFactory.createPayuBaseActivity(this);
            paymentIntent.putExtra(PaymentConstants.STRING_EXTRA,rechargeAmount.getText().toString());
            String operatorCode = (String) operatorCodeListMap.get(buttonOperator.getText().toString());
            paymentIntent.putExtra(PaymentConstants.AIRTIME_EXTRA,getAirtimeRequest(operatorCode));
            startActivity(paymentIntent);
        }
    }

    /**
     * get airtime request model
     * clientmobile+pincode+number+amount+operator+type = hash
     */

    private AirtimeRequest getAirtimeRequest(String code) {
        AirtimeRequest airtimeRequest = new AirtimeRequest();
        airtimeRequest.clientmobile = LocalPreferenceUtility.getUserMobileNumber(getApplicationContext());
        airtimeRequest.pincode = LocalPreferenceUtility.getUserPassCode(getApplicationContext());
        airtimeRequest.number = phoneNumberEditText.getText().toString();
        airtimeRequest.amount = rechargeAmount.getText().toString();
        airtimeRequest.cyclenumber = "0";
        airtimeRequest.operator = code;
        airtimeRequest.type = NetworkConstant.TYPE_AIRTIME;
        StringBuilder builder = new StringBuilder();
        builder.append(airtimeRequest.clientmobile);
        builder.append(MobicashUtils.md5(airtimeRequest.pincode));
        builder.append(airtimeRequest.number);
        builder.append(airtimeRequest.amount);
        builder.append(airtimeRequest.operator);
        builder.append(airtimeRequest.type);
        try {
            airtimeRequest.hash = MobicashUtils.getSha1(builder.toString());
        } catch (Exception e) {
        }

        return airtimeRequest;
    }

    /**
     * validate inputs
     *
     * @return
     */
    private boolean onValidate() {
        if (rechargeAmount == null || phoneNumberEditText == null || buttonOperator == null) {
            showAlert(getString(R.string.Invalid_alert_title), getString(R.string.all_fields_required));
            return false;
        }
        String text = buttonOperator.getText().toString();
        if (text.isEmpty() || text.equals(getString(R.string.select))) {
            showAlert(getString(R.string.alert_dialog_choose_operator_title), getString(R.string.alert_dialog_choose_operator_message));
            return false;
        }
        String phoneNumber = phoneNumberEditText.getText().toString();
        int existPlus = (phoneNumber != null && phoneNumber.startsWith("+")) ? 1 : 0;
        if (phoneNumber.isEmpty()) {
            showAlert(getString(R.string.phone_validation_alert_title), getString(R.string.error_empty_phone_number));
            return false;
        } else if (phoneNumber.length() < MINIMUM_PHONE_NUMBER_LENGHT
                + existPlus
                || phoneNumber.length() > MAXIMUM_PHONE_NUMBER_LENGHT
                + existPlus) {
            showAlert(getString(R.string.phone_validation_alert_title), getString(R.string.phone_validation_alert_message));
            return false;
        } else if (phoneNumber.startsWith("+") || phoneNumber.startsWith("0")) {
            showAlert(getString(R.string.phone_validation_alert_title), getString(R.string.phone_validation_alert_message));
            return false;
        }
        try {
            if (rechargeAmount.getText().toString().isEmpty() || rechargeAmount.getText().toString().equals("0")
                    || rechargeAmount.getText().toString().equals("00") || rechargeAmount.getText().toString().equals("000")) {
                showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.alert_dialog_min_balance_alert_message));
                return false;
            } else if (Integer.parseInt(rechargeAmount.getText().toString()) < MINIMUM_BALANCE) {
                showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.alert_dialog_min_balance_alert_message));
                return false;
            }

        } catch (NumberFormatException nfe) {
            Log.d(TAG, nfe.getMessage());
            showAlert(getString(R.string.improper_input_title), getString(R.string.improper_input_msg));
            return false;
        }

        return true;
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

    public Dialog onCreateDialogForOperatorSelection() {

        AlertDialog.Builder alertDialogBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(this);
        }

        alertDialogBuilder.setTitle(getString(R.string.operator_title))
                .setSingleChoiceItems(operatorListArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex = which;
                        buttonOperator.setText(operatorListArray[which].toString());
                        dialog.dismiss();
                    }
                });

        return alertDialogBuilder.create();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.radio_button_wallet:
                paymentSelectedIndex = 0;
                break;
            case R.id.radio_button_payu:
                paymentSelectedIndex = 1;
                break;
            default:
                break;
        }
    }
}
