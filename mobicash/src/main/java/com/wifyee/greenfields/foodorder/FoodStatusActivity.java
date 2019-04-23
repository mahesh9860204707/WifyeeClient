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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.RecyclerTouchListener;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.util.List;

public class FoodStatusActivity extends AppCompatActivity {
    Toolbar mToolbar;
    ImageButton back;
    Context mContext;
    public ProgressDialog progressDialog = null;
    RecyclerView foodorderStatus_recyler;
    FoodItemAdapter foodItemAdapter;
    List<ItemsDeatils> mItemsDetails;
    OrderStatusDetailList mOrderStatusDetailList;
    List<OrdersList> foodOder_List;
    FoodOrderStautsAdapter foodOrderStautsAdapter;
    LinearLayout emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_status);
        mContext = this;
        mToolbar=(Toolbar)findViewById(R.id.mtoolbar);
        back=(ImageButton)findViewById(R.id.toolbar_back);
        emptyView=(LinearLayout)findViewById(R.id.empty_view_containers);
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
        foodorderStatus_recyler=(RecyclerView)findViewById(R.id.orderstauts_recyclerview); 
        MobicashIntentService.startActionGetFoodOrderStatus(mContext, getFoodStatusRequest());
        foodorderStatus_recyler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                foodorderStatus_recyler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                OrdersList dataModel= foodOder_List.get(position);
                MobicashIntentService.startActionGetFoodOrderStatusDetails(mContext, getFoodStatusDetails(dataModel.order_id));
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));




    }
    /*private void onSuccess(String title, FoodOrderStatusResponse foodOrderStatusResponse) {
        if (foodOrderStatusResponse.status.equalsIgnoreCase("1")&& foodOrderStatusResponse.msg.equalsIgnoreCase("SUCCESS")) {
            // custom dialog
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.foodstatusdetail);
            dialog.setTitle(title);
            dialog.setCancelable(false);
            // recharge status
            if (foodOrderStatusResponse.orderStatusList != null) {
                ((TextView) dialog.findViewById(R.id.tv_recharge_status_value)).setText(airtimeResponse.rechargeStatus);
            }
            // reference id
            if (foodOrderStatusResponse.referenceID != null) {
                ((TextView) dialog.findViewById(R.id.tv_reference_id_value)).setText(airtimeResponse.referenceID);
            }
            // to do recharge id
            if (foodOrderStatusResponse.rechargeID != null) {
                ((TextView) dialog.findViewById(R.id.tv_recharge_id_value)).setText(airtimeResponse.rechargeID);
            }
            //recharge_transaction_date
            if (foodOrderStatusResponse.txnDate != null) {
                ((TextView) dialog.findViewById(R.id.tv_recharge_transaction_date_value)).setText(airtimeResponse.txnDate);
            }
            //recharge_number
            if (foodOrderStatusResponse.number != null) {
                ((TextView) dialog.findViewById(R.id.tv_recharge_number_value)).setText(airtimeResponse.number);
            }
            //recharge_amount
            if (foodOrderStatusResponse.amount != null) {
                ((TextView) dialog.findViewById(R.id.tv_recharge_amount_value)).setText(airtimeResponse.amount);
            }

            Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
            // if button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                }
            });

            dialog.show();
        }
        else if(foodOrderStatusResponse.status.equalsIgnoreCase("1")&& foodOrderStatusResponse.msg.equalsIgnoreCase("FAILED"))
        {
          //  showErrorDialog(getString(R.string.worng_Operator));
        }
        else {
            if (airtimeResponse.msg != null) {
                showErrorDialog(airtimeResponse.msg);
            }
        }

    }*/
 private FoodStatusDerailRequest getFoodStatusDetails(String orderid) {
     FoodStatusDerailRequest mFoodStatusDerailRequest=new FoodStatusDerailRequest();
     mFoodStatusDerailRequest.orderID=orderid;

     return mFoodStatusDerailRequest;
 }
    private FoodOrderStatusRequest getFoodStatusRequest() {
        FoodOrderStatusRequest mFoodOrderStatusRequest=new FoodOrderStatusRequest();
        mFoodOrderStatusRequest.UserType="client";
        mFoodOrderStatusRequest.UserId= LocalPreferenceUtility.getUserCode(mContext);
       return mFoodOrderStatusRequest;
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
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(allfoodOrderStautsListReceiver);
    }
    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(allfoodOrderStautsListReceiver, new IntentFilter(action));
        }
        showProgressDialog();
        MobicashIntentService.startActionGetFoodOrderStatus(mContext, getFoodStatusRequest());
    }
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_FOODODER_STATUS_LIST_SUCCESS,
            NetworkConstant.STATUS_FOODORDER_STATUS_LIST_FAIL,
            NetworkConstant.STATUS_FOODODER_STATUS_DEATILS_LIST_SUCCESS,
            NetworkConstant.STATUS_FOODORDER_STATUS_DETAILS_LIST_FAIL
    };
    public void setupUI() {
        foodorderStatus_recyler.setVisibility(View.VISIBLE);
      //  emptyView.setVisibility(View.INVISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        foodorderStatus_recyler.setLayoutManager(linearLayoutManager);
       foodOrderStautsAdapter = new FoodOrderStautsAdapter(mContext, foodorderStatus_recyler, foodOder_List);
        foodorderStatus_recyler.setAdapter(foodOrderStautsAdapter);
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
    private BroadcastReceiver allfoodOrderStautsListReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String actionOperatorList = intent.getAction();
            if (actionOperatorList.equals(NetworkConstant.STATUS_FOODODER_STATUS_LIST_SUCCESS)) {
                FoodOrderStatusResponse foodOrderResponse = (FoodOrderStatusResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (foodOrderResponse != null && foodOrderResponse.orderStatusList != null) {
                    cancelProgressDialog();
                    foodOder_List = foodOrderResponse.orderStatusList;
                    setupUI();
                } else {
                    foodorderStatus_recyler.setVisibility(View.INVISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                    cancelProgressDialog();
                }
            } else if (actionOperatorList.equals(NetworkConstant.STATUS_FOODORDER_STATUS_LIST_FAIL)) {
                foodorderStatus_recyler.setVisibility(View.INVISIBLE);
               // emptyView.setVisibility(View.VISIBLE);
                cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    cancelProgressDialog();
                    showErrorDialog(failureResponse.msg);
                }
                showErrorDialog(String.valueOf(R.string.error_message));
                foodorderStatus_recyler.setVisibility(View.INVISIBLE);
                emptyView.setVisibility(View.VISIBLE);
            }
            if (actionOperatorList.equals(NetworkConstant.STATUS_FOODODER_STATUS_DEATILS_LIST_SUCCESS)) {
                FoodStatusDetailResponse foodStatusDetailResponse = (FoodStatusDetailResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (foodStatusDetailResponse != null && foodStatusDetailResponse.orderStatusDetailList!= null) {
                    cancelProgressDialog();
                    mOrderStatusDetailList=foodStatusDetailResponse.orderStatusDetailList;
                    mItemsDetails = foodStatusDetailResponse.orderStatusDetailList.orderStatusItemsDetailList;
                    onSuccess(mOrderStatusDetailList,mItemsDetails);

                } else {
                   Toast.makeText(mContext,"No Detail found",Toast.LENGTH_SHORT).show();
                    cancelProgressDialog();
                }
            } else if (actionOperatorList.equals(NetworkConstant.STATUS_FOODORDER_STATUS_DETAILS_LIST_FAIL)) {
                foodorderStatus_recyler.setVisibility(View.INVISIBLE);
                // emptyView.setVisibility(View.VISIBLE);
                cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    cancelProgressDialog();
                    showErrorDialog(failureResponse.msg);
                }
                showErrorDialog(String.valueOf(R.string.error_message));
            }

        }
    };

    private void onSuccess(OrderStatusDetailList mOrderStatusDetailList, List<ItemsDeatils> mItemsDetails) {
        if (mOrderStatusDetailList.orderStatusItemsDetailList!=null) {
            // custom dialog
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.foodstatusdetail);
            dialog.setTitle("Detail");
            dialog.setCancelable(false);
            RecyclerView mitem_recycler;
            // recharge status
            if (mOrderStatusDetailList.getOrder_amount() != null) {
                ((TextView) dialog.findViewById(R.id.txt_oderid)).setText(mOrderStatusDetailList.getOrder_id());
            }
            // reference id
            if (mOrderStatusDetailList.getOrder_amount()!=null) {
                ((TextView) dialog.findViewById(R.id.txt_orderamount)).setText(mOrderStatusDetailList.getOrder_amount());
            }
            // to do recharge id
            if (mOrderStatusDetailList.getOrder_on()!=null) {
                ((TextView) dialog.findViewById(R.id.txt_orderon)).setText(mOrderStatusDetailList.getOrder_on());
            }
            //recharge_transaction_date
            if (mOrderStatusDetailList.getOrder_status()!=null) {
                ((TextView) dialog.findViewById(R.id.Order_Status)).setText(mOrderStatusDetailList.getOrder_status());
            }
            if(mItemsDetails.size()!=0)
            {
                mitem_recycler=(RecyclerView)dialog.findViewById(R.id.item_recyler) ;
                mitem_recycler.setVisibility(View.VISIBLE);
                //  emptyView.setVisibility(View.INVISIBLE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                mitem_recycler.setLayoutManager(linearLayoutManager);
                 foodItemAdapter = new FoodItemAdapter(mContext, mitem_recycler, mItemsDetails);
                mitem_recycler.setAdapter(foodItemAdapter);
            }


            Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
            // if button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                }
            });

            dialog.show();
        }
       /* else if(foodOrderStatusResponse.status.equalsIgnoreCase("1")&& foodOrderStatusResponse.msg.equalsIgnoreCase("FAILED"))
        {
            //  showErrorDialog(getString(R.string.worng_Operator));
        }
        else {
            if (airtimeResponse.msg != null) {
                showErrorDialog(airtimeResponse.msg);
            }
        }*/

    }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setVisible(true);
        if (id == R.id.action_cart) {
            startActivity(IntentFactory.createAddTOCartActivity(this));
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
