package com.imovie.mogic.login.net;

import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;

import java.util.List;

/**
 * Created by $zhou on 2017/3/7 0007.
 */

public class LoginHelper {
    /**
     *验证码
     * @param mobile
     */
    public static void getRegAuth(final String mobile,final int type) {
        LoginWebHelper.getRegAuth(mobile,type, new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
                TestModel t = resultModel;
                String s= resultCode;
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {

            }

            @Override
            public void onError(String errorMsg) {

            }
        });

    }

    /**
     *注册
     * @param mobile
     */
    public static void getRegister(final String userName,final String password,final int idType,final String mobile,final String phoneVerifyCode,final String idNumber) {
        LoginWebHelper.getRegister(userName,password,idType,mobile,phoneVerifyCode,idNumber, new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
                TestModel t = resultModel;
                String s= resultCode;
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {

            }

            @Override
            public void onError(String errorMsg) {

            }
        });

    }

}
