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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;

import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.dairyorder.OrderSummaryActivity;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class FoodOrderListActivity extends AppCompatActivity implements FoodOderListAdapter.ItemListener{

    public ProgressDialog progressDialog = null;
    private Context mContext = null;
    FoodParentSlugAdapter foodParentSlugAdapter;
    List<CategoryList> category_List;
    ArrayList<String> categorylist=new ArrayList<>();
    Spinner spinner_category;
    Toolbar mToolbar;
    TextView toolBarTitle;
    TextView selected_merchant;
    LinearLayout emptyView;
  //  RecyclerView categoryList_recyclerView;
    GPSTracker gps;
    public static String merchantId;
    public static String merchantName,currentStatus;

    RecyclerView foododerRecycler;
    RelativeLayout btn_addToCart;
    private List<FoodOderList> foodOder_List;
    private FoodOderListAdapter foodOderListAdapter;
    FoodOderListAdapter.ItemListener listener;
    TextView textCartItemCount,viewCart,itemCount,totalPrice,txtTaxes;
    private SpinKitView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_list);
        ButterKnife.bind(this);
        spinner_category =(Spinner)findViewById(R.id.select_category);
       // categoryList_recyclerView=(RecyclerView)findViewById(R.id.parentSlug_recyclerview_main) ;
        mContext = this;
        listener = this;

        emptyView = findViewById(R.id.empty_view_containers);
        mToolbar = findViewById(R.id.mtoolbar);
        toolBarTitle = mToolbar.findViewById(R.id.food_oder);
        selected_merchant=(TextView)findViewById(R.id.merchant_names);
        foododerRecycler = (RecyclerView) findViewById(R.id.food_recyclerview);
        btn_addToCart=(RelativeLayout) findViewById(R.id.add_to_cart);
        viewCart = (TextView) findViewById(R.id.view_cart);
        progressBar =  findViewById(R.id.progressbar);
        itemCount =  findViewById(R.id.item_count);
        totalPrice =  findViewById(R.id.total_price);
        txtTaxes =  findViewById(R.id.txt_taxes);

        //gps = new GPSTracker(mContext);
        //mAllFoodItemsFragment = new AllFoodItemFragment();

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
        viewCart.setTypeface(Fonts.getSemiBold(this));
        totalPrice.setTypeface(Fonts.getSemiBold(this));
        itemCount.setTypeface(Fonts.getRegular(this));
        txtTaxes.setTypeface(Fonts.getRegular(this));
        /*categorylist.clear();
        categorylist.add("All");
        categorylist.add("non-veg");
        categorylist.add("veg");*/

        merchantId = getIntent().getStringExtra("merchantid");
        merchantName = getIntent().getStringExtra("merchantName");
        currentStatus = getIntent().getStringExtra("current_status");

        /*final ArrayAdapter<String> servicedataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorylist);
        servicedataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(servicedataAdapter);*/

        toolBarTitle.setText(merchantName);
        selected_merchant.setText(merchantName);

        if(currentStatus.equals("0")){
            btn_addToCart.setEnabled(false);
            btn_addToCart.setAlpha(0.5f);
            //btn_addToCart.setBackgroundColor(getResources().getColor(R.color.log_gray_text_color));
            viewCart.setText("Currently Unavailable");
        }else {
            btn_addToCart.setEnabled(true);
            viewCart.setText("View Cart");
            btn_addToCart.setAlpha(1f);
            //btn_addToCart.setBackgroundColor(getResources().getColor(R.color.food_btn_color));
        }

        btn_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(fillList()!=0) {
                    startActivity(IntentFactory.createAddTOCartActivity(FoodOrderListActivity.this));
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    //finish();

                //}else{
                    //showErrorDialog("Please add item to cart before place order");
                //}
            }
        });


        //selectFrag("All");

        //    getLatitudeAndLongitude();
       // MobicashIntentService.startActionFoodOrderList(this, );

        /*spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  selectFrag(spinner_category.getSelectedItem().toString());
        //        MobicashIntentService.startActionFoodOrderListByParentSlug(mContext, getFoodCategoryRequest(spinner_category.getSelectedItem().toString()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });*/

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

    /*private AddressRequest getLatitudeAndLongitude() {
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
    }*/

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

/*
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
*/

    private void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
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
        MobicashIntentService.startActionAllFoodOrderList(mContext,getMerchantID());
        //setupBadge();

    }

    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_ALLFOODODER_LIST_SUCCESS,
            NetworkConstant.STATUS_ALLFOODORDER_LIST_FAIL
    };

    private FoodOrderRequest getMerchantID() {
        FoodOrderRequest foodOrderRequest=new FoodOrderRequest();
        //foodOrderRequest.FoodType= LocalPreferenceUtility.getMerchantId(mContext);
        foodOrderRequest.FoodType= merchantId;
        return foodOrderRequest;
    }

    private BroadcastReceiver allfoodOderListReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String actionOperatorList = intent.getAction();
            progressBar.setVisibility(View.INVISIBLE);
            if (actionOperatorList.equals(NetworkConstant.STATUS_ALLFOODODER_LIST_SUCCESS)) {
                FoodOrderResponse foodOrderResponse = (FoodOrderResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (foodOrderResponse != null && foodOrderResponse.FoodOderList != null) {
                    //cancelProgressDialog();
                    foodOder_List = foodOrderResponse.FoodOderList;
                    setupUI();
                } else {
                    foododerRecycler.setVisibility(View.INVISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                    //cancelProgressDialog();
                }
            } else if (actionOperatorList.equals(NetworkConstant.STATUS_ALLFOODORDER_LIST_FAIL)) {
                foododerRecycler.setVisibility(View.INVISIBLE);
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
        foododerRecycler.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        ///  RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(mContext);
        foododerRecycler.setLayoutManager(recyclerViewLayoutManager);
        foodOderListAdapter = new FoodOderListAdapter(mContext, foodOder_List,listener);
        foododerRecycler.setAdapter(foodOderListAdapter);
        fillListItem();
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


    private MenuByParentSlugRequest getFoodCategoryRequest(String category) {
        MenuByParentSlugRequest menuByParentSlugRequest = new MenuByParentSlugRequest();
        if (spinner_category.getSelectedItem().toString().equalsIgnoreCase("veg")) {
            menuByParentSlugRequest.Category = NetworkConstant.TYPE_FOODVEG;
        } else if (spinner_category.getSelectedItem().toString().equalsIgnoreCase("non-veg")){
            menuByParentSlugRequest.Category = NetworkConstant.TYPE_FOODNONVEG;
        }
        return menuByParentSlugRequest;
    }


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

    /*@Override
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
        *//*int id = item.getItemId();
        item.setVisible(true);
        if (id == R.id.action_favourte) {
            startActivity(IntentFactory.createFoodStatusActivity(this));
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            return true;
        } else if (id == R.id.action_cart) {
            startActivity(IntentFactory.createAddTOCartActivity(this));
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

            return true;
        }*//*
        switch (item.getItemId()) {
            case R.id.action_cart: {
                startActivity(IntentFactory.createAddTOCartActivity(this));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void setupBadge() {
        if (textCartItemCount != null) {
            if (fillList() == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                    btn_addToCart.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(fillList(), 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                    btn_addToCart.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public int fillList() {
        int total=0;
        SQLController controller=new SQLController(mContext);
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

    @Override
    public void onBuyCallBack(FoodOderList item, String quantity,int flag) {
        if(flag==0) {
            insert(item.foodImage, item.name, item.itemID, item.description,
                    quantity, item.price,item.getDiscountPrice(),item.getQuantiy(),item.getCategory());
        }
        fillListItem();
    }

    @Override
    public void onRemoveCartCallBack(String id,int flag) {
        if (flag==0) {
            deleteCart(id);
        }
        fillListItem();
    }

    private void insert(String image_path,String item_name,String item_id, String item_description,
                        String quantity,String price,String discount, String qty_half_full, String category){
        SQLController controller = new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "Insert into food_cart(image_path,item_name,item_id,item_description,quantity,price,discount,qty_half_full,category) values " +
                "('"+image_path+"','"+item_name+"','"+item_id+"','"+item_description+"','"+quantity+"','"+price+"','"+discount+"','"+qty_half_full+"','"+category+"');";

        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.e("added","Record Added successfully");
            //setupBadge();
        }else {
            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            Log.e("result",result);
        }
        controller.close();
    }

    public void fillListItem() {
        double calculateAmount=0;
        int totalItem=0;
        SQLController controller=new SQLController(getApplicationContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT * from food_cart";

        Cursor cursor = controller.retrieve(query);
        if(cursor.getCount()>0){
            btn_addToCart.setVisibility(View.VISIBLE);
            cursor.moveToFirst();
            do{
                String quantity = cursor.getString(cursor.getColumnIndex("quantity"));
                String price = cursor.getString(cursor.getColumnIndex("price"));

                calculateAmount = calculateAmount + Double.parseDouble(price) * Integer.parseInt(quantity);
                totalItem = totalItem + Integer.parseInt(quantity);

            }while (cursor.moveToNext());

            totalPrice.setText("₹"+calculateAmount);
            itemCount.setText(""+totalItem+" Items");
            txtTaxes.setText("plus taxes");
        }else {
            btn_addToCart.setVisibility(View.GONE);
        }

        cursor.close();
        controller.close();
    }

    private void deleteCart(String id){
        SQLController controller = new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "DELETE from food_cart where item_id ='"+id+"'";
        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.d("delete","Record delete");
            //setupBadge();
        }else {
            Log.d("result",result);
        }
        controller.close();
    }
}
