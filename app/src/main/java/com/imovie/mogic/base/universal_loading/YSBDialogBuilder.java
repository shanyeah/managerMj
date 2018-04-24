package com.imovie.mogic.base.universal_loading;

import android.app.Dialog;

/**
 * Created by zhou on 2015/12/22.
 */
public class YSBDialogBuilder {
    private static YSBDialogBuilder ourInstance = new YSBDialogBuilder();

    private Dialog mDialog;

    public static YSBDialogBuilder getInstance() {
        return ourInstance;
    }

    private YSBDialogBuilder() {
    }

    /**
     * 建立要显示的Dialog 对象，此方法不显示该dialog
     */
    public static void createLoadingDialog(Dialog dialog){

    }

    /**
     * 建立并显示要显示的dialog对象
     * @param dialog 目标dialog 对象
     */
    public static void showLoadingDialog(Dialog dialog){

    }

    /**
     * 显示builder中的dialog对象
     */
    public void show(){

    }

    /**
     * dismiss dialog
     */
    public void dismissDialog(){

    }
}
