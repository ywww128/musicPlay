package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplayer.entity.Song;
import com.example.musicplayer.util.SongPlayingUtils;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.wcy.lrcview.LrcView;

/**
 * @author zjw
 *
 */
public class SongPlayingActivity extends AppCompatActivity {
    private static ArrayList<Song> songs = new ArrayList<>();
    private static Song currentSong;                // 当前播放的音乐
    private Song newSong;                           // 新歌曲
    private Intent intent;                          // 传递数据

    private Timer timer;                            // 时钟
    private TimerTask timerTask;                    // 任务

    private TitleBar titleBarReturn;                // 返回

    private CircleImageView circleImageView;        // 圆形框图片
    private ObjectAnimator rotationAnimator;        // 动画效果

    private LrcView lrcView;                        // 歌词框

    private ShineButton shineButtonLove;            // 喜欢
    private ImageView ivDownload;                   // 下载
    private ImageView ivDialogue;                   // 评论
    private ImageView ivMore;                       // 更多

    private ImageView ivListeningMode;              // 播放模式
    private ImageView ivBack;                       // 上一曲
    private ImageView ivPlayMusic;                  // 播放
    private ImageView ivNext;                       // 下一曲
    private ImageView ivMusicList;                  // 播放列表

    private XUIListPopup musicListPopup;            // 播放列表弹窗

    private TextView tvMusicCurrentTime;            // 音乐播放时长
    private AppCompatSeekBar seekBarSongProgress;   // 音乐进度条
    private TextView tvMusicTotalTime;              // 音乐总时长

    private MediaPlayer mediaPlayer;                // 播放器
    private boolean isPlayMusic;                    // 音乐是否在播放
    private int playMode;                           // 播放模式，0-顺序，1-随机，2-循环

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_playing);

        initView();             // 界面初始化
        getSongByIntent();      // 从Intent中获取传递过来的歌曲
        updateSong();           // 更新歌曲播放
        initRotationAnimator(); // 图片旋转效果初始化
        setOnListener();        // 设置监听器
        initLrcView();          // 歌词框初始化
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("test", "onStart()is running...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("test", "onResume()is running...");
        getSongByIntent();      // 从Intent中获取传递过来的歌曲
        updateSong();           // 更新歌曲播放
        for(Song s: songs) {
            Log.i("song", String.valueOf(s.id));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("test", "onPause()is running...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("test", "onStop()is running...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("test", "onRestart()is running...");
    }

    /**
     * 从Intent中获取传输过来的信息
     */
    private void getSongByIntent() {
        intent = getIntent();
        Bundle bundle = intent.getBundleExtra("newSong");
        if(bundle != null) {
            newSong = (Song) bundle.get("song");
        }
        Log.i("song", "newSong id:" + String.valueOf(newSong.id));
    }

    /**
     * 这里因为将SongPlayingActivity设置为SingleTask模式，Intent传值会有问题，
     * 使用onNewIntent方法，在该方法里面接收intent
     * 该生命周期位置为restart()→onNewIntent()→start()→resume()
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.i("test", "onNewIntent() is running....");
    }

    /**
     * 单击监听器
     */
    private class MClick implements View.OnClickListener {

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.title_bar_return:     // 返回
                    Intent intent = new Intent(SongPlayingActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.shine_button_love:    // 喜欢
                    shineButtonLove.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_love_red));
                    break;
                case R.id.iv_download:          // 下载
                    System.out.println("download");
                    break;
                case R.id.iv_dialogue:          // 评论
                    System.out.println("dialogue");
                    break;
                case R.id.iv_more:              // 更多
                    System.out.println("more");
                    break;
                case R.id.iv_listening_mode:    // 播放模式
                    playMode = (playMode + 1) % 3;
                    switch (playMode) {
                        case 0:     // 顺序播放
                            ivListeningMode.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_transfer));
                            break;
                        case 1:     // 随机播放
                            ivListeningMode.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_shuffle));
                            break;
                        case 2:     // 单曲循环
                            ivListeningMode.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_loop));
                            break;
                        default:
                            break;
                    }
                    break;
                case R.id.iv_back:              // 上一曲
                    newSong = SongPlayingUtils.getPreSong(songs, currentSong);
                    updateSong();
                    for(Song song: songs) {
                        Log.i("back", String.valueOf(song.id));
                    }
                    break;
                case R.id.iv_play_music:        // 播放
                    if(!isPlayMusic) {
                        play();
                    } else {
                        pause();
                    }
                    break;
                case R.id.iv_next:              // 下一曲
                    newSong = SongPlayingUtils.getNextSong(songs, currentSong);
                    updateSong();
                    break;
                case R.id.iv_music_list:        // 播放列表
                    System.out.println("music list");
                    initListPopupIfNeed();
                    musicListPopup.setAnimStyle(XUIPopup.ANIM_AUTO);
                    musicListPopup.setPreferredDirection(XUIPopup.DIRECTION_TOP);
                    musicListPopup.show(v);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 进度条监听器
     */
    private class MSeekBarChange implements SeekBar.OnSeekBarChangeListener {
        /**
         * 进度条改变时调用
         * @param seekBar 拖动条
         * @param progress 进度
         * @param fromUser true/false
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tvMusicCurrentTime.setText(SongPlayingUtils.convertTime(mediaPlayer.getCurrentPosition()));
        }

        /**
         * 进度条开始拖动时调用
         * @param seekBar 拖动条
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        /**
         * 进度条停止拖动时调用
         * @param seekBar 拖动条
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int position = seekBarSongProgress.getProgress();
            tvMusicCurrentTime.setText(SongPlayingUtils.convertTime(position));
            mediaPlayer.seekTo(position);
            lrcView.updateTime(position);
        }
    }

    /**
     * 播放歌曲
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void play() {
        isPlayMusic = true;         // 标识音乐正在播放
        mediaPlayer.start();        // 开启音乐
        rotationAnimator.resume();  // 重启旋转动画
        // 设置中间按钮图片为暂停
        ivPlayMusic.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_suspend));
    }

    /**
     * 暂停歌曲
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void pause() {
        isPlayMusic = false;        // 标识音乐已经暂停
        mediaPlayer.pause();        // 暂停音乐
        rotationAnimator.pause();   // 暂停旋转动画
        // 设置中间按钮图片为播放
        ivPlayMusic.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_play));
    }

    /**
     * 更新歌曲，即根据歌曲id等信息判断是否换下一首歌
     */
    private void updateSong() {
        // 先判断新传入的歌曲是否与当前播放的音乐是同一首歌，如果不是则进入
        if(currentSong == null || newSong != null && newSong.id != currentSong.id) {
            currentSong = newSong;  // 更新当前歌曲为新音乐
            if(!SongPlayingUtils.isNewSongInSongs(songs, newSong)) {
                songs.add(newSong);     // 如果新歌曲不在播放列表中，则将新歌曲加入到播放列表中
            }
            try {
                if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    // 歌曲状态重置，将进度条以及歌词进度等重置为0
                    seekBarSongProgress.setProgress(0);
                    tvMusicCurrentTime.setText(SongPlayingUtils.convertTime(0));
                    lrcView.updateTime(0);
                }
                assert mediaPlayer != null;
                mediaPlayer.reset();
                if(currentSong != null) {
                    mediaPlayer.setDataSource(currentSong.rUrl);
                    titleBarReturn.setTitle(SongPlayingUtils.getSongTitle(currentSong));
                }
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        seekBarSongProgress.setMax(mediaPlayer.getDuration());
                        tvMusicTotalTime.setText(SongPlayingUtils.convertTime(mediaPlayer.getDuration()));
                        play();
                    }
                });
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置时钟，调整当
     */
    private void setTimer() {
        // 时钟，定时执行音乐进度条调整任务
        timer = new Timer();
        timerTask = new TimerTask() {   // 待完成的任务
            @Override
            public void run() {
                if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                    // 获取当前进度
                    int progress = mediaPlayer.getCurrentPosition();
                    seekBarSongProgress.setProgress(progress);
                    lrcView.updateTime(progress);

                    if(mediaPlayer.getDuration() - progress <= 1000) {
                        // 根据播放模式更新下一首歌
                        switch (playMode) {
                            case 0:     // 顺序播放
                                newSong = SongPlayingUtils.getNextSong(songs, currentSong);
                                if(newSong == currentSong) {
                                    mediaPlayer.seekTo(0);
                                } else {
                                    updateSong();
                                }
                                break;
                            case 1:     // 随机播放
                                newSong = SongPlayingUtils.getRandomSong(songs);
                                if(newSong == currentSong) {
                                    mediaPlayer.seekTo(0);
                                } else {
                                    updateSong();
                                }
                                break;
                            case 2:     // 循环播放
                                mediaPlayer.seekTo(0);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);  // 表示500毫秒后每隔500毫秒执行一次timerTask
    }

    /**
     * 当需要列表弹窗时初始化弹窗界面
     */
    private void initListPopupIfNeed() {
        String[] songsInformation = SongPlayingUtils.songArrayListToArray(songs);
        XUISimpleAdapter adapter = XUISimpleAdapter.create(this, songsInformation);
        musicListPopup = new XUIListPopup(this, adapter);
        // 设置列表弹窗监听器，当其中的item被单击时触发
        musicListPopup.create(1000, 500, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newSong = songs.get((int) id);
                updateSong();
            }
        });
    }

    /**
     * 歌词模块初始化
     */
    private void initLrcView() {
        String lrcStr = SongPlayingUtils.getLrcStrFromLrcFile(this,"lrc/wenquan.lrc");
        lrcView.loadLrc(lrcStr);
        lrcView.updateTime(0);
        lrcView.setDraggable(true, new LrcView.OnPlayClickListener() {
            @Override
            public boolean onPlayClick(LrcView view, long time) {
                view.updateTime(time);
                seekBarSongProgress.setProgress((int)time);
                mediaPlayer.seekTo((int)time);
                if(!isPlayMusic) {
                    play();
                }
                return true;
            }
        });
    }

    /**
     * 图片旋转效果初始化
     */
    private void initRotationAnimator() {
        // 设置旋转动画，旋转角度为一周，360度
        rotationAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0.0f, 360.f);
        rotationAnimator.setDuration(3000);                             // 设定旋转一圈的时间
        rotationAnimator.setRepeatCount(Animation.INFINITE);            // 设定无限循环
        rotationAnimator.setRepeatMode(ObjectAnimator.RESTART);         // 设定循环模式
        rotationAnimator.setInterpolator(new LinearInterpolator());     // 匀速
        rotationAnimator.start();                                       // 开始旋转动画
        rotationAnimator.pause();                                       // 暂停旋转动画
    }

    /**
     * 界面初始化
     */
    private void initView() {
        playMode = 0;   // 1表示初始播放模式为顺序播放

        titleBarReturn = findViewById(R.id.title_bar_return);

        circleImageView = findViewById(R.id.img_circle_disc);

        lrcView = findViewById(R.id.lrc_view);

        shineButtonLove = findViewById(R.id.shine_button_love);
        ivDownload = findViewById(R.id.iv_download);
        ivDialogue = findViewById(R.id.iv_dialogue);
        ivMore = findViewById(R.id.iv_more);

        tvMusicCurrentTime = findViewById(R.id.tv_music_current_time);
        seekBarSongProgress = findViewById(R.id.seek_bar_song_progress);
        tvMusicTotalTime = findViewById(R.id.tv_music_total_time);

        ivListeningMode = findViewById(R.id.iv_listening_mode);
        ivBack = findViewById(R.id.iv_back);
        ivPlayMusic = findViewById(R.id.iv_play_music);
        ivNext = findViewById(R.id.iv_next);
        ivMusicList = findViewById(R.id.iv_music_list);

        mediaPlayer = new MediaPlayer();

        setTimer();
    }

    /**
     * 监听器设置
     */
    private void setOnListener() {
        MClick mClick = new MClick();
        titleBarReturn.setOnClickListener(mClick);

        shineButtonLove.setOnClickListener(mClick);
        ivDownload.setOnClickListener(mClick);
        ivDialogue.setOnClickListener(mClick);
        ivMore.setOnClickListener(mClick);

        // 进度条监听器
        seekBarSongProgress.setOnSeekBarChangeListener(new MSeekBarChange());

        ivListeningMode.setOnClickListener(mClick);
        ivBack.setOnClickListener(mClick);
        ivPlayMusic.setOnClickListener(mClick);
        ivNext.setOnClickListener(mClick);
        ivMusicList.setOnClickListener(mClick);
    }
}