package com.example.musicplayer.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

/**
 * @author zjw
 * @date 2020/11/29 18:09
 * @ClassName FileUtils
 */
public class FileUtils {
    public static String getDiskFileDir(Context context) {
        String filePath = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
            !Environment.isExternalStorageRemovable()) {
            filePath = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath();
        } else {
            filePath = context.getFilesDir().getPath();
        }
        return filePath;
    }
}
