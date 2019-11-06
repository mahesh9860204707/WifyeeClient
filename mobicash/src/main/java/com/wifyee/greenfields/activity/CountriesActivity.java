package com.wifyee.greenfields.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.adapters.CountriesAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.foodorder.CategoryList;
import com.wifyee.greenfields.foodorder.FoodOderListAdapter;
import com.wifyee.greenfields.foodorder.FoodOrderResponse;
import com.wifyee.greenfields.models.CountriesList;
import com.wifyee.greenfields.models.CountriesResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountriesActivity extends AppCompatActivity implements CountriesAdapter.CountriesListener{
    public ProgressDialog progressDialog = null;
    private Context mContext = null;
    List<CountriesList> countries_List;
    RecyclerView countriesRecycler;
    CountriesAdapter countriesAdapter;
    private SpinKitView progressBar;
    LinearLayout emptyView;
    CountriesAdapter.CountriesListener listener;

  /*  @BindView(R.id.toolbar)
    Toolbar mToolbar;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
     //   ButterKnife.bind(this);
        mContext = this;
        listener = this;

       /* Toolbar  mToolbar = (Toolbar) findViewById(R.id.toolbar);
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

            *//*back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });*//*
        }
        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        toolBarTitle.setTypeface(Fonts.getSemiBold(this));*/
        emptyView = findViewById(R.id.empty_view_containers);
        progressBar =  findViewById(R.id.progressbar);
        countriesRecycler = (RecyclerView) findViewById(R.id.country_recyclerview);
    }
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(allfoodOderListReceiver);
    }


    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(allfoodOderListReceiver, new IntentFilter(action));
        }
        //showProgressDialog();
        progressBar.setVisibility(View.VISIBLE);
        MobicashIntentService.startActionGetCountriesList(CountriesActivity.this);
       // MobicashIntentService.startActionAllFoodOrderList(mContext,getMerchantID());
        //setupBadge();

    }

    private String[] broadCastReceiverActionList = {
            NetworkConstant.COUNTRIES_LIST_SUCCESS,
            NetworkConstant.COUNTRIES_LIST_FAILURE
    };


    private BroadcastReceiver allfoodOderListReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String actionOperatorList = intent.getAction();
            progressBar.setVisibility(View.INVISIBLE);
            if (actionOperatorList.equals(NetworkConstant.COUNTRIES_LIST_SUCCESS)) {
              //  CountriesList countriesResponse = (CountriesList) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                ArrayList<CountriesList> countries_List1= (ArrayList<CountriesList>) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (countries_List1 != null ) {
                    //cancelProgressDialog();
                    countries_List = countries_List1;
                    setupUI();
                } else {
                    countriesRecycler.setVisibility(View.INVISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                    //cancelProgressDialog();
                }
            } else if (actionOperatorList.equals(NetworkConstant.COUNTRIES_LIST_FAILURE)) {
                countriesRecycler.setVisibility(View.INVISIBLE);
                emptyView.setVisibility(View.VISIBLE);
                //cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    //cancelProgressDialog();
                    showErrorDialog(failureResponse.msg);
                }
                else {
                    showErrorDialog(getString(R.string.error_message));
                }
            }
        }
    };


    public void setupUI() {
        countriesRecycler.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        ///  RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(mContext);
        countriesRecycler.setLayoutManager(recyclerViewLayoutManager);
        countriesAdapter = new CountriesAdapter(mContext, countries_List,listener);
        countriesRecycler.setAdapter(countriesAdapter);
        //fillListItem();
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
    public void onCountriesClecked(CountriesList item) {
        startActivity(IntentFactory.createUserEnrollmentActivity(getApplicationContext()).putExtra("nationalityId",item.getCountryCode()).putExtra("countryId",item.getId()));
        finish();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            LocalBroadcastManager.getInstance(CountriesActivity.this).unregisterReceiver(allfoodOderListReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* if (pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}
