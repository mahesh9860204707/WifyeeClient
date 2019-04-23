package com.wifyee.greenfields.foodorder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;

import com.wifyee.greenfields.constants.NetworkConstant;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class FoodOrderListActivity extends AppCompatActivity
{

    public ProgressDialog progressDialog = null;
    private Context mContext = null;
    FoodParentSlugAdapter foodParentSlugAdapter;
    List<CategoryList> category_List;
    private AllFoodItemFragment mAllFoodItemsFragment;
    ArrayList<String> categorylist=new ArrayList<>();
    Spinner spinner_category;
    Toolbar mToolbar;
    ImageButton back;
    TextView selected_merchant;
    LinearLayout emptyView;
  //  RecyclerView categoryList_recyclerView;
    GPSTracker gps;
    public static String merchantId;
    public static String merchantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_list);
        ButterKnife.bind(this);
        spinner_category =(Spinner)findViewById(R.id.select_category);
       // categoryList_recyclerView=(RecyclerView)findViewById(R.id.parentSlug_recyclerview_main) ;
        mContext = this;
        emptyView=(LinearLayout)findViewById(R.id.empty_view_containers);
        mToolbar=(Toolbar)findViewById(R.id.mtoolbar);
        back=(ImageButton)findViewById(R.id.toolbar_back);
        selected_merchant=(TextView)findViewById(R.id.merchant_names);

        gps = new GPSTracker(mContext);
        mAllFoodItemsFragment = new AllFoodItemFragment();

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
        categorylist.clear();
        categorylist.add("All");
        categorylist.add("non-veg");
        categorylist.add("veg");

        merchantId = getIntent().getStringExtra("merchantid");
        merchantName = getIntent().getStringExtra("merchantName");

        final ArrayAdapter<String> servicedataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorylist);
        servicedataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(servicedataAdapter);
        selected_merchant.setText(merchantName);

        selectFrag("All");

        //    getLatitudeAndLongitude();
       // MobicashIntentService.startActionFoodOrderList(this, );

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  selectFrag(spinner_category.getSelectedItem().toString());
        //        MobicashIntentService.startActionFoodOrderListByParentSlug(mContext, getFoodCategoryRequest(spinner_category.getSelectedItem().toString()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /*categoryList_recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext,
                categoryList_recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                String title1 = ((TextView) categoryList_recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tv_categoryName)).getText().toString();
            //    Toast.makeText(mContext, "Parent Slug :"+title1,Toast.LENGTH_LONG).show();
                for(int i=0; i<category_List.size();i++)
                {
                    if(category_List.get(i).categoryName.toString().equalsIgnoreCase(title1))
                    {
                        parentSlugFragment(category_List.get(i).categorySlug.toString());
                        Toast.makeText(mContext, "Parent Slug :"+category_List.get(i).categorySlug.toString(),Toast.LENGTH_LONG).show();
                        break;
                    }
                }
           }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));*/
    }

    private AddressRequest getLatitudeAndLongitude() {
        AddressRequest addressRequest = new AddressRequest();
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            addressRequest.latitude = String.valueOf(latitude);
            addressRequest.longitude = String.valueOf(longitude);
            Toast.makeText(mContext,latitude+"-"+longitude,Toast.LENGTH_SHORT).show();
        } else {
            addressRequest.latitude = "";
            addressRequest.longitude = "";
        }
        return addressRequest;
    }

    public void parentSlugFragment(String parentCategory) {
        Fragment foodSlugFragment = null;
        Bundle bundle =new Bundle();
        bundle.putString("message", parentCategory);
        bundle.putString("merchantid", LocalPreferenceUtility.getMerchantId(mContext));
        foodSlugFragment = new AllNonVegFoodItemFragment();
        foodSlugFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, foodSlugFragment);
        transaction.commit();
    }

    public void selectFrag(String view) {
        Fragment foodFragment = null;
        if(view.equalsIgnoreCase("All")) {
            Bundle bundle =new Bundle();
            bundle.putString("message", "all");
            bundle.putString("merchantid",merchantId);
            foodFragment = new AllFoodItemFragment();
            foodFragment.setArguments(bundle);
        }
        else if(view.equalsIgnoreCase("veg")) {
            Bundle bundle =new Bundle();
            bundle.putString("message", "veg");
            bundle.putString("merchantid",merchantId);
            foodFragment = new AllNonVegFoodItemFragment();
            foodFragment.setArguments(bundle);
        }
        else if(view.equalsIgnoreCase("Non-veg")){
            Bundle bundle =new Bundle();
            bundle.putString("message", "Non-veg");
            //bundle.putString("merchantid", LocalPreferenceUtility.getMerchantId(mContext));
            bundle.putString("merchantid", merchantId);
            foodFragment = new AllNonVegFoodItemFragment();
            foodFragment.setArguments(bundle);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, foodFragment);
        transaction.commit();
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
        /*LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(categoryListReceiver, new IntentFilter(action));
        }
      //  showProgressDialog();

        MobicashIntentService.startActionFoodOrderListByParentSlug(mContext, getFoodCategoryRequest(spinner_category.getSelectedItem().toString()));*/
    }

    private MenuByParentSlugRequest getFoodCategoryRequest(String category) {
        MenuByParentSlugRequest menuByParentSlugRequest = new MenuByParentSlugRequest();
        if (spinner_category.getSelectedItem().toString().equalsIgnoreCase("veg")) {
            menuByParentSlugRequest.Category = NetworkConstant.TYPE_FOODVEG;
        } else if (spinner_category.getSelectedItem().toString().equalsIgnoreCase("non-veg")){
            menuByParentSlugRequest.Category = NetworkConstant.TYPE_FOODNONVEG;
        }
        return menuByParentSlugRequest;
    }

    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_FOODODER_BYPARENTSLUG_LIST_SUCCESS,
            NetworkConstant.STATUS_FOODORDER_BYPARENTSLUG_LIST_FAIL
    };

    /*public void setupUI() {
        categoryList_recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        categoryList_recyclerView.setLayoutManager(linearLayoutManager);
        foodParentSlugAdapter = new FoodParentSlugAdapter(mContext, categoryList_recyclerView, category_List);
        categoryList_recyclerView.setAdapter(foodParentSlugAdapter);
    }*/

    private BroadcastReceiver categoryListReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String actionOperatorList = intent.getAction();
            /*if (actionOperatorList.equals(NetworkConstant.STATUS_FOODODER_BYPARENTSLUG_LIST_SUCCESS)) {
                MenuByParentSlugResponse menuByParentSlugResponse = (MenuByParentSlugResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (menuByParentSlugResponse != null && menuByParentSlugResponse.categoryList != null) {
                    cancelProgressDialog();
                    category_List = menuByParentSlugResponse.categoryList;
                    setupUI();

                } else {
                    categoryList_recyclerView.setVisibility(View.INVISIBLE);
                   emptyView.setVisibility(View.VISIBLE);
                 //   showErrorDialog(getString(R.string.error_message));
                    cancelProgressDialog();
                }

            } else if (actionOperatorList.equals(NetworkConstant.STATUS_FOODORDER_BYPARENTSLUG_LIST_FAIL)) {
                categoryList_recyclerView.setVisibility(View.GONE);
               emptyView.setVisibility(View.VISIBLE);
                cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    cancelProgressDialog();
                  //  showErrorDialog(failureResponse.msg);
                }
                else {
                   // showErrorDialog(getString(R.string.error_message));
                }

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
 @Override
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
    }
}
