package com.nine.surpermario;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/9/27 0027.
 */

class Sprite {
    private ArrayList<Bitmap> bitmaps;//适用一组图片构建Sprite
    private Bitmap bitmap;//适用一张图片构建Sprite
    private int width;
    private int height;
    private int x;
    private int y;
    private int speedX;
    private int speedY;
    private boolean isVisible;
    private int frameNumber;
    private int[] frameX;
    private int[] frameY;
    private Rect src;
    private Rect dst;
    private int frameSequenceIndex;//当前帧的索引（在序列中）
    private int[] frameSequence;//帧序列

    public Sprite(ArrayList<Bitmap> bitmaps, int width, int height) {
        this.bitmaps = bitmaps;
        this.width = width;
        this.height = height;
        frameSequence=new int[bitmaps.size()];//初始化默认的帧序列
        for (int i = 0; i < frameSequence.length; i++) {
            frameSequence[i] = i;
        }
    }

    public Sprite(Bitmap bitmap) {
        this(bitmap,bitmap.getWidth(),bitmap.getHeight());
    }

    public Sprite(Bitmap bitmap, int width, int height) {
        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
        int w=bitmap.getWidth()/width;//大图分割为小图
        int h=bitmap.getHeight()/height;
        frameNumber=w*h;//总帧数
        frameX=new int[frameNumber];//存放每一帧的坐标
        frameY=new int[frameNumber];
        for (int i = 0; i < h; i++) {//行
            for (int j = 0; j < w; j++) {//列
                frameX[w*i+j]=width*j;//计算每一帧在原图上的坐标
                frameY[w*i+j]=height*i;
            }
        }
        src=new Rect();
        dst=new Rect();
        frameSequence=new int[frameNumber];//初始化默认的帧序列
        for (int i = 0; i < frameNumber; i++) {
            frameSequence[i] = i;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getFrameSequenceIndex() {
        return frameSequenceIndex;
    }

    public void setFrameSequenceIndex(int frameSequenceIndex) {
        this.frameSequenceIndex = frameSequenceIndex;
    }

    public int[] getFrameSequence() {
        return frameSequence;
    }

    public void setFrameSequence(int[] frameSequence) {
        this.frameSequence = frameSequence;
    }

    public void setPosition(int x, int y){
        this.x=x;
        this.y=y;
    }

    public void logic() {
       move(speedX,speedY);
    }

    public void draw(Canvas canvas){
       if (isVisible){
           if (bitmap!=null){
               src.set(frameX[frameSequence[frameSequenceIndex]]
                       ,frameY[frameSequence[frameSequenceIndex]],
                       frameX[frameSequence[frameSequenceIndex]]+width,
                       frameY[frameSequence[frameSequenceIndex]]+height);
               dst.set(x,y,x+width,y+height);
               canvas.drawBitmap(bitmap,src,dst,null);
           }else if (bitmaps!=null){
               canvas.drawBitmap(bitmaps.get(frameSequence[frameSequenceIndex]),x,y,null);
           }
       }
    }

    public void move(float distanceX, float distanceY) {
        x+=distanceX;
        y+=distanceY;
        outOfBounds();
    }

    public void outOfBounds() {
        if (getX()<0||getX()>1280||getY()<0||getY()>768){
            setVisible(false);
        }
    }

    public boolean collisionWith(Sprite sprite){//碰撞检测，排除法
        if (!isVisible()||!sprite.isVisible()){//排除隐藏
            return false;
        }
        if (getX()<sprite.getX()&&getX()+getWidth()<sprite.getX()){//排除右侧
            return false;
        }
        if (sprite.getX()<getX()&&sprite.getX()+sprite.getWidth()<getX()){//排除左侧
            return false;
        }
        if (getY()<sprite.getY()&&getY()+getHeight()<sprite.getY()){//排除下侧
            return false;
        }
        if (sprite.getY()<getY()&&sprite.getY()+sprite.getHeight()<getY()){//排除上侧
            return false;
        }
        return true;
    }

    public void nextFrame(){
        setFrameSequenceIndex((getFrameSequenceIndex()+1)%getFrameSequence().length);
    }
}
