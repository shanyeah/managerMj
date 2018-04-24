package com.imovie.mogic.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.imovie.mogic.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class RoundedImageView extends android.support.v7.widget.AppCompatImageView{

    public final static int DRAW_CIRCLE =       0X00;
    public final static int DRAW_ROUND_RECT =   0x01;
    public final static int DRAW_CHAT_LEFT =   0x02;
    public final static int DRAW_CHAT_RIGHT =   0x03;
    public final static int DRAW_NONE =         0x04;

    private int mBorderThickness = 0;
    private Context mContext;
    private int mBorderColor = 0xFFFFFFFF;
    private int mDrawType = DRAW_CIRCLE;
    private float cornerX;
    private float cornerY;

    private AtomicBoolean isTouch = new AtomicBoolean(false);

    private Bitmap  originBitmap;
    private Bitmap  drawingBitmap;
    private boolean isNeedMulti;

    public RoundedImageView(Context context) {
        super(context);
        mContext = context;
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setCustomAttributes(attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setCustomAttributes(attrs);
    }

    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs,
                R.styleable.RoundedImageView);
        mBorderThickness = a.getDimensionPixelSize(R.styleable.RoundedImageView_border_thickness, 0);
        mBorderColor = a.getColor(R.styleable.RoundedImageView_border_color, mBorderColor);
        mDrawType = a.getInt(R.styleable.RoundedImageView_draw_type, DRAW_CIRCLE);
        cornerX = a.getFloat(R.styleable.RoundedImageView_corner_x, 15);
        cornerY = a.getFloat(R.styleable.RoundedImageView_corner_y, 15);
        isNeedMulti = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mDrawType == DRAW_NONE) {
            super.onDraw(canvas);
            return;
        }

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.measure(0, 0);
        if(drawable.getClass() == NinePatchDrawable.class)
            return;

        int w = getWidth(), h = getHeight();

        if (originBitmap == null) {
            Bitmap b = convertDrawable2BitmapByCanvas(drawable);
            originBitmap = createScaledBitmap(b, w, h, ScalingLogic.CROP);
        }

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(mBorderColor);
        paint.setStyle(Paint.Style.FILL);

        if (mDrawType == DRAW_CIRCLE) {
            int radius = (w < h ? w : h) / 2 - mBorderThickness;

            if (drawingBitmap == null) {
                drawingBitmap = getCroppedBitmap(originBitmap, radius);
            }

            canvas.drawCircle(w / 2,
                    h / 2, radius + mBorderThickness, paint);
            canvas.drawBitmap(drawingBitmap, w / 2 - radius, h / 2 - radius, null);

            if (isTouch.get()) {
                multi(canvas);
            }

        } else if (mDrawType == DRAW_ROUND_RECT) {

            if (drawingBitmap == null) {
                drawingBitmap = getRoundRectBitmap(originBitmap);
            }

            canvas.drawRoundRect(new RectF(0, 0, w, h), cornerX, cornerY, paint);
            canvas.drawBitmap(drawingBitmap, mBorderThickness, mBorderThickness, paint);

            if (isTouch.get()) {
                multi(canvas);
            }

        } else if (mDrawType == DRAW_CHAT_LEFT){

            if (drawingBitmap == null) {
                drawingBitmap = getChatBitmap(originBitmap, DRAW_CHAT_LEFT);
            }

            canvas.drawBitmap(drawingBitmap, 0, 0, null);

            if (isTouch.get()) {
                multi(canvas);
            }

        } else if (mDrawType == DRAW_CHAT_RIGHT) {

            if (drawingBitmap == null) {
                drawingBitmap = getChatBitmap(originBitmap, DRAW_CHAT_RIGHT);
            }

            canvas.drawBitmap(drawingBitmap, 0, 0, null);

            if (isTouch.get()) {
                multi(canvas);
            }

        } else {

            canvas.drawBitmap(originBitmap, mBorderThickness, mBorderThickness, paint);

            if (isTouch.get()) {
                multi(canvas);
            }
        }
    }

    private Bitmap convertDrawable2BitmapByCanvas(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ?
                        Config.ARGB_8888: Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;
        if (bmp.getWidth() != diameter || bmp.getHeight() != diameter)
            scaledSrcBmp = Bitmap.createScaledBitmap(bmp, diameter, diameter, false);
        else
            scaledSrcBmp = bmp;
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);

        return output;
    }

    private Bitmap getRoundRectBitmap(Bitmap bmp) {

        Bitmap output = Bitmap.createBitmap(bmp.getWidth() - 2*mBorderThickness, bmp.getHeight()- 2*mBorderThickness,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bmp.getWidth() - 2*mBorderThickness, bmp.getHeight()- 2*mBorderThickness);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawRoundRect(rectF, cornerX, cornerY,  paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bmp, rect, rect, paint);

        return output;
    }

    private Bitmap getChatBitmap(Bitmap bmp, int drawType) {

        Bitmap output = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();


        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(Color.parseColor("#BAB399"));
//        Shader s = new BitmapShader(bmp, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
//        paint.setShader(s);

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        int border = 0;
        if (mBorderThickness*2 < getWidth() && mBorderThickness*2 < getHeight()) {
            border = mBorderThickness;
        }

        if (drawType == DRAW_CHAT_LEFT) {
            final Rect rect = new Rect(20+border, border, bmp.getWidth()-border, bmp.getHeight()-border);
            RectF rectF = new RectF(rect);
            canvas.drawRoundRect(rectF, cornerX, cornerY, paint);
            canvas.drawVertices(Canvas.VertexMode.TRIANGLES, 6, new float[]{border, 50+border/2f, 20+border, 35+border/2f, 20+border, 65+border/2f}, 0, null, 0, new int[]{Color.RED, Color.RED, Color.RED, 0xff000000, 0xff000000, 0xff000000}, 0, null, 0, 0, paint);
        } else {
            final Rect rect = new Rect(border, border, bmp.getWidth() - 20 - border, bmp.getHeight() - border);
            RectF rectF = new RectF(rect);
            canvas.drawRoundRect(rectF, cornerX, cornerY, paint);
            canvas.drawVertices(Canvas.VertexMode.TRIANGLES, 6, new float[]{bmp.getWidth()-border, 50+border/2f, bmp.getWidth()-20-border, 35+border/2f, bmp.getWidth()-20-border, 65+border/2f}, 0, null, 0, new int[]{Color.RED, Color.RED, Color.RED, 0xff000000, 0xff000000, 0xff000000}, 0, null, 0, 0, paint);
        }
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bmp, 0, 0, paint);

        paint.setColor(mBorderColor);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
        if (drawType == DRAW_CHAT_LEFT) {
            final Rect rect = new Rect(20, 0, bmp.getWidth(), bmp.getHeight());
            RectF rectF = new RectF(rect);
            canvas.drawRoundRect(rectF, cornerX, cornerY, paint);
            canvas.drawVertices(Canvas.VertexMode.TRIANGLES, 6, new float[]{0, 50+border/2f, 20, 35, 20, 65+border}, 0, null, 0, new int[]{mBorderColor, mBorderColor, mBorderColor, 0xff000000, 0xff000000, 0xff000000}, 0, null, 0, 0, paint);
        } else {
            final Rect rect = new Rect(0, 0, bmp.getWidth() - 20, bmp.getHeight());
            RectF rectF = new RectF(rect);
            canvas.drawRoundRect(rectF, cornerX, cornerY, paint);
            canvas.drawVertices(Canvas.VertexMode.TRIANGLES, 6, new float[]{bmp.getWidth(), 50+border/2f, bmp.getWidth()-20, 35, bmp.getWidth()-20, 65+border}, 0, null, 0, new int[]{mBorderColor, mBorderColor, mBorderColor, 0xff000000, 0xff000000, 0xff000000}, 0, null, 0, 0, paint);
        }

        return output;
    }

    private static enum ScalingLogic {
        CROP,
        FIT
    }

    private Bitmap createPureColorBitmap(int color) {

        Bitmap bitmap = Bitmap.createBitmap(16, 16, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawColor(color);

        return bitmap;
    }

    private Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));return scaledBitmap;
    }
    private Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int)(srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int)(srcWidth / dstAspect);
                final int scrRectTop = (int)(srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }
    private Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            isTouch.set(true);
            postInvalidate();
        }

        if(action == MotionEvent.ACTION_OUTSIDE || action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_POINTER_UP) {
            isTouch.set(false);
            postInvalidate();
        }
        return  super.onTouchEvent(event);
    }

    private void multi(Canvas canvas) {

        if (!isNeedMulti)
            return;
        canvas.drawColor(0xeeeeeecc, Mode.MULTIPLY);
//        Paint paint = new Paint();
//        paint.setColor(0xeeeeeecc);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
//        canvas.drawPaint(paint);
//        canvas.drawARGB(200, 200, 200, 0);
    }

    public int getDrawType() {
        return mDrawType;
    }

    public void setDrawType(int type) {
        drawingBitmap = null;
        mDrawType = type;
        postInvalidate();
    }

    public void setPureColorSrc(int color) {

        Bitmap bitmap = createPureColorBitmap(color);
        setImageBitmap(bitmap);
        postInvalidate();
    }

    public void setBorderThickness(int value, boolean dp) {

        if (!dp) {
            mBorderThickness = value;
        } else {
            float scale = mContext.getResources().getDisplayMetrics().density;
            mBorderThickness = (int) (value * scale + 0.5f);
        }
        postInvalidate();
    }

    public void setBorderColor(int color) {

        this.mBorderColor = color;
        postInvalidate();
    }

    public void isNeedMulti(boolean b) {
        isNeedMulti = b;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {

        if (originBitmap != null) {
            originBitmap.recycle();
            originBitmap = null;
        }
        if (drawingBitmap != null) {
            drawingBitmap.recycle();
            drawingBitmap = null;
        }
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {

        if (originBitmap != null) {
            originBitmap.recycle();
            originBitmap = null;
        }
        if (drawingBitmap != null) {
            drawingBitmap.recycle();
            drawingBitmap = null;
        }
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageResource(int resId) {

        if (originBitmap != null) {
            originBitmap.recycle();
            originBitmap = null;
        }
        if (drawingBitmap != null) {
            drawingBitmap.recycle();
            drawingBitmap = null;
        }
        super.setImageResource(resId);
    }
}
