package model;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TripsApi {
    @GET("api/Trips?")
   // @GET("api/trips?InvoiceNo={InvoiceNo}") //15 Apr
    Call<TripJSON> fetchTrips(@Query("InvoiceNo") String invoiceNo);

    //@GET("api")
    //Call<TripJSON> getTrip(@Query("id") String invoiceNo);

    //@POST("api/Trips")
    @POST("api/trips?InvoiceNo=InvoiceNo") //15Apr
    Call<TripJSON> postTrip(@Body TripJSON tripJSON);

    //@POST("api/{id}")
    //Call<TripJSON> postTrips(@Path("id") int id, @Body TripJSON tripJSON);

    @PUT("api/{id}")
    Call<TripJSON> putTrips(@Path("id") int id, @Body TripJSON tripJSON);

    @PUT("trips/{id}")
    Call<TripJSON> putTrips(
            @Field("tripId") int tripId,
            @Field("gateId") int gateId,
            @Field("transporterId") int transporterId,
            @Field("invoiceNo") String invoiceNo,
            @Field("transporterNo") long transporterNo,
            //@Field("gateNo") long gateNo,
           // @Field("receivedParcels") int issuedParcels,//check this 2line and run 23Mar2021 @11:26
            //@Field("receivedQuantity") int issuedQuantity,

            @Field("receivedQuantity") int receivedQuantity,
            @Field("receivedParcels") int receivedParcels,

            //@Field("driverName") String driverName,
           // @Field("sealNo") String sealNo,
            @Field("transporterName") String transporterName
           // @Field("gateName") String gateName,
           // @Field("truckRegNo") String truckRegNo
            //@Field("dispatchDate")Date dispatchDate
            );

    @PATCH("api/{id}")
    Call<TripJSON> patchTrips(@Path("id") int id, @Body TripJSON trips);

    Call<TripJSON> patchTrips(TripJSON tripJSON);

    Call<List<TripJSON>> putTrips(TripJSON tripJSON);
}
