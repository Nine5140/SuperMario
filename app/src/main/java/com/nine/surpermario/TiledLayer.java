package com.nine.surpermario;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Administrator on 2018/9/27 0027.
 */

class TiledLayer {
    private Bitmap bitmap;
    private int width;
    private int height;
    private int x;
    private int y;
    private int[] tiledX;
    private int[] tiledY;
    private Rect src;
    private Rect dst;
    private int cols;//列数
    private int rows;//行数
    private int[][] tiledCell;//存放各行和列平铺图块的索引，用以构成完整的地图

    public TiledLayer(Bitmap bitmap, int width, int height, int cols, int rows) {
        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
        this.cols = cols;
        this.rows = rows;
        int w=bitmap.getWidth()/width;//大图分割为小图
        int h=bitmap.getHeight()/height;
        tiledX =new int[w*h+1];//索引0用来表示透明
        tiledY=new int[w*h+1];
        for (int i = 0; i < h; i++) {//行
            for (int j = 0; j < w; j++) {//列
                tiledX[w*i+j+1]=width*j;//计算每一帧在原图上的坐标
                tiledY[w*i+j+1]=height*i;
            }
        }
        src=new Rect();
        dst=new Rect();
        tiledCell = new int[rows][cols];
    }

    public Bitmap getBitmap() {
        return bitmap;
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

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getTiledCellIndex(int row,int col) {
        return tiledCell[row][col];
    }

    public void setTiledCell(int[][] tiledCell) {
        this.tiledCell = tiledCell;
    }

    public void setPosition(int x, int y){
        this.x=x;
        this.y=y;
    }

    public void draw(Canvas canvas){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int tiledIndex=tiledCell[i][j];//取出第i行第j列的索引
                if (tiledIndex==0){
                    continue;//透明无需绘制
                }
                src.set(tiledX[tiledIndex],tiledY[tiledIndex],
                        tiledX[tiledIndex]+getWidth(),
                        tiledY[tiledIndex]+getHeight());
                dst.set(getX()+j*getWidth(),getY()+i*getHeight(),
                        getX()+(j+1)*getWidth(),getY()+(i+1)*getHeight() );
                canvas.drawBitmap(bitmap,src,dst,null);
            }
        }
    }

    public void move(float distanceX, float distanceY) {
        x+=distanceX;
        y+=distanceY;
        outOfBounds();
    }

    public void outOfBounds() {
        if (getX()>0){
            setX(0);
        }else if (getX()<1280-cols*width){
            setX(1280-cols*width);
        }
    }
}
