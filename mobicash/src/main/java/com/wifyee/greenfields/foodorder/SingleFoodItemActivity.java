package com.wifyee.greenfields.foodorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.SharedPrefence.SharedPreference;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;

import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;

import com.wifyee.greenfields.models.requests.DeductMoneyWallet;
import com.wifyee.greenfields.models.response.FailureResponse;

import com.wifyee.greenfields.services.MobicashIntentService;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SingleFoodItemActivity extends AppCompatActivity {
    TextView FoodName, FoodPrice, FoodDes, FoodQty;
    ImageView FoodImg;
    Context mContext;
    public ProgressDialog progressDialog = null;
    String foodimg;
    Button btn_buyNow;
    Toolbar mToolbar;
    ImageButton back;
    String food_id;
   public static String k="0";
    private Double gst = 0.00, total_amount = 0.00;
    private int paymentSelectedIndex = 0;
    SharedPreference sharedPreference ;
    private ArrayList<Items> itemsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        sharedPreference = new SharedPreference(mContext);
        setContentView(R.layout.activity_single_food_item);
        btn_buyNow = (Button) findViewById(R.id.btn_buy);
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        back = (ImageButton) findViewById(R.id.toolbar_back);
        FoodName = (TextView) findViewById(R.id.tv_productname);
        FoodPrice = (TextView) findViewById(R.id.tv_price);
        FoodDes = (TextView) findViewById(R.id.tv_descprition);
        FoodQty = (TextView) findViewById(R.id.tv_quantity);
        FoodImg = (ImageView) findViewById(R.id.imag_food);

        Intent intent = getIntent();

        FoodName.setText(intent.getStringExtra("food_name"));
        k=intent.getStringExtra("food_price");
       /* double h = (gst * Double.parseDouble(intent.getStringExtra("food_price"))) / 100;
        total_amount = Double.parseDouble(intent.getStringExtra("food_price")) + h;
        FoodPrice.setText(String.valueOf(total_amount));*/
        FoodDes.setText(intent.getStringExtra("food_desc"));
        FoodQty.setText(intent.getStringExtra("food_qty"));
        food_id = intent.getStringExtra("food_id");
        // FoodImg.setText(intent.getStringExtra(""));

        foodimg = intent.getStringExtra("food_image");
        Picasso.with(mContext).load(NetworkConstant.MOBICASH_BASE_URL_TESTING + "/uploads/food/" + intent.getStringExtra("food_image")).into(FoodImg);
        MobicashIntentService.startActionGstONFoodItem(mContext);

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
        btn_buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog();

            }
        });
    }

    private void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelProgressDialog();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(gstOnFoodItem);

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
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  /*  double h = (gst * Double.parseDouble(FoodPrice.getText().toString())) / 100;
                    total_amount = Double.parseDouble(FoodPrice.getText().toString()) + h;*/
                //    Toast.makeText(getApplicationContext(), String.valueOf(total_amount), Toast.LENGTH_SHORT).show();

                if (paymentSelectedIndex == 0) {
                  //  showProgressDialog();
                    MobicashIntentService.startActionDeductMoneyWallet(mContext, getpaymetbywallet(String.valueOf(total_amount)));
                } else if ((paymentSelectedIndex == 1)) {

                    Intent paymentIntent = IntentFactory.createPayuBaseActivity(mContext);
                    paymentIntent.putExtra(PaymentConstants.STRING_EXTRA, String.valueOf(total_amount));
                    paymentIntent.putExtra("foodCart", "foodamount");
                    paymentIntent.putExtra(PaymentConstants.FOODORDER_EXTRA, getaddToCartRequest(String.valueOf(total_amount)));
                    startActivity(paymentIntent);

                }   else if ((paymentSelectedIndex == 2)) {
                    MobicashIntentService.startActionSendFoodRequest(mContext, getaddToCartRequest(String.valueOf(total_amount)),
                            "","","","","","","","");
                }
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private DeductMoneyWallet getpaymetbywallet(String amount) {
        showProgressDialog();
        DeductMoneyWallet foodPaymentbywallet = new DeductMoneyWallet();
        foodPaymentbywallet.clientMobile = LocalPreferenceUtility.getUserMobileNumber(mContext);
        foodPaymentbywallet.pincode = LocalPreferenceUtility.getUserPassCode(mContext);
        foodPaymentbywallet.amount = amount;
        foodPaymentbywallet.description = "foodOrder";
        StringBuilder sb = new StringBuilder(foodPaymentbywallet.clientMobile);
        sb.append(foodPaymentbywallet.amount);
        sb.append(MobicashUtils.md5(foodPaymentbywallet.pincode));
        try {
            foodPaymentbywallet.hash = MobicashUtils.getSha1(sb.toString());
            ;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        ;

        return foodPaymentbywallet;
    }

    private CartFoodOderRequest getaddToCartRequest(String finalprice) {
        CartFoodOderRequest foodOrderRequest = new CartFoodOderRequest();
        itemsArrayList.clear();

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyymmddhhmmss");
        String datetime = formatter.format(today);
        String code_format = format.format(today);
        foodOrderRequest.orderId = "C_" + LocalPreferenceUtility.getMerchantId(mContext) + code_format;
        Log.e("id",food_id);
        Log.e("quantity",FoodQty.getText().toString());
        Log.e("price",finalprice);

        itemsArrayList.add(new Items(food_id, FoodQty.getText().toString(),finalprice));
        foodOrderRequest.setItems(itemsArrayList);
        if (paymentSelectedIndex==0) {
            foodOrderRequest.payment_mode="wallet";
        } else if (paymentSelectedIndex==1)  {
            foodOrderRequest.payment_mode="payu";
        }
        else if (paymentSelectedIndex==2) {
            foodOrderRequest.payment_mode="cod";
        }

        foodOrderRequest.userId = LocalPreferenceUtility.getUserCode(mContext);
        foodOrderRequest.merchantId = LocalPreferenceUtility.getMerchantId(mContext);
        foodOrderRequest.orderDateTime = datetime;
        foodOrderRequest.orderPrice = finalprice;
        foodOrderRequest.userType = "client";
        return foodOrderRequest;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(gstOnFoodItem, new IntentFilter(action));
        }
     // showProgressDialog();
        //MobicashIntentService.startActionAllFoodOrderList(mContext);
    }

    protected void showProgressDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.setIcon(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private String[] broadCastReceiverActionList = {
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
            if (actionOperatorList.equals(NetworkConstant.STATUS_GSTONFOOD_LIST_SUCCESS)) {
                GstOnFoodItemResponse gstOnFoodItemResponse = (GstOnFoodItemResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (gstOnFoodItemResponse != null && gstOnFoodItemResponse.gst != null) {
                    cancelProgressDialog();
                    gst = Double.valueOf(gstOnFoodItemResponse.gst);
                    double h = (gst * Double.parseDouble(k)) / 100;
                    total_amount = Double.parseDouble(k) + h;
                    FoodPrice.setText(String.valueOf(total_amount));
                }
            } else if (actionOperatorList.equals(NetworkConstant.STATUS_GSTONFOOD_LIST_FAIL)) {
                gst = 0.00;
                cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    cancelProgressDialog();
                   // showErrorDialog(failureResponse.msg);
                }
                showErrorDialog(String.valueOf(R.string.error_message));
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
                    //showErrorDialog(failureResponse.msg);
                    showErrorDialog(String.valueOf(R.string.error_message));
                }
                showErrorDialog(String.valueOf(R.string.error_message));
            }
            if (actionOperatorList.equals(NetworkConstant.STATUS_DEDUCTMONEY_WALLET_SUCCESS)) {
                DeductMoneyWalletResponse deductMoneyWalletResponse = (DeductMoneyWalletResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (deductMoneyWalletResponse != null && deductMoneyWalletResponse.status.equalsIgnoreCase("1")) {
                    onSuccess(deductMoneyWalletResponse);

                }

            } else if (actionOperatorList.equals(NetworkConstant.STATUS_DEDUCT_MONEY_WALLET_FAIL)) {

                cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    cancelProgressDialog();
                   // showErrorDialog(failureResponse.msg);
                    showErrorDialog(String.valueOf(R.string.error_message));
                }
                showErrorDialog(String.valueOf(R.string.error_message));
            }
        }
     };
    public void showErrorDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
    private void onSuccess(DeductMoneyWalletResponse deductMoneyWalletResponse) {

        SharedPrefenceList curent_productItem;
        curent_productItem = new SharedPrefenceList();
        curent_productItem.setName(FoodName.getText().toString());
        curent_productItem.setDescription(FoodDes.getText().toString());
        curent_productItem.setFoodImage(foodimg);
        curent_productItem.setPrice(FoodPrice.getText().toString());
        curent_productItem.setQuantiy(FoodQty.getText().toString());
        sharedPreference.removeFavoriteItem(mContext, curent_productItem);
        if (deductMoneyWalletResponse.getStatus().equalsIgnoreCase("1")) {
            final Dialog dialog = new Dialog(mContext);
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
                    MobicashIntentService.startActionSendFoodRequest(mContext, getaddToCartRequest(String.valueOf(total_amount)),
                            "","","","","","","","");


                    dialog.dismiss();

                }
            });

            dialog.show();
        } else

        {
            showSuccessDialog("Transaction Fail");
        }
    }
    private void showSuccessDialog(final String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
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
}





