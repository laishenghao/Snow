package com.haoye.snow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Random;

/**********************************************************
 *  Author: laishenghao                                   *
 *  Date: 2016/4/15.                                      *
 *  Blog: http://www.cnblogs.com/laishenghao/             *
 *  Copyright © 2016 All Rights Reserved.                 *
 **********************************************************/
public class Snow extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private SnowFlake[]   mFlakes;
    private int           mViewWidth  = 200;
    private int           mViewHeight = 100;
    private int           mFlakeCount = 20;
    private int           mMinSize    = 50;
    private int           mMaxSize    = 70;
    private int           mSpeedX     = 10;
    private int           mSpeedY     = 20;
    private Bitmap        mSnowBitmap = null;
    private boolean       mStart      = false;

    public Snow(Context context) {
        this(context, null);
    }

    public Snow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Snow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHolder();
        setZOrderOnTop(true);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Snow, defStyleAttr, 0);
        int cnt = array.getIndexCount();
        for (int i = 0; i < cnt; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
            case R.styleable.Snow_flakeCount:
                mFlakeCount = array.getInteger(attr, 0);
                break;
            case R.styleable.Snow_minSize:
                mMinSize = array.getInteger(attr, 50);
                break;
            case R.styleable.Snow_maxSize:
                mMaxSize = array.getInteger(attr, 70);
                break;
            case R.styleable.Snow_flakeSrc:
                Integer srcId = array.getResourceId(attr, R.drawable.snow_flake);
                mSnowBitmap   = BitmapFactory.decodeResource(getResources(), srcId);
                break;
            case R.styleable.Snow_speedX:
                mSpeedX = array.getInteger(attr, 10);
                break;
            case R.styleable.Snow_speedY:
                mSpeedY = array.getInteger(attr, 10);
                break;
            default:
                break;
            }
        }
        if (mMinSize > mMaxSize) {
            mMaxSize = mMinSize;
        }
        array.recycle();
    }

    private void initHolder() {
        mHolder = this.getHolder();
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        mHolder.addCallback(this);
    }

    private void initSnowFlakes() {
        mFlakes = new SnowFlake[mFlakeCount];
        boolean isRightDir = new Random().nextBoolean();
        for (int i = 0; i < mFlakes.length; i++) {
            mFlakes[i] = new SnowFlake();
            mFlakes[i].setWidth(new Random().nextInt(mMaxSize-mMinSize) + mMinSize);
            mFlakes[i].setHeight(mFlakes[i].getWidth());
            mFlakes[i].setX(new Random().nextInt(mViewWidth));
            mFlakes[i].setY(-(new Random().nextInt(mViewHeight)));
            mFlakes[i].setSpeedY(new Random().nextInt(4) + mSpeedY);
            if (isRightDir) {
                mFlakes[i].setSpeedX(new Random().nextInt(4) + mSpeedX);
            }
            else {
                mFlakes[i].setSpeedX(-(new Random().nextInt(4) + mSpeedX));
            }
        }
    }

    private void updatePara() {
        int x;
        int y;
        for (SnowFlake flake : mFlakes) {
            if (flake == null) {
                break;
            }
            x = flake.getX() + flake.getSpeedX();
            y = flake.getY() + flake.getSpeedY();
            if ((x > mViewWidth + 20 || x < 0)
                    || (y > mViewHeight + 20)) {
                x = new Random().nextInt(mViewWidth);
                y = 0;
            }
            flake.setX(x);
            flake.setY(y);
        }
    }

    private void drawView() {
        if (mHolder == null) {
            return;
        }
        Canvas canvas = mHolder.lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        drawSnow(canvas);
        mHolder.unlockCanvasAndPost(canvas);
    }

    private void drawSnow(Canvas canvas) {
        Rect  rect  = new Rect();
        Paint paint = new Paint();
        for (SnowFlake flake : mFlakes) {
            rect.left   = flake.getX();
            rect.top    = flake.getY();
            rect.right  = rect.left + flake.getWidth();
            rect.bottom = rect.top  + flake.getHeight();
            canvas.drawBitmap(mSnowBitmap, null, rect, paint);
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mStart = (visibility == VISIBLE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initSnowFlakes();
        start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //--- measure the view's width
        int widthMode  = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            mViewWidth = (getPaddingStart() + mSnowBitmap.getWidth() + getPaddingEnd());
        }

        //--- measure the view's height
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            mViewHeight = (getPaddingTop() + mSnowBitmap.getHeight() + getPaddingBottom());
        }

        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    public void start() {
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        if (mStart) {
                            updatePara();
                            drawView();
                        }
                        Thread.sleep(20);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
