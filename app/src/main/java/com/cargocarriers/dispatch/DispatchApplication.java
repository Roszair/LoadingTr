package com.cargocarriers.dispatch;

import android.app.Application;

import com.cargocarriers.objectbox.base.ObjectBox;

import Network.APIClient;

public class DispatchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.INSTANCE.init(this);
        APIClient.initializeRetrofit();
    }
}
