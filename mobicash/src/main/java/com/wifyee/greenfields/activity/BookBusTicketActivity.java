package com.wifyee.greenfields.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
//import com.facebook.login.LoginResult;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.adapters.SpinerAdapter;
import com.wifyee.greenfields.adapters.SpinerCityAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.requests.GetClientProfileInfoRequest;
import com.wifyee.greenfields.models.response.BankListResponseModel;
import com.wifyee.greenfields.models.response.CityResponse;
import com.wifyee.greenfields.models.response.GetClientProfileInfoResponse;
import com.wifyee.greenfields.models.response.GetProfileInfo;
import com.wifyee.greenfields.models.response.PlanDataSummary;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.wifyee.greenfields.activity.WebViewActivity.ACTION_STATUS_VALUE;

public class BookBusTicketActivity extends BaseActivity {


     private WebView webView;
     private LinearLayout webLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_bus_ticket);

        webView = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webLoading = (LinearLayout) findViewById(R.id.refresh_list);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        showProgressDialog();
        //callApiBusBooking(BusBookingList.this, strMobile, strDate, strToCity, strFromCity);

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
            public void onReceivedHttpError(WebView view, WebResourceRequest
                    request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.d("status", "" + errorResponse);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError
                    error) {
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
        webView.getSettings().setJavaScriptEnabled(true);
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("userType","client");
            jsonObject.put("userId", LocalPreferenceUtility.getUserCode(this));
            jsonObject.put("userMobile", LocalPreferenceUtility.getUserMobileNumber(this));
            jsonObject.put("userTerminal", "abcd");

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        // Show the web page
        webView.postUrl(NetworkConstant.BUS_BOOKING_END_POINT,
                (jsonObject.toString()).getBytes());
        cancelProgressDialog();

        // webView.loadUrl("http://google.com");
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
            if (content.toLowerCase().contains("status")) {
                webVieww.post(new Runnable() {
                    @Override
                    public void run() {
                        webVieww.setVisibility(View.GONE);
                        finish();
                    }
                });

                webLoadingg.post(new Runnable() {
                    @Override
                    public void run() {
                        webLoadingg.setVisibility(View.GONE);
                        finish();
                    }
                });
                cancelProgressDialog();
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_SUCCESS);
                broadcastIntent.putExtra(ACTION_STATUS_VALUE, content);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

            }
        }
    }


}
