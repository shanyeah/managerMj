package com.imovie.mogic.home.checkUpdate.model;


import com.imovie.mogic.dbbase.model.BaseModel;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class UpdateModel extends BaseModel {

//    public String version = "";     // 服务器最新版本号
//    public String updateTime = "";  // 更新时间
//    public boolean forceUpdate = false; // false：非强制更新  true：强制更新
//    public String url = "";         // 下载地址
//    public String   updateDec="";//更新详情

    public String versionName = "";     // 服务器最新版本号
    public String releaseTime = "";  // 更新时间
    public int upgradeType = 0; // 0为最新版本，1为有新版本，2为有新版本且必须升级
    public String downloadUrl = "";         // 下载地址
    public String remark="";//更新详情
    public long fileSize;
    public String hash;

}
