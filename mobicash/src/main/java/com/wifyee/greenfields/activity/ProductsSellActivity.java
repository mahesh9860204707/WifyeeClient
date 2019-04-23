package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.OnLoadMoreListener;
import com.wifyee.greenfields.adapters.SellingProductsListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.requests.ImageObjects;
import com.wifyee.greenfields.models.requests.SellingResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.GetClientProfileInfoResponse;
import com.wifyee.greenfields.models.response.GetProfileInfo;
import com.wifyee.greenfields.models.response.LoginResponse;
import com.wifyee.greenfields.models.response.MacUpdateAddressResponse;
import com.wifyee.greenfields.models.response.OTP_Response;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class ProductsSellActivity extends BaseActivity
{
    private RecyclerView recyclerView;
    private SellingResponse sellingResponse;
    private ImageObjects imageObjects;
    private ArrayList<ImageObjects> imageObjectsArrayList;
    private ArrayList<SellingResponse> sellingResponseArrayList;
    private int PAGE_SIZE = 5;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private SellingProductsListAdapter productsListAdapter;
    private Toolbar mToolbar;
    private ImageButton back;
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_OFFER_NEAR_LIST_FAIL,
            NetworkConstant.STATUS_OFFER_NEAR_LIST_SUCCESS

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_product);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        back = (ImageButton) findViewById(R.id.toolbar_back);
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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_selling);
        showProgressDialog();
        callApiForSelling(ProductsSellActivity.this);
       // MobicashIntentService.startActionGetOfferNerar(getApplicationContext());
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
       /* productsListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (contacts.size() <= 20) {
                    contacts.add(null);
                    contactAdapter.notifyItemInserted(contacts.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contacts.remove(contacts.size() - 1);
                            contactAdapter.notifyItemRemoved(contacts.size());

                            //Generating more data
                            int index = contacts.size();
                            int end = index + 10;
                            for (int i = index; i < end; i++) {
                                Contact contact = new Contact();
                                contact.setPhone(phoneNumberGenerating());
                                contact.setEmail("DevExchanges" + i + "@gmail.com");
                                contacts.add(contact);
                            }
                            contactAdapter.notifyDataSetChanged();
                            contactAdapter.setLoaded();
                        }
                    }, 5000);
                } else {
                    Toast.makeText(Pro.this, "Loading data completed", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }
    private OnLoadMoreListener onLoadMoreListener;
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginStatusReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(loginStatusReceiver, new IntentFilter(action));
        }

    }
    /*@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebook.onActivityResult(requestCode, resultCode, data);
        mGoogle.onActivityResult(requestCode, resultCode, data);
    }*/
    /**
     * Handling broadcast event for user login .
     */
    private BroadcastReceiver loginStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            cancelProgressDialog();
            try {
                if (action.equals(NetworkConstant.STATUS_OFFER_NEAR_LIST_SUCCESS)) {
                    cancelProgressDialog();
                    bindAdapterSelling(sellingResponseArrayList);
                } else if (action.equals(NetworkConstant.STATUS_OFFER_NEAR_LIST_FAIL)) {
                    cancelProgressDialog();
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_USER_SIGNUP_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        showErrorDialog(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showErrorDialog(errorMessage);
                    }
                }

                    // Toast.makeText(getApplicationContext(),"Some Error Occured in Confirmation the  Otp .Please try after some time",Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                Timber.e(" Exception caught in loginStatusReceiver " + e.getMessage());
            }
        }
    };
        //Call Api for Products Selling
    private void callApiForSelling(final Activity context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("begin", "0");
            jsonObject.put("end", "100");
            jsonObject.put("sort", "ASC");

            //jsonObject.put("sort", LocalPreferenceUtility.getUserCode(context));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, NetworkConstant.MOBICASH_BASE_URL_TESTING+NetworkConstant.SELLING_ITEMS_MERCHANT, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        cancelProgressDialog();
                        JSONArray sellingssListArray = response.getJSONArray(ResponseAttributeConstants.SELLING_DATA);
                        int sizeOfSellingListArray = sellingssListArray.length();
                        //sellingResponseArrayList.clear();
                        for (int i = 0; i < sizeOfSellingListArray; i++) {
                            sellingResponse = new SellingResponse();
                            JSONObject sellingData = sellingssListArray.getJSONObject(i);
                            String itemId = sellingData.getString("itemId");
                            sellingResponse.setItemId(itemId);
                            String itemName = sellingData.getString("itemName");
                            sellingResponse.setItemName(itemName);
                            String itemPrice = sellingData.getString("itemPrice");
                            sellingResponse.setItemPrice(itemPrice);
                            String discountType = sellingData.getString("discountType");
                            sellingResponse.setDiscountType(discountType);
                            String discount = sellingData.getString("discount");
                            sellingResponse.setDiscount(discount);
                            String mobicash_commission = sellingData.getString("mobicash_commission");
                            sellingResponse.setMobicash_commission(mobicash_commission);
                            String commission_type = sellingData.getString("commission_type");
                            sellingResponse.setCommission_type(commission_type);
                            String quantity = sellingData.getString("quentity");
                            sellingResponse.setQuentity(quantity);
                            String status = sellingData.getString("status");
                            sellingResponse.setStatus(status);
                            String createdDate = sellingData.getString("createdDate");
                            sellingResponse.setCreatedDate(createdDate);
                            String updateddDate = sellingData.getString("updateddDate");
                            sellingResponse.setUpdateddDate(updateddDate);
                            JSONArray imageArray = new JSONArray(sellingData.getString("imageObj"));
                            imageObjectsArrayList = new ArrayList<>();
                            for (int j = 0; j < imageArray.length(); j++) {
                                if (imageArray.length() > 0) {
                                    imageObjects = new ImageObjects();
                                    JSONObject imageObject = imageArray.getJSONObject(j);
                                    String id = imageObject.getString("imageId");
                                    imageObjects.setImageId(id);
                                    String path = imageObject.getString("imagePath");
                                    imageObjects.setImagePath(path);
                                    imageObjectsArrayList.add(imageObjects);
                                    sellingResponse.setImageObj(imageObjectsArrayList);
                                }
                            }
                            sellingResponseArrayList.add(sellingResponse);
                        }
                        //Binding Selling Adapter
                        bindAdapterSelling(sellingResponseArrayList);
                        cancelProgressDialog();
                    } else {
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
                cancelProgressDialog();
                Log.d("volley_error", String.valueOf(error));
                Toast.makeText(context, "response Error", Toast.LENGTH_SHORT).show();
                cancelProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                cancelProgressDialog();
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                Log.d("params", String.valueOf(params));
                return params;
            }
        };
        sellingResponseArrayList = new ArrayList<>();
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setShouldCache(false);
    }

    private void initListener() {
        productsListAdapter.setOnItemClickListener(new SellingProductsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(ProductsSellActivity.this, ProductDetailsActivity.class).putExtra("ArrayValue", (Serializable) sellingResponseArrayList.get(position)));
                Toast.makeText(ProductsSellActivity.this, "Clicked at index " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Binding Adapter in recyclerView
    private void bindAdapterSelling(ArrayList<SellingResponse> sellingResponseArrayList) {
        productsListAdapter = new SellingProductsListAdapter(this);
        productsListAdapter.addAll(sellingResponseArrayList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(productsListAdapter);

        initListener();
        // sellingResponseArrayList.clear();
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                    isLoading = true;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData();
                        }
                    }, 2000);
                }
            }
        }
    };

    private void loadData() {
        isLoading = false;

        List<SellingResponse> productList = new ArrayList<>();
        SellingResponse products;

        int index = productsListAdapter.getItemCount() - 1;
        int end = index + PAGE_SIZE;

        if (end <= sellingResponseArrayList.size()) {
            for (int i = index; i < end; i++) {
                products = new SellingResponse();

                products.setItemId(String.valueOf(i) + 1);
                products.setItemPrice(products.getItemPrice());
                products.setItemName(products.getItemName());
                products.setDiscount(products.getDiscount());
                products.setDiscountType(products.getDiscountType());
                productList.add(products);
            }
            productsListAdapter.addAll(productList);
            productsListAdapter.notifyDataSetChanged();
            productsListAdapter.setLoading(false);
            if (end >= sellingResponseArrayList.size()) {
                productsListAdapter.setLoading(false);
            }

        }else{
            productsListAdapter.setLoading(false);
        }

    }
}

