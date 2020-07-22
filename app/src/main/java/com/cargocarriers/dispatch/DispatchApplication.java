package com.cargocarriers.dispatch;

import androidx.multidex.MultiDexApplication;

import com.cargocarriers.objectbox.base.ObjectBox;

import Network.APIClient;

public class DispatchApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.INSTANCE.init(this);
        APIClient.initializeRetrofit();
    }
}
