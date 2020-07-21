package com.cargocarriers.dispatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.Result;
import java.util.Calendar;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView transporterBarcodeView;
    private TextView gateBarcodeTextView;
    private ZXingScannerView zXingScannerView;
    private Button next;
    EditText edtTruckRegNo;
    private String truckRegNo;
    private long gateNo;
    private long transporterNo;
    private long dateTime;

//remember to secure the app add to manifests file

    public static final String DATETIME_KEY = "DateTimeKey";
    public static final String TRANSPORTER_NO_KEY = "TransporterNoKey";
    public static final String GATE_NO_KEY = "GateNoKey";
    public static final String TRUCK_REG_NO_KEY = "TruckRegNoKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gateBarcodeTextView = findViewById(com.cargocarriers.dispatch.R.id.gateBarcodeTextView);
        transporterBarcodeView = findViewById(com.cargocarriers.dispatch.R.id.transporterBarcodeView);
        edtTruckRegNo = findViewById(R.id.truckRegNo);
        TextView datetime = findViewById(R.id.datetime);
        dateTime = Calendar.getInstance().getTimeInMillis();
        datetime.setText(Calendar.getInstance().getTime().toString());

        next = findViewById(R.id.btnNext);
        next.setOnClickListener(this);

    }

    private void invalidInput() {
        new AlertDialog.Builder(MainActivity.this)
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
            case R.id.btnNext:
                truckRegNo = edtTruckRegNo.getText().toString();
                if (!truckRegNo.isEmpty() && gateNo > 0 && transporterNo > 0) {
                    openNext2();
                } else
                    invalidInput();
                break;
        }
    }

    public void gate(View view) {
        zXingScannerView = findViewById(R.id.zXingScannerView);
        zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            public void handleResult(Result result) {
                //gateNo = String.valueOf (parseInt (result.getText()));
                gateNo = Long.parseLong(result.getText());
                gateBarcodeTextView.setText(result.getText());
                zXingScannerView.resumeCameraPreview(this);
            }
        });
        zXingScannerView.startCamera();
    }

    public void transporter(View view) {
        zXingScannerView = findViewById(R.id.zXingScannerView);
        zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            public void handleResult(Result result) {
                transporterNo = Long.parseLong(result.getText());
                transporterBarcodeView.setText(String.valueOf(transporterNo));
                zXingScannerView.resumeCameraPreview(this);

            }
        });
        zXingScannerView.startCamera();
    }
}
