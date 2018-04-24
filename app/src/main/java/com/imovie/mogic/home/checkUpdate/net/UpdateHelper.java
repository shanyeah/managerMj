package com.imovie.mogic.home.checkUpdate.net;

import android.content.Context;
import android.content.Intent;

import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.dbbase.util.LogUtil;
import com.imovie.mogic.home.checkUpdate.AutoUpdateActivity;
import com.imovie.mogic.home.checkUpdate.model.UpdateModel;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;

import java.util.List;

/**
 * 检测更新工具
 */
public class UpdateHelper{

    // 自动更新版本
    public static void autoCheckUpdate(final Context ctx) {

        CheckUpdateWebHelper.checkUpdate(new IModelResultListener<UpdateModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return true;
            }

            @Override
            public void onSuccess(String resultCode, UpdateModel resultModel, List<UpdateModel> resultModelList, String resultMsg, String hint) {

                if (resultModel != null && !StringHelper.isEmpty(resultModel.versionName) && !resultModel.versionName.equals(AppConfig.getVersionName())) {
                    if(resultModel.upgradeType!=0) {
                        Intent intent = new Intent(ctx, AutoUpdateActivity.class);
                        intent.putExtra(AutoUpdateActivity.INTENT_KEY_UPDATEMODEL, resultModel);
                        ctx.startActivity(intent);
                    }
                }
//                Log.e("-----v",AppConfig.getVersionName());

                LogUtil.LogMsg(getClass(), "获取自动更新成功！\n更新信息提示是：" + resultMsg);
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
                LogUtil.LogMsg(getClass(), "获取自动更新失败了！\nresultMsg --> " + resultMsg);
            }

            @Override
            public void onError(String errorMsg) {
                LogUtil.LogMsg(getClass(), "获取自动更新异常！\nerror --> " + errorMsg);
            }
        });
    }

//    // 手动更新版本
//    public static void manualCheckUpdate(final Context ctx) {
//        CheckUpdateWebHelper.checkUpdate(new IModelResultListener<UpdateModel>() {
//            @Override
//            public boolean onGetResultModel(HttpResultModel resultModel) {
//                return true;
//            }
//
//            @Override
//            public void onSuccess(String resultCode, UpdateModel resultModel, List<UpdateModel> resultModelList, String resultMsg, String hint) {
//                if(null == resultModel) {
//                    if(null != resultMsg && !resultMsg.isEmpty()) {
//                        Toast.makeText(ctx, resultMsg, Toast.LENGTH_SHORT).show();
//                    }
//                    return;
//                }
//                //
//                Intent intent = new Intent(ctx, ManualUpdateActivity.class);
//                intent.putExtra(ManualUpdateActivity.INTENT_KEY_UPDATEMODEL, resultModel);
//                ctx.startActivity(intent);
//            }
//
//            @Override
//            public void onFail(String resultCode, String resultMsg, String hint) {
//                Toast.makeText(ctx, resultMsg, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//                Toast.makeText(ctx, errorMsg, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
