package com.cargocarriers.objectbox.entities.trip

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.Unique
import java.util.*

@Entity
data class Trip(
    @Id var id: Long = 0,
    @Index @Unique
    var invoiceNo: String? = null,
    var transporterNo: String? = null,
    var gateNo: String? = null,
    var truckRegNo: String? = null,
    var receivedParcels: Int = 0,
    var receivedQuantity: Int = 0,
    var sealNo: String? = null,
    var driverName: String? = null,
    var transporterName: String? = null,
    var gateName: String? = null,
    var issuedQuantity: Int = 0,
    var issuedParcels: Int = 0,
    var dispatchDate: Long = -1,
    var syncStatus: Int = -1

)