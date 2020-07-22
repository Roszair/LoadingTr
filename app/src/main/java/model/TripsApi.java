package model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TripsApi {
    @GET
    Call<List<TripJSON>> fetchTrips();

    @POST("api/trips")
    Call<TripJSON> postTrip(@Body TripJSON tripJSON);

    @PUT("api/{id}")
    Call<TripJSON> putTrips(@Path("id") int id, @Body TripJSON tripJSON);

    @PUT("trips/{id}")
    Call<TripJSON> putTrips(
            @Field("tripId") int tripId,
            @Field("gateId") int gateId,
            @Field("transporterId") int transporterId,
            @Field("invoiceNo") String invoiceNo,
            @Field("transporterNo") String transporterNo,
            @Field("gateNo") String gateNo,
            @Field("receivedParcels") int issuedParcels,
            @Field("receivedQuantity") int issuedQuantity
            //@Field("driverName") String driverName
            //@Field("sealNumber") String sealNumber

    );

    @PATCH("api/{id}")
    Call<TripJSON> patchTrips(@Path("id") int id, @Body TripJSON trips);

    Call<TripJSON> patchTrips(TripJSON tripJSON);

    Call<List<TripJSON>> putTrips(TripJSON tripJSON);
}
