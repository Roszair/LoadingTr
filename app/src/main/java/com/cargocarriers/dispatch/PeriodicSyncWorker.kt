package com.cargocarriers.dispatch

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Created by Rosina Mothibi.
 */
class PeriodicSyncWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    companion object {
        val TAG : String = PeriodicSyncWorker::class.java.simpleName
    }

    override fun doWork(): Result {

        // Do the work here--in this case, upload the images.
        SyncTrips.uploadUnSyncedTrips()
        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}