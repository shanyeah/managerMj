package com.imovie.mogic.web.http;


import com.imovie.mogic.dbbase.model.BaseObject;

import java.net.HttpURLConnection;


/**
 * Created by zhouxinshan on 2016/4/12.
 * <p/>
 * HttpHelper base class
 */
public abstract class BaseHttpHelper extends BaseObject {

    /**
     * the connection instance
     */
    protected HttpURLConnection httpURLConnection;


}
