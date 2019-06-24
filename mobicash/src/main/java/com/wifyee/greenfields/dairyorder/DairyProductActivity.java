package com.wifyee.greenfields.dairyorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.activity.BaseActivity;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.foodorder.AddressRequest;
import com.wifyee.greenfields.foodorder.GPSTracker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DairyProductActivity extends BaseActivity implements DairyMainListAdapter.ItemListener {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    private RecyclerView recyclerView;
    private Context mContext = null;
    private DairyMainListAdapter.ItemListener listener ;
    private String  productId;
    GPSTracker gps;
    ImageView icNotHere;
    TextView txtNotHere,txtDetailNotHere;
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            DairyNetworkConstant.DAIRY_MERCHANT_LIST_STATUS_SUCCESS,
            DairyNetworkConstant.DAIRY_MERCHANT_LIST_STATUS_FAILURE,
            DairyNetworkConstant.DAIRY_WALLET_BALANCE_STATUS_SUCCESS,
            DairyNetworkConstant.DAIRY_WALLET_BALANCE_STATUS_FAILURE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_product);
        ButterKnife.bind(this);
        mContext = this;
        listener = this;

        gps = new GPSTracker(mContext);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        icNotHere = findViewById(R.id.ic_not_here);
        txtNotHere = findViewById(R.id.txt_we_are_not);
        txtDetailNotHere = findViewById(R.id.txt_detail_we_are_not);
        String title = getIntent().getStringExtra(NetworkConstant.EXTRA_DATA1);
        productId = getIntent().getStringExtra(NetworkConstant.EXTRA_DATA);

        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(title);

            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager horizontalLayoutManagaer =  new GridLayoutManager(mContext, 2);
        //AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getMerchantListDairyReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(getMerchantListDairyReceiver, new IntentFilter(action));
        }
        showProgressDialog();

        DairyProductIntentService.startActionGetMerchantList(this,getLatitudeAndLongitude());

        DairyProductIntentService.startActionGetWalletBalance(this, LocalPreferenceUtility.getUserMobileNumber(this));
    }

    private AddressRequest getLatitudeAndLongitude() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.latitude = LocalPreferenceUtility.getLatitude(DairyProductActivity.this);
        addressRequest.longitude = LocalPreferenceUtility.getLongitude(DairyProductActivity.this);
        addressRequest.productId = productId;
        /*if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            addressRequest.latitude = String.valueOf(latitude);
            addressRequest.longitude = String.valueOf(longitude);
            addressRequest.productId = productId;
            //Toast.makeText(mContext,latitude+"-"+longitude,Toast.LENGTH_SHORT).show();
        } else {
            gps.showSettingsAlert();
            addressRequest.latitude = "";
            addressRequest.longitude = "";
        }*/
        return addressRequest;
    }

    /**
     * Handling broadcast event for user Signup .
     */
    private BroadcastReceiver getMerchantListDairyReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            cancelProgressDialog();

            try {
                if (action.equals(DairyNetworkConstant.DAIRY_MERCHANT_LIST_STATUS_SUCCESS)) {
                    ArrayList<DairyMerchantListModel> item = intent.getParcelableArrayListExtra(NetworkConstant.EXTRA_DATA);
                    if (item.size()>0){
                        icNotHere.setVisibility(View.GONE);
                        txtNotHere.setVisibility(View.GONE);
                        txtDetailNotHere.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        DairyMainListAdapter adapter = new DairyMainListAdapter(DairyProductActivity.this, item, listener);
                        recyclerView.setAdapter(adapter);
                    }else {
                        icNotHere.setVisibility(View.VISIBLE);
                        txtNotHere.setVisibility(View.VISIBLE);
                        txtDetailNotHere.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }


                }else if(action.equals(DairyNetworkConstant.DAIRY_MERCHANT_LIST_STATUS_FAILURE)){
                    showErrorDialog("Something went wrong!");
                }else if(action.equals(DairyNetworkConstant.DAIRY_WALLET_BALANCE_STATUS_SUCCESS)){
                    LocalPreferenceUtility.saveWalletBalance(DairyProductActivity.this,intent.getStringExtra(NetworkConstant.EXTRA_DATA));
                }
            } catch (Exception e) {
                Timber.e(" Exception caught in getMerchantListDairyReceiver " + e.getMessage());
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemClick(DairyMerchantListModel item) {
        Intent intent = new Intent(this,DairyItemListActivity.class);
        intent.putExtra("data",item.getId());
        intent.putExtra("id",item.getId());
        intent.putExtra("current_status",item.getCurrentStatus());
        Log.e("data",item.getId());
        startActivity(intent);
    }
}
