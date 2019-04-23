package com.wifyee.greenfields.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.requests.PayUPaymentGatewayRequest;
import com.wifyee.greenfields.models.response.CityResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static com.wifyee.greenfields.R.id.view;

public class BusBookingList extends BaseActivity
{
   private String strDate="";
    private String strMobile="";
    private String strToCity="";
    private String strFromCity="";
    private WebView webView;
    LinearLayout webLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_booking_list);

        webView = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webLoading = (LinearLayout) findViewById(R.id.refresh_list);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        strDate = getIntent().getStringExtra("date");
        strMobile = getIntent().getStringExtra("mobile");
        strToCity = getIntent().getStringExtra("toCity");
        strFromCity = getIntent().getStringExtra("fromCity");
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
            jsonObject.put("userMobile",strMobile);
            jsonObject.put("userTerminal", "");
            jsonObject.put("firstname",LocalPreferenceUtility.getUserFirstsName(this));
            jsonObject.put("lastname",LocalPreferenceUtility.getUserLastName(this));
            jsonObject.put("email",LocalPreferenceUtility.getUserEmail(this));
            //jsonObject.put("requestType","2");
            jsonObject.put("city_to",strToCity);
            jsonObject.put("city_from",strFromCity);
            jsonObject.put("request_source","app");
            jsonObject.put("doj",strDate);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        // Show the web page
        webView.postUrl(NetworkConstant.BUS_BOOKING_END_POINT,
                (jsonObject.toString()).getBytes());
        cancelProgressDialog();
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
                    }
                });

                webLoadingg.post(new Runnable() {
                    @Override
                    public void run() {
                        webLoadingg.setVisibility(View.GONE);
                    }
                });
                cancelProgressDialog();
               /* Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_SUCCESS);
                broadcastIntent.putExtra(ACTION_STATUS_VALUE, content);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);*/
            }
        }
    }

}
