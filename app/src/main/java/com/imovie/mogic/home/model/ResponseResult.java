package com.imovie.mogic.home.model;

public class ResponseResult {

    public ResponseResult() {
    }

    public ResponseResult(int code) {
        this.code = code;
    }

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public final static int SC_OK = 0;//查询成功
    public final static int SC_ERROR = 1000;//非法请求。HTTP头中的参数不完整
    public final static int SIGN_ERROR = 1100;//response签名有错
    private int code = SC_ERROR;
    private String msg;
    private String text;
    private String time;//时间戳秒
    private String md5Key;
    private Object obj;


    public boolean issuccess() {
        if (this.getCode() == 0) {
            return true;
        }
        return false;
    }

    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        if (this.code != 0 && msg == null) {
            return "系统错误[" + code + "]";
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
