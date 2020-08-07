package com.cargocarriers.dispatch;

import androidx.annotation.Nullable;
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

import static com.cargocarriers.dispatch.ScannerActivity.ScannerConstants.SCAN_RESULT;

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
    public static final int SCAN_SERIAL_REQUEST = 1007;
    public static final String SCAN_MODE = "SCAN_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionRequest.Companion.permissions(this);
        zXingScannerView = findViewById(R.id.zXingScannerViewMain);
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
            case R.id.btnNext:
                truckRegNo = edtTruckRegNo.getText().toString();
                if (!truckRegNo.isEmpty() && gateNo > 0 && transporterNo > 0) {
                    openNext2();
                } else
                    invalidInput();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        edtTruckRegNo.getText().clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_SERIAL_REQUEST){
            if(data != null){
                ResultMode resultMode = ResultMode.Companion.getByVal(data.getIntExtra(SCAN_MODE, -1));
                String result = data.getStringExtra(SCAN_RESULT);
                switch (resultMode){
                    case SCAN_GATE:
                        if(result != null) {
                            gateNo = Long.parseLong(result);
                            gateBarcodeTextView.setText(result);
                        }
                        break;
                    case SCAN_TRANSPORTER:
                        if(result != null) {
                            transporterNo = Long.parseLong(result);
                            transporterBarcodeView.setText(result);
                        }
                        break;
                }
            }
        }
    }

    public void gate(View view) {
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
}
