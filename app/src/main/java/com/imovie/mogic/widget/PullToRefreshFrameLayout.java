package com.imovie.mogic.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.imovie.mogic.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhou on 2015/10/16.
 *
 * 带下拉刷新的FrameLayout
 */
public class PullToRefreshFrameLayout extends FrameLayout {

    protected static final int STATE_IDLE         = 0x00;   //状态----空闲
    protected static final int STATE_REFRESHING   = 0x01;   //状态----刷新中

    //上下文
    protected Context mContext;

    //头部
    View mHeadView;

    //头部箭头
    View mHeadArrow;

    //头部进度
    View mHeadProgressBar;

    //头部文字
    TextView mHeadTextState;
    TextView mHeadTextLastRefreshTime;

    //头部高度
    int mHeaderHeight = 60;

    //向上动画
    RotateAnimation mRotateUpAnim;

    //向下动画
    RotateAnimation mRotateDownAnim;

    //是否初始化了
    Boolean bInit = false;

    //是否可拉伸
    boolean pullEnable = true;

    //最小滑动距离
    int minTouchDistance = 0;

    //手指落点Y值
    float downY = 0;

    //状态
    int state = STATE_IDLE;

    //头部可见高度
    int mVisibleHeaderHeight = 0;

    //日期格式化工具
    SimpleDateFormat dateFormat;

    //上次更新时间
    long lastRefreshTime = 0;

    //更新监听器
    OnPullToRefreshListener listener;

    public interface OnPullToRefreshListener {
        void onRefresh();
    }

    public PullToRefreshFrameLayout(Context context) {
        super(context);
        mContext = context;
        initUI();
    }

    public PullToRefreshFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initUI();
    }

    public PullToRefreshFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initUI();
    }

    private void initUI() {
        if (bInit)
            return;
        bInit = true;
        initHeader();
        minTouchDistance = ViewConfiguration.get(mContext).getScaledTouchSlop();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    private void initHeader() {

        initAnimation();
        mHeaderHeight = dip2px(mHeaderHeight);
        mHeadView = LayoutInflater.from(mContext).inflate(R.layout.pulltorefresh_header, null);
        //将头部隐藏在上方
        LayoutParams headLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headLayoutParams.setMargins(0, -mHeaderHeight, 0, 0);
        mHeadView.setLayoutParams(headLayoutParams);

        addView(mHeadView);

        mHeadArrow = mHeadView.findViewById(R.id.pullToRefresh_header_iv_arrow);
        mHeadProgressBar = mHeadView.findViewById(R.id.pullToRefresh_header_pb);
        mHeadTextState = (TextView) mHeadView.findViewById(R.id.pullToRefresh_header_tv_hint);
        mHeadTextLastRefreshTime = (TextView) mHeadView.findViewById(R.id.pullToRefresh_header_tv_lastRefreshTime);

    }

    private void initAnimation() {
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(180);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(180);
        mRotateDownAnim.setFillAfter(true);
    }

    /**
     * 反转头部箭头
     * @param bInversion    true--已反转 false--未反转
     */
    private void inversionHeaderArrow(boolean bInversion) {

        if (mHeadArrow.getTag() != null) {
            if((Boolean)mHeadArrow.getTag() == bInversion)
                return;
        }
        mHeadArrow.setTag(bInversion);
        if (bInversion) {
            mHeadArrow.startAnimation(mRotateUpAnim);

        } else {
            mHeadArrow.startAnimation(mRotateDownAnim);
        }
    }

    /**
     *改变头部文字
     * @param state             状态
     * @param lastRefreshTime   上次更新时间，格式：YYYY-MM-DD HH:mm
     */
    private void changeHeaderText(CharSequence state, CharSequence lastRefreshTime) {

        if (state != null)
            mHeadTextState.setText(state);
        if(lastRefreshTime != null)
            mHeadTextLastRefreshTime.setText(lastRefreshTime);
    }

    /**
     * 显示正在刷新进度条
     * @param bShow     true--显示 false--不显示
     */
    private void showHeaderProgressBar(boolean bShow) {

        if (bShow) {
            mHeadArrow.clearAnimation();
            mHeadArrow.setVisibility(INVISIBLE);
            mHeadProgressBar.setVisibility(VISIBLE);
        } else {
            mHeadProgressBar.setVisibility(INVISIBLE);
            mHeadArrow.setVisibility(VISIBLE);
        }

    }

    /**
     * 开始执行刷新
     */
    public void beginRefresh() {

        //更改状态为刷新
        state = STATE_REFRESHING;
        //滚动到头部可视位置
        scrollTo(0, -mHeaderHeight);
        //更改头部文字
        changeHeaderText("正在刷新", null);
        //显示头部进度
        showHeaderProgressBar(true);
        //开始执行刷新
        if (listener != null) {
            listener.onRefresh();
        }
    }

    /**
     * 结束刷新
     * @param isSuccess 成功标识
     */
    public void endRefresh(boolean isSuccess) {

        //仅当正在刷新的时候才可以结束刷新
        if (state == STATE_IDLE)
            return;

        if (isSuccess) {
            lastRefreshTime = System.currentTimeMillis();
            //更改头部文字
            changeHeaderText("刷新成功", "刚刚");
        } else {
            changeHeaderText("刷新失败", null);
        }
        //将下拉箭头复位
        inversionHeaderArrow(false);
        //隐藏头部进度
        showHeaderProgressBar(false);
        //0.5秒以后隐藏头部
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //复位
                PullToRefreshFrameLayout.this.scrollTo(0, 0);
                //更改头部文字
                changeHeaderText("下拉刷新", null);
                //更改标识
                state = STATE_IDLE;
            }
        }, 500);
    }

    /**
     * 设置是否可以下拉刷新
     * @param pullEnable    true--可以  false--不可
     */
    public void setPullEnable(boolean pullEnable) {
        this.pullEnable = pullEnable;
    }

    /**
     * 获取是否可以下拉刷新
     * @return      true--可以  false--不可
     */
    public boolean isPullEnable() {
        return pullEnable;
    }

    /**
     * 设置监听器
     * @param listener  刷新监听器
     */
    public void setOnPullToRefreshListener(OnPullToRefreshListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (pullEnable && state == STATE_IDLE) {

            switch (ev.getAction()) {

                case MotionEvent.ACTION_DOWN: {
                    downY = ev.getY();
                    break;
                }

                case MotionEvent.ACTION_MOVE: {

                    //滑动必须大于最小距离
                    float y = ev.getY();
                    float distance = Math.abs(y - downY);
                    if (distance < minTouchDistance)
                        return false;

                    //必须向下划才拦截
                    if(y > downY) {
                        //更改头部上次刷新时间文字
                        String timeText;
                        long current = System.currentTimeMillis();
                        if (lastRefreshTime == 0)
                            timeText = "";
                        else {
                            timeText = getTimeGapString(current, lastRefreshTime);
                        }
                        changeHeaderText(null, timeText);

                        return true;
                    }
                }
            }
        }

        return false;//不拦截
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (pullEnable && state == STATE_IDLE) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN: {

                    downY = event.getY();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {

                    float distance = event.getY() - downY;

                    //向下划动才有效
                    if (distance > 0) {

                        if (mVisibleHeaderHeight < mHeaderHeight) {
                            mVisibleHeaderHeight = (int) (distance * 0.8f);
                            //指针箭头向下
                            inversionHeaderArrow(false);
                            //更改文字
                            changeHeaderText("下拉刷新", null);

                        } else {
                            mVisibleHeaderHeight = (int) (mHeaderHeight + (distance - mHeaderHeight / 0.8f) * 0.1f);
                            //指针箭头向上
                            inversionHeaderArrow(true);
                            //更改文字
                            changeHeaderText("松手刷新", null);

                        }
                    } else {
                        mVisibleHeaderHeight = 0;
                    }

                    scrollTo(0, -mVisibleHeaderHeight);
                    break;
                }
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP: {

                    if (mVisibleHeaderHeight >= mHeaderHeight) {

                        //开始执行刷新
                        beginRefresh();
                    } else {
                        scrollTo(0, 0);
                    }
                }
            }
            return true;
        }
        return super.onTouchEvent(event);

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据时间获取间隔文字(单位：毫秒(ms))
     * @param t1    时间1
     * @param t2    时间2
     * @return      间隔文字
     */
    private String getTimeGapString(long t1, long t2) {

        String result = "";

        long gap = Math.abs(t1 - t2);
        gap = gap / 1000;

        if (gap < 60) {
            result = "刚刚";
            return result;
        }
        gap = gap / 60;
        if (gap < 60) {
            result = gap + "分钟前";
            return result;
        }
        gap = gap / 60;
        if (gap < 24) {
            result = gap + "小时前";
            return result;
        }
        gap = gap / 24;
        if (gap < 3) {
            result = gap + "天前";
            return result;
        }

        result = dateFormat.format(new Date(Math.min(t1, t2)));
        return result;
    }
}
