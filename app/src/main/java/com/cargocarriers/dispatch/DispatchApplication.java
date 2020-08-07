package com.cargocarriers.dispatch;

import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDexApplication;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.cargocarriers.objectbox.base.ObjectBox;

import java.util.concurrent.TimeUnit;

import Network.APIClient;

import static androidx.work.ExistingPeriodicWorkPolicy.REPLACE;

public class DispatchApplication extends MultiDexApplication {
private static final String TAG = DispatchApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.INSTANCE.init(this);
        APIClient.initializeRetrofit();
        initTimeIntervalWorker(this);
    }
    private void initTimeIntervalWorker(Context context) {
        Log.d(TAG, "Setup periodice workwers");

        //Cancel all previous tasks
         WorkManager wm = WorkManager.getInstance(context.getApplicationContext());
        wm.cancelAllWork();
        Constraints constraints = new Constraints.Builder().build();
        PeriodicWorkRequest timedIntervalRequest = new PeriodicWorkRequest.
                Builder(PeriodicSyncWorker.class, 15, TimeUnit.MINUTES)
                .addTag(PeriodicSyncWorker.Companion.getTAG())
                .setConstraints(constraints)
                .build();
        wm.enqueueUniquePeriodicWork(PeriodicSyncWorker.Companion.getTAG(), REPLACE, timedIntervalRequest);
    }
}
