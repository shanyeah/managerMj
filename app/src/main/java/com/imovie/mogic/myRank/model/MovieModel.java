package com.imovie.mogic.myRank.model;


import com.imovie.mogic.dbbase.model.BaseModel;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class MovieModel extends BaseModel {
    public int id;
    public String name;
    public String enName;
    public String aliasName;
    public String movieDesc;
    public String movieShortDesc;
    public String directors;
    public String actors;
    public String runTime;
    public String prodNation;
    public long pubDate;
    public long updateTime;

    public String lang;
    public String times;
    public String previewPoster;

    public String xmlCrc;
    public String priceType;
    public String category;

    public String fileQuality;
    public int fileId;
    public long releaseTime;
    public String hash;
    public int movieStatus;

    public String movieStudio;
    public String award;
    public String nomination;

    public String imdbScore;
    public String smallPoster;
    public String smallPosterHash;
    public int recentPlayCount;

}
