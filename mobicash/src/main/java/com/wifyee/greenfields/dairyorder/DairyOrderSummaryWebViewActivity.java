package com.wifyee.greenfields.dairyorder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
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

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.activity.BaseActivity;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DairyOrderSummaryWebViewActivity extends BaseActivity {

    private WebView webView;
    LinearLayout webLoading = null;
    private static String TAG = DairyOrderSummaryWebViewActivity.class.getSimpleName();
    private ProgressDialog progressDialog = null;
    private Toolbar toolbar;
    public static final String ACTION_STATUS = "action_success";
    public static final String ACTION_STATUS_FAIL = "action_fail";
    public static final String ACTION_STATUS_VALUE = "action_value";
    public static final String ACTION_ADD_WALLET_SUCCESS ="action_add_wallet_success";
    public static final String ACTION_ADD_WALLET_FAILED ="action_add_wallet_failed";
    private String pay_mode="";
    private ArrayList<PlaceOrderData> orderData;
    private String totalAmount;
    private String payAmount,location,latitude,longitude,complete_add;

    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            ACTION_STATUS,
            ACTION_STATUS_FAIL,
            ACTION_ADD_WALLET_SUCCESS,
            ACTION_ADD_WALLET_FAILED,
            DairyNetworkConstant.DAIRY_ADD_ORDER_STATUS_FAILURE,
            DairyNetworkConstant.DAIRY_ADD_ORDER_STATUS_SUCCESS,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_order_summary_web_view);
        webView = (WebView) findViewById(R.id.webView1);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webLoading = (LinearLayout) findViewById(R.id.refresh_list);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.payment_process_title));
        }

        //get data from intent
        orderData = getIntent().getParcelableArrayListExtra("data");
        totalAmount = getIntent().getStringExtra("totalAmount");

        // For back button in tool bar handling.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

        webView.addJavascriptInterface(new MyJavaScriptInterface(webView,webLoading), "INTERFACE");

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

        webView.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        pay_mode = intent.getStringExtra("paymode");
        payAmount = getIntent().getStringExtra("pay_amount");
        location = getIntent().getStringExtra("location");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        complete_add = getIntent().getStringExtra("complete_add ");
        if(intent!=null) {
            JSONObject data = new JSONObject();
            try {
                 data.put("amount",getIntent().getStringExtra("pay_amount"));
                //data.put("amount","10");
                data.put("loginType", "client");
                data.put("userid", LocalPreferenceUtility.getUserCode(DairyOrderSummaryWebViewActivity.this));
                data.put("buyerMobile",LocalPreferenceUtility.getUserMobileNumber(DairyOrderSummaryWebViewActivity.this));
                data.put("buyerEmail",LocalPreferenceUtility.getUserEmail(DairyOrderSummaryWebViewActivity.this));
                data.put("responseUrl", DairyNetworkConstant.PAYMENT_RESPONSE_URL);
                data.put("buyerName",LocalPreferenceUtility.getUserFirstsName(DairyOrderSummaryWebViewActivity.this));
                Log.d("data",data.toString());
                Log.d("url",DairyNetworkConstant.PAYMENT_URL);
                 //Show the web page
                webView.postUrl(DairyNetworkConstant.PAYMENT_URL, ("data=" + data.toString()).getBytes());
              //  webView.loadUrl("http://google.com");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                Log.d("content",content);
                if(content.contains("Credit")) {
                    if (pay_mode.equals(DairyNetworkConstant.PAYMENT_MODE_WALLET)) {
                        //request api add money to wallet
                        showSuccessTransactionDialog();
                        finish();
                    } else if (pay_mode.equals(DairyNetworkConstant.PAYMENT_MODE_INSTAMOJO)) {
                        try {
                            webLoading.setVisibility(View.VISIBLE);
                            String refId1 = content.substring(content.indexOf("payment_request"));
                            String refId2 = refId1.substring(refId1.indexOf("payment-requests")+1);
                            String refId3 = refId2.substring(refId2.indexOf("/")+1);
                            String refId = refId3.substring(0,(refId3.indexOf("/") - 1));
                            DairyProductIntentService.startActionAddOrder(DairyOrderSummaryWebViewActivity.this, orderData,
                                    payAmount, DairyNetworkConstant.PAYMENT_MODE_INSTAMOJO, refId,location,latitude,longitude,complete_add);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    showSuccessDialog("Payment Failed");
                }
            } else if (actionTransactionHistory.equals(DairyNetworkConstant.DAIRY_ADD_ORDER_STATUS_SUCCESS)) {
                //cancelProgressDialog();
                //onDestroy();
                showSuccessTransactionDialog();
                deleteCart();
                webLoading.setVisibility(View.GONE);
                //showErrorDialog("Your oder has been placed successfully.");

            }else if(actionTransactionHistory.equals(DairyNetworkConstant.DAIRY_ADD_ORDER_STATUS_FAILURE)){
                //cancelProgressDialog();
                webLoading.setVisibility(View.GONE);
                showErrorDialog("something went wrong on place order.Please contact our customer service representative");
                finish();
            }
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
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

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void deleteCart(){
        SQLController controller=new SQLController(DairyOrderSummaryWebViewActivity.this);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "DELETE from cart";
        String result = controller.fireQuery(query);
        if(result.equals("Done")){
            Log.w("delete","Delete all successfully");
        }else {
            Log.w("result",result);
        }
        controller.close();

    }
}
