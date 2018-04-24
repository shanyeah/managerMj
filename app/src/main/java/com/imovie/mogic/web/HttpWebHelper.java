package com.imovie.mogic.web;


import com.imovie.mogic.dbbase.model.BaseModel;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;
import com.imovie.mogic.dbbase.util.LogUtil;
import com.imovie.mogic.myRank.model.HttpMovie;
import com.imovie.mogic.web.common.HttpException;
import com.imovie.mogic.web.model.HttpResultModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */
public class HttpWebHelper extends BaseWebHelper {

    public final static int TYPE_0 = 0;      // 0 返回的Stirng
    public final static int TYPE_1 = 1;   // 1返回的是list
    public final static int TYPE_2 = 2;   //其它
    public final static int TYPE_3 = 3;   //3返回的是map

        @Override
        public <T extends BaseModel> void sendPostWithTranslate(final Class<T> TCls, final String url, final int type,final int urlType, Map<String, Object> paramMap,
                                                                final IModelResultListener<T> resultListener) {
//        System.out.print("\n---url:" + url);

            sendPost(url, type,urlType, paramMap, new IResultListener() {
                @Override
                public void onResult(CoreFuncReturn result) {

                    try {
                        if (result.isOK) {
//                        Log.e("----tag:",""+result.tag);
                            if (resultListener == null)
                                return;

                            HttpResultModel resultModel = new HttpResultModel();

                            if (urlType == 1) {
                                HttpResultModel model = new HttpResultModel();
                                model.setModelByJson(result.tag + "");
                                ArrayList<T> arrayList = changeData(model.data, TCls);
                                resultListener.onSuccess(model.code, null, arrayList, model.message, model.hint);
                                return;
                            } else if (urlType == 0) {
                                resultModel.setModelByJson(result.tag + "");
                                resultListener.onSuccess(result.tag + "", null, null, resultModel.message, resultModel.hint);
                            } else if (urlType == 2) {
                                HttpMovie model = new HttpMovie();
                                model.setModelByJson(result.tag + "");
                                ArrayList<T> arrayList = changeData(model.movies, TCls);
                                resultListener.onSuccess(model.returnCode, null, arrayList, model.returnMsg, model.hint);
                                return;
                            } else if (urlType == 3) {
                                HttpResultModel model = new HttpResultModel();
                                model.setModelByJson(result.tag + "");
                                BaseModel baseModel = changeDataForMap(model.data, TCls);
                                resultListener.onSuccess(model.code, (T) baseModel, null, model.message, model.hint);
                                return;
                            }else{

//                        resultListener.onSuccess(result.tag + "", null, null, resultModel.message, resultModel.hint);
//                        System.out.print("\n---tag:"+result.tag+"");
//                        if (!resultListener.onGetResultModel(resultModel))
//                            return;

                                //success
                                if ("0".equals(resultModel.code)) {

                                    if (TCls == null) {
                                        resultListener.onSuccess(resultModel.code, null, null, resultModel.message, resultModel.hint);
                                        return;
                                    }

                                    Object data = resultModel.data;

                                    if (data == null) {
                                        resultListener.onSuccess(resultModel.code, null, null, resultModel.message, resultModel.hint);
                                        return;
                                    }

                                    Constructor[] constructors = TCls.getConstructors();
                                    Constructor constructor = constructors[0];
                                    List<Object> params = new ArrayList<Object>();
                                    for (Class<?> pType : constructor.getParameterTypes()) {
                                        params.add((pType.isPrimitive()) ? primitiveWrapperMap.get(pType) : null);
                                    }

                                    if (data instanceof Map) {

                                        BaseModel baseModel = null;
                                        if (params.size() == 0) {
                                            baseModel = (BaseModel) constructor.newInstance();
                                        } else {
                                            baseModel = (BaseModel) constructor.newInstance(params);
                                        }
                                        baseModel.setModelByMap((Map) data);
                                        resultListener.onSuccess(resultModel.code, (T) baseModel, null, resultModel.message, resultModel.hint);
                                    } else if (data instanceof List) {

                                        ArrayList<T> arrayList = new ArrayList<T>();
                                        for (Map map : (List<Map>) data) {

                                            T baseModel = null;
                                            if (params.size() == 0) {
                                                baseModel = (T) constructor.newInstance();
                                            } else {
                                                throw new HttpException(TCls.getSimpleName() + "返回model需要无参构造函数");
                                                //baseModel = (T) constructor.newInstance(params);
                                            }
                                            baseModel.setModelByMap(map);

                                            arrayList.add(baseModel);
                                        }
                                        resultListener.onSuccess(resultModel.code, null, arrayList, resultModel.message, resultModel.hint);
                                    }
                                } else {
                                    resultListener.onFail(resultModel.code, resultModel.message, resultModel.hint);
                                }
                            }
                        } else{

                            if (resultListener != null) {
                                LogUtil.LogErr(this.getClass(), result.msg, null);
                                resultListener.onError(result.tag + "");
                            }

                        }

                    } catch (Exception e) {
                        LogUtil.LogErr(BaseWebHelper.class, e);
                        if (resultListener != null) {
                            resultListener.onError(e.getMessage());
                        }
                    }

                }
            });

        }

        public <T extends BaseModel> ArrayList<T> changeData(final Object data,final Class<T> TCls) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            if (data == null) {
                return null;
            }

            Constructor[] constructors = TCls.getConstructors();
            Constructor constructor = constructors[0];
            List<Object> params = new ArrayList<Object>();
            for (Class<?> pType : constructor.getParameterTypes()) {
                params.add((pType.isPrimitive()) ? primitiveWrapperMap.get(pType) : null);
            }
            if (data instanceof List) {

                ArrayList<T> arrayList = new ArrayList<T>();
                for (Map map : (List<Map>) data) {

                    T baseModel = null;
                    if (params.size() == 0) {
                        baseModel = (T) constructor.newInstance();
                    } else {
                        //baseModel = (T) constructor.newInstance(params);
                    }
                    baseModel.setModelByMap(map);

                    arrayList.add(baseModel);
                }
                return arrayList;


            }else{
                return null;
            }
        }

        public <T extends BaseModel> BaseModel changeDataForMap(final Object data,final Class<T> TCls) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            if (data == null) {
                return null;
            }

            Constructor[] constructors = TCls.getConstructors();
            Constructor constructor = constructors[0];
            List<Object> params = new ArrayList<Object>();
            for (Class<?> pType : constructor.getParameterTypes()) {
                params.add((pType.isPrimitive()) ? primitiveWrapperMap.get(pType) : null);
            }
            if (data instanceof Map) {

                BaseModel baseModel = null;
                if (params.size() == 0) {
                    baseModel = (BaseModel) constructor.newInstance();
                } else {
                    baseModel = (BaseModel) constructor.newInstance(params);
                }
                baseModel.setModelByMap((Map) data);
                return baseModel;

            }else{
                return null;
            }
        }


    }
