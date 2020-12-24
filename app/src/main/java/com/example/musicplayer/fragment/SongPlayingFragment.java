package com.example.musicplayer.fragment;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;
import android.os.IBinder;

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
import com.example.musicplayer.bean.PlaySongData;
import com.example.musicplayer.service.MusicPlayingService;
import com.example.musicplayer.util.DownloadUtils;
import com.example.musicplayer.util.SongPlayingUtils;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar;
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.wcy.lrcview.LrcView;

/**
 * @author zjw
 */
public class SongPlayingFragment extends Fragment implements
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        LrcView.OnPlayClickListener {
    private View view;

    private static MusicPlayingService mpService;

    private PlaySongData newSong;
    private TitleBar titleBar;

    private CircleImageView circleImageView;
    private ObjectAnimator rotationAnimator;
    private LrcView lrcView;

    private ImageView ivLove;
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

    /**
     * 播放模式，0-顺序，1-随机，2-循环
     */
    private static int playMode = 0;

    private MsgReceiver msgReceiver;

    public void setNewSong(PlaySongData playSongData) {
        this.newSong = playSongData;
    }

    /**
     * 监听应用程序服务状态（连接 or 断开）
     */
    ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.i("helloService", "003");
            mpService = ((MusicPlayingService.MyBinder) iBinder).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            Log.i("initView", "执行initView");
            view = inflater.inflate(R.layout.fragment_song_playing, container, false);
            init();
        }
        if(mpService == null) {
            Log.i("helloService", "000");
            Intent intent = new Intent(getActivity(), MusicPlayingService.class);
            if(newSong != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("newSong", newSong);
                intent.putExtra("newSong", bundle);
            }
            getActivity().bindService(intent, sc, Context.BIND_AUTO_CREATE);
        }
        if (mpService != null && newSong != null) {
            Log.i("helloService", "004");
            mpService.addSong(newSong);
            newSong = null;
        }
        return view;
    }

    /**
     * 界面初始化以及监听器设置
     */
    private void init() {
        // 界面初始化
        titleBar = view.findViewById(R.id.title_bar_return);
        circleImageView = view.findViewById(R.id.img_circle_disc);
        lrcView = view.findViewById(R.id.lrc_view);
        ivLove = view.findViewById(R.id.shine_button_love);
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

        // 监听器设置
        titleBar.setOnClickListener(this);
        ivLove.setOnClickListener(this);
        ivDownload.setOnClickListener(this);
        ivDialogue.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        ivListeningMode.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivPlayMusic.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivMusicList.setOnClickListener(this);
        seekBarSongProgress.setOnSeekBarChangeListener(this);
        lrcView.setDraggable(true, this);

        changePlayModeDrawable();

        // 旋转动画初始化
        initRotationAnimator();

        // 动态注册广播接收器
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("UPDATE_MUSIC_PLAYING_FRAGMENT");
        Objects.requireNonNull(getActivity()).registerReceiver(msgReceiver, intentFilter);
    }

    /**
     * 点击事件触发
     * @param v 点击的按钮
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.title_bar_return:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                break;
            case R.id.shine_button_love:
                System.out.println("shine_button_love");
                break;
            case R.id.iv_download:
                PlaySongData currentSong = mpService.getCurrentSong();
                if(currentSong != null) {
                    DownloadUtils.downloadMusicToFile(currentSong.getUrl(), currentSong, getContext(), getActivity());
                    CookieBar.builder(getActivity())
                            .setTitle("下载提示")
                            .setMessage("歌曲“" + currentSong.getName() + "”已经下载完成！")
                            .show();
                }
                break;
            case R.id.iv_dialogue:
                System.out.println("iv_dialogue");
                break;
            case R.id.iv_more:
                System.out.println("iv_more");
                break;
            case R.id.iv_listening_mode:
                playMode = (++playMode) % 3;
                changePlayModeDrawable();
                mpService.setPlayMode(playMode);
                break;
            case R.id.iv_back:
                mpService.playPreSong();
                break;
            case R.id.iv_play_music:
                if(mpService.startOrPauseMusic() == 1) {
                    playView();
                } else {
                    pauseView();
                }
                break;
            case R.id.iv_next:
                mpService.playNextSong();
                break;
            case R.id.iv_music_list:
                initListPopupIfNeed();
                musicListPopup.setAnimStyle(XUIPopup.ANIM_AUTO);
                musicListPopup.setPreferredDirection(XUIPopup.DIRECTION_TOP);
                musicListPopup.show(v);
                break;
            default:
                break;
        }
    }

    /**
     * 拖动条进度改变时会自动调用该方法
     * @param seekBar 拖动条
     * @param progress 进度
     * @param fromUser true/false
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    /**
     * 拖动条开始被拖动时自动调用该方法
     * @param seekBar 拖动条
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 拖动条拖动停止时会自动调用该方法
     * @param seekBar 拖动条
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mpService.seekTo(seekBar.getProgress());
    }

    /**
     * 歌词界面点击播放事件
     * @param lrc 歌词界面
     * @param time 点击播放时，线条所处时间
     * @return 返回正确点击状态
     */
    @Override
    public boolean onPlayClick(LrcView lrc, long time) {
        mpService.seekTo((int) time);
        return true;
    }

    /**
     * 图片旋转效果初始化
     */
    private void initRotationAnimator() {
        // 设置旋转动画，旋转角度为一周，360度
        rotationAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0.0f, 360.f);
        // 设定旋转一圈的时间
        rotationAnimator.setDuration(10000);
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
     * 播放音乐时，界面更新
     */
    private void playView() {
        rotationAnimator.resume();
        ivPlayMusic.setImageResource(R.drawable.song_playing_suspend);
    }

    /**
     * 暂停音乐时，界面更新
     */
    private void pauseView() {
        rotationAnimator.pause();
        ivPlayMusic.setImageResource(R.drawable.song_playing_play);
    }

    /**
     * 当需要列表弹窗时初始化弹窗界面
     */
    private void initListPopupIfNeed() {
        String[] songsInformation = SongPlayingUtils.songArrayListToArray(mpService.getSongs());
        XUISimpleAdapter adapter = XUISimpleAdapter.create(getActivity(), songsInformation);
        musicListPopup = new XUIListPopup(Objects.requireNonNull(getActivity()), adapter);
        // 设置列表弹窗监听器，当其中的item被单击时触发
        musicListPopup.create(1000, 500, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mpService.playSongByIndex((int) id);
                // musicListPopup.dismiss();
            }
        });
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

    /**
     * 广播接收器
     * @author len
     *
     */
    private class MsgReceiver extends BroadcastReceiver {
        String tempLrc = "";
        String tempTitleName = "";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("helloBundle", "hello...");
            // 拿到bundle更新界面
            Bundle progressBundle = intent.getBundleExtra("progressBundle");
            if(progressBundle != null) {
                String lrc = progressBundle.getString("lrc");
                String titleName = progressBundle.getString("titleName");
                boolean isPlaying = progressBundle.getBoolean("isPlaying");
                if(titleName != null && !tempTitleName.equals(titleName)) {
                    titleBar.setTitle(titleName);
                    tempTitleName = titleName;
                }
                if(lrc != null && !tempLrc.equals(lrc)) {
                    lrcView.loadLrc(lrc);
                    tempLrc = lrc;
                }
                if(isPlaying) {
                    playView();
                } else {
                    pauseView();
                }
                int duration = progressBundle.getInt("duration");
                int currentPosition = progressBundle.getInt("currentPosition");
                seekBarSongProgress.setMax(duration);
                seekBarSongProgress.setProgress(currentPosition);
                tvMusicTotalTime.setText(SongPlayingUtils.convertTime(duration));
                tvMusicCurrentTime.setText(SongPlayingUtils.convertTime(currentPosition));
                lrcView.updateTime(currentPosition);
            }
        }
    }
}