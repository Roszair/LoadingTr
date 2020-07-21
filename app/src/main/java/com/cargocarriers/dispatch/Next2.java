package com.cargocarriers.dispatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cargocarriers.objectbox.entities.trip.Trip;
import com.cargocarriers.objectbox.entities.trip.TripData;

import com.google.zxing.Result;

import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Network.APIClient;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import model.TripJSON;
import model.TripsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cargocarriers.dispatch.MainActivity.DATETIME_KEY;
import static com.cargocarriers.dispatch.MainActivity.GATE_NO_KEY;
import static com.cargocarriers.dispatch.MainActivity.TRANSPORTER_NO_KEY;
import static com.cargocarriers.dispatch.MainActivity.TRUCK_REG_NO_KEY;

public class Next2 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Next2.class.getSimpleName();
    private TextView txtInvoiceBarcodeTextView, invoiceBarcodeTextView;
    private ZXingScannerView zXingScannerView;
    private Button nxtInvoice;
    public TripsApi tripsApi;
    private ProgressBar spinner;
    private EditText edtReceivedQuantity, edtReceivedParcels, edtSealNo, edtDriver;

    Button proceed;
    TextView transporterBarcodeView, gateBarcodeTextView;
    int receivedParcels, receivedQuantity;
    String driverName, invoiceNo, sealNo;
    HashMap<String, Trip> tripHashMap = new HashMap<>();

    Button btnReset, btnScanInvoice, btnNextInvoice, btnSubmit;
    SharedPreferences sPref;
    final String SAVED_TEXT = "saved_text";
    private String truckRegNo;
    private long gateNo;
    private long transporterNo;
    private long dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next2);

        dateTime = getIntent().getIntExtra(DATETIME_KEY, -1);
        truckRegNo = getIntent().getStringExtra(TRUCK_REG_NO_KEY);
        gateNo = getIntent().getLongExtra(GATE_NO_KEY, -1);
        transporterNo = getIntent().getLongExtra(TRANSPORTER_NO_KEY, -1);

        tripsApi = APIClient.getRetrofit().create(TripsApi.class);

        //txtInvoiceBarcodeTextView = findViewById(R.id.invoiceBarcodeView);
        invoiceBarcodeTextView = findViewById(com.cargocarriers.dispatch.R.id.invoiceBarcodeView);
        btnReset = findViewById(R.id.btnClear);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnScanInvoice = findViewById(R.id.btnScanInvoice);
        btnNextInvoice = findViewById(R.id.btnNextInvoice);
        edtReceivedParcels = findViewById(R.id.edtReceivedParcels);
        edtReceivedQuantity = findViewById(R.id.edtReceivedQuantity);
        spinner = findViewById(R.id.progressBar1);
        edtDriver = findViewById(R.id.driverName);
        edtSealNo = findViewById(R.id.sealNo);

        TextView datetime = findViewById(R.id.datetime);

        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnScanInvoice.setOnClickListener(this);
        btnNextInvoice.setOnClickListener(this);

        Date timenow = Calendar.getInstance().getTime();
        datetime.setText(timenow.toString());

       /* btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                TripJSON trip = new TripJSON(invoiceNo, gateId, transporterId, Driver, SealNumber, truckRegNo, Integer.parseInt(receivedParcels.getText().toString()), Integer.parseInt(receivedQuantity.getText().toString()));
                putTrips(trip);
                updateTrips();


            }
        });

       /* Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cbisrva.cargojhb:9093/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tripsApi = retrofit.create(TripsApi.class);

        //postTrip();25 june
        //updateTrips();
        //tripsApi.postTrips response and failure*/
    }

    private void uploadTrip(Trip trip) {
        TripJSON tripJSON = TripJSON.converterTripJSON (trip);
        Call<List<TripJSON>> call = tripsApi.putTrips(tripJSON);
        call.enqueue(new Callback<List<TripJSON>>() {
            @Override
            public void onResponse(Call<List<TripJSON>> call, Response<List<TripJSON>> response) {
                if (response.isSuccessful()) {

                    spinner.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(), "TripJSON submitted successfully", Toast.LENGTH_SHORT);
                    toast.show();
                    clearData();
                    return;
                }
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<TripJSON>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                spinner.setVisibility(View.GONE);
                Toast toast = Toast.makeText(getApplicationContext(), "Failed to submit trips", Toast.LENGTH_SHORT);
                toast.show();
                clearData();
            }
        });
    }

    /*private void updateTrips() {
        TripJSON trip = new TripJSON(1, 5, 2, "driver", "sealNumber", "truckRegNo", 55, 67);

        Call<TripJSON> call = tripsApi.patchTrips(1, trip);
        call.enqueue(new Callback<TripJSON>() {
            @Override
            public void onResponse(Call<TripJSON> call, Response<TripJSON> response) {
                if (!response.isSuccessful()) {
                    btnSubmit.setText("Code: " + response.code());
                    return;

                }
                TripJSON postResponse = response.body();
                String content = "";
                content += "TripJSON ID: " + postResponse.getTripId() + "\n";
                content += "Invoice Number: " + postResponse.getInvoiceNo() + "\n";
                content += "Gate ID: " + postResponse.getGateId() + "\n";
                content += "Transporter ID: " + postResponse.getTransporterId() + "\n";
                content += "Truck Registration Number: " + postResponse.getRegistrationNo() + "\n";
                content += "Received Parcels: " + postResponse.getReceivedParcels() + "\n";
                content += "Received Quantity: " + postResponse.getReceivedQuantity() + "\n";
                content += "Driver: " + postResponse.getDriver() + "\n";
                content += "Seal Number: " + postResponse.getSealNumber() + "\n\n";

                btnSubmit.append(content);
            }


            @Override
            public void onFailure(Call<TripJSON> call, Throwable t) {

            }
        });

    }*/

    private void clearData() {
        edtReceivedQuantity.setText("");
        edtReceivedParcels.setText("");
        //txtInvoiceBarcodeTextView.setText("");
        invoiceBarcodeTextView.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearData();
    }

    //scANNING Invoice
    public void scanInvoiceBarcode(View view) {
        zXingScannerView = findViewById(R.id.zXingScannerView);
        zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            public void handleResult(Result result) {
                invoiceNo = result.getText();
                //txtInvoiceBarcodeTextView.setText(invoiceNo);
                //invoiceBarcodeTextView.setText(result.getText());
                invoiceBarcodeTextView.setText(invoiceNo);
                zXingScannerView.resumeCameraPreview(this);
            }
        });
        zXingScannerView.startCamera();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSubmit:
                validateTrips();
                break;
            case R.id.btnClear:
                clearData();
                break;
            case R.id.btnNextInvoice:
                nextInvoice();
                break;
            case R.id.btnScanInvoice:
                scanInvoiceBarcode();
                break;
            default:
                break;
        }
    }

    private void scanInvoiceBarcode() {

    }

    private void nextInvoice() {
        invoiceNo = btnScanInvoice.getText().toString();
        sealNo = edtSealNo.getText().toString();
        driverName = edtDriver.getText().toString();
        receivedQuantity = Integer.parseInt(edtReceivedQuantity.getText().toString());
        receivedParcels = Integer.parseInt(edtReceivedParcels.getText().toString());

        if (sealNo.isEmpty() || driverName.isEmpty() || receivedQuantity <= 0 || receivedParcels <= 0 || invoiceNo.isEmpty()) {
            invalidInput();
        } else {
            Trip trip = new Trip();
            trip.setInvoiceNo(invoiceNo);
            trip.setGateId(gateNo);
            trip.setReceivedParcels(receivedParcels);
            trip.setReceivedQuantity(receivedQuantity);
            trip.setTransporterId(transporterNo);
            trip.setTruckRegNo(truckRegNo);

            tripHashMap.put(invoiceNo, trip);
            clearData();
        }
    }

    private void validateTrips() {
        if (!driverName.isEmpty() && !sealNo.isEmpty()) {
            for (Trip trip : tripHashMap.values()) {
                trip.setSealNumber(sealNo);
                trip.setDriverName(driverName);
                TripData.Companion.insertOrUpdate(trip);
            }
            finalizeTripsSubmission();
        } else
            invalidInput();
    }

    private void finalizeTripsSubmission() {
        SyncTrips.Companion.uploadUnSyncedTrips();
    }

    private void invalidInput() {
        new AlertDialog.Builder(Next2.this)
                .setTitle("Info Alert")
                .setMessage("Please enter all required fields.")
                .setCancelable(false)
                .setPositiveButton("ok", (dialog, which) -> dialog.dismiss()).show();
    }

    /*void saveText(){
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, regTruck.getText().toString());
        ed.putString(SAVED_TEXT, gateBarcodeTextView.getText().toString());
        ed.putString(SAVED_TEXT, transporterBarcodeView.getText().toString());
        ed.putString(SAVED_TEXT, invoiceBarcodeTextView.getText().toString());
        ed.putString(SAVED_TEXT, receivedQuantity.getText().toString());
        ed.putString(SAVED_TEXT, receivedParcels.getText().toString());
        ed.putString(SAVED_TEXT, Driver.getText().toString());
        ed.putString(SAVED_TEXT, SealNumber.getText().toString());
        ed.commit();
        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }*/
   /* void loadText(){
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_TEXT,"");
        regTruck.setText(savedText);
        gateBarcodeTextView.setText(savedText);
        transporterBarcodeView.setText(savedText);
        invoiceBarcodeTextView.setText(savedText);
        receivedQuantity.setText(savedText);
        receivedParcels.setText(savedText);
        Driver.setText(savedText);
        SealNumber.setText(savedText);
        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }*/
    /*@Override
    protected void onDestroy(){
        super.onDestroy();
        saveText();
    }*/

}