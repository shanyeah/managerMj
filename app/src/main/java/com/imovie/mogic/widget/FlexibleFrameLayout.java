package com.imovie.mogic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by zhou on 2015/9/17.
 */
public class FlexibleFrameLayout extends FrameLayout {

    /**
     * 若同时设置了flexHeight和flexView，将以flexHeight为主
     */

    private boolean bInit = false;
    private Context mContext;
    private final int MIN_SCROLL_DISTANCE = 10;
    private boolean isFlexible = false;         //是否可以伸缩
    private int flexHeight = 0;                 //可伸缩的高度
    private View flexView;                      //需要设置伸缩的View(必须是它的孩子，否则可能会出现异常)
    private ViewGroup.LayoutParams mParams;
    private int mHeight;

    private int currentMove = 0;                //当前已伸缩高度
    private float downY = 0;                    //手指按下的触摸点的y值

    public interface OnFlexChangeListener {
        void onFlexChange(int flexHeight, int currentFlexHeight, boolean isOnTop);
    }
    private OnFlexChangeListener onFlexChangeListener;

    public FlexibleFrameLayout(Context context) {
        super(context);
        mContext = context;
    }

    public FlexibleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public FlexibleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isFlexible) {

            if (mParams == null) {
                mParams = getLayoutParams();
            }

            if (mHeight == 0) {
                mHeight = getHeight();
            }

            if (flexHeight == 0 || flexView != null) {
                int currentHeight = flexView!=null?flexView.getHeight():-1;
                if (currentHeight > 0 && flexHeight != currentHeight) {
                    flexHeight = currentHeight;
                    setFlexHeight(flexHeight, false);
                }
            }


            switch (ev.getAction()) {

                case MotionEvent.ACTION_DOWN: {
                    downY = ev.getY();
                    return false;
                }
                case MotionEvent.ACTION_MOVE: {

                    float currentY = ev.getY();

                    float distance = currentY - downY;

                    if (Math.abs(distance) < MIN_SCROLL_DISTANCE)
                        return false;

                    if (distance > 0) {
                        //向下划
                        if (currentMove == 0) {
                            downY = ev.getY();
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        //向上划
                        if (currentMove == flexHeight) {
                            downY = ev.getY();
                            return false;
                        } else {
                            return true;
                        }
                    }

                }
            }
        }
        return false;//不拦截触摸事件，将会传递到子控件中继续处理
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isFlexible) {
            try {

                if (flexHeight != 0) {

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN: {
                            downY = event.getY();
                            break;
                        }

                        case MotionEvent.ACTION_MOVE: {

                            float currentY = event.getY();

                            float distance = currentY - downY;
                            downY = event.getY();

                            if (distance > 0) {
                                //向下划
                                if (currentMove == 0) {
                                    return true;
                                }
                                currentMove -= (int)distance;
                                if (currentMove < 0) {
                                    currentMove = 0;
                                }
                                scrollTo(0,currentMove);

                            } else {
                                //向上划
                                if (currentMove == flexHeight) {
                                    return true;
                                }
                                currentMove -= (int)distance;
                                if (currentMove > flexHeight) {
                                    currentMove = flexHeight;
                                }
                                scrollTo(0,currentMove);
                            }
                            if (onFlexChangeListener != null) {
                                if(currentMove == 0){
                                    onFlexChangeListener.onFlexChange(flexHeight, currentMove, true);
                                }else {
                                    onFlexChangeListener.onFlexChange(flexHeight, currentMove, false);
                                }

                            }

                            break;
                        }
                    }
                }

            } catch (Exception e) {
                System.out.print("####FlexibleFrameLayout####\nerror-->" + e.getMessage());
                isFlexible = false;
            }
        }

        return true;
    }

    public void setFlexible(boolean isFlexible) {
        this.isFlexible = isFlexible;

        if (isFlexible) {
            if (mParams != null && mHeight != 0) {
                mParams.height = mHeight + flexHeight;
                setLayoutParams(mParams);
            }
        } else {
            if (mParams != null && mHeight != 0) {
                mParams.height = mHeight + currentMove;
                setLayoutParams(mParams);
            }
        }
    }

    public boolean getIsFlexible() {
        return this.isFlexible;
    }

    public void setFlexHeight(int flexHeight, boolean isDip) {
        if (isDip) {
            this.flexHeight = dip2px(flexHeight);
        } else {
            this.flexHeight = flexHeight;
        }
        if (isFlexible && mParams != null && mHeight != 0) {
            mParams.height = mHeight + flexHeight;
            setLayoutParams(mParams);
        }
    }

    public int getFlexHeight() {
        return flexHeight;
    }

    public int getCurrentVisibleFlexHeight() {
        return flexHeight - currentMove;
    }

    public void setFlexView(View flexView) {
        flexHeight = 0;
        this.flexView = flexView;
        if (this.flexView != null)
            flexHeight = this.flexView.getHeight();
    }

    public View getFlexView() {
        return this.flexView;
    }

    public void hideFlexHeight() {

        try {

            if (flexHeight == 0 && flexView == null)
                return;

            if (flexHeight == 0 && flexView.getVisibility() != GONE) {
                flexHeight = flexView.getHeight();
            }
            FlexibleFrameLayout.this.scrollTo(0, flexHeight);
            currentMove = flexHeight;

            if (onFlexChangeListener != null) {
                onFlexChangeListener.onFlexChange(flexHeight, currentMove, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showFlexHeight() {

        try {
            if (flexHeight == 0 && flexView == null)
                return;

            if (flexHeight == 0 && flexView.getVisibility() != GONE) {
                flexHeight = flexView.getHeight();
            }

            FlexibleFrameLayout.this.scrollTo(0, 0);
            currentMove = 0;

            if (onFlexChangeListener != null) {
                onFlexChangeListener.onFlexChange(flexHeight, currentMove, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnFlexChangeListener(OnFlexChangeListener listener) {
        this.onFlexChangeListener = listener;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
