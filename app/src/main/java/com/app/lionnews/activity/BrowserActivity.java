package com.app.lionnews.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.lionnews.R;

public class BrowserActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CookieManager.getInstance().setAcceptCookie(true);
        webView = new WebView(this);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            }
        });
        setContentView(webView);

        //progressDialog = ProgressDialog.show(getApplicationContext(), "", "Загрузка. Пожалуйста подождите...", true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
//                CookieManager.getInstance().flush();
                //progressDialog.dismiss();
                if (url.equals("http://noaccept.termof/"))
                    System.exit(0);
                if (url.equals("http://agree.termof/")) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
//                    webView.loadUrl("http://puprt.com/wq8b27ds");
                super.onPageFinished(view, url);
            }
        });
        webView.loadUrl("http://sportsnewsapp.ru/term");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }
}