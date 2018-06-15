package com.imovie.mogic.web;

import android.os.Handler;
import android.os.Message;

import com.imovie.mogic.BuildConfig;
import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.dbbase.model.BaseModel;
import com.imovie.mogic.dbbase.model.BaseObject;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;
import com.imovie.mogic.dbbase.util.JsonFormatter;
import com.imovie.mogic.dbbase.util.JsonHelper;
import com.imovie.mogic.web.http.HttpHelper;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ${zhouxinshan} on 2016/4/12.
 * <H3>BaseWebHelper</H3>
 */
public abstract class BaseWebHelper extends BaseObject {
    protected static final Map<Class<?>, Object> primitiveWrapperMap = new HashMap<Class<?>, Object>();

    static {
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.TRUE);
        primitiveWrapperMap.put(Byte.TYPE, new Byte("0"));
        primitiveWrapperMap.put(Character.TYPE, new Character('0'));
        primitiveWrapperMap.put(Short.TYPE, new Short("0"));
        primitiveWrapperMap.put(Integer.TYPE, new Integer("0"));
        primitiveWrapperMap.put(Long.TYPE, new Long("0"));
        primitiveWrapperMap.put(Double.TYPE, new Double("0"));
        primitiveWrapperMap.put(Float.TYPE, new Float("0"));
        primitiveWrapperMap.put(Void.TYPE, new Object());
    }

    /**
     * send your post to Url
     *
     * @param Url      the target address
     * @param type    return string type
     * @param paramMap params key-value
     * @param listener result listener
     */
    protected void sendPost(final String Url, final int type,final int urlType, Map<String, Object> paramMap, final IResultListener listener) {

        sendPost(true, Url, type,urlType, paramMap, listener);

    }

    /**
     * send your post to Url
     *
     * @param isUsingHandler whether the result in using thread or in result thread
     * @param Url            the target address
     * @param paramMap       params key-value
     * @param listener       result listener
     */
    protected void sendPost(final boolean isUsingHandler, final String Url, final int type,final int urlType, Map<String, Object> paramMap, final IResultListener listener) {
        try {
            final Handler handler;
            if (isUsingHandler) {
                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if (listener != null) {
                            listener.onResult(msg.obj != null ? (CoreFuncReturn) msg.obj
                                    : new CoreFuncReturn(false, "获取数据失败"));
                        }
                        return true;
                    }
                });
            } else {
                handler = null;
            }

            HttpHelper helper;
            if (type == HttpHelper.TYPE) {
                helper = new HttpHelper(Url);
            } else if (type == HttpHelper.TYPE_1) {
                helper = new HttpHelper(Url, HttpHelper.TYPE_1);
            }else{
                helper = new HttpHelper(Url, type);
            }
            helper.setRequestParam(paramMap);
            if ("true".equals(BuildConfig.DEBUG_LOG)) {
                logMsg("----调用接口URL是：" + Url + "。 参数是：" + paramMap.toString());
            }

            helper.setOnResultListener(new HttpHelper.onResultListener() {
                @Override
                public void onResult(CoreFuncReturn result) {
//                    logMsg("调用接口URL是：" + Url + "。 返回结果：" + JsonFormatter.format(result.tag + ""));
                    if ("true".equals(BuildConfig.DEBUG_LOG)) {
                    logMsg("---调用接口URL是：" + Url + "。 返回结果：" + result.tag + "");
                    }
                    if (result.isOK) {
                        if (isUsingHandler) {
                            Message message = new Message();
                            message.what = 0;
                            message.obj = new CoreFuncReturn(true, "获取数据成功！", result.tag);
                            handler.sendMessage(message);
                        } else {
                            if (listener != null) {
                                listener.onResult(result.tag != null ? (CoreFuncReturn) result.tag
                                        : new CoreFuncReturn(false, "获取数据失败"));
                            }
                        }
                    } else {
                        if (isUsingHandler) {
                            Message message = new Message();
                            message.what = 0;
                            message.obj = new CoreFuncReturn(false, result.msg, result.tag);
                            handler.sendMessage(message);
                        } else {
                            if (listener != null) {
                                listener.onResult(new CoreFuncReturn(false, result.msg, result.tag));
                            }
                        }
                    }
                }
            });
            helper.exec();
            // helper.send();
        } catch (Exception ex) {
            logErr(ex);
        } finally {
            logMsg("调用接口URL是：" + Url + "。 参数是：" + JsonFormatter.format(JsonHelper.map2Json(paramMap)));
        }
    }


    /**
     * @param TCls           返回结果model class
     * @param url
     * @param paramMap
     * @param resultListener
     * @param <T>
     */
    public abstract <T extends BaseModel> void sendPostWithTranslate(final Class<T> TCls, final String url, final int type, final int urlType, Map<String, Object> paramMap,
                                                                     final IModelResultListener<T> resultListener);


}
