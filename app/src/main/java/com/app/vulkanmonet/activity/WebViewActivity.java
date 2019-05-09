package com.app.vulkanmonet.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.vulkanmonet.R;

public class WebViewActivity extends AppCompatActivity {
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
        SharedPreferences.Editor ed = mSP.edit();
        ed.putString("save", "web");
        ed.commit();
        CookieManager.getInstance().setAcceptCookie(true);
        setContentView(R.layout.activity_browser);
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.pb);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!isOnline(getApplicationContext())) {
                    webView.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(getApplicationContext(), NoConnectionActivity.class));
                    finish();
                } else {
                    CookieManager.getInstance().flush();
                    if (url.equals("http://not.term/")) {
                        webView.setVisibility(View.GONE);
                        mSP = getSharedPreferences("settings", Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = mSP.edit();
                        ed.putString("save", "");
                        ed.commit();
                        startActivity(new Intent(getApplicationContext(), LaunchActivity.class));
                        finish();
                        //System.exit(0);
                    }
                    if (url.equals("http://agree.term/")) {
                        webView.setVisibility(View.GONE);
                        //mSP = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor ed = mSP.edit();
                        ed.putString("save", "main");
                        ed.commit();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                    if (!url.contains("term") && progressBar.getVisibility() == ProgressBar.VISIBLE)
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    super.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressBar.getVisibility() == ProgressBar.VISIBLE)
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                CookieManager.getInstance().flush();
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

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && progressBar.getVisibility() == ProgressBar.VISIBLE)
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                if (newProgress < 100 && !view.getUrl().contains("pin") && progressBar.getVisibility() == ProgressBar.INVISIBLE)
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                super.onProgressChanged(view, newProgress);
            }
        });
        Intent intent = getIntent();
        progressBar.setVisibility(ProgressBar.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.loadUrl(intent.getStringExtra("url"));


    }

    public static boolean isOnline(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;
        }

        return false;
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
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }
}