package com.wifyee.greenfields.foodorder;

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
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.activity.MedicineWebViewActivity;
import com.wifyee.greenfields.activity.MobicashDashBoardActivity;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.dairyorder.DairyNetworkConstant;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONException;
import org.json.JSONObject;

public class FoodWebViewActivity extends AppCompatActivity {

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
            ACTION_STATUS_FAIL,
            NetworkConstant.STATUS_FOODODER_BYMERCHANT_LIST_SUCCESS,
            NetworkConstant.STATUS_FOODORDER_BYMERCHANT_LIST_FAIL,
    };

    String amount,voucherDiscAmt,claimType,voucherId,voucherNo,orderId,location,latitude,longitude,completeAddress;
    private CartFoodOderRequest cartFoodOderRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_web_view);

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
        orderId = intent.getStringExtra("order_id");
        voucherDiscAmt = intent.getStringExtra("voucherDiscAmt");
        claimType = intent.getStringExtra("claimType");
        voucherId = intent.getStringExtra("voucherId");
        voucherNo = intent.getStringExtra("voucherNo");
        location = intent.getStringExtra("location");
        completeAddress = intent.getStringExtra("completeAddress");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        cartFoodOderRequest = (CartFoodOderRequest) getIntent().getSerializableExtra(PaymentConstants.FOODORDER_EXTRA);

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
            data.put("amount",amount);
            data.put("loginType", "Client");
            data.put("purpose", "Food Order Payment");
            data.put("ref_no", orderId);
            data.put("clientId", LocalPreferenceUtility.getUserCode(mcontext));
            data.put("phone",LocalPreferenceUtility.getUserMobileNumber(mcontext));
            data.put("email","");
            data.put("responseUrl", DairyNetworkConstant.MEDICINE_PAYMENT_RESPONSE_URL);
            data.put("webhook", DairyNetworkConstant.WEBHOOK);
            data.put("buyer_name",LocalPreferenceUtility.getUserLastName(mcontext) + " "
                    +LocalPreferenceUtility.getUserFirstsName(mcontext));
            //Show the web page
            webView.postUrl(DairyNetworkConstant.MEDICINE_PAYMENT_URL, ("" + data.toString()).getBytes());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
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
                    String refId = object.getString(ResponseAttributeConstants.PAYMENT_REQ_ID);
                    webLoading.setVisibility(View.VISIBLE);
                    MobicashIntentService.startActionSendFoodRequest(mcontext,cartFoodOderRequest,location,latitude,longitude,
                            completeAddress,voucherDiscAmt,claimType,voucherId,voucherNo,refId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }if (actionTransactionHistory.equals(NetworkConstant.STATUS_FOODODER_BYMERCHANT_LIST_SUCCESS)) {
                CartFoodOrderResponse gstOnFoodItemResponse = (CartFoodOrderResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (gstOnFoodItemResponse != null && gstOnFoodItemResponse.status != null) {
                    webLoading.setVisibility(View.INVISIBLE);
                    AddToCartActivity.isVoucherClaim = false;
                    AddToCartActivity.voucherDiscAmt="";
                    AddToCartActivity.voucherName="";
                    AddToCartActivity.voucherNo="";
                    AddToCartActivity.voucherId="";
                    showSuccessDialog("Success");
                }
            } else if (actionTransactionHistory.equals(NetworkConstant.STATUS_FOODORDER_BYMERCHANT_LIST_FAIL)) {
                webLoading.setVisibility(View.INVISIBLE);
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    webLoading.setVisibility(View.INVISIBLE);
                    AddToCartActivity.isVoucherClaim = false;
                    AddToCartActivity.voucherDiscAmt="";
                    AddToCartActivity.voucherName="";
                    AddToCartActivity.voucherNo="";
                    AddToCartActivity.voucherId="";
                    showErrorDialog(failureResponse.msg);
                }
                showErrorDialog(String.valueOf(R.string.error_message));
            }
        }
    };

    private void showSuccessDialog(final String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext);
        alertDialogBuilder
                .setTitle("Success")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        deleteCart();
                        startActivity(new Intent(mcontext, MobicashDashBoardActivity.class));
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showErrorDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
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

    private void deleteCart(){
        SQLController controller=new SQLController(mcontext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "DELETE from food_cart";
        String result = controller.fireQuery(query);
        if(result.equals("Done")){
            Log.w("delete","Delete all successfully");
        }else {
            Log.w("result",result);
        }
        controller.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AddToCartActivity.isVoucherClaim = false;
    }
}
