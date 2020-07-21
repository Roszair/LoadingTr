package com.cargocarriers.objectbox.entities.trip

import com.cargocarriers.objectbox.base.ObjectBox
import com.cargocarriers.objectbox.enums.SyncStatus
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.equal

class TripData {
    companion object {
        private val TAG: String = TripData::class.java.simpleName
        private val tripBox: Box<Trip> = ObjectBox.boxStore.boxFor()

        fun findByInvoiceNo(invoiceNo: String): Trip? {
            return tripBox.query().equal(Trip_.invoiceNo, invoiceNo).build().findFirst()
        }
        fun getAllTrips(): List<Trip> {
            return tripBox.query().build().find()
        }
        fun getAllUnSyncedTrips(): MutableList<Trip> {
            return try {
                tripBox.query().equal(Trip_.syncStatus, SyncStatus.UNSYNCED.id).build().find()
            }catch (e: Exception){
                mutableListOf()
            }
        }

        fun insertOrUpdate(trip: Trip) {
            try {
                val previous =
                        tripBox.query().equal(Trip_.invoiceNo, trip.invoiceNo).build().findFirst()
                if (previous != null) {
                    trip.id = previous.id
                }
                tripBox.put(trip)
            } catch (e: Exception) {
                /*ExceptionLogging.getInstance()
                        .logException(TAG, Throwable(e))*/
            }
        }

        fun bulkInsertOrUpdate(trips: List<Trip>) {
            ObjectBox.boxStore.runInTx {
                for (trip in trips) {
                    insertOrUpdate(trip)
                }
            }
        }

        fun clear() {
            try {
                tripBox.removeAll()
            } catch (e: Exception) {
              /*  ExceptionLogging.getInstance()
                        .logException(TAG, Throwable(e))*/
            }
        }

    }
}