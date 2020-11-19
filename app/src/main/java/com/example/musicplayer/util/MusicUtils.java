package com.example.musicplayer.util;

import android.annotation.SuppressLint;

public class MusicUtils {
    @SuppressLint("DefaultLocale")
    public static String convertTime(int duration) {
        duration /= 1000;
        int minute = duration / 60;  // 计算分钟数
        int second = duration % 60;  // 计算余下的秒数
        return String.format("%02d:%02d", minute, second);
    }
}
