package com.wifyee.greenfields.foodorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.SharedPrefence.SharedPreference;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.activity.AddAddress;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.foodorder.SharedPrefenceAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.foodorder.CartFoodOderRequest;
import com.wifyee.greenfields.mapper.*;
import com.wifyee.greenfields.models.requests.DeductMoneyWallet;
import com.wifyee.greenfields.foodorder.Items;
import com.wifyee.greenfields.foodorder.SharedPrefenceList;
import com.wifyee.greenfields.foodorder.CartFoodOrderResponse;
import com.wifyee.greenfields.foodorder.DeductMoneyWalletResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.foodorder.GstOnFoodItemResponse;
import com.wifyee.greenfields.models.response.PayUPaymentGatewayResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.wifyee.greenfields.activity.WebViewActivity.ACTION_STATUS_VALUE;

public class AddToCartActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton back;
    private SharedPreference sharedPreference;
    private ArrayList<Items> itemsArrayList=new ArrayList<>();
    private TextView selected_merchant,address,change;
    private RecyclerView recyclerView_favorites;
    private Context mcontext;
    private Button btn_continue,addAddress;
    private Double gst = 0.00, total_amount = 0.00;
    private SharedPrefenceAdapter favoritesAdapter;
    private List<Items> shred_list = new ArrayList<>();
    private Items itemValue ;
    private int paymentSelectedIndex = 0;
    CartFoodOderRequest foodOrderRequest=new CartFoodOderRequest();
    public ProgressDialog progressDialog = null;
    ///  public static List<SharedPrefenceList> DefaultOder_list;
    ArrayList<SharedPrefenceList> Current_favoritesList = new ArrayList<>();
    static public TextView tv_totalamount;
    private List<SharedPrefenceList> favorites;
    RelativeLayout rl_address;
    LinearLayout llBottom;
    public static boolean is_address_set;
    public static String completeAddress,location,latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        back = (ImageButton) findViewById(R.id.toolbar_back);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        tv_totalamount = (TextView) findViewById(R.id.tv_totalamount);
        addAddress = (Button) findViewById(R.id.add_address);
        change = (TextView) findViewById(R.id.change);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        address = (TextView) findViewById(R.id.address);
        llBottom = (LinearLayout) findViewById(R.id.ll);
        recyclerView_favorites = (RecyclerView) findViewById(R.id.favroite_recyclerview);
        mcontext = this;
        selected_merchant=(TextView)findViewById(R.id.merchant_names);
        sharedPreference = new SharedPreference(mcontext);
        // DefaultOder_list= sharedPreference.getFavorites(mcontext);
        favorites = sharedPreference.getFavorites(mcontext);
        selected_merchant.setText(LocalPreferenceUtility.getMerchantName(mcontext));
        MobicashIntentService.startActionGstONFoodItem(mcontext);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* String json = "{\"responseStatus\":\"success\",\"responseCode\":\"000\",\"ResponseMsg\":\"Transaction has been done successfully.\",\"RefId\":\"6683597859\",\"userId\":\"222\",\"userType\":\"client\",\"merIdtId\":\"\",\"transactionAmount\":\"1.00\",\"orderId\":\"1462284021\",\"transactionId\":\"178\",\"productInfo\":\"Airtime Mobile\"}";
                   try {
                        JSONObject response = new JSONObject(json);
                        response.getString("responseStatus");
                        if (response != null) {
                            PayUPaymentGatewayResponse  payUPaymentGatewayResponse = new PayUPaymentGatewayResponse();
                            if (response.has(ResponseAttributeConstants.RESPONSE_STATUS))
                                payUPaymentGatewayResponse.responseStatus = response.getString(ResponseAttributeConstants.RESPONSE_STATUS);
                            if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                                payUPaymentGatewayResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);
                            if (response.has(ResponseAttributeConstants.RESPONSE_MSG))
                                payUPaymentGatewayResponse.ResponseMsg = response.getString(ResponseAttributeConstants.RESPONSE_MSG);
                            if (response.has(ResponseAttributeConstants.REF_ID))
                                payUPaymentGatewayResponse.RefId = response.getString(ResponseAttributeConstants.REF_ID);
                            if (response.has(ResponseAttributeConstants.CLI_IDT_ID))
                                payUPaymentGatewayResponse.cliIdtId = response.getString(ResponseAttributeConstants.CLI_IDT_ID);
                            if (response.has(ResponseAttributeConstants.MER_IDT_ID))
                                payUPaymentGatewayResponse.merIdtId = response.getString(ResponseAttributeConstants.MER_IDT_ID);
                        }
                        Log.d("My App", response.toString());
                        Log.d("phonetype value ", response.getString("phonetype"));

                    } catch (Throwable tx) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
                    }*/
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });

        }

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //appy gst on price
                if (shred_list.size()!=0) {
                    shred_list.clear();
                }
                if (!tv_totalamount.getText().toString().equalsIgnoreCase("0.0")) {
                    double h = (gst * Double.parseDouble(tv_totalamount.getText().toString())) / 100;
                    total_amount = Double.parseDouble(tv_totalamount.getText().toString()) + h;
                    Toast.makeText(getApplicationContext(), String.valueOf(total_amount), Toast.LENGTH_SHORT).show();
                    setDialog();
                  //  MobicashIntentService.startActionSendFoodRequest(mcontext,getaddToCartRequest(String.valueOf(total_amount)));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Amount should be Greater Than 0.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddToCartActivity.this, AddAddress.class);
                intent.putExtra("cart","food_cart");
                startActivity(intent);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddToCartActivity.this, AddAddress.class);
                intent.putExtra("cart","food_cart");
                startActivity(intent);
            }
        });

        if (favorites != null) {
            Double totalamount = 0.00;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
            recyclerView_favorites.setLayoutManager(linearLayoutManager);
            favoritesAdapter = new SharedPrefenceAdapter(mcontext, recyclerView_favorites, favorites);
            for (int k = 0; k < favorites.size(); k++) {
                try {
                    String pricevlaue = favorites.get(k).price.trim();
                    Double current = Double.valueOf(pricevlaue);
                    totalamount = current + totalamount;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            tv_totalamount.setText(String.valueOf(totalamount));
            recyclerView_favorites.setAdapter(favoritesAdapter);
            favoritesAdapter.notifyDataSetChanged();
        }
    }

    private void setDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.paymentby_layout);
        dialog.setTitle("");
        dialog.setCancelable(false);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_grp);
       /* RadioButton pay_mobicash=(RadioButton)dialog.findViewById(R.id.pay_mobicash);
        RadioButton pay_payU=(RadioButton)dialog.findViewById(R.id.pay_payU);*/
        Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
        Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int position) {
                switch (position) {
                    case R.id.pay_mobicash:
                        paymentSelectedIndex = 0;
                        break;
                    case R.id.pay_payU:
                        paymentSelectedIndex = 1;
                        break;
                    case R.id.cash:
                        paymentSelectedIndex = 2;
                        break;
                    default:
                        break;
                }
            }
        });
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  /*  double h = (gst * Double.parseDouble(FoodPrice.getText().toString())) / 100;
                    total_amount = Double.parseDouble(FoodPrice.getText().toString()) + h;*/
                //    Toast.makeText(getApplicationConftext(), String.valueOf(total_amount), Toast.LENGTH_SHORT).show();
                if (paymentSelectedIndex == 0) {
                      showProgressDialog();
                    MobicashIntentService.startActionDeductMoneyWallet(mcontext, getpaymetbywallet(String.valueOf(total_amount)));
                } else if ((paymentSelectedIndex == 1)) {
                    Intent paymentIntent = IntentFactory.createPayuBaseActivity(mcontext);
                    paymentIntent.putExtra(PaymentConstants.STRING_EXTRA, String.valueOf(total_amount));
                    paymentIntent.putExtra("foodCart", "foodamount");
                    paymentIntent.putExtra(PaymentConstants.FOODORDER_EXTRA, getaddToCartRequest(String.valueOf(total_amount)));
                    startActivity(paymentIntent);
                }
                else if ((paymentSelectedIndex == 2)) {
                    showProgressDialog();
                    MobicashIntentService.startActionSendFoodRequest(mcontext,
                            getaddToCartRequest(String.valueOf(total_amount)),location,latitude,longitude,completeAddress);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private DeductMoneyWallet getpaymetbywallet(String amount) {
        DeductMoneyWallet foodPaymentbywallet = new DeductMoneyWallet();
        foodPaymentbywallet.clientMobile = LocalPreferenceUtility.getUserMobileNumber(mcontext);
        foodPaymentbywallet.pincode = LocalPreferenceUtility.getUserPassCode(mcontext);
        foodPaymentbywallet.amount = amount;
        foodPaymentbywallet.description = "foodOrder";
        StringBuilder sb = new StringBuilder(foodPaymentbywallet.clientMobile);
        sb.append(foodPaymentbywallet.amount);
        sb.append(MobicashUtils.md5(foodPaymentbywallet.pincode));
        try {
            foodPaymentbywallet.hash = MobicashUtils.getSha1(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        ;
        return foodPaymentbywallet;
    }
    private CartFoodOderRequest getaddToCartRequest(String finalprice) {
        foodOrderRequest =new CartFoodOderRequest();
        itemsArrayList.clear();
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss", Locale.US);
        SimpleDateFormat format=new SimpleDateFormat("yyyymmddhhmmss",Locale.US);
        String datetime = formatter.format(today);
        String code_format=format.format(today);
        foodOrderRequest.orderId ="C_"+LocalPreferenceUtility.getMerchantId(mcontext)+code_format;

        for (int y = 0; y < sharedPreference.getFavorites(mcontext).size(); y++) {
           itemsArrayList.add(new Items(sharedPreference.getFavorites(mcontext).get(y).mercantId,
                   sharedPreference.getFavorites(mcontext).get(y).quantiy,
                   sharedPreference.getFavorites(mcontext).get(y).price));
           foodOrderRequest.setItems(itemsArrayList);
        }
        if (paymentSelectedIndex==0) {
            foodOrderRequest.payment_mode="wallet";
        } else if (paymentSelectedIndex==1)  {
            foodOrderRequest.payment_mode="payu";
        }
        else if (paymentSelectedIndex==2) {
            foodOrderRequest.payment_mode="cod";
        }
        foodOrderRequest.userId = LocalPreferenceUtility.getUserCode(mcontext);
        foodOrderRequest.merchantId = LocalPreferenceUtility.getMerchantId(mcontext);
        foodOrderRequest.orderDateTime = datetime;
        foodOrderRequest.orderPrice = finalprice;
        foodOrderRequest.userType = "client";
        return foodOrderRequest;
    }

    private void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerView_favorites.setAdapter(favoritesAdapter);
        favoritesAdapter.notifyDataSetChanged();

        cancelProgressDialog();
        LocalBroadcastManager.getInstance(mcontext).unregisterReceiver(gstOnFoodItem);
    }


    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mcontext);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(gstOnFoodItem, new IntentFilter(action));
        }

        sharedPreference=new SharedPreference(mcontext);
        favorites=sharedPreference.getFavorites(mcontext);
        favoritesAdapter = new SharedPrefenceAdapter(mcontext, recyclerView_favorites, favorites);
        recyclerView_favorites.setAdapter(favoritesAdapter);
        favoritesAdapter.notifyDataSetChanged();
        if (favorites != null) {
            Double totalamount = 0.00;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
            recyclerView_favorites.setLayoutManager(linearLayoutManager);
            favoritesAdapter = new SharedPrefenceAdapter(mcontext, recyclerView_favorites, favorites);
            for (int k = 0; k < favorites.size(); k++) {
                try {
                    String pricevlaue = favorites.get(k).price.trim();
                    Double current = Double.valueOf(pricevlaue);
                    totalamount = current + totalamount;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            tv_totalamount.setText(String.valueOf(totalamount));
            recyclerView_favorites.setAdapter(favoritesAdapter);
            favoritesAdapter.notifyDataSetChanged();
        }

        if (is_address_set){
            rl_address.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
            addAddress.setVisibility(View.INVISIBLE);
            addAddress.setClickable(false);
            address.setText(completeAddress);
        }

    }

    protected void showProgressDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(mcontext, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            progressDialog = new ProgressDialog(mcontext);
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.setIcon(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_SUCCESS,
            NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_FAIL,
            NetworkConstant.STATUS_GSTONFOOD_LIST_SUCCESS,
            NetworkConstant.STATUS_GSTONFOOD_LIST_FAIL,
            NetworkConstant.STATUS_FOODODER_BYMERCHANT_LIST_SUCCESS,
            NetworkConstant.STATUS_FOODORDER_BYMERCHANT_LIST_FAIL,
            NetworkConstant.STATUS_DEDUCTMONEY_WALLET_SUCCESS,
            NetworkConstant.STATUS_DEDUCT_MONEY_WALLET_FAIL
    };
    private BroadcastReceiver gstOnFoodItem = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionOperatorList = intent.getAction();
            if(actionOperatorList.equals(NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_SUCCESS)) {
                String content = intent.getStringExtra(ACTION_STATUS_VALUE);
                try {
                    JSONObject response = new JSONObject(content);
                    PayUPaymentGatewayResponse mPayUPaymentGatewayResponse = com.wifyee.greenfields.mapper.ModelMapper.transformJSONObjectToPayUPaymentGatewayResponse(response);
                    onPayUSuccess(mPayUPaymentGatewayResponse);
                   // showSuccessDialog(mPayUPaymentGatewayResponse.getResponseMsg());
                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + content + "\"");
                }
                PayUPaymentGatewayResponse payUPaymentGatewayResponse = (PayUPaymentGatewayResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if(payUPaymentGatewayResponse!=null) {
                    onPayUSuccess(payUPaymentGatewayResponse);
                }
            }else  if(actionOperatorList.equals(NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_FAIL)){
            }
            if (actionOperatorList.equals(NetworkConstant.STATUS_GSTONFOOD_LIST_SUCCESS)) {
                GstOnFoodItemResponse gstOnFoodItemResponse = (GstOnFoodItemResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (gstOnFoodItemResponse != null && gstOnFoodItemResponse.gst != null) {
                    cancelProgressDialog();
                    gst = Double.valueOf(gstOnFoodItemResponse.gst);
                } else {
                    gst = 0.00;
                    cancelProgressDialog();
                }
            } else if (actionOperatorList.equals(NetworkConstant.STATUS_GSTONFOOD_LIST_FAIL)) {
                gst = 0.00;
                cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    cancelProgressDialog();
                 //   showErrorDialog(failureResponse.msg);
                }
               // showErrorDialog(String.valueOf(R.string.error_message));
            }
            if (actionOperatorList.equals(NetworkConstant.STATUS_FOODODER_BYMERCHANT_LIST_SUCCESS)) {
                CartFoodOrderResponse gstOnFoodItemResponse = (CartFoodOrderResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (gstOnFoodItemResponse != null && gstOnFoodItemResponse.status != null) {
                    cancelProgressDialog();
                    showSuccessDialog("Success");
                }
            } else if (actionOperatorList.equals(NetworkConstant.STATUS_FOODORDER_BYMERCHANT_LIST_FAIL)) {
                cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    cancelProgressDialog();
                    showErrorDialog(failureResponse.msg);
                }
                showErrorDialog(String.valueOf(R.string.error_message));
            }
            if (actionOperatorList.equals(NetworkConstant.STATUS_DEDUCTMONEY_WALLET_SUCCESS)) {
                DeductMoneyWalletResponse deductMoneyWalletResponse = (DeductMoneyWalletResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (deductMoneyWalletResponse != null && deductMoneyWalletResponse.status.equalsIgnoreCase("1")) {
                    onSuccess(deductMoneyWalletResponse);
                }
            }  else if (actionOperatorList.equals(NetworkConstant.STATUS_DEDUCT_MONEY_WALLET_FAIL)) {
                cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    cancelProgressDialog();
                    showErrorDialog(failureResponse.msg);
                }
                showErrorDialog(getString(R.string.error_message));
            }
        }
        public void showErrorDialog(String message) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);
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
        private void showSuccessDialog(final String msg) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext);
            alertDialogBuilder
                    .setTitle("Success")
                    .setMessage(msg)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            SharedPreference sharedPreference = new SharedPreference();
                            sharedPreference.clearCart(AddToCartActivity.this);
                            finish();
                            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    };
    private void onPayUSuccess(PayUPaymentGatewayResponse payUPaymentGatewayResponse) {
        cancelProgressDialog();
        //sharedPreference.removeFavoriteItem(mcontext, curent_productItem);
        if (payUPaymentGatewayResponse.getResponseCode().equalsIgnoreCase("000")) {
            final Dialog dialog = new Dialog(mcontext);
            dialog.setContentView(R.layout.payu_transaction_result);
            dialog.setTitle("Detail");
            dialog.setCancelable(false);
            if (payUPaymentGatewayResponse.getResponseStatus()!= null) {
                ((TextView) dialog.findViewById(R.id.txt_status)).setText("Success");
            }
            if (payUPaymentGatewayResponse.getRefId() != null) {
                ((TextView) dialog.findViewById(R.id.txt_balance)).setText(payUPaymentGatewayResponse.getRefId());
            }
            if (payUPaymentGatewayResponse.getCliIdtId() != null) {
                ((TextView) dialog.findViewById(R.id.txt_clientId)).setText(payUPaymentGatewayResponse.getCliIdtId());
            }
            if (payUPaymentGatewayResponse.getMerIdtId() != null) {
                ((TextView) dialog.findViewById(R.id.txt_transactiondate)).setText(payUPaymentGatewayResponse.getMerIdtId());
            }
            Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
            // if button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    MobicashIntentService.startActionSendFoodRequest(mcontext, getaddToCartRequest(String.valueOf(total_amount)),
                            location,latitude,longitude,completeAddress);
                    dialog.dismiss();

                }
            });
            dialog.show();
        } else
        {
            // showSuccessDialog("Transaction Fail");
        }
    }

    private void onSuccess(DeductMoneyWalletResponse deductMoneyWalletResponse) {
        cancelProgressDialog();
        //sharedPreference.removeFavoriteItem(mcontext, curent_productItem);
        if (deductMoneyWalletResponse.getStatus().equalsIgnoreCase("1")) {
            final Dialog dialog = new Dialog(mcontext);
            dialog.setContentView(R.layout.success_transation_data);
            dialog.setTitle("Detail");
            dialog.setCancelable(false);
            if (deductMoneyWalletResponse.getStatus()!= null) {
                ((TextView) dialog.findViewById(R.id.txt_status)).setText("Success");
            }
            if (deductMoneyWalletResponse.getClientNewBalance() != null) {
                ((TextView) dialog.findViewById(R.id.txt_balance)).setText(deductMoneyWalletResponse.getClientNewBalance());
            }
            if (deductMoneyWalletResponse.getClientId() != null) {
                ((TextView) dialog.findViewById(R.id.txt_clientId)).setText(deductMoneyWalletResponse.getClientId());
            }
            if (deductMoneyWalletResponse.getClientMobile() != null) {
                ((TextView) dialog.findViewById(R.id.txt_clientmobile)).setText(deductMoneyWalletResponse.getClientMobile());
            }
            if (deductMoneyWalletResponse.getClientTransactionDate() != null) {
                ((TextView) dialog.findViewById(R.id.txt_transactiondate)).setText(deductMoneyWalletResponse.getClientTransactionDate());
            }
            Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
            // if button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    MobicashIntentService.startActionSendFoodRequest(mcontext,
                            getaddToCartRequest(String.valueOf(total_amount)),
                            location,latitude,longitude,completeAddress);


                    dialog.dismiss();

                }
            });

            dialog.show();
        } else

        {
         // showSuccessDialog("Transaction Fail");
        }
    }
    private void showSuccessDialog(final String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext);
        alertDialogBuilder
                .setTitle("Success")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                        finish();
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                   }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addcart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setVisible(true);
        if (id == R.id.action_favourte) {
            startActivity(IntentFactory.createFoodStatusActivity(this));
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}


