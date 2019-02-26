package com.nine.surpermario;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import junit.framework.AssertionFailedError;

import java.io.IOException;

/**
 * Created by nine on 2018/12/17.
 */

public class MyMusic {

    private Context context;
    private MediaPlayer mediaPlayer;
    private String fileName;

    public MyMusic(Context context) {
        this.context = context;
        mediaPlayer = new MediaPlayer();
    }

    public void play(String fileName) {
        if (fileName.equals(this.fileName)) {
            return;
        }
        this.fileName = fileName;
        mediaPlayer.reset();
        try {
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(fileName);
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}
