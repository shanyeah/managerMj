package com.imovie.mogic.web.common;

import java.io.IOException;

/**
 * Created by zhouxinshan on 2016/4/12.
 * <p/>
 * 解码格式异常
 */
public class CEFormatException extends IOException {
    public CEFormatException(String message) {
        super(message);
    }
}
