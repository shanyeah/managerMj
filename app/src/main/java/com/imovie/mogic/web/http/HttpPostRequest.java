package com.imovie.mogic.web.http;

/**
 * Created by zhouxinshan on 2016/4/12.
 * <p/>
 * http post method request
 */
public class HttpPostRequest extends BaseRequest {

    public HttpPostRequest() {
        this(null);
    }

    public HttpPostRequest(String url) {
        this.url = url;
        this.requestMethod = Constant.METHOD_POST;
    }


}
