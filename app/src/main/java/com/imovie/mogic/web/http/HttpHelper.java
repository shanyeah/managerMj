package com.imovie.mogic.web.http;

import android.util.Log;

import com.imovie.mogic.dbbase.model.CoreFuncReturn;
import com.imovie.mogic.dbbase.util.JsonFormatter;
import com.imovie.mogic.dbbase.util.JsonHelper;
import com.imovie.mogic.utills.AliJsonUtil;
import com.imovie.mogic.web.common.CompressUtil;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by zhouxinshan on 2016/4/12.
 * <p/>
 * http request helper
 */
public class HttpHelper extends BaseHttpHelper {

    public final static int TYPE = 0;      // 0 返回的是xml,换掉"\r|\n"
    public final static int TYPE_1 = 1;   // 1返回的是string,不换掉"\r|\n"
    public final static int TYPE_2 = 2;   //2post方法返json格式
    public final static int TYPE_3 = 3;   //get方法
    public final static int TYPE_4 = 4;   //put方法
    public final static int TYPE_5 = 5;   //delete方法

    /**
     * the url of you request
     */
    protected String url;
    protected int textType = 0;     // 0 返回的是xml,换掉"\r|\n", 1返回的是string,不换掉"\r|\n"

    public HttpHelper() {
        super();
    }

    public HttpHelper(String url) {
        this.url = url;
    }

    public HttpHelper(String url, int textType) {
        this.url = url;
        this.textType = textType;
    }

    /**
     * get the url of you request
     *
     * @return url
     */
    public String getRequestUrl() {
        return url;
    }

    /**
     * you can set your requestUrl
     *
     * @param url the url of you request
     */
    public void setRequestUrl(String url) {
        this.url = url;
    }

    onResultListener listener;

    String requestParam = "";

    public void exec() {

        if (url == null)
            return;
        try {

            new Thread(new Runnable() {
                @Override
                public void run() {
//                    try {
                        String result = null;

                        if (textType == TYPE_5) {
                            try {
                                URL url = new URL(HttpHelper.this.url);
                                httpURLConnection = (HttpURLConnection) url.openConnection();
                                httpURLConnection.setDoInput(true);
                                httpURLConnection.setRequestProperty(Constant.PROPERTY_CONTENT_TYPE, Constant.CONTENT_TYPE.APPLICATION_JSON);
//                                httpURLConnection.setRequestProperty(Constant.PROPERTY_ACCEPT_ENCODING, "gzip,deflate");
                                httpURLConnection.setRequestProperty(Constant.PROPERTY_CONNECTION, "Keep-Alive");
                                httpURLConnection.setDoOutput(true);
                                httpURLConnection.setRequestMethod(Constant.METHOD_DELETE);
                                int respondCode = httpURLConnection.getResponseCode();
                                if (respondCode == HttpURLConnection.HTTP_OK) {

                                    InputStream inputStream = httpURLConnection.getInputStream();
                                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                                    byte[] b = new byte[4096];
                                    for (int n; (n = inputStream.read(b)) != -1;) {
                                        outStream.write(b, 0, n);
                                    }
                                    b=null;
                                    result = new String(outStream.toByteArray(),"utf-8");
                                    outStream.close();
                                    listener.onResult(new CoreFuncReturn(true, "获取成功", result));
                                }else {
                                    listener.onResult(new CoreFuncReturn(false, "访问失败"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            BaseReturn conn = XMLDataGetter.doGetHttpRequest(HttpHelper.this.url);
//                            result = conn.getOtherText().toString();
//                            result = result.replaceAll("\r|\n", "");
                        } else if (textType == TYPE_1) {
//                            BaseReturn conn = XMLDataGetter.doGetHttpRequest(HttpHelper.this.url);
//                            result = conn.getOtherText().toString();
                        } else {
                            try {
                                URL url = new URL(HttpHelper.this.url);
                                httpURLConnection = (HttpURLConnection) url.openConnection();
                                httpURLConnection.setDoInput(true);
                                httpURLConnection.setRequestProperty(Constant.PROPERTY_CONTENT_TYPE, Constant.CONTENT_TYPE.APPLICATION_JSON);
//                                httpURLConnection.setRequestProperty(Constant.PROPERTY_ACCEPT_ENCODING, "gzip,deflate");
                                httpURLConnection.setRequestProperty(Constant.PROPERTY_CONNECTION, "Keep-Alive");

                                if(textType==TYPE_3){
                                    httpURLConnection.setDoOutput(false);
                                    httpURLConnection.setRequestMethod(Constant.METHOD_GET);
                                    int respondCode = httpURLConnection.getResponseCode();
                                    if (respondCode == HttpURLConnection.HTTP_OK) {

                                    InputStream inputStream = httpURLConnection.getInputStream();
                                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                                    byte[] b = new byte[4096];
                                    for (int n; (n = inputStream.read(b)) != -1;) {
                                        outStream.write(b, 0, n);
                                    }
                                    b=null;
                                    result = new String(outStream.toByteArray(),"utf-8");
                                    outStream.close();
                                        listener.onResult(new CoreFuncReturn(true, "获取成功", result));
                                    }else {
                                        listener.onResult(new CoreFuncReturn(false, "访问失败"));
                                    }
//                                }
//                                else if(textType==TYPE_4){
//                                    StringBuffer sbuffer=null;
//                                    httpURLConnection.setDoOutput(true);
//                                    httpURLConnection.setRequestMethod(Constant.METHOD_PUT);
//                                    httpURLConnection.setUseCaches(false);
//                                    httpURLConnection.setInstanceFollowRedirects(true);
//                                    httpURLConnection.setRequestProperty("Content-Type",  "application/json;charset=utf-8");//json格式上传的模式
//                                    httpURLConnection.setRequestProperty("X-Auth-Token", "token");
//
//                                    OutputStream out = httpURLConnection.getOutputStream();//向对象输出流写出数据，这些数据将存到内存缓冲区中
//                                    out.write(4096);            //out.write(new String("测试数据").getBytes());            //刷新对象输出流，将任何字节都写入潜在的流中
//                                    out.flush();
//                                    // 关闭流对象,此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中
//                                    out.close();
//
//                                    int respondCode = httpURLConnection.getResponseCode();
//                                    if (respondCode == HttpURLConnection.HTTP_OK) {
//
//                                        InputStreamReader   inputStream =new InputStreamReader(httpURLConnection.getInputStream());//调用HttpURLConnection连接对象的getInputStream()函数, 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
//                                        BufferedReader reader = new BufferedReader(inputStream);
//                                        String lines;
//                                        sbuffer= new StringBuffer("");
//                                        while ((lines = reader.readLine()) != null) {
//                                            lines = new String(lines.getBytes(), "utf-8");
//                                            sbuffer.append(lines);
//                                        }
//                                        reader.close();
//                                        result = sbuffer.toString();
//                                        listener.onResult(new CoreFuncReturn(true, "获取成功", result));
//                                    }else {
//                                        listener.onResult(new CoreFuncReturn(false, "访问失败"));
//                                    }



                                    } else{
                                    if(textType==TYPE_4){
                                        httpURLConnection.setRequestMethod(Constant.METHOD_PUT);
                                    }else{
                                        httpURLConnection.setRequestMethod(Constant.METHOD_POST);
                                    }

//                                    httpURLConnection.setRequestMethod(Constant.METHOD_POST);

                                    httpURLConnection.setDoOutput(true);
                                    OutputStream os = httpURLConnection.getOutputStream();
                                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
                                    bufferedWriter.write(HttpHelper.this.requestParam);
                                    bufferedWriter.flush();
                                    bufferedWriter.close();
                                    os.close();
                                    int respondCode = httpURLConnection.getResponseCode();
                                    if (respondCode == HttpURLConnection.HTTP_OK) {
                                        InputStream is = httpURLConnection.getInputStream();

                                        ByteArrayOutputStream bos = new ByteArrayOutputStream();

                                        byte[] buffer = new byte[8096];
                                        int len;
                                        int cursor = 0;

                                        while ((len = is.read(buffer, cursor, 8096 - cursor)) > 0) {

                                            bos.write(buffer, cursor, len);
                                            cursor += len;
                                            if (8096 - cursor <= 1) {
                                                cursor = 0;
                                            }
                                        }

                                        byte[] b = bos.toByteArray();

                                        result = CompressUtil.deCompress(bos.toByteArray());
//                           result = changeInputStream(is, "UTF-8", true);
                                        System.out.print("\n接口返回结果：");
                                        System.out.print("\n"+ JsonFormatter.format(result));
                                        System.out.print("\n###################################################");
                                        listener.onResult(new CoreFuncReturn(true, "获取成功", result));
//                                    bos.close();
                                        is.close();
                                    }else {
                                        listener.onResult(new CoreFuncReturn(false, "访问失败"));
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onResult(new CoreFuncReturn(false, "访问失败"));
                            }

                        }
                }
            }).start();


        } catch (Exception e) {

            e.printStackTrace();
            listener.onResult(new CoreFuncReturn(false, "访问失败"));
        }

    }


//    public static void doGetHttp(String urlString) {
//        BaseReturn baseReturn = new BaseReturn();
//        if (NetWorkTypeUtils.getAvailableNetWorkInfo(MyApplication.getInstance()) == null) {
//            baseReturn.setCode(HTTP_CODE_1010);
//            baseReturn.setMessage(HTTP_MSG_1010);
//            return baseReturn;
//        }
//
////		Log.e("---urlSB", urlString.toString());
//        InputStream inputStream = null;
//        try {
//            URLConnection con = getGetURLConnection(urlString.toString());
//            if (con == null) {
//                baseReturn.setCode(HTTP_CODE_1010);
//                baseReturn.setMessage(HTTP_MSG_1010);
//                return baseReturn;
//            } else if (((HttpURLConnection) con).getResponseCode() != 200) {
//                baseReturn.setCode(HTTP_CODE_1010);
//                baseReturn.setMessage("网络连接出错:" + ((HttpURLConnection) con).getResponseCode());
//                return baseReturn;
//            }
//
//            if (baseReturn.getCode() == BaseReturn.SUCCESS) {
//                inputStream = con.getInputStream();
//                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//                byte[] b = new byte[4096];
//                for (int n; (n = inputStream.read(b)) != -1; ) {
//                    outStream.write(b, 0, n);
//                }
//                b = null;
//                baseReturn.setOtherText(new String(outStream.toByteArray(), "GBK"));
//                outStream.close();
//            }
//
//        } catch (Exception e) {
//            baseReturn.setCode(HTTP_CODE_1000);
//            baseReturn.setMessage(e.getMessage());
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return baseReturn;
//    }

//    public CoreFuncReturn sendPost (String uri, Map<String, Object> paramMap, String paramString, String encode) {
//
//        try {
//
////            logMsg("调用方法"+uri);
//
//            // 使用HttpPost请求方式
//            HttpPost httpPost = new HttpPost(uri);
//
//            httpPost.setHeader("Content-type", "application/json");
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Accept-Encoding", "gzip,deflate");
////            httpPost.setHeader("Connection", "Keep-Alive");
//
//            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
//
//            if (paramMap != null && !paramMap.isEmpty()) {
//
//                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
//                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()+""));
////                    logMsg("参数是："+entry.getKey()+":"+entry.getValue());
//                }
//                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, encode);
//                httpPost.setEntity(entity);
//
//            }
//
//            if (paramString != null) {
//
//                paramString = new String(paramString.getBytes(encode), encode);
//
//
//
//                StringEntity entity = new StringEntity(paramString, encode);
//                entity.setContentType("application/x-www-form-urlencoded");
//                httpPost.setEntity(entity);
//            }
//            // 实例化一个默认的Http客户�?
//            final HttpParams httpParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
//            DefaultHttpClient client = new DefaultHttpClient();
//            client.setParams(httpParams);
//
//            //todo
////            logMsg(httpPost.getEntity().toString());
//
//            // 执行请求，并获得响应数据
//            HttpResponse httpResponse = client.execute(httpPost);
//
//            // 判断是否请求成功，为200时表示成功，其他均问有问题�?
//            if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                // 通过HttpEntity获得响应�?
//                InputStream inputStream = httpResponse.getEntity().getContent();
//                String result = changeInputStream(inputStream, encode, true);
//
//                return new CoreFuncReturn(true, "获取成功", result);
//            } else {
//                return new CoreFuncReturn(false, "访问失败");
//            }
//
//        } catch (Exception ex) {
//
//            logErr(ex);
//
//            if (ex instanceof UnknownHostException) {
//                return new CoreFuncReturn(false, "无法解析地址，请检查网络或稍后再试。", ex.getMessage());
//            }
//            else if (ex instanceof TimeoutException) {
//                return new CoreFuncReturn(false, "连接超时", ex.getMessage());
//            } else if (ex instanceof HttpHostConnectException) {
//                return new CoreFuncReturn(false, "服务器连接异常，请检查网络权限", ex.getMessage());
//            }
//            else {
//                return new CoreFuncReturn(false, "", ex.getMessage());
//            }
//        }
//
//    }

    /**
     * 把Web站点返回的响应流转换为字符串格式
     *
     * @param inputStream
     *            响应�?
     * @param encode
     *            编码格式
     * @return 转换后的字符�?
     */
    private String changeInputStream(InputStream inputStream, String encode, boolean deflate) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                if (!deflate) {
                    result = new String(outputStream.toByteArray(), encode);
                } else {
                    result = CompressUtil.deCompress(outputStream.toByteArray());
                }

            } catch (IOException e) {
                logErr(e);
            }
        }
        return result;
    }


    public void setRequestParam(Map<String, Object> requestParam) {
//        this.requestParam = JsonHelper.map2Json(requestParam);
        this.requestParam = AliJsonUtil.toJSONString(requestParam);
        Log.e("-----111",this.requestParam.toString());
    }

    public interface onResultListener {

        void onResult(CoreFuncReturn result);
    }

    public void setOnResultListener(onResultListener listener) {
        this.listener = listener;
    }


    /**
     * 下载工具
     */

    public interface onDownloadStatueListener {
        public void onDownloading(int total, int current);

        public void onDownloadFinished(String path);

        public void onDownloadError(String error);
    }

    public void simpleDownload(String url, String localPath, String fileName,boolean isCoverOld,
                               onDownloadStatueListener listener) {
        simpleDownload(url, localPath, fileName, isCoverOld, true, 3, listener);
    }

    public void simpleDownload(String url, String localPath, String fileName, boolean isCoverOld,
                               int reTryTTL, onDownloadStatueListener listener) {
        simpleDownload(url, localPath, fileName, isCoverOld, true, reTryTTL, listener);
    }

    public void simpleDownload(final String url, final String localPath, final String fileName,
                               boolean isCoverOld, boolean isUsingThread, final int reTryTTL,
                               final onDownloadStatueListener listener) {

        if (!isCoverOld) {

            File file = new File(localPath + fileName);
            if (file != null) {

                if (file.exists()) {
                    listener.onDownloadFinished(localPath + fileName);
                    return;
                }

            } else {
                listener.onDownloadError("文件路径错误");
            }

        }

        if (reTryTTL < 0)
            return;
        if (isUsingThread) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    beginDownLoad(url, localPath, fileName, reTryTTL, listener);
                }
            }).start();

        } else {
            beginDownLoad(url, localPath, fileName, reTryTTL, listener);
        }
    }

    private void beginDownLoad(String url, String localPath, String fileName, int TTL,
                               onDownloadStatueListener listener) {


        try {
            URL url1 = new URL(url);

            URLConnection connection = url1.openConnection();

//                    connection.setRequestProperty("Range", "bytes=2000000-");

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
//                        httpURLConnection.setRequestMethod("POST");
//
//                        httpURLConnection.setDoInput(true);
//                        httpURLConnection.setDoOutput(true);
//                        httpURLConnection.setRequestProperty("connection", "Keep-Alive");
//                        httpURLConnection.setRequestProperty("Charsert", "UTF-8");
//                        httpURLConnection.setInstanceFollowRedirects(true);
//                        httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//                        httpURLConnection.connect();
                httpURLConnection.setInstanceFollowRedirects(true);

            }


            InputStream is = connection.getInputStream();

            File dir = new File(localPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(localPath + fileName + ".temp");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);

            int total = connection.getContentLength();
            int read;
            int sum = 0;
            byte[] buff = new byte[1024];

            long prevMillis = System.currentTimeMillis();

            while ((read = is.read(buff)) != -1) {
                fos.write(buff, 0, read);
                sum += read;

                long currentMillis = System.currentTimeMillis();
                if (currentMillis - prevMillis >= 500) {
                    prevMillis = currentMillis;
                    //反馈下载进度
                    if (listener != null) {
                        listener.onDownloading(total, sum);
                    }
                }

            }

            fos.close();

            //重命名文件
            File dst = new File(localPath + fileName);
            if (dst.exists()) {
                dst.delete();
            }
            if (file.renameTo(dst)) {

                if (listener != null) {
                    listener.onDownloading(total, total);
                }

                if (listener != null) {

                    listener.onDownloadFinished(localPath + fileName);
                }
            }

        } catch (Exception ex) {
            logErr(ex);
            int currentTTL = TTL - 1;
            if (currentTTL > 0) {
                beginDownLoad(url, localPath, fileName, currentTTL, listener);
            } else {
                if (listener != null) {
                    listener.onDownloadError(ex.getMessage());
                }
            }


        }

    }


    public static String getHttpURLResult(String strUrl) {

        String result = "";
        try {

            URL url = new URL(strUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty(Constant.PROPERTY_CONTENT_TYPE, Constant.CONTENT_TYPE.APPLICATION_JSON);
            httpURLConnection.setRequestProperty(Constant.PROPERTY_ACCEPT_ENCODING, "gzip,deflate");
            httpURLConnection.setRequestProperty(Constant.PROPERTY_CONNECTION, "Keep-Alive");
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setRequestMethod(Constant.METHOD_GET);
            int respondCode = httpURLConnection.getResponseCode();
            if (respondCode == HttpURLConnection.HTTP_OK) {

                InputStream inputStream = httpURLConnection.getInputStream();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                for (int n; (n = inputStream.read(b)) != -1;) {
                    outStream.write(b, 0, n);
                }
                b=null;
                result = new String(outStream.toByteArray(),"utf-8");
                outStream.close();

                //                       listener.onResult(new CoreFuncReturn(true, "获取成功", result));
            }else {
                //                       listener.onResult(new CoreFuncReturn(false, "访问失败"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

}
