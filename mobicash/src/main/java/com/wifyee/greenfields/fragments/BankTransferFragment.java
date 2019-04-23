package com.wifyee.greenfields.fragments;

import android.app.Activity;
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
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.activity.AddBeneficiaryOTPActivity;
import com.wifyee.greenfields.activity.MobicashDashBoardActivity;
import com.wifyee.greenfields.adapters.BeneficiaryListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.requests.BeneficiaryBean;
import com.wifyee.greenfields.models.requests.ClientBankTransferRequest;
import com.wifyee.greenfields.models.response.BankTransferResponse;
import com.wifyee.greenfields.models.response.CityResponse;
import com.wifyee.greenfields.models.response.ClientBankTransferResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * Created by sumanta on 12/22/16.
 */

public class BankTransferFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    // Tag for logging.
    public static final String TAG = BankTransferFragment.class.getSimpleName();
    public ProgressDialog progressDialog = null;
    public ArrayList<String> beneficiaryList=new ArrayList<>();
    private String beneFiciaryID="";
    private String branchName="";
    private boolean otpVerify;

    @BindView(R.id.tv_ifsc)
    TextView ifscCode;

    @BindView(R.id.spinner_select_beneficiary)
    Spinner spinnerBeneficiary;

    @BindView(R.id.edit_beneficiary_amount)
    EditText beneAmount;

    @BindView(R.id.edit_text_comments)
    EditText beneComments;

    @BindView(R.id.tv_bank_account)
    TextView bankAccount;

    @BindView(R.id.tv_bank_name)
    TextView bankName;

    @BindView(R.id.tv_beneficiary_name)
    TextView beneficiaryName;

    @BindView(R.id.tv_beneficiary_mobile_number)
    TextView beneMobileNumber;

    @BindView(R.id.tv_kyc_status)
    TextView mkycStatus;

    @BindView(R.id.verify_otp)
    TextView mVerifyOtp;

    @BindView(R.id.btn_submit)
    Button submit;

    @BindView(R.id.radio_group_transfer_type)
    RadioGroup typeGroup;

    //transfer type
    private String type = MobicashUtils.TRANSFER_TYPE_NEFT;
    private Context mContext = null;
    private BeneficiaryBean beneficiaryBean;
    private ArrayList<BeneficiaryBean> beneficiaryBeenList=new ArrayList<>();
    private BeneficiaryBean beneficiaryBeanLocalData;
    /**
     * listener for handing progress
     */
    BankTransferFragmentListener bankTransferFragmentListener;
    /**
     * listener for progress of request
     */
    public interface BankTransferFragmentListener {

        void overridePendingTransition();

        void showError(String msg);

        void showProgress();

        void cancelProgress();

    }
    /**
     * blank constructor required
     */
    public BankTransferFragment() {
    }
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_CLIENT_BANK_TRANSFER_SUCCESS,
            NetworkConstant.STATUS_CLIENT_BANK_TRANSFER_FAIl
    };

    /**
     * ifsc code patter regex
     */
    public static final Pattern VALID_IFSC_CODE_REGEX = Pattern.compile("[A-Z|a-z]{4}[0][\\d]{6}", Pattern.CASE_INSENSITIVE);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //showProgressDialog();
        callApiBeneficiaryList(getActivity());
        View view = inflater.inflate(R.layout.bank_transfer_fragment, container, false);
        ButterKnife.bind(this, view);
        bankTransferFragmentListener = (BankTransferFragmentListener) getActivity();
        typeGroup.setOnCheckedChangeListener(this);
        submit.setOnClickListener(this);
        spinnerBeneficiary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem=beneficiaryBeenList.get(position).getBeneficiary_id();
                bindUI(selectedItem,beneficiaryBeenList);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mVerifyOtp.setOnClickListener(this);
        return view;
    }
    //Bind UI of Fund Details
    private void bindUI(String selectedItem, ArrayList<BeneficiaryBean> beneficiaryBeenList)
    {
        for(int k=0;k<beneficiaryBeenList.size();k++)
        {
            if (beneficiaryBeenList.get(k).getBeneficiary_id().equals(selectedItem)) {
                beneficiaryBeanLocalData = new BeneficiaryBean();
                beneficiaryBeanLocalData.setBenBankName(beneficiaryBeenList.get(k).getBenBankName());
                beneficiaryBeanLocalData.setBenFirstName(beneficiaryBeenList.get(k).getBenFirstName());
                beneficiaryBeanLocalData.setBeneficiary_id(beneficiaryBeenList.get(k).getBeneficiary_id());
                beneficiaryBeanLocalData.setBenIfscCode(beneficiaryBeenList.get(k).getBenIfscCode());
                beneficiaryBeanLocalData.setBenMobileNumber(beneficiaryBeenList.get(k).getBenMobileNumber());
                beneficiaryBeanLocalData.setBenAccountNumber(beneficiaryBeenList.get(k).getBenAccountNumber());
                beneficiaryBeanLocalData.setKYC(beneficiaryBeenList.get(k).getKYC());
                beneFiciaryID = beneficiaryBeenList.get(k).getBeneficiary_id();
                branchName = beneficiaryBeenList.get(k).getBenBranchName();
                otpVerify= beneficiaryBeenList.get(k).isBenOtpVerify();
                bindUiData(beneficiaryBeanLocalData,otpVerify);
            }
            else {
                Toast.makeText(getActivity(),"There is no data",Toast.LENGTH_SHORT).show();
            }
        }
    }
   //Bind UI View data
    private void bindUiData(BeneficiaryBean beneficiaryBeanLocalData, boolean otpVerify) {
        if(otpVerify==false) {
            mVerifyOtp.setVisibility(View.VISIBLE);
        }else if(otpVerify==true){
            mVerifyOtp.setVisibility(View.GONE);
        }
        ifscCode.setText(beneficiaryBeanLocalData.getBenIfscCode());
        bankAccount.setText(beneficiaryBeanLocalData.getBenAccountNumber());
        beneMobileNumber.setText(beneficiaryBeanLocalData.getBenMobileNumber());
        bankName.setText(beneficiaryBeanLocalData.getBenBankName());
        beneficiaryName.setText(beneficiaryBeanLocalData.getBenFirstName());
        mkycStatus.setText(beneficiaryBeanLocalData.getKYC());
        ifscCode.setText(beneficiaryBeanLocalData.getBenIfscCode());

    }
    //Call Api Beneficiary List
    private void callApiBeneficiaryList(final Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", LocalPreferenceUtility.getUserCode(context));
            jsonObject.put("userType","Client");
            jsonObject.put("agentCode","RS00789");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.USER_BENEFICIARY_LIST_END_POINT ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        JSONObject jsonObject=response.getJSONObject("data");
                        JSONObject jsonData=jsonObject.getJSONObject("DATA");
                        JSONArray jsonArray=jsonData.getJSONArray("BENEFICIARY_DATA");
                        beneficiaryBeenList.clear();
                        for(int i=0;i<jsonArray.length();i++) {
                            beneficiaryBean=new BeneficiaryBean();
                            JSONObject benJsonObject=jsonArray.getJSONObject(i);
                            String benID=benJsonObject.getString("BENE_ID");
                            String benMobile=benJsonObject.getString("BENE_MOBILENO");
                            String benName=benJsonObject.getString("BENE_NAME");
                            String benNickName=benJsonObject.getString("BENE_NICKNAME");
                            String benBankName=benJsonObject.getString("BENE_BANKNAME");
                            String benBankAccount=benJsonObject.getString("BANK_ACCOUNTNO");
                            String benIFSCCode=benJsonObject.getString("BANKIFSC_CODE");
                            String benStatus=benJsonObject.getString("BENEVERIFIED_STATUS");
                            boolean benOtpVerify=benJsonObject.getBoolean("BENE_OTP_VERIFIED");
                            beneficiaryBean.setBenIfscCode(benIFSCCode);
                            beneficiaryBean.setBenAccountNumber(benBankAccount);
                            beneficiaryBean.setBenMobileNumber(benMobile);
                            beneficiaryBean.setBenFirstName(benName);
                            beneficiaryBean.setBenBankName(benBankName);
                            beneficiaryBean.setStatus(benStatus);
                            beneficiaryBean.setBeneficiary_id(benID);
                            beneficiaryBean.setBenOtpVerify(benOtpVerify);
                            beneficiaryBeenList.add(beneficiaryBean);
                            beneficiaryList.clear();
                            for(int j=0;j<beneficiaryBeenList.size();j++) {
                                beneficiaryList.add(beneficiaryBeenList.get(j).getBenFirstName());
                            }
                            //bindSpinnerAdapter(cityListResponse);
                            cancelProgressDialog();
                        }
                        //Bind BeneFiciary list
                        bindBeneFiciaryList(beneficiaryList);
                        cancelProgressDialog();
                    }
                    else {
                        cancelProgressDialog();
                        Toast.makeText(context, "Some Issue in Getting response", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    cancelProgressDialog();
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

    ///BInd Adapter Beneficiary List
    private void bindBeneFiciaryList(ArrayList<String> beneficiaryBeenList)
    {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, beneficiaryBeenList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBeneficiary.setAdapter(spinnerArrayAdapter);
        cancelProgressDialog();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_submit:
                if (onValidate()) {
                    if (MobicashUtils.isNetworkAvailable(getActivity())) {
                        onSubmitPressed();
                    } else {
                        showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.network_not_available));
                    }
                }
                break;
            case R.id.verify_otp:
                callApiVerifyOtp(beneFiciaryID,getActivity());
                break;
            default:
                break;
        }
    }
   //Calling verify Otp
    private void callApiVerifyOtp(final String beneFiciaryID, final Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", LocalPreferenceUtility.getUserCode(context));
            jsonObject.put("userType","Client");
            jsonObject.put("agentCode","RS00789");
            jsonObject.put("customerMobile",LocalPreferenceUtility.getUserMobileNumber(context));
            jsonObject.put("requestFor","CUSTVERIFICATION");

            StringBuilder sb = new StringBuilder(LocalPreferenceUtility.getUserCode(context));
            sb.append("Client");
            sb.append("RS00789");
            sb.append(LocalPreferenceUtility.getUserMobileNumber(context));
            sb.append("CUSTVERIFICATION");
            try {
                String hashValue="";
                hashValue = MobicashUtils.getSha1(sb.toString());
                jsonObject.put("hash",hashValue);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.USER_SEND_BENEFICIARY_OTP_VERIFICATION_END_POINT ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0)
                    {
                        String  requestCode = response.getString("responseCode");
                        //call Activity for verify OTP
                        callActivityVerifyOtp(context,requestCode,beneFiciaryID);
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

    //Call class for very Otp
    private void callActivityVerifyOtp(Context context, String code, String benId) {
        startActivity(new Intent(context, AddBeneficiaryOTPActivity.class).putExtra("requestCode",code).putExtra("benId",benId));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(bankTransferStatusReceiver, new IntentFilter(action));
        }
     //   callApiBeneficiaryList(getActivity());
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(bankTransferStatusReceiver);
    }
    /**
     * start action
     */
    private void onSubmitPressed() {
        if (mContext != null) {
            bankTransferFragmentListener.showProgress();
            MobicashIntentService.startActionClientBankTransfer(mContext, getClientBankTransferRequest());
        }
    }
    /**
     * get bank transfer client request model
     * clientmobile+clientId+pincode +transferType+accountNo+IFSC+ amount+beneficiaryMobile = hash
     */
    private ClientBankTransferRequest getClientBankTransferRequest()
    {
        ClientBankTransferRequest clientBankTransferRequest = new ClientBankTransferRequest();
        clientBankTransferRequest.agentCode="9782868195211807";
        clientBankTransferRequest.userMobile = LocalPreferenceUtility.getUserMobileNumber(mContext);
        clientBankTransferRequest.userTerminal="";
        clientBankTransferRequest.pincode = LocalPreferenceUtility.getUserPassCode(mContext);
        clientBankTransferRequest.userId = LocalPreferenceUtility.getUserCode(mContext);
        clientBankTransferRequest.transferType = type;
        clientBankTransferRequest.accountNo = bankAccount.getText().toString().trim();
        clientBankTransferRequest.IFSC = ifscCode.getText().toString().trim();
        clientBankTransferRequest.bankName = bankName.getText().toString().trim();
        clientBankTransferRequest.branchName = branchName;
        clientBankTransferRequest.amount = beneAmount.getText().toString().trim();
        clientBankTransferRequest.firstName = LocalPreferenceUtility.getUserFirstsName(mContext);
        clientBankTransferRequest.lastName = LocalPreferenceUtility.getUserLastName(mContext);
        clientBankTransferRequest.beneficiaryMobile = beneMobileNumber.getText().toString();
        clientBankTransferRequest.emailId = LocalPreferenceUtility.getUserEmail(mContext);
        clientBankTransferRequest.comments = beneComments.getText().toString().trim();
        clientBankTransferRequest.userType = PaymentConstants.USER_CLIENT;
        clientBankTransferRequest.beneficiaryID=beneFiciaryID;
        clientBankTransferRequest.kycStatus="NON-KYC";
        StringBuilder builder = new StringBuilder(clientBankTransferRequest.agentCode);
        builder.append(clientBankTransferRequest.userType);
        builder.append(clientBankTransferRequest.userId);
        builder.append(clientBankTransferRequest.userMobile);
        builder.append(clientBankTransferRequest.transferType);
        builder.append(clientBankTransferRequest.accountNo);
        builder.append(clientBankTransferRequest.IFSC);
        builder.append(clientBankTransferRequest.amount);
        builder.append(clientBankTransferRequest.beneficiaryMobile);
        builder.append(clientBankTransferRequest.beneficiaryID);
        try {
            clientBankTransferRequest.hash=MobicashUtils.getSha1(builder.toString());
        } catch (Exception e) {
            Timber.e("Exception occured : " + e.getMessage());
        }
        return clientBankTransferRequest;
    }
    /**
     * Handling broadcast event for user log .
     */
    private BroadcastReceiver bankTransferStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && bankTransferFragmentListener != null && getActivity() != null && isAdded()) {
                String action = intent.getAction();
                bankTransferFragmentListener.cancelProgress();
                if (action.equals(NetworkConstant.STATUS_CLIENT_BANK_TRANSFER_SUCCESS)) {
                    bankTransferFragmentListener.cancelProgress();
                    showSuccesDialog(getActivity());
                    ClientBankTransferResponse clientBankTransferResponse = (ClientBankTransferResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (clientBankTransferResponse != null && clientBankTransferResponse.bankTransferStatus != null
                            && clientBankTransferResponse.bankTransferStatus.mobicashClientBankTransferResponseList != null) {
                        onSuccess(getString(R.string.bank_transfer_status_title), clientBankTransferResponse.bankTransferStatus.mobicashClientBankTransferResponseList);
                    }
                }
                else if (action.equals(NetworkConstant.STATUS_CLIENT_BANK_TRANSFER_FAIl)) {
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    bankTransferFragmentListener.cancelProgress();
                    if (failureResponse != null && failureResponse.msg != null) {
                        bankTransferFragmentListener.showError(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        bankTransferFragmentListener.showError(errorMessage);
                    }

                }
            }
        }
    };

    private void showSuccesDialog(final Activity context)
    {
        final Dialog layout = new Dialog(context);
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
                    startActivity(new Intent(context,MobicashDashBoardActivity.class));
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
    }
    /**
     * show success message alert
     *
     * @param transferStatusList
     */
    private void onSuccess(String title, List transferStatusList) {
        if (!transferStatusList.isEmpty()) {
            // custom dialog
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_transfer_status_layout);
            dialog.setTitle(title);
            try {
                BankTransferResponse bankTransferResponse = (BankTransferResponse) transferStatusList.get(0);
                if (bankTransferResponse != null) {
                    // transfer status
                    if (bankTransferResponse.bankTransferStatus != null && !bankTransferResponse.bankTransferStatus.isEmpty()) {
                        ((TextView) dialog.findViewById(R.id.tv_bank_transfer_status_value)).setText(bankTransferResponse.bankTransferStatus);
                    }
                    // reference id
                    if (bankTransferResponse.bankTransferReferenceId != null && !bankTransferResponse.bankTransferReferenceId.isEmpty()) {
                        ((TextView) dialog.findViewById(R.id.tv_bank_transfer_reference_id_value)).setText(bankTransferResponse.bankTransferReferenceId);
                    }
                    // transaction id
                    if (bankTransferResponse.bankTransferTransactionId != null && !bankTransferResponse.bankTransferTransactionId.isEmpty()) {
                        ((TextView) dialog.findViewById(R.id.tv_bank_transfer_transaction_id_value)).setText(bankTransferResponse.bankTransferTransactionId);
                    }
                    //transaction_date
                    if (bankTransferResponse.bankTransferTransactionDate != null && !bankTransferResponse.bankTransferTransactionDate.isEmpty()) {
                        ((TextView) dialog.findViewById(R.id.tv_bank_transfer_date_value)).setText(bankTransferResponse.bankTransferTransactionDate);
                    }
                    //desc
                    if (bankTransferResponse.bankTransferDescription != null && !bankTransferResponse.bankTransferDescription.isEmpty()) {
                        ((TextView) dialog.findViewById(R.id.tv_bank_transfer_description_value)).setText(bankTransferResponse.bankTransferDescription);
                    }
                    //recharge_amount
                    if (bankTransferResponse.bankTransferAmount != null && !bankTransferResponse.bankTransferAmount.isEmpty()) {
                        ((TextView) dialog.findViewById(R.id.tv_bank_transfer_amount_value)).setText(bankTransferResponse.bankTransferAmount);
                    }
                }
                Button okButton = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } catch (IndexOutOfBoundsException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    /**
     * validate inputs by user
     *
     * @return
     */
    private boolean onValidate() {
        //amount validation
        try {
            if (beneAmount.getText().toString().isEmpty() || beneAmount.getText().toString().equals("0")
                    || beneAmount.getText().toString().equals("00") || beneAmount.getText().toString().equals("000")) {
                showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.alert_dialog_min_balance_alert_message));
                return false;
            } else if (Integer.parseInt(beneAmount.getText().toString()) < 10) {
                showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.alert_dialog_min_balance_alert_message));
                return false;
            }
            else if (otpVerify==false) {
                showAlert(getString(R.string.alert_dialog_error_title), "Please Verify OTP");
                return false;
            }




        } catch (NumberFormatException nfe) {
            Log.d(TAG, nfe.getMessage());
            showAlert(getString(R.string.improper_input_title), getString(R.string.improper_input_msg));
            return false;
        }


        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int position) {
        switch (position) {
            case R.id.radio_button_neft:
                type = MobicashUtils.TRANSFER_TYPE_NEFT;
                break;
            case R.id.radio_button_imps:
                type = MobicashUtils.TRANSFER_TYPE_IMPS;
                break;
            default:
                break;
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
            alertDialogBuilder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
     * validate ifsc code
     *
     * @param ifsc
     * @return
     */
    public static boolean validateIFSC(String ifsc) {
        Matcher matcher = VALID_IFSC_CODE_REGEX.matcher(ifsc);
        return matcher.find();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
                if (beneAmount != null) {
                    beneAmount.setText("");
                }

            }

        }
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
}
