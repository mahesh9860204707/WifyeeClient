package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.requests.MedicineOrderModel;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public class UploadMedicinePrescriptionActivity extends BaseActivity implements View.OnClickListener,LocationListener{

    private Toolbar mToolbar;
    private ImageButton back;
    private ImageView imageMedicine;
    private Button uploadImage,submitButton;
    protected String latitude,longitude;
    private LocationManager locationManager;
    private String userID="";
    private String imagePath="";
    private MedicineOrderModel medicineOrderModel;
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_UPLOAD_MEDICINE_ORDER_SUCCESS,
            NetworkConstant.STATUS_UPLOAD_MEDICINE_ORDER_FAIL,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_medicine_preciption);

        userID= LocalPreferenceUtility.getUserCode(this);
        imageMedicine= (ImageView) findViewById(R.id.image_medicine);
        submitButton= (Button) findViewById(R.id.submit_medicine);
        submitButton.setOnClickListener(this);
        uploadImage= (Button) findViewById(R.id.upload_medicine);
        uploadImage.setOnClickListener(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        back= (ImageButton) findViewById(R.id.toolbar_back);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        //Getting Current location
        getLocation();
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
    }
    //initialize location provider for getting location
    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v)
    {
        if(v==uploadImage) {
            selectImage(this);
        }
        else if(v==submitButton) {
           if (imagePath.equals("")) {
                Toast.makeText(getApplicationContext(),"Please Upload Medicine Prescription.",Toast.LENGTH_LONG).show();
                return;
            }
            medicineOrderModel=new MedicineOrderModel();
            medicineOrderModel.setUserId(userID);
            medicineOrderModel.setLongLoc(longitude);
            medicineOrderModel.setLotLoc(latitude);
            medicineOrderModel.priscriptionDocument=new File(imagePath);
            medicineOrderModel.setUserType("client");
            showProgressDialog();
            MobicashIntentService.startActionMedicineOrder(this,medicineOrderModel);
        }
    }
    //select image from Camera to set Imageview
    private void selectImage(Context mContext)
    {
        final CharSequence[] options = {"Take Photo","Cancel"};
        // final CharSequence[] options = { "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    try {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    imageMedicine.setImageBitmap(thumbnail);
                    saveImage(thumbnail);
            } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
   //Save Image from Bitmap
    public String saveImage(Bitmap myBitmap) {
         imageMedicine.buildDrawingCache();
         myBitmap = imageMedicine.getDrawingCache();
         OutputStream fOut = null;
        try {
            File root = new File(Environment.getExternalStorageDirectory()+ File.separator + "MedicineFolder" + File.separator);
            root.mkdirs();
            File sdImageMainDirectory = new File(root, currentDateFormat()+".jpg");
            fOut = new FileOutputStream(sdImageMainDirectory);
            imagePath=sdImageMainDirectory.getAbsolutePath();
            Toast.makeText(this, "Correct end now", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error Occured. Please try again later."+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        try {
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            Toast.makeText(this, "Correct end now", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Exception" +e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return "";
    }
    //Getting Current Data and Time
    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this,"Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude(),Toast.LENGTH_SHORT).show();
        latitude=String.valueOf(location.getLatitude());
        longitude=String.valueOf(location.getLongitude());
       /* try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
           Log.e("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude()," Getting Location");
        }catch(Exception e) {
            e.printStackTrace();
        }*/
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(operatorListReceiver, new IntentFilter(action));
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(operatorListReceiver);
    }
    /**
     * Handling broadcast event for user log .
     */
    private BroadcastReceiver operatorListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(NetworkConstant.STATUS_UPLOAD_MEDICINE_ORDER_SUCCESS)) {
                cancelProgressDialog();
                showSuccesDialog(UploadMedicinePrescriptionActivity.this);
            } else if (action != null && action.equals(NetworkConstant.STATUS_UPLOAD_MEDICINE_ORDER_FAIL)) {
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                Timber.w("handleUploadMedicineOrderInfo = > FailureResponse ==>" + new Gson().toJson(failureResponse));
                cancelProgressDialog();
            }
        }
    };
   //Create Success Dialog
    public void showSuccesDialog(final Activity activity) {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        layout.setContentView(R.layout.showpopup__medicine_success);
        // Set dialog title
        layout.setTitle("Medicine Prescription Success");
        Button yes = (Button) layout.findViewById(R.id.yes);
        layout.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    finish();
                    layout.dismiss();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}
