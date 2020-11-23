package com.example.musicplayer;

import android.app.Application;

import com.xuexiang.xui.XUI;

/**
 * @author 14548
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化UI框架
        XUI.init(this);
        //开启UI框架调试日志
        XUI.debug(true);

    }
}
