package com.imovie.mogic.myRank;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.myRank.model.MovieDetail;
import com.imovie.mogic.myRank.model.MovieModel;
import com.imovie.mogic.myRank.net.RankWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MovieDetailActivity extends BaseActivity {
    
    private MovieModel movieModel;
    private DisplayImageOptions options = null;


    private TitleBar titleBar;
    private ImageView ivPoster;
    private TextView tvName;
    private TextView tvTimes;
    private TextView tvArea;
    private TextView tvLang;
    private TextView tvTimeLong;
    private TextView tvCategory;
    private TextView tvDirectors;
    private TextView tvActors;
    private TextView tvMovieDesc;
    private Button btPlayMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discovery_movie_detail);
        initView();
        setView();
        initListener();

    }

    private void initView() {
        ivPoster = (ImageView) findViewById(R.id.ivPoster);
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        tvName = (TextView) findViewById(R.id.tvName);
        tvTimes = (TextView) findViewById(R.id.tvTimes);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvLang = (TextView) findViewById(R.id.tvLang);
        tvTimeLong = (TextView) findViewById(R.id.tvTimeLong);
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        tvDirectors = (TextView) findViewById(R.id.tvDirectors);

        tvActors = (TextView) findViewById(R.id.tvActors);
        tvMovieDesc = (TextView) findViewById(R.id.tvMovieDesc);
        btPlayMovie = (Button) findViewById(R.id.btPlayMovie);
        
        movieModel = (MovieModel) getIntent().getExtras().getSerializable("movieModel");
        titleBar.setTitle(movieModel.name);
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RelativeLayout.LayoutParams ivParam=(RelativeLayout.LayoutParams)ivPoster.getLayoutParams();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int width = (screenWidth - 125) / 3;
        ivParam.width = width;
        ivParam.height =(width*7)/5;
        ivPoster.setLayoutParams(ivParam);



    }

    private void setView() {

        tvName.setText(movieModel.name);
        tvTimes.setText(movieModel.times);
        tvArea.setText(movieModel.prodNation);
        tvLang.setText(movieModel.lang);
        tvTimeLong.setText(movieModel.runTime);
        tvCategory.setText(movieModel.name);
        tvDirectors.setText(movieModel.directors);
        tvActors.setText(movieModel.actors);
        tvMovieDesc.setText(movieModel.movieDesc);

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading()
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        try {
            ImageLoader.getInstance().displayImage(movieModel.previewPoster,ivPoster,options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getMovieDetail(movieModel.id);

    }

    private void initListener() {
        btPlayMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailActivity.this,MovieSelectActivity.class);
                intent.putExtra("movieId",movieModel.id);
                startActivity(intent);
            }
        });
    }

    private void getMovieDetail(int movieId){
        RankWebHelper.getMovieDetail(movieId, new IModelResultListener<MovieModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, MovieModel resultModel, List<MovieModel> resultModelList, String resultMsg, String hint) {
//                Log.e("---e",resultCode);
                MovieDetail movieDetail = new MovieDetail();
                movieDetail.setModelByJson(resultCode);
                tvMovieDesc.setText(movieDetail.movie.movieDesc);
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

