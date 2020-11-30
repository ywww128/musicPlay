package com.example.musicplayer.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.bean.PlaySongData;
import com.example.musicplayer.util.DownloadUtils;
import com.example.musicplayer.util.FileUtils;
import com.example.musicplayer.util.SongPlayingUtils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import me.wcy.lrcview.LrcView;

import static android.os.Environment.DIRECTORY_MUSIC;

/**
 * @author zjw
 */
public class SongPlayingFragment extends Fragment {
    private final static int ZERO = 0;
    private final static int THOUSAND = 1000;
    private View view;

    private static ArrayList<PlaySongData> songs = new ArrayList<>();
    private static PlaySongData currentSong;
    private PlaySongData newSong;
    private TitleBar titleBarReturn;

    private CircleImageView circleImageView;
    private ObjectAnimator rotationAnimator;
    private LrcView lrcView;

    private ShineButton shineButtonLove;
    private ImageView ivDownload;
    private ImageView ivDialogue;
    private ImageView ivMore;

    private TextView tvMusicCurrentTime;
    private AppCompatSeekBar seekBarSongProgress;
    private TextView tvMusicTotalTime;

    private ImageView ivListeningMode;
    private ImageView ivBack;
    private ImageView ivPlayMusic;
    private ImageView ivNext;
    private ImageView ivMusicList;
    private XUIListPopup musicListPopup;

    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean isPlayMusic;

    /**
     * 播放模式，0-顺序，1-随机，2-循环
     */
    private static int playMode = 0;
    private static boolean isFirst = true;

    public void setNewSong(PlaySongData playSongData) {
        this.newSong = playSongData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("sdPath", FileUtils.getDiskFileDir(getContext()));

        Log.i("viewTest", "执行了一次onCreateView()...");
        view = inflater.inflate(R.layout.fragment_song_playing, container, false);
        initView();
        setOnListener();
        initRotationAnimator();
        setTimer();
        // 如果新传入的歌和正在播放的歌曲相同，或者新传入的歌曲id号为-1（表示继续播放原歌曲），则恢复现场
        boolean isRestoreSite = (currentSong != null && currentSong.getId() == newSong.getId()) || newSong.getId() == -1;
        if(isFirst && newSong.getId() == -1) {
            // 使用场景：打开音乐软件，直接点开播放界面时，且重复点击
        } else if(isRestoreSite) {
            // 恢复现场
            seekBarSongProgress.setMax(mediaPlayer.getDuration());
            seekBarSongProgress.setProgress(mediaPlayer.getCurrentPosition());
            tvMusicCurrentTime.setText(SongPlayingUtils.convertTime(mediaPlayer.getCurrentPosition()));
            tvMusicTotalTime.setText(SongPlayingUtils.convertTime(mediaPlayer.getDuration()));
            titleBarReturn.setTitle(SongPlayingUtils.getSongTitle(currentSong));
            lrcView.loadLrc(currentSong.getLrc());
            lrcView.updateTime(mediaPlayer.getCurrentPosition());
            play();
        } else {
            // 有新歌曲传入，将isFirst置为false，后面就不会再进入第一个if判断
            isFirst = false;
            updateSong();
        }

        return view;
    }

    private void initView() {
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
        changePlayModeDrawable();
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

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i("completion", "播放完成。。。");
                playNextSongByMode();
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
                    DownloadUtils.downloadMusicToFile(currentSong.getUrl(), currentSong, getContext());
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
                    changePlayModeDrawable();
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
                Log.i("playNextSong", "执行成功位置2...");
                assert mediaPlayer != null;
                mediaPlayer.reset();

                mediaPlayer.setDataSource(currentSong.getUrl());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.i("playNextSong", "执行成功位置3...");
                        // 设置歌词为当前歌曲的歌词
                        lrcView.loadLrc(currentSong.getLrc());
                        lrcView.updateTime(ZERO);
                        tvMusicTotalTime.setText(SongPlayingUtils.convertTime(mediaPlayer.getDuration()));
                        Log.i("playNextSong", SongPlayingUtils.getSongTitle(currentSong));
                        titleBarReturn.setTitle(SongPlayingUtils.getSongTitle(currentSong));
                        seekBarSongProgress.setMax(mediaPlayer.getDuration());
                        play();
                    }
                });
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据音乐播放模式，选择下一首歌并播放
     */
    private void playNextSongByMode() {
        Log.i("playNextSong", "执行成功位置1...");
        // 根据播放模式更新下一首歌
        switch (playMode) {
            // 顺序播放
            case 0:
                newSong = SongPlayingUtils.getNextSong(songs, currentSong);
                assert newSong != null;
                if(newSong.getId() == currentSong.getId()) {
                    mediaPlayer.seekTo(ZERO);
                    play();
                } else {
                    updateSong();
                }
                break;
            // 随机播放
            case 1:
                newSong = SongPlayingUtils.getRandomSong(songs);
                if(newSong.getId() == currentSong.getId()) {
                    mediaPlayer.seekTo(ZERO);
                    play();
                } else {
                    updateSong();
                }
                break;
            // 循环播放
            case 2:
                mediaPlayer.seekTo(ZERO);
                play();
                break;
            default:
                break;
        }
    }

    /**
     * 设置时钟，每秒执行一次，用于更新进度条和歌词进度等信息
     */
    private void setTimer() {
        // 线程工厂
        ThreadFactory myThreadFactory = new ThreadFactoryBuilder().setNameFormat("seekBarRunning-pool-%d").build();
        // 线程池
        ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1, myThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        // 设置线程任务为每秒执行一次
        pool.scheduleAtFixedRate(() -> {
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                // 获取当前进度
                int progress = mediaPlayer.getCurrentPosition();
                seekBarSongProgress.setProgress(progress);
                lrcView.updateTime(progress);
            }
        }, ZERO, THOUSAND, TimeUnit.MILLISECONDS);
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
                // musicListPopup.dismiss();
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

    /**
     * 根据模式值，修改播放模式的图片
     */
    private void changePlayModeDrawable() {
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
    }
}