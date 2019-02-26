package com.nine.surpermario;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            //View.SYSTEM_UI_FLAG_HIDE_NAVIGATION :隐藏虚拟按键（导航栏）
            //View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY :粘性全屏沉浸模式
            //顶部下滑会显示出透明的状态栏和虚拟导航栏，在一段时间以后自动恢复成全屏沉浸模式
        }
        setContentView(new MyView(this));
    }
}
