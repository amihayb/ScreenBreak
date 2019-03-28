package com.screenbreak;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import timber.log.Timber;

public class ScreenBreakApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
