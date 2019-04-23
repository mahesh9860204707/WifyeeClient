package com.wifyee.greenfields.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.adapters.OrderItemAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.mapper.ModelMapper;
import com.wifyee.greenfields.models.OrderItemModel;
import com.wifyee.greenfields.models.response.FailureResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;

public class OrderItemDetails extends AppCompatActivity {

    private OrderItemAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<OrderItemModel> listArray = new ArrayList<>();
    OrderItemModel[] order;
    private ProgressDialog progressDialog;
    Toolbar mToolbar;
    String orderId,merchantId,taskId,merchantType;
    ImageView img1,img2,img3,img4,img5;
    TextView txt1,txt2,txt3,txt4,txt5;
    View view1,view2,view3,view4;
    ImageView imageView;

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

        orderId = getIntent().getStringExtra("order_id");
        merchantId = getIntent().getStringExtra("merchantId");
        taskId = getIntent().getStringExtra("taskId");
        merchantType = getIntent().getStringExtra("merchantType");

        title.setText(orderId);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new OrderItemAdapter(this, listArray);
        recyclerView.setAdapter(adapter);
        dataLoad();

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
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject object) {
                        Log.e("-ORDER_ITEM-",object.toString());
                        cancelProgressDialog();
                        try {
                            if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                JSONObject jsonObject1 = object.getJSONObject(ResponseAttributeConstants.ORDER_DETAILS);
                                if (jsonObject1.has(ResponseAttributeConstants.PRESCRIPTION_IMAGE)){
                                    Log.w("in ores","presciption image");
                                    recyclerView.setVisibility(View.GONE);
                                    imageView.setVisibility(View.VISIBLE);
                                    Picasso.with(OrderItemDetails.this)
                                            .load(jsonObject1.getString(ResponseAttributeConstants.PRESCRIPTION_IMAGE))
                                            .into(imageView);
                                }
                                if(jsonObject1.has(ResponseAttributeConstants.ITEM)) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    imageView.setVisibility(View.GONE);
                                    JSONArray jsonArray = jsonObject1.getJSONArray(ResponseAttributeConstants.ITEM);
                                    order = new OrderItemModel[jsonArray.length()];
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        order[i] = new OrderItemModel(
                                                jsonObject.getString(ResponseAttributeConstants.ITEMNAME),
                                                jsonObject.getString(ResponseAttributeConstants.ITEMQUANTITY),
                                                jsonObject.getString(ResponseAttributeConstants.ITEMAMOUNT)
                                        );
                                        listArray.add(order[i]);
                                    }
                                    recyclerView.getRecycledViewPool().clear();
                                    adapter.notifyDataSetChanged();
                                }

                                JSONArray array = object.getJSONArray(ResponseAttributeConstants.ORDER_STATUS);
                                for (int j=0; j<array.length();j++){
                                    JSONObject jsonObject = array.getJSONObject(j);
                                    //String status = jsonObject.getString(ResponseAttributeConstants.TASK_STATUS);
                                    String status_date = jsonObject.getString(ResponseAttributeConstants.TASK_STATUS_DATE);
                                    if(j==0){
                                        txt1.setText("Ordered on "+status_date);
                                    }
                                    else if(j==1){
                                        txt2.setText("Processing on "+status_date);
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
}
