package com.example.musicplayer;

import android.app.Application;

import com.xuexiang.xui.XUI;

/**
 * @author ywww
 * @date 2020-11-15 22:06
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化UI框架
        XUI.init(this);
        // 开启UI框架测试日志
        XUI.debug(true);
    }
}
