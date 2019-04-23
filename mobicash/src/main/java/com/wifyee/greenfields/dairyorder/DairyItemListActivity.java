package com.wifyee.greenfields.dairyorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifyee.greenfields.R;
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
    private Button btnPlaceOrder;
    private String  itemId;
    private String  merId;
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
    TextView textCartItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_item_list);
        ButterKnife.bind(this);
        mContext = this;
        listener = this;
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.item_title);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }

        btnPlaceOrder = (Button)findViewById(R.id.btn_place_order);
        itemId = getIntent().getStringExtra("data");
        merId = getIntent().getStringExtra("id");
        emptyListView = (LinearLayout) findViewById(R.id.empty_list);
        recyclerView = (RecyclerView) findViewById(R.id.dairy_list_item);
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
                if(orderItem.size()>0) {
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
        DairyProductIntentService.startActionListDairyItem(this,itemId);
    }

    /**
     * Handling broadcast event for user Signup .
     */
    private BroadcastReceiver getMerchantListDairyItemReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            cancelProgressDialog();
            try {
                if (action.equals(DairyNetworkConstant.DAIRY_LIST_ITEM_STATUS_SUCCESS)) {
                    ArrayList<DairyProductListItem> item = intent.getParcelableArrayListExtra(NetworkConstant.EXTRA_DATA);
                    DairyListItemAdapter adapter = new DairyListItemAdapter(DairyItemListActivity.this, item, listener);
                    recyclerView.setAdapter(adapter);
                }else if(action.equals(DairyNetworkConstant.DAIRY_LIST_ITEM_STATUS_FAILURE)){

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
                selectedItem = new ArrayList<>();;
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
    public void onBuyCallBack(final DairyProductListItem item,final String itemId,final String quantity,final String quantityUnit) {
            //addToCart();
           // selectedItem.add(item);
            PlaceOrderData data = new PlaceOrderData();
            data.setItemImagePath(item.getItemImagePath());
            data.setItemName(item.getItemName());
            data.setItemType(item.getItemQuality());
            data.setMerchantId(merId);
            data.setItemId(itemId);
            data.setQuantity(quantity);
            data.setQuantityUnit(quantityUnit);
            data.setOrderPrice(item.getItemPrice());
            orderItem.add(data);

            insert(item.getItemImagePath(), item.getItemName(), item.getItemQuality(), merId, itemId,
                    quantity, quantityUnit, item.getItemPrice());
    }

    @Override
    public void onRemoveCartCallBack(final String topicId) {
            //removeFromCart();
            deleteCart(topicId);
            removeDataFromOrderItem(topicId);
            removeSelectedItem(topicId);
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
        SQLController controller=new SQLController(getApplicationContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT count(*) as total from cart";

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
                        String item_id, String quantity, String unit,String price){
        SQLController controller = new SQLController(getApplicationContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "Insert into cart(image_path,item_name,item_type,merchant_id,item_id,quantity,unit,price) values " +
                "('"+image_path+"','"+item_name+"','"+item_type+"','"+merchant_id+"','"+item_id+"','"+quantity+"','"+unit+"','"+price+"');";

        Log.e("query",query);

        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.e("added","Record Added successfully");
            setupBadge();
        }else {
            Log.e("result",result);
        }
        controller.close();
    }

    private void deleteCart(String id){
        SQLController controller = new SQLController(getApplicationContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "DELETE from cart where item_id ='"+id+"'";
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
