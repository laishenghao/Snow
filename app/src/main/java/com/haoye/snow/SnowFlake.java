package com.haoye.snow;



/**********************************************************
 *  Author: laishenghao                                   *
 *  Date: 2016/4/15.                                      *
 *  Blog: http://www.cnblogs.com/laishenghao/             *
 *  Copyright © 2016 All Rights Reserved.                 *
 **********************************************************/
public class SnowFlake {
    private int mWidth;
    private int mHeight;
    private int mX;
    private int mY;
    private int mSpeedX;
    private int mSpeedY;

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getSpeedX() {
        return mSpeedX;
    }

    public void setSpeedX(int speedX) {
        this.mSpeedX = mSpeedX;
    }

    public int getSpeedY() {
        return mSpeedY;
    }

    public void setSpeedY(int speedY) {
        this.mSpeedY = speedY;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getX() {
        return mX;
    }

    public void setX(int x) {
        this.mX = x;
    }

    public int getY() {
        return mY;
    }

    public void setY(int y) {
        this.mY = y;
    }
}
