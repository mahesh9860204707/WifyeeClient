package com.wifyee.greenfields.foodorder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.RecyclerTouchListener;

import com.wifyee.greenfields.constants.NetworkConstant;

import com.wifyee.greenfields.dairyorder.DairyProductActivity;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.models.response.FailureResponse;

import com.wifyee.greenfields.services.MobicashIntentService;

import java.util.List;

public class MerchantActivity extends AppCompatActivity {
    public ProgressDialog progressDialog = null;
    private Context mContext = null;
    GPSTracker gps;
    Toolbar mToolbar;
    ImageButton back;
    List<Restaurant> category_List;
    MerchantRestuarantAdapter merchantRestuarantAdapter;
    RecyclerView merchant_rest;//,merchant_horiz;
    //LinearLayout emptyView;
    ImageView icNotHere;
    TextView txtNotHere,txtDetailNotHere;
    TextView textCartItemCount;
    private SpinKitView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        merchant_rest= findViewById(R.id.merchant_recyclerview_main);
        icNotHere = findViewById(R.id.ic_not_here);
        txtNotHere = findViewById(R.id.txt_we_are_not);
        txtDetailNotHere = findViewById(R.id.txt_detail_we_are_not);
        progressBar = findViewById(R.id.progressbar);
        // merchant_horiz=(RecyclerView)findViewById(R.id.merchant_recyclerview_horiz);

        mToolbar = findViewById(R.id.toolbar);
        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        mContext = this;

        gps = new GPSTracker(mContext);

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

        toolBarTitle.setTypeface(Fonts.getSemiBold(this));

        //showProgressDialog();

        //MobicashIntentService.startActionFoodMerchantByLocation(this, getLatitudeAndLongitude());

        merchant_rest.addOnItemTouchListener(new RecyclerTouchListener(mContext,
                merchant_rest, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Restaurant dataModel= category_List.get(position);
                //LocalPreferenceUtility.putMerchantId(getApplicationContext(), category_List.get(position).getMerchant_id());
                //LocalPreferenceUtility.putMerchantName(getApplicationContext(), dataModel.restaurant_name);

                Intent intent = new Intent(MerchantActivity.this, FoodOrderListActivity.class);
                intent.putExtra("merchantid",category_List.get(position).getMerchant_id());
                intent.putExtra("merchantName",dataModel.restaurant_name);
                intent.putExtra("current_status",dataModel.getStatus());
                intent.putExtra("flag","normal");
                startActivity(intent);

                //startActivity(IntentFactory.createAddOderByMerchantFoodActivity(getApplicationContext()));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

//        merchant_horiz.addOnItemTouchListener(new RecyclerTouchListener(mContext,
//                merchant_rest, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, final int position) {
//                Restaurant dataModel= category_List.get(position);
//                LocalPreferenceUtility.putMerchantId(getApplicationContext(),category_List.get(position).getMerchant_id());
//                LocalPreferenceUtility.putMerchantName(getApplicationContext(), dataModel.restaurant_name);
//                startActivity(IntentFactory.createAddOderByMerchantFoodActivity(getApplicationContext()));
//            }
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));
    }


    private AddressRequest getLatitudeAndLongitude() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.latitude = LocalPreferenceUtility.getLatitude(MerchantActivity.this);
        addressRequest.longitude = LocalPreferenceUtility.getLongitude(MerchantActivity.this);
        /*if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            addressRequest.latitude = String.valueOf(latitude);
            addressRequest.longitude = String.valueOf(longitude);
            //addressRequest.distance="300";
            //Toast.makeText(mContext,latitude+"-"+longitude,Toast.LENGTH_SHORT).show();
        } else {
            gps.showSettingsAlert();
            addressRequest.latitude = "";
            addressRequest.longitude = "";
        }*/
        return addressRequest;
    }

    @Override
    public void onPause() {
        super.onPause();
        //cancelProgressDialog();
        progressBar.setVisibility(View.INVISIBLE);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(merchantcategoryListReceiver);
    }

    private void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(merchantcategoryListReceiver, new IntentFilter(action));
        }

        //  showProgressDialog();
        progressBar.setVisibility(View.VISIBLE);
        MobicashIntentService.startActionFoodMerchantByLocation(this, getLatitudeAndLongitude());

        setupBadge();
    }

    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_FOODODER_BYMERCHANT_LIST_SUCCESS,
            NetworkConstant.STATUS_FOODORDER_BYMERCHANT_LIST_FAIL
    };

    private BroadcastReceiver merchantcategoryListReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String actionOperatorList = intent.getAction();
            //cancelProgressDialog();
            progressBar.setVisibility(View.INVISIBLE);
            if (actionOperatorList.equals(NetworkConstant.STATUS_FOODODER_BYMERCHANT_LIST_SUCCESS)) {
                AddressResponse menuByaddressResponse = (AddressResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (menuByaddressResponse != null && menuByaddressResponse.restaurant != null) {
                    //cancelProgressDialog();
                    progressBar.setVisibility(View.INVISIBLE);
                    category_List = menuByaddressResponse.restaurant;
                    if (category_List.size()>0) {
                        icNotHere.setVisibility(View.GONE);
                        txtNotHere.setVisibility(View.GONE);
                        txtDetailNotHere.setVisibility(View.GONE);
                        merchant_rest.setVisibility(View.VISIBLE);
                        setupUI();
                    }else{
                        icNotHere.setVisibility(View.VISIBLE);
                        txtNotHere.setVisibility(View.VISIBLE);
                        txtDetailNotHere.setVisibility(View.VISIBLE);
                        merchant_rest.setVisibility(View.GONE);
                    }

                } else {
                    //merchant_rest.setVisibility(View.INVISIBLE);
                    icNotHere.setVisibility(View.VISIBLE);
                    txtNotHere.setVisibility(View.VISIBLE);
                     txtDetailNotHere.setVisibility(View.VISIBLE);
                    merchant_rest.setVisibility(View.GONE);

                    LocalPreferenceUtility.putMerchantId(mContext,"");
                    LocalPreferenceUtility.putMerchantName(mContext,"");
                    //cancelProgressDialog();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            } else if (actionOperatorList.equals(NetworkConstant.STATUS_FOODORDER_BYMERCHANT_LIST_FAIL)) {
               // merchant_horiz.setVisibility(View.GONE);
                merchant_rest.setVisibility(View.GONE);
                //emptyView.setVisibility(View.VISIBLE);
                //cancelProgressDialog();
                progressBar.setVisibility(View.INVISIBLE);
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {

                    showErrorDialog(failureResponse.msg);
                }
                else {
                    showErrorDialog(getString(R.string.error_message));
                }
            }
           /* else{
                cancelProgressDialog();
                showErrorDialog(String.valueOf(R.string.error_message));
            }*/

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

    public void setupUI() {
        //cancelProgressDialog();
        progressBar.setVisibility(View.INVISIBLE);
        merchant_rest.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(mContext, 2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        merchant_rest.setLayoutManager(linearLayoutManager);
       // merchant_horiz.setLayoutManager(linearLayoutManager);
        merchantRestuarantAdapter = new MerchantRestuarantAdapter(mContext, merchant_rest, category_List);
        //merchantRestuarantAdapter = new MerchantRestuarantAdapter(mContext, merchant_horiz, category_List);
       // merchant_horiz.setAdapter(merchantRestuarantAdapter);
        merchant_rest.setAdapter(merchantRestuarantAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart: {
                //startActivity(IntentFactory.createAddTOCartActivity(this));
                Intent intent = new Intent(MerchantActivity.this, AddToCartActivity.class);
                intent.putExtra("flag","normal");
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        if (textCartItemCount != null) {
            if (fillList() == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(fillList(), 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public int fillList() {
        int total=0;
        SQLController controller = new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT count(*) as total from food_cart";

        Cursor data = controller.retrieve(query);
        if(data.getCount()>0){
            data.moveToFirst();
            do{
                total =  data.getInt(data.getColumnIndex("total"));

            }while (data.moveToNext());
        }

        data.close();
        controller.close();
        return total;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foodorder_menu, menu);
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
        } else if (id == R.id.action_cart) {
            startActivity(IntentFactory.createAddTOCartActivity(this));
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
