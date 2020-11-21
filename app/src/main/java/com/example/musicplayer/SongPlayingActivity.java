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

import com.example.musicplayer.entity.MusicInfo;
import com.example.musicplayer.util.MusicUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.wcy.lrcview.LrcView;

/**
 * @author zjw
 * https://www.zybuluo.com/tianyu-211/note/612033
 */
public class SongPlayingActivity extends AppCompatActivity {
    private MusicInfo musicInfo;        // 音乐信息

    // 计时器以及任务
    private Timer timer;
    private TimerTask timerTask;

    private TitleBar titleBarReturn;    // 返回

    private CircleImageView circleImageView;    // 圆形框图片
    private ObjectAnimator rotationAnimator;    // 动画效果

    private LrcView lrcView;  // 歌词框

    private ShineButton shineButtonLove;        // 喜欢
    private ImageView ivDownload;
    private ImageView ivDialogue;
    private ImageView ivMore;

    private ImageView ivListeningMode;  // 播放模式
    private ImageView ivBack;
    private ImageView ivPlayMusic;      // 音乐播放
    private ImageView ivNext;
    private ImageView ivMusicList;

    private TextView tvMusicCurrentTime;    // 音乐播放时长
    private AppCompatSeekBar seekBarSongProgress;  // 音乐进度条
    private TextView tvMusicTotalTime;      // 音乐总时长


    private MediaPlayer mediaPlayer;    // 播放器
    private boolean isPlayMusic;        // 音乐是否在播放

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_playing);
        // 图片旋转效果初始化
        initRotationAnimator();
        initView();
        initLrcView();
    }

    private void initView() {
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



        MClick mClick = new MClick();
        shineButtonLove.setOnClickListener(mClick);
        ivListeningMode.setOnClickListener(mClick);
        titleBarReturn.setOnClickListener(mClick);
        ivPlayMusic.setOnClickListener(mClick);

        // 音乐播放器调用，以及音乐信息
        mediaPlayer = MediaPlayer.create(this, R.raw.wenquan);
        mediaPlayer.seekTo(0);  // 歌曲加载完毕，定位到开始位置，等待播放
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
                    lrcView.updateTime(progress);
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);  // 表示500毫秒后每隔500毫秒执行一次timerTask



        tvMusicTotalTime.setText(MusicUtils.convertTime(musicInfo.getDuration()));  // 设置歌曲总时间标签值


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
                lrcView.updateTime(position);
            }
        });
    }

    private void initLrcView() {
        File file = new File("C:\\Users\\zjw\\Desktop\\wenquan.txt");
        lrcView.loadLrc("[00:00.000] 作词 : 许嵩\n" +
                "[00:01.000] 作曲 : 许嵩\n" +
                "[01:45.585][00:13.101]你用温柔手臂弯住迟钝的我\n" +
                "[01:50.437][00:18.065]蓝色披风鼓囊着半立方悸动\n" +
                "[01:55.287][00:22.933]骑行在泉水小镇下坡有点陡\n" +
                "[02:00.370][00:27.698]正好紧贴我背后\n" +
                "[02:05.503][00:32.931]和你在一起就会莫名的放松\n" +
                "[02:10.320][00:37.549]所有阴翳的回忆都一扫而空\n" +
                "[02:15.202][00:42.565]在深巷小馆品尝当地的果酒\n" +
                "[02:20.285][00:47.514]像你清新而醇厚\n" +
                "[02:24.289][00:54.199]喜欢看你的笑容\n" +
                "[02:26.636][00:56.566]绽放在夏日雨后\n" +
                "[02:29.069][00:58.999]你融化了多少的忧愁或许只有我能懂\n" +
                "[02:34.719][01:04.781]你轻轻的问我\n" +
                "[02:37.755][01:07.531]我有没有点重\n" +
                "[02:39.853][01:09.749]可抱住你就不想松手\n" +
                "[02:44.207][01:14.093]喜欢看你的笑容\n" +
                "[02:46.620][01:16.382]环绕着香甜的风\n" +
                "[02:49.119][01:19.116]你戳中了我的审美点准确得好像针灸\n" +
                "[02:54.737][01:24.748]我微微的颤抖\n" +
                "[02:57.103][01:26.983]那紧张的喉咙\n" +
                "[02:59.620][01:29.465]认真讲述了没做的梦\n" +
                "[03:04.304][01:34.182]很生动\n" +
                "[03:07.924]说实话我可不确定我能够陪你多久\n" +
                "[03:13.120]没关系能博红颜一笑是我的温柔\n" +
                "[03:17.786]等我们绕过几轮冬夏和春秋\n" +
                "[03:22.175]还能不能和你遨游\n" +
                "[03:29.320]喜欢看你的笑容\n" +
                "[03:31.370]绽放在夏日雨后\n" +
                "[03:33.870]你融化了多少的忧愁或许只有我能懂\n" +
                "[03:39.553]你轻轻的问我\n" +
                "[03:42.269]我有没有点重\n" +
                "[03:44.553]可抱住你就不想松手\n" +
                "[03:51.320]喜欢看你的笑容\n" +
                "[03:52.036]环绕着香甜的风\n" +
                "[03:54.087]有泡在丛林里的温泉神奇疗愈的作用\n" +
                "[03:59.687]我微微的颤抖\n" +
                "[04:02.070]那紧张的喉咙\n" +
                "[04:14.990][04:04.503]认真讲述了没做的梦\n" +
                "[04:20.591][04:08.974]很生动\n");
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
        rotationAnimator.setDuration(3000);    // 设定旋转一圈的时间
        rotationAnimator.setRepeatCount(Animation.INFINITE);           // 设定无限循环
        rotationAnimator.setRepeatMode(ObjectAnimator.RESTART);        // 设定循环模式
        rotationAnimator.setInterpolator(new LinearInterpolator());    // 匀速
        rotationAnimator.start();  // 开始旋转动画
        rotationAnimator.pause();  // 暂停旋转动画
    }

    private class MClick implements View.OnClickListener {

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.shine_button_love:    // 是否喜欢歌曲的切换
                    shineButtonLove.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_love_red));
                    break;
                case R.id.iv_listening_mode:    // 播放模式的切换
                    ivListeningMode.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_transfer));
                    break;
                case R.id.title_bar_return:     // 返回上一页
                    Intent intent = new Intent(SongPlayingActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_play_music:        // 音乐播放
                    if(!isPlayMusic) {
                        play();  // 播放
                    } else {
                        pause();  // 暂停
                    }
                    break;
            }
        }
    }


    /**
     * 播放歌曲
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void play() {
        isPlayMusic = true;
        mediaPlayer.start();    // 开启音乐
        rotationAnimator.resume();     // 重启旋转动画
        // 设置中间按钮图片为暂停
        ivPlayMusic.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_suspend));
    }


    /**
     * 暂停歌曲
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void pause() {
        isPlayMusic = false;
        mediaPlayer.pause();    // 暂停音乐
        rotationAnimator.pause();      // 暂停旋转动画
        // 设置中间按钮图片为播放
        ivPlayMusic.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_play));
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