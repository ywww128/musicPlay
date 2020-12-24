package com.example.xui;

import android.app.Application;

public class myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        XUI.init(this); //初始化UI框架
        XUI.debug(true);
    }
}
