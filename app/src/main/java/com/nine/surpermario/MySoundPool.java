package com.nine.surpermario;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

/**
 * Created by nine on 2018/12/17.
 */

public class MySoundPool {

    private Context context;
    private SoundPool soundPool;
    private final int kill_m;
    private final int over;
    private final int jump;
    private final int win;
    private final int coin;
    private final int gameover;
    private final int add_life;

    public MySoundPool(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(5).build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        kill_m = getSound("music/kill_m.mp3");
        over = getSound("music/over.mp3");
        jump = getSound("music/jump.mp3");
        win = getSound("music/finish.mp3");
        coin = getSound("music/coin.mp3");
        gameover = getSound("music/gameover.mp3");
        add_life=getSound("music/add_life.mp3");
    }

    private int getSound(String fileName) {
        int soundID = 0;
        try {
            soundID = soundPool.load(context.getAssets().openFd(fileName), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return soundID;
    }

    public void play(int soundID) {
        soundPool.play(soundID, 1, 1, 1, 0, 1);
    }

    public int getKill_m() {
        return kill_m;
    }

    public int getOver() {
        return over;
    }

    public int getJump() {
        return jump;
    }

    public int getWin() {
        return win;
    }

    public int getCoin() {
        return coin;
    }

    public int getGameover() {
        return gameover;
    }

    public int getAdd_life() {
        return add_life;
    }
}
