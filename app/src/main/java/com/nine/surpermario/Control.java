package com.nine.surpermario;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by nine on 2018/12/12.
 */

public class Control extends Sprite {

    private ControlState controlState;
    private boolean isPress;

    public Control(ArrayList<Bitmap> bitmaps, int width, int height) {
        super(bitmaps, width, height);
    }

    public Control(Bitmap bitmap) {
        super(bitmap);
    }

    public Control(Bitmap bitmap, int width, int height) {
        super(bitmap, width, height);
    }

    @Override
    public void draw(Canvas canvas) {
        switch (controlState){
            case A:
                if (isPress){
                    setFrameSequenceIndex(3);
                }else {
                    setFrameSequenceIndex(2);
                }
                super.draw(canvas);
                break;
            case B:
                if (isPress){
                    setFrameSequenceIndex(5);
                }else {
                    setFrameSequenceIndex(4);
                }
                super.draw(canvas);
                break;
            case LEFT:
                if (isPress){
                    setFrameSequenceIndex(1);
                }else {
                    setFrameSequenceIndex(0);
                }
                super.draw(canvas);
                break;
            case RIGHT:
                if (isPress){
                    setFrameSequenceIndex(1);
                }else {
                    setFrameSequenceIndex(0);
                }
                canvas.save();
                canvas.scale(-1,1,getX()+getWidth()/2,getY()+getHeight()/2);
                super.draw(canvas);
                canvas.restore();
                break;
            case UP:

            case DOWN:
                if (isPress){
                    setFrameSequenceIndex(7);
                }else {
                    setFrameSequenceIndex(6);
                }
                super.draw(canvas);
                break;
        }
    }

    public ControlState getControlState() {
        return controlState;
    }

    public void setControlState(ControlState controlState) {
        this.controlState = controlState;
    }

    public boolean isPress() {
        return isPress;
    }

    public void setPress(boolean press) {
        isPress = press;
    }
}
