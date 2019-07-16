package com.wifyee.greenfields.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.dairyorder.DairyNetworkConstant;
import com.wifyee.greenfields.dairyorder.DairyOrderSummaryWebViewActivity;
import com.wifyee.greenfields.dairyorder.DairyProductIntentService;
import com.wifyee.greenfields.dairyorder.JSONBuilder;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MedicineWebViewActivity extends AppCompatActivity {

    private WebView webView;
    LinearLayout webLoading = null;
    private static String TAG = MedicineWebViewActivity.class.getSimpleName();
    private ProgressDialog progressDialog = null;
    private Toolbar toolbar;
    public static final String ACTION_STATUS = "action_success";
    public static final String ACTION_STATUS_FAIL = "action_fail";
    public static final String ACTION_STATUS_VALUE = "action_value";
    private Context mcontext;

    private String[] broadCastReceiverActionList = {
            ACTION_STATUS,
            ACTION_STATUS_FAIL
    };

    String amount,discount_amt,claimType,voucherId,voucherNo,orderId,deliverAmt,orderOn;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_payment_web);
        webView = (WebView) findViewById(R.id.webView1);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webLoading = (LinearLayout) findViewById(R.id.refresh_list);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolBarTitle = toolbar.findViewById(R.id.toolbar_title);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.secondaryPrimary), PorterDuff.Mode.SRC_ATOP);
        }

        toolBarTitle.setTypeface(Fonts.getSemiBold(this));

        //get data from intent
        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        discount_amt = intent.getStringExtra("discount_amt");
        claimType = intent.getStringExtra("claim_type");
        voucherId = intent.getStringExtra("voucher_id");
        voucherNo = intent.getStringExtra("voucher_no");
        orderId = intent.getStringExtra("order_id");
        deliverAmt = intent.getStringExtra("deliverAmt");
        orderOn = intent.getStringExtra("order_on");

        mcontext = this;

        // For back button in tool bar handling.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

        webView.addJavascriptInterface(new MedicineWebViewActivity.MyJavaScriptInterface(webView,webLoading), "INTERFACE");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("status",""+errorCode);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
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

        //if(intent!=null) {
            JSONObject data = new JSONObject();
            try {
                data.put("amount",amount);
                data.put("loginType", "Client");
                data.put("purpose", "Medicine Payment");
                data.put("ref_no", orderId);
                data.put("clientId", LocalPreferenceUtility.getUserCode(MedicineWebViewActivity.this));
                data.put("phone",LocalPreferenceUtility.getUserMobileNumber(MedicineWebViewActivity.this));
                data.put("email","");
                data.put("responseUrl", DairyNetworkConstant.MEDICINE_PAYMENT_RESPONSE_URL);
                data.put("webhook", DairyNetworkConstant.WEBHOOK);
                data.put("buyer_name",LocalPreferenceUtility.getUserLastName(MedicineWebViewActivity.this)+" "+LocalPreferenceUtility.getUserFirstsName(MedicineWebViewActivity.this));
                //Log.e("data",data.toString());
                //Log.e("url",DairyNetworkConstant.MEDICINE_PAYMENT_URL);
                //Show the web page
                webView.postUrl(DairyNetworkConstant.MEDICINE_PAYMENT_URL, ("" + data.toString()).getBytes());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        //}
    }

    /* An instance of this class will be registered as a JavaScript interface */
    class MyJavaScriptInterface {

        private WebView webVieww;
        private LinearLayout webLoadingg = null;

        public MyJavaScriptInterface( WebView wv,LinearLayout wl) {
            webVieww = wv;
            webLoadingg = wl;
        }

        @SuppressWarnings("unused")
        @JavascriptInterface
        public void processContent(String aContent) {
            final String content = aContent;
            Log.e("PAYMENT",aContent);
            if (content.toLowerCase().contains("status") || content.toLowerCase().contains("Credit")){
                // || content.toLowerCase().contains(DairyNetworkConstant.ORDER_REF_ID) ) {

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
                broadcastIntent.setAction(ACTION_STATUS);
                broadcastIntent.putExtra(ACTION_STATUS_VALUE, content);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(transactionHistoryReceiver);
        if ( pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }
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

    /**
     * Handling broadcast event for payment status
     */
    private BroadcastReceiver transactionHistoryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionTransactionHistory = intent.getAction();
            if (actionTransactionHistory.equals(ACTION_STATUS)) {
                String content = intent.getStringExtra(ACTION_STATUS_VALUE);
                //Log.e("Intentcontent",content);
                try {
                    JSONObject object = new JSONObject(content);
                    String refId = object.getString(ResponseAttributeConstants.PAYMENT_REQ_ID);
                    actionAddPostOrder(refId);
                    /*if(object.getInt(ResponseAttributeConstants.STATUS)!=0){
                            actionAddPostOrder(refId);
                    }else {
                        //showSuccessDialog("Payment Failed");
                        //actionAddPostOrder(refId);
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*if(content.contains("Credit")) {
                    try {
                        webLoading.setVisibility(View.VISIBLE);
                        String refId1 = content.substring(content.indexOf("payment_request"));
                        String refId2 = refId1.substring(refId1.indexOf("payment-requests")+1);
                        String refId3 = refId2.substring(refId2.indexOf("/")+1);
                        String refId = refId3.substring(0,(refId3.indexOf("/") - 1));
                        Log.e("refId1","1"+refId1);
                        Log.e("refId2","2"+refId2);
                        Log.e("refId3","3"+refId3);
                        Log.e("refId","a"+refId);
                        *//*DairyProductIntentService.startActionAddOrder(DairyOrderSummaryWebViewActivity.this, orderData,
                                payAmount, DairyNetworkConstant.PAYMENT_MODE_INSTAMOJO, refId,
                                location,latitude,longitude,complete_add,discount_amt,dateFrom,dateTo,
                                perDay,claimType,voucherId,voucherNo);*//*
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    showSuccessDialog("Payment Failed");
                }*/
            }
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    private void showSuccessDialog(final String msg){
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

    private void actionAddPostOrder(String refId){
        webLoading.setVisibility(View.VISIBLE);

        JSONObject json = new JSONObject();

        try {
            json = JSONBuilder.getPostAddOrderJson(mcontext, orderId, "", deliverAmt, discount_amt,
                    amount, claimType, voucherId, voucherNo, orderOn, DairyNetworkConstant.PAYMENT_MODE_INSTAMOJO,refId);
        }catch (Exception e) {
            e.printStackTrace();
        }
        //Log.e("POST_ORDER_JSON",json.toString());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        pDialog = new SweetAlertDialog(mcontext, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Please wait...");
        pDialog.setCancelable(false);

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING+NetworkConstant.MEDICINE_UPDATE)
                .addJSONObjectBody(json)
                .setOkHttpClient(okHttpClient)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called post add order");
                        Log.e("responsePostAddOrd",response.toString());
                        webLoading.setVisibility(View.GONE);
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                pDialog.setTitleText("Success!")
                                        .setContentText(response.getString(ResponseAttributeConstants.MSG))
                                        .setConfirmText("OK")
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        onPause();
                                        finish();
                                    }
                                });
                                pDialog.show();

//
                            } else {
                                pDialog.setTitleText("Failed! Please Try Again.")
                                        .setContentText(response.getString(ResponseAttributeConstants.MSG))
                                        .setConfirmText("OK")
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        onPause();
                                        finish();
                                    }
                                });
                                pDialog.show();

                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("ErrorAddOrder",error.toString());
                        Timber.e("called onError of User dairy order API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        pDialog.setTitleText("Error!")
                                .setContentText(error.toString())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        pDialog.show();
                    }
                });
    }

}
