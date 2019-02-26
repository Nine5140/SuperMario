package com.nine.surpermario;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by nine on 2018/12/12.
 */

class MyView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private final SurfaceHolder holder;
    private final float scaleX;
    private final float scaleY;
    private boolean isRun;
    private Paint paint;
    private Control control_a;
    private Control control_b;
    private Control control_left;
    private Control control_right;
    private Control control_down;
    private RectF rectF;
    private Mario mario;
    private int speedY;
    private TiledLayer tiledLayer_background;
    private TiledLayer tiledLayer_collision;
    private TiledLayer tiledLayer_foreground;
    private boolean gameover;
    private GameState gameState;
    private int step;
    private ArrayList<Mushroom> mushrooms;
    private boolean isVisibleEnemy1;
    private boolean isVisibleEnemy2;
    private boolean isVisibleEnemy3;
    private MyMusic myMusic;
    private MySoundPool mySoundPool;
    private ArrayList<Coin> coins;
    private ArrayList<Bitmap> coinBitmaps;
    private Bitmap coinBitmap;
    private int coinCount;
    private boolean isVisibleCoin1;
    private boolean isVisibleCoin2;
    private boolean isVisibleCoin3;
    private int life = 3;
    private boolean gameover2;
    private int score;

    public MyView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Point screenSize = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        if (windowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                windowManager.getDefaultDisplay().getRealSize(screenSize);//包含虚拟导航键的尺寸。
            } else {
                windowManager.getDefaultDisplay().getSize(screenSize);
            }
        }
        scaleX = (float) screenSize.x / 1280;
        scaleY = (float) screenSize.y / 768;
        init();
    }

    private void init() {
        if (gameState == GameState.WIN) {
            gameState = GameState.MENU;
        } else {
            gameState = GameState.LOGO;
        }

        if (!gameover2) {
            score = 0;
            life = 3;
        }

        step = 0;
        myMusic = new MyMusic(getContext());
        mySoundPool = new MySoundPool(getContext());

        control_a = new Control(getBitmap("control.png"), 80, 80);
        control_a.setControlState(ControlState.A);
        control_a.setPosition(900, 580);
        control_a.setVisible(true);
        control_b = new Control(getBitmap("control.png"), 80, 80);
        control_b.setControlState(ControlState.B);
        control_b.setPosition(1100, 500);
        control_b.setVisible(true);
        control_left = new Control(getBitmap("control.png"), 80, 80);
        control_left.setControlState(ControlState.LEFT);
        control_left.setPosition(80, 600);
        control_left.setVisible(true);
        control_right = new Control(getBitmap("control.png"), 80, 80);
        control_right.setControlState(ControlState.RIGHT);
        control_right.setPosition(300, 600);
        control_right.setVisible(true);
        control_down = new Control(getBitmap("control.png"), 80, 80);
        control_down.setControlState(ControlState.DOWN);
        control_down.setPosition(190, 680);
        control_down.setVisible(true);
        rectF = new RectF();

        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            bitmaps.add(getBitmap("mario" + i + ".png"));
        }

        coinBitmaps = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            coinBitmaps.add(getBitmap("coin_" + i + ".png"));
        }

        mario = new Mario(bitmaps, 63, 84);
        mario.setPosition(0, 516);
        mario.setVisible(true);

        mushrooms = new ArrayList<>();
        isVisibleEnemy1 = false;
        isVisibleEnemy2 = false;
        isVisibleEnemy3 = false;

        coinBitmap = getBitmap("coin_0.png");
        coins = new ArrayList<>();
        isVisibleCoin1 = false;
        isVisibleCoin2 = false;
        isVisibleCoin3 = false;

        gameover = false;
        gameover2 = false;

        int[][] map_background = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 33, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 71, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274,
                        0, 0, 0, 0, 95, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298,
                        0, 0, 0, 0, 95, 0, 0, 0, 0, 125, 126, 0, 0, 129, 130, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322,
                        0, 0, 0, 0, 95, 0, 0, 0, 0, 125, 0, 0, 0, 129, 130, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346,
                        0, 0, 0, 0, 95, 0, 0, 0, 0, 125, 126, 127, 128, 129, 130, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 0, 0, 0, 0, 0,
                        313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370,
                        0, 0, 0, 0, 95, 0, 0, 0, 0, 149, 150, 151, 152, 153, 154, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 133, 134, 135, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 0, 0, 0, 0, 0,
                        337, 338, 339, 340, 341, 342, 343, 344, 345, 346, 0, 0, 0, 0, 0, 227,
                        228, 229, 0, 0, 0, 0, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394,
                        0, 0, 0, 0, 95, 0, 0, 0, 0, 173, 174, 175, 176, 177, 178, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 265, 266, 267, 0, 157, 158, 159, 0, 273, 274, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 0, 0, 0, 0, 0,
                        361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 0, 0, 0, 0, 0, 251,
                        252, 253, 0, 0, 0, 0, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418,
                        0, 0, 0, 0, 95, 0, 0, 0, 0, 197, 198, 199, 200, 201, 202, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 289, 0, 0, 0, 181, 182, 183, 0, 297, 298, 0,
                        0, 0, 0, 0, 0, 227, 228, 229, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346, 0, 0, 0, 0, 0,
                        385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 0, 0, 0, 0, 0, 275,
                        276, 277, 0, 0, 0, 0, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442,
                        0, 0, 0, 0, 95, 0, 0, 0, 0, 221, 222, 223, 224, 225, 226, 0,
                        23, 24, 0, 0},
                {0, 0, 0, 0, 0, 313, 0, 0, 0, 205, 206, 207, 0, 321, 322, 0,
                        0, 0, 0, 0, 0, 251, 252, 253, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 0, 0, 0, 0, 0,
                        409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 0, 0, 0, 0, 0, 299,
                        300, 301, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 95, 0, 0, 0, 0, 245, 246, 247, 248, 249, 250, 0,
                        47, 48, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 345, 346, 0,
                        0, 0, 0, 0, 0, 275, 276, 277, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 0, 0, 0, 0, 0,
                        433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 299, 300, 301, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0}
        };

        int[][] map_collison = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        29, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 29, 29, 9, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 6, 9, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 0, 0, 0, 6, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 29, 29, 9, 0,
                        0, 0, 0, 29, 29, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 29, 29, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30, 30,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 49, 50,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 55, 56, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30, 30, 30,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 73, 74,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 79, 80, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30, 30, 30, 30,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 97, 98,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 103, 104, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30, 30, 30, 30, 30,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3,
                        2, 3, 2, 3, 2, 3, 2, 3, 4, 0, 0, 0, 1, 3, 2, 3,
                        2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 4,
                        0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 1, 3, 2, 3, 2, 3, 2, 3, 2, 3,
                        2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3,
                        2, 3, 2, 3},
                {26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27,
                        26, 27, 26, 27, 26, 27, 26, 27, 28, 0, 0, 0, 25, 27, 26, 27,
                        26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 28,
                        0, 0, 0, 0, 0, 0, 0, 0, 25, 26, 27, 28, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 25, 27, 26, 27, 26, 27, 26, 27, 26, 27,
                        26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27,
                        26, 27, 26, 27},
                {26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27,
                        26, 27, 26, 27, 26, 27, 26, 27, 28, 0, 0, 0, 25, 27, 26, 27,
                        26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 28,
                        0, 0, 0, 0, 0, 0, 0, 0, 25, 26, 27, 28, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 25, 27, 26, 27, 26, 27, 26, 27, 26, 27,
                        26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27,
                        26, 27, 26, 27},
                {26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27,
                        26, 27, 26, 27, 26, 27, 26, 27, 28, 0, 0, 0, 25, 27, 26, 27,
                        26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 28,
                        0, 0, 0, 0, 0, 0, 0, 0, 25, 26, 27, 28, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 25, 27, 26, 27, 26, 27, 26, 27, 26, 27,
                        26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27, 26, 27,
                        26, 27, 26, 27}
        };

        int[][] map_foreground = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 133, 134, 135, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 133, 134, 135, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 157, 158, 159, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 157, 158, 159, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 181, 182, 183, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 181, 182, 183, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 116, 117, 117, 118, 205, 206, 207, 116, 117, 117, 117, 118, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 116,
                        117, 117, 205, 206, 207, 117, 117, 117, 118, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0}
        };
        tiledLayer_background = new TiledLayer(getBitmap("tiled.png"),
                42, 42, 100, 18);
        tiledLayer_background.setTiledCell(map_background);
        tiledLayer_background.setPosition(0, 12);
        tiledLayer_collision = new TiledLayer(getBitmap("tiled.png"),
                42, 42, 100, 18);
        tiledLayer_collision.setTiledCell(map_collison);
        tiledLayer_collision.setPosition(0, 12);
        tiledLayer_foreground = new TiledLayer(getBitmap("tiled.png"),
                42, 42, 100, 18);
        tiledLayer_foreground.setTiledCell(map_foreground);
        tiledLayer_foreground.setPosition(0, 12);
    }

    private Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContext().getAssets().open(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isRun = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRun = false;
        myMusic.stop();
    }

    @Override
    public void run() {
        while (isRun) {
            long startTime = System.currentTimeMillis();
            logic();
            redraw();
            long endTime = System.currentTimeMillis();
            long diffTime = endTime - startTime;//结束时间减开始时间。
            if (diffTime < 1000 / 60) {
                try {
                    Thread.sleep(1000 / 60 - diffTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void logic() {
        if (gameover2) {
            gameState = GameState.GAME;
        } else if (gameover) {
            gameState = GameState.GAMEOVER;
        }
        switch (gameState) {
            case LOGO:
                step += 5;
                if (step == 200) {
                    gameState = GameState.CG;
                }
                break;
            case CG:
                step += 5;
                if (step == 800) {
                    gameState = GameState.MENU;
                }
                break;
            case MENU:
                //gameState = GameState.GAME;
                break;
            case GAME:
                myMusic.play("music/bgm.mp3");
                if (mushrooms != null) {
                    for (Mushroom mushroom : mushrooms) {
                        mushroom.logic();
                    }
                }
                if (mario.getX() > 900) {
                    myMusic.stop();
                    mySoundPool.play(mySoundPool.getWin());
                    gameState = GameState.WIN;
                    step = 0;
                }
                if (coins != null) {
                    for (Coin coin : coins) {
                        coin.logic();
                    }
                }
                control();
                mario.logic();
                move();
                collisionWithMap();
                progress();
                coin();
                collisionWithCoin();
                collisionWithEnemy();
                break;
            case GAMEOVER:
                gameState = GameState.MENU;
                break;
            case WIN:
                step += 5;
                if (step == 450) {
                    init();
                }
                break;
        }
    }


    private void progress() {
        if (tiledLayer_collision.getX() == 0 && !isVisibleEnemy1) {
            for (int i = 0; i < 4; i++) {
                Mushroom mushroom = new Mushroom(getBitmap("mushroom.png"),
                        40, 40);
                mushroom.setPosition(600 + 100 * i, 560);
                mushroom.setMoveX(true);
                mushroom.setLeft(true);
                mushroom.setVisible(true);
                mushrooms.add(mushroom);
            }
            isVisibleEnemy1 = true;
        }
        if (tiledLayer_collision.getX() == -600 && !isVisibleEnemy2) {
            for (int i = 0; i < 4; i++) {
                Mushroom mushroom = new Mushroom(getBitmap("mushroom.png"),
                        40, 40);
                mushroom.setPosition(800 + 100 * i, 560);
                mushroom.setMoveX(true);
                mushroom.setLeft(true);
                mushroom.setVisible(true);
                mushrooms.add(mushroom);
            }
            isVisibleEnemy2 = true;
        }
        if (tiledLayer_collision.getX() == -2400 && !isVisibleEnemy3) {
            for (int i = 0; i < 4; i++) {
                Mushroom mushroom = new Mushroom(getBitmap("mushroom.png"),
                        40, 40);
                mushroom.setPosition(750 + 80 * i, 300 - 100 * i);
                mushroom.setMoveX(true);
                mushroom.setLeft(true);
                mushroom.setVisible(true);
                mushrooms.add(mushroom);
            }
            isVisibleEnemy3 = true;
        }
    }

    private void coin() {
        if (tiledLayer_collision.getX() == 0 && !isVisibleCoin1) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 3; j++) {
                    Coin coin = new Coin(coinBitmaps, 40, 40);
                    coin.setPosition(500 + 50 * i, 350 - 50 * j);
                    coin.setVisible(true);
                    coins.add(coin);
                }
            }
            isVisibleCoin1 = true;
        }
        if (tiledLayer_collision.getX() == 0 && !isVisibleCoin2) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 4; j++) {
                    Coin coin = new Coin(coinBitmaps, 40, 40);
                    coin.setPosition(1550 + 50 * i, 130 - 30 * j);
                    coin.setVisible(true);
                    coins.add(coin);
                }
            }
            isVisibleCoin2 = true;
        }
        if (tiledLayer_collision.getX() == 0 && !isVisibleCoin3) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 5; j++) {
                    Coin coin = new Coin(coinBitmaps, 40, 40);
                    coin.setPosition(2450 + 50 * i, 560 - 50 * j);
                    coin.setVisible(true);
                    coins.add(coin);
                }
            }
            isVisibleCoin3 = true;
        }
    }

    private void collisionWithEnemy() {
        if (mushrooms != null) {
            for (Mushroom mushroom : mushrooms) {
                if (!mushroom.isDead() && !mario.isDead()) {
                    if (mushroom.collisionWith(mario)) {
                        if (mario.isMoveY() && speedY > 0) {
                            mySoundPool.play(mySoundPool.getKill_m());
                            mushroom.setDead(true);
                            score += 100;
                            mushroom.setMoveX(false);
                            speedY = -10;
                        } else {
                            life--;
                            myMusic.stop();
                            mySoundPool.play(mySoundPool.getOver());
                            mario.setDead(true);
                            mario.setMoveY(true);
                            speedY = -20;
                        }
                    }
                }
            }
        }
    }

    private void collisionWithCoin() {
        if (coins != null) {
            for (Coin coin : coins) {
                if (coin.collisionWith(mario)) {
                    mySoundPool.play(mySoundPool.getCoin());
                    coin.setVisible(false);
                    coinCount++;
                    score += 1000;
                }
                if (coinCount==100){
                    coinCount=0;
                    life++;
                    mySoundPool.play(mySoundPool.getAdd_life());
                }
            }
        }
    }

    private void collisionWithMap() {
        if (!mario.isDead()) {
            if (mario.siteCollisionWith(tiledLayer_collision, Site.UP_LEFT)
                    || mario.siteCollisionWith(tiledLayer_collision, Site.UP_RIGHT)) {
                speedY = Math.abs(speedY);
            }
            if (mario.siteCollisionWith(tiledLayer_collision, Site.LEFT_UP)
                    || mario.siteCollisionWith(tiledLayer_collision, Site.LEFT_DOWN)) {
                mario.move(8, 0);
            }
            if (mario.siteCollisionWith(tiledLayer_collision, Site.RIGHT_UP)
                    || mario.siteCollisionWith(tiledLayer_collision, Site.RIGHT_DOWN)) {
                mario.move(-8, 0);
            }
            if (mario.siteCollisionWith(tiledLayer_collision, Site.DOWN_CENTER)) {
                mario.setMoveY(false);
                mario.setPosition(mario.getX(), (mario.getY() + mario.getHeight() - 12)
                        / tiledLayer_collision.getHeight() * tiledLayer_collision.getHeight()
                        - mario.getHeight() + 12);
            } else if (!mario.isMoveY()) {
                mario.setMoveY(true);
                speedY = 0;
            }
        }
        if (mushrooms != null) {
            for (Mushroom mushroom : mushrooms) {
                if (mushroom.isVisible() && !mushroom.isDead()) {
                    if (mushroom.siteCollisionWith(tiledLayer_collision, Site.LEFT_UP) ||
                            mushroom.siteCollisionWith(tiledLayer_collision, Site.LEFT_DOWN)) {
                        mushroom.setLeft(false);
                    }
                    if (mushroom.siteCollisionWith(tiledLayer_collision, Site.RIGHT_UP) ||
                            mushroom.siteCollisionWith(tiledLayer_collision, Site.RIGHT_DOWN)) {
                        mushroom.setLeft(true);
                    }
                    if (mushroom.siteCollisionWith(tiledLayer_collision, Site.DOWN_CENTER)) {
                        mushroom.setMoveY(false);
                        mushroom.setPosition(mushroom.getX(),
                                (mushroom.getY() + mushroom.getHeight() - 12)
                                        / tiledLayer_collision.getHeight() * tiledLayer_collision.getHeight()
                                        - mushroom.getHeight() + 12);
                        if (!mushroom.isMoveX()) {
                            mushroom.setMoveX(true);
                            mushroom.setLeft(true);
                        }
                    } else if (!mushroom.isMoveY()) {
                        mushroom.setMoveY(true);
                    }
                }
            }
        }
    }

    private void move() {
        if (!mario.isDead() && mario.isMoveX()) {
            if (mario.isMirror()) {
                mario.move(-8, 0);
            } else {
                if (mario.getX() < 640 - mario.getWidth() / 2) {
                    mario.move(8, 0);
                } else if (tiledLayer_collision.getX() >
                        1280 - tiledLayer_collision.getCols() * tiledLayer_collision.getWidth()) {
                    tiledLayer_background.move(-8, 0);
                    tiledLayer_collision.move(-8, 0);
                    tiledLayer_foreground.move(-8, 0);
                    if (mushrooms != null) {
                        for (Mushroom mushroom : mushrooms) {
                            mushroom.move(-8, 0);
                        }
                    }
                    if (coins != null) {
                        for (Coin coin : coins) {
                            coin.move(-8, 0);
                        }
                    }
                } else {
                    mario.move(8, 0);
                }
            }
        }
        if (mario.isMoveY()) {
            mario.move(0, speedY);
            speedY++;
        }
        if (mario.getY() > 650) {
            if (!mario.isDead()) {
                life--;
                mario.setDead(true);
                mario.setMoveY(true);
                speedY = -20;
                myMusic.stop();
                mySoundPool.play(mySoundPool.getOver());
            } else if (life == 0) {
                gameover = true;
                coinCount = 0;
                myMusic.stop();
                mySoundPool.play(mySoundPool.getGameover());
            } else {
                gameover2 = true;

            }
        }

    }

    private void control() {
        if (!mario.isDead()) {
            if (control_left.isPress()) {
                mario.setMirror(true);
                mario.setMoveX(true);
            } else if (control_right.isPress()) {
                mario.setMirror(false);
                mario.setMoveX(true);
            } else {
                mario.setMoveX(false);
            }
            if (control_b.isPress()) {
                if (!mario.isMoveY()) {
                    mySoundPool.play(mySoundPool.getJump());
                    mario.setMoveY(true);
                    speedY = -20;
                }
            }
        }
    }

    private void redraw() {
        Canvas lockCanvas = holder.lockCanvas();
        try {
            synchronized (holder) {
                myDraw(lockCanvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (lockCanvas != null) {
                holder.unlockCanvasAndPost(lockCanvas);
            }
        }
    }

    private void myDraw(Canvas canvas) {

        canvas.save();
        canvas.scale(scaleX, scaleY);
        paint.reset();

        if (gameover2) {
            canvas.drawColor(Color.BLACK);
            paint.setTextSize(50);
            paint.setColor(Color.WHITE);
            canvas.drawBitmap(getBitmap("mario0.png"), 550, 310, null);
            canvas.drawText(String.format(Locale.CHINA, "X %02d", life),
                    650, 380, paint);
        } else if (gameover) {
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(getBitmap("over.png"), 450, 260, null);
        } else {
            switch (gameState) {
                case LOGO:
                    canvas.drawBitmap(getBitmap("logo.png"), 0, 0, paint);
                    break;
                case CG:
                    canvas.drawColor(Color.BLACK);
                    canvas.save();
                    canvas.clipRect(0, 768 - step, 1280, 768);
                    canvas.drawBitmap(getBitmap("cg.png"), 0, 0, paint);
                    canvas.restore();
                    break;
                case MENU:
                    canvas.drawBitmap(getBitmap("menu.png"), 0, 0, paint);
                    break;
                case GAME:
                    canvas.drawColor(Color.argb(255, 178, 230, 255));
                    tiledLayer_background.draw(canvas);
                    tiledLayer_collision.draw(canvas);
                    if (mushrooms != null) {
                        for (Mushroom mushroom : mushrooms) {
                            mushroom.draw(canvas);
                        }
                    }
                    if (coins != null) {
                        for (Coin coin : coins) {
                            coin.draw(canvas);
                        }
                    }
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(27);
                    canvas.drawBitmap(coinBitmap, 300, 15, null);
                    canvas.drawText(String.format(Locale.CHINA, "X %2d", coinCount), 340, 45, paint);
                    canvas.drawText(String.format(Locale.CHINA, "SCORE : %2d", score), 540, 45, paint);
                    canvas.drawBitmap(getBitmap("life.png"), 800, 15, null);
                    canvas.drawText(String.format(Locale.CHINA, "X %2d", life), 840, 45, paint);
                    mario.draw(canvas);
                    tiledLayer_foreground.draw(canvas);
                    control_a.draw(canvas);
                    control_b.draw(canvas);
                    control_left.draw(canvas);
                    control_right.draw(canvas);
                    control_down.draw(canvas);
                    break;
                case WIN:
                    canvas.drawBitmap(getBitmap("win.png"), 0, 0, paint);
                    break;
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (gameover2) {
            init();
        } else if (gameover) {
            init();
        }

        if (gameState == GameState.CG) {
            gameState = GameState.MENU;
        }
        rectF.set(540 * scaleX, 460 * scaleY, 840 * scaleX, 590 * scaleY);
        if (rectF.contains(event.getX(), event.getY())) {
            gameState = GameState.GAME;
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                rectF.set(1100 * scaleX, 500 * scaleY, 1180 * scaleX, 600 * scaleY);
                if (rectF.contains(event.getX(), event.getY())) {
                    control_b.setPress(true);
                }
                rectF.set(80 * scaleX, 600 * scaleY, 180 * scaleX, 680 * scaleY);
                if (rectF.contains(event.getX(), event.getY())) {
                    control_left.setPress(true);
                }
                rectF.set(300 * scaleX, 600 * scaleY, 380 * scaleX, 680 * scaleY);
                if (rectF.contains(event.getX(), event.getY())) {
                    control_right.setPress(true);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    rectF.set(1100 * scaleX, 500 * scaleY, 1180 * scaleX, 600 * scaleY);
                    if (rectF.contains(event.getX(i), event.getY(i))) {
                        control_b.setPress(true);
                    }
                    rectF.set(80 * scaleX, 600 * scaleY, 180 * scaleX, 680 * scaleY);
                    if (rectF.contains(event.getX(i), event.getY(i))) {
                        control_left.setPress(true);
                    }
                    rectF.set(300 * scaleX, 600 * scaleY, 380 * scaleX, 680 * scaleY);
                    if (rectF.contains(event.getX(i), event.getY(i))) {
                        control_right.setPress(true);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                control_b.setPress(false);
                control_left.setPress(false);
                control_right.setPress(false);
                for (int i = 0; i < event.getPointerCount(); i++) {
                    rectF.set(1100 * scaleX, 500 * scaleY, 1180 * scaleX, 600 * scaleY);
                    if (rectF.contains(event.getX(i), event.getY(i))) {
                        control_b.setPress(true);
                    }
                    rectF.set(80 * scaleX, 600 * scaleY, 180 * scaleX, 680 * scaleY);
                    if (rectF.contains(event.getX(i), event.getY(i))) {
                        control_left.setPress(true);
                    }
                    rectF.set(300 * scaleX, 600 * scaleY, 380 * scaleX, 680 * scaleY);
                    if (rectF.contains(event.getX(i), event.getY(i))) {
                        control_right.setPress(true);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    rectF.set(1100 * scaleX, 500 * scaleY, 1180 * scaleX, 600 * scaleY);
                    if (rectF.contains(event.getX(i), event.getY(i))) {
                        control_b.setPress(false);
                    }
                    rectF.set(80 * scaleX, 600 * scaleY, 180 * scaleX, 680 * scaleY);
                    if (rectF.contains(event.getX(i), event.getY(i))) {
                        control_left.setPress(false);
                    }
                    rectF.set(300 * scaleX, 600 * scaleY, 380 * scaleX, 680 * scaleY);
                    if (rectF.contains(event.getX(i), event.getY(i))) {
                        control_right.setPress(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                rectF.set(1100 * scaleX, 500 * scaleY, 1180 * scaleX, 600 * scaleY);
                if (rectF.contains(event.getX(), event.getY())) {
                    control_b.setPress(false);
                }
                rectF.set(80 * scaleX, 600 * scaleY, 180 * scaleX, 680 * scaleY);
                if (rectF.contains(event.getX(), event.getY())) {
                    control_left.setPress(false);
                }
                rectF.set(300 * scaleX, 600 * scaleY, 380 * scaleX, 680 * scaleY);
                if (rectF.contains(event.getX(), event.getY())) {
                    control_right.setPress(false);
                }
                performClick();
                break;
        }
        control();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
