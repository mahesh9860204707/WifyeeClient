package com.wifyee.greenfields.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.activity.AddAddress;
import com.wifyee.greenfields.activity.BaseActivity;
import com.wifyee.greenfields.activity.SignUpOTPActivity;
import com.wifyee.greenfields.adapters.OrderAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.dairyorder.DairyListItemAdapter;
import com.wifyee.greenfields.dairyorder.DairyNetworkConstant;
import com.wifyee.greenfields.dairyorder.OrderSummaryActivity;
import com.wifyee.greenfields.dairyorder.PlaceOrderAdapter;
import com.wifyee.greenfields.dairyorder.PlaceOrderData;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.foodorder.AddToCartActivity;
import com.wifyee.greenfields.foodorder.ModelMapper;
import com.wifyee.greenfields.interfaces.FragmentInterface;
import com.wifyee.greenfields.models.ItemQtyModel;
import com.wifyee.greenfields.models.OrderModel;
import com.wifyee.greenfields.models.response.FailureResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class MyCartFragment extends Fragment implements FragmentInterface {

    private RecyclerView recyclerView;
    private Button btn_continue,addAddress;
    private Context mContext = null;
    private TextView totalAmount,tvExceed,address,change;
    private ArrayList<PlaceOrderData> orderItem = new ArrayList<>() ;
    PlaceOrderAdapter adapter;
    FragmentInterface fInterface;
    private ArrayList<ItemQtyModel> itemQtyModels = new ArrayList<>() ;
    ItemQtyModel[] item;
    public ProgressDialog progressDialog;
    JSONObject object;
    ArrayList<Integer> exceed = new ArrayList<>();
    RelativeLayout rl_address;
    LinearLayout llBottom;
    public static boolean is_address_set;
    public static String completeAddress,location,latitude,longitude;

    public MyCartFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fInterface = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_cart_fragment, container, false);

        totalAmount = (TextView)view.findViewById(R.id.tv_totalamount);
        tvExceed = (TextView)view.findViewById(R.id.tv_exceed);
        addAddress = (Button) view.findViewById(R.id.add_address);
        rl_address = (RelativeLayout) view.findViewById(R.id.rl_address);
        address = (TextView) view.findViewById(R.id.address);
        change = (TextView) view.findViewById(R.id.change);
        llBottom = (LinearLayout) view.findViewById(R.id.ll);
        recyclerView = (RecyclerView) view.findViewById(R.id.order_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new PlaceOrderAdapter(getContext(), orderItem,fInterface);
        recyclerView.setAdapter(adapter);
        fillList();

        btn_continue = (Button) view.findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = totalAmount.getText().toString();
                if (containsDigit(text) && !text.equals("0.0")) {
                    //  setDialog();
                    orderItem = adapter.placeOrderData;
                    object = setItem();
                    checkItemQuantity(object);

                }else{
                    showErrorDialog("Add item quantity to continue payment!");
                }
            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddAddress.class);
                intent.putExtra("cart","other_cart");
                startActivity(intent);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddAddress.class);
                intent.putExtra("cart","other_cart");
                startActivity(intent);
            }
        });

        // int quantity = 0;
        setItem();


        return view;
    }

    @Override
    public void onResume() {
        if (is_address_set){
            rl_address.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
            addAddress.setVisibility(View.INVISIBLE);
            addAddress.setClickable(false);
            address.setText(completeAddress);
        }
        super.onResume();
    }

    private JSONObject setItem(){
        double amount = 0.0;
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
                }
                object.put(ResponseAttributeConstants.ITEM, jsonArr);

                //Item Qty Checking
                //checkItemQuantity(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        totalAmount.setText(String.valueOf(amount));

        return object;
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
        if (orderItem!=null){
            orderItem.clear();
        }
        SQLController controller=new SQLController(getContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT * from cart order by id desc";

        Cursor cursor = controller.retrieve(query);
        if(cursor.getCount()>0){
            //emptyCartIcon.setVisibility(View.GONE);
            //emptyCartTxt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
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

                double calculateAmount = Double.parseDouble(price) * Integer.parseInt(quantity);

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
                orderItem.add(data);

            }while (cursor.moveToNext());
        }else {
            recyclerView.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
        cursor.close();
        controller.close();
    }

    public void showErrorDialog(final String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                arg0.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    protected void showProgressDialog() {

        progressDialog = new ProgressDialog(getContext(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
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

    private void checkItemQuantity(JSONObject object){
        if(itemQtyModels!=null){
            itemQtyModels.clear();
        }
        showProgressDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                            Intent intent = IntentFactory.createOrderSummaryDetailsActivity(getContext());
                            intent.putParcelableArrayListExtra("data",orderItem);
                            intent.putExtra("amount",totalAmount.getText().toString());
                            intent.putExtra("location",location);
                            intent.putExtra("latitude",latitude);
                            intent.putExtra("longitude",longitude);
                            intent.putExtra("complete_add",completeAddress);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getContext(),"Please check item quantity is exceeding",Toast.LENGTH_SHORT).show();
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
        Double amount = 0.0;
        if(orderItem.size()>0){
            for(int i= 0;i<orderItem.size();i++){
                int quantityValue = Integer.parseInt(orderItem.get(i).getQuantity());
                double quantityAmount = (quantityValue * Double.parseDouble(orderItem.get(i).getOrderPrice()));
                amount = amount + quantityAmount;
            }
        }
        totalAmount.setText(String.valueOf(amount));
    }
}
