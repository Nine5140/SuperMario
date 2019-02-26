package com.nine.surpermario;

import android.graphics.Bitmap;

/**
 * Created by nine on 2018/12/17.
 */

public class Mushroom extends Sprite {

    private boolean isLeft;
    private boolean isMoveX;
    private boolean isMoveY;
    private boolean isDead;
    private int delay;
    private int speedY;

    public Mushroom(Bitmap bitmap, int width, int height) {
        super(bitmap, width, height);
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
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

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public int getSpeedY() {
        return speedY;
    }

    @Override
    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    @Override
    public void logic() {
        if (isDead()) {
            if (delay++ == 20) {
                setVisible(false);
            } else {
                setFrameSequenceIndex(2);
            }
        } else {
            if (getFrameSequenceIndex() == 0) {
                setFrameSequenceIndex(1);
            } else if (getFrameSequenceIndex() == 1) {
                setFrameSequenceIndex(0);
            }
        }
        move();
    }

    private void move() {
        if (isMoveX) {
            if (isLeft) {
                move(-4, 0);
            } else {
                move(4, 0);
            }
        }
        if (isMoveY) {
            move(0, speedY++);
        }
    }

    @Override
    public void outOfBounds() {
        if (getX() < -getWidth()) {
            setVisible(false);
        }
        if (getY() > 700) {
            setVisible(false);
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
        if (siteCol >= tiledLayer.getCols() || siteRow > tiledLayer.getRows()) {
            return true;
        }
        if (tiledLayer.getTiledCellIndex(siteRow, siteCol) != 0) {
            return true;
        }
        return false;
    }
}
