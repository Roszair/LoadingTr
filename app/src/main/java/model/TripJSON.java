package model;

import android.widget.EditText;

import com.cargocarriers.objectbox.entities.trip.Trip;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripJSON {

        @SerializedName("TripId")
        @Expose
        private int tripId;
        @SerializedName("InvoiceNo")
        @Expose
        private String invoiceNo;
        @SerializedName("TransporterId")
        @Expose
        private long transporterId;
        @SerializedName("GateId")
        @Expose
        private long gateId;
        @SerializedName("truckRegNo")
        @Expose
        private String truckRegNo;
        @SerializedName("ReceivedParcels")
        @Expose
        private int receivedParcels;
        @SerializedName("ReceivedQuantity")
        @Expose
        private int receivedQuantity;
        @SerializedName("SealNumber")
        @Expose
        private String sealNumber;
        @SerializedName("DriverName")
        @Expose
        private String driverName;

    public TripJSON() {
    }

    public TripJSON( String invoiceNo, long transporterId, long gateId, String truckRegNo, int receivedParcels, int receivedQuantity, String sealNumber, String driverName) {

            this.transporterId = transporterId;
            this.gateId = gateId;
            this.invoiceNo = invoiceNo;
            this.truckRegNo = truckRegNo;
            this.receivedParcels = receivedParcels;
            this.receivedQuantity = receivedQuantity;
            this.sealNumber = sealNumber;
            this.driverName = driverName;
        }
    
    public static TripJSON converterTripJSON(Trip trip) {
        return new TripJSON(trip.getInvoiceNo(),trip.getTransporterId(), trip.getGateId(), trip.getTruckRegNo(), trip.getReceivedParcels(), trip.getReceivedQuantity(), trip.getSealNumber(), trip.getDriverName());

    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public long getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(long transporterId) {
        this.transporterId = transporterId;
    }

    public long getGateId() {
        return gateId;
    }

    public void setGateId(long gateId) {
        this.gateId = gateId;
    }

    public String getTruckRegNo() {
        return truckRegNo;
    }

    public void setTruckRegNo(String truckRegNo) {
        this.truckRegNo = truckRegNo;
    }

    public int getReceivedParcels() {
        return receivedParcels;
    }

    public void setReceivedParcels(int receivedParcels) {
        this.receivedParcels = receivedParcels;
    }

    public int getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(int receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public String getSealNumber() {
        return sealNumber;
    }

    public void setSealNumber(String sealNumber) {
        this.sealNumber = sealNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
