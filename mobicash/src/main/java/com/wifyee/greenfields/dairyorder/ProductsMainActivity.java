package com.wifyee.greenfields.dairyorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.activity.BaseActivity;
import com.wifyee.greenfields.constants.NetworkConstant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsMainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.grid_view_image_text)
    GridView gridView;

    @BindView(R.id.empty_text_view)
    TextView tvEmptyView;

    private ArrayList<MainCategoryModel> data;

    private String[] broadCastReceiverActionList = {
            DairyNetworkConstant.MAIN_CATEGORY_LIST_ITEM_SUCCESS,
            DairyNetworkConstant.MAIN_CATEGORY_LIST_ITEM_FAIL
    };

    String[] gridViewString = null;
    int[] gridViewImageId = null;
            //{
//            R.drawable.apple, R.drawable.food_ordering, R.drawable.food_ordering,
//            R.drawable.food_ordering, R.drawable.food_ordering, R.drawable.food_ordering,
//            R.drawable.food_ordering
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_main);
        ButterKnife.bind(this);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.product_title);

            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                if(data!=null) {
                    Intent productIntent = IntentFactory.createDairyProductActivity(ProductsMainActivity.this);
                    productIntent.putExtra(NetworkConstant.EXTRA_DATA, data.get(i).getCatId() );
                    productIntent.putExtra(NetworkConstant.EXTRA_DATA1, data.get(i).getCatName() );
                    startActivity(productIntent);
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                }

//                if(gridViewString[+i].equals("Dairy")){
//                    startActivity(IntentFactory.createDairyProductActivity(ProductsMainActivity.this));
//                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//                }else if(gridViewString[+i].equals("Medicine")){
//                    startActivity(IntentFactory.createMedicinePreciptionActivity(ProductsMainActivity.this));
//                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//                } else if(gridViewString[+i].equals("Food")){
//                    startActivity(IntentFactory.createAddOderFoodActivity(ProductsMainActivity.this));
//                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getCategoryBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(getCategoryBroadcastReceiver, new IntentFilter(action));
        }
        showProgressDialog();
        DairyProductIntentService.startActionMerchantTypeList(this);
    }

    private BroadcastReceiver getCategoryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            cancelProgressDialog();
            if (action.equals(DairyNetworkConstant.MAIN_CATEGORY_LIST_ITEM_SUCCESS)) {
                data = intent.getParcelableArrayListExtra(NetworkConstant.EXTRA_DATA);
                int size = data.size();
                gridViewString = new String[size];
                gridViewImageId = new int[size];
                for(int i =0;i<size;i++){
                    gridViewString[i] = data.get(i).getCatName();
                    gridViewImageId[i] = R.drawable.food_ordering;
                }
                CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(ProductsMainActivity.this, gridViewString, gridViewImageId);
                gridView = (GridView) findViewById(R.id.grid_view_image_text);
                gridView.setAdapter(adapterViewAndroid);
                tvEmptyView.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
            }else if(action.equals(DairyNetworkConstant.MAIN_CATEGORY_LIST_ITEM_FAIL)){
                tvEmptyView.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
            }
        }
    };
}

