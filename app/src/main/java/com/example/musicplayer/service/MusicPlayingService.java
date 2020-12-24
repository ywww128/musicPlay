package com.example.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * @author zjw
 * @date 2020/12/5 15:40
 * @ClassName MusicPlayingService
 */
public class MusicPlayingService extends Service {
    public final IBinder iBinder = new MyBinder();
    public static MediaPlayer mediaPlayer = new MediaPlayer();

    public class MyBinder extends Binder {
        MusicPlayingService getService() {
            return MusicPlayingService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public void startMusic() {

    }

}
