package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.foodorder.DeductMoneyWalletResponse;
import com.wifyee.greenfields.fragments.ProfileFragment;
import com.wifyee.greenfields.mapper.ModelMapper;
import com.wifyee.greenfields.models.requests.AddMoneyWallet;
import com.wifyee.greenfields.models.requests.AirtimeRequest;
import com.wifyee.greenfields.models.requests.DeductMoneyWallet;
import com.wifyee.greenfields.models.requests.InstaMojoRequest;
import com.wifyee.greenfields.models.requests.MerchantRequest;
import com.wifyee.greenfields.models.requests.PayUPaymentGatewayRequest;
import com.wifyee.greenfields.models.requests.PlanPaymentRequest;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.PayUPaymentGatewayResponse;
import com.wifyee.greenfields.models.response.PlanDataSummary;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

public class WebViewActivity extends BaseActivity {

    private WebView webView;
    LinearLayout webLoading = null;
    private static String TAG = WebViewActivity.class.getSimpleName();
    private ProgressDialog progressDialog = null;
    private Toolbar toolbar;
    public static final String ACTION_STATUS_VALUE = "action_value";
    private PayUPaymentGatewayRequest mPayUPaymentGatewayRequest;
    private AddMoneyWallet addMoneyWallet;
    private DeductMoneyWallet deductMoneyWallet;
    public PlanPaymentRequest planPaymentReques;
    public InstaMojoRequest instaMojoRequest;
    public MerchantRequest merchantRequest;
    private PlanDataSummary planDataSummary;
    private String clientMobile = "";
    private String pincode = "";
    private static int SPLASH_TIME_OUT = 180000;
    private String hash = "";
    private AirtimeRequest airtimeBeanData;
    private String transactionId = "";
    private String planID = "";
    int temp = 0;
    private Intent intent;
    private String todayString = "";
    private String instaMojoString = "";
    private Dialog layout;
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_SUCCESS,
            NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_FAIL,
            NetworkConstant.STATUS_USER_INSTA_MOJO_PAYMENT_SUCCESS,
            NetworkConstant.STATUS_USER_INSTA_MOJO_PAYMENT_FAIL
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webLoading = (LinearLayout) findViewById(R.id.refresh_list);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        intent = getIntent();
        airtimeBeanData = (AirtimeRequest) intent.getSerializableExtra(PaymentConstants.AIRTIME_EXTRA);
        planID = intent.getStringExtra("planId");
        instaMojoString = intent.getStringExtra("InstaMojo");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        String format = simpleDateFormat.format(new Date());
        todayString = format;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.payment_process_title));

            // For back button in tool bar handling.
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }

        webView.addJavascriptInterface(new MyJavaScriptInterface(webView, webLoading), "INTERFACE");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("status", "" + errorCode);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.d("status", "" + errorResponse);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.d("status", "" + error);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.d("status", "" + url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webLoading.setVisibility(View.GONE);
                view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
            }
        });
        if (instaMojoString != null) {
            PayUPaymentGatewayRequest mPayUPaymentGatewayRequest = (PayUPaymentGatewayRequest) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
            instaMojoRequest = new InstaMojoRequest();
            instaMojoRequest.setLoginType("super");
            instaMojoRequest.setBuyerName(mPayUPaymentGatewayRequest.firstname);
            instaMojoRequest.setBuyerEmail(mPayUPaymentGatewayRequest.email);
            instaMojoRequest.setBuyerMobile(LocalPreferenceUtility.getUserMobileNumber(this));
            instaMojoRequest.setAmount(mPayUPaymentGatewayRequest.amount);
            instaMojoRequest.setResponseUrl("http://wifyee.com/thanks.php");
            MobicashIntentService.startActionInstaMojoPayment(WebViewActivity.this, instaMojoRequest);
        }

        webView.getSettings().setJavaScriptEnabled(true);
        if (intent != null) {
            JSONObject jsonObjectPayRequest = new JSONObject();
            try {
                PayUPaymentGatewayRequest mPayUPaymentGatewayRequest = (PayUPaymentGatewayRequest) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                try {
                    jsonObjectPayRequest.put(ResponseAttributeConstants.AMOUNT, mPayUPaymentGatewayRequest.amount);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.FIRST_NAME, mPayUPaymentGatewayRequest.firstname);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.LAST_NAME, mPayUPaymentGatewayRequest.lastname);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.EMAIL, mPayUPaymentGatewayRequest.email);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.PHONE, mPayUPaymentGatewayRequest.phone);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.PRODUCT_INFO, mPayUPaymentGatewayRequest.productinfo);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.MERCHANT_ID, mPayUPaymentGatewayRequest.merchantId);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.PG, mPayUPaymentGatewayRequest.pg);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.ADDRESS1, mPayUPaymentGatewayRequest.address1);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.CITY, mPayUPaymentGatewayRequest.city);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.STATE, mPayUPaymentGatewayRequest.state);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.COUNTRY, mPayUPaymentGatewayRequest.country);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.ZIP_CODE, mPayUPaymentGatewayRequest.zipcode);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.USER_ID, mPayUPaymentGatewayRequest.userId);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.USER_TYPE, mPayUPaymentGatewayRequest.userType);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.BANK_CODE, mPayUPaymentGatewayRequest.bankcode);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.CCNAME, mPayUPaymentGatewayRequest.ccname);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.CCNUM, mPayUPaymentGatewayRequest.ccnum);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.CCVV, mPayUPaymentGatewayRequest.ccvv);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.REDIRECT_URL, mPayUPaymentGatewayRequest.redirectURL);
                    jsonObjectPayRequest.put(ResponseAttributeConstants.TRANX_ID, mPayUPaymentGatewayRequest.tranxid);
                    transactionId = mPayUPaymentGatewayRequest.tranxid;
                    if (!mPayUPaymentGatewayRequest.ccexpmon.equals("")) {
                        jsonObjectPayRequest.put(ResponseAttributeConstants.CCEXPMON, mPayUPaymentGatewayRequest.ccexpmon);
                    }
                    if (!mPayUPaymentGatewayRequest.ccexpyr.equals("")) {
                        jsonObjectPayRequest.put(ResponseAttributeConstants.CCEXPYR, mPayUPaymentGatewayRequest.ccexpyr);
                    }

                } catch (JSONException e) {
                    Timber.e("JSONException. message : " + e.getMessage());
                }
                // Show the web page
                //Log.e("-JSON-",jsonObjectPayRequest.toString());
                webView.postUrl(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_PAYU_PAYMENT_GATEWAY_END_POINT,
                        (jsonObjectPayRequest.toString()).getBytes());
                // webView.loadUrl("http://google.com");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (intent != null) {
            try {
                addMoneyWallet = (AddMoneyWallet) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA_WALLET);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(ResponseAttributeConstants.ADD_WALLET_AMOUNT, addMoneyWallet.amount);
                    jsonObject.put(ResponseAttributeConstants.ADD_WALLET_RECIEVER, addMoneyWallet.clientreceiver);
                    jsonObject.put(ResponseAttributeConstants.ADD_WALLET_HASH, addMoneyWallet.hash);
                    jsonObject.put(ResponseAttributeConstants.ADD_WALLET_PINCODE, addMoneyWallet.pincode);
                    jsonObject.put(ResponseAttributeConstants.ADD_WALLET_TXN_ID, addMoneyWallet.txnId);
                    //Log.e("WALLETjson",jsonObject.toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (intent != null) {
            PayUPaymentGatewayRequest mPayUPaymentGatewayRequest = (PayUPaymentGatewayRequest) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            String currentDateandTime = sdf.format(new Date());
            planPaymentReques = new PlanPaymentRequest();
            planPaymentReques.plan_id = mPayUPaymentGatewayRequest.planId;
            planPaymentReques.macAddress = MobicashUtils.getMacAddress(this);
            planPaymentReques.paid_amount = mPayUPaymentGatewayRequest.amount;
            planPaymentReques.paid_tax = "0.0";
            planPaymentReques.invoice_id = mPayUPaymentGatewayRequest.tranxid;
            planPaymentReques.paid_on = currentDateandTime;
            planPaymentReques.mobile = mPayUPaymentGatewayRequest.mobile;
            planPaymentReques.macAddress = MobicashUtils.getMacAddress(this);
            merchantRequest = new MerchantRequest();
            if (planPaymentReques.plan_id != null) {
                MobicashIntentService.startActionPlanPurchase(WebViewActivity.this, planPaymentReques);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CallApiForDataUsage(WebViewActivity.this, planPaymentReques);
                    }
                });
            }
            if (mPayUPaymentGatewayRequest.merchantId != null) {
                String merchantId = mPayUPaymentGatewayRequest.merchantId;
                String amount = mPayUPaymentGatewayRequest.amount;
                String mobile = LocalPreferenceUtility.getUserMobileNumber(this);
                StringBuilder sb = new StringBuilder(merchantId);
                sb.append(amount);
                sb.append(mobile);
                merchantRequest.mobile = mobile;
                merchantRequest.amount = amount;
                merchantRequest.merchantId = merchantId;
                try {
                    String hashValue = "";
                    hashValue = MobicashUtils.getSha1(sb.toString());
                    merchantRequest.hash = hashValue;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
        //  MobicashIntentService.startActionClientPaymentDetails(mContext, paymentRequest);
        deductMoneyWallet = new DeductMoneyWallet();
        deductMoneyWallet.description = "";
        if (intent != null) {
            PayUPaymentGatewayRequest mPayUPaymentGatewayRequest = (PayUPaymentGatewayRequest) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
            if (mPayUPaymentGatewayRequest.description != null) {
                clientMobile = LocalPreferenceUtility.getUserMobileNumber(this);
                pincode = LocalPreferenceUtility.getUserPassCode(this);
                StringBuilder sb = new StringBuilder(clientMobile);
                sb.append(mPayUPaymentGatewayRequest.amount);
                sb.append(MobicashUtils.md5(pincode));
                deductMoneyWallet.clientMobile = clientMobile;
                deductMoneyWallet.merchantId = mPayUPaymentGatewayRequest.merchantId;
                deductMoneyWallet.amount = mPayUPaymentGatewayRequest.amount;
                deductMoneyWallet.pincode = pincode;
                deductMoneyWallet.description = "Deduction from Wallet";
                deductMoneyWallet.merchantDebit = mPayUPaymentGatewayRequest.merchantDebit;
                //  addMoneyWallet.txnId=mPayUPaymentGatewayRequest.tranxid;
                try {
                    String hashValue = "";
                    hashValue = MobicashUtils.getSha1(sb.toString());
                    deductMoneyWallet.hash = hashValue;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //Call Api for data usage
    private void CallApiForDataUsage(final WebViewActivity context, final PlanPaymentRequest planPaymentReques) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("macAddress",macAddrr);
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
                        callApiPurchaseOnWifyeeServer(planDataSummary, planPaymentReques);
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
    }

    //Plan Update to Wifyee Server change
    private void callApiPurchaseOnWifyeeServer(PlanDataSummary planDataSummary, PlanPaymentRequest planRequest) {
        final JSONObject jsondata = new JSONObject();
        if (planRequest.mobile.equals(LocalPreferenceUtility.getUserMobileNumber(this))) {
            try {
                jsondata.put("planId", planRequest.plan_id);
                jsondata.put("status", "active");
                jsondata.put("userMobile", planRequest.mobile);
                //jsondata.put("remaining_plan_time",planDataSummary.getTime_remaining());
                changetimetosec(planDataSummary.getTime_remaining());
                jsondata.put("remaining_plan_time", temp);
                jsondata.put("purchased_on", todayString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                jsondata.put("planId", planRequest.plan_id);
                jsondata.put("status", "active");
                jsondata.put("userMobile", planRequest.mobile);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        AndroidNetworking.post(NetworkConstant.PLAN_UPDATE_ON_WIFYEE_SERVER)
                .addJSONObjectBody(jsondata)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                JSONObject message = response.getJSONObject("msg");
                                Toast.makeText(WebViewActivity.this, "Update Plan Success" + message, Toast.LENGTH_SHORT).show();
                                Timber.d("Got Success...");
                                Timber.d("handleActionGetLogOnInfo = > JSONObject response ==>" + new Gson().toJson(response));
                            } else {
                                Timber.d("Got failure inGetLogOnInfoResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleGetLogOnInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                            }
                        } catch (Exception e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
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

    private void changetimetosec(String time_remaining) {
        String day = "", min = "", hr = "", sec = "";
        //CallApiForDataUsage();
        temp = 0;
        int days = 0, second = 0, minute = 0, hour = 0;
        String hh = time_remaining;
        //     String hh="4 min";
        String[] h1 = hh.split("\\s*,\\s*");
        {
            if (h1[0].toLowerCase().contains("days")) {
                h1[0] = h1[0].replaceAll("days", "");
                day = h1[0].trim();

            }
            if ((h1[0].toLowerCase().contains("hr"))) {
                h1[0] = h1[0].replaceAll("hr", "");
                hr = h1[0].trim();
            }
            if ((h1[0].toLowerCase().contains("min"))) {
                h1[0] = h1[0].replaceAll("min", "");
                min = h1[0].trim();
            }
            if ((h1[0].toLowerCase().contains("sec"))) {
                h1[0] = h1[0].replaceAll("sec", "");
                sec = h1[0].trim();
            }
        }
        if (day.length() > 0) {
            days = Integer.parseInt(day);
            days = 24 * days;
        } else {
            days = 0;
        }
        if (hr.length() > 0) {
            hour = Integer.parseInt(hr);
        } else {
            hour = 0;
        }
        if (min.length() > 0) {
            minute = Integer.parseInt(min);
        } else {
            minute = 0;
        }
        if (sec.length() > 0) {
            second = Integer.parseInt(sec);
        } else {
            second = 0;
        }
        temp = second + (60 * minute) + (3600 * hour) + (3600 * days);
        //Toast.makeText(getApplicationContext(),String.valueOf(temp),Toast.LENGTH_LONG).show();
    }

    /* An instance of this class will be registered as a JavaScript interface */
    class MyJavaScriptInterface {

        private WebView webVieww;
        private LinearLayout webLoadingg = null;

        public MyJavaScriptInterface(WebView wv, LinearLayout wl) {
            webVieww = wv;
            webLoadingg = wl;
        }

        @SuppressWarnings("unused")
        @JavascriptInterface
        public void processContent(String aContent) {
            final String content = aContent;
            Log.e("content",content);
            if (content.toLowerCase().contains("status")) {
                webVieww.post(new Runnable() {
                    @Override
                    public void run() {
                        webVieww.setVisibility(View.GONE);
                    }
                });

                webLoadingg.post(new Runnable() {
                    @Override
                    public void run() {
                        webLoadingg.setVisibility(View.GONE);
                    }
                });
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_SUCCESS);
                broadcastIntent.putExtra(ACTION_STATUS_VALUE, content);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(transactionHistoryReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(transactionHistoryReceiver, new IntentFilter(action));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (layout != null) {
            layout.dismiss();
        }
    }


    /**
     * Handling broadcast event for payment status
     */
    private BroadcastReceiver transactionHistoryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionTransactionHistory = intent.getAction();
            if (actionTransactionHistory.equals(NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_SUCCESS)) {
                String content = intent.getStringExtra(ACTION_STATUS_VALUE);
                Log.e("Getting Response", content);
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(NetworkConstant.ACTION_SHOW_ID_SUCCESS_PAID);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                if (addMoneyWallet != null) {
                    addMoneyWallet.status = "success";
                    MobicashIntentService.startActionAddMoneyWallet(WebViewActivity.this, addMoneyWallet);
                }
                if (intent != null) {
                    try {
                        if (deductMoneyWallet.merchantId != null) {
                            MobicashIntentService.startActionClientPaymentDetails(WebViewActivity.this, merchantRequest);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (actionTransactionHistory.equals(NetworkConstant.STATUS_MERCHANT_PAYMENT_SUCCESS)) {
                    try {
                        showSuccessDialogCustomize(WebViewActivity.this);
                    } catch (Exception ex) {
                        layout.dismiss();
                        ex.printStackTrace();
                    }
                }
                if (deductMoneyWallet != null) {
                    if (!deductMoneyWallet.description.equals("")) {
                        MobicashIntentService.startActionDeductMoneyWallet(WebViewActivity.this, deductMoneyWallet);
                    }
                }
                if (actionTransactionHistory.equals(NetworkConstant.STATUS_ADD_MONEY_WALLET_SUCCESS))
                {
                    try {
                        showSuccessDialogCustomize(WebViewActivity.this);
                    } catch (Exception ex) {
                        layout.dismiss();
                        ex.printStackTrace();
                    }

                }
                if (actionTransactionHistory.equals(NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_SUCCESS))
                    ;
                {
                    try {
                     showSuccessDialogCustomize(WebViewActivity.this);
                    } catch (Exception ex) {
                        layout.dismiss();
                        ex.printStackTrace();
                    }
                }
                if (actionTransactionHistory.equals(NetworkConstant.STATUS_DEDUCTMONEY_WALLET_SUCCESS))
                    ;
                {
                    showSuccessDialogCustomize(WebViewActivity.this);
                }
                if (actionTransactionHistory.equals(NetworkConstant.STATUS_DEDUCT_MONEY_WALLET_FAIL))
                    ;
                {
                    // showErrorDialog("Getting issue in Merchant Pay");
                }
//                if (intent != null) {
//                    try {
//                        if (airtimeBeanData != null) {
//                            MobicashIntentService.startActionAirtimeRecharge(WebViewActivity.this, airtimeBeanData, transactionId);
//                        }
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }

            }
            if (actionTransactionHistory.equals(NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_FAIL)) {
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null) {
                    Timber.d("STATUS_PAYMENT = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    showErrorDialog(failureResponse.msg);
                } else {
                    String errorMessage = getString(R.string.error_message);
                    showErrorDialog(errorMessage);
                }
            }
            if (actionTransactionHistory.equals(NetworkConstant.STATUS_USER_INSTA_MOJO_PAYMENT_SUCCESS)) {
                showSuccessDialogCustomize(WebViewActivity.this);
            }
            if (actionTransactionHistory.equals(NetworkConstant.STATUS_USER_INSTA_MOJO_PAYMENT_FAIL)) {
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null) {
                    Timber.d("STATUS_PAYMENT = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    showErrorDialog(failureResponse.msg);
                } else {
                    String errorMessage = getString(R.string.error_message);
                    showErrorDialog(errorMessage);
                }
            }
        }
    };

    // Merchant Debit from Client
    private void callApiAddMoneyToMerchant(final Activity context, String amount, String merchantId) {
        final JSONObject jsondata = new JSONObject();
        StringBuilder builder = new StringBuilder(merchantId);
        builder.append(amount);
        builder.append("credit");
        String hash = "";
        try {
            hash = MobicashUtils.getSha1(builder.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            jsondata.put("credit_type", "credit");
            jsondata.put("credit_amount", amount);
            jsondata.put("merchant_id", merchantId);
            jsondata.put("hash", hash);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(NetworkConstant.PAY_TO_MERCHANT_MONEY)
                .addJSONObjectBody(jsondata)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                showSuccessDialog(context);
                                Timber.d("Got Success...");
                                Timber.d("handleActionGetLogOnInfo = > JSONObject response ==>" + new Gson().toJson(response));
                            } else {
                                Timber.d("Got failure inGetPayToMerchantInfoResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleGetPayToMerchantInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                            }
                        } catch (Exception e) {
                            Timber.e("JSONException Caught.  Message :" + e.getMessage());
                            cancelProgressDialog();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Get GetPayToMerchant Info API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        cancelProgressDialog();
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("GetPay ToMerchantInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                    }
                });
    }

    //Call Api for success Transaction
    private void calApiSuccessTransaction(String content) {

    }

    //SuccesFully Transaction
    private void showSuccessDialog(Activity activity) {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        layout.setContentView(R.layout.showpopup_success);
        // Set dialog title
        layout.setTitle("Success");
        Button yes = (Button) layout.findViewById(R.id.yes);
        layout.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    finish();
                    layout.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });


    }

    private void onPayUSuccess(PayUPaymentGatewayResponse payUPaymentGatewayResponse, Context mcontext) {
        cancelProgressDialog();
        //sharedPreference.removeFavoriteItem(mcontext, curent_productItem);
        if (payUPaymentGatewayResponse.getResponseCode().equalsIgnoreCase("000")) {
            final Dialog dialog = new Dialog(mcontext);
            dialog.setContentView(R.layout.payu_transaction_result);
            dialog.setTitle("Detail");
            dialog.setCancelable(false);
            if (payUPaymentGatewayResponse.getResponseStatus() != null) {
                ((TextView) dialog.findViewById(R.id.txt_status)).setText("Success");
            }
            if (payUPaymentGatewayResponse.getRefId() != null) {
                ((TextView) dialog.findViewById(R.id.txt_balance)).setText(payUPaymentGatewayResponse.getRefId());
            }
            if (payUPaymentGatewayResponse.getCliIdtId() != null) {
                ((TextView) dialog.findViewById(R.id.txt_clientId)).setText(payUPaymentGatewayResponse.getCliIdtId());
            }

            if (payUPaymentGatewayResponse.getMerIdtId() != null) {
                ((TextView) dialog.findViewById(R.id.txt_transactiondate)).setText(payUPaymentGatewayResponse.getMerIdtId());
            }
            Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
            // if button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    //    MobicashIntentService.startActionSendFoodRequest(mcontext, getaddToCartRequest(String.valueOf(total_amount)));

                    dialog.dismiss();

                }
            });

            dialog.show();
        } else

        {
            // showSuccessDialog("Transaction Fail");
        }
    }

    //SuccesFully Transaction

    private void showSuccessDialogCustomize(final Activity activity) {
        layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_success);
        // Set dialog title
        layout.setTitle("Success");

        Button yes = (Button) layout.findViewById(R.id.yes);
        layout.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (intent != null) {
                        try {
                            if (airtimeBeanData != null) {
                                MobicashIntentService.startActionAirtimeRecharge(WebViewActivity.this, airtimeBeanData, transactionId);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout.dismiss();
                            startActivity(new Intent(activity, MobicashDashBoardActivity.class));
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            finish();
                        }
                    }, 100);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });


    }

    /**
     * show profile upload progress dialog
     */

    protected void showProgressDialog() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("Please wait creating id...");
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

    private void showErrorDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Something went wrong,Please contact Admin.");

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

    public void showErrorDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
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

    private void showSuccessDialog(final String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setTitle("Success")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}