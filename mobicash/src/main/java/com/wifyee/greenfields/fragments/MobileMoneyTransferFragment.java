package com.wifyee.greenfields.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.requests.ClientToClientMoneyTransferRequest;
import com.wifyee.greenfields.models.response.ClientToClientMoneyTransferResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class MobileMoneyTransferFragment extends Fragment implements View.OnClickListener {

    private final int MINIMUM_PHONE_NUMBER_LENGHT = 4;
    private final int MAXIMUM_PHONE_NUMBER_LENGHT = 19;
    private final int MINIMUM_BALANCE = 10;

    @BindView(R.id.edit_text_mobile_number)
    EditText mobileNumber;

    @BindView(R.id.edit_text_amount)
    EditText amount;

    @BindView(R.id.send_money)
    Button sendMoney;

    private ProgressDialog progressDialog = null;

    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_USER_CLIENT_TO_CLIENT_MONEY_TRANSFER_SUCCESS,
            NetworkConstant.STATUS_USER_CLIENT_TO_CLIENT_MONEY_TRANSFER_FAIL
    };

    public MobileMoneyTransferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mobile_money_transfer, container, false);
        ButterKnife.bind(this, view);
        sendMoney.setOnClickListener(this);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        if (getActivity() != null && isAdded()) {
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getContext());
            for (String action : broadCastReceiverActionList) {
                broadcastManager.registerReceiver(clientLogsReceiver, new IntentFilter(action));
            }
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {

            case R.id.send_money:
                if (onValidate()) {
                    showProgressDialog();
                    requestData();
                }

            default:
                break;
        }
    }


    /**
     * validate inputs
     *
     * @return
     */
    private boolean onValidate() {

        String phoneNumber = mobileNumber.getText().toString();
        int existPlus = (phoneNumber != null && phoneNumber.startsWith("+")) ? 1 : 0;
        if (phoneNumber.isEmpty()) {
            showError(getString(R.string.error_empty_phone_number));
            return false;
        } else if (phoneNumber.length() < MINIMUM_PHONE_NUMBER_LENGHT
                + existPlus
                || phoneNumber.length() > MAXIMUM_PHONE_NUMBER_LENGHT
                + existPlus) {
            showError(getString(R.string.phone_validation_alert_message));
            return false;
        } else if (phoneNumber.startsWith("+") || phoneNumber.startsWith("0")) {
            showError(getString(R.string.phone_validation_alert_message));
            return false;
        }


        try {
            if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")
                    || amount.getText().toString().equals("00") || amount.getText().toString().equals("000")) {
                showError(getString(R.string.alert_dialog_min_balance_alert_message));
                return false;
            } else if (Integer.parseInt(amount.getText().toString()) < MINIMUM_BALANCE) {
                showError(getString(R.string.alert_dialog_min_balance_alert_message));
                return false;
            }

        } catch (NumberFormatException e) {
            Timber.e("NumberFormatException occured :" + e.getMessage());
            showError(getString(R.string.improper_input_msg));
            return false;
        }

        return true;

    }

    /**
     * show success message alert
     *
     * @param statusMessage
     */
    private void onSuccess(String title, String statusMessage) {
        AlertDialog.Builder alertDialogBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            alertDialogBuilder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(getActivity());
        }

        alertDialogBuilder
                .setTitle(title)
                .setMessage(statusMessage)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * start request for send money
     */
    private void requestData() {

        ClientToClientMoneyTransferRequest clientToClientMoneyTransferRequest = new ClientToClientMoneyTransferRequest();
        try {
            clientToClientMoneyTransferRequest.senderMobileNumber = LocalPreferenceUtility.getUserMobileNumber(getContext());
            clientToClientMoneyTransferRequest.receiverMobileNumber = mobileNumber.getText().toString();
            clientToClientMoneyTransferRequest.amount = amount.getText().toString();
            clientToClientMoneyTransferRequest.hash = MobicashUtils.getSha1(clientToClientMoneyTransferRequest.senderMobileNumber
                    + clientToClientMoneyTransferRequest.receiverMobileNumber
                    + clientToClientMoneyTransferRequest.amount);
        } catch (NoSuchAlgorithmException e) {
            Timber.e("NoSuchAlgorithmException message " + e.getMessage());
        }

        MobicashIntentService.startActionClientToClientMoneyTransfer(getContext(), clientToClientMoneyTransferRequest);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && isAdded()) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(clientLogsReceiver);
        }
    }


    /**
     * Handling broadcast event for send money .
     */
    private BroadcastReceiver clientLogsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && getActivity() != null && isAdded()) {
                cancelProgressDialog();
                if (action.equals(NetworkConstant.STATUS_USER_CLIENT_TO_CLIENT_MONEY_TRANSFER_SUCCESS)) {
                    ClientToClientMoneyTransferResponse clientToClientMoneyTransferResponse = (ClientToClientMoneyTransferResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (clientToClientMoneyTransferResponse != null) {
                        onSuccess(getString(R.string.title_status), clientToClientMoneyTransferResponse.msg);
                    }
                } else if (action.equals(NetworkConstant.STATUS_USER_CLIENT_TO_CLIENT_MONEY_TRANSFER_FAIL)) {
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null && failureResponse.msg != null) {
                        showError(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showError(errorMessage);
                    }
                }
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * show profile upload progress dialog
     */
    protected void showProgressDialog() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.setIcon(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * cancel upload progress dialog
     */

    protected void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    /**
     * show error dialog
     */

    public void showError(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }


}
