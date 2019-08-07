package com.wifyee.greenfields.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.wifyee.greenfields.BuildConfig;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.MobicashApplication;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.CircularNetworkImageView;
import com.wifyee.greenfields.Utils.CustomTypefaceSpan;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.Constants;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.constants.WifiConstant;
import com.wifyee.greenfields.dairyorder.OrderSummaryActivity;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.foodorder.GPSTracker;
import com.wifyee.greenfields.fragments.BankTransferFragment;
import com.wifyee.greenfields.fragments.HomeFragment;
import com.wifyee.greenfields.fragments.LogFragment;
import com.wifyee.greenfields.fragments.LogFragment.LogFragmentListener;
import com.wifyee.greenfields.fragments.ManageBeneficiaryFragment;
import com.wifyee.greenfields.fragments.MyCartFragment;
import com.wifyee.greenfields.fragments.OrderFragment;
import com.wifyee.greenfields.fragments.ProductFragment;
import com.wifyee.greenfields.fragments.ProfileFragment;
import com.wifyee.greenfields.interfaces.FragmentInterface;
import com.wifyee.greenfields.mapper.ModelMapper;
import com.wifyee.greenfields.models.requests.ServerRequest;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.GetClientProfileInfoResponse;
import com.wifyee.greenfields.models.response.GetProfileInfo;
import com.wifyee.greenfields.models.response.LoginResponse;
import com.wifyee.greenfields.models.response.PlanDataSummary;
import com.wifyee.greenfields.models.response.ServerResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import timber.log.Timber;


public class MobicashDashBoardActivity extends BaseActivity implements LogFragmentListener,
        ProfileFragment.ProfileFragmentListner, BankTransferFragment.BankTransferFragmentListener,
        View.OnClickListener,NavigationView.OnNavigationItemSelectedListener, PlaceSelectionListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Context mContext = null;
    private TabLayout mTabLayout;
    private ImageView setting, logout;
    private CircularNetworkImageView ispLogoImage;
    private PlanDataSummary planDataSummary;
    private ServerResponse serverResponse;
    private ServerRequest serverRequest;
    private int[] mTabsIcons = {
            R.mipmap.home_icon,
            R.drawable.ic_my_order,
            R.drawable.cart,
            // R.mipmap.product_icon,
            R.mipmap.log_icon,
            R.mipmap.profile_icon
    };

    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_SERVER_USAGE_DATA_SUCCESS,
            NetworkConstant.STATUS_SERVER_USAGE_DATA_FAIL,

    };

    DrawerLayout drawer;
    NavigationView navigationView;
    private static final String LOG_TAG = "PlaceSelectionListener";
    private static final int REQUEST_SELECT_PLACE = 1000;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    public static TextView locationTxt;
    Geocoder geocoder;
    List<Address> addresses;
    String currentVersion,newVersion;
    LinearLayout ll;
    private static final String TAG = MobicashDashBoardActivity.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();
    private FusedLocationProviderClient fusedLocationClient;
    String latitude,longitude;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    MyPagerAdapter pagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //UpdateHelper.with(this).onUpdateNeeded(this).check();

        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest.setInterval(NetworkConstant.LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(NetworkConstant.FASTEST_LOCATION_INTERVAL);
        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        mLocationRequest.setPriority(priority);
        mLocationClient.connect();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                serverRequest=new ServerRequest();
                serverRequest.req_username= LocalPreferenceUtility.getUserMobileNumber(MobicashDashBoardActivity.this) + "@wifyee";
                serverRequest.req_action="get_one";
                serverRequest.req_customercode="95629568";
                serverRequest.req_timeinterval="1 day";
               /// MobicashIntentService.startActionToServer(MobicashDashBoardActivity.this,serverRequest);
                callApiWifiSoft(MobicashDashBoardActivity.this,serverRequest);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final RelativeLayout content =  findViewById(R.id.content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LinearLayout llLocation = toolbar.findViewById(R.id.ll_loc);
        locationTxt = toolbar.findViewById(R.id.location);
        TextView txtLocation = toolbar.findViewById(R.id.txt);
        txtLocation.setTypeface(Fonts.getRegular(this));
        locationTxt.setTypeface(Fonts.getSemiBold(this));

        llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_FULLSCREEN)
                            .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                            .build(MobicashDashBoardActivity.this);
                    startActivityForResult(intent, REQUEST_SELECT_PLACE);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
                //content.setScaleX(slideOffset);
                //content.setScaleY(slideOffset);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.secondaryPrimary));

        //drawer.setBackgroundColor(getResources().getColor(R.color.secondaryPrimary));

        navigationView = findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }

        navigationView.setNavigationItemSelectedListener(this);

        setting = (ImageView) findViewById(R.id.settings);
        setting.setOnClickListener(this);
        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(this);
        ispLogoImage= (CircularNetworkImageView) findViewById(R.id.isp_logo);
        // this.registerReceiver(this.dataUsage, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        //getSupportActionBar().hide();
        ButterKnife.bind(this);
        mContext = this;

        //addFireBaseEvent();
        // Setup the viewPager
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
        }
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);
            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }

            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    FragmentInterface fragmentInterface = (FragmentInterface) pagerAdapter.instantiateItem(viewPager,1);
                    fragmentInterface.fragmentBecameVisible();

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            if (viewPager != null) {
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
            }
            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
        }

        System.gc();

        //gps = new GPSTracker(MobicashDashBoardActivity.this);

        /*if (latitude!=null && longitude!=null) {
            LocalPreferenceUtility.putLatitude(getApplicationContext(),String.valueOf(latitude));
            LocalPreferenceUtility.putLongitude(getApplicationContext(),String.valueOf(longitude));
            String loc = getAddress(Double.parseDouble(latitude),Double.parseDouble(longitude));
            Log.e("location",loc);
            locationTxt.setText(loc);
        } else {
           // gps.showSettingsAlert();
        }*/

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            CheckVersion(currentVersion);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }*/

        FirebaseMessaging.getInstance().subscribeToTopic("All");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String msg = task.getResult().getToken();
                        sendRegistrationToServer(msg);
                        Log.d(TAG, msg);
                    }
                });

    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.e(LOG_TAG, "Place Selected: " + place.getName() + "LatLng"+place.getLatLng());
        locationTxt.setText(place.getAddress().toString());
        String LatLng = place.getLatLng().toString().replace("lat/lng: (","").replace(")","");
        String[] arrStr = LatLng.split(",");
        LocalPreferenceUtility.putLatitude(getApplicationContext(),String.valueOf(arrStr[0]));
        LocalPreferenceUtility.putLongitude(getApplicationContext(),String.valueOf(arrStr[1]));
        String pincode = getPincode(Double.parseDouble(arrStr[0]),Double.parseDouble(arrStr[1]));
        LocalPreferenceUtility.putCurrentPincode(getApplicationContext(),pincode);
        Fragment fragment = pagerAdapter.getFragment(mTabLayout
                .getSelectedTabPosition());
        ((HomeFragment) fragment).show();
        Log.e("pincode",pincode);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dash_board, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(IntentFactory.createSettingActivity(this));
            return true;
        } else if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MobicashDashBoardActivity.this);
            builder.setMessage("Are you sure you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            showProgressDialog();
                            String hash = "";
                            String mobile = LocalPreferenceUtility.getUserMobileNumber(MobicashDashBoardActivity.this);
                            String pincode = LocalPreferenceUtility.getUserPassCode(MobicashDashBoardActivity.this);
                            String macaddress = MobicashUtils.getMacAddress(MobicashDashBoardActivity.this);
                            StringBuilder builder = new StringBuilder();
                            builder.append(mobile);
                            builder.append(MobicashUtils.md5(pincode));
                            try {
                                hash = MobicashUtils.getSha1(builder.toString());
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            callLogoutApi(MobicashDashBoardActivity.this, mobile, pincode, macaddress, hash);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Logout");
            alert.show();

            /*new SweetAlertDialog(MobicashDashBoardActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("You want to logout?")
                    .setCancelText("No")
                    .setConfirmText("Yes")
                    .showCancelButton(true)
                    .setCancelClickListener(null)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(final SweetAlertDialog sDialog) {
                            showProgressDialog();
                            String hash = "";
                            String mobile = LocalPreferenceUtility.getUserMobileNumber(MobicashDashBoardActivity.this);
                            String pincode = LocalPreferenceUtility.getUserPassCode(MobicashDashBoardActivity.this);
                            String macaddress = MobicashUtils.getMacAddress(MobicashDashBoardActivity.this);
                            StringBuilder builder = new StringBuilder();
                            builder.append(mobile);
                            builder.append(MobicashUtils.md5(pincode));
                            try {
                                hash = MobicashUtils.getSha1(builder.toString());
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            callLogoutApi(MobicashDashBoardActivity.this, mobile, pincode, macaddress, hash);
                            sDialog.dismiss();
                        }
                    })
                    .show();*/

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(serverStatusReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(serverStatusReceiver, new IntentFilter(action));
        }

        /*ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
        }
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);
            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }
            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
        }*/

    }

    /*@Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue reposting.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        dialog.show();
    }*/


    /**
     * Handling broadcast event for user login .
     */
    private BroadcastReceiver serverStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            cancelProgressDialog();
            try {
                if (action.equals(NetworkConstant.STATUS_USER_LOGIN_SUCCESS)) {
                    ServerResponse mServerResponse = (ServerResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    Timber.d("STATUS_USER_LOGIN_SUCCESS = > LoginResponse  ==>" + new Gson().toJson(mServerResponse));
                    //startWifyeeRegistrationRequest(mLoginResponse);
                } else if (action.equals(NetworkConstant.STATUS_USER_LOGIN_FAIL)) {
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_USER_SIGNUP_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        showErrorDialog(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showErrorDialog(errorMessage);
                    }
                }

            } catch (Exception e) {
                Timber.e(" Exception caught in loginStatusReceiver " + e.getMessage());
            }
        }
    };


    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    /**
     * add event
     */
    /*private void addFireBaseEvent() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, LocalPreferenceUtility.getUserMobileNumber(this));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, LocalPreferenceUtility.getUserCode(this));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }*/

    @Override
    public void overridePendingTransition() {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    @Override
    public void showError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void cancelProgress() {
        cancelProgressDialog();
    }

    @Override
    public void onClick(View v) {
        if (v == setting) {
            startActivity(IntentFactory.createSettingActivity(this));
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        if (v == logout) {
            showProgressDialog();
            String hash = "";
            String mobile = LocalPreferenceUtility.getUserMobileNumber(this);
            String pincode = LocalPreferenceUtility.getUserPassCode(this);
            String macaddress = MobicashUtils.getMacAddress(this);
            StringBuilder builder = new StringBuilder();
            builder.append(mobile);
            builder.append(MobicashUtils.md5(pincode));
            try {
                hash = MobicashUtils.getSha1(builder.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            callLogoutApi(this, mobile, pincode, macaddress, hash);
        }
    }

    //Call LogOut Api
    private void callLogoutApi(final Context context, String mobile, String pincode, String macaddress, String hash) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clientmobile", mobile);
            jsonObject.put("pincode", MobicashUtils.md5(pincode));
            jsonObject.put("MAC", macaddress);
            jsonObject.put("hash", hash);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.LOGOUT_API, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        LocalPreferenceUtility.clearSession(MobicashDashBoardActivity.this);
                        Intent i = new Intent(MobicashDashBoardActivity.this, SignInBaseActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                        finish();
                        cancelProgressDialog();
                    } else {
                        cancelProgressDialog();
                        Toast.makeText(context, "Some Issue in Getting response", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error", String.valueOf(error));
                cancelProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                Log.d("params", String.valueOf(params));
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setShouldCache(false);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Fonts.getSemiBold(MobicashDashBoardActivity.this);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //mNewTitle.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, mNewTitle.length(), 0); //Use this if you want to center the items
        mi.setTitle(mNewTitle);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            //new HomeFragment();

        } else if (id == R.id.my_cashback) {
            Intent intent = new Intent(MobicashDashBoardActivity.this,MyCashback.class);
            startActivity(intent);
             //new BankTransferFragment();

        } else if (id == R.id.my_credit) {
            Intent intent = new Intent(MobicashDashBoardActivity.this,MyCreditActivity.class);
            startActivity(intent);
             //new BankTransferFragment();

        } else if (id == R.id.my_vouchers) {
            Intent intent = new Intent(MobicashDashBoardActivity.this,MyVoucherList.class);
            startActivity(intent);
             //new BankTransferFragment();

        } else if (id == R.id.beneficiary) {
            //new ManageBeneficiaryFragment();

        }  else if (id == R.id.refund_cancellation) {
                Intent intent = new Intent(MobicashDashBoardActivity.this,WebViewPolicy.class);
                intent.putExtra("url",NetworkConstant.REFUND_CANCELLATION);
                intent.putExtra("title","Refund & Cancellation");
                startActivity(intent);

        }  else if (id == R.id.privacy_policy) {
            Intent intent = new Intent(MobicashDashBoardActivity.this,WebViewPolicy.class);
            intent.putExtra("url",NetworkConstant.PRIVACY_POLICY);
            intent.putExtra("title","Privacy Policy");
            startActivity(intent);

        }  else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Wifyee");
                String sAux = "Let me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id="+getPackageName();
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Share"));
            } catch(Exception e) {
                Log.w("LOG",e.toString());
            }

        }/*else if (id == R.id.logout) {

        }*/

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Create class PagerAdapter with Fragment
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT = 5;
        private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();

        private final String[] mTabsTitle = {
                getString(R.string.menu_home),
                getString(R.string.my_order),
                getString(R.string.my_cart),
                //getString(R.string.menu_product),
                getString(R.string.menu_log),
                getString(R.string.menu_profile)};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View view = LayoutInflater.from(MobicashDashBoardActivity.this).inflate(R.layout.custom_tab, null);
            TextView title =  view.findViewById(R.id.title);
            title.setText(mTabsTitle[position]);
            ImageView icon =  view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
            TextView b =  view.findViewById(R.id.badge);
            ll = view.findViewById(R.id.badgeCotainer);
            if(position==2) {
                //setupBadge(b,ll);
            }

            return view;
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return new HomeFragment();
                case 1:
                    //return new BankTransferFragment();
                    return new OrderFragment();
                case 2:
                    //ll.setVisibility(View.GONE);
                    return new MyCartFragment();
                case 3:
                    return new LogFragment();
                case 4:
                    return new ProfileFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final Fragment fragment = (Fragment) super.instantiateItem(container, position);
            instantiatedFragments.put(position, new WeakReference<>(fragment));
            return fragment;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            instantiatedFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Nullable
        public Fragment getFragment(final int position) {
            final WeakReference<Fragment> wr = instantiatedFragments.get(position);
            if (wr != null) {
                return wr.get();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitle[position];
        }

        private void setupBadge(TextView textCartItemCount, LinearLayout ll) {
            if (textCartItemCount != null) {
                if (fillList() == 0) {
                    ll.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                    textCartItemCount.setText(String.valueOf(Math.min(fillList(), 99)));
                }
            }
        }

        public int fillList() {
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * set color
     *
     * @param actBar
     * @param color
     */

    private void setActionbarTextColor(ActionBar actBar, int color) {
        String title = actBar.getTitle().toString();
        Spannable spannablerTitle = new SpannableString(title);
        spannablerTitle.setSpan(new ForegroundColorSpan(color), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actBar.setTitle(spannablerTitle);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                System.exit(0);
                            }
                        }).setNegativeButton("No", null).show();
    }

    //Call Api for data usage
   /* protected void callApiForDataUsage(final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobileNumber", LocalPreferenceUtility.getUserMobileNumber(context));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.USER_VIEW_DATA_USAGE, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        planDataSummary = new PlanDataSummary();
                        JSONObject jsonObject1 = response.getJSONObject("current_plan");
                        String planName = jsonObject1.getString("plan_name");
                        String timeUsed = jsonObject1.getString("time_used");
                        String dataDownload = jsonObject1.getString("data_download");
                        String dataRemaining = jsonObject1.getString("data_remaining");
                        String dataTotal = jsonObject1.getString("data_total");
                        String timeRemaining = jsonObject1.getString("time_remaining");
                        String timeTotal = jsonObject1.getString("time_total");
                        planDataSummary.setData_download(dataDownload);
                        planDataSummary.setData_remaining(dataRemaining);
                        planDataSummary.setData_total(dataTotal);
                        planDataSummary.setPlan_name(planName);
                        planDataSummary.setTime_remaining(timeRemaining);
                        planDataSummary.setTime_used(timeUsed);
                        planDataSummary.setTime_total(timeTotal);
                        //Calling Api Soft data Usage
                    } else {
                        cancelProgressDialog();
                        Toast.makeText(context, "Some Issue in Getting response", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error", String.valueOf(error));
                Toast.makeText(context, "response Error", Toast.LENGTH_SHORT).show();
                cancelProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                Log.d("params", String.valueOf(params));
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setShouldCache(false);
    }*/

    //Call Api for Fetch User Data on Wifyee Server
    private void callApiWifiSoft(final Context activity, ServerRequest serverRequest) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_action", "get_one");
            jsonObject.put("req_customercode", "95629568");
            jsonObject.put("req_username", LocalPreferenceUtility.getUserMobileNumber(this) + "@wifyee");
            jsonObject.put("req_timeinterval", "1 day");

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.WIFYEE_HOTSPOT_FETCH_USER, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null) {
                        Log.e("JSON",response.toString());
                        serverResponse=new ServerResponse();
                           JSONObject jsonObject1 = response.getJSONObject("0");
                            final String sessionID = jsonObject1.getString("sessionId");
                            serverResponse.sessionID=sessionID;
                            final String uniqueID = jsonObject1.getString("uniqueId");
                            serverResponse.uniqueID=uniqueID;
                            final String nasIPAddress = jsonObject1.getString("nasIP");
                            serverResponse.nasIPAddress=nasIPAddress;
                            final String nasPortID = jsonObject1.getString("nasPortId");
                            serverResponse.nasPortID=nasPortID;
                            final String nasPortType = jsonObject1.getString("nasPortType");
                            serverResponse.nasPortType=nasPortType;
                            final String startTime = jsonObject1.getString("startTime");
                            serverResponse.startTime=startTime;
                            final String stopTime = jsonObject1.getString("stopTime");
                            serverResponse.stopTime=stopTime;
                            final String sessionTime = jsonObject1.getString("sessionTime");
                            serverResponse.sessionTime=sessionTime;
                            final String uploadData = jsonObject1.getString("uploadData");
                            serverResponse.uploadData=uploadData;
                            final String downloadData = jsonObject1.getString("downloadData");
                            serverResponse.downloadData=downloadData;
                            final String calledStationId = jsonObject1.getString("CallingStationId");
                            LocalPreferenceUtility.putDeviceMacID(mContext, calledStationId);
                            final String framedIPAddress = jsonObject1.getString("framedIPAddress");
                            serverResponse.framedIPAddress=framedIPAddress;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Call Api For Send Data to Server
                                callApiForServerReport(activity,serverResponse);
                            }
                        });
                    } else {
                        cancelProgressDialog();
                        Toast.makeText(activity, "Issue in call Data usage on Wifyee server", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error", String.valueOf(error));
             //   Toast.makeText(activity, "response Error", Toast.LENGTH_SHORT).show();
                cancelProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                Log.d("params", String.valueOf(params));
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setShouldCache(false);
    }
    //Call Api Send to server data
    private void callApiForServerReport(final Context activity,final ServerResponse serverResponse) {

          RequestQueue requestQueue = Volley.newRequestQueue(activity);
        JSONObject jsonObjectValue = new JSONObject();
        try {
            jsonObjectValue.put("sessionId", serverResponse.sessionID);
            jsonObjectValue.put("uniqueId", serverResponse.uniqueID);
            jsonObjectValue.put("userMac", MobicashUtils.getMacAddress(activity));
            jsonObjectValue.put("nasIP", serverResponse.nasIPAddress);
            jsonObjectValue.put("nasPortId", serverResponse.nasPortID);
            jsonObjectValue.put("nasPortType", serverResponse.nasPortType);
            jsonObjectValue.put("startTime", serverResponse.startTime);
            jsonObjectValue.put("stopTime", serverResponse.stopTime);
            jsonObjectValue.put("uploadData", serverResponse.uploadData);
            jsonObjectValue.put("downloadData", serverResponse.downloadData);
            jsonObjectValue.put("calledStationId", serverResponse.calledStationId);
            jsonObjectValue.put("framedIPAddress", serverResponse.framedIPAddress);
            jsonObjectValue.put("terminateCause", "Lost Service");

            StringBuilder builder = new StringBuilder(serverResponse.sessionID);
            builder.append(serverResponse.uniqueID);
            builder.append(MobicashUtils.getMacAddress(activity));
            builder.append(serverResponse.nasIPAddress);
            builder.append(serverResponse.nasPortID);
            builder.append(serverResponse.nasPortType);
            builder.append(serverResponse.startTime);
            builder.append(serverResponse.stopTime);
            builder.append(serverResponse.uploadData);
            builder.append(serverResponse.downloadData);
            builder.append(serverResponse.calledStationId);
            builder.append(serverResponse.framedIPAddress);
            builder.append("Lost Service");
            try {
                String hash = "";
                hash = MobicashUtils.getSha1(builder.toString());
                jsonObjectValue.put("hash", hash);
            } catch (Exception es) {
                es.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.USER_DATA_HOTSPOT_FETCH_USER, jsonObjectValue, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        Log.e("getting Response", response.toString());
                        String message = response.getString("msg");
                        Toast.makeText(activity, "Success=" + message, Toast.LENGTH_SHORT).show();
                        callApiLogoForISP(serverResponse.calledStationId);

                    } else {
                        cancelProgressDialog();
                        Toast.makeText(activity, "Issue in call Data usage on Wifyee Server", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error", String.valueOf(error));
               // Toast.makeText(activity, "response Error", Toast.LENGTH_SHORT).show();
                cancelProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> paramsValue = new HashMap<String, String>();
                paramsValue.put("Content-Type", "application/json");
                Log.d("params", String.valueOf(paramsValue));
                return paramsValue;
            }
        };
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setShouldCache(false);
    }

    private void callApiLogoForISP(String macDeviceID)
    {
        final JSONObject jsondata=new JSONObject();
        try {
            jsondata.put("mac_id",macDeviceID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(NetworkConstant.GET_ISP_LOGO)
                .addJSONObjectBody(jsondata)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                String ispLogo = response.getString("url");
                                bindIspLogo(ispLogo);
                                Timber.d("Got Success...");
                                Timber.d("handleActionGetLogOnInfo = > JSONObject response ==>" + new Gson().toJson(response));
                            } else {
                                cancelProgressDialog();
                                Timber.d("Got failure inGetLogOnInfoResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleGetLogOnInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                            }
                        } catch (Exception e) {
                            Timber.e("JSONException Caught.Message : " + e.getMessage());
                            cancelProgressDialog();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Get GetLogOn Info API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        cancelProgressDialog();
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("GetLogOnInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                    }
                });

    }
    //Bind InterNet sevice Provider Logo
    private void bindIspLogo(String ispLogo)
    {
        ispLogoImage.setImageUrl(ispLogo,MobicashApplication.getInstance().getImageLoader());
    }

    private String getAddress(Double Lat, Double Long){
        String addr="";
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(Lat, Long, 1);
            if (addresses != null && addresses.size() > 0) {
                addr = addresses.get(0).getAddressLine(0);
                Log.e("address",addr);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return addr;
    }

    private String getPincode(Double Lat, Double Long){
        String pincode="";
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(Lat, Long, 1);
            if (addresses != null && addresses.size() > 0) {
                pincode = addresses.get(0).getPostalCode();
                Log.e("pincode",pincode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pincode;
    }

    private void CheckVersion(final String currentVersion){

        String uri = NetworkConstant.CHECK_VERSION;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("JSON",response);
                try {
                    JSONObject object = new JSONObject(response);
                    newVersion = object.getString("app_version");

                    if (Float.valueOf(currentVersion) < Float.valueOf(newVersion)) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MobicashDashBoardActivity.this);
                        alert.setTitle("Update");
                        alert.setCancelable(false);
                        alert.setMessage("Please update to new version to continue use");
                        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                                }
                            }
                        });

                        alert.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
                //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void sendRegistrationToServer(String token) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(ResponseAttributeConstants.USER_TYPE, "client");
                jsonObject.put(ResponseAttributeConstants.USER_ID, LocalPreferenceUtility.getUserCode(getApplicationContext()));
                jsonObject.put(ResponseAttributeConstants.TOKEN, token);
                Log.e("json", jsonObject.toString());
            } catch (JSONException e) {
                Timber.e("JSONException. message : " + e.getMessage());
            }
            AndroidNetworking.post(NetworkConstant.TOKEN_UPDATE)
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Log.e("RSP_TOKEN_UPDATE", response.toString());
                            try {
                                if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                    Log.w("TOKEN","token updated");
                                } else {
                                    Log.w("TOKEN","token not updated");
                                }
                            } catch (JSONException e) {
                                Timber.e("JSONException Caught.  Message : " + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Timber.e("called onError of User Login API.");
                            Timber.e("Error Message : " + error.getMessage());
                            Timber.e("Error code : " + error.getErrorCode());
                            Timber.e("Error Body : " + error.getErrorBody());
                            Timber.e("Error Detail : " + error.getErrorDetail());
                        }
                    });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            //onLocationChanged(location);
                            LocalPreferenceUtility.putLatitude(getApplicationContext(),String.valueOf(location.getLatitude()));
                            LocalPreferenceUtility.putLongitude(getApplicationContext(),String.valueOf(location.getLongitude()));
                            String loc = getAddress(location.getLatitude(),location.getLongitude());
                            String pincode = getPincode(location.getLatitude(),location.getLongitude());
                            LocalPreferenceUtility.putCurrentPincode(getApplicationContext(),pincode);
                            locationTxt.setText(loc);
                            Fragment fragment = pagerAdapter.getFragment(mTabLayout
                                    .getSelectedTabPosition());
                            ((HomeFragment) fragment).show();
                        }
                    }
                });

        Log.d(TAG, "Connected to Google API");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");
        if (location != null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            /*String loc = getAddress(location.getLatitude(),location.getLongitude());
            Log.e("location",loc);
            locationTxt.setText(loc);*/
            Log.d(TAG, "== location != null");
        }
    }
}
