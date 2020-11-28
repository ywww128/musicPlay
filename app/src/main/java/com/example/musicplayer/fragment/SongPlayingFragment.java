package com.example.musicplayer.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.bean.PlaySongData;
import com.example.musicplayer.util.SongPlayingUtils;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import me.wcy.lrcview.LrcView;

/**
 * @author zjw
 */
public class SongPlayingFragment extends Fragment {
    /**
     * 播放列表
     */
    private static ArrayList<PlaySongData> songs = new ArrayList<>();

    /**
     * 当前播放的音乐
     */
    private static PlaySongData currentSong;
    /**
     * 新歌曲
     */
    private PlaySongData newSong;
    /**
     * 传递数据
     */
    private Intent intent;

    /**
     * 返回标题
     */
    private TitleBar titleBarReturn;

    /**
     * 圆形框图片
     */
    private CircleImageView circleImageView;
    /**
     * 旋转动画效果
     */
    private ObjectAnimator rotationAnimator;

    /**
     * 歌词框
     */
    private LrcView lrcView;

    /**
     * 喜欢
     */
    private ShineButton shineButtonLove;
    /**
     * 下载
     */
    private ImageView ivDownload;
    /**
     * 评论
     */
    private ImageView ivDialogue;
    /**
     * 更多
     */
    private ImageView ivMore;

    /**
     * 播放模式
     */
    private ImageView ivListeningMode;
    /**
     * 上一曲
     */
    private ImageView ivBack;
    /**
     * 播放
     */
    private ImageView ivPlayMusic;
    /**
     * 下一曲
     */
    private ImageView ivNext;
    /**
     * 播放列表
     */
    private ImageView ivMusicList;

    /**
     * 播放列表弹窗
     */
    private XUIListPopup musicListPopup;

    /**
     * 音乐播放时长
     */
    private TextView tvMusicCurrentTime;
    /**
     * 音乐进度条
     */
    private AppCompatSeekBar seekBarSongProgress;
    /**
     * 音乐总时长
     */
    private TextView tvMusicTotalTime;

    /**
     * 播放器
     */
    private static MediaPlayer mediaPlayer = new MediaPlayer();;
    /**
     * 音乐是否在播放
     */
    private boolean isPlayMusic;
    /**
     * 播放模式，0-顺序，1-随机，2-循环
     */
    private int playMode;

    public void setNewSong(PlaySongData playSongData) {
        this.newSong = playSongData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_playing, container, false);
        initView(view);
        setOnListener();
        initRotationAnimator();
        setTimer();
        Log.i("123", "123");
        updateSong();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initView(View view) {
        // 1表示初始播放模式为顺序播放
        playMode = 0;

        titleBarReturn = view.findViewById(R.id.title_bar_return);

        circleImageView = view.findViewById(R.id.img_circle_disc);

        lrcView = view.findViewById(R.id.lrc_view);

        shineButtonLove = view.findViewById(R.id.shine_button_love);
        ivDownload = view.findViewById(R.id.iv_download);
        ivDialogue = view.findViewById(R.id.iv_dialogue);
        ivMore = view.findViewById(R.id.iv_more);

        tvMusicCurrentTime = view.findViewById(R.id.tv_music_current_time);
        seekBarSongProgress = view.findViewById(R.id.seek_bar_song_progress);
        tvMusicTotalTime = view.findViewById(R.id.tv_music_total_time);

        ivListeningMode = view.findViewById(R.id.iv_listening_mode);
        ivBack = view.findViewById(R.id.iv_back);
        ivPlayMusic = view.findViewById(R.id.iv_play_music);
        ivNext = view.findViewById(R.id.iv_next);
        ivMusicList = view.findViewById(R.id.iv_music_list);
    }

    /**
     * 监听器设置
     */
    private void setOnListener() {
        MyClick myClick = new MyClick();
        titleBarReturn.setOnClickListener(myClick);

        shineButtonLove.setOnClickListener(myClick);
        ivDownload.setOnClickListener(myClick);
        ivDialogue.setOnClickListener(myClick);
        ivMore.setOnClickListener(myClick);

        // 进度条监听器
        seekBarSongProgress.setOnSeekBarChangeListener(new MySeekBarChange());

        ivListeningMode.setOnClickListener(myClick);
        ivBack.setOnClickListener(myClick);
        ivPlayMusic.setOnClickListener(myClick);
        ivNext.setOnClickListener(myClick);
        ivMusicList.setOnClickListener(myClick);

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
     * 单击监听器
     */
    private class MyClick implements View.OnClickListener {

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.title_bar_return:     // 返回
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                    break;
                case R.id.shine_button_love:    // 喜欢

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
                        // 顺序播放
                        case 0:
                            ivListeningMode.setImageResource(R.drawable.song_playing_transfer);
                            break;
                        // 随机播放
                        case 1:
                            ivListeningMode.setImageResource(R.drawable.song_playing_shuffle);
                            break;
                        // 单曲循环
                        case 2:
                            ivListeningMode.setImageResource(R.drawable.song_playing_loop);
                            break;
                        default:
                            break;
                    }
                    break;
                case R.id.iv_back:              // 上一曲
                    newSong = SongPlayingUtils.getPreSong(songs, currentSong);
                    updateSong();
                    for(PlaySongData song: songs) {
                        Log.i("back", String.valueOf(song.getId()));
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
    private class MySeekBarChange implements SeekBar.OnSeekBarChangeListener {
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
            if(currentSong.getDuration() - position <= 1000) {
                playNextSong();
            }
        }
    }

    /**
     * 播放歌曲
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void play() {
        // 标识音乐正在播放
        isPlayMusic = true;
        // 开启音乐
        mediaPlayer.start();
        // 重启旋转动画
        rotationAnimator.resume();
        // 设置中间按钮图片为暂停
        ivPlayMusic.setImageResource(R.drawable.song_playing_suspend);
    }

    /**
     * 暂停歌曲
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void pause() {
        // 标识音乐已经暂停
        isPlayMusic = false;
        // 暂停音乐
        mediaPlayer.pause();
        // 暂停旋转动画
        rotationAnimator.pause();
        // 设置中间按钮图片为播放
        ivPlayMusic.setImageResource(R.drawable.song_playing_play);
    }

    /**
     * 更新歌曲，即根据歌曲id等信息判断是否换下一首歌
     */
    private void updateSong() {
        boolean isTrue = (currentSong == null || (newSong != null && newSong.getId() != currentSong.getId()));
        // 先判断新传入的歌曲是否与当前播放的音乐是同一首歌，如果不是则进入
        if(isTrue) {
            // 更新当前歌曲为新音乐
            currentSong = newSong;
            if(!SongPlayingUtils.isNewSongInSongs(songs, newSong)) {
                // 如果新歌曲不在播放列表中，则将新歌曲加入到播放列表中
                songs.add(newSong);
            }
            try {
                assert mediaPlayer != null;
                mediaPlayer.reset();
                mediaPlayer.setDataSource(currentSong.getUrl());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // 设置歌词为当前歌曲的歌词
                        lrcView.loadLrc(currentSong.getLrc());
                        lrcView.updateTime(0);
                        titleBarReturn.setTitle(SongPlayingUtils.getSongTitle(currentSong));
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

    private void playNextSong() {
        int zero =0;
        // 根据播放模式更新下一首歌
        switch (playMode) {
            // 顺序播放
            case 0:
                newSong = SongPlayingUtils.getNextSong(songs, currentSong);
                assert newSong != null;
                if(newSong.getId() == currentSong.getId()) {
                    mediaPlayer.seekTo(zero);
                } else {
                    updateSong();
                }
                break;
            // 随机播放
            case 1:
                newSong = SongPlayingUtils.getRandomSong(songs);
                if(newSong == currentSong) {
                    mediaPlayer.seekTo(zero);
                } else {
                    updateSong();
                }
                break;
            // 循环播放
            case 2:
                mediaPlayer.seekTo(zero);
                break;
            default:
                break;
        }
    }

    /**
     * 设置时钟，调整当
     */
    private void setTimer() {
        // 线程池
        ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1);
        // 设置线程任务为每秒执行一次
        pool.scheduleAtFixedRate(() -> {
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                // 获取当前进度
                int progress = mediaPlayer.getCurrentPosition();
                seekBarSongProgress.setProgress(progress);
                lrcView.updateTime(progress);
                int thousand = 1000;
                if(mediaPlayer.getDuration() - progress <= thousand) {
                    playNextSong();
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 当需要列表弹窗时初始化弹窗界面
     */
    private void initListPopupIfNeed() {
        String[] songsInformation = SongPlayingUtils.songArrayListToArray(songs);
        XUISimpleAdapter adapter = XUISimpleAdapter.create(getActivity(), songsInformation);
        musicListPopup = new XUIListPopup(Objects.requireNonNull(getActivity()), adapter);
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
     * 图片旋转效果初始化
     */
    private void initRotationAnimator() {
        // 设置旋转动画，旋转角度为一周，360度
        rotationAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0.0f, 360.f);
        // 设定旋转一圈的时间
        rotationAnimator.setDuration(3000);
        // 设定无限循环
        rotationAnimator.setRepeatCount(Animation.INFINITE);
        // 设定循环模式
        rotationAnimator.setRepeatMode(ObjectAnimator.RESTART);
        // 匀速
        rotationAnimator.setInterpolator(new LinearInterpolator());
        // 开始旋转动画
        rotationAnimator.start();
        // 暂停旋转动画
        rotationAnimator.pause();
    }
}