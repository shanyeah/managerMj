package com.imovie.mogic.utills;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by zhou on 2016/8/10.
 */
public class WeakHandler extends Handler {
    private WeakReference<Object> hostRef;

    public WeakHandler(Object host) {
        this.hostRef = new WeakReference<>(host);
    }

    @Override
    public void dispatchMessage(Message msg) {
        // dispatchMessage  是 handler 所有发出的消息的最终归宿，
        // 无论是 post runnable 或者是 message 都会走到这里
        //所以需要在这里判断 handler 依附的对象是否被回收，如果回收了就不会继续执行 因此并不是执行完了再判断
        //TODO
        if (hostRef == null || hostRef.get() == null) {
            return;
        }
        super.dispatchMessage(msg);
    }
}
