package com.imovie.mogic.web.http;

import java.util.HashMap;


/**
 * Created by zhouxinshan on 2016/4/12..
 * <p/>
 * http request
 */
public class BaseRequest {

    /**
     * the request url(your server ip address)
     */
    protected String url;

    /**
     * if true
     * when connected, you can get the inputStream from server
     * otherwise not
     * whether you change it depends on if you want get the result from server, default true will get result from server.
     */
    protected boolean doInput = true;

    /**
     * if true
     * when connected, you can get an outputStream to server
     * otherwise not
     */
    protected boolean doOutput = true;

    /**
     * the method of request server, one of {@link Constant#METHOD_POST}, {@link Constant#METHOD_PUT} or {@link Constant#METHOD_GET}
     */
    protected String requestMethod;

    /**
     * properties of request. it will write in your http request header
     *
     * @see Constant
     */
    protected HashMap<String, String> properties;

    /**
     * the entity of request. data(or params) you need send to server
     */
    protected String entity;

}
