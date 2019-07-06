package com.wifyee.greenfields.dairyorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.activity.BaseActivity;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.wifyee.greenfields.dairyorder.CounterCart.setBadgeCount;

public class DairyItemListActivity extends BaseActivity implements DairyListItemAdapter.ItemListener,View.OnClickListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private LayerDrawable icon;
    private AlertDialog searchDialog;
    private Context mContext = null;
    private int totalCartCount;
    private LinearLayout emptyListView;
    private RecyclerView recyclerView;
    private RelativeLayout btnPlaceOrder;
    private String  itemId,merId,categoryId="",merchantType="",longitude="",latitude="";

    DairyListItemAdapter.ItemListener listener ;
    ArrayList<DairyProductListItem> selectedItem = new ArrayList<>();
    ArrayList<PlaceOrderData> orderItem = new ArrayList<>();
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            DairyNetworkConstant.DAIRY_LIST_ITEM_STATUS_SUCCESS,
            DairyNetworkConstant.DAIRY_LIST_ITEM_STATUS_FAILURE,
    };

    ImageView icNotHere;
    TextView textCartItemCount,txtNotHere,txtDetailNotHere,viewCart,itemCount,totalPrice,txtTaxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_item_list);
        ButterKnife.bind(this);
        mContext = this;
        listener = this;

        TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);

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

        btnPlaceOrder = findViewById(R.id.btn_place_order);
        //emptyListView = (LinearLayout) findViewById(R.id.empty_list);
        recyclerView =  findViewById(R.id.dairy_list_item);
        icNotHere = findViewById(R.id.ic_not_here);
        txtNotHere = findViewById(R.id.txt_we_are_not);
        txtDetailNotHere = findViewById(R.id.txt_detail_we_are_not);
        viewCart =  findViewById(R.id.view_cart);
        //progressBar =  findViewById(R.id.progressbar);
        itemCount =  findViewById(R.id.item_count);
        totalPrice =  findViewById(R.id.total_price);
        txtTaxes =  findViewById(R.id.txt_taxes);

        toolbarTitle.setTypeface(Fonts.getSemiBold(this));
        txtNotHere.setTypeface(Fonts.getSemiBold(this));
        txtDetailNotHere.setTypeface(Fonts.getRegular(this));
        viewCart.setTypeface(Fonts.getSemiBold(this));
        totalPrice.setTypeface(Fonts.getSemiBold(this));
        itemCount.setTypeface(Fonts.getRegular(this));
        txtTaxes.setTypeface(Fonts.getRegular(this));

        itemId = getIntent().getStringExtra("data");
        categoryId = getIntent().getStringExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID);
        merchantType = getIntent().getStringExtra(NetworkConstant.EXTRA_DATA);
        latitude = LocalPreferenceUtility.getLatitude(DairyItemListActivity.this);
        longitude = LocalPreferenceUtility.getLongitude(DairyItemListActivity.this);

        merId = itemId;

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        btnPlaceOrder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.btn_place_order:
                if(countCart()!=0) {
                    Intent intent = new Intent(this, OrderSummaryActivity.class);
                    Bundle data = new Bundle();
                    //data.putParcelableArrayList("data", orderItem);
                    //intent.putExtra("main_data", data);
                    startActivity(intent);
                    selectedItem = new ArrayList<>();;
                    orderItem = new ArrayList<>();
                    //finish();
                }else{
                    showErrorDialog("Please add item to cart before place order");
                }
                break;
        }
    }

    public void fillListItem() {
        Log.e("Method","Function Calling");
        double calculateAmount=0;
        int totalItem=0;
        SQLController controller=new SQLController(getApplicationContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT * from cart_item";

        Cursor cursor = controller.retrieve(query);
        if(cursor.getCount()>0){
            btnPlaceOrder.setVisibility(View.VISIBLE);
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
            btnPlaceOrder.setVisibility(View.GONE);
        }
        cursor.close();
        controller.close();
    }

    public int countCart() {
        int total=0;
        SQLController controller=new SQLController(getApplicationContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT count(*) as total from cart_item";

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
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getMerchantListDairyItemReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(getMerchantListDairyItemReceiver, new IntentFilter(action));
        }
        showProgressDialog();
        DairyProductIntentService.startActionListDairyItem(this,itemId,categoryId,merchantType,latitude,longitude);

        setupBadge();
    }

    /**
     * Handling broadcast event for user Signup .
     */
    private BroadcastReceiver getMerchantListDairyItemReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //cancelProgressDialog();
            try {
                if (action.equals(DairyNetworkConstant.DAIRY_LIST_ITEM_STATUS_SUCCESS)) {
                    ArrayList<DairyProductListItem> item = intent.getParcelableArrayListExtra(NetworkConstant.EXTRA_DATA);
                    if (item.size()>0) {
                        icNotHere.setVisibility(View.GONE);
                        txtNotHere.setVisibility(View.GONE);
                        txtDetailNotHere.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        btnPlaceOrder.setVisibility(View.VISIBLE);
                        DairyListItemAdapter adapter = new DairyListItemAdapter(DairyItemListActivity.this, item, listener);
                        cancelProgressDialog();
                        recyclerView.setAdapter(adapter);
                        fillListItem();
                    }else {
                        cancelProgressDialog();
                        icNotHere.setVisibility(View.VISIBLE);
                        txtNotHere.setVisibility(View.VISIBLE);
                        txtDetailNotHere.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        btnPlaceOrder.setVisibility(View.GONE);
                    }
                }else if(action.equals(DairyNetworkConstant.DAIRY_LIST_ITEM_STATUS_FAILURE)){
                    cancelProgressDialog();
                    Toast.makeText(mContext,"Something went wrong! Please try again",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Timber.e("Exception caught in getMerchantListDairyReceiver " + e.getMessage());
            }
        }
    };


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dairy_menu_cart, menu);
        MenuItem itemCart  =  menu.findItem(R.id.action_add_to_cart);
        icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(this, icon, "0");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add_to_cart){
            //if(totalCartCount !=0) {
                Intent intent = new Intent(this, OrderSummaryActivity.class);
                Bundle data = new Bundle();
                data.putParcelableArrayList("data", orderItem);
                intent.putExtra("main_data", data);
                startActivity(intent);
                selectedItem = new ArrayList<>();;
                orderItem = new ArrayList<>();
            //}
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/

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

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cart: {
                Intent intent = new Intent(this, OrderSummaryActivity.class);
                Bundle data = new Bundle();
                data.putParcelableArrayList("data", orderItem);
                intent.putExtra("main_data", data);
                startActivity(intent);
                selectedItem = new ArrayList<>();
                orderItem = new ArrayList<>();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemClick(DairyProductListItem item) {
       //TODO
    }

    @Override
    public void onBuyCallBack(final DairyProductListItem item,final String itemId,
                              final String quantity,final String quantityUnit,int flag) {
        if(flag == 0) {
            PlaceOrderData data = new PlaceOrderData();
            data.setItemImagePath(item.getItemImagePath());
            data.setItemName(item.getItemName());
            data.setItemType(item.getItemQuality());
            data.setMerchantId(item.getMerchantId());
            data.setItemId(itemId);
            data.setQuantity(quantity);
            data.setQuantityUnit(quantityUnit);
            data.setOrderPrice(item.getItemPrice());
            orderItem.add(data);

            insert(item.getItemImagePath(), item.getItemName(), item.getItemQuality(), item.getMerchantId(), itemId,
                    quantity, quantityUnit, item.getItemPrice(), item.getItemDiscount());
        }

        fillListItem();
    }

    @Override
    public void onRemoveCartCallBack(final String topicId,int flag) {
            //removeFromCart();
        if (flag==0) {
            deleteCart(topicId);
            removeDataFromOrderItem(topicId);
            removeSelectedItem(topicId);
        }
        fillListItem();
    }

    private void removeDataFromOrderItem(String topicId){
        int size = orderItem.size();
        if(size>0){
            for(int i=0;i<size;i++){
                if(orderItem.get(i).getItemId().equals(topicId)){
                    orderItem.remove(i);
                }
            }
        }
    }

    private void removeSelectedItem(String topicId){
        int size = orderItem.size();
        if(size>0){
            for(int i=0;i<size;i++){
                if(orderItem.get(i).getItemId().equals(topicId)){
                    orderItem.remove(i);
                }
            }
        }
    }

    public void addToCart() {
        if(totalCartCount>=0) {
            totalCartCount++;
            setBadgeCount(getApplicationContext(), icon, String.valueOf(totalCartCount));
        }
    }

    public void removeFromCart(){
        if(totalCartCount>0) {
            totalCartCount--;
            setBadgeCount(getApplicationContext(), icon, String.valueOf(totalCartCount));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
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
        SQLController controller=new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT count(*) as total from cart_item";

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

    private void insert(String image_path,String item_name, String item_type, String merchant_id,
                        String item_id, String quantity, String unit,String price,String discount){
        SQLController controller = new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "Insert into cart_item(image_path,item_name,item_type,merchant_id,item_id,quantity,unit,price,discount) values " +
                "('"+image_path+"','"+item_name+"','"+item_type+"','"+merchant_id+"','"+item_id+"','"+quantity+"','"+unit+"','"+price+"','"+discount+"');";

        Log.e("query",query);

        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.e("added","Record Added successfully");
            setupBadge();
        }else {
            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            Log.e("result",result);
        }
        controller.close();
    }

    private void deleteCart(String id){
        SQLController controller = new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "DELETE from cart_item where item_id ='"+id+"'";
        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.d("delete","Record delete");
            setupBadge();
        }else {
            Log.d("result",result);
        }
        controller.close();
    }
}
