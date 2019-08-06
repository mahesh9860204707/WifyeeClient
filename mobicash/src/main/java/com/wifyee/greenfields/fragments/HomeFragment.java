package com.wifyee.greenfields.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.CircularNetworkImageView;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;

import com.wifyee.greenfields.activity.BaseActivity;
import com.wifyee.greenfields.activity.BookExperinceActivity;
import com.wifyee.greenfields.activity.FollowBrandsActivity;
import com.wifyee.greenfields.activity.MerchantCategoryListActivity;
import com.wifyee.greenfields.activity.MobicashDashBoardActivity;
import com.wifyee.greenfields.activity.MoreCategory;
import com.wifyee.greenfields.activity.MyCreditActivity;
import com.wifyee.greenfields.activity.ProductsSellActivity;
import com.wifyee.greenfields.activity.RequestBroadbandActivity;
import com.wifyee.greenfields.activity.SignUpOTPActivity;
import com.wifyee.greenfields.activity.VoucherList;
import com.wifyee.greenfields.adapters.OtherMerchantAdapter;
import com.wifyee.greenfields.adapters.VoucherListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.constants.WifiConstant;
import com.wifyee.greenfields.dairyorder.DairyProductActivity;
import com.wifyee.greenfields.dairyorder.OrderSummaryDetails;
import com.wifyee.greenfields.mapper.ModelMapper;
import com.wifyee.greenfields.models.MyCreditModel;
import com.wifyee.greenfields.models.OtherMerchantModel;
import com.wifyee.greenfields.models.VoucherModel;
import com.wifyee.greenfields.models.requests.GetClientProfileInfoRequest;
import com.wifyee.greenfields.models.requests.LoginRequest;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.GetClientProfileInfoResponse;
import com.wifyee.greenfields.models.response.GetProfileInfo;
import com.wifyee.greenfields.services.MobicashIntentService;
import com.wifyee.greenfields.slider.FragmentSlider;
import com.wifyee.greenfields.slider.SliderIndicator;
import com.wifyee.greenfields.slider.SliderPagerAdapter;
import com.wifyee.greenfields.slider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import timber.log.Timber;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.telephony.TelephonyManager;

public class HomeFragment extends Fragment implements View.OnClickListener{

    public static final String ARG_PAGE = "ARG_PAGE";
    public ProgressDialog progressDialog = null;
    private Context mContext = null;
    private LoginRequest mLoginRequest;
    private RecyclerView recycleView,recyclerViewGrocery,recyclerViewVouchers,recyclerViewOtherMerchant,recyclerViewOrders;
    private TextView walletBalance,approveCredit,textConnectWifi,helpDesk;
    private Button planWifi;
    private ImageView connectWifi,connect;
    private LinearLayout dataLayout,creditLayout;
    private CircularNetworkImageView circularNetworkImageView;
    private RecycleViewPagerAdapter recycleViewPagerAdapter;
    private RecycleViewOrdersPagerAdapter recycleViewOrdersPagerAdapter;
    private RecycleViewNestedPagerAdapter recycleViewNestedPagerAdapter;
    private VoucherListAdapter voucherAdapter;
    private RecycleViewGroceryAdapter recycleViewGroceryAdapter;
    private WifiManager mainWifi;
    private int flag;
    private ConnectivityManager connectivityManager;
    private boolean off;
    private String formattedDate="";
    private final int REQUEST_CODE=99;
    private ImageView offersNears;
    private LinearLayout followBrandsLayout,bookExperienceLayout,broadbandLayout;
    private LinearLayout shoppingLayout;
    private Method dataConnSwitchmethod_ON;
    private Method dataConnSwitchmethod_OFF;
    private Class telephonyManagerClass;
    private Object ITelephonyStub;
    private Class ITelephonyClass;
    private boolean switchOn;
    private List<VoucherModel> list = new ArrayList<>();
    private List<OtherMerchantModel> otherlist = new ArrayList<>();
    private VoucherModel[] voucher;
    private OtherMerchantModel[] otherMerchant;
    private OtherMerchantAdapter merchantAdapter;
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_SUCCESS,
            NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_FAIL
    };
    //SliderLayout sliderLayout;
    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        @SuppressLint("WifiManagerLeak")
        WifiManager wifi = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
        ButterKnife.bind(getActivity());
        mContext = getActivity();
        System.gc();
    }

    TextView view_more_voucher,txt_vouchers,txtOtherMerchant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        GetDataConnectionAPI(getActivity());
        //turn3GOff();
        //requestData(getActivity());
        //connectWifiOpenHomeScreen("Wifyee",getActivity());
        mLoginRequest = new LoginRequest();
        mLoginRequest.clientmobile=LocalPreferenceUtility.getUserMobileNumber(getActivity());
        mLoginRequest.pincode=LocalPreferenceUtility.getUserPassCode(getActivity());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        formattedDate = df.format(c.getTime());

        //sliderView = (SliderView) view.findViewById(R.id.sliderView);
        //mLinearLayout = (LinearLayout) view.findViewById(R.id.pagesContainer);

        //setupSlider();

        return view;
    }

    /*private void setupSlider() {
        sliderView.setDurationScroll(1000);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentSlider.newInstance("http://45.249.108.75/img/b2.jpeg"));
        fragments.add(FragmentSlider.newInstance("http://45.249.108.75/img/b3.jpeg"));
        fragments.add(FragmentSlider.newInstance("http://45.249.108.75/img/b4.jpeg"));
        fragments.add(FragmentSlider.newInstance("http://45.249.108.75/img/b5.jpeg"));
        fragments.add(FragmentSlider.newInstance("http://45.249.108.75/img/b6.jpeg"));
        fragments.add(FragmentSlider.newInstance("http://45.249.108.75/img/b7.jpeg"));
        fragments.add(FragmentSlider.newInstance("http://45.249.108.75/img/b8.jpeg"));
        fragments.add(FragmentSlider.newInstance("http://45.249.108.75/img/b9.jpeg"));

        mAdapter = new SliderPagerAdapter(getChildFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }*/

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && isAdded()) {
            // Register for all the actions
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
            for (String action : broadCastReceiverActionList) {
                broadcastManager.registerReceiver(walletBalanceReceiver, new IntentFilter(action));
            }
            MobicashIntentService.startActionGetClientProfileInfo(getContext(), getClientProfileInfoRequest());
        }
    }

    int bv = Build.VERSION.SDK_INT;

  /*  boolean turnOnDataConnection(boolean ON,Context context)
    {
        try{
            if(bv == Build.VERSION_CODES.FROYO) {
                Method dataConnSwitchmethod;
                Class<?> telephonyManagerClass;
                Object ITelephonyStub;
                Class<?> ITelephonyClass;
                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
                Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
                getITelephonyMethod.setAccessible(true);
                ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
                ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());
                if (ON) {
                    dataConnSwitchmethod = ITelephonyClass
                            .getDeclaredMethod("enableDataConnectivity");
                } else {
                    dataConnSwitchmethod = ITelephonyClass
                            .getDeclaredMethod("disableDataConnectivity");
                }
                dataConnSwitchmethod.setAccessible(true);
                dataConnSwitchmethod.invoke(ITelephonyStub);

            }
            else {
                //log.i("App running on Ginger bread+");
                final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final Class<?> conmanClass = Class.forName(conman.getClass().getName());
                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                iConnectivityManagerField.setAccessible(true);
                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                final Class<?> iConnectivityManagerClass =  Class.forName(iConnectivityManager.getClass().getName());
                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataDisable", Boolean.TYPE);
                setMobileDataEnabledMethod.setAccessible(true);
                setMobileDataEnabledMethod.invoke(iConnectivityManager, ON);
            }
            return true;
        }catch(Exception e){
            Log.e(TAG,"error turning on/off data");
            return false;
        }

    }*/


   //Turn 3G Off
    private void turn3GOff() {
        dataConnSwitchmethod_OFF.setAccessible(true);
        try {
            dataConnSwitchmethod_OFF.invoke(ITelephonyStub);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void GetDataConnectionAPI(Context context) {
        context.getApplicationContext();
        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().
                getSystemService(Context.TELEPHONY_SERVICE);
        try {
            telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
            Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
            getITelephonyMethod.setAccessible(true);
            ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
            ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());
            dataConnSwitchmethod_OFF = ITelephonyClass.getDeclaredMethod("disableDataConnectivity");
            dataConnSwitchmethod_ON = ITelephonyClass.getDeclaredMethod("enableDataConnectivity");
        } catch (Exception e) { // ugly but works for me
            e.printStackTrace();
        }
    }
    /**
     * request scan list
     */
    private void requestData(Context context) {
       // showProgressDialog(context);
        mainWifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if (!mainWifi.isWifiEnabled()) {
            mainWifi.setWifiEnabled(true);
        }
        mainWifi.startScan();
       // cancelProgressDialog();
    }
    /**
     * show profile upload progress dialog
     */
    protected void showProgressDialog(Context activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(activity, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            progressDialog = new ProgressDialog(activity);
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.setIcon(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    /**
     * cancel upload progress dialog
     */
    protected void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }
    /**
     * operator list and recharge status receiver
     */
    private BroadcastReceiver walletBalanceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_SUCCESS)) {
                GetClientProfileInfoResponse getClientProfileInfoResponse = (GetClientProfileInfoResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (getActivity() != null && isAdded()) {
                    if (getClientProfileInfoResponse != null
                            && getClientProfileInfoResponse.clientProfileInfo.mobicashClientProfile != null)
                    {
                        GetProfileInfo getProfileInfo = getClientProfileInfoResponse.clientProfileInfo.mobicashClientProfile;
                        if (getProfileInfo != null && getProfileInfo.clientWalletBalance != null
                                && getProfileInfo.clientApprovedCreditLimit != null
                                && getProfileInfo.clientProfileImage != null && getProfileInfo.client_firstname != null
                                && getProfileInfo.client_lastname != null)
                            LocalPreferenceUtility.putUserFirstName(getActivity(),getProfileInfo.client_firstname );
                        LocalPreferenceUtility.putUserLastName(getActivity(),getProfileInfo.client_lastname);
                        LocalPreferenceUtility.putUserEmail(getActivity(),getProfileInfo.client_email);
                        LocalPreferenceUtility.putClientAddress(getActivity(),getProfileInfo.custAddess);
                        LocalPreferenceUtility.putClientDob(getActivity(),getProfileInfo.customerDOB);
                        LocalPreferenceUtility.putClientTitle(getActivity(),getProfileInfo.customerTitle);
                        LocalPreferenceUtility.putClientClientIdentityID(getActivity(),getProfileInfo.indentityId);
//                            dataLayout.setVisibility(View.GONE);
 //                       creditLayout.setVisibility(View.GONE);
                        walletBalance.setText(getString(R.string.Rs, getProfileInfo.clientWalletBalance));
                        LocalPreferenceUtility.saveWalletBalance(mContext,getProfileInfo.clientWalletBalance);

                      //  approveCredit.setText(getString(R.string.Rs, getProfileInfo.clientApprovedCreditLimit));
                        walletBalance.setVisibility(View.VISIBLE);
                      //  approveCredit.setVisibility(View.VISIBLE);
//                        circularNetworkImageView.setImageUrl(getProfileInfo.clientProfileImage, MobicashApplication.getInstance().getImageLoader());
                    } else {
                        dataLayout.setVisibility(View.GONE);
                        creditLayout.setVisibility(View.GONE);
                       // walletBalance.setText(getString(R.string.Rs, "0.00"));
                       // approveCredit.setText(getString(R.string.Rs, "0.00"));
                        //walletBalance.setVisibility(View.VISIBLE);
                       // approveCredit.setVisibility(View.VISIBLE);
                    }
                }
            } else if (action.equals(NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_FAIL)) {
//                dataLayout.setVisibility(View.GONE);
           //     creditLayout.setVisibility(View.GONE);
                walletBalance.setText(getString(R.string.Rs, "0.00"));
             //   approveCredit.setText(getString(R.string.Rs, "0.00"));
                walletBalance.setVisibility(View.VISIBLE);
              //  approveCredit.setVisibility(View.VISIBLE);
            }
        }
    };
    /**
     * get operator list request
     */
    private GetClientProfileInfoRequest getClientProfileInfoRequest() {
        GetClientProfileInfoRequest getClientProfileInfoRequest = null;
        if (getActivity() != null && isAdded()) {
            getClientProfileInfoRequest = new GetClientProfileInfoRequest();
            getClientProfileInfoRequest.clientId = LocalPreferenceUtility.getUserCode(mContext);
            getClientProfileInfoRequest.pincode = LocalPreferenceUtility.getUserPassCode(mContext);
            try {
                StringBuilder sb = new StringBuilder(getClientProfileInfoRequest.clientId);
                sb.append(MobicashUtils.md5(getClientProfileInfoRequest.pincode));
                String hashMapValue="";
                hashMapValue = MobicashUtils.getSha1(sb.toString());
                getClientProfileInfoRequest.hash=hashMapValue;
            } catch (Exception e) {
            }
        }
        return getClientProfileInfoRequest;
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(walletBalanceReceiver);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        walletBalance = (TextView) view.findViewById(R.id.wallet_balance);
        offersNears= (ImageView) view.findViewById(R.id.offers_nears_by);
        offersNears.setOnClickListener(this);
        followBrandsLayout=(LinearLayout) view.findViewById(R.id.follow_brands_layout);
        followBrandsLayout.setOnClickListener(this);
        broadbandLayout=(LinearLayout) view.findViewById(R.id.broadband_layout);
        broadbandLayout.setOnClickListener(this);
        bookExperienceLayout=(LinearLayout) view.findViewById(R.id.book_experience_layout);
        bookExperienceLayout.setOnClickListener(this);
        shoppingLayout=(LinearLayout) view.findViewById(R.id.shopping_mall);
        shoppingLayout.setOnClickListener(this);
        helpDesk= (TextView) view.findViewById(R.id.help_desk);
        txt_vouchers= (TextView) view.findViewById(R.id.txt_vouchers);
        view_more_voucher = view.findViewById(R.id.view_more_voucher);
        txtOtherMerchant = view.findViewById(R.id.txt_other_merchant);

        helpDesk.setOnClickListener(this);
        connect = view.findViewById(R.id.connect);
        connect.setOnClickListener(this);

       // dataLayout = (LinearLayout) view.findViewById(R.id.network_data_line_bar_holder_layout);
       // creditLayout = (LinearLayout) view.findViewById(R.id.network_data_line_bar_holder_layout_approval);
      //  circularNetworkImageView = (CircularNetworkImageView) view.findViewById(R.id.iv_profileView);
        recycleView = (RecyclerView) view.findViewById(R.id.horizontal_recycler_view);
        recyclerViewGrocery = (RecyclerView) view.findViewById(R.id.recycler_grocery);
        recyclerViewOtherMerchant = (RecyclerView) view.findViewById(R.id.recycler_other_merchant);
        recyclerViewOrders= (RecyclerView) view.findViewById(R.id.horizontal_recycler_item);
        recyclerViewVouchers= (RecyclerView) view.findViewById(R.id.recycler_view_vouchers);

        recycleViewPagerAdapter = new RecycleViewPagerAdapter();
        //recycleView.addItemDecoration(new DividerItemDecoration(mContext,
               // DividerItemDecoration.HORIZONTAL));
        //recycleView.addItemDecoration(new DividerItemDecoration(mContext,
                //DividerItemDecoration.VERTICAL));
        LinearLayoutManager horizontalLayoutManagaer =  new GridLayoutManager(mContext, 4);
        recycleView.setLayoutManager(horizontalLayoutManagaer);
        recycleView.setAdapter(recycleViewPagerAdapter);


        recycleViewOrdersPagerAdapter= new RecycleViewOrdersPagerAdapter();

        //recyclerViewOrders.addItemDecoration(new DividerItemDecoration(mContext,
                //DividerItemDecoration.HORIZONTAL));
       // recyclerViewOrders.addItemDecoration(new DividerItemDecoration(mContext,
                //DividerItemDecoration.VERTICAL));
        LinearLayoutManager horizontalLayoutManagaerOreders =  new GridLayoutManager(mContext, 3);
        recyclerViewOrders.setLayoutManager(horizontalLayoutManagaerOreders);
        recyclerViewOrders.setHasFixedSize(true);
        recyclerViewOrders.setAdapter(recycleViewOrdersPagerAdapter);


        //Grocer recyclerview
        recycleViewGroceryAdapter = new RecycleViewGroceryAdapter();
        //recyclerViewGrocery.addItemDecoration(new DividerItemDecoration(mContext,
                //DividerItemDecoration.HORIZONTAL));
        //recyclerViewGrocery.addItemDecoration(new DividerItemDecoration(mContext,
                //DividerItemDecoration.VERTICAL));
        LinearLayoutManager llm =  new GridLayoutManager(mContext, 3);
        recyclerViewGrocery.setLayoutManager(llm);
        recyclerViewGrocery.setHasFixedSize(true);
        recyclerViewGrocery.setAdapter(recycleViewGroceryAdapter);

        LinearLayoutManager horizonatlVoucher = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewVouchers.setLayoutManager(horizonatlVoucher);
        voucherAdapter = new VoucherListAdapter(mContext,list,0);
        recyclerViewVouchers.setAdapter(voucherAdapter);
        voucherAdapter.notifyDataSetChanged();

        callVoucherList();

        LinearLayoutManager horizonatlOtherMerchant = new GridLayoutManager(mContext,3);
        recyclerViewOtherMerchant.setLayoutManager(horizonatlOtherMerchant);
        merchantAdapter = new OtherMerchantAdapter(mContext,otherlist);
        recyclerViewOtherMerchant.setAdapter(merchantAdapter);
        merchantAdapter.notifyDataSetChanged();

        callOtherMerchantList();

        view_more_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, VoucherList.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        //nested view
        /*recycleViewNestedPagerAdapter = new RecycleViewNestedPagerAdapter();
        LinearLayoutManager horizontalLayoutManagaerr
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recycleViewNested.setLayoutManager(horizontalLayoutManagaerr);
        recycleViewNested.setAdapter(recycleViewNestedPagerAdapter);*/

    }

    private void callOtherMerchantList() {
        if (!LocalPreferenceUtility.getPinCode(mContext).isEmpty()) {

            if (otherlist != null) { otherlist.clear(); }

            JSONObject json = new JSONObject();
            try {
                json.put("pincode", LocalPreferenceUtility.getPinCode(mContext));
                //json.put("pincode", "416510");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.e("json",json.toString());

            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .build();

            AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING +
                    NetworkConstant.PARAM_GET_OTHER_MERCHANT_LIST)
                    .addJSONObjectBody(json)
                    .setOkHttpClient(okHttpClient)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            Log.e("--RESPONSE--", object.toString());
                            cancelProgressDialog();
                            try {
                                if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                    JSONArray jsonArray = object.getJSONArray(ResponseAttributeConstants.OFFERS_DATA);
                                    if (jsonArray.length()>0) {
                                        txtOtherMerchant.setVisibility(View.VISIBLE);
                                        recyclerViewOtherMerchant.setVisibility(View.VISIBLE);

                                        otherMerchant = new OtherMerchantModel[jsonArray.length()];
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            otherMerchant[i] = new OtherMerchantModel(
                                                    jsonObject.getString("id"),
                                                    jsonObject.getString("merchant_type_name"),
                                                    jsonObject.getString("image")
                                            );
                                            otherlist.add(otherMerchant[i]);
                                        }
                                        recyclerViewOtherMerchant.getRecycledViewPool().clear();
                                        merchantAdapter.notifyDataSetChanged();
                                    }else {
                                        txtOtherMerchant.setVisibility(View.GONE);
                                        recyclerViewOtherMerchant.setVisibility(View.GONE);
                                    }
                                } else {
                                    String msg = object.getString(ResponseAttributeConstants.MSG);
                                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                cancelProgressDialog();
                                Timber.e("JSONException Caught.  Message : " + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            cancelProgressDialog();
                            // handle error
                            Timber.e("called onError of User Invoice API.");
                            Timber.e("Error Message : " + error.getMessage());
                            Timber.e("Error code : " + error.getErrorCode());
                            Timber.e("Error Body : " + error.getErrorBody());
                            Timber.e("Error Detail : " + error.getErrorDetail());
                        }
                    });
        }else {
            view_more_voucher.setVisibility(View.GONE);
            txt_vouchers.setVisibility(View.GONE);
            recyclerViewVouchers.setVisibility(View.GONE);
        }
    }



    private void callVoucherList() {
        if (!LocalPreferenceUtility.getPinCode(mContext).isEmpty()) {

            if (list != null) { list.clear(); }

            JSONObject json = new JSONObject();
            try {
                json.put("pincode", LocalPreferenceUtility.getPinCode(mContext));
                json.put("limit", "4");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.e("json",json.toString());

            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .build();

            AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING +
                    NetworkConstant.PARAM_VOUCHER_LIST_BY_PINCODE)
                    .addJSONObjectBody(json)
                    .setOkHttpClient(okHttpClient)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            Log.e("--RESPONSE--", object.toString());
                            cancelProgressDialog();
                            try {
                                if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                    JSONArray jsonArray = object.getJSONArray(ResponseAttributeConstants.OFFERS_DATA);
                                    if (jsonArray.length()>0) {
                                        view_more_voucher.setVisibility(View.VISIBLE);
                                        txt_vouchers.setVisibility(View.VISIBLE);
                                        recyclerViewVouchers.setVisibility(View.VISIBLE);

                                        voucher = new VoucherModel[jsonArray.length()];
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            voucher[i] = new VoucherModel(
                                                    jsonObject.getString("id"),
                                                    jsonObject.getString("voucher_no"),
                                                    jsonObject.getString("voucher_name"),
                                                    jsonObject.getString("voucher_details"),
                                                    jsonObject.getString("voucher_amount"),
                                                    jsonObject.getString("discount_amount"),
                                                    jsonObject.getString("valid_from"),
                                                    jsonObject.getString("valid_upto"),
                                                    jsonObject.getString("image_path")
                                            );
                                            list.add(voucher[i]);
                                        }
                                        recyclerViewVouchers.getRecycledViewPool().clear();
                                        voucherAdapter.notifyDataSetChanged();
                                    }else {
                                        view_more_voucher.setVisibility(View.GONE);
                                        txt_vouchers.setVisibility(View.GONE);
                                        recyclerViewVouchers.setVisibility(View.GONE);
                                    }
                                } else {
                                    String msg = object.getString(ResponseAttributeConstants.MSG);
                                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                cancelProgressDialog();
                                Timber.e("JSONException Caught.  Message : " + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            cancelProgressDialog();
                            // handle error
                            Timber.e("called onError of User Invoice API.");
                            Timber.e("Error Message : " + error.getMessage());
                            Timber.e("Error code : " + error.getErrorCode());
                            Timber.e("Error Body : " + error.getErrorBody());
                            Timber.e("Error Detail : " + error.getErrorDetail());
                        }
                    });
        }else {
            view_more_voucher.setVisibility(View.GONE);
            txt_vouchers.setVisibility(View.GONE);
            recyclerViewVouchers.setVisibility(View.GONE);
            showErrorDialog("Please update your profile to get voucher list");
        }
    }

    /* private boolean getMobileDataEnabled() {
         try {
             Method method = connectivityManager.getClass().getMethod("getMobileDataEnabled");
             return (Boolean)method.invoke(connectivityManager);
         } catch (Exception e) {
             e.printStackTrace();
             return false;
         }
     }
     private void setMobileDataEnabled(boolean on) {
         try {
             Method method = connectivityManager.getClass().getMethod("setMobileDataEnabled",
                     boolean.class);
             method.invoke(connectivityManager, on);
         } catch (NoSuchMethodException e) {
             e.printStackTrace();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 */
    //Connect Wi Fi
    private void connectWifiOpenHomeScreen(String s,final Activity activity) {
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\""+s+"\""; //IMPORTANT! This should be in Quotes!!
        wc.priority = 40;
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        int res = mainWifi.addNetwork(wc);
        Log.d("WifiPreference", "add Network returned " + res);
        boolean es = mainWifi.saveConfiguration();
        Log.d("WifiPreference", "saveConfiguration returned " + es);
        // boolean b = mainWifi.enableNetwork(res, true);
        boolean b = mainWifi.enableNetwork(res, true);
        Log.d("WifiPreference", "enableNetwork returned " + b);
        if(b==true) {
            //shoPopupConnected(activity,wc);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    callApiGettingStatus(activity);
                }
            }, 8000);

            Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show();
        }
        else if(b==false) {
           //showPopupWiFiConfig(activity);
           // Toast.makeText(activity, "Not Connected", Toast.LENGTH_SHORT).show();
            cancelProgressDialog();
        }
    }
  //Show Popup Manualy configure WIFi
    private void showPopupWiFiConfig(final Activity activity)
    {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_wifi_manual);
        Button yes = (Button) layout.findViewById(R.id.yes);
        // Set dialog title
        layout.setTitle("Wi fi Configuration");
        cancelProgressDialog();
        layout.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                    callApiGettingStatus(activity);
                    layout.dismiss();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    //Connect Open WiFi
    private void connectWifiOpen(String s, Activity activity) {
                WifiConfiguration wc = new WifiConfiguration();
                wc.SSID = "\""+s+"\""; //IMPORTANT! This should be in Quotes!!
                wc.priority = 40;
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                int res = mainWifi.addNetwork(wc);
                Log.d("WifiPreference", "add Network returned " + res);
                boolean es = mainWifi.saveConfiguration();
                Log.d("WifiPreference", "saveConfiguration returned " + es);
               // boolean b = mainWifi.enableNetwork(res, true);
                boolean b = mainWifi.enableNetwork(res, true);
                Log.d("WifiPreference", "enableNetwork returned " + b);
                if(b==true) {
                        shoPopupConnected(activity, wc);
                        Toast.makeText(activity, "Connected",Toast.LENGTH_SHORT).show();
                }
                else if(b==false) {
                    showPopupWiFiConfig(activity);
                    cancelProgressDialog();
                    Toast.makeText(activity, "Not Connected",Toast.LENGTH_SHORT).show();
                }
    }

    //Customize Show Popup
    private void shoPopupConnected(final Activity activity, WifiConfiguration wc)
    {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_view_connected);
        // Set dialog title
        layout.setTitle("Net Connected");
        TextView textSSID = (TextView) layout.findViewById(R.id.ssid_name);
        textSSID.setText(wc.SSID);
        TextView textBSSID = (TextView) layout.findViewById(R.id.bssid_name);
        textBSSID.setText(wc.BSSID);
        Button yes = (Button) layout.findViewById(R.id.yes);
        cancelProgressDialog();
        layout.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showProgressDialog(activity);
                    /*final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {*/
                    //Do something after 100ms
                    callApiGettingStatus(activity);
                    layout.dismiss();
                     /*   }
                    }, 10000);
                }*/
                }
                catch (Exception ex) {
                    cancelProgressDialog();
                    ex.printStackTrace();
                }
            }
        });
    }

    //Getting Status of WIFI HotSpot
    private void callApiGettingStatus(final Activity activity)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        try {
            // prepare the Request
            JsonObjectRequest strRequest = new JsonObjectRequest(Request.Method.GET, NetworkConstant.LOGIN_STATUS,null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response!=null) {
                                    JSONObject jsonData = new JSONObject(response.toString());
                                    String clientState = jsonData.getString("clientState");
                                    String nasID = jsonData.getString("nasid");
                                    String challenge = jsonData.getString("challenge");
                                 //   Toast.makeText(activity,""+formattedDate+""+response.toString(), Toast.LENGTH_SHORT).show();
                                    if(!clientState.equals(0)) {
                                        callLoginResponse(activity, challenge);
                                    }
                                    else{
                                        cancelProgressDialog();
                                        Toast.makeText(getActivity(),"Already connected through wifyee Internet",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    cancelProgressDialog();
                                    Toast.makeText(getActivity(),"Some error occur in WIFI HotSpot",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                cancelProgressDialog();
                                Toast.makeText(getActivity(),"Some error occur in WIFI HotSpot",Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
                            }
                            // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cancelProgressDialog();
                            Toast.makeText(getActivity(),"Some error occur in WIFI HotSpot",Toast.LENGTH_SHORT).show();
                           // Toast.makeText(activity, "Error on calling Api Getting Status"+ ""+formattedDate+""+ error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
// add it to the RequestQueue
            requestQueue.add(strRequest);

        } catch (Exception e) {
            cancelProgressDialog();
            Toast.makeText(getActivity(),"Some error occur in WIFI HotSpot",Toast.LENGTH_SHORT).show();
            Timber.e("JSONException. message : " + e.getMessage());
        }

    }

    //Call Login Response on Wi-Fi Hot spot
    private void callLoginResponse(final Activity activity,String challenge) {
        final JSONObject jsondata=new JSONObject();
        try {
            jsondata.put("req_action","LOGIN");
            jsondata.put("req_customercode","95629568");
            jsondata.put("req_password",mLoginRequest.pincode);
            jsondata.put("req_username", mLoginRequest.clientmobile+"@wifyee");
            jsondata.put("uamip","10.0.0.1");
            jsondata.put("uamport","3990");
            jsondata.put("challenge",challenge);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(NetworkConstant.LOGON_APP_USER_STATUS_ON_WI_FI)
                .addJSONObjectBody(jsondata)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null) {
                                JSONObject jsonLoginData = response.getJSONObject("login");
                                JSONObject parametersdata = jsonLoginData.getJSONObject("parameter");
                                String responseKey = parametersdata.getString("response");
                                callApiLoginUserToInternet(activity,responseKey);
                                Timber.d("Got Success...");
                                Timber.d("handleActionGetLogOnInfo => JSONObject response ==>" + new Gson().toJson(response));
                            } else {
                                cancelProgressDialog();
                                Timber.d("Got failure inGetLogOnInfoResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Toast.makeText(mContext,"Wifi network Slow .Please Check Your Internet",Toast.LENGTH_SHORT).show();
                                Timber.w("handleGetLogOnInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                            }
                        } catch (Exception e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                            Toast.makeText(mContext,"Wifi network Slow .Please Check Your Internet",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mContext,"Wifi network Slow .Please Check Your Internet",Toast.LENGTH_SHORT).show();

                    }
                });
    }
    //Call Api To Login User to Internet
    private void callApiLoginUserToInternet(final Activity activity,String responseKey)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        try {
            StringBuilder builder=new StringBuilder();
            builder.append(WifiConstant.WIFYEE_HOTSPOT_LOGIN_USER)
                    .append(mLoginRequest.clientmobile+"@wifyee").append("&")
                    .append(WifiConstant.WIFYEE_HOTSPOT_LOGIN_USER_RESPONSE)
                    .append(responseKey);
            Log.e("Builder Value",builder.toString());
            // prepare the Request
            StringRequest strRequest = new StringRequest(Request.Method.GET, NetworkConstant.LOGON_STATUS_ON_WI_FI+builder,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response!= null) {

                                cancelProgressDialog();
                               // Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
                                //callApiGettingStatus(activity);
                            }else {
                                cancelProgressDialog();
                                Toast.makeText(mContext,"Wifi network Slow .Please Check Your Internet",Toast.LENGTH_SHORT).show();
                            }
                           // Toast.makeText(activity,""+formattedDate+""+response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            cancelProgressDialog();
                            Toast.makeText(activity, "Error on calling Log On to Internet"+ ""+formattedDate+""+error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
             // add it to the RequestQueue
            requestQueue.add(strRequest);

        } catch (Exception e) {
            cancelProgressDialog();
           Toast.makeText(activity, "Error on calling Log On to Internet"+ ""+formattedDate+""+e.toString(), Toast.LENGTH_SHORT).show();
            Timber.e("JSONException. message : " + e.getMessage());
        }

    }
    @Override
    public void onClick(View v) {

        if(v==offersNears) {
            startActivity(new Intent(getActivity(), ProductsSellActivity.class));
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        if(v==followBrandsLayout) {
            startActivity(new Intent(getActivity(), FollowBrandsActivity.class));
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        if(v==bookExperienceLayout) {
            startActivity(new Intent(getActivity(), BookExperinceActivity.class));
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        if(v==broadbandLayout) {
            startActivity(new Intent(getActivity(), RequestBroadbandActivity.class));
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        if(v==shoppingLayout) {
            startActivity(new Intent(getActivity(), MerchantCategoryListActivity.class));
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }

        if(v==helpDesk) {

            showBottomSheetDialogFragment();
            //showPopupHelpDesk(getActivity());
        }

        if(v==connect) {
            String strWiFyeeNamesecond="Wifyee";
            if(strWiFyeeNamesecond.equals("Wifyee")) {
                connectWifiOpen(strWiFyeeNamesecond, getActivity());
            }
            else {
                Toast.makeText(getActivity(),"This network is not Available",Toast.LENGTH_SHORT).show();
            }
        }
    }
   //Create Popup of Help Desk
    private void showPopupHelpDesk(final Activity context)
    {
        final Dialog layout = new Dialog(context);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_help_desk);
        // Set dialog title
        layout.setTitle("Helpline Number");
        Button yes = (Button) layout.findViewById(R.id.yes);
        TextView firstPhoneNumber=(TextView) layout.findViewById(R.id.helpline_number_first);
        TextView secondPhoneNumber=(TextView) layout.findViewById(R.id.helpline_number_second);
        layout.show();
        firstPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:02262361058"));
                startActivity(callIntent);*/
            }
        });

        secondPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9619610799"));
                startActivity(callIntent);*/
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
    }

    /**
     * recycle view adapter
     */
    private class RecycleViewPagerAdapter extends RecyclerView.Adapter<RecycleViewPagerAdapter.MyViewHolder> {

        private String[] title = {
                getString(R.string.menu_prepaid),
                getString(R.string.menu_postpaid),
                getString(R.string.menu_landline),
                getString(R.string.menu_electricity),
                "Add Money",
                //"Request Credit",
                "Send Money",
                "Pay Merchant",
                "Request Money",
                "Order Broadband",
                //"Ticketing",
                "More"
        };

        private int[] mTabsIcons = {
                R.drawable.ic_prepaid,
                R.drawable.ic_postpaid,
                R.drawable.ic_landline,
                R.drawable.ic_electricity,
                R.drawable.ic_add_money,
                //R.drawable.ic_request_credit,
                R.drawable.ic_send_money,
                R.drawable.ic_pay,
                R.drawable.ic_request_money,
                R.drawable.ic_broadband,
                //R.mipmap.ticketing,
                R.drawable.ic_more

        };

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView txtView;
            public ImageView icon;
            public RelativeLayout itemHolder;

            public MyViewHolder(View view) {
                super(view);
                txtView = (TextView) view.findViewById(R.id.title);
                icon = (ImageView) view.findViewById(R.id.icon);
                itemHolder = (RelativeLayout) view.findViewById(R.id.list_item_view);
            }
        }
        public RecycleViewPagerAdapter() {
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pay_recharge_item, parent, false);

            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.txtView.setText(title[position]);
            holder.icon.setImageResource(mTabsIcons[position]);
            holder.txtView.setTypeface(Fonts.getRegular(mContext));
            holder.itemHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createPrepaidActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 1:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createPostpaidActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 2:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createLandLineActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 3:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createElectricityBillPaymentActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 4:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createAddMoneyActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        /*case 5:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createRequestCreditActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;*/
                        case 5:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createSendMoneyActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 6:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.scanQRCodeActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 7:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createRequestMoneyActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 8:
                            if (getActivity() != null && isAdded()) {
                                startActivity(new Intent(getContext(), RequestBroadbandActivity.class));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        /*case 9:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createTicketingActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;*/
                        case 9:
                            if (getActivity() != null && isAdded()) {
                                startActivity(new Intent(getActivity(), MoreCategory.class));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        /*case 12:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.scanQRCodeActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 13:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createAddMoneyActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 14:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createRequestCreditActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 15:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createTicketingActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 16:
                            if (getActivity() != null && isAdded()) {
                                startActivity(new Intent(getActivity(), BookExperinceActivity.class));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 17:
                            if (getActivity() != null && isAdded()) {
                                startActivity(new Intent(getActivity(), RequestBroadbandActivity.class));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;*/

                        default:
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return title.length;
        }
    }

    /**
     * recycle view adapter
     */
    private class RecycleViewNestedPagerAdapter extends RecyclerView.Adapter<RecycleViewNestedPagerAdapter.MyViewHolder> {

        private String[] title = {
                getString(R.string.menu_add_money),
                getString(R.string.menu_request_credit),
                getString(R.string.menu_offer_discount),
                getString(R.string.menu_ticketing),
                getString(R.string.menu_send_money),
                getString(R.string.menu_request_money),
                getString(R.string.menu_pay_merchant),
                getString(R.string.menu_split_money),
                getString(R.string.menu_transaction)};

        private int[] mTabsIcons = {
                R.drawable.ic_payment,
                R.drawable.ic_request_credit,
                R.mipmap.offers_discounts,
                R.mipmap.ticketing,
                R.mipmap.send_money_icon,
                R.mipmap.request_money_icon,
                R.mipmap.pay_merchant,
                R.mipmap.split_money,
                R.drawable.ic_transaction};

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txtView;
            public ImageView icon;
            public LinearLayout itemHolder;

            public MyViewHolder(View view) {
                super(view);
                txtView = (TextView) view.findViewById(R.id.title_below);
                icon = (ImageView) view.findViewById(R.id.icon);
                itemHolder = (LinearLayout) view.findViewById(R.id.list_item_view_horizontal);
            }
        }


        public RecycleViewNestedPagerAdapter() {
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.horizental_list_below_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.txtView.setText(title[position]);
            holder.icon.setImageResource(mTabsIcons[position]);

            holder.itemHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createAddMoneyActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 1:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createRequestCreditActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 3:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createTicketingActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 4:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createSendMoneyActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 5:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createRequestMoneyActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 6:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.scanQRCodeActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 7:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createSplitMoneyActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;

                        case 8:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createTransactionActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return title.length;
        }
    }

    /**
     * recycle view adapter
     */

    private class RecycleViewOrdersPagerAdapter extends RecyclerView.Adapter<RecycleViewOrdersPagerAdapter.MyViewHolder> {

       /* private String[] title = {
                getString(R.string.order_food),
                getString(R.string.menu_medicine),
                getString(R.string.menu_product),
                getString(R.string.connect),
                getString(R.string.buy_plans)
               };*/

        private String[] title = {
                getString(R.string.order_food),
                getString(R.string.menu_medicine),
                "Dairy",
                "Fruits",
                "Vegetables",
                "Meat"
        };

        private int[] mTabsIcons = {
                R.drawable.ic_food_new,
                R.drawable.ic_medicine_new,
                R.drawable.ic_dairy_new,
                R.drawable.ic_fruits_new,
                R.drawable.ic_vegetables_new,
                R.drawable.ic_meat_new,
        };

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txtView;
            public ImageView icon;
            public RelativeLayout itemHolder;

            public MyViewHolder(View view) {
                super(view);
                txtView = (TextView) view.findViewById(R.id.title_below);
                icon = (ImageView) view.findViewById(R.id.icon);
                itemHolder = (RelativeLayout) view.findViewById(R.id.list_item_view_horizontal);
            }
        }

        public RecycleViewOrdersPagerAdapter() {
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ex_item, parent, false);

            /*int height = parent.getMeasuredHeight() / 4;
            itemView.setMinimumHeight(height);*/
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.txtView.setText(title[position]);
            holder.icon.setImageResource(mTabsIcons[position]);
            holder.txtView.setTypeface(Fonts.getRegular(mContext));
            //holder.itemHolder.setBackgroundResource(colors[position]);

            holder.itemHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createAddOderFoodActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;

                        case 1:
                            if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createMedicinePreciptionActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 2:
                            if (getActivity() != null && isAdded()) {
                               Intent productIntent = IntentFactory.createDairyProductActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "12");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Dairy");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 3:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyProductActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "14");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Fruits");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }

                            //startActivity(IntentFactory.createProductActivity(getContext()));
                            //getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                            /*if (getActivity() != null && isAdded()) {
                                startActivity(IntentFactory.createRequestMoneyActivity(getContext()));
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                String strWiFyeeNamesecond="Wifyee";
                               if(strWiFyeeName.equals("wifyee testing")) {
                                    connectWifiOpen(strWiFyeeName,getActivity());
                                }
                                if(strWiFyeeNamesecond.equals("Wifyee")) {
                                    connectWifiOpen(strWiFyeeNamesecond, getActivity());
                                }
                                else {
                                    Toast.makeText(getActivity(),"This network is not Available",Toast.LENGTH_SHORT).show();
                                }
                            }*/
                            break;

                        case 4:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyProductActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "15");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Vegetables");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                //startActivity(IntentFactory.createWiFiPlansActivity(getContext()));
                                //getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;

                        case 5:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyProductActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "16");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Meat");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
//                            if (getActivity() != null && isAdded()) {
//                                startActivity(IntentFactory.createsellingProductsActivity(getContext()));
//                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//                            }

                        default:
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return title.length;
        }
    }


    //RecyclerView Grocery
    private class RecycleViewGroceryAdapter extends RecyclerView.Adapter<RecycleViewGroceryAdapter.MyViewHolder> {

        private String[] title = {
                "Cooking Oil",
                "Rice",
                "Sugar",
                "Soap | Shampoo",
                "Masalas",
                "Wheat",
                "Atta | Rice",//214
                "Dal | Pulses",//261,266
                "Dry Fruits",//228
                "Healthy Oils",
                "Namkeens",//215
                "Noodles | Pasta",//271,229
                "Health Drinks",//298
                "Tea | Coffee",//220,300
                "Kitchen Cleaning",//268
                "Exotic Corner",
                "Oral Care",//264
                "Liquid Detergents",
                "Handwash | Antiseptic",//272,273
                "Frozen Snack",
                "Fresheners Repellents",
                "Floor Toilet Cleaners",//296
                "Face Cleaners | Moisturisers",//225
                "Fabric Conditioners",
                "Detergent Powders",//224
                "Brooms | Brushes"//297
                //"Bath | Hair"
        };

        private int[] mTabsIcons = {
                R.drawable.ic_oil,
                R.drawable.ic_rice,
                R.drawable.ic_sugar,
                R.drawable.ic_soap,
                R.drawable.ic_masalas,
                R.drawable.ic_wheat,
                R.drawable.ic_atta_rice,
                R.drawable.ic_dal_pulses,
                R.drawable.ic_dry_fruits,
                R.drawable.ic_healthy,
                R.drawable.ic_namkeens,
                R.drawable.ic_noodles_pasta,
                R.drawable.ic_health_drinks,
                R.drawable.ic_tea_coffee,
                R.drawable.ic_kitchen_cleaning,
                R.drawable.ic_exotic_corner,
                R.drawable.ic_oral_care,
                R.drawable.ic_liquid_detergents,
                R.drawable.ic_handwash_antiseptic,
                R.drawable.ic_frozen_snacks,
                R.drawable.ic_freshners_repellents,
                R.drawable.ic_floor_toilet_cleaners,
                R.drawable.ic_face_cleaners_moisturisers,
                R.drawable.ic_fabric_conditioners,
                R.drawable.ic_detergent_powders,
                R.drawable.ic_brooms_brushes
                //R.drawable.ic_bath_hair
        };

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txtView;
            public ImageView icon;
            RelativeLayout itemHolder;

            public MyViewHolder(View view) {
                super(view);
                txtView = (TextView) view.findViewById(R.id.title_below);
                icon = (ImageView) view.findViewById(R.id.icon);
                itemHolder = (RelativeLayout) view.findViewById(R.id.list_item_view_horizontal);
            }
        }

        public RecycleViewGroceryAdapter() {
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ex_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.txtView.setText(title[position]);
            holder.icon.setImageResource(mTabsIcons[position]);
            holder.txtView.setTypeface(Fonts.getRegular(mContext));

            holder.itemHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Oil");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "3");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;

                        case 1:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Rice");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "4");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;

                        case 2:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Sugar");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "12");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;

                        case 3:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Soap");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "217,219");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;

                        case 4:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Masalas");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "213");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;

                        case 5:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Wheat");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "13");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;

                        case 6:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Atta | Rice");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "214");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 7:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Dal | Pulses");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, " 212,261,266");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 8:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Dry Fruits");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "228");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 10:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Namkeens");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "215");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 11:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Noodels | Pasta");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "271,229");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 12:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Health Drinks");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "298");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 13:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Tea | Coffee");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "220,300");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 14:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Kitchen Cleaning");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "268");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 16:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Oral Care");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "264,269");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 18:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Handwash | Antiseptic");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "272");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 21:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Floor Toilet Cleaners");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "296");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 22:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Face Cleaners | Moisturisers");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "225");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 24:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Detergent Powders");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "224");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;
                        case 25:
                            if (getActivity() != null && isAdded()) {
                                Intent productIntent = IntentFactory.createDairyItemLisActivity(getContext());
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA, "10");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA1, "Brooms | Brushes");
                                productIntent.putExtra(NetworkConstant.EXTRA_DATA_CATEGORY_ID, "297");
                                startActivity(productIntent);
                                getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            }
                            break;

                        default:
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return title.length;
        }
    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getActivity().getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Toast.makeText(getActivity(), "Number=" + num, Toast.LENGTH_LONG).show();
                               /* Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse(num));
                                startActivity(callIntent);*/
                            }
                        }
                    }
                    break;
                }
        }
    }

    public void showErrorDialog(final String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                arg0.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    public void showBottomSheetDialogFragment() {
        BottomSheetHelpDeskFragment bottomSheetFragment = new BottomSheetHelpDeskFragment();
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
    }
}
