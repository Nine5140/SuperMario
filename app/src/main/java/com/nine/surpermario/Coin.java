package com.nine.surpermario;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by nine on 2018/12/18.
 */

public class Coin extends Sprite {

    private int delay;

    public Coin(ArrayList<Bitmap> bitmaps, int width, int height) {
        super(bitmaps, width, height);
    }

    @Override
    public void logic() {
        if (isVisible()) {
            if (delay++ > 5) {
                nextFrame();
                delay = 0;
            }
        }
    }

    @Override
    public void outOfBounds() {
        if (getX() < -getWidth()) {
            setVisible(false);
        }
    }
}
