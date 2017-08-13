package com.waiter.mh;


import android.content.Intent;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.waiter.mh.utils.Config;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * 扫描条码类,采用的是ZXing(ZBar对中文支持不友好，有乱码)
 */
public class ScanActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final String TAG = ScanActivity.class.getSimpleName();
    private QRCodeView mQRCodeView;
    private int switchStart;//记录时那个界面启动的扫描

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        switchStart = getIntent().getIntExtra(Config.START_SCAN, 0);//获取别的界面传递过来的数据
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
//        mQRCodeView.startSpot();//默认开始扫描
        mQRCodeView.startSpotDelay(200);//0.5秒延时
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
//        Log.i(TAG, "result:" + result);
//        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        //发送广播
        Intent intent = new Intent();
        // 设置Intent的Action属性
        intent.setAction("com.waiter.mh.action.SCAN_CODE_BROADCAST");
        intent.putExtra("scan_code", result);
        // 发送广播
        sendBroadcast(intent);

        //盒子商品关联界面的扫描商品编号启动的扫描，只扫描一次，有结果就返回
        if (switchStart == Config.ONCE_SCAN) {
            finish();
        }
        //盒子商品关联界面的扫描盒子编号启动的扫描，会不停的扫描多个盒子
        else if (switchStart == Config.CONTINUOUS_SCAN) {

        }
        vibrate();
//        mQRCodeView.startSpot();//该方法有1.5s延时
        mQRCodeView.startSpotDelay(500);//0.5秒延时
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_spot:
                mQRCodeView.startSpot();
                break;
            case R.id.stop_spot:
                mQRCodeView.stopSpot();
                break;
            case R.id.start_spot_showrect:
                mQRCodeView.startSpotAndShowRect();
                break;
            case R.id.stop_spot_hiddenrect:
                mQRCodeView.stopSpotAndHiddenRect();
                break;
            case R.id.show_rect:
                mQRCodeView.showScanRect();
                break;
            case R.id.hidden_rect:
                mQRCodeView.hiddenScanRect();
                break;
            case R.id.start_preview:
                mQRCodeView.startCamera();
                break;
            case R.id.stop_preview:
                mQRCodeView.stopCamera();
                break;
            case R.id.open_flashlight:
                mQRCodeView.openFlashlight();
                break;
            case R.id.close_flashlight:
                mQRCodeView.closeFlashlight();
                break;
            case R.id.scan_barcode:
                mQRCodeView.changeToScanBarcodeStyle();
                break;
            case R.id.scan_qrcode:
                mQRCodeView.changeToScanQRCodeStyle();
                break;
            case R.id.choose_qrcde_from_gallery:
                break;
        }
    }
}
