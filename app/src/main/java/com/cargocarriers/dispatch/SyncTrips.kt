package com.cargocarriers.dispatch

import Network.APIClient
import android.util.Log
import com.cargocarriers.objectbox.entities.trip.TripData
import com.cargocarriers.objectbox.enums.SyncStatus
import model.TripJSON
import model.TripsApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SyncTrips {
    companion object {
        private val TAG: String = SyncTrips::class.java.simpleName
        var tripsApi: TripsApi = APIClient.getRetrofit().create(TripsApi::class.java)
        fun uploadUnSyncedTrips() {
            val trips = TripData.getAllUnSyncedTrips()
            for (trip in trips) {
                val tripJSON = TripJSON.converterTripJSON(trip)
                val call: Call<TripJSON> = tripsApi.postTrip(tripJSON)

                call.enqueue(object : Callback<TripJSON> {
                    override fun onResponse(call: Call<TripJSON>, response: Response<TripJSON>) {
                        if (response.isSuccessful) {
                            trip.syncStatus = SyncStatus.SYNCED.id
                            TripData.insertOrUpdate(trip)
                            return
                        }
                    }

                    override fun onFailure(call: Call<TripJSON?>, t: Throwable) {
                        Log.e(TAG, t.message)
                    }
                })
            }
        }

    }
}