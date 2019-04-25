package com.app.lionnews.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.lionnews.R;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.facebook.appevents.AppEventsLogger;
import com.yandex.metrica.YandexMetrica;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class BrowserActivity extends AppCompatActivity {
    private WebView webView;

    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    SharedPreferences mSP;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSP = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=mSP.edit();
        ed.putString("save","browser");
        ed.commit();
        CookieManager.getInstance().setAcceptCookie(true);
        setContentView(R.layout.activity_browser);
        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {


                if (url.contains("success")) {
                    //yandex
                    Map<String, Object> eventAttributes = new HashMap<String, Object>();
                    eventAttributes.put("Test", "b&k leon");
                    eventAttributes.put("Payment", 1);
                    Date date = new Date();
                    eventAttributes.put("Date", date.toString());
                    YandexMetrica.reportEvent("Payment confirmed", eventAttributes);
                    //facebook
                    AppEventsLogger logger = AppEventsLogger.newLogger(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("Application", "b&k leon");
                    params.putInt("Payment", 1);
                    params.putString("Date", date.toString());
                    logger.logEvent("Payment confirmed", params);
                    //appsflyer
                    AppsFlyerLib.getInstance().trackEvent(getApplicationContext(), AFInAppEventType.LEVEL_ACHIEVED, eventAttributes);
                }




                if (url.equals("http://noaccept.termof/")) {
                    webView.setVisibility(View.GONE);
                    System.exit(0);
                }
                if (url.equals("http://agree.termof/")) {
                    webView.setVisibility(View.GONE);
                   //mSP = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor ed=mSP.edit();
                    ed.putString("save","main");
                    ed.commit();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressBar.getVisibility() == ProgressBar.VISIBLE)
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                super.onPageFinished(view, url);
            }



            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(WebView WebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
        Intent intent = getIntent();
        progressBar.setVisibility(ProgressBar.VISIBLE);
//        webView.loadUrl("http://vk.com");
        webView.loadUrl(intent.getStringExtra("url"));

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }
}