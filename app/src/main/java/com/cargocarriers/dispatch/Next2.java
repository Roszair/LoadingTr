package com.cargocarriers.dispatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Network.APIClient;
import model.TripJSON;
import model.TripsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cargocarriers.dispatch.MainActivity.GATE_NO_KEY;
import static com.cargocarriers.dispatch.MainActivity.SCAN_MODE;
import static com.cargocarriers.dispatch.MainActivity.SCAN_SERIAL_REQUEST;
import static com.cargocarriers.dispatch.MainActivity.TRANSPORTER_NO_KEY;
import static com.cargocarriers.dispatch.MainActivity.TRUCK_REG_NO_KEY;
import static com.cargocarriers.dispatch.ScannerActivity.ScannerConstants.SCAN_RESULT;

public class Next2 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Next2.class.getSimpleName();
    private TextView invoiceBarcodeTextView;
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
    private String gateNo;
    private String transporterNo;
    private Date dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next2);

        truckRegNo = getIntent().getStringExtra(TRUCK_REG_NO_KEY);
        gateNo = getIntent().getStringExtra(GATE_NO_KEY);
        transporterNo = getIntent().getStringExtra(TRANSPORTER_NO_KEY);

        tripsApi = APIClient.getRetrofit().create(TripsApi.class);

        invoiceBarcodeTextView = findViewById(R.id.invoiceBarcodeView);
        btnReset = findViewById(R.id.btnClear);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnScanInvoice = findViewById(R.id.btnScanInvoiceUI);
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
    }

    private void uploadTrip(Trip trip) {
        TripJSON tripJSON = TripJSON.converterTripJSON(trip);
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

    private void clearData() {
        edtReceivedQuantity.setText("");
        edtReceivedParcels.setText("");
        invoiceBarcodeTextView.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_SERIAL_REQUEST) {
            if (data != null) {
                ResultMode resultMode = ResultMode.Companion.getByVal(data.getIntExtra(SCAN_MODE, -1));
                String result = data.getStringExtra(SCAN_RESULT);
                if (resultMode == ResultMode.SCAN_INVOICE) {
                    if (result != null) {
                        invoiceNo = result;
                        invoiceBarcodeTextView.setText(result);
                    }
                }
            }
        }
    }

    public void scanInvoiceBarcode() {
        PermissionRequest.Companion.permissions(this);
        Intent intent = new Intent(this, ScannerActivity.class);
        intent.putExtra(SCAN_MODE, ResultMode.SCAN_INVOICE.getId());
        startActivityForResult(intent, SCAN_SERIAL_REQUEST);
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
            case R.id.btnScanInvoiceUI:
                scanInvoiceBarcode();
                break;
            default:
                break;
        }
    }

    // Pressing this method, data will be stored and sync. It will only sync required data excluding sealNo and driverName
    private void nextInvoice() {
        initInputValues();
        if (sealNo.isEmpty() || driverName.isEmpty() || receivedQuantity <= 0 || receivedParcels <= 0 || invoiceNo.isEmpty()) {
            invalidInput();
        } else {
            addTrip();
            clearData();
        }
    }

    private void addTrip() {
        Trip trip = new Trip();
        trip.setInvoiceNo(invoiceNo);
        trip.setGateNo(gateNo);
        trip.setReceivedParcels(receivedParcels);
        trip.setReceivedQuantity(receivedQuantity);
        trip.setTransporterNo(transporterNo);
        trip.setTruckRegNo(truckRegNo);
        trip.setDriverName(driverName);
        trip.setSealNo(sealNo);

        tripHashMap.put(invoiceNo, trip);
    }

    private void validateTrips() {
        initInputValues();
        if (!sealNo.isEmpty() && !driverName.isEmpty() && receivedQuantity > 0 && receivedParcels > 0 && !invoiceNo.isEmpty()){
            addTrip();
        }

        if (!driverName.isEmpty() && !sealNo.isEmpty())//edited on 31/07/2020
        {
            for (Trip trip : tripHashMap.values()) {
                TripData.Companion.insertOrUpdate(trip);//save data into database
            }
            finalizeTripsSubmission();
        } else
            invalidInput();
    }

    private void initInputValues() {
        invoiceNo = invoiceBarcodeTextView.getText().toString();
        sealNo = edtSealNo.getText().toString();
        driverName = edtDriver.getText().toString();
        receivedQuantity = Integer.parseInt(edtReceivedQuantity.getText().toString());
        receivedParcels = Integer.parseInt(edtReceivedParcels.getText().toString());
    }

    // this method will send to webAPI
    private void finalizeTripsSubmission() {
        Toast.makeText(this, "Trips submitted successfully.", Toast.LENGTH_SHORT).show();
        SyncTrips.Companion.uploadUnSyncedTrips();
        clearData();
        tripHashMap.clear();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void invalidInput() {
        new AlertDialog.Builder(Next2.this, R.style.AlertDialogTheme)
                .setTitle("Info Alert")
                .setMessage("Please enter all required fields.")
                .setCancelable(false)
                .setPositiveButton("ok", (dialog, which) -> dialog.dismiss()).show();
    }
}