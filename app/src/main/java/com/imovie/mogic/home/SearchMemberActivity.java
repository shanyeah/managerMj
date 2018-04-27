package com.imovie.mogic.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.zxing.activity.CaptureActivity;
import com.imovie.mogic.home.adater.UserInfoAdapter;
import com.imovie.mogic.home.model.SearchModel;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.mine.SetingHallActivity;
import com.imovie.mogic.mine.net.MineWebHelper;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.ClearButtonEditText;
import com.imovie.mogic.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class SearchMemberActivity extends BaseActivity {
    public static final int SELECT_RESULT = 90;
    public TitleBar titleBar;
    public ListView lvGoodsTagList;
    public UserInfoAdapter adapter;
    public List<SearchUserModel> list = new ArrayList<>();
    public Button btChargeSelect;
    public EditText etUserId;
    public SearchUserModel userModel = new SearchUserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_search_member);
        initView();
        setView();
        initListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent mIntent = new Intent();
            mIntent.putExtra("result", SELECT_RESULT);
            mIntent.putExtra("userModel", userModel);
            SearchMemberActivity.this.setResult(SELECT_RESULT, mIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    private void initView(){
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("选择付款帐号");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("result", SELECT_RESULT);
                    mIntent.putExtra("userModel", userModel);
                    SearchMemberActivity.this.setResult(SELECT_RESULT, mIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        btChargeSelect = (Button) findViewById(R.id.btChargeSelect);
        etUserId = (EditText) findViewById(R.id.etUserId);
        lvGoodsTagList = (ListView) findViewById(R.id.lvGoodsTagList);

    }

    private void setView(){
        adapter = new UserInfoAdapter(SearchMemberActivity.this,list);
        lvGoodsTagList.setAdapter(adapter);
        lvGoodsTagList.setDivider(null);
    }

    private void initListener() {
        btChargeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etUserId.getText().toString();
                if(StringHelper.isEmail(user)) {
                    Utills.showShortToast("请输入充值用户");
                    return;
                }
                getCheckUserInfo(user);
            }
        });

        lvGoodsTagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userModel = adapter.getItem(i);
                Intent mIntent = new Intent();
                mIntent.putExtra("result", SELECT_RESULT);
                mIntent.putExtra("userModel", userModel);
                SearchMemberActivity.this.setResult(SELECT_RESULT, mIntent);
                finish();
            }
        });
    }

    public void getCheckUserInfo(String input){
        HomeWebHelper.getCheckUserInfo(input,new IModelResultListener<SearchModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, SearchModel resultModel, List<SearchModel> resultModelList, String resultMsg, String hint) {
//                Log.e("----city:",""+resultCode);
                if(resultCode.equals("0")) {
                    adapter.list.clear();
                    if (resultModel.list.size() > 0) {
                        adapter.list.addAll(resultModel.list);
                        adapter.notifyDataSetChanged();
                    }
                }

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

