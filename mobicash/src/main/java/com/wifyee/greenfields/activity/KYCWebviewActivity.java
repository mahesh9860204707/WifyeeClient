package com.wifyee.greenfields.activity;

import android.graphics.Bitmap;
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

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.requests.KYCBean;
import com.wifyee.greenfields.models.requests.KYCDocumentsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import timber.log.Timber;

public class KYCWebviewActivity extends BaseActivity
{
    private WebView webView;
    private LinearLayout webLoading = null;
    private ArrayList<KYCDocumentsBean> kycDocumentsBeanArrayList;
    private File fileIdProof,fileAddressProof;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kycwebview);

        kycDocumentsBeanArrayList= (ArrayList<KYCDocumentsBean>)getIntent().getSerializableExtra("KycValue");
        fileIdProof= (File) getIntent().getSerializableExtra("fileIdProof");
        fileAddressProof=(File)getIntent().getSerializableExtra("fileAddressProof");

        webView = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webLoading = (LinearLayout) findViewById(R.id.refresh_list);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setAllowFileAccess(true);
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
        JSONObject jsonFinalvalue=new JSONObject();
        JSONArray itemSelectedJson = new JSONArray();
        try {
            for (int i = 0; i < kycDocumentsBeanArrayList.size(); i++) {
                JSONObject jsonObjectKyc = new JSONObject();
                jsonObjectKyc.put("CUSTOMER_MOBILE",kycDocumentsBeanArrayList.get(i).getCUSTOMER_MOBILE());
                jsonObjectKyc.put("CUSTOMER_NAME", kycDocumentsBeanArrayList.get(i).getCUSTOMER_NAME());
                jsonObjectKyc.put("documentFile", kycDocumentsBeanArrayList.get(i).getDocumentFile());
                jsonObjectKyc.put("documentName", kycDocumentsBeanArrayList.get(i).getDocumentName());
                jsonObjectKyc.put("documentType", kycDocumentsBeanArrayList.get(i).getDocumentType());
                jsonObjectKyc.put("proofNumber", kycDocumentsBeanArrayList.get(i).getProofNumber());
                itemSelectedJson.put(jsonObjectKyc);
                jsonFinalvalue.put("documents",itemSelectedJson);
            }
            jsonFinalvalue.put("userType","client");
            jsonFinalvalue.put("userId",LocalPreferenceUtility.getUserCode(this));
            jsonFinalvalue.put("identityImagePath",fileIdProof);
            jsonFinalvalue.put("addressImagePath",fileAddressProof);
            Log.e("Getting Json Value", "Getting Json Object Value" +jsonFinalvalue);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        // Show the web page
        webView.postUrl(NetworkConstant.ADD_KYC_TO_SERVER,
                (jsonFinalvalue.toString()).getBytes());
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
            }
        }
    }

}
