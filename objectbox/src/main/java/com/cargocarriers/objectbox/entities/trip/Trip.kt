package com.cargocarriers.objectbox.entities.trip

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.Unique

@Entity
data class Trip(
    @Id var id: Long = 0,
    @Index @Unique
    var invoiceNo: String? = null,
    var transporterId: Long = 0,
    var gateId: Long = 0,
    var truckRegNo: String? = null,
    var receivedParcels: Int = 0,
    var receivedQuantity: Int = 0,
    var sealNumber: String? = null,
    var driverName: String? = null,
    var syncStatus: Int = -1
)