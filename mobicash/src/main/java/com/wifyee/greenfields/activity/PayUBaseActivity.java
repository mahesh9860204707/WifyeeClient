package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.adapters.SpinerAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.fragments.MonthYearPickerDialog;
import com.wifyee.greenfields.models.requests.AddMoneyWallet;
import com.wifyee.greenfields.models.requests.AirtimeRequest;
import com.wifyee.greenfields.models.requests.CardDetails;
import com.wifyee.greenfields.models.requests.GetClientProfileInfoRequest;
import com.wifyee.greenfields.models.requests.PayUPaymentGatewayRequest;
import com.wifyee.greenfields.models.response.BankListResponseModel;
import com.wifyee.greenfields.models.response.GetClientProfileInfoResponse;
import com.wifyee.greenfields.models.response.GetProfileInfo;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PayUBaseActivity extends FragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,AdapterView.OnItemSelectedListener {

    private Button payNowButton;
    private Spinner spinnerNetbanking;
    private ProgressBar mProgressBar;
    private RadioGroup typeGroup;
    private View debitCredit, netBanking;
    private ProgressDialog progressDialog = null;
    private Spinner bankListButton;
    private Dialog selectOperatorDialog;
    private String[] bankListArray = null;
    private String[] bankListCodeArray = null;
    private HashMap bankListMap = null;
    private int selectedIndex = 0;
    private TextView walletBalance,totalAmountToPay;
    private Spinner spinnerCustom;
    private String amountToPay ;
    private RadioButton bn,cc,dc,im;
    private String cardTypeValue="";
    private String bankCode="";
    private String bankCodeSpinner="";
    private String wiFypayment="";
    private String planId="";
    private String plandata="";
    private String planName="";
    private String keyAddMoney="";
    private String keyMerchantPay="";
    private String airtimeRequest="";
    private String foodkey="";
    private String foodRequest="";
    private  int flag;
    private RadioGroup radioGroup;
    private AirtimeRequest airtimeBeanData;
    private RadioButton radioSbi,radiohdfc,radioAxis,radioCiti,radioIcici;
    private String mobileNumberRecharge="";
    private List<BankListResponseModel> bankListResponseModelList=new ArrayList<>();
    private boolean isCvvValid = false;
    private boolean isExpiryMonthValid = false;
    private boolean isExpiryYearValid = false;
    private boolean isCardNumberValid = false;
    private String cvv;
    private int tranxidValue,cardTypeIndex;
    private int finalAmount;
    private TextView transactionId;
    private EditText nameOnCardEditText;
    private EditText cardNumberEditText;
    private EditText cardCvvEditText;
    private EditText cardExpiryMonthEditText;
    private EditText cardExpiryYearEditText;
    private EditText cardNameEditText;
    private CheckBox saveCardCheckBox;
    private CheckBox enableOneClickPaymentCheckBox;
    private ImageView cardImage;
    private ImageView cvvImage;
    private LinearLayout mLinearLayout;
    private TextView issuingBankDown;
    private SpinerAdapter spinerAdapter;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private ArrayList<String> cardTypeCode;
    private AddMoneyWallet addMoneyWallet;
    private String merchantID="";
    private String description="";
    private CardDetails cardDetails;
    private String firstName="";
    private String clientEmail="";
    private String lastName="";
    private LinearLayout linearRadioGroupLayout,checkBoxLinearLayout;
    private CheckBox mobicasCheckBox;
    private ArrayList<CardDetails> cardDetailsArrayList=new ArrayList<>();
    private Double foodAmount,walletParseAmount;
    private int walletMoney;

    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.BANK_LIST_SUCCESS,
            NetworkConstant.BANK_LIST_FAILURE,
            NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_SUCCESS,
            NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_FAIL,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ubase);
        keyAddMoney= getIntent().getStringExtra("addMoneyKey");
        keyMerchantPay = getIntent().getStringExtra("merchantPay");
        wiFypayment = getIntent().getStringExtra("wifyee");
        planName = getIntent().getStringExtra("planName");
        planId = getIntent().getStringExtra("planId");
        plandata = getIntent().getStringExtra("planData");
        description = getIntent().getStringExtra("description");
        airtimeRequest= getIntent().getStringExtra("airtime");
        //Getting food Value
        foodRequest=getIntent().getStringExtra("foodorder");
        foodkey=getIntent().getStringExtra("foodCart");
        mobileNumberRecharge=getIntent().getStringExtra("mobileNumber");
        merchantID= getIntent().getStringExtra(NetworkConstant.MERCHANT_ID);
        (payNowButton = (Button) findViewById(R.id.button_pay_now)).setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        amountToPay = getIntent().getStringExtra(PaymentConstants.STRING_EXTRA);
        airtimeBeanData= (AirtimeRequest) getIntent().getSerializableExtra(PaymentConstants.AIRTIME_EXTRA);
      /*  if (amountToPay != null) {
            finalAmount = Integer.parseInt(amountToPay);
        }
        else{
        }*/
        if (amountToPay != null ) {
            try {
                if(foodkey!= null) {
                    if (foodkey.equals("foodamount")) {
                        foodAmount = Double.parseDouble(amountToPay);
                        finalAmount = foodAmount.intValue();
                    } else if (!foodkey.equals("foodamount")) {
                        finalAmount = Integer.parseInt(amountToPay);
                    }
                }else{
                    finalAmount = Integer.parseInt(amountToPay);
                }

            } catch (NumberFormatException e) {

                e.printStackTrace();
            }
        }
        ((TextView) findViewById(R.id.textview_amount)).setText(PaymentConstants.AMOUNT + ": Rs "
                + getIntent().getStringExtra(PaymentConstants.STRING_EXTRA));
        transactionId = ((TextView) findViewById(R.id.textview_txnid));
        tranxidValue = MobicashUtils.generateTransactionId();
        transactionId.setText(PaymentConstants.TXN_ID + ": " + tranxidValue);
        typeGroup = (RadioGroup) findViewById(R.id.radio_group_payment_type);
        bn = (RadioButton) findViewById(R.id.radio_button_net_banking);
        cc = (RadioButton) findViewById(R.id.radio_button_credit_credit);
        dc = (RadioButton) findViewById(R.id.radio_button_debit_credit);
        im= (RadioButton) findViewById(R.id.radio_button_insta_mojo);
        typeGroup.setOnCheckedChangeListener(this);
        debitCredit = (View) findViewById(R.id.debit_credit);
        netBanking = (View) findViewById(R.id.netbanking);
        mobicasCheckBox= (CheckBox) findViewById(R.id.mobicashWallet);
        linearRadioGroupLayout = (LinearLayout) findViewById(R.id.linear_layout_radiogroup);
        checkBoxLinearLayout= (LinearLayout) findViewById(R.id.check_box_layout);
        bankListButton = (Spinner) netBanking.findViewById(R.id.bank_list_spinner);
        bankListButton.setOnItemSelectedListener(this);
        radioGroup = (RadioGroup)netBanking.findViewById(R.id.myRadioGroup);
        radioAxis = (RadioButton) netBanking.findViewById(R.id.axis);
        radioSbi = (RadioButton) netBanking.findViewById(R.id.sbi);
        radiohdfc = (RadioButton) netBanking.findViewById(R.id.hdfc);
        radioIcici = (RadioButton) netBanking.findViewById(R.id.icici);
        radioCiti = (RadioButton) netBanking.findViewById(R.id.citi);
      //  walletMoney=Integer.parseInt(walletBalance.getText().toString());
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.axis)
                {
                    bankCode="AXIB";
                    bankListButton.setSelection(0);
                    Toast.makeText(getApplicationContext(), "choice: Axis", Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.hdfc) {
                    bankCode="HDFB";
                    bankListButton.setSelection(7);
                    Toast.makeText(getApplicationContext(), "choice: Hdfc", Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.sbi) {
                    bankCode="SBIB";
                    bankListButton.setSelection(18);
                    Toast.makeText(getApplicationContext(), "choice: Sbi", Toast.LENGTH_SHORT).show();
                }
                else if(checkedId == R.id.icici) {
                    bankCode="ICIB";
                    bankListButton.setSelection(8);
                    Toast.makeText(getApplicationContext(), "choice: ICICI", Toast.LENGTH_SHORT).show();
                }
                else if(checkedId == R.id.citi) {
                    bankCode="CITNB";
                    bankListButton.setSelection(29);
                    Toast.makeText(getApplicationContext(), "choice: Citi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        walletBalance = (TextView) findViewById(R.id.current_balance);
        totalAmountToPay = (TextView) findViewById(R.id.total_paid_amount);
        nameOnCardEditText = (EditText) findViewById(R.id.edit_text_name_on_card);
        cardNumberEditText = (EditText) findViewById(R.id.edit_text_card_number);
        cardCvvEditText = (EditText) findViewById(R.id.edit_text_card_cvv);
        cardExpiryMonthEditText = (EditText) findViewById(R.id.edit_text_expiry_month);
        cardExpiryYearEditText = (EditText) findViewById(R.id.edit_text_expiry_year);
        cardNameEditText = (EditText) findViewById(R.id.edit_text_card_label);
        saveCardCheckBox = (CheckBox) findViewById(R.id.check_box_save_card);
        enableOneClickPaymentCheckBox = (CheckBox) findViewById(R.id.check_box_enable_oneclick_payment);
        cardImage = (ImageView) findViewById(R.id.image_card_type);
        cvvImage = (ImageView) findViewById(R.id.image_cvv);
        mLinearLayout = (LinearLayout) findViewById(R.id.layout_expiry_date);
        issuingBankDown = (TextView) findViewById(R.id.text_view_issuing_bank_down_error);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            cardExpiryMonthEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
                    newFragment.show(getFragmentManager(), "DatePicker");
                    newFragment.setListener(datePickerListener);
                }
            });

            cardExpiryYearEditText.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
                    newFragment.show(getFragmentManager(), "DatePicker");
                    newFragment.setListener(datePickerListener);
                }
            });
            datePickerListener
                    = new DatePickerDialog.OnDateSetListener() {

                // when dialog box is closed, below method will be called.
                public void onDateSet(DatePicker view, int selectedDay,
                                      int selectedMonth, int selectedYear) {
                    cardExpiryYearEditText.setText("" + selectedYear);
                    cardExpiryMonthEditText.setText("" + selectedMonth);

                    if (!cardExpiryMonthEditText.getText().toString().equals("") && !cardExpiryYearEditText.getText().toString().equals("")) {
                        isExpiryYearValid = true;
                        isExpiryMonthValid = true;
                    }
                    if (selectedYear == Calendar.YEAR && selectedMonth < Calendar.MONTH) {
                        isExpiryMonthValid = false;
                    }
                }
            };

        } else {
            cardExpiryYearEditText.setFocusableInTouchMode(true);
            cardExpiryMonthEditText.setFocusableInTouchMode(true);
            cardExpiryYearEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    charSequence.toString();
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }
                @Override
                public void afterTextChanged(Editable editable) {

                    if (editable.length() == 4 && Integer.parseInt(editable.toString()) >= Calendar.YEAR) {

                        isExpiryYearValid = true;

                    } else
                        isExpiryYearValid = false;

                }

            });

        }
        cardNameEditText.setVisibility(View.GONE);
        enableOneClickPaymentCheckBox.setVisibility(View.GONE);
        cardCvvEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                cvv = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
 //Intialize Custom Spinner
        initCustomSpinner();
//Getting Intent Value
        Intent intent = getIntent();
        if (intent != null) {
            try {
                addMoneyWallet = (AddMoneyWallet) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA_WALLET);
                addMoneyWallet.txnId=String.valueOf(tranxidValue);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(ResponseAttributeConstants.ADD_WALLET_AMOUNT, addMoneyWallet.amount);
                    jsonObject.put(ResponseAttributeConstants.ADD_WALLET_RECIEVER, addMoneyWallet.clientreceiver);
                    jsonObject.put(ResponseAttributeConstants.ADD_WALLET_HASH, addMoneyWallet.hash);
                    jsonObject.put(ResponseAttributeConstants.ADD_WALLET_PINCODE, addMoneyWallet.pincode);
                    jsonObject.put(ResponseAttributeConstants.ADD_WALLET_TXN_ID,addMoneyWallet.txnId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(PayUBaseActivity.this,cardDetailsArrayList);
        spinnerCustom.setAdapter(customSpinnerAdapter);
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardTypeValue=cardDetailsArrayList.get(position).getCardType().toString();
                cardTypeIndex = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mobicasCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.isChecked()) {
                    linearRadioGroupLayout.setVisibility(View.GONE);
                    debitCredit.setVisibility(View.GONE);
                    netBanking.setVisibility(View.GONE);
                }
                else {
                    linearRadioGroupLayout.setVisibility(View.VISIBLE);
                    debitCredit.setVisibility(View.VISIBLE);
                }

            }
        }
        );

        if(keyAddMoney!=null) {
            checkBoxLinearLayout.setVisibility(View.GONE);
        }
        else {
            checkBoxLinearLayout.setVisibility(View.VISIBLE);
        }
      /*  GetClientProfileRequest mGetClientProfileRequest = new GetClientProfileRequest();
        mGetClientProfileRequest.clientId = LocalPreferenceUtility.getUserCode(PayUBaseActivity.this);
        mGetClientProfileRequest.pincode = LocalPreferenceUtility.getUserPassCode(PayUBaseActivity.this);
        try {
            mGetClientProfileRequest.hash = MobicashUtils.getSha1(mGetClientProfileRequest.clientId + mGetClientProfileRequest.pincode);
        } catch (NoSuchAlgorithmException e) {
            Timber.e("NoSuchAlgorithmException Occurred . Message : " + e.getMessage());
        }

        MobicashIntentService.startActionGetClientProfile(PayUBaseActivity.this, mGetClientProfileRequest);*/
    }

    private void initCustomSpinner() {
        spinnerCustom= (Spinner) findViewById(R.id.spinner_card_type);
        cardDetails=new CardDetails();
        cardDetails.setCardName("--Select Card Type--");
        cardDetails.setCardType("None");
        cardDetailsArrayList.add(cardDetails);

        cardDetails=new CardDetails();
        cardDetails.setCardName("Visa Debit Cards (All Banks");
        cardDetails.setCardType("VISA");
        cardDetailsArrayList.add(cardDetails);

        cardDetails=new CardDetails();
        cardDetails.setCardName("MasterCard Debit Cards (All Banks)");
        cardDetails.setCardType("MAST");
        cardDetailsArrayList.add(cardDetails);

        cardDetails=new CardDetails();
        cardDetails.setCardName("CITI Debit Card");
        cardDetails.setCardType("CITD");
        cardDetailsArrayList.add(cardDetails);

        cardDetails=new CardDetails();
        cardDetails.setCardName("State Bank Maestro Cards");
        cardDetails.setCardType("SMAE");
        cardDetailsArrayList.add(cardDetails);

        cardDetails=new CardDetails();
        cardDetails.setCardName("Other Maestro Cards");
        cardDetails.setCardType("MAES");
        cardDetailsArrayList.add(cardDetails);
                // Spinner Drop down elements
    }
    /**
     * operator list action
     */
    private void onOperatorButtonPressed() {
        selectOperatorDialog = onCreateDialogForOperatorSelection();
        selectOperatorDialog.show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int position) {
        switch (position) {
            case R.id.radio_button_debit_credit:
                debitCredit.setVisibility(View.VISIBLE);
                netBanking.setVisibility(View.GONE);
                break;
            case R.id.radio_button_credit_credit:
                debitCredit.setVisibility(View.VISIBLE);
                netBanking.setVisibility(View.GONE);
                break;
            case R.id.radio_button_net_banking:
                debitCredit.setVisibility(View.GONE);
                netBanking.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_button_insta_mojo:
                debitCredit.setVisibility(View.GONE);
                netBanking.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(bankListReceiver, new IntentFilter(action));
        }
        showProgressDialog();
        MobicashIntentService.startActionGetBankList(getApplicationContext());
        MobicashIntentService.startActionGetClientProfileInfo(getApplicationContext(), getClientProfileInfoRequest());
    }

    /**
     * get operator list request
     */

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

    @Override
    protected void onPause() {
        super.onPause();
        cancelProgressDialog();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bankListReceiver);
    }

    /**
     * operator list and recharge status receiver
     */
    private BroadcastReceiver bankListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionOperatorList = intent.getAction();

            if (actionOperatorList.equals(NetworkConstant.BANK_LIST_SUCCESS)) {
                List<BankListResponseModel> bankListResponse = intent.getParcelableArrayListExtra(NetworkConstant.EXTRA_DATA);
                if (bankListResponse != null) {
                    if (bankListResponse != null)
                    {
                        bindSpinner(bankListResponse);
                      /*  bankListCodeArray= new String[bankListResponse.size()];
                        bankListMap = new HashMap(bankListResponse.size());

                        int j = 0;
                        for (BankListResponseModel item : bankListResponse) {
                            bankListArray[j] = item.getBankName();
                            bankListArray[j] = item.getBankCode();
                            bankListMap.put(item.getBankName(), item.getBankCode());
                            j++;
                        }
                        Arrays.sort(bankListArray);*/
                        bankListResponseModelList.addAll(bankListResponse);
                        cancelProgressDialog();
                    } else {
                        cancelProgressDialog();
                    }
                } else {
                    cancelProgressDialog();
                }
            } else if (actionOperatorList.equals(NetworkConstant.BANK_LIST_FAILURE)) {
                cancelProgressDialog();
            }
            if (actionOperatorList.equals(NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_SUCCESS)) {
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
                         lastName=getProfileInfo.client_lastname;
                    clientEmail=getProfileInfo.client_email;

                    walletBalance.setText(getString(R.string.current_wallet_balance, getProfileInfo.clientWalletBalance));
                    if(getProfileInfo.clientWalletBalance.equals("0"))
                    {
                        checkBoxLinearLayout.setVisibility(View.GONE);
                    }/*else{
                        checkBoxLinearLayout.setVisibility(View.VISIBLE);
                    }*/
                    if(amountToPay!=null) {
                        if (finalAmount > Integer.parseInt(getProfileInfo.clientWalletBalance)) {
                            int amount = finalAmount - Integer.parseInt(getProfileInfo.clientWalletBalance);
                            totalAmountToPay.setText(getString(R.string.total_amount, String.valueOf(amount)));
                            finalAmount = amount;
                        }
                        else {
                            totalAmountToPay.setHint(getString(R.string.total_amount));
                            totalAmountToPay.setText(amountToPay);

                        }
                    }
                } else {
                    walletBalance.setText(getString(R.string.current_wallet_balance, "0.00"));
                    if (!foodkey.equalsIgnoreCase("foodamount")) {
                        finalAmount = Integer.parseInt(amountToPay);
                    } else {
                        foodAmount=Double.parseDouble(amountToPay);
                        finalAmount=foodAmount.intValue();
                    }
                  //  finalAmount = Integer.parseInt(amountToPay);
                }

            } else if (actionOperatorList.equals(NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_FAIL)) {
                walletBalance.setText(getString(R.string.current_wallet_balance, "0.00"));
            }

        }
    };
    //Binding spinner for bank list
    private void bindSpinner(List<BankListResponseModel> bankListArray) {
        spinerAdapter = new SpinerAdapter(PayUBaseActivity.this, android.R.layout.simple_spinner_item, bankListArray);
        bankListButton.setAdapter(spinerAdapter);
    }
    /**
     * show error dialog
     */
    public void showErrorDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
    public Dialog onCreateDialogForOperatorSelection() {

        AlertDialog.Builder alertDialogBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(this);
        }

        alertDialogBuilder.setTitle(getString(R.string.bank_title))
                .setSingleChoiceItems(bankListArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex = which;
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

    /**
     * show profile upload progress dialog
     */
    protected void showProgressDialog() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            progressDialog = new ProgressDialog(this);
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

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_pay_now) {
            if(mobicasCheckBox.isChecked()) {
                flag =1;
                PayUPaymentGatewayRequest mPayUPaymentGatewayRequest = new PayUPaymentGatewayRequest();
                checkBoxLinearLayout.setVisibility(View.GONE);
                mPayUPaymentGatewayRequest.amount = totalAmountToPay.getText().toString();
                mPayUPaymentGatewayRequest.description="deduct From Wallet";
                mPayUPaymentGatewayRequest.merchantDebit=keyMerchantPay;
                mPayUPaymentGatewayRequest.email = LocalPreferenceUtility.getUserEmail(getApplicationContext());
                mPayUPaymentGatewayRequest.phone = LocalPreferenceUtility.getUserMobileNumber(getApplicationContext());
                mPayUPaymentGatewayRequest.mobile=mobileNumberRecharge;
                mPayUPaymentGatewayRequest.userId = LocalPreferenceUtility.getUserCode(getApplicationContext());
                    mPayUPaymentGatewayRequest.planId = planId;
                    mPayUPaymentGatewayRequest.planName = planName;
                    mPayUPaymentGatewayRequest.planData = plandata;
                    mPayUPaymentGatewayRequest.merchantId = merchantID;
                if(wiFypayment!=null) {
                    mPayUPaymentGatewayRequest.productinfo = wiFypayment;
                }
                mPayUPaymentGatewayRequest.userType = PaymentConstants.USER_CLIENT;
                mPayUPaymentGatewayRequest.tranxid = String.valueOf(tranxidValue);
                mPayUPaymentGatewayRequest.firstname = LocalPreferenceUtility.getUserFirstsName(getApplicationContext());
                mPayUPaymentGatewayRequest.lastname = LocalPreferenceUtility.getUserLastName(getApplicationContext());
                //call web view
                Intent i = new Intent(PayUBaseActivity.this,WebViewActivity.class);
                i.putExtra(NetworkConstant.EXTRA_DATA, mPayUPaymentGatewayRequest);
                i.putExtra(NetworkConstant.EXTRA_DATA_WALLET, addMoneyWallet);
                if(airtimeBeanData!=null) {
                    i.putExtra(PaymentConstants.AIRTIME_EXTRA,airtimeBeanData);
                }
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
            if(bn.isChecked()) {
                flag =2;
                PayUPaymentGatewayRequest mPayUPaymentGatewayRequest = new PayUPaymentGatewayRequest();
                checkBoxLinearLayout.setVisibility(View.GONE);
                mPayUPaymentGatewayRequest.amount = String.valueOf(finalAmount);
                mPayUPaymentGatewayRequest.ccvv = cardCvvEditText.getText().toString();
                mPayUPaymentGatewayRequest.ccexpyr = cardExpiryYearEditText.getText().toString();
                mPayUPaymentGatewayRequest.ccexpmon = cardExpiryMonthEditText.getText().toString();
                mPayUPaymentGatewayRequest.ccname = nameOnCardEditText.getText().toString();
                mPayUPaymentGatewayRequest.ccnum = cardNumberEditText.getText().toString();
                mPayUPaymentGatewayRequest.email = clientEmail;
                mPayUPaymentGatewayRequest.mobile=mobileNumberRecharge;
                mPayUPaymentGatewayRequest.phone = LocalPreferenceUtility.getUserMobileNumber(getApplicationContext());
                mPayUPaymentGatewayRequest.userId = LocalPreferenceUtility.getUserCode(getApplicationContext());
                mPayUPaymentGatewayRequest.merchantDebit = keyMerchantPay;
                if(mPayUPaymentGatewayRequest.planId!=null) {
                    mPayUPaymentGatewayRequest.planId = planId;
                    mPayUPaymentGatewayRequest.planName = planName;
                    mPayUPaymentGatewayRequest.planData = plandata;
                    mPayUPaymentGatewayRequest.merchantId = merchantID;
                    mPayUPaymentGatewayRequest.description = description;
                }
                if(keyAddMoney!= null) {
                    mPayUPaymentGatewayRequest.redirectURL = NetworkConstant.USER_CLIENT_PAYU_PAYMENT_GATEWAY_PAYMENT_RESPONSE_URL;
                    mPayUPaymentGatewayRequest.merchantId = merchantID;
                }
                else {
                    mPayUPaymentGatewayRequest.redirectURL = NetworkConstant.USER_CLIENT_PAYU_PAYMENT_UNWALLET_GATEWAY_PAYMENT_RESPONSE_URL;
                }
                mPayUPaymentGatewayRequest.bankcode=bankCode;
                mPayUPaymentGatewayRequest.merchantId ="";
                if(wiFypayment!=null) {
                    mPayUPaymentGatewayRequest.productinfo = wiFypayment;
                }
                else {
                    mPayUPaymentGatewayRequest.productinfo = "Airtime Mobile";
                }
                mPayUPaymentGatewayRequest.pg = PaymentConstants.PG_TYPE_NB;
                mPayUPaymentGatewayRequest.userType = PaymentConstants.USER_CLIENT;
                mPayUPaymentGatewayRequest.tranxid = String.valueOf(tranxidValue);
                mPayUPaymentGatewayRequest.firstname = firstName;
                mPayUPaymentGatewayRequest.lastname = lastName;
                //call web view
                Intent i = new Intent(PayUBaseActivity.this, WebViewActivity.class);
                i.putExtra(NetworkConstant.EXTRA_DATA, mPayUPaymentGatewayRequest);
                i.putExtra(NetworkConstant.EXTRA_DATA_WALLET, addMoneyWallet);
                if(airtimeBeanData!=null) {
                    i.putExtra(PaymentConstants.AIRTIME_EXTRA, airtimeBeanData);
                }
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
                if ((cc.isChecked() || dc.isChecked()) && cardTypeIndex!=0 && validate()) {
                      flag =0;
                    PayUPaymentGatewayRequest mPayUPaymentGatewayRequestCCDC = new PayUPaymentGatewayRequest();
                    checkBoxLinearLayout.setVisibility(View.GONE);
                    mPayUPaymentGatewayRequestCCDC.amount = String.valueOf(finalAmount);
                    mPayUPaymentGatewayRequestCCDC.ccvv = cardCvvEditText.getText().toString();
                    mPayUPaymentGatewayRequestCCDC.ccexpyr = cardExpiryYearEditText.getText().toString();
                    mPayUPaymentGatewayRequestCCDC.ccexpmon = cardExpiryMonthEditText.getText().toString();
                    mPayUPaymentGatewayRequestCCDC.ccname = nameOnCardEditText.getText().toString();
                    mPayUPaymentGatewayRequestCCDC.ccnum = cardNumberEditText.getText().toString();
                    mPayUPaymentGatewayRequestCCDC.merchantDebit = keyMerchantPay;
                    mPayUPaymentGatewayRequestCCDC.mobile=mobileNumberRecharge;
                    //mPayUPaymentGatewayRequestCCDC.email = LocalPreferenceUtility.getUserEmail(getApplicationContext());;
                    mPayUPaymentGatewayRequestCCDC.email = clientEmail;
                    mPayUPaymentGatewayRequestCCDC.phone = LocalPreferenceUtility.getUserMobileNumber(getApplicationContext());
                    mPayUPaymentGatewayRequestCCDC.userId = LocalPreferenceUtility.getUserCode(getApplicationContext());
                    if (mPayUPaymentGatewayRequestCCDC.planId!= null) {
                        mPayUPaymentGatewayRequestCCDC.planId = planId;
                        mPayUPaymentGatewayRequestCCDC.planName = planName;
                        mPayUPaymentGatewayRequestCCDC.planData = plandata;
                        mPayUPaymentGatewayRequestCCDC.merchantId = merchantID;
                        mPayUPaymentGatewayRequestCCDC.description = description;
                    }
                    if(keyAddMoney!= null) {
                        mPayUPaymentGatewayRequestCCDC.redirectURL = NetworkConstant.USER_CLIENT_PAYU_PAYMENT_GATEWAY_PAYMENT_RESPONSE_URL;
                    }
                    else {
                        mPayUPaymentGatewayRequestCCDC.redirectURL = NetworkConstant.USER_CLIENT_PAYU_PAYMENT_UNWALLET_GATEWAY_PAYMENT_RESPONSE_URL;
                    }
                    if (cc.isChecked()) {
                        mPayUPaymentGatewayRequestCCDC.pg = PaymentConstants.PG_TYPE_CC;
                    }
                    if (dc.isChecked()) {
                        mPayUPaymentGatewayRequestCCDC.pg = PaymentConstants.PG_TYPE_DC;
                    }
                    mPayUPaymentGatewayRequestCCDC.tranxid = String.valueOf(tranxidValue);
                    mPayUPaymentGatewayRequestCCDC.userType = PaymentConstants.USER_CLIENT;
                    mPayUPaymentGatewayRequestCCDC.bankcode = cardTypeValue;
                    mPayUPaymentGatewayRequestCCDC.firstname = firstName;
                    mPayUPaymentGatewayRequestCCDC.lastname = lastName;
                    if (merchantID != null) {
                        mPayUPaymentGatewayRequestCCDC.merchantId = merchantID;
                    }
                    mPayUPaymentGatewayRequestCCDC.state = "";
                    mPayUPaymentGatewayRequestCCDC.address1 = "";
                    if (wiFypayment != null) {
                        mPayUPaymentGatewayRequestCCDC.productinfo = wiFypayment;
                    } else {
                        mPayUPaymentGatewayRequestCCDC.productinfo = "Airtime Mobile";
                    }
                    mPayUPaymentGatewayRequestCCDC.city = "";
                    mPayUPaymentGatewayRequestCCDC.zipcode = "";
                    mPayUPaymentGatewayRequestCCDC.country = "";
                    //call web view
                    Intent i = new Intent(PayUBaseActivity.this, WebViewActivity.class);
                    i.putExtra(NetworkConstant.EXTRA_DATA_WALLET, addMoneyWallet);
                    i.putExtra(NetworkConstant.EXTRA_DATA, mPayUPaymentGatewayRequestCCDC);
                    if(airtimeBeanData!=null) {
                        i.putExtra(PaymentConstants.AIRTIME_EXTRA, airtimeBeanData);
                    }
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                 }
                 if(im.isChecked()) {
                     PayUPaymentGatewayRequest mPayUPaymentGatewayRequestCCDC = new PayUPaymentGatewayRequest();
                     checkBoxLinearLayout.setVisibility(View.GONE);
                     mPayUPaymentGatewayRequestCCDC.amount = String.valueOf(finalAmount);
                     mPayUPaymentGatewayRequestCCDC.tranxid = String.valueOf(tranxidValue);
                     mPayUPaymentGatewayRequestCCDC.userType = PaymentConstants.USER_CLIENT;
                     mPayUPaymentGatewayRequestCCDC.firstname = firstName;
                     mPayUPaymentGatewayRequestCCDC.lastname = lastName;
                     mPayUPaymentGatewayRequestCCDC.email = clientEmail;
                     //call web view
                     Intent i = new Intent(PayUBaseActivity.this, WebViewActivity.class);
                     i.putExtra(NetworkConstant.EXTRA_DATA_WALLET, addMoneyWallet);
                     i.putExtra("InstaMojo","instaMojo");
                     i.putExtra(NetworkConstant.EXTRA_DATA, mPayUPaymentGatewayRequestCCDC);
                     if(airtimeBeanData!=null) {
                         i.putExtra(PaymentConstants.AIRTIME_EXTRA, airtimeBeanData);
                     }
                     startActivity(i);
                     finish();
                     overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                 }
                if(flag==0) {
                 //   showDialog();
                }
               else if(flag==1) {
                    Toast.makeText(PayUBaseActivity.this,"Transaction Processing",Toast.LENGTH_SHORT).show();
                }
                else if(flag==2) {
                    Toast.makeText(PayUBaseActivity.this,"Transaction Processing",Toast.LENGTH_SHORT).show();
                }
        }


    }
    //show dialog
    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setTitle("Error")
                .setMessage("Please enter valid data")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    //Validation Form
    private boolean validate() {

        boolean status = true;

        if ((nameOnCardEditText.getText().toString().length() <= 0)) {
            status = false;
        }

        if(cardCvvEditText.getText().toString().length() <= 0 ) {

            status = false;
        }

        if(cardNumberEditText.getText().toString().length() <= 0) {
            status = false;
        }

        return status;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        bankCode =bankListResponseModelList.get(position).getBankCode();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<CardDetails> asr;

        public CustomSpinnerAdapter(Context context,ArrayList<CardDetails> asr) {
            this.asr=asr;
            activity = context;
        }
        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(PayUBaseActivity.this);
            txt.setPadding(10, 10, 10, 10);
            txt.setTextSize(14);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position).getCardName());
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(PayUBaseActivity.this);
            txt.setGravity(Gravity.LEFT);
            txt.setPadding(10, 10, 10, 10);
            txt.setTextSize(14);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.rect_shape_drop_down, 0);
            txt.setText(asr.get(i).getCardName());
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

    }
}

