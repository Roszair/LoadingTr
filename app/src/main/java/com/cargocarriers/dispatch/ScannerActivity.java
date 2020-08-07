package com.cargocarriers.dispatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.cargocarriers.dispatch.MainActivity.SCAN_MODE;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = ScannerActivity.class.getSimpleName();
    private ZXingScannerView mScannerView;
    private ResultMode resultMode = ResultMode.UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        resultMode = ResultMode.Companion.getByVal(getIntent().getIntExtra(SCAN_MODE, -1));
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    protected void setStatusBarTranslucent() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        String result = rawResult.getText();
        BarcodeFormat format = rawResult.getBarcodeFormat();

        Log.v(TAG, "Scanned code: " + rawResult.getText());
        Log.v(TAG, "Scanend code type: " + rawResult.getBarcodeFormat().toString());

        //Return error
        if (result == null) {
            setResult(RESULT_CANCELED, returnErrorCode(result, format));
            finish();
        }

        if (result != null && result.isEmpty()) {
            setResult(RESULT_CANCELED, returnErrorCode(result, format));
            finish();
        }

        //Return correct code
        setResult(RESULT_OK, returnCorrectCode(result, format));
        finish();
    }

    private Intent returnErrorCode(String result, BarcodeFormat format) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ScannerConstants.ERROR_INFO,"Scanning error occurred.");
        return returnIntent;
    }

    private Intent returnCorrectCode(String result, BarcodeFormat format) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ScannerConstants.SCAN_RESULT, result);
        returnIntent.putExtra(SCAN_MODE, resultMode.getId());
        return returnIntent;
    }

    public interface ScannerConstants {
        public static final String SCAN_MODES = "SCAN_MODES";
        public static final String SCAN_RESULT = "SCAN_RESULT";
        public static final String SCAN_RESULT_TYPE = "SCAN_RESULT_TYPE";
        public static final String ERROR_INFO = "ERROR_INFO";
        public static final int BAR_SCAN = 0;
        public static final int QR_SCAN = 1;
    }
}
