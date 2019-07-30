package com.wifyee.greenfields.dairyorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.MobicashApplication;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.activity.AddAddress;
import com.wifyee.greenfields.activity.BaseActivity;
import com.wifyee.greenfields.activity.DiscountClaim;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.foodorder.AddToCartActivity;
import com.wifyee.greenfields.interfaces.FragmentInterface;
import com.wifyee.greenfields.models.ItemQtyModel;
import com.wifyee.greenfields.models.requests.GetClientProfileRequest;
import com.wifyee.greenfields.models.response.GetClientProfileResponse;
import com.wifyee.greenfields.models.response.GetProfile;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class OrderSummaryActivity extends BaseActivity implements FragmentInterface {

    @BindView(R.id.mtoolbar)
    Toolbar mToolbar;

    private RecyclerView recyclerView;
    private Button addAddress;
    private RelativeLayout btn_continue;
    private Context mContext = null;
    public TextView totalAmount,tvExceed,address,change,totalDiscountAmt,claimHere,txtClaimText,
            emptyCartTxt,txtTotalDiscountAmt,txtDeliverTo,itemCount,txtTaxes;
    private ArrayList<PlaceOrderData> orderItem = new ArrayList<>() ;
    PlaceOrderAdapter adapter;
    FragmentInterface fInterface;
    private ArrayList<ItemQtyModel> itemQtyModels = new ArrayList<>() ;
    ItemQtyModel[] item;
    JSONObject object;
    ArrayList<Integer> exceed = new ArrayList<>();
    RelativeLayout rl_address,rl_discount;
    public static boolean is_address_set,isVoucherClaim;
    public static String completeAddress,location,latitude,longitude,
            voucherId="",voucherNo="",claimType="",voucherName="",voucherDiscAmt="";
    ImageView emptyCartIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        ButterKnife.bind(this);
        mContext = this;
        fInterface = this;

        /*Bundle bd = getIntent().getBundleExtra("main_data");
        orderItem = bd.getParcelableArrayList("data");*/
        tvExceed = (TextView)findViewById(R.id.tv_exceed);
        addAddress = (Button) findViewById(R.id.add_address);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        rl_discount = (RelativeLayout) findViewById(R.id.rl_discount);
        address = (TextView) findViewById(R.id.address);
        change = (TextView) findViewById(R.id.change);
        totalDiscountAmt = (TextView) findViewById(R.id.total_discount_amt);
        recyclerView = (RecyclerView) findViewById(R.id.order_recyclerview);
        claimHere = (TextView) findViewById(R.id.claim_here);
        txtClaimText = (TextView) findViewById(R.id.txt);
        emptyCartIcon = findViewById(R.id.empty_cart_icon);
        emptyCartTxt = findViewById(R.id.empty_txt);
        txtTotalDiscountAmt = findViewById(R.id.txt_total_discount_amt);
        txtDeliverTo = findViewById(R.id.txt_deliver_to);
        btn_continue =  findViewById(R.id.rl_proceedt_payment);
        totalAmount = findViewById(R.id.tv_totalamount);
        itemCount =  findViewById(R.id.item_count);
        txtTaxes =  findViewById(R.id.txt_taxes);
        TextView txtProceedToPayment =  findViewById(R.id.txt_proceed_to_payment);

        TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.order_summary_details);

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

        toolbarTitle.setTypeface(Fonts.getSemiBold(this));
        addAddress.setTypeface(Fonts.getSemiBold(this));
        emptyCartTxt.setTypeface(Fonts.getSemiBold(this));
        txtProceedToPayment.setTypeface(Fonts.getSemiBold(this));
        txtTotalDiscountAmt.setTypeface(Fonts.getRegular(this));
        totalDiscountAmt.setTypeface(Fonts.getSemiBold(this));
        txtClaimText.setTypeface(Fonts.getRegular(this));
        claimHere.setTypeface(Fonts.getSemiBold(this));
        txtDeliverTo.setTypeface(Fonts.getRegular(this));
        change.setTypeface(Fonts.getSemiBold(this));
        address.setTypeface(Fonts.getRegular(this));
        totalAmount.setTypeface(Fonts.getSemiBold(this));
        itemCount.setTypeface(Fonts.getRegular(this));
        txtTaxes.setTypeface(Fonts.getRegular(this));

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new PlaceOrderAdapter(OrderSummaryActivity.this, orderItem,fInterface);
        recyclerView.setAdapter(adapter);

        fillList();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = totalAmount.getText().toString();
                if(isVoucherClaim) {
                    if (containsDigit(text) && !text.equals("0.0")) {
                        //  setDialog();
                        orderItem = adapter.placeOrderData;
                        checkItemQuantity(object);

                    /* Intent intent = IntentFactory.createOrderSummaryDetailsActivity(OrderSummaryActivity.this);
                    intent.putParcelableArrayListExtra("data",orderItem);
                    intent.putExtra("amount",totalAmount.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    finish();*/

                    }else{
                        showErrorDialog("Add item quantity to continue payment!");
                    }
                }else {
                    showErrorDialog("Please claim the discount amount first.");
                }
            }
        });

       // int quantity = 0;
        double amount = 0.0;
        double discountAmt = 0.0;
        int totalItem = 0;
        object = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        try {
            if(orderItem.size()>0) {
                int size = orderItem.size();
                for (int i = 0; i < size; i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(ResponseAttributeConstants.ITEMID, orderItem.get(i).getItemId());
                    jsonArr.put(jsonObject);

                    int quantityValue = Integer.parseInt(orderItem.get(i).getQuantity());
                    double quantityAmount = (quantityValue * Double.parseDouble(orderItem.get(i).getOrderPrice()));
                    amount = amount + quantityAmount;

                    double quantityDiscountAmt = (quantityValue * Double.parseDouble(orderItem.get(i).getItemDiscount()));
                    discountAmt = discountAmt + quantityDiscountAmt;

                    totalItem = totalItem + quantityValue;

                }
                object.put(ResponseAttributeConstants.ITEM, jsonArr);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        itemCount.setText(""+totalItem+" Items");
        totalAmount.setText(String.valueOf(amount));
        double roundOff = Math.round(discountAmt * 100.0) / 100.0;
        String s = String.format("%.2f", roundOff);
        totalDiscountAmt.setText("₹"+s);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderSummaryActivity.this, AddAddress.class);
                intent.putExtra("cart","other_summary_cart");
                startActivity(intent);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderSummaryActivity.this, AddAddress.class);
                intent.putExtra("cart","other_summary_cart");
                startActivity(intent);
            }
        });

        claimHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderSummaryActivity.this, DiscountClaim.class);
                intent.putExtra("flag","order_summary");
                intent.putExtra("amount",totalDiscountAmt.getText().toString());
                startActivity(intent);
            }
        });
    }

    public final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }

    public void fillList() {
        SQLController controller=new SQLController(getApplicationContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT * from cart_item order by id asc";

        Cursor cursor = controller.retrieve(query);
        if(cursor.getCount()>0){
            emptyCartIcon.setVisibility(View.GONE);
            emptyCartTxt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            rl_discount.setVisibility(View.VISIBLE);
            //rlBottom.setVisibility(View.VISIBLE);
            cursor.moveToFirst();
            do{
                String image_path =  cursor.getString(cursor.getColumnIndex("image_path"));
                String item_name =  cursor.getString(cursor.getColumnIndex("item_name"));
                String item_type = cursor.getString(cursor.getColumnIndex("item_type"));
                String merchant_id = cursor.getString(cursor.getColumnIndex("merchant_id"));
                String item_id = cursor.getString(cursor.getColumnIndex("item_id"));
                String quantity = cursor.getString(cursor.getColumnIndex("quantity"));
                String unit = cursor.getString(cursor.getColumnIndex("unit"));
                String price = cursor.getString(cursor.getColumnIndex("price"));
                String discount = cursor.getString(cursor.getColumnIndex("discount"));

                double calculateAmount = Double.parseDouble(price) * Integer.parseInt(quantity);
               // double calculateDiscount = Double.parseDouble(discount) * Integer.parseInt(quantity);

                PlaceOrderData data = new PlaceOrderData();
                data.setItemImagePath(image_path);
                data.setItemName(item_name);
                data.setItemType(item_type);
                data.setMerchantId(merchant_id);
                data.setItemId(item_id);
                data.setQuantity(quantity);
                data.setQuantityUnit(unit);
                data.setOrderPrice(price);
                data.setCalculatedAmt(String.valueOf(calculateAmount));
                data.setItemDiscount(discount);
                orderItem.add(data);
                Log.w("data ","Data Fetched");

            }while (cursor.moveToNext());
        }else {
            recyclerView.setVisibility(View.GONE);
            rl_discount.setVisibility(View.GONE);
            emptyCartIcon.setVisibility(View.VISIBLE);
            emptyCartTxt.setVisibility(View.VISIBLE);
            //rlBottom.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
        cursor.close();
        controller.close();
    }

    @Override
    protected void onResume() {
        if (is_address_set){
            rl_address.setVisibility(View.VISIBLE);
            btn_continue.setVisibility(View.VISIBLE);
            addAddress.setVisibility(View.INVISIBLE);
            addAddress.setClickable(false);
            address.setText(completeAddress);
        }

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

    private void checkItemQuantity(JSONObject object){
        if(itemQtyModels!=null){
            itemQtyModels.clear();
        }
        showProgressDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(OrderSummaryActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DairyNetworkConstant.BASE_URL_DAIRY
                + DairyNetworkConstant.CHECK_ITEM_QUANTITY, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                Log.w("checkItem",response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        JSONArray jsonArray = response.getJSONArray(DairyNetworkConstant.DATA);
                        item = new ItemQtyModel[jsonArray.length()];
                        for (int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            item[i] = new ItemQtyModel(
                                    jsonObject.getString(ResponseAttributeConstants.ITEMID),
                                    jsonObject.getString(ResponseAttributeConstants.QTY)
                            );
                            itemQtyModels.add(item[i]);
                        }

                        exceed.clear();
                        StringBuilder sb = new StringBuilder();
                        sb.append("We're sorry! following quantity are available.");
                        for(int k=0;k<orderItem.size();k++){
                            if (orderItem.get(k).getItemId().equals(itemQtyModels.get(k).getItemId())){
                                if (Integer.parseInt(orderItem.get(k).getQuantity())<=Integer.parseInt(itemQtyModels.get(k).getItemQuantity())){
                                    exceed.add(1);

                                }else{
                                    exceed.add(0);
                                    String item = "\n"+itemQtyModels.get(k).getItemQuantity()+" units of "+orderItem.get(k).getItemName();
                                    sb.append(item);
                                }
                            }
                        }

                        int flag = 1;
                        for (int i =0; i<exceed.size();i++){
                            if (exceed.get(i).equals(0)){
                                flag=0;
                            }
                        }
                        if (flag == 1){
                            Intent intent = IntentFactory.createOrderSummaryDetailsActivity(OrderSummaryActivity.this);
                            intent.putParcelableArrayListExtra("data",orderItem);
                            intent.putExtra("amount",totalAmount.getText().toString());
                            intent.putExtra("location",location);
                            intent.putExtra("latitude",latitude);
                            intent.putExtra("longitude",longitude);
                            intent.putExtra("complete_add",completeAddress);
                            intent.putExtra("claim_type",claimType);
                            intent.putExtra("voucher_id",voucherId);
                            intent.putExtra("voucher_no",voucherNo);
                            intent.putExtra("discount_amt",totalDiscountAmt.getText().toString().replace("₹",""));
                            startActivity(intent);
                            isVoucherClaim = false;
                        }else {
                            Toast.makeText(OrderSummaryActivity.this,"Please check item quantity is exceeding",Toast.LENGTH_SHORT).show();
                            Log.e("exceedArray",String.valueOf(exceed));
                            tvExceed.setText(sb);
                            tvExceed.setVisibility(View.VISIBLE);
                            tvExceed.postDelayed(new Runnable() {
                                public void run() {
                                    tvExceed.setVisibility(View.GONE);
                                }
                            }, 10000);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    cancelProgressDialog();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkItem_error", String.valueOf(error));
                cancelProgressDialog();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                Log.d("params_login", String.valueOf(params));
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);
    }


    @Override
    public void fragmentBecameVisible() {
        double amount = 0.0;
        double discountAmt = 0.0;
        int totalItem = 0;
        if(orderItem.size()>0){
            for(int i= 0;i<orderItem.size();i++){
                int quantityValue = Integer.parseInt(orderItem.get(i).getQuantity());
                double quantityAmount = (quantityValue * Double.parseDouble(orderItem.get(i).getOrderPrice()));
                amount = amount + quantityAmount;

                double quantityDiscountAmt = (quantityValue * Double.parseDouble(orderItem.get(i).getItemDiscount()));
                discountAmt = discountAmt + quantityDiscountAmt;

                totalItem = totalItem + quantityValue;
            }
        }
        itemCount.setText(""+totalItem+" Items");
        totalAmount.setText(String.valueOf(amount));
        double roundOff = Math.round(discountAmt * 100.0) / 100.0;
        String s = String.format("%.2f", roundOff);
        totalDiscountAmt.setText("₹"+s);
    }
}

