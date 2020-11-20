package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.entity.MusicInfo;
import com.example.musicplayer.util.MusicUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.wcy.lrcview.LrcView;

/**
 * @author zjw
 * https://www.zybuluo.com/tianyu-211/note/612033
 */
public class SongPlayingActivity extends AppCompatActivity implements
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {
    private MusicInfo musicInfo;        // 音乐信息

    private TitleBar titleBarReturn;    // 返回

    private ShineButton ivLove;         // 喜欢

    private ImageView ivListeningMode;  // 播放模式
    private ImageView ivPlayMusic;      // 音乐播放

    private MediaPlayer mediaPlayer;    // 播放器
    private boolean isPlayMusic;        // 音乐是否在播放

    private CircleImageView circleImageView;    // 圆形框图片
    private ObjectAnimator rotationAnimator;           // 动画效果

    private AppCompatSeekBar seekBarSongProgress;  // 音乐进度条

    // 计时器以及任务
    private Timer timer;
    private TimerTask timerTask;

    private TextView tvMusicCurrentTime;    // 音乐播放时长
    private TextView tvMusicTotalTime;      // 音乐总时长

    private LrcView lrcView;  // 歌词框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_playing);
        // 图片旋转效果初始化
        initRotationAnimator();
        initView();
    }

    private void initView() {
        ivLove  = findViewById(R.id.shine_button_love);
        ivLove.setOnClickListener(new MClick());
        ivListeningMode = findViewById(R.id.iv_listening_mode);
        ivListeningMode.setOnClickListener(new MClick());
        titleBarReturn = findViewById(R.id.title_bar_return);
        titleBarReturn.setOnClickListener(new MClick());
        ivPlayMusic = findViewById(R.id.iv_play_music);
        ivPlayMusic.setOnClickListener(new MClick());

        // 音乐播放器调用，以及音乐信息
        mediaPlayer = MediaPlayer.create(this, R.raw.wenquan);
//        try{
//            mediaPlayer.setDataSource("http://www.ytmp3.cn/down/57799.mp3");
//            mediaPlayer.prepareAsync();
//        } catch(Exception e) {
//
//        }

        mediaPlayer.seekTo(0);
        musicInfo = new MusicInfo();
        musicInfo.setName("wenquan");
        musicInfo.setArtist("许嵩");
        musicInfo.setDuration(mediaPlayer.getDuration());

        // 时钟，定时执行音乐进度条调整任务
        timer = new Timer();
        timerTask = new TimerTask() {   // 待完成的任务
            @Override
            public void run() {
                if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                    // 获取当前进度
                    int progress = mediaPlayer.getCurrentPosition();
                    seekBarSongProgress.setProgress(progress);
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);  // 表示500毫秒后每隔500毫秒执行一次timerTask


        tvMusicCurrentTime = findViewById(R.id.tv_music_current_time);
        tvMusicTotalTime = findViewById(R.id.tv_music_total_time);
        tvMusicTotalTime.setText(MusicUtils.convertTime(musicInfo.getDuration()));  // 设置歌曲总时间标签值

        seekBarSongProgress = findViewById(R.id.seek_bar_song_progress);
        seekBarSongProgress.setMax(mediaPlayer.getDuration());  // 设置进度条的最大值为音乐总时长
        seekBarSongProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 进度条改变时调用
             * @param seekBar
             * @param progress
             * @param fromUser
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMusicCurrentTime.setText(MusicUtils.convertTime(mediaPlayer.getCurrentPosition()));
                if(progress >= seekBarSongProgress.getMax()) {
                    mediaPlayer.pause();
                    rotationAnimator.pause();      // 暂停旋转动画
                    // 设置中间按钮图片为播放
                    ivPlayMusic.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_play));
                }
            }

            /**
             * 进度条开始拖动时调用
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * 进度条停止拖动时调用
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int position = seekBarSongProgress.getProgress();
                tvMusicCurrentTime.setText(MusicUtils.convertTime(position));
                mediaPlayer.seekTo(position);
            }
        });
    }

//    private void initLrcView() {
//        lrcView = findViewById(R.id.lrc_view);
//        lrcView.loadLrc(R.raw.);
//    }


    /**
     * 图片旋转效果初始化
     */
    private void initRotationAnimator() {
        circleImageView = findViewById(R.id.img_circle_disc);
        // 设置旋转动画，旋转角度为一周，360度
        rotationAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0.0f, 360.f);
        rotationAnimator.setDuration(3000);    // 设定旋转一圈的时间
        rotationAnimator.setRepeatCount(Animation.INFINITE);           // 设定无限循环
        rotationAnimator.setRepeatMode(ObjectAnimator.RESTART);        // 设定循环模式
        rotationAnimator.setInterpolator(new LinearInterpolator());    // 匀速
        rotationAnimator.start();  // 开始旋转动画
        rotationAnimator.pause();  // 暂停旋转动画
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        tvMusicCurrentTime.setText("" + percent + "%");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Toast.makeText(this, "onPrepared called", Toast.LENGTH_SHORT).show();
    }

    private class MClick implements View.OnClickListener {

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.shine_button_love:    // 是否喜欢歌曲的切换
                    ivLove.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_love_red));
                    break;
                case R.id.iv_listening_mode:    // 播放模式的切换
                    ivListeningMode.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_transfer));
                    break;
                case R.id.title_bar_return:     // 返回上一页
                    Intent intent = new Intent(SongPlayingActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_play_music:        // 音乐播放
                    if(!isPlayMusic) {  // 音乐未在播放
                        isPlayMusic = true;
                        mediaPlayer.start();    // 开启音乐
                        rotationAnimator.resume();     // 重启旋转动画
                        // 设置中间按钮图片为暂停
                        ivPlayMusic.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_suspend));
                    } else {
                        isPlayMusic = false;
                        mediaPlayer.pause();    // 暂停音乐
                        rotationAnimator.pause();      // 暂停旋转动画
                        // 设置中间按钮图片为播放
                        ivPlayMusic.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_play));
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}