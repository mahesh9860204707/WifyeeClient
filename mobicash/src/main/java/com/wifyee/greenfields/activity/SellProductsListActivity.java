package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wifyee.greenfields.R;

/**
 * Created by amit on 7/24/2018.
 */

public class SellProductsListActivity extends  BaseActivity
{
    private Toolbar mToolbar;
    private ImageButton back;
    private String url="https://www.rmcluniverse.com/";
    private WebView webView;
    LinearLayout webLoading = null;
    LinearLayout layout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        back= (ImageButton) findViewById(R.id.toolbar_back);
        layout= (LinearLayout) findViewById(R.id.linear_layout);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            back.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });

            webView = (WebView) findViewById(R.id.webView1);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webLoading = (LinearLayout) findViewById(R.id.refresh_list);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
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
        webView.getSettings().setJavaScriptEnabled(true);
        // Show the web page
        webView.loadUrl(url);
    }

    protected void showAppExitDialog(Activity mActivity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Please confirm");
        builder.setMessage("No back history found, want to exit the app?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Let allow the system to handle the event, such as exit the app
                SellProductsListActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when want to stay in the app
                Toast.makeText(SellProductsListActivity.this,"thank you",Toast.LENGTH_LONG).show();
            }
        });
        // Create the alert dialog using alert dialog builder
        AlertDialog dialog = builder.create();
        // Finally, display the dialog when user press back button
        dialog.show();
    }


    @Override
    public void onBackPressed(){
        if(webView.canGoBack()){
            // If web view have back history, then go to the web view back history
            webView.goBack();
            Snackbar.make(layout,"Go to back history",Snackbar.LENGTH_LONG).show();
        }else {
            // Ask the user to exit the app or stay in here
            showAppExitDialog(this);
        }
    }
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

        }
    }
}


