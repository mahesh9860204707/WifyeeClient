package com.wifyee.greenfields.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.dairyorder.OrderSummaryActivity;
import com.wifyee.greenfields.foodorder.AddToCartActivity;
import com.wifyee.greenfields.foodorder.GPSTracker;
import com.wifyee.greenfields.fragments.MyCartFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddAddress extends AppCompatActivity implements PlaceSelectionListener {

    TextView location,change;
    EditText completeAddress;
    Button saveProceed;
    Geocoder geocoder;
    List<Address> addresses;
    private static final String LOG_TAG = "PlaceSelectionListener";
    private static final int REQUEST_SELECT_PLACE = 1000;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    String cart="";
    Double lat1,lng1,lat2,lng2;
    GPSTracker gps;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        location = findViewById(R.id.location);
        change = findViewById(R.id.change);
        completeAddress = findViewById(R.id.complete_address);
        saveProceed = findViewById(R.id.save_proceed);

        saveProceed.setAlpha(0.2f);
        saveProceed.setEnabled(false);

        gps = new GPSTracker(this);

        cart = getIntent().getStringExtra("cart");

        completeAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (completeAddress.getText().toString().length()!=0){
                    saveProceed.setAlpha(1f);
                    saveProceed.setEnabled(true);
                }else {
                    saveProceed.setAlpha(0.2f);
                    saveProceed.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //lat1 =  Double.parseDouble(LocalPreferenceUtility.getLatitude(AddAddress.this));
        //lng1 = Double.parseDouble(LocalPreferenceUtility.getLongitude(AddAddress.this));

        if(cart.equalsIgnoreCase("food_cart")){
            Log.e("if","in if");
            lat1 =  Double.parseDouble(LocalPreferenceUtility.getLatitudeFood(AddAddress.this));
            lng1 = Double.parseDouble(LocalPreferenceUtility.getLongitudeFood(AddAddress.this));
        }else {
            Log.e("else","in else");
            lat1 =  Double.parseDouble(LocalPreferenceUtility.getLatitudeOther(AddAddress.this));
            lng1 = Double.parseDouble(LocalPreferenceUtility.getLongitudeOther(AddAddress.this));
        }

       // getAddress(lat1,lng1);

        if (gps.canGetLocation()) {

            lat2 = gps.getLatitude();
            lng2 = gps.getLongitude();
            getAddress(lat2,lng2);
            float dist = distanceKm(lat1, lng1, lat2, lng2);
            Log.e("DISTANCE", String.valueOf(dist));
            if (dist >= 500) {
                final Dialog layout = new Dialog(AddAddress.this);
                layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                layout.setContentView(R.layout.popup_too_far);
                layout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                layout.show();
                Button confirm = layout.findViewById(R.id.confirm_btn);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layout.dismiss();
                    }
                });
            }
        } else {
            gps.showSettingsAlert();
        }

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_FULLSCREEN)
                            .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                            .build(AddAddress.this);
                    startActivityForResult(intent, REQUEST_SELECT_PLACE);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        saveProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location.getText().toString().trim().length() != 0 && completeAddress.getText().toString().trim().length()!=0) {
                /*float[] result = new float[1];
                Location.distanceBetween(lat2, lng2, lat1, lng1,result);
                double distnce = (double) result[0];*/
                    float dist = distanceKm(lat1, lng1, lat2, lng2);
                    Log.e("DIST", String.valueOf(dist));
                   /* Log.e("lat1", String.valueOf(lat1));
                    Log.e("lng1", String.valueOf(lng1));
                    Log.e("lat2", String.valueOf(lat2));
                    Log.e("lng2", String.valueOf(lng2));*/
                    if (dist <= 500) {
                        if (cart.equalsIgnoreCase("food_cart")) {
                            AddToCartActivity.is_address_set = true;
                            AddToCartActivity.completeAddress = completeAddress.getText().toString();
                            AddToCartActivity.location = location.getText().toString();
                            AddToCartActivity.latitude = String.valueOf(lat2);
                            AddToCartActivity.longitude = String.valueOf(lng2);
                            finish();
                        } else if (cart.equalsIgnoreCase("other_summary_cart")) {
                            OrderSummaryActivity.is_address_set = true;
                            OrderSummaryActivity.completeAddress = completeAddress.getText().toString();
                            OrderSummaryActivity.location = location.getText().toString();
                            OrderSummaryActivity.latitude = String.valueOf(lat2);
                            OrderSummaryActivity.longitude = String.valueOf(lng2);
                            finish();
                        } else if (cart.equalsIgnoreCase("other_cart")) {
                            MyCartFragment.is_address_set = true;
                            MyCartFragment.completeAddress = completeAddress.getText().toString();
                            MyCartFragment.location = location.getText().toString();
                            MyCartFragment.latitude = String.valueOf(lat2);
                            MyCartFragment.longitude = String.valueOf(lng2);
                            finish();
                        }
                    } else {
                        final Dialog layout = new Dialog(AddAddress.this);
                        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        layout.setContentView(R.layout.popup_too_far);
                        layout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        layout.show();
                        Button confirm = layout.findViewById(R.id.confirm_btn);
                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                layout.dismiss();
                            }
                        });
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Please select location",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getAddress(Double Lat, Double Long){
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(Lat, Long, 1);
            if (addresses != null && addresses.size() > 0) {
                String addr = addresses.get(0).getAddressLine(0);
                location.setText(addr);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.e(LOG_TAG, "Place Selected: " + place.getName() + "LatLng"+place.getLatLng());
        location.setText(place.getAddress().toString());
        String LatLng = place.getLatLng().toString().replace("lat/lng: (","").replace(")","");
        String[] arrStr = LatLng.split(",");
        lat2 = Double.parseDouble(arrStr[0]);
        lng2 = Double.parseDouble(arrStr[1]);
        float dist = distanceKm(lat1, lng1, lat2, lng2);
        Log.e("DISTANCE",String.valueOf(dist));
        if (dist >= 500) {
            final Dialog layout = new Dialog(AddAddress.this);
            layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
            layout.setContentView(R.layout.popup_too_far);
            layout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            layout.show();
            Button confirm = layout.findViewById(R.id.confirm_btn);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.dismiss();
                }
            });
        }
    }

    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private float distanceKm(double lat_a, double lng_a, double lat_b, double lng_b){
        Location loc1 = new Location("");

        loc1.setLatitude(lat_a);
        loc1.setLongitude(lng_a);

        Location loc2 = new Location("");
        loc2.setLatitude(lat_b);
        loc2.setLongitude(lng_b);

        return loc1.distanceTo(loc2);
    }

    private double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = (double) (180.f/Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

}
