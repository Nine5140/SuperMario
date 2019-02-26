package com.nine.surpermario;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by nine on 2018/12/12.
 */

public class Mario extends Sprite {

    private boolean isMirror;
    private boolean isMoveX;
    private boolean isMoveY;
    private boolean isDead;

    public Mario(ArrayList<Bitmap> bitmaps, int width, int height) {
        super(bitmaps, width, height);
    }

    public Mario(Bitmap bitmap) {
        super(bitmap);
    }

    public Mario(Bitmap bitmap, int width, int height) {
        super(bitmap, width, height);
    }

    public boolean isMirror() {
        return isMirror;
    }

    public void setMirror(boolean mirror) {
        isMirror = mirror;
    }

    public boolean isMoveX() {
        return isMoveX;
    }

    public void setMoveX(boolean moveX) {
        isMoveX = moveX;
    }

    public boolean isMoveY() {
        return isMoveY;
    }

    public void setMoveY(boolean moveY) {
        isMoveY = moveY;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void logic() {
        if (isDead()) {
            setFrameSequenceIndex(18);
        } else if (isMoveY()) {
            setFrameSequenceIndex(17);
        } else if (isMoveX()) {
            nextFrame();
            if (getFrameSequenceIndex() == 16) {
                setFrameSequenceIndex(1);
            }
        } else {
            setFrameSequenceIndex(0);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (isMirror) {
            canvas.save();
            canvas.scale(-1, 1, getX() + getWidth() / 2, getY() + getHeight() / 2);
            super.draw(canvas);
            canvas.restore();
        } else {
            super.draw(canvas);
        }
    }

    @Override
    public void outOfBounds() {
        if (getX() < 0) {
            setX(0);
        } else if (getX() > 1280 - getWidth()) {
            setX(1280 - getWidth());
        }
        if (getY() < 0) {
            setY(0);
        } else if (getY() > 768 - getHeight()) {
            setY(768 - getHeight());
        }
    }

    public boolean siteCollisionWith(TiledLayer tiledLayer, Site site) {
        int siteX = 0;
        int siteY = 0;
        switch (site) {
            case LEFT_UP:
                siteX = getX();
                siteY = getY() + getHeight() / 3;
                break;
            case LEFT_DOWN:
                siteX = getX();
                siteY = getY() + 2 * getHeight() / 3;
                break;
            case RIGHT_UP:
                siteX = getX() + getWidth();
                siteY = getY() + getHeight() / 3;
                break;
            case RIGHT_DOWN:
                siteX = getX() + getWidth();
                siteY = getY() + 2 * getHeight() / 3;
                break;
            case UP_LEFT:
                siteX = getX() + getWidth() / 3;
                siteY = getY();
                break;
            case UP_RIGHT:
                siteX = getX() + 2 * getWidth() / 3;
                siteY = getY();
                break;
            case DOWN_CENTER:
                siteX = getX() + getWidth() / 2;
                siteY = getY() + getHeight();
                break;
        }
        int siteMapX = siteX - tiledLayer.getX();
        int siteMapY = siteY - tiledLayer.getY();
        int siteCol = siteMapX / tiledLayer.getWidth();
        int siteRow = siteMapY / tiledLayer.getHeight();
        if (siteCol >= tiledLayer.getCols() || siteRow >= tiledLayer.getRows()) {
            return true;
        }
        if (tiledLayer.getTiledCellIndex(siteRow, siteCol) != 0) {
            return true;
        }
        return false;
    }
}
