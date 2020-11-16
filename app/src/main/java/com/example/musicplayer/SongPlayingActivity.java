package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.xuexiang.xui.widget.actionbar.TitleBar;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongPlayingActivity extends AppCompatActivity {
    private ImageView ivLove;
    private ImageView ivListeningMode;
    private TitleBar titleBarReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_playing);
        initView();
        imgRotation();
    }

    private void initView() {
        /**
         * 添加点击监视器，用来表示是否喜欢该音乐
         */
        ivLove  = findViewById(R.id.song_playing_love);
        ivLove.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                ivLove.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_love_red));
            }
        });

        /**
         * 添加点击监视器，用来修改播放音乐的模式
         */
        ivListeningMode = findViewById(R.id.iv_listening_mode);
        ivListeningMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivListeningMode.setImageDrawable(getResources().getDrawable(R.drawable.song_playing_transfer));
            }
        });

        /**
         * 设置返回按钮，用于返回上一个页面
         */
        titleBarReturn = findViewById(R.id.title_bar_return);
        titleBarReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongPlayingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void imgRotation() {
        CircleImageView circleImageView = findViewById(R.id.img_circle_disc);
        // loadAnimation 为内部解析xml并创建Animation对象
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_animation);
        // 启动动画
        circleImageView.startAnimation(animation);
    }
}