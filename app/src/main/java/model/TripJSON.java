package model;

import com.cargocarriers.objectbox.entities.trip.Trip;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;


public class TripJSON {

    @SerializedName("TripId")
    @Expose
    private Integer tripId;

    @SerializedName("InvoiceNo")
    @Expose
    private String invoiceNo;


    //@SerializedName("GateName") 23 Mrach2021@17:20pm
    //@Expose
    //private String gateName;

    //@SerializedName("GateNo")
    //@Expose
    //private String gateNo;


    @SerializedName("TransporterName")
    @Expose
    private String transporterName;

    @SerializedName("TransporterNo")
    @Expose
    private String transporterNo;

    @SerializedName("ReceivedParcels")
    @Expose
    private Integer receivedParcels;

    @SerializedName("ReceivedQuantity")
    @Expose
    private Integer receivedQuantity;

    @SerializedName("IssuedParcels")
    @Expose
    private Integer issuedParcels;

    @SerializedName("IssuedQuantity")
    @Expose
    private Integer issuedQuantity;

   // @SerializedName("truckRegNo")
    //@Expose
    //private String truckRegNo;

    //@SerializedName("DriverName")
    //@Expose
    //private String driverName;

    //@SerializedName("SealNumber")
    //@Expose
    //private String sealNumber;

    //@SerializedName("DispatchDate")
    //@Expose
    //private Long dispatchDate;

    public TripJSON() {
    }

    public TripJSON(String invoiceNo, String transporterNo, String gateNo, String truckRegNo, int receivedParcels,
                    int receivedQuantity, String sealNumber, String driverName, /*long dispatchDate,*/ String transporterName, String gateName, int issuedParcels, int  issuedQuantity) {
            //this.gateNo = gateNo;
            this.transporterNo = transporterNo;
            this.invoiceNo = invoiceNo;
           // this.truckRegNo = truckRegNo;
            this.receivedParcels = receivedParcels;
            this.receivedQuantity = receivedQuantity;
           // this.sealNumber = sealNumber;
            //this.driverName = driverName;
            //this.dispatchDate = dispatchDate;
            this.transporterName = transporterName;
            //this.gateName = gateName;
            this.issuedParcels = issuedParcels;
            this.issuedQuantity = issuedQuantity;
        }
    
    public static TripJSON converterTripJSON(Trip trip) {
        return new TripJSON(trip.getInvoiceNo(),trip.getTransporterNo(), trip.getGateNo(), trip.getTruckRegNo(),
                trip.getReceivedParcels(), trip.getReceivedQuantity(), trip.getSealNo(), trip.getDriverName(),
                /*trip.getDispatchDate(),*/ trip.getTransporterName(), trip.getGateName(), trip.getIssuedParcels(), trip.getIssuedQuantity());

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

    /*public String getTruckRegNo() {
        return truckRegNo;
    }

    public void setTruckRegNo(String truckRegNo) {
        this.truckRegNo = truckRegNo;
    }*/

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

    /*public String getSealNumber() {
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
    }*/

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    /*public String getGateName() {
        return gateName;
    }

    public void setGateName(String gateName) {
        this.gateName = gateName;
    }

    public String getGateNo() {
        return gateNo;
    }

    public void setGateNo(String gateNo) {
        this.gateNo = gateNo;
    }*/

    public String getTransporterNo() {
        return transporterNo;
    }

    public void setTransporterNo(String transporterNo) {
        this.transporterNo = transporterNo;
    }

    public String getTransporterName() {
        return transporterName;
    }

    public void setTransporterName(String transporterName) {
        this.transporterName = transporterName;
    }

    public void setReceivedParcels(Integer receivedParcels) {
        this.receivedParcels = receivedParcels;
    }

    public void setReceivedQuantity(Integer receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

   // public Long getDispatchDate() {
     //   return dispatchDate;
   // }

    /*public void setDispatchDate(Long dispatchDate) {
        this.dispatchDate = dispatchDate;
    }*/

    public Integer getIssuedParcels() {
        return issuedParcels;
    }

    public void setIssuedParcels(Integer issuedParcels) {
        this.issuedParcels = issuedParcels;
    }

    public Integer getIssuedQuantity() {
        return issuedQuantity;
    }

    public void setIssuedQuantity(Integer issuedQuantity) {
        this.issuedQuantity = issuedQuantity;
    }
}
