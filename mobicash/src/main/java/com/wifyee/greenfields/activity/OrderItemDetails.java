package com.wifyee.greenfields.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.DateConvert;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.adapters.OrderItemAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.dairyorder.DairyNetworkConstant;
import com.wifyee.greenfields.dairyorder.DairyOrderSummaryWebViewActivity;
import com.wifyee.greenfields.dairyorder.JSONBuilder;
import com.wifyee.greenfields.dairyorder.OrderSummaryDetails;
import com.wifyee.greenfields.foodorder.AddToCartActivity;
import com.wifyee.greenfields.foodorder.CartFoodOrderResponse;
import com.wifyee.greenfields.foodorder.DeductMoneyWalletResponse;
import com.wifyee.greenfields.foodorder.GstOnFoodItemResponse;
import com.wifyee.greenfields.mapper.ModelMapper;
import com.wifyee.greenfields.models.OrderItemModel;
import com.wifyee.greenfields.models.requests.DeductMoneyWallet;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.PayUPaymentGatewayResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import timber.log.Timber;

import static com.wifyee.greenfields.activity.WebViewActivity.ACTION_STATUS_VALUE;

public class OrderItemDetails extends AppCompatActivity {

    private OrderItemAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<OrderItemModel> listArray = new ArrayList<>();
    OrderItemModel[] order;
    private ProgressDialog progressDialog;
    Toolbar mToolbar;
    String orderId,merchantId,taskId,merchantType,paymentMode,orderOn;
    ImageView img1,img2,img3,img4,img5;
    TextView txt1,txt2,txt3,txt4,txt5,paid,subTotal,deliveryFee,total,itemCount,totalamount,totalDiscountAmt,txtClaimText;
    View view1,view2,view3,view4;
    ImageView imageView;
    private LinearLayout llPaymentDetails;
    private RelativeLayout rlPlaceOrder;
    private CardView cardViewDiscount,cardViewCoupon,cardViewPayment;
    public static boolean isVoucherClaim;
    public static String voucherId="",voucherNo="",claimType="",voucherName="",voucherDiscAmt="";
    private int paymentSelectedIndex = 0;
    private RadioGroup paymentGroup;
    private Context mcontext;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_details);

        recyclerView = findViewById(R.id.recyclerView);
        mToolbar = findViewById(R.id.toolbar);
        TextView title = mToolbar.findViewById(R.id.toolbar_title);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txt4 = findViewById(R.id.txt4);
        txt5 = findViewById(R.id.txt5);
        view1 = findViewById(R.id.view_1);
        view2 = findViewById(R.id.view_2);
        view3 = findViewById(R.id.view_3);
        view4 = findViewById(R.id.view_4);
        imageView = findViewById(R.id.image_view);
        TextView txt_bill_details = findViewById(R.id.txt_bill_details);
        TextView txt_item_total = findViewById(R.id.txt_item_total);
        TextView txt_delivery = findViewById(R.id.txt_delivery);
        TextView txt_total = findViewById(R.id.txt_total);
        TextView txt_taxes = findViewById(R.id.txt_taxes);
        paid = findViewById(R.id.paid);
        subTotal = findViewById(R.id.sub_total_price);
        deliveryFee = findViewById(R.id.delivery_fee_price);
        total = findViewById(R.id.total_price);
        llPaymentDetails = findViewById(R.id.payment_ll);
        cardViewDiscount = findViewById(R.id.card_view_discount);
        cardViewCoupon = findViewById(R.id.card_view_coupon);
        cardViewPayment = findViewById(R.id.card_view_payment);
        itemCount = findViewById(R.id.item_count);
        totalamount = findViewById(R.id.tv_totalamount);
        rlPlaceOrder = findViewById(R.id.rl_place_order);
        TextView txtPlaceOrder = findViewById(R.id.txt_place_order);
        TextView txtTotalDiscountAmt = findViewById(R.id.txt_total_discount_amt);
        totalDiscountAmt = findViewById(R.id.total_discount_amt);
        txtClaimText = findViewById(R.id.txt);
        TextView claimHere = findViewById(R.id.claim_here);
        TextView txtPayment = findViewById(R.id.txt_payment);
        final RadioButton paymentCod = findViewById(R.id.rb_cod);
        RadioButton paymentWallet = findViewById(R.id.rb_wallet);
        RadioButton paymentNetBanking = findViewById(R.id.rb_netbanking);
        paymentGroup = findViewById(R.id.payment_group);

        mcontext = this;

        orderId = getIntent().getStringExtra("order_id");
        merchantId = getIntent().getStringExtra("merchantId");
        taskId = getIntent().getStringExtra("taskId");
        merchantType = getIntent().getStringExtra("merchantType");
        orderOn = getIntent().getStringExtra("order_on");

        title.setTypeface(Fonts.getSemiBold(this));
        title.setText(orderId);

        txt1.setTypeface(Fonts.getRegular(this));
        txt2.setTypeface(Fonts.getRegular(this));
        txt3.setTypeface(Fonts.getRegular(this));
        txt4.setTypeface(Fonts.getRegular(this));
        txt5.setTypeface(Fonts.getRegular(this));
        txt_bill_details.setTypeface(Fonts.getRegular(this));
        txt_item_total.setTypeface(Fonts.getRegular(this));
        txt_delivery.setTypeface(Fonts.getRegular(this));
        txt_total.setTypeface(Fonts.getSemiBold(this));
        paid.setTypeface(Fonts.getRegular(this));
        subTotal.setTypeface(Fonts.getRegular(this));
        deliveryFee.setTypeface(Fonts.getRegular(this));
        txtTotalDiscountAmt.setTypeface(Fonts.getRegular(this));
        totalDiscountAmt.setTypeface(Fonts.getSemiBold(this));
        txtClaimText.setTypeface(Fonts.getRegular(this));
        claimHere.setTypeface(Fonts.getSemiBold(this));
        txt_taxes.setTypeface(Fonts.getRegular(this));
        total.setTypeface(Fonts.getBold(this));
        totalamount.setTypeface(Fonts.getBold(this));
        itemCount.setTypeface(Fonts.getRegular(this));
        txtPlaceOrder.setTypeface(Fonts.getSemiBold(this));
        txtPayment.setTypeface(Fonts.getRegular(this));
        paymentWallet.setTypeface(Fonts.getRegular(this));
        paymentNetBanking.setTypeface(Fonts.getRegular(this));

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
        int walletAmount = !LocalPreferenceUtility.getWalletBalance(mcontext).equals("")
                ?Integer.parseInt(LocalPreferenceUtility.getWalletBalance(mcontext)) : 0;
        paymentWallet.setText("Wallet (₹"+walletAmount+")");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new OrderItemAdapter(this, listArray);
        recyclerView.setAdapter(adapter);
        dataLoad();

        claimHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderItemDetails.this, DiscountClaim.class);
                intent.putExtra("flag","order_item");
                intent.putExtra("amount",totalDiscountAmt.getText().toString());
                startActivity(intent);
            }
        });

        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_cod) {
                    paymentSelectedIndex = 0;
                } else if(checkedId == R.id.rb_wallet) {
                    paymentSelectedIndex = 1;
                } else {
                    paymentSelectedIndex = 2;
                }
            }
        });

        rlPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),""+total_amount,Toast.LENGTH_SHORT).show();
                if(!total.getText().toString().replace("₹","").equals("0")) {
                    if(cardViewDiscount.getVisibility() == View.VISIBLE && isVoucherClaim){
                        placeOrder(view);
                    }else if(cardViewDiscount.getVisibility() == View.GONE){
                        placeOrder(view);
                    }else {
                        Snackbar.make(view,"Please claim the discount amount first.",Snackbar.LENGTH_LONG).show();
                    }
                }else {
                    //Snackbar.make(view,"Look like your cart is empty",Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        if(isVoucherClaim){
            if (claimType.equals("1")){
                txtClaimText.setText("Cashback amount will be added in your wallet within 24 hours");
            }else if (claimType.equals("2")){
                String second = "<font color='#1b7836'>"+voucherName+"</font>";
                String fourth = "<font color='#1b7836'>₹"+voucherDiscAmt+"</font>";
                txtClaimText.setText(Html.fromHtml("You have selected "+second+" voucher of "+fourth+""));
            }
        }else {
            txtClaimText.setText("");
        }
        super.onResume();
    }

    private void dataLoad(){
        showProgressDialog();
        final JSONObject jsonObject = new JSONObject();
        String url;
        switch (merchantType) {
            case "6":
                url = NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.FOOD_ORDER_ITEM;
                break;
            case "7":
                url = NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.MEDICINE_ORDER_ITEM;
                //cardViewCoupon.setVisibility(View.VISIBLE);
                //cardViewDiscount.setVisibility(View.VISIBLE);
                break;
            default:
                url = NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.OTHER_ORDER_ITEM;
                break;
        }

        try {
            jsonObject.put(ResponseAttributeConstants.ORDER_ID, orderId);
            jsonObject.put(ResponseAttributeConstants.MERCHANTID, merchantId);
            jsonObject.put(ResponseAttributeConstants.TASK_ID, taskId);
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        //Log.e("ORD_details",jsonObject.toString());
        //Log.e("url",url);
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject object) {
                        Log.e("-ORDER_ITEM-",object.toString());
                        cancelProgressDialog();
                        llPaymentDetails.setVisibility(View.VISIBLE);

                        try {
                            if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                JSONObject jsonObject1 = object.getJSONObject(ResponseAttributeConstants.ORDER_DETAILS);
                                String orderAmt = jsonObject1.getString(ResponseAttributeConstants.ORDER_AMOUNT);
                                /*if (jsonObject1.has(ResponseAttributeConstants.PRESCRIPTION_IMAGE)){
                                    Log.w("in ores","presciption image");
                                    recyclerView.setVisibility(View.GONE);
                                    imageView.setVisibility(View.VISIBLE);
                                    Picasso.with(OrderItemDetails.this)
                                            .load(jsonObject1.getString(ResponseAttributeConstants.PRESCRIPTION_IMAGE))
                                            .into(imageView);
                                }*/
                                if(jsonObject1.has(ResponseAttributeConstants.DELIVERY_AMT)){
                                    deliveryFee.setText("₹"+jsonObject1.getString(ResponseAttributeConstants.DELIVERY_AMT));
                                }/*else {
                                    deliveryFee.setText("₹0");
                                }*/

                                //if (jsonObject1.has(ResponseAttributeConstants.ORDER_STATUS)) {
                                    String orderStatus = jsonObject1.getString(ResponseAttributeConstants.ORDER_STATUS);
                                    String paymentStatus = jsonObject1.getString(ResponseAttributeConstants.PAYMENT_STATUS);
                                    if((orderStatus.equals("1") || orderStatus.equals("2") || orderStatus.equals("3") ||
                                            orderStatus.equals("4")) && paymentStatus.equalsIgnoreCase("Pending")){
                                        if(merchantType.equals("7")){
                                            cardViewCoupon.setVisibility(View.VISIBLE);
                                            cardViewDiscount.setVisibility(View.VISIBLE);
                                            cardViewPayment.setVisibility(View.VISIBLE);
                                            rlPlaceOrder.setVisibility(View.VISIBLE);
                                            String disc_amt = jsonObject1.getString(ResponseAttributeConstants.DISC_AMT);
                                            if(!disc_amt.equals("0.00")){
                                                totalDiscountAmt.setText("₹"+disc_amt);
                                            }else {
                                                cardViewDiscount.setVisibility(View.GONE);
                                            }
                                        }else {
                                            //cardViewCoupon.setVisibility(View.GONE);
                                            //cardViewDiscount.setVisibility(View.GONE);
                                        }
                                        //cardViewPayment.setVisibility(View.VISIBLE);
                                        //rlPlaceOrder.setVisibility(View.VISIBLE);
                                    }
                                //}

                                if(jsonObject1.has(ResponseAttributeConstants.ITEM)) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    imageView.setVisibility(View.GONE);
                                    JSONArray jsonArray = jsonObject1.getJSONArray(ResponseAttributeConstants.ITEM);
                                    double subTotalAmount = 0.0;
                                    int items = 0;
                                    order = new OrderItemModel[jsonArray.length()];
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        order[i] = new OrderItemModel(
                                                jsonObject.getString(ResponseAttributeConstants.ITEMNAME),
                                                jsonObject.getString(ResponseAttributeConstants.ITEMQUANTITY),
                                                jsonObject.getString(ResponseAttributeConstants.ITEMAMOUNT)
                                        );
                                        listArray.add(order[i]);
                                        double amountQuantity = (Integer.parseInt(jsonObject.getString(ResponseAttributeConstants.ITEMQUANTITY)) *
                                                Double.parseDouble(jsonObject.getString(ResponseAttributeConstants.ITEMAMOUNT)));
                                        subTotalAmount = subTotalAmount + amountQuantity;
                                        items = items + (Integer.parseInt(jsonObject.getString(ResponseAttributeConstants.ITEMQUANTITY)));
                                    }

                                    recyclerView.getRecycledViewPool().clear();
                                    adapter.notifyDataSetChanged();

                                    subTotal.setText("₹"+subTotalAmount);
                                    itemCount.setText(""+items+" Items");
                                    totalamount.setText("₹"+orderAmt);
                                    total.setText("₹"+orderAmt);
                                    String paymentMode = jsonObject1.getString(ResponseAttributeConstants.PAY_MODE);
                                    if (!merchantType.equals("7")) {
                                        if (paymentMode.equalsIgnoreCase("cod")) {
                                            paid.setText("Payment Mode Cash");
                                        } else {
                                            paid.setText("Paid Via " + paymentMode.substring(0, 1).toUpperCase() + paymentMode.substring(1));
                                        }
                                    }else if (merchantType.equals("7") &&
                                            jsonObject1.getString(ResponseAttributeConstants.PAYMENT_STATUS).equalsIgnoreCase("Complete")){
                                        paid.setText("Paid Via " + paymentMode.substring(0, 1).toUpperCase() + paymentMode.substring(1));
                                    }
                                }

                                JSONArray array = object.getJSONArray(ResponseAttributeConstants.ORDER_STATUS);
                                for (int j=0; j<array.length();j++){
                                    JSONObject jsonObject = array.getJSONObject(j);
                                    //String status = jsonObject.getString(ResponseAttributeConstants.TASK_STATUS);
                                    String st_date = jsonObject.getString(ResponseAttributeConstants.TASK_STATUS_DATE);
                                    String status_date = DateConvert.getDate(st_date);
                                    if(j==0){
                                        txt1.setText("Ordered on "+status_date);
                                        txt1.setTypeface(Fonts.getSemiBold(OrderItemDetails.this));
                                        txt1.setTextColor(getResources().getColor(R.color.black));
                                    }
                                    else if(j==1){
                                        txt2.setText("Processing on "+status_date);
                                        //Font
                                        txt2.setTypeface(Fonts.getSemiBold(OrderItemDetails.this));
                                        txt1.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        //text color
                                        txt2.setTextColor(getResources().getColor(R.color.black));
                                        txt1.setTextColor(getResources().getColor(R.color.text_color));

                                        view1.setBackgroundColor(getResources().getColor(R.color.tracking_green));
                                        if(array.length()-1 == j){
                                            img2.setImageResource(R.drawable.ic_active);
                                            img2.setBackground(getResources().getDrawable(R.drawable.tracking_oval_yellow));
                                            img2.setPadding(7,7,7,7);
                                        }else {
                                            img2.setBackgroundResource(R.drawable.ic_done);
                                            img2.setBackground(getResources().getDrawable(R.drawable.tracking_oval_green));
                                            img2.setPadding(2, 2, 2, 2);
                                        }
                                    }
                                    else if(j==2){
                                        txt3.setText("Packing on " + status_date);
                                        view2.setBackgroundColor(getResources().getColor(R.color.tracking_green));
                                        //font
                                        txt3.setTypeface(Fonts.getSemiBold(OrderItemDetails.this));
                                        txt1.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        txt2.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        //text color
                                        txt3.setTextColor(getResources().getColor(R.color.black));
                                        txt1.setTextColor(getResources().getColor(R.color.text_color));
                                        txt2.setTextColor(getResources().getColor(R.color.text_color));

                                        if(array.length()-1 == j){
                                            img3.setImageResource(R.drawable.ic_active);
                                            img3.setBackground(getResources().getDrawable(R.drawable.tracking_oval_yellow));
                                            img3.setPadding(7,7,7,7);
                                        }else {
                                            img3.setBackgroundResource(R.drawable.ic_done);
                                            img3.setBackground(getResources().getDrawable(R.drawable.tracking_oval_green));
                                            img3.setPadding(2, 2, 2, 2);
                                        }
                                    }
                                    else if(j==3){
                                        txt4.setText("Shipping on "+status_date);
                                        view3.setBackgroundColor(getResources().getColor(R.color.tracking_green));
                                        //font
                                        txt4.setTypeface(Fonts.getSemiBold(OrderItemDetails.this));
                                        txt1.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        txt2.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        txt3.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        //text color
                                        txt4.setTextColor(getResources().getColor(R.color.black));
                                        txt1.setTextColor(getResources().getColor(R.color.text_color));
                                        txt2.setTextColor(getResources().getColor(R.color.text_color));
                                        txt3.setTextColor(getResources().getColor(R.color.text_color));

                                        if(array.length()-1 == j){
                                            img4.setImageResource(R.drawable.ic_active);
                                            img4.setBackground(getResources().getDrawable(R.drawable.tracking_oval_yellow));
                                            img4.setPadding(7,7,7,7);
                                        }else {
                                            img4.setBackgroundResource(R.drawable.ic_done);
                                            img4.setBackground(getResources().getDrawable(R.drawable.tracking_oval_green));
                                            img4.setPadding(2, 2, 2, 2);
                                        }
                                    }
                                    else if(j==4){
                                        txt5.setText("Delivered on "+status_date);
                                        view4.setBackgroundColor(getResources().getColor(R.color.tracking_green));                                            img5.setBackgroundResource(R.drawable.ic_done);
                                        img5.setBackgroundResource(R.drawable.ic_done);
                                        img5.setPadding(2,2,2,2);
                                        //font
                                        txt5.setTypeface(Fonts.getSemiBold(OrderItemDetails.this));
                                        txt1.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        txt2.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        txt3.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        txt4.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        //text color
                                        txt5.setTextColor(getResources().getColor(R.color.black));
                                        txt1.setTextColor(getResources().getColor(R.color.text_color));
                                        txt2.setTextColor(getResources().getColor(R.color.text_color));
                                        txt3.setTextColor(getResources().getColor(R.color.text_color));
                                        txt4.setTextColor(getResources().getColor(R.color.text_color));

                                        if(array.length()-1 == j){
                                            img5.setBackground(getResources().getDrawable(R.drawable.tracking_oval_yellow));
                                        }else {
                                            img5.setBackground(getResources().getDrawable(R.drawable.tracking_oval_green));
                                        }
                                    }
                                    else if(j==5){
                                        txt5.setText("Delivered on "+status_date);
                                        view4.setBackgroundColor(getResources().getColor(R.color.tracking_green));
                                        img5.setBackgroundResource(R.drawable.ic_done);
                                        img5.setPadding(2, 2, 2, 2);
                                        //font
                                        txt5.setTypeface(Fonts.getSemiBold(OrderItemDetails.this));
                                        txt1.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        txt2.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        txt3.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        txt4.setTypeface(Fonts.getRegular(OrderItemDetails.this));
                                        //text color
                                        txt5.setTextColor(getResources().getColor(R.color.black));
                                        txt1.setTextColor(getResources().getColor(R.color.text_color));
                                        txt2.setTextColor(getResources().getColor(R.color.text_color));
                                        txt3.setTextColor(getResources().getColor(R.color.text_color));
                                        txt4.setTextColor(getResources().getColor(R.color.text_color));
                                        if(array.length()-1 == j){
                                            img5.setBackground(getResources().getDrawable(R.drawable.tracking_oval_yellow));
                                        }else {
                                            img5.setBackground(getResources().getDrawable(R.drawable.tracking_oval_green));
                                        }
                                    }
                                }

                            } else {

                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        cancelProgressDialog();
                        // handle error
                        Timber.e("called onError of User Invoice API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("UserInvoice = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                    }
                });
    }

    private void placeOrder(View view){
        String total_amount = total.getText().toString().replace("₹","");
        String deliverAmt = deliveryFee.getText().toString().replace("₹","");
        if (paymentSelectedIndex == 0) {
            paymentMode = "cod";
            actionAddPostOrder();
        } else if ((paymentSelectedIndex == 1)) {
            paymentMode = "wallet";
            double amount = Double.parseDouble(total_amount);
            int walletAmount = !LocalPreferenceUtility.getWalletBalance(mcontext).equals("")
                    ?Integer.parseInt(LocalPreferenceUtility.getWalletBalance(mcontext)) : 0;
            if(walletAmount >= amount){
                actionAddPostOrder();
            }else {
                Snackbar.make(view,"Amount is exceeding to the wallet amount",Snackbar.LENGTH_LONG).show();
            }
        }
        else if ((paymentSelectedIndex == 2)) {
            //paymentMode = "instamojo";
            //if (!LocalPreferenceUtility.getPinCode(OrderItemDetails.this).isEmpty()) {
                Intent i = new Intent(OrderItemDetails.this, MedicineWebViewActivity.class);
                i.putExtra("amount", total_amount);
                i.putExtra("deliverAmt", deliverAmt);
                i.putExtra("discount_amt",voucherDiscAmt);
                i.putExtra("claim_type",claimType);
                i.putExtra("voucher_id",voucherId);
                i.putExtra("voucher_no",voucherNo);
                i.putExtra("order_id",orderId);
                i.putExtra("order_on",orderOn);
                startActivity(i);
                finish();
            /*}else{
                Snackbar.make(view,"Please update your profile before proceed!",Snackbar.LENGTH_LONG).show();
            }*/
        }
    }

    private void actionAddPostOrder(){
        pDialog = new SweetAlertDialog(mcontext, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Please wait...");
        pDialog.show();
        pDialog.setCancelable(false);

        JSONObject json = new JSONObject();
        String deliverAmt = deliveryFee.getText().toString().replace("₹","");
        String total_amount = total.getText().toString().replace("₹","");

        try {
            json = JSONBuilder.getPostAddOrderJson(mcontext, orderId, "", deliverAmt, voucherDiscAmt,
                    total_amount, claimType, voucherId, voucherNo, orderOn, paymentMode, "");
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("POST_ORDER_JSON",json.toString());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING+NetworkConstant.MEDICINE_UPDATE)
                .addJSONObjectBody(json)
                .setOkHttpClient(okHttpClient)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called post add order");
                        Log.e("responsePostAddOrd",response.toString());
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                pDialog.setTitleText("Success!")
                                        .setContentText(response.getString(ResponseAttributeConstants.MSG))
                                        .setConfirmText("OK")
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        onPause();
                                        finish();
                                    }
                                });
//
                            } else {
                                pDialog.setTitleText("Failed! Please Try Again.")
                                        .setContentText(response.getString(ResponseAttributeConstants.MSG))
                                        .setConfirmText("OK")
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        cancelProgressDialog();
                        Log.e("ErrorAddOrder",error.toString());
                        Timber.e("called onError of User dairy order API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        pDialog.setTitleText("Error!")
                                .setContentText(error.toString())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIcon(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isVoucherClaim = false;
    }
}
