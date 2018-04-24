package com.imovie.mogic.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.imovie.mogic.R;
import com.imovie.mogic.chat.adapter.AddContactAdapter;
import com.imovie.mogic.chat.model.ContactModel;
import com.imovie.mogic.chat.net.ChatWebHelper;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;
import com.imovie.mogic.widget.YSBPageListView;
import com.imovie.mogic.widget.interfaces.IPageList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GroupMembersActivity extends BaseActivity{

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;
    public static final String GROUP_ID = "groupId";

    private TitleBar titleBar;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;

    private YSBPageListView lvGroupList;
    public List<ContactModel.Contact> list = new ArrayList<>();
    public AddContactAdapter adapter;
    private boolean isHaveAD = false;
    private int groupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_visitor_list_activity);
        initView();
        setView();
        initListener();
//        getMyMovie();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView(){

        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle(getIntent().getStringExtra("title"));
        groupId = getIntent().getIntExtra(GROUP_ID,0);

        pull_content = (PullToRefreshFrameLayout) findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) findViewById(R.id.ff_list);
        lvGroupList = (YSBPageListView) findViewById(R.id.lvGroupList);
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setView(){
        adapter = new AddContactAdapter(this,list);
        adapter.clickType=0;
        lvGroupList.setAdapter(adapter);
        lvGroupList.setEmptyTips("暂无数据");
        setPullAndFlexListener();
        refresh();
    }



    private void setPullAndFlexListener(){
//        ff_list.setFlexView(ll_ad);
        ff_list.setFlexible(true);
        ff_list.setOnFlexChangeListener(new FlexibleFrameLayout.OnFlexChangeListener() {
            @Override
            public void onFlexChange(int flexHeight, int currentFlexHeight, boolean isOnTop) {
                if (isOnTop) {
                    pull_content.setPullEnable(true);
                } else {
                    pull_content.setPullEnable(false);
                }
            }

        });
        pull_content.setOnPullToRefreshListener(new PullToRefreshFrameLayout.OnPullToRefreshListener() {
            @Override
            public void onRefresh() {
//				llFilter.reset();
                refresh();
            }
        });

        lvGroupList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Utills.showShortToast(""+scrollState);
//                if (scrollState == SCROLL_STATE_IDLE) {

                    //列表处于最上方
                    if (view.getChildAt(0).getTop() == 0) {
//                        ff_list.setFlexible(true);
                        pull_content.setPullEnable(true);

                    } else {
//                        ff_list.setFlexible(false);
                        pull_content.setPullEnable(false);
                    }
//                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        lvGroupList.setOnPageListener(new IPageList.OnPageListener() {
            @Override
            public void onLoadMoreItems() {
                getGroupMembers();
            }
        });

    }

    private void initListener() {
        lvGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    Intent intent = new Intent(GroupMembersActivity.this, UserCenterActivity.class);
//                    intent.putExtra(UserCenterActivity.USER_ID, adapter.getItem(position).userId);
//                    startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });

    }


    public void refresh(){
        adapter.list.clear();
        adapter.notifyDataSetChanged();
        lvGroupList.setHaveMoreData(true);
        lvGroupList.setTopLoadView(true);
        lvGroupList.startLoad();

    }
    private void getGroupMembers(){
        int page = adapter.list.size()/ lvGroupList.getPageSize() + 1;
        ChatWebHelper.getGroupMembers(groupId,page, lvGroupList.getPageSize(),new IModelResultListener<ContactModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                lvGroupList.finishLoading(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, ContactModel resultModel, List<ContactModel> resultModelList, String resultMsg, String hint) {

                Log.e("----group",""+resultCode);
                pull_content.endRefresh(true);
                try {
                    if(resultModel.list.size()>0){
                        adapter.list.addAll(resultModel.list);
                        adapter.notifyDataSetChanged();
                    }
                    lvGroupList.finishLoading(lvGroupList.getPageSize() == resultModel.list.size());
                } catch (Exception e) {
                    lvGroupList.finishLoading(false);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                lvGroupList.finishLoading(true);

            }

            @Override
            public void onError(String errorMsg) {
                pull_content.endRefresh(true);
                lvGroupList.finishLoading(true);

            }
        });
    }


    private final UIHandler uiHandler = new UIHandler(this);
    private static class UIHandler extends Handler {
        private final WeakReference<GroupMembersActivity> activity;
        public UIHandler(GroupMembersActivity act) {
            super();
            activity = new WeakReference<GroupMembersActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
//                    activity.get().queryWechatOrder(activity.get().payOrderModel.orderNo);
                    break;
                case MSG_WXSEND:
//                    PayReq req = new PayReq();
//                    req.appId = activity.get().payOrderModel.appid;
//                    req.partnerId = activity.get().payOrderModel.partnerid;
//                    req.prepayId = activity.get().payOrderModel.prepayId;
//                    req.nonceStr = activity.get().payOrderModel.noncestr;
//                    req.timeStamp = activity.get().payOrderModel.timestamp;
//                    req.packageValue = activity.get().payOrderModel.packageStr;
//                    req.sign = activity.get().payOrderModel.sign;
//                    req.extData = "魔杰电竞"+activity.get().cardModel.name+"消费";
//                    Toast.makeText(activity.get(), "正常调起支付", Toast.LENGTH_SHORT).show();
//                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                    activity.get().api.sendReq(req);
                    break;
                default:
                    break;
            }
        }
    };

}


