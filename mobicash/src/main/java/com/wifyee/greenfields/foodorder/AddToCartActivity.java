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
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.SharedPrefence.SharedPreference;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.activity.AddAddress;
import com.wifyee.greenfields.activity.ApplyCoupons;
import com.wifyee.greenfields.activity.DiscountClaim;
import com.wifyee.greenfields.activity.MobicashDashBoardActivity;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.dairyorder.OrderSummaryActivity;
import com.wifyee.greenfields.dairyorder.OrderSummaryDetails;
import com.wifyee.greenfields.dairyorder.PlaceOrderData;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.foodorder.SharedPrefenceAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.foodorder.CartFoodOderRequest;
import com.wifyee.greenfields.interfaces.FragmentInterface;
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

public class AddToCartActivity extends AppCompatActivity implements FragmentInterface {

    private Toolbar mToolbar;
    private ImageButton back;
    private SharedPreference sharedPreference;
    private ArrayList<Items> itemsArrayList=new ArrayList<>();
    private TextView address,change;
    private RecyclerView recyclerView_favorites;
    private Context mcontext;
    private Button btn_continue,addAddress;
    private Double gst = 0.00;
    private SharedPrefenceAdapter favoritesAdapter;
    private List<Items> shred_list = new ArrayList<>();
    private Items itemValue ;
    private int paymentSelectedIndex = 0,deliveryFee;
    CartFoodOderRequest foodOrderRequest=new CartFoodOderRequest();
    public ProgressDialog progressDialog = null;
    ///  public static List<SharedPrefenceList> DefaultOder_list;
    ArrayList<SharedPrefenceList> Current_favoritesList = new ArrayList<>();
    public TextView tv_totalamount,toolBarTitle,totalDiscountAmt,claimHere,txtClaimText,
            txtTotalDiscountAmt,txtApplyCoupon,txtSummary,txtItemTotal,subTotalPrice,txtDelivery,
            deliveryFeePrice,txtTotal,totalPrice,txtPayment;
    private List<SharedPrefenceList> favorites = new ArrayList<>();
    RelativeLayout rl_address;
    LinearLayout llBottom;
    public static boolean is_address_set,isVoucherClaim;
    public static String completeAddress,location,latitude,longitude,voucherId="",
            voucherNo="",claimType="",voucherName="",voucherDiscAmt="";
    private RadioButton paymentCod,paymentWallet,paymentNetBanking;
    private CardView cardViewCoupon,cardViewDiscount,cardViewBillDetails,cardViewPayment;
    private RadioGroup paymentGroup;
    private int total_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        toolBarTitle = mToolbar.findViewById(R.id.food_oder);
        back = (ImageButton) findViewById(R.id.toolbar_back);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        tv_totalamount = (TextView) findViewById(R.id.tv_totalamount);
        addAddress = (Button) findViewById(R.id.add_address);
        change = (TextView) findViewById(R.id.change);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        address = (TextView) findViewById(R.id.address);
        llBottom = (LinearLayout) findViewById(R.id.ll);
        recyclerView_favorites = (RecyclerView) findViewById(R.id.favroite_recyclerview);
        totalDiscountAmt = findViewById(R.id.total_discount_amt);
        claimHere =  findViewById(R.id.claim_here);
        txtClaimText = findViewById(R.id.txt);
        txtTotalDiscountAmt = findViewById(R.id.txt_total_discount_amt);
        txtApplyCoupon = findViewById(R.id.txt_apply_coupon);
        cardViewDiscount = findViewById(R.id.card_view_discount);
        cardViewCoupon = findViewById(R.id.card_view_coupon);
        txtSummary = findViewById(R.id.txt_summary);
        txtItemTotal = findViewById(R.id.txt_item_total);
        subTotalPrice = findViewById(R.id.sub_total_price);
        txtDelivery = findViewById(R.id.txt_delivery);
        deliveryFeePrice = findViewById(R.id.delivery_fee_price);
        txtTotal = findViewById(R.id.txt_total);
        totalPrice = findViewById(R.id.total_price);
        txtPayment = findViewById(R.id.txt_payment);
        paymentCod = findViewById(R.id.rb_cod);
        paymentWallet = findViewById(R.id.rb_wallet);
        paymentNetBanking = findViewById(R.id.rb_netbanking);
        cardViewBillDetails = findViewById(R.id.card_view_bill_details);
        cardViewPayment = findViewById(R.id.card_view_payment);
        paymentGroup = findViewById(R.id.payment_group);
        TextView txtDeliverTo = findViewById(R.id.txt_deliver_to);

        mcontext = this;
        sharedPreference = new SharedPreference(mcontext);


        //MobicashIntentService.startActionGstONFoodItem(mcontext);

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

        toolBarTitle.setText(LocalPreferenceUtility.getMerchantName(mcontext));
        toolBarTitle.setTypeface(Fonts.getSemiBold(this));
        addAddress.setTypeface(Fonts.getSemiBold(this));
        btn_continue.setTypeface(Fonts.getSemiBold(this));
        txtTotalDiscountAmt.setTypeface(Fonts.getRegular(this));
        totalDiscountAmt.setTypeface(Fonts.getSemiBold(this));
        txtClaimText.setTypeface(Fonts.getRegular(this));
        claimHere.setTypeface(Fonts.getSemiBold(this));
        txtApplyCoupon.setTypeface(Fonts.getSemiBold(this));
        txtSummary.setTypeface(Fonts.getRegular(this));
        txtItemTotal.setTypeface(Fonts.getRegular(this));
        subTotalPrice.setTypeface(Fonts.getRegular(this));
        txtDelivery.setTypeface(Fonts.getRegular(this));
        deliveryFeePrice.setTypeface(Fonts.getRegular(this));
        txtTotal.setTypeface(Fonts.getSemiBold(this));
        totalPrice.setTypeface(Fonts.getSemiBold(this));
        paymentCod.setTypeface(Fonts.getRegular(this));
        txtPayment.setTypeface(Fonts.getRegular(this));
        paymentWallet.setTypeface(Fonts.getRegular(this));
        paymentNetBanking.setTypeface(Fonts.getRegular(this));
        txtDeliverTo.setTypeface(Fonts.getRegular(this));
        change.setTypeface(Fonts.getSemiBold(this));
        address.setTypeface(Fonts.getRegular(this));

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),""+total_amount,Toast.LENGTH_SHORT).show();
                if(favorites.size()!= 0) {
                    if(cardViewDiscount.getVisibility() == View.VISIBLE && isVoucherClaim){
                       placeOrder();
                       //Snackbar.make(view,"All set",Snackbar.LENGTH_LONG).show();
                    }else if(cardViewDiscount.getVisibility() == View.GONE){
                        placeOrder();
                        //Snackbar.make(view,"without voucher",Snackbar.LENGTH_LONG).show();
                    }else {
                        Snackbar.make(view,"Please claim the discount amount first.",Snackbar.LENGTH_LONG).show();
                    }
                }else {
                    Snackbar.make(view,"Look like your cart is empty",Snackbar.LENGTH_LONG).show();
                }

                /*//appy gst on price
                if (shred_list.size()!=0) {
                    shred_list.clear();
                }
                if (!tv_totalamount.getText().toString().equalsIgnoreCase("0.0")) {
                    double h = (gst * Double.parseDouble(tv_totalamount.getText().toString())) / 100;
                    total_amount = Double.parseDouble(tv_totalamount.getText().toString()) + h;
                    //Toast.makeText(getApplicationContext(), String.valueOf(total_amount), Toast.LENGTH_SHORT).show();
                    setDialog();
                  //  MobicashIntentService.startActionSendFoodRequest(mcontext,getaddToCartRequest(String.valueOf(total_amount)));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Amount should be Greater Than 0.", Toast.LENGTH_SHORT).show();
                }*/


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

        claimHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddToCartActivity.this, DiscountClaim.class);
                intent.putExtra("flag","food_cart");
                intent.putExtra("amount",totalDiscountAmt.getText().toString());
                startActivity(intent);
            }
        });

        cardViewCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddToCartActivity.this, ApplyCoupons.class);
                startActivity(intent);
            }
        });

        //if (favorites != null) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        recyclerView_favorites.setLayoutManager(linearLayoutManager);
        favoritesAdapter = new SharedPrefenceAdapter(mcontext, recyclerView_favorites, favorites,this);
        recyclerView_favorites.setAdapter(favoritesAdapter);

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

    }

    private void placeOrder(){
        if (paymentSelectedIndex == 0) {
            showProgressDialog();
            MobicashIntentService.startActionSendFoodRequest(mcontext,
                    getaddToCartRequest(String.valueOf(total_amount)),location,latitude,longitude,
                    completeAddress,voucherDiscAmt,claimType,voucherId,voucherNo,"");
        } else if ((paymentSelectedIndex == 1)) {
            showProgressDialog();
            MobicashIntentService.startActionSendFoodRequest(mcontext,
                    getaddToCartRequest(String.valueOf(total_amount)),location,latitude,longitude,
                    completeAddress,voucherDiscAmt,claimType,voucherId,voucherNo,"");
            //MobicashIntentService.startActionDeductMoneyWallet(mcontext, getpaymetbywallet(String.valueOf(total_amount)));
        }
        else if ((paymentSelectedIndex == 2)) {
            foodOrderRequest = new CartFoodOderRequest();
            Intent i = new Intent(mcontext,FoodWebViewActivity.class);
            i.putExtra("amount",String.valueOf(total_amount));
            i.putExtra(PaymentConstants.FOODORDER_EXTRA, getaddToCartRequest(String.valueOf(total_amount)));
            i.putExtra("order_id",foodOrderRequest.orderId);
            i.putExtra("location",location);
            i.putExtra("latitude",latitude);
            i.putExtra("longitude",longitude);
            i.putExtra("completeAddress",completeAddress);
            i.putExtra("voucherDiscAmt",voucherDiscAmt);
            i.putExtra("claimType",claimType);
            i.putExtra("voucherId",voucherId);
            i.putExtra("voucherNo",voucherNo);
            startActivity(i);

            /*Intent paymentIntent = IntentFactory.createPayuBaseActivity(mcontext);
            paymentIntent.putExtra(PaymentConstants.STRING_EXTRA, String.valueOf(total_amount));
            paymentIntent.putExtra("foodCart", "foodamount");
            paymentIntent.putExtra(PaymentConstants.FOODORDER_EXTRA, getaddToCartRequest(String.valueOf(total_amount)));
            startActivity(paymentIntent);*/
        }
    }


    public void fillList() {
        SQLController controller = new SQLController(getApplicationContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT * from food_cart order by id asc";

        Cursor cursor = controller.retrieve(query);
        if (cursor.getCount() > 0) {
            //Log.e("if", "in if");
            cursor.moveToFirst();
            do {
                String image_path = cursor.getString(cursor.getColumnIndex("image_path"));
                String item_name = cursor.getString(cursor.getColumnIndex("item_name"));
                String item_id = cursor.getString(cursor.getColumnIndex("item_id"));
                String item_description = cursor.getString(cursor.getColumnIndex("item_description"));
                String quantity = cursor.getString(cursor.getColumnIndex("quantity"));
                String price = cursor.getString(cursor.getColumnIndex("price"));
                String discount = cursor.getString(cursor.getColumnIndex("discount"));
                String qty_half_full = cursor.getString(cursor.getColumnIndex("qty_half_full"));
                String category = cursor.getString(cursor.getColumnIndex("category"));

                double calculateAmount = Double.parseDouble(price) * Integer.parseInt(quantity);
                double calculateDiscount = Double.parseDouble(discount) * Integer.parseInt(quantity);

                SharedPrefenceList data = new SharedPrefenceList();
                data.setFoodImage(image_path);
                data.setName(item_name);
                data.setItemId(item_id);
                data.setDescription(item_description);
                data.setQuantiy(quantity);
                data.setPrice(price);
                data.setCalculatedAmt(String.valueOf(calculateAmount));
                data.setDiscountAmt(String.valueOf(calculateDiscount));
                data.setQty_half_full(qty_half_full);
                data.setCategory(category);

                favorites.add(data);
                Log.w("data ", "Data Fetched");

            } while (cursor.moveToNext());
        } else {
            //Log.e("else","in else");
            //recyclerView.setVisibility(View.GONE);
            //rl_discount.setVisibility(View.GONE);
            //emptyCartIcon.setVisibility(View.VISIBLE);
            //emptyCartTxt.setVisibility(View.VISIBLE);
            //rlBottom.setVisibility(View.GONE);
        }

        favoritesAdapter.notifyDataSetChanged();
        cursor.close();
        controller.close();

        double totalamount = 0.00;
        double discountAmt = 0.0;
        int totalItem = 0;

        if (favorites.size() > 0){
            for (int k = 0; k < favorites.size(); k++) {
                try {
                    String pricevlaue = favorites.get(k).calculatedAmt.trim();
                    String qty = favorites.get(k).quantiy.trim();
                    Double current = Double.valueOf(pricevlaue);
                    totalamount = current + totalamount;
                    //Log.e("amount",String.valueOf(totalamount));

                    double quantityDiscountAmt = Double.parseDouble(favorites.get(k).getDiscountAmt());
                    discountAmt = discountAmt + quantityDiscountAmt;

                    totalItem = totalItem + Integer.parseInt(qty);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            txtItemTotal.setText("Item Total ("+totalItem+" Item)");
            tv_totalamount.setText(String.valueOf(totalamount));
            double roundOff = Math.round(discountAmt * 100.0) / 100.0;
            String sumDiscount = String.format("%.2f", roundOff);
            totalDiscountAmt.setText("₹"+sumDiscount);

            if(sumDiscount.equals("0.00") || sumDiscount.equals("0.0") || sumDiscount.equals("0")){
                cardViewDiscount.setVisibility(View.GONE);
            }else {
                cardViewDiscount.setVisibility(View.VISIBLE);
            }

            float amt = (float) totalamount;
            int itemTotalAmount = Math.round(amt);
            if(itemTotalAmount<200){
                deliveryFee = 50;
            }else if(itemTotalAmount<500){
                deliveryFee = 20;
            }else {
                deliveryFee = 0;
            }

            int total = itemTotalAmount + deliveryFee;
            //totalAmount = String.valueOf(total);
            if(deliveryFee==0){
                deliveryFeePrice.setText("Free");
                deliveryFeePrice.setTextColor(getResources().getColor(R.color.secondaryPrimary));
            }else {
                deliveryFeePrice.setText("₹"+deliveryFee);
            }
            totalPrice.setText("₹"+total);
            total_amount =  total;
            subTotalPrice.setText("₹"+itemTotalAmount);

        } else {
            cardViewDiscount.setVisibility(View.GONE);
            cardViewCoupon.setVisibility(View.GONE);
            cardViewBillDetails.setVisibility(View.GONE);
            cardViewPayment.setVisibility(View.GONE);
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
                            getaddToCartRequest(String.valueOf(total_amount)),location,latitude,longitude,completeAddress,
                            voucherDiscAmt,claimType,voucherId,voucherNo,"");
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss", Locale.US);
        SimpleDateFormat format=new SimpleDateFormat("yyyymmddHHmmss",Locale.US);
        String datetime = formatter.format(today);
        String code_format = format.format(today);
        foodOrderRequest.orderId ="C_"+LocalPreferenceUtility.getMerchantId(mcontext)+code_format;

        for (int y = 0; y < favorites.size(); y++) {
           itemsArrayList.add(new Items(favorites.get(y).itemId, favorites.get(y).quantiy, favorites.get(y).price));
           foodOrderRequest.setItems(itemsArrayList);
        }

        if (paymentSelectedIndex==0) {
            foodOrderRequest.payment_mode="cod";
        } else if (paymentSelectedIndex==1)  {
            foodOrderRequest.payment_mode="wallet";
        }
        else if (paymentSelectedIndex==2) {
            foodOrderRequest.payment_mode="online";
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

        if (favorites!=null){
            favorites.clear();
        }

        fillList();

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

        //sharedPreference=new SharedPreference(mcontext);
        //favorites = sharedPreference.getFavorites(mcontext);
        //favoritesAdapter = new SharedPrefenceAdapter(mcontext, recyclerView_favorites, favorites);
        //recyclerView_favorites.setAdapter(favoritesAdapter);
        //favoritesAdapter.notifyDataSetChanged();
        /*if (favorites != null) {
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
        }*/

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
                    isVoucherClaim = false;
                    voucherDiscAmt="";
                    voucherName="";
                    voucherNo="";
                    voucherId="";
                    showSuccessDialog("Success");
                }
            } else if (actionOperatorList.equals(NetworkConstant.STATUS_FOODORDER_BYMERCHANT_LIST_FAIL)) {
                cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    cancelProgressDialog();
                    isVoucherClaim = false;
                    voucherDiscAmt="";
                    voucherName="";
                    voucherNo="";
                    voucherId="";
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
                            deleteCart();
                            startActivity(new Intent(AddToCartActivity.this, MobicashDashBoardActivity.class));
                            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                            finish();
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
                            location,latitude,longitude,completeAddress,voucherDiscAmt,claimType,voucherId,voucherNo,"");
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
                            location,latitude,longitude,completeAddress,voucherDiscAmt,claimType,voucherId,voucherNo,"");


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

    @Override
    public void fragmentBecameVisible() {
        double amount = 0.0;
        double discountAmt = 0.0;
        int totalItem = 0;

        if(favorites.size()>0){
            cardViewDiscount.setVisibility(View.VISIBLE);
            cardViewCoupon.setVisibility(View.VISIBLE);
            cardViewBillDetails.setVisibility(View.VISIBLE);
            cardViewPayment.setVisibility(View.VISIBLE);

            for(int i= 0;i<favorites.size();i++){
                int quantityValue = Integer.parseInt(favorites.get(i).getQuantiy());
                double quantityAmount = (quantityValue * Double.parseDouble(favorites.get(i).getPrice()));
                amount = amount + quantityAmount;

                double quantityDiscountAmt = (quantityValue * Double.parseDouble(favorites.get(i).getDiscountAmt()));
                discountAmt = discountAmt + quantityDiscountAmt;

                totalItem = totalItem + quantityValue;

            }

            txtItemTotal.setText("Item Total ("+totalItem+" Item)");
            tv_totalamount.setText(String.valueOf(amount));
            double roundOff = Math.round(discountAmt * 100.0) / 100.0;
            String sumDiscount = String.format("%.2f", roundOff);
            totalDiscountAmt.setText("₹"+sumDiscount);

            if(sumDiscount.equals("0.00") || sumDiscount.equals("0.0") || sumDiscount.equals("0")){
                cardViewDiscount.setVisibility(View.GONE);
            }else {
                cardViewDiscount.setVisibility(View.VISIBLE);
            }

            float amt = (float) amount;
            int itemTotalAmount = Math.round(amt);
            if(itemTotalAmount<200){
                deliveryFee = 50;
            }else if(itemTotalAmount<500){
                deliveryFee = 20;
            }else {
                deliveryFee = 0;
            }

            int total = itemTotalAmount + deliveryFee;
            //totalAmount = String.valueOf(total);

            if(deliveryFee==0){
                deliveryFeePrice.setText("Free");
                deliveryFeePrice.setTextColor(getResources().getColor(R.color.secondaryPrimary));
            }else {
                deliveryFeePrice.setText("₹"+deliveryFee);
                deliveryFeePrice.setTextColor(getResources().getColor(R.color.black));
            }
            totalPrice.setText("₹"+total);
            total_amount = total;
            subTotalPrice.setText("₹"+itemTotalAmount);

        }else {
            cardViewDiscount.setVisibility(View.GONE);
            cardViewCoupon.setVisibility(View.GONE);
            cardViewBillDetails.setVisibility(View.GONE);
            cardViewPayment.setVisibility(View.GONE);
            LocalPreferenceUtility.putMerchantId(mcontext, "");
            LocalPreferenceUtility.putMerchantName(mcontext, "Food Cart");
        }
    }

    private void deleteCart(){
        SQLController controller=new SQLController(AddToCartActivity.this);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "DELETE from food_cart";
        String result = controller.fireQuery(query);
        if(result.equals("Done")){
            Log.w("delete","Delete all successfully");
        }else {
            Log.w("result",result);
        }
        controller.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isVoucherClaim = false;
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