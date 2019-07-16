package com.wifyee.greenfields.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
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

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.dairyorder.DairyNetworkConstant;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PayCreditWebViewActivity extends AppCompatActivity {
    private WebView webView;
    LinearLayout webLoading = null;
    private static String TAG = MedicineWebViewActivity.class.getSimpleName();
    private Toolbar toolbar;
    public static final String ACTION_STATUS = "action_success";
    public static final String ACTION_STATUS_FAIL = "action_fail";
    public static final String ACTION_STATUS_VALUE = "action_value";
    private Context mcontext;

    private String[] broadCastReceiverActionList = {
            ACTION_STATUS,
            ACTION_STATUS_FAIL
    };
    String mcId,merId,amount;
    private SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_credit_web_view);
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

        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        mcId = intent.getStringExtra("mc_id");
        merId = intent.getStringExtra("mer_id");

        mcontext = this;

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

        JSONObject data = new JSONObject();
        try {
            data.put("mcdId",mcId);
            data.put("merchantId",merId);
            data.put("clientId",LocalPreferenceUtility.getUserCode(mcontext));
            data.put("clientPhone",LocalPreferenceUtility.getUserMobileNumber(mcontext));
            data.put("paymentAmt",amount);
            data.put("paymentMode","online");
            //Log.e("data",data.toString());
            //Log.e("url",DairyNetworkConstant.MEDICINE_PAYMENT_URL);
            //Show the web page
            webView.postUrl(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.CREDIT_PAY_ONLINE, ("" + data.toString()).getBytes());
        } catch (JSONException e) {
            e.printStackTrace();
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
    private BroadcastReceiver transactionHistoryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionTransactionHistory = intent.getAction();
            if (actionTransactionHistory.equals(ACTION_STATUS)) {
                String content = intent.getStringExtra(ACTION_STATUS_VALUE);
                //Log.e("Intentcontent",content);
                try {
                    JSONObject object = new JSONObject(content);
                    pDialog = new SweetAlertDialog(mcontext, SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Please wait...");
                    pDialog.setCancelable(false);
                    if(object.getInt(ResponseAttributeConstants.STATUS)!=0){
                        pDialog.setTitleText("Success!")
                                .setContentText("Payment credit successfully")
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
                    }else {
                        pDialog.setTitleText("Failed!")
                                .setContentText("Payment failed")
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
                    e.printStackTrace();
                }
            }
        }
    };
}
