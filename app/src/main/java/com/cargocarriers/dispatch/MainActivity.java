package com.cargocarriers.dispatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cargocarriers.objectbox.entities.trip.Trip;
import com.cargocarriers.objectbox.entities.trip.TripData;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Network.APIClient;
import model.TripJSON;
import model.TripsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cargocarriers.dispatch.ScannerActivity.ScannerConstants.SCAN_RESULT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private TextView transporterBarcodeView;
    private TextView gateBarcodeTextView;


    Button btnReset, btnScanInvoice, btnSubmit;
    private ProgressBar progressBar;
    EditText edtInvoiceNo, edtGateNo, edtGateName, edtTransporterNo, edtTruckRegNo,
            edtTransporterName, edtReceivedQuantity, edtReceivedParcels, edtDriverName, edtSealNo;
    private String truckRegNo,gateNo, transporterNo, invoiceNo;

    private Date dateTime;
    private TripJSON tripJSON;

    RadioButton yes, no;
//remember to secure the app add to manifests file

    public static final String DATETIME_KEY = "DateTimeKey";
    public static final String TRANSPORTER_NO_KEY = "TransporterNoKey";
    public static final String GATE_NO_KEY = "GateNoKey";
    public static final String TRUCK_REG_NO_KEY = "TruckRegNoKey";
    public static final int SCAN_SERIAL_REQUEST = 1007;
    public static final String SCAN_MODE = "SCAN_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionRequest.Companion.permissions(this);

        edtInvoiceNo = findViewById(R.id.edtInvoiceNo);
       // edtGateNo = findViewById(R.id.edtGateNo);
       // edtGateName = findViewById(R.id.edtGateName);
        edtTransporterName = findViewById(R.id.edtTransporterName);
        edtTransporterNo = findViewById(R.id.edtTransporterNo);
        edtReceivedQuantity = findViewById(R.id.edtReceivedQuantity);
        edtReceivedParcels = findViewById(R.id.edtReceivedParcels);
        //edtDriverName = findViewById(R.id.edtDriverName);
        //edtSealNo = findViewById(R.id.edtSealNo);
       // edtTruckRegNo = findViewById(R.id.edtTruckRegNo);
        progressBar = findViewById(R.id.progressBar);

        yes = (RadioButton) findViewById(R.id.rbYes);
        no = (RadioButton) findViewById(R.id.rbNo);

        btnScanInvoice = findViewById(R.id.btnScanInvoice);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnReset = findViewById(R.id.btnClear);

        btnScanInvoice.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        TextView datetime = findViewById(R.id.datetime);
        dateTime = Calendar.getInstance().getTime();
        datetime.setText(Calendar.getInstance().getTime().toString());
    }

    private void invalidInput() {
        new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme)
                .setTitle("Info Alert")
                .setMessage("Please enter all information in the required fields.")
                .setCancelable(false)
                .setPositiveButton("ok", (dialog, which) -> dialog.dismiss()).show();
    }

    public void openNext2() {
        Intent intent = new Intent(this, Next2.class);
        intent.putExtra(DATETIME_KEY, dateTime);
        intent.putExtra(TRANSPORTER_NO_KEY, transporterNo);
        intent.putExtra(GATE_NO_KEY, gateNo);
        intent.putExtra(TRUCK_REG_NO_KEY, truckRegNo);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScanInvoice:
                scanInvoiceBarcode();
                break;
            case R.id.btnSubmit:
               validateTrips();
                break;
            case R.id.btnClear:
                clearData();
                break;
        }
    }

    private void validateTrips() {
        // String driverName =  edtDriverName.getText().toString();
         int receivedParcels = Integer.parseInt(edtReceivedParcels.getText().toString());
         int receivedQuantity = Integer.parseInt(edtReceivedQuantity.getText().toString());
         //String truckRegNo = edtTruckRegNo.getText().toString();
        // String sealNo = edtSealNo.getText().toString();
        if (!edtReceivedParcels.getText().toString().isEmpty() && !edtReceivedQuantity.getText().toString().isEmpty() )//edited on 31/07/2020_ removed gate info23Mar2021
        {
            if(tripJSON != null) {
                if(receivedParcels == tripJSON.getIssuedParcels() && receivedQuantity == tripJSON.getIssuedQuantity()) {
                    long currentDate = Calendar.getInstance().getTimeInMillis();

                    Trip trip = new Trip();
                    trip.setInvoiceNo(invoiceNo);
                    //trip.setGateNo(tripJSON.getGateNo());
                    trip.setReceivedParcels(receivedParcels);
                    trip.setReceivedQuantity(receivedQuantity);

                    trip.setIssuedParcels(tripJSON.getIssuedParcels());

                    //trip.setReceivedQuantity(tripJSON.getIssuedQuantity());//I think is the line that causes the issued qnty not to have a value. edited @10:05 24March2021
                    trip.setIssuedQuantity(tripJSON.getIssuedQuantity());

                    trip.setTransporterNo(tripJSON.getTransporterNo());
                    trip.setTransporterName(tripJSON.getTransporterName());
                    //trip.setTruckRegNo(truckRegNo);
                    //trip.setDriverName(driverName);
                    //trip.setSealNo(sealNo);
                    //trip.setGateName(tripJSON.getGateName());
                    trip.setDispatchDate(currentDate);
                    TripData.Companion.insertOrUpdate(trip);//save data into database
                    finalizeTripsSubmission();
                }
                else{
                    Toast.makeText(this, "Quantity/Parcel does not match, please double check your quantity/parcel.", Toast.LENGTH_SHORT).show();
                }
            }
        } else
            invalidInput();
    }
    private void finalizeTripsSubmission() {
        Toast.makeText(this, "Trips submitted successfully.", Toast.LENGTH_SHORT).show();
        SyncTrips.Companion.uploadUnSyncedTrips();
        clearData();
    }

    /*@Override //removed truck/gate info 23Mar2021
    protected void onResume() {
        super.onResume();
        edtTruckRegNo.getText().clear();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_SERIAL_REQUEST) {
            if (data != null) {
                ResultMode resultMode = ResultMode.Companion.getByVal(data.getIntExtra(SCAN_MODE, -1));
                String result = data.getStringExtra(SCAN_RESULT);
                switch (resultMode) {

                    case SCAN_INVOICE:
                        if (result != null) {
                            invoiceNo = result;
                            validInvoiceNumberOnline(invoiceNo);
                            edtInvoiceNo.setText(result);
                        }
                        break;
                    /*case SCAN_GATE:
                        if (result != null) {
                            gateNo = result;
                            gateBarcodeTextView.setText(result);
                        }
                        break;*/
                    case SCAN_TRANSPORTER:
                        if (result != null) {
                            transporterNo = result;
                            transporterBarcodeView.setText(result);
                        }
                        break;
                }
            }
        }
    }

    private void clearData() {
        edtReceivedQuantity.setText("");
        edtReceivedParcels.setText("");
       // edtDriverName.setText("");
       // edtSealNo.setText("");
        //edtTruckRegNo.setText("");
       // edtGateName.setText("");
       // edtGateNo.setText("");
        edtTransporterName.setText("");
        edtTransporterNo.setText("");
        edtInvoiceNo.setText("");

        tripJSON = null;
    }

    private void validInvoiceNumberOnline(String invoiceNo) {
        progressBar.setVisibility(View.VISIBLE);
        TripsApi tripsApi = APIClient.getRetrofit().create(TripsApi.class);
        Call<TripJSON> call = tripsApi.fetchTrips(invoiceNo);
        tripJSON = null;
        call.enqueue(new Callback<TripJSON>() {
            @Override
            public void onResponse(Call<TripJSON> call, Response<TripJSON> response) {
                if (response.isSuccessful()) {
                    tripJSON = response.body();
                    Log.d(TAG,"Results: " + tripJSON);
                    updateViews(tripJSON);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TripJSON> call, Throwable t) {
                tripJSON = null;
                Log.e(TAG, "Api error: ", t);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to validate invoice number online. \n Check internet connection.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateViews(TripJSON tripJSON) {
        if (tripJSON == null)
            Toast.makeText(MainActivity.this, "Failed to validate invoice number online.", Toast.LENGTH_LONG).show();
        else {
            edtInvoiceNo.setText(tripJSON.getInvoiceNo());
           // edtGateName.setText(tripJSON.getGateName());
            //edtGateNo.setText(tripJSON.getGateNo());//returning boolean instead of...
            edtTransporterNo.setText(tripJSON.getTransporterNo());
            edtTransporterName.setText(tripJSON.getTransporterName());
        }
    }

    public void gate(View view) { //...this method is linked to the invbut
        PermissionRequest.Companion.permissions(this);
        Intent intent = new Intent(this, ScannerActivity.class);
        intent.putExtra(SCAN_MODE, ResultMode.SCAN_GATE.getId());
        startActivityForResult(intent, SCAN_SERIAL_REQUEST);
    }

    public void transporter(View view) {
        PermissionRequest.Companion.permissions(this);
        Intent intent = new Intent(this, ScannerActivity.class);
        intent.putExtra(SCAN_MODE, ResultMode.SCAN_TRANSPORTER.getId());
        startActivityForResult(intent, SCAN_SERIAL_REQUEST);
    }

    public void scanInvoiceBarcode() {
        PermissionRequest.Companion.permissions(this);
        Intent intent = new Intent(this, ScannerActivity.class);
        intent.putExtra(SCAN_MODE, ResultMode.SCAN_INVOICE.getId());
        startActivityForResult(intent, SCAN_SERIAL_REQUEST);
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        String str="";

        switch (view.getId()){
            case R.id.rbNo:
                if (checked)
                    str = "No, package is not in good condition";
                break;
            case R.id.rbYes:
                if (checked)
                    str = "Yes, package is in good condition";
                break;
        }
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }
}
