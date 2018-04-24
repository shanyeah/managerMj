package com.imovie.mogic.myRank;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.imovie.mogic.R;
import com.imovie.mogic.card.model.InternetBarModel;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.myRank.net.RankWebHelper;
import com.imovie.mogic.myRank.widget.SpinerPopWindow;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class MovieSelectActivity extends BaseActivity {
    private Button btSelectMovie;
    private TitleBar titleBar;

    private SpinerPopWindow<InternetBarModel> popWindow;
    private List<InternetBarModel> listRoom = new ArrayList<>();

    private SpinerPopWindow<InternetBarModel> mSpinerPopWindow;
    private List<InternetBarModel> list = new ArrayList<>();
    private TextView tvValue;
    private TextView tvRoomCategory;

    private String stgId = "";
    private String roomId = "";
    private int movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discovery_movie_select);
        initView();
        initListener();
        movieId = getIntent().getIntExtra("movieId",0);
        getMovieStgList(movieId);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    private void getMovieStgList(int movieId){
        RankWebHelper.getMovieStgList(movieId, new IModelResultListener<InternetBarModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, InternetBarModel resultModel, List<InternetBarModel> resultModelList, String resultMsg, String hint) {
                if(resultModelList.size()>0){
                    mSpinerPopWindow.refreshData(resultModelList);
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

    private void getRoomCategoryList(String stgId){
        RankWebHelper.getRoomCategoryList(stgId, new IModelResultListener<InternetBarModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, InternetBarModel resultModel, List<InternetBarModel> resultModelList, String resultMsg, String hint) {
                if(resultModelList.size()>0){
                    popWindow.setWidth(tvRoomCategory.getWidth());
                    popWindow.showAsDropDown(tvRoomCategory);
                    setTvRoomCategory(R.drawable.icon_up);
                    popWindow.refreshData(resultModelList);
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

    private void selectMovie(String stgId,int movieId,String roomCategoryId){
        RankWebHelper.getBookMovie(stgId, movieId,roomCategoryId,new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
//                Log.e("-----movie",""+resultCode);
                if(resultCode.contains("SUCCESS")){
                    Toast.makeText(MovieSelectActivity.this, "成功预定电影",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(MovieSelectActivity.this, "预定电影失败",Toast.LENGTH_SHORT).show();  
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



    private void initView() {
        btSelectMovie = (Button) findViewById(R.id.btSelectMovie);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitle("预定电影");
        tvValue = (TextView) findViewById(R.id.tv_value);
        tvRoomCategory = (TextView) findViewById(R.id.tvRoomCategory);
        mSpinerPopWindow = new SpinerPopWindow<InternetBarModel>(this, list,itemClickListener);
        popWindow = new SpinerPopWindow<InternetBarModel>(this, listRoom,itemRoomClickListener);


    }

    private void initListener() {
        btSelectMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringHelper.isEmpty(stgId)){
                    Toast.makeText(MovieSelectActivity.this, "请先选择网吧",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringHelper.isEmpty(roomId)){
                    Toast.makeText(MovieSelectActivity.this, "请先选择包房",Toast.LENGTH_SHORT).show();
                    return;
                }

                selectMovie(stgId,movieId,roomId);

            }
        });
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        mSpinerPopWindow.setWidth(tvValue.getWidth());
                        mSpinerPopWindow.showAsDropDown(tvValue);
                        setTextImage(R.drawable.icon_up);

            }
        });

        tvRoomCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringHelper.isEmpty(stgId)){
                    Toast.makeText(MovieSelectActivity.this, "请先选择网吧",Toast.LENGTH_SHORT).show();
                    return;
                }

                getRoomCategoryList(stgId);

            }
        });

        mSpinerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setTextImage(R.drawable.icon_down);
            }
        });
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setTvRoomCategory(R.drawable.icon_down);
            }
        });
    }



    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            InternetBarModel internetBarModel = (InternetBarModel)mSpinerPopWindow.list.get(position);
            tvValue.setText(internetBarModel.name);
            mSpinerPopWindow.dismiss();
            stgId = "" + internetBarModel.id;
//            Toast.makeText(MovieSelectActivity.this, "点击了:" + internetBarModel.id,Toast.LENGTH_SHORT).show();
        }
    };

    private AdapterView.OnItemClickListener itemRoomClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            InternetBarModel internetBarModel = (InternetBarModel)popWindow.list.get(position);
            tvRoomCategory.setText(internetBarModel.name);
            roomId = "" + internetBarModel.id;
            popWindow.dismiss();
//            Toast.makeText(MovieSelectActivity.this, "点击了:" + internetBarModel.id,Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tvValue.setCompoundDrawables(null, null, drawable, null);
    }

    private void setTvRoomCategory(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tvRoomCategory.setCompoundDrawables(null, null, drawable, null);
    }

}

