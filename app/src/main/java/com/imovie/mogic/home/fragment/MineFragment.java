package com.imovie.mogic.home.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.LoginActivity;
import com.imovie.mogic.login.model.MyDataModel;
import com.imovie.mogic.mine.UpdateMyInfoActivity;
import com.imovie.mogic.mine.ResetPasswordActivity;
import com.imovie.mogic.mine.MyIncomeActivity;
import com.imovie.mogic.mine.attend.widget.AttendDialog;
import com.imovie.mogic.myRank.MineChargeActivity;
import com.imovie.mogic.myRank.MineOrderActivity;
import com.imovie.mogic.myRank.MinePraiseActivity;
import com.imovie.mogic.myRank.MineRankActivity;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MineFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public File savePictureFile;
    public static final int PHOTO_REQUEST_TAKEPHOTO = 11;// take photo
    public static final int PHOTO_REQUEST_GALLERY = 12;// choose from gallery

    private String mParam1;
    private String mParam2;
    private TitleBar titleBar;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    private RelativeLayout rl_mine_request;
    private RelativeLayout rl_mine_rank;
    private RelativeLayout rl_mine_praise;
    private RelativeLayout rl_mine_exercises;
    private RelativeLayout rl_mine_mission;
    private RelativeLayout rl_mine_passwrod;
    private RelativeLayout rlMineInfo;
    private RelativeLayout rl_mine_exit;

//    private ImageView ivMineSetting;
//    public RoundedImageView profile;
//    private TextView name;
    private TextView tvTodayIntegration;
    private TextView tvBalance;
    private TextView tvBean;
    private TextView tvTicket;

    private DisplayImageOptions mOption;

    public MineFragment() {

    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.person_default_profile)
                .showImageOnFail(R.drawable.person_default_profile)
                .showImageForEmptyUri(R.drawable.person_default_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.home_fragment_mine, container, false);
        initView(v);
        setView();
        setListener();
        return v;
    }

    private void initView(View view) {
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        titleBar.setTitle("我的");
        titleBar.setLeftLayoutGone();

        pull_content = (PullToRefreshFrameLayout) view.findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) view.findViewById(R.id.ff_list);

        rl_mine_praise = (RelativeLayout) view.findViewById(R.id.rl_mine_praise);
        rl_mine_exercises = (RelativeLayout) view.findViewById(R.id.rl_mine_exercises);
        rl_mine_mission = (RelativeLayout) view.findViewById(R.id.rl_mine_mission);
        rl_mine_exit = (RelativeLayout) view.findViewById(R.id.rl_mine_exit);
        rl_mine_rank= (RelativeLayout) view.findViewById(R.id.rl_mine_rank);
        rlMineInfo = (RelativeLayout) view.findViewById(R.id.rlMineInfo);
        rl_mine_passwrod = (RelativeLayout) view.findViewById(R.id.rl_mine_password);
        rl_mine_request = (RelativeLayout) view.findViewById(R.id.rl_mine_request);

//        ivMineSetting = (ImageView) view.findViewById(R.id.ivMineSetting);
//        profile= (RoundedImageView) view.findViewById(R.id.profile);
//
//        name= (TextView) view.findViewById(R.id.name);
//        tvTodayIntegration= (TextView) view.findViewById(R.id.tvTodayIntegration);
//        tvBalance= (TextView) view.findViewById(R.id.tvBalance);
//        tvBean= (TextView) view.findViewById(R.id.tvBean);
//        tvTicket= (TextView) view.findViewById(R.id.tvTicket);

    }

    private void setView() {
        setPullAndFlexListener();
    }

    private void setPullAndFlexListener(){
//        ff_list.setFlexView(ll_ad);
//        ff_list.setFlexible(true);
        pull_content.setPullEnable(false);

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
//                getMyData();
            }
        });

    }
    private void setListener() {

        rl_mine_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MineRankActivity.class);
                intent.putExtra("stgId",21);
                startActivity(intent);
            }
        });


        rl_mine_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MinePraiseActivity.class);
                intent.putExtra("stgId",21);
                startActivity(intent);
            }
        });

        rl_mine_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttendDialog dialog = new AttendDialog(getContext());
                dialog.setContent("确定要退出登录吗？");
                dialog.setOnSelectListener(new AttendDialog.onSelectListener() {
                    @Override
                    public void onSelect(String type) {
                        SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
                        editor.putString("phone","");
                        editor.putString("password","");
                        editor.putBoolean("isLogin",false);
                        editor.putInt("memberId",0);
                        editor.putInt("status",0);
                        editor.putInt("cardNo",0);
                        editor.commit();
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });

        rl_mine_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyIncomeActivity.class);
                startActivity(intent);
            }
        });
        rl_mine_exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MineOrderActivity.class);
                intent.putExtra("stgId",21);
                startActivity(intent);
            }
        });
        rl_mine_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MineChargeActivity.class);
                intent.putExtra("stgId",21);
                startActivity(intent);
            }
        });

        rl_mine_passwrod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        rlMineInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),UpdateMyInfoActivity.class);
                startActivity(intent);
            }
        });

    }
}
