package com.imovie.mogic.utills;

/**
 * 药师帮定制异常类。
 * 用于主动抛出异常，
 * Created by zhou on 2015/4/24.
 */
public class YSBException extends RuntimeException {
    public YSBException() {
        super();
    }

    public YSBException(String detailMessage) {
        super(detailMessage);
    }

    public YSBException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public YSBException(Throwable throwable) {
        super(throwable);
    }

}
