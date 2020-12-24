package com.example.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.musicplayer.R;
import com.example.musicplayer.fragment.BottomMainFragment;
import com.example.musicplayer.fragment.SongPlayingFragment;
import com.example.musicplayer.fragment.TopMainFragment;
import com.example.musicplayer.volley.UserLoginCheck;

/**
 * @author ywww
 * 主Activity
 */
public class MainActivity extends AppCompatActivity {

    final static int TWENTY_THREE = 23;

    private FragmentManager fManager;
    private TopMainFragment topMainFragment;
    private BottomMainFragment bottomMainFragment;
    private SongPlayingFragment songPlayingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在Android6.0后，需要进行动态获取权限，这里获取向外存中读写权限
        if(Build.VERSION.SDK_INT > TWENTY_THREE) {
            int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        setContentView(R.layout.activity_main);
        fManager = getSupportFragmentManager();
        topMainFragment = new TopMainFragment();
        bottomMainFragment = new BottomMainFragment();
        FragmentTransaction fragmentTransaction = fManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_top_main,topMainFragment);
        fragmentTransaction.add(R.id.fragment_bottom_main,bottomMainFragment).commit();

        /**
         * 测试区域
         */
    }

    public void hideTopView(FragmentTransaction fragmentTransaction){
        fragmentTransaction.hide(topMainFragment);
    }

    public void hideBottomView(FragmentTransaction fragmentTransaction){
        fragmentTransaction.hide(bottomMainFragment);
    }


    public FragmentManager getManager(){
        return fManager;
    }

    public BottomMainFragment getBottomMainFragment(){
        return bottomMainFragment;
    }

    public TopMainFragment getTopMainFragment() {
        return topMainFragment;
    }

    public SongPlayingFragment getSongPlayingFragment(){
        return songPlayingFragment;
    }

}