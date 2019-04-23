package com.wifyee.greenfields.dairyorder;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.foodorder.GPSTracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShippingAddress extends AppCompatActivity {

    EditText name,mobileNo,alternateNo,flatNoBuildingNo,addressEt,city,state,pincode;
    Button continueBtn;
    TextView delivery;
    String Id;
    Double Long,Lat;
    GPSTracker gps;
    List<Address> addresses;
    Geocoder geocoder;
    ProgressBar progressBar;
    View divider;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        progressBar = findViewById(R.id.progressbar);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.contact_no);
        alternateNo = findViewById(R.id.alternate_contact_no);
        addressEt = findViewById(R.id.address);
        flatNoBuildingNo = findViewById(R.id.flat_no_building_no);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pin_code);
        delivery = findViewById(R.id.delivery_status);
        continueBtn = findViewById(R.id.submit);
        CardView cardView = findViewById(R.id.card_view);
        divider = findViewById(R.id.divider1);

        gps = new GPSTracker(this);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gps.canGetLocation()) {
                    divider.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    Lat = gps.getLatitude();
                    Long = gps.getLongitude();
                    getAddress(Lat,Long);

                }else{
                    gps.showSettingsAlert();
                }
            }
        });

        bundle = getIntent().getExtras();

        if(bundle!=null){
            Id = bundle.getString("address_id");
            name.setText(bundle.getString("name"));
            mobileNo.setText(bundle.getString("mobile_no"));
            alternateNo.setText(bundle.getString("alternate_no"));
            city.setText(bundle.getString("city"));
            addressEt.setText(bundle.getString("locality"));
            flatNoBuildingNo.setText(bundle.getString("flat_no"));
            pincode.setText(bundle.getString("pincode"));
            state.setText(bundle.getString("state"));
        }

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDataValid()){
                    saveData(name.getText().toString(),mobileNo.getText().toString(),
                            alternateNo.getText().toString(),city.getText().toString(),
                            addressEt.getText().toString(),flatNoBuildingNo.getText().toString(),
                            pincode.getText().toString(),state.getText().toString());
                }
            }
        });
    }

    public boolean isDataValid(){
        if(name.getText().toString().trim().length()==0){
            showToastWithMessage("Please enter customer name");
            name.requestFocus();
            return false;
        }
        if(mobileNo.getText().toString().trim().length()==0){
            Toast.makeText(getApplicationContext(),"Please enter mobile number", Toast.LENGTH_SHORT).show();
            mobileNo.requestFocus();
            return false;
        }
        if(mobileNo.getText().toString().trim().length()<10){
            Toast.makeText(getApplicationContext(),"Please enter valid mobile number", Toast.LENGTH_SHORT).show();
            mobileNo.requestFocus();
            return false;
        }
        if (city.getText().toString().trim().length() == 0) {
            showToastWithMessage("Please enter city");
            city.requestFocus();
            return false;
        }
        if (addressEt.getText().toString().trim().length() == 0) {
            showToastWithMessage("Please enter locality, area or street");
            addressEt.requestFocus();
            return false;
        }
        if (flatNoBuildingNo.getText().toString().trim().length() == 0) {
            showToastWithMessage("Please enter flat no. , building name");
            flatNoBuildingNo.requestFocus();
            return false;
        }
        if (pincode.getText().toString().trim().length() == 0) {
            showToastWithMessage("Please enter pincode");
            pincode.requestFocus();
            return false;
        }
        if (pincode.getText().toString().trim().length() < 6) {
            showToastWithMessage("Please enter valid pincode");
            pincode.requestFocus();
            return false;
        }

        if (state.getText().toString().trim().length() == 0) {
            showToastWithMessage("Please enter state");
            state.requestFocus();
            return false;
        }
        return true;
    }

    private void getAddress(Double Lat, Double Long){
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Lat, Long, 1);
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String cty = addresses.get(0).getLocality();
                String stat = addresses.get(0).getAdminArea();
                String postalCode = addresses.get(0).getPostalCode();

                addressEt.setText(address);
                city.setText(cty);
                state.setText(stat);
                pincode.setText(postalCode);

                divider.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showToastWithMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        //mdToast.show();
    }

    private void saveData(String name, String mobile_no, String alternateNo, String city, String locality, String flatNo, String pincode, String state){
        SQLController controller = new SQLController(getApplicationContext());
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "";
        if(bundle!=null){
            query = "UPDATE address SET name='"+name+"', mobile_no='"+mobile_no+"', alternate_no='"+alternateNo+"', city='"+city+"', locality='"+locality+"', flat_no='"+flatNo+"', pincode='"+pincode+"', state='"+state+"' where id='"+Id+"'";
        }
        else {
            query = "Insert into address(name,mobile_no,alternate_no,city,locality,flat_no,pincode,state) values " +
                    "('"+name+"','"+mobile_no +"','"+alternateNo+"','"+city +"','"+locality+"','"+flatNo +"','"+pincode+"','"+state+"');";
        }

        Log.w("query",query);

        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.w("added","Record Added successfully");
            if(bundle!=null){
                Intent intent = new Intent(ShippingAddress.this,AddressList.class);
                startActivity(intent);
                finish();
            }else {
                finish();
            }
        }else {
            Log.e("result",result);
        }
        controller.close();
    }
}
