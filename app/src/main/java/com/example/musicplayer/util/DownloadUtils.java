package com.example.musicplayer.util;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zjw
 * @date 2020/11/28 22:37
 * @ClassName DownloadUtils
 * @thanks https://www.cnblogs.com/jmcui/p/8017473.html
 */
public class DownloadUtils {
    @SuppressLint("SdCardPath")
    private static final String SD_CARD_MUSIC_PATH = "/sdcard/Music/";

    private static ThreadFactory downloadMusicThreadFactory = new ThreadFactoryBuilder().setNameFormat("download-music-pool-%d").build();
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            3,
            10,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(3),
            downloadMusicThreadFactory,
            new ThreadPoolExecutor.AbortPolicy());

    public static void downloadMusicToFile(String musicUrl, String dir, String filename) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fileOutputStream = null;
                BufferedInputStream bufferedInputStream = null;
                HttpURLConnection httpUrlConnection = null;
                URL url = null;
                byte[] buf = new byte[4096];
                int size = 0;
                try {
                    // 建立链接
                    url = new URL(musicUrl);
                    httpUrlConnection = (HttpURLConnection) url.openConnection();
                    // 链接指定资源
                    httpUrlConnection.connect();
                    // 获取网络输入流
                    bufferedInputStream = new BufferedInputStream(httpUrlConnection.getInputStream());
                    // 创建文件输入流，用于保存下载的文件
                    fileOutputStream = new FileOutputStream(SD_CARD_MUSIC_PATH + filename);
                    // 从链接资源中读取数据到文件中，读取方式为每次读取4个字节
                    while((size = bufferedInputStream.read(buf)) != -1)  {
                        fileOutputStream.write(buf, 0, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 关闭资源
                    try {
                        assert fileOutputStream != null;
                        fileOutputStream.close();
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    httpUrlConnection.disconnect();
                }
            }
        });
    }
}
