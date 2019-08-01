package com.wifyee.greenfields.dairyorder;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.activity.ApplyCoupons;
import com.wifyee.greenfields.activity.BaseActivity;
import com.wifyee.greenfields.activity.MobicashDashBoardActivity;
import com.wifyee.greenfields.activity.OrderItemDetails;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.models.requests.GetClientProfileRequest;
import com.wifyee.greenfields.models.response.GetClientProfileResponse;
import com.wifyee.greenfields.models.response.GetProfile;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class OrderSummaryDetails extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.edit_address)
    TextView edtAddress;

    @BindView(R.id.place_order)
    Button btnPlaceOrder;

    @BindView(R.id.payment_group)
    RadioGroup paymentGroup;

    @BindView(R.id.rb_cod)
    RadioButton paymentCod;

    @BindView(R.id.rb_wallet)
    RadioButton paymentWallet;

    @BindView(R.id.rb_netbanking)
    RadioButton paymentNetBanking;

    @BindView(R.id.sub_total_price)
    TextView tvSubTotal;

    @BindView(R.id.total_price)
    TextView tvTotal;

    @BindView(R.id.item_total)
    TextView tvItemTotal;

    @BindView(R.id.tv_address_value)
    TextView tvAddressValue;

    @BindView(R.id.edit_cart)
    TextView tvEditCart;

    @BindView(R.id.delivery_value)
    TextView tvNoOfDelivery;

    @BindView(R.id.delivery_fee_price)
    TextView tvDeliveryFee;

    @BindView(R.id.txt_subscribe_here)
    TextView tvSubscribe_here;

    @BindView(R.id.til_date_from)
    TextInputLayout tilDateFrom;

    @BindView(R.id.til_date_to)
    TextInputLayout tilDateTo;

    @BindView(R.id.txt_per_day)
    TextView tvPerDay;

    @BindView(R.id.txt_note)
    TextView txtNote;

    @BindView(R.id.txt_apply_coupon)
    TextView tvApplyCoupon;

    @BindView(R.id.txt_summary)
    TextView tvSummary;

    @BindView(R.id.txt_delivery)
    TextView tvDeliver;

    @BindView(R.id.txt_total)
    TextView txtTotal;

    @BindView(R.id.ic_subscribe_arrow)
    ImageView icSubscribeArrow;

    @BindView(R.id.card_view_coupon)
    CardView cardViewCoupon;

    private ArrayList<PlaceOrderData> orderItem ;
    private int paymentSelectedIndex;
    private Context mContext = null;
    private String totalAmount,location,latitude,longitude,complete_add,discount_amt;
    private int totalItem,deliveryFee;

    public static String fullname,mobile_no,alternate_no,city , locality , flat_no, pincode, state;
    public static boolean addressChange = false;
    TextView name,address,mobileNo;
    Button addressBtn;
    LinearLayout ll_subscribe;
    RelativeLayout subscribe;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText dateFrom,dateTo;
    private Spinner perDayLitreSpinner;
    CardView card_view_payment;
    String perDay="",dtFrom="",dtTo="", claimType,voucherId,voucherNo,flag,tuvId;
    /**
     * List of actions supported.
     */

    private String[] broadCastReceiverActionList = {
            DairyNetworkConstant.DAIRY_ADD_ORDER_STATUS_FAILURE,
            DairyNetworkConstant.DAIRY_ADD_ORDER_STATUS_SUCCESS,
            NetworkConstant.STATUS_GET_CLIENT_PROFILE_SUCCESS,
            NetworkConstant.STATUS_GET_CLIENT_PROFILE_FAIL
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary_details);
        ButterKnife.bind(this);
        mContext = this;
        name = findViewById(R.id.name);
        address = findViewById(R.id.address_tv);
        mobileNo = findViewById(R.id.mobile_no);
        addressBtn = findViewById(R.id.btn_address);
        subscribe = findViewById(R.id.subscription_layout);
        ll_subscribe = findViewById(R.id.ll_subscribe);
        dateFrom = findViewById(R.id.date_from);
        dateTo = findViewById(R.id.date_to);
        perDayLitreSpinner = findViewById(R.id.per_day_litre);
        card_view_payment = findViewById(R.id.card_view_payment);

        /*String amount = LocalPreferenceUtility.getWalletBalance(this);
        Log.e("amt","amt "+amount);*/

        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        toolBarTitle.setTypeface(Fonts.getSemiBold(this));
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.secondaryPrimary), PorterDuff.Mode.SRC_ATOP);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rotationAngle = 0;
                if (ll_subscribe.getVisibility()==View.GONE){
                    ll_subscribe.setVisibility(View.VISIBLE);
                    rotationAngle = rotationAngle == 0 ? 180 : 0;  //toggle
                    icSubscribeArrow.animate().rotation(rotationAngle).setDuration(500).start();
                }else {
                    ll_subscribe.setVisibility(View.GONE);
                    icSubscribeArrow.animate().rotation(rotationAngle).setDuration(500).start();
                }
            }
        });

        cardViewCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderSummaryDetails.this, ApplyCoupons.class);
                startActivity(intent);
            }
        });


        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(dateFrom);
                dateFrom.setFocusable(true);
            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(dateTo);
                dateTo.setFocusable(true);
            }
        });

        addressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderSummaryDetails.this, AddressList.class);
                startActivity(intent);
            }
        });

        tvSubscribe_here.setTypeface(Fonts.getRegular(this));
        dateFrom.setTypeface(Fonts.getSemiBold(this));
        dateTo.setTypeface(Fonts.getSemiBold(this));
        tilDateFrom.setTypeface(Fonts.getRegular(this));
        tilDateTo.setTypeface(Fonts.getRegular(this));
        tvPerDay.setTypeface(Fonts.getRegular(this));
        txtNote.setTypeface(Fonts.getRegular(this));
        tvApplyCoupon.setTypeface(Fonts.getSemiBold(this));
        tvSummary.setTypeface(Fonts.getRegular(this));
        tvItemTotal.setTypeface(Fonts.getRegular(this));
        tvSubTotal.setTypeface(Fonts.getRegular(this));
        tvDeliver.setTypeface(Fonts.getRegular(this));
        tvDeliveryFee.setTypeface(Fonts.getRegular(this));
        txtTotal.setTypeface(Fonts.getSemiBold(this));
        tvTotal.setTypeface(Fonts.getSemiBold(this));
        paymentCod.setTypeface(Fonts.getRegular(this));
        paymentWallet.setTypeface(Fonts.getRegular(this));
        paymentNetBanking.setTypeface(Fonts.getRegular(this));
        btnPlaceOrder.setTypeface(Fonts.getSemiBold(this));

        orderItem = getIntent().getParcelableArrayListExtra("data");
        //Log.e("orderItem",orderItem.toString());
        //Log.e("orderItem",orderItem.toArray().toString());
        totalAmount = getIntent().getStringExtra("amount");
        location = getIntent().getStringExtra("location");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        complete_add = getIntent().getStringExtra("complete_add");
        discount_amt = getIntent().getStringExtra("discount_amt");
        claimType = getIntent().getStringExtra("claim_type");
        voucherId = getIntent().getStringExtra("voucher_id");
        voucherNo = getIntent().getStringExtra("voucher_no");
        flag = getIntent().getStringExtra("flag");
        tuvId = getIntent().getStringExtra("tuv_id");

        if (flag!=null){
            card_view_payment.setVisibility(View.GONE);
        }else {
            card_view_payment.setVisibility(View.VISIBLE);
        }

        int size = orderItem.size();
       /*for(int i=0;i<size;i++){
            totalItem = totalItem +Integer.parseInt(orderItem.get(i).getQuantity());
        }*/

        tvItemTotal.setText("Item Total ("+size+" Item)");
        //tvNoOfDelivery.setText(String.valueOf(totalItem));

        float amt = Float.parseFloat(totalAmount);
        int itemTotalAmount = Math.round(amt);
        if(itemTotalAmount<200){
            deliveryFee = 50;
        }else if(itemTotalAmount<500){
            deliveryFee = 20;
        }else {
            deliveryFee = 0;
        }

        int total = itemTotalAmount + deliveryFee;
        totalAmount = String.valueOf(total);

        if(deliveryFee==0){
            tvDeliveryFee.setText("Free");
            tvDeliveryFee.setTextColor(getResources().getColor(R.color.secondaryPrimary));
        }else {
            tvDeliveryFee.setText("₹"+deliveryFee);
            tvDeliveryFee.setTextColor(getResources().getColor(R.color.black));
        }
        tvTotal.setText("₹"+totalAmount);
        tvSubTotal.setText("₹"+itemTotalAmount);

        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.rb_cod) {
                    paymentSelectedIndex = 0;
                } else if(checkedId == R.id.rb_wallet) {
                    paymentSelectedIndex = 1;
                } else {
                    paymentSelectedIndex = 2;
                }
            }
        });


        tvEditCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

        edtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dtFrom = dateFrom.getText().toString();
                dtTo = dateTo.getText().toString();
                perDay = perDayLitreSpinner.getSelectedItem().toString().replace(" Litre","");
                if(perDay.equalsIgnoreCase("Please select milk quantity")){
                    perDay="";
                }

                if (flag!=null){
                    if (flag.equalsIgnoreCase("voucher")){
                        Snackbar.make(view,"you can order",Snackbar.LENGTH_SHORT).show();

                    }
                }else {
                    if (paymentSelectedIndex == 1) {
                        double amount = Double.parseDouble(totalAmount);
                        int walletAmount = !LocalPreferenceUtility.getWalletBalance(OrderSummaryDetails.this).equals("") ? Integer.parseInt(LocalPreferenceUtility.getWalletBalance(OrderSummaryDetails.this)) : 0;
                        //int walletAmount = Integer.parseInt(LocalPreferenceUtility.getWalletBalance(OrderSummaryDetails.this));
                        // if (!LocalPreferenceUtility.getPinCode(OrderSummaryActivity.this).isEmpty()) {
                        if (walletAmount > amount) {
                            showProgressDialog();
                            DairyProductIntentService.startActionAddOrder(OrderSummaryDetails.this, orderItem,
                                    totalAmount, DairyNetworkConstant.PAYMENT_MODE_WALLET,
                                    "", location, latitude, longitude, complete_add, discount_amt, dtFrom, dtTo, perDay,
                                    claimType, voucherId, voucherNo);
                        } else {
                            //add amount and then place order.
                            Intent i = new Intent(OrderSummaryDetails.this, DairyOrderSummaryWebViewActivity.class);
                            i.putExtra("pay_amount", String.valueOf(amount - walletAmount));
                            i.putExtra("paymode", "wallet");
                            i.putParcelableArrayListExtra("data", orderItem);
                            i.putExtra("totalAmount", totalAmount);
                            i.putExtra("location", location);
                            i.putExtra("latitude", latitude);
                            i.putExtra("longitude", longitude);
                            i.putExtra("complete_add", complete_add);
                            i.putExtra("discount_amt", discount_amt);
                            i.putExtra("date_from", dtFrom);
                            i.putExtra("date_to", dtTo);
                            i.putExtra("per_day", perDay);
                            i.putExtra("claim_type", claimType);
                            i.putExtra("voucher_id", voucherId);
                            i.putExtra("voucher_no", voucherNo);
                            startActivity(i);
                            finish();
                        }
                    } else if (paymentSelectedIndex == 2) {
                        if (!LocalPreferenceUtility.getPinCode(OrderSummaryDetails.this).isEmpty()) {
                            Intent i = new Intent(OrderSummaryDetails.this, DairyOrderSummaryWebViewActivity.class);
                            i.putExtra("pay_amount", totalAmount);
                            i.putExtra("paymode", DairyNetworkConstant.PAYMENT_MODE_ONLINE);
                            i.putParcelableArrayListExtra("data", orderItem);
                            i.putExtra("location", location);
                            i.putExtra("latitude", latitude);
                            i.putExtra("longitude", longitude);
                            i.putExtra("complete_add", complete_add);
                            i.putExtra("discount_amt", discount_amt);
                            i.putExtra("date_from", dtFrom);
                            i.putExtra("date_to", dtTo);
                            i.putExtra("per_day", perDay);
                            i.putExtra("claim_type", claimType);
                            i.putExtra("voucher_id", voucherId);
                            i.putExtra("voucher_no", voucherNo);
                            startActivity(i);
                            finish();
                        } else {
                            showErrorDialog("Please update your profile before proceed!");
                        }
                    } else if (paymentSelectedIndex == 0) {
                        if (!LocalPreferenceUtility.getPinCode(OrderSummaryDetails.this).isEmpty()) {
                            showProgressDialog();
                            DairyProductIntentService.startActionAddOrder(OrderSummaryDetails.this, orderItem,
                                    totalAmount, DairyNetworkConstant.PAYMENT_MODE_COD,
                                    "", location, latitude, longitude, complete_add, discount_amt, dtFrom, dtTo, perDay,
                                    claimType, voucherId, voucherNo);
                        } else {
                            showErrorDialog("Please update your profile before proceed!");
                        }
                    }
                }
            }
        });

    }

    private void showEditDialog() {

        final View view = getLayoutInflater().inflate(R.layout.edit_text_dialog_layout, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Address");
        alertDialog.setCancelable(false);

        final EditText etComments = (EditText) view.findViewById(R.id.etComments);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvAddressValue.setText(etComments.getText().toString());
                alertDialog.dismiss();
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getMerchantAddOrderDairyItemReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(getMerchantAddOrderDairyItemReceiver, new IntentFilter(action));
        }
        /**
         * send request for client profile.
         */
        GetClientProfileRequest mGetClientProfileRequest = new GetClientProfileRequest();
        mGetClientProfileRequest.clientId = LocalPreferenceUtility.getUserCode(mContext);
        mGetClientProfileRequest.pincode = LocalPreferenceUtility.getUserPassCode(mContext);
        try {
            mGetClientProfileRequest.hash = MobicashUtils.getSha1(mGetClientProfileRequest.clientId + MobicashUtils.md5(mGetClientProfileRequest.pincode));
        } catch (NoSuchAlgorithmException e) {
            Timber.e("NoSuchAlgorithmException Occurred . Message : " + e.getMessage());
        }

        MobicashIntentService.startActionGetClientProfile(mContext, mGetClientProfileRequest);

        /*if(addressChange){
            name.setVisibility(View.VISIBLE);
            address.setVisibility(View.VISIBLE);
            mobileNo.setVisibility(View.VISIBLE);
            name.setText(fullname);
            address.setText(flat_no+", "+locality+", "+city+", "+state+" - "+pincode);
            mobileNo.setText(mobile_no);
        }else {
            fillList();
        }*/
    }

    public void fillList() {
        SQLController controller=new SQLController(getApplicationContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT * from address order by id desc LIMIT 1";

        Cursor data = controller.retrieve(query);
        if(data.getCount()>0){
            name.setVisibility(View.VISIBLE);
            address.setVisibility(View.VISIBLE);
            mobileNo.setVisibility(View.VISIBLE);
            data.moveToFirst();
            do{
                fullname =  data.getString(data.getColumnIndex("name"));
                mobile_no =  data.getString(data.getColumnIndex("mobile_no"));
                alternate_no =  data.getString(data.getColumnIndex("alternate_no"));
                city = data.getString(data.getColumnIndex("city"));
                locality = data.getString(data.getColumnIndex("locality"));
                flat_no = data.getString(data.getColumnIndex("flat_no"));
                pincode = data.getString(data.getColumnIndex("pincode"));
                state = data.getString(data.getColumnIndex("state"));

                name.setText(fullname);
                address.setText(flat_no+", "+locality+" ,"+city+", "+state+" - "+pincode);
                mobileNo.setText(mobile_no);

            }while (data.moveToNext());
        }else {
            name.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            mobileNo.setVisibility(View.GONE);
        }

        data.close();
        controller.close();
    }

    /**
     * Handling broadcast event for user Signup .
     */
    private BroadcastReceiver getMerchantAddOrderDairyItemReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            cancelProgressDialog();
            try {
                if (action.equals(DairyNetworkConstant.DAIRY_ADD_ORDER_STATUS_SUCCESS)) {
                    cancelProgressDialog();
                    deleteCart();
                    showSuccessDialog();

                }else if(action.equals(DairyNetworkConstant.DAIRY_ADD_ORDER_STATUS_FAILURE)){
                    showErrorDialog("something went wrong.Please try again");

                }else if(action.equals(NetworkConstant.STATUS_GET_CLIENT_PROFILE_SUCCESS)) {
                    GetClientProfileResponse mGetClientProfileResponse = (GetClientProfileResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    Timber.d("STATUS_GET_CLIENT_PROFILE_SUCCESS = > GetClientProfileResponse  ==>" + new Gson().toJson(mGetClientProfileResponse));
                    if (mGetClientProfileResponse != null && mGetClientProfileResponse.clientProfile != null && mGetClientProfileResponse.clientProfile.mobicashClientProfile != null) {
                        GetProfile mGetProfile = mGetClientProfileResponse.clientProfile.mobicashClientProfile;
                        if (mGetProfile.clientZipcode != null) {
                            LocalPreferenceUtility.putPinCode(OrderSummaryDetails.this,mGetProfile.clientZipcode);
                        }
                    } else {
                        Timber.d("STATUS_GET_CLIENT_PROFILE_SUCCESS = > GetClientProfileResponse is null");
                    }
                }
            } catch (Exception e) {
                Timber.e(" Exception caught in getMerchantListDairyReceiver " + e.getMessage());
            }
        }
    };

    /**
     * show Transaction progress dialog
     */
    protected void showSuccessDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Your order has been successfully placed, You soon will get a message regarding order status. Thank you for choosing Wifyee.");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                arg0.dismiss();
                startActivity(new Intent(OrderSummaryDetails.this, MobicashDashBoardActivity.class));
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    private void deleteCart(){
        SQLController controller=new SQLController(OrderSummaryDetails.this);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "DELETE from cart_item";
        String result = controller.fireQuery(query);
        if(result.equals("Done")){
            Log.w("delete","Delete all successfully");
        }else {
            Log.w("result",result);
        }
        controller.close();
    }

    private void setDate(final EditText editText){

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(OrderSummaryDetails.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();
        mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

    }
}
