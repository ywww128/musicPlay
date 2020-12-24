package com.example.musicplayer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.bean.PlaySongData;
import com.example.musicplayer.util.SongPlayingUtils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zjw
 */
public class MusicPlayingService extends Service {
    private final static int ZERO = 0;
    private final static int THOUSAND = 1000;

    private static MediaPlayer player = new MediaPlayer();
    private final IBinder iBinder = new MyBinder();
    private static ArrayList<PlaySongData> songs = new ArrayList<>();
    private PlaySongData currentSong;
    private int songIndex = 0;
    /**
     * 播放模式，0-顺序，1-随机，2-循环
     */
    private int playMode = 0;

    private NotificationManager notificationManager;
    private Notification notification = null;
    private RemoteViews remoteView;

    /**
     * 线程工厂
     */
    ThreadFactory myThreadFactory = new ThreadFactoryBuilder().setNameFormat("music-playing-service-pool-%d").build();

    public MusicPlayingService() {
        Log.i("helloService", "001");

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("currentSong", currentSong.getUrl());
                mp.start();
            }
        });

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                switch(playMode) {
                    case 0:
                        playNextSong();
                        break;
                    case 1:
                        // 播放随机的一首歌
                        Random random = new Random();
                        songIndex = random.nextInt(songs.size());
                        prepareMusic();
                        break;
                    case 2:
                        player.seekTo(0);
                        startOrPauseMusic();
                        break;
                    default:
                        break;
                }
            }
        });

        // MediaPlayer中onCompletion和onError的坑:https://blog.csdn.net/wgheng2011/article/details/72725381
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
    }

    public class MyBinder extends Binder {
        public MusicPlayingService getService() {
            return MusicPlayingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        initNotificationBar();

        Log.i("helloService", "002");
        setTimer();
        Bundle bundle = intent.getBundleExtra("newSong");
        if(bundle != null) {
            PlaySongData newSong = (PlaySongData) bundle.get("newSong");
            if(!SongPlayingUtils.isNewSongInSongs(songs, newSong)) {
                songs.add(newSong);
                songIndex = songs.size() - 1;
                prepareMusic();
            }
        }
        return iBinder;
    }

    /**
     * 播放或暂停歌曲
     * @return 此时播放状态
     */
    public int startOrPauseMusic() {
        if(!player.isPlaying()) {
            player.start();
            return 1;
        } else {
            player.pause();
            return 0;
        }
    }

    /**
     * 销毁player
     */
    public void stopMusic() {
        if(player != null) {
            player.stop();
            player.release();
        }
    }

    /**
     * 播放夏一首歌曲
     */
    public void playNextSong() {
        if(songs.size() != 0) {
            songIndex = (++songIndex) % songs.size();
        }

        Log.i("songIndex", String.valueOf(songIndex));
        prepareMusic();
    }

    /**
     * 播放上一首歌曲
     */
    public void playPreSong() {
        if(songs.size() != 0) {
            songIndex = (--songIndex + songs.size()) % songs.size();
        }
        Log.i("songIndex", String.valueOf(songIndex));
        prepareMusic();
    }

    /**
     * 播放进度定位到position进度的位置
     * @param position 进度
     */
    public void seekTo(int position) {
        player.seekTo(position);
    }

    /**
     * 准备播放音乐
     */
    private void prepareMusic() {
        if(songs.size() != 0) {
            player.reset();
            currentSong = songs.get(songIndex);
            Log.i("currentSong1", currentSong.getName() + ":" + currentSong.getUrl());
            try {
                if(currentSong != null) {
                    player.setDataSource(currentSong.getUrl());
                    player.prepareAsync();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取songs歌曲列表
     * @return 歌曲列表
     */
    public ArrayList<PlaySongData> getSongs() {
        return songs;
    }

    /**
     * 往songs列表中添加歌曲并播放
     * @param newSong 新歌曲
     */
    public void addSong(PlaySongData newSong) {
        if(!SongPlayingUtils.isNewSongInSongs(songs, newSong)) {
            songs.add(newSong);
            songIndex = songs.size() - 1;
            prepareMusic();
        } else if(newSong.getId() != currentSong.getId()) {
            for(int i = 0; i < songs.size(); ++i) {
                if(songs.get(i).getId() == newSong.getId()) {
                    songIndex = i;
                    prepareMusic();
                    break;
                }
            }
        }
    }

    /**
     * 通过给定的下标播放对应歌曲
     * @param index 下标值
     */
    public void playSongByIndex(int index) {
        songIndex = index;
        prepareMusic();
    }

    public PlaySongData getCurrentSong() {
        return currentSong;
    }

    /**
     * 设置播放模式的值
     * @param playMode 播放模式，0-顺序，1-随机，2-循环
     */
    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    /**
     * 设置时钟，每秒执行一次，用于更新进度条和歌词进度等信息
     */
    private void setTimer() {
        // 线程池
        ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1, myThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        // 设置线程任务为每秒执行一次
        pool.scheduleAtFixedRate(() -> {
            // 更新通知栏信息
            if(notificationManager != null && notification != null) {
                if(player.isPlaying()) {
                    remoteView.setImageViewResource(R.id.iv_play_song, R.drawable.song_playing_suspend);
                } else {
                    remoteView.setImageViewResource(R.id.iv_play_song, R.drawable.song_playing_play);
                }
                if(currentSong != null) {
                    remoteView.setTextViewText(R.id.tv_notification_title, currentSong.getName());
                    remoteView.setTextViewText(R.id.tv_notification_artists, SongPlayingUtils.connectSingerNameWithAndSymbol(currentSong.getArtists()));
                }
                notificationManager.notify(0, notification);
            }

            // 获取当前正在播放歌曲的总时长和当前进度值
            Intent intent = new Intent("UPDATE_MUSIC_PLAYING_FRAGMENT");
            Bundle bundle = new Bundle();

            if(currentSong != null) {
                String lrc = currentSong.getLrc() == null ? "" : currentSong.getLrc();
                bundle.putString("titleName", SongPlayingUtils.getSongTitle(currentSong));
                bundle.putString("lrc", lrc);
            }
            bundle.putBoolean("isPlaying", player.isPlaying());
            intent.putExtra("viewBundle", bundle);
            int duration = player.getDuration();
            int currentPosition = player.getCurrentPosition();
            bundle.putInt("duration", duration);
            bundle.putInt("currentPosition", currentPosition);

            intent.putExtra("progressBundle", bundle);
            sendBroadcast(intent);
        }, ZERO, 200, TimeUnit.MILLISECONDS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String buttonIndex = null;
        if(intent != null) {
            buttonIndex = intent.getStringExtra("buttonIndex");
        }
        if(buttonIndex != null) {
            if("1".equals(buttonIndex)) {
                Log.i("onStartCommand", "pre...");
                playPreSong();
                if(currentSong != null) {
                    remoteView.setTextViewText(R.id.tv_notification_title, currentSong.getName());
                    remoteView.setTextViewText(R.id.tv_notification_artists, SongPlayingUtils.connectSingerNameWithAndSymbol(currentSong.getArtists()));
                }
            } else if("2".equals(buttonIndex)) {
                Log.i("onStartCommand", "play...");
                if(startOrPauseMusic() == 1) {
                    remoteView.setImageViewResource(R.id.iv_play_song, R.drawable.song_playing_suspend);
                } else {
                    remoteView.setImageViewResource(R.id.iv_play_song, R.drawable.song_playing_play);
                }
            } else {
                Log.i("onStartCommand", "next...");
                playNextSong();
                if(currentSong != null) {
                    remoteView.setTextViewText(R.id.tv_notification_title, currentSong.getName());
                    remoteView.setTextViewText(R.id.tv_notification_artists, SongPlayingUtils.connectSingerNameWithAndSymbol(currentSong.getArtists()));
                }
            }
            notificationManager.notify(0, notification);
        }


        return super.onStartCommand(intent, flags, startId);
    }

    private void initNotificationBar() {
        // 1.获得一个通知管理器
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 2. 建立一个通知  Android 8.0后，需要自建通知通道

        remoteView = new RemoteViews(getPackageName(), R.layout.notification_bar_music);

        if(currentSong != null) {
            remoteView.setCharSequence(R.id.tv_notification_title, "setText", currentSong.getName());
            remoteView.setCharSequence(R.id.tv_notification_artists, "setText", SongPlayingUtils.connectSingerNameWithAndSymbol(currentSong.getArtists()));
        }

        Intent actionIntent = new Intent(this, MusicPlayingService.class);
        actionIntent.putExtra("buttonIndex", "1");
        PendingIntent pendingIntentPreSong = PendingIntent.getService(this, 2, actionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.iv_pre_song, pendingIntentPreSong);

        actionIntent.putExtra("buttonIndex", "2");
        PendingIntent pendingIntentPlaySong = PendingIntent.getService(this, 3, actionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.iv_play_song, pendingIntentPlaySong);

        actionIntent.putExtra("buttonIndex", "3");
        PendingIntent pendingIntentNextSong = PendingIntent.getService(this, 1, actionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.iv_next_song, pendingIntentNextSong);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        remoteView.setOnClickPendingIntent(R.id.iv_notification_picture, pendingIntent);

        // if：安卓版本大于8.0   else if：安卓版本大于4.1
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String id = "mChannel";
            String name = "通道1";
            // 自建通道
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            // 通道和管理器相连接（建立关系），可以协同运行
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this, id)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.pic_icon_audio))
                    .setSmallIcon(R.drawable.pic_icon_audio)
                    .setContent(remoteView)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)
                    .build();
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(this)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.pic_icon_audio))
                    .setSmallIcon(R.drawable.pic_icon_audio)
                    .setContent(remoteView)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)
                    .build();
        }
        // 3. 发送通知
        notificationManager.notify(0, notification);
    }
}
