package com.imovie.mogic.ScanPay;

import android.graphics.Bitmap;
import android.os.Handler;

import com.imovie.mogic.ScanPay.view.ViewfinderView;
import com.imovie.mogic.home.BaseActivity;


/**
 * Created by zhou on 2016/1/4.
 */
public abstract class BaseScannerActivity extends BaseActivity {
    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public abstract void handleDecode(com.google.zxing.Result result, Bitmap barcode);

    /**
     * 绘制取景框
     */
    public abstract void drawViewfinder();

    /**
     * 返回
     * @return
     */
    public  abstract Handler getDispatchHandler();

    public abstract ViewfinderView getViewfinderView() ;
}
