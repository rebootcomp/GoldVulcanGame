package com.app.lionnews;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.app.lionnews.activity.StartUpActivity;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.app.lionnews.activity.MainActivity;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import org.json.JSONObject;

import java.util.Map;

import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;

public class App extends Application {

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    private String push_url = null;

    @Override
    public void onCreate() {
        super.onCreate();
        AppsFlyerConversionListener conversionDataListener = new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> map) {

            }

            @Override
            public void onInstallConversionFailure(String s) {

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {

            }
        };
        AppsFlyerLib.getInstance().init("fyBPJBo8HhxeRrvYWZZ3PX", conversionDataListener, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);
        //AppsFlyerLib.getInstance().sendDeepLinkData(this);
        if (Config.ANALYTICS_ID.length() > 0) {
            analytics = GoogleAnalytics.getInstance(this);
            analytics.setLocalDispatchPeriod(1800);

            tracker = analytics.newTracker(Config.ANALYTICS_ID); // Replace with actual tracker/property Id
            tracker.enableExceptionReporting(true);
            tracker.enableAdvertisingIdCollection(true);
            tracker.enableAutoActivityTracking(true);
        }

        //OneSignal Push
       // if (!TextUtils.isEmpty(getString(R.string.onesignal_app_id)))
            // OneSignal Initialization
           // OneSignal.init(this, getString(R.string.onesignal_google_project_number), getString(R.string.onesignal_app_id), new NotificationHandler());
        // Создание расширенной конфигурации библиотеки.
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("eaebfac8-adbb-4667-8584-61d403a1b30f").build();
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this);
        setAdvertiserIDCollectionEnabled(true);

    }


    public synchronized String getPushUrl() {
        String url = push_url;
        push_url = null;
        return url;
    }

    public synchronized void setPushUrl(String url) {
        this.push_url = url;
    }
} 