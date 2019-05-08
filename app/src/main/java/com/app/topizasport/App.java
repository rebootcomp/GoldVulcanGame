package com.app.topizasport;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("68d3a92a-5fa3-40fd-b8bb-052986a898d6").build();
        YandexMetrica.activate(getApplicationContext(), config);
        YandexMetrica.enableActivityAutoTracking(this);
    }
}