package com.imovie.mogic.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.wxapi.Constants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "WXPayEntryActivity";
	public static final String PAY_FINISH_BROADCAST="pay_finish_broadcast";
    	private IWXAPI api;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_USER_CANCEL:
//				Utills.showShortToast("支付取消");
//				Intent intent1 = new Intent(PAY_FINISH_BROADCAST);
//				sendBroadcast(intent1);
				break;
			case BaseResp.ErrCode.ERR_COMM:
				Utills.showShortToast("支付失败，稍后再试.");
				break;
			case BaseResp.ErrCode.ERR_OK:
				Intent intent = new Intent(PAY_FINISH_BROADCAST);
				sendBroadcast(intent);
				break;
			default:
				break;
			}
			this.finish();
		}
	}
}