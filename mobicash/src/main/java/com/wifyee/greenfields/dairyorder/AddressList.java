package com.wifyee.greenfields.dairyorder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;

import java.util.ArrayList;

public class AddressList extends AppCompatActivity {

    AddressAdapter adapter;
    ArrayList<AddressModal> listArray = new ArrayList<>();
    RecyclerView recyclerView;
    Button deliverHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new AddressAdapter(getApplicationContext(), listArray);
        deliverHere = findViewById(R.id.deliver_here);


        CardView cardView = findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressList.this,ShippingAddress.class);
                startActivity(intent);
                //finish();
            }
        });

        deliverHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<adapter.getData().size();i++){
                    AddressModal modal = adapter.getData().get(i);
                    String id = modal.getId();
                    String name = modal.getName();
                    String mobile = modal.getMobileNo();
                    String alternate = modal.getAlternateNo();
                    String city = modal.getCity();
                    String locality = modal.getLocality();
                    String flat = modal.getFlatNo();
                    String pincode = modal.getPincode();
                    String state = modal.getState();

                    OrderSummaryDetails.fullname = name;
                    OrderSummaryDetails.mobile_no = mobile;
                    OrderSummaryDetails.alternate_no = alternate;
                    //BuyPaymentProceed.Address = flat+", "+locality+", "+city+", "+state+" - "+pincode;
                    OrderSummaryDetails.flat_no = flat;
                    OrderSummaryDetails.locality= locality;
                    OrderSummaryDetails.city = city;
                    OrderSummaryDetails.state = state;
                    OrderSummaryDetails.pincode =  pincode;
                    OrderSummaryDetails.addressChange = true;
                    finish();

                }
            }
        });
    }

    @Override
    protected void onResume() {
        if(listArray!=null){
            listArray.clear();
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        AddressFeed();
        super.onResume();
    }

    public void AddressFeed() {
        SQLController controller=new SQLController(getApplicationContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT * from address order by id desc";

        Cursor data = controller.retrieve(query);
        if(data.getCount()>0){
            data.moveToFirst();
            do{
                String id = data.getString(data.getColumnIndex("id"));
                String fullname =  data.getString(data.getColumnIndex("name"));
                String mobile_no =  data.getString(data.getColumnIndex("mobile_no"));
                String alternate_no =  data.getString(data.getColumnIndex("alternate_no"));
                String city = data.getString(data.getColumnIndex("city"));
                String locality = data.getString(data.getColumnIndex("locality"));
                String flat_no = data.getString(data.getColumnIndex("flat_no"));
                String pincode = data.getString(data.getColumnIndex("pincode"));
                String state = data.getString(data.getColumnIndex("state"));

                listArray.add(new AddressModal(id,fullname,mobile_no,alternate_no,city,locality,flat_no,pincode,state));
                Log.w("data ","Data Fetched");

            }while (data.moveToNext());
        }

        data.close();
        controller.close();
    }
}
