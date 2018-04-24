package com.imovie.mogic.db.DBTools;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/5/4.
 */
public class MovieManager {

    private MovieManager() {

    }
    private static final MovieManager instance = new MovieManager();

    public static synchronized MovieManager getInstance() {
        return instance;
    }


//    public void saveMovieInfoList(int movieId, List<MovieInfo> movieList) {
//        DBSaver saver = new DBSaver();
//        DBPicker picker = new DBPicker();
//
////        String sqlDelete = "delete from " + SQLiteReflect.getTableNameByCls(MovieInfo.class)
////                + " where movieId = " + movieId;
////        saver.execSQL(sqlDelete);
//
//        // clear movieInfo table for this user
////		List<MovieInfo> MovieInfoList = picker.pickModelsWithModelCode(MovieInfo.class, "movieId=" + movieId);
////		for (MovieInfo movieInfo : MovieInfoList) {
////			saver.deleteModel(movieInfo);
////		}
//
//        if (movieList == null)
//            return;
//
//		/* if table is null,insert data to table,if table exist data but "useid" is different as insert  */
//        for (MovieInfo movieInfo : movieList) {
//
////            movieInfo.movieId = movieId;
//
//            List<MovieInfo> movieList1 = picker.pickModelsWithModelCode(MovieInfo.class,
//                    "movieId=" + movieInfo.movieId);
//            if (movieList1.size() == 0) {
//                saver.insertModel(movieInfo);
//            } else {
//                saver.updateModel(movieInfo);
//            }
//        }
//    }

//    /* 查是否存在该hash */
//    public boolean queryMovieInfoByHash(String hash) {
//        DBPicker picker = new DBPicker();
//        List<MovieInfo> movieList = picker.pickModelsWithModelCode(MovieInfo.class, "hash = '" + hash + "'");
//        if (movieList.size() > 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }


//    public void createIndex(String sql) {
//        sql = "CREATE INDEX salary_index ON TotalMovieInfo (movieId)";
//        Log.e("---sql", sql);
//        DBSaver saver = new DBSaver();
//        saver.execSQL(sql);
//    }

}
