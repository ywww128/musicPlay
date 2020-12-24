package com.example.musicplayer.fragment;

import android.animation.ObjectAnimator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.bean.PlaySongData;
import com.example.musicplayer.volley.UserLoginCheck;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

import java.util.Objects;


/**
 * @author ywww
 * @date 2020-11-20 19:36
 */
public class BottomMainFragment extends Fragment {
    /**
     * 导航栏的4个tab
     */
    private LinearLayout mainTab1;
    private LinearLayout mainTab2;
    private LinearLayout mainTab3;
    private LinearLayout mainTab4;
    /**
     * 导航栏的4个tab对应的ImageView
     */
    private ImageView imageMainTab1;
    private ImageView imageMainTab2;
    private ImageView imageMainTab3;
    private ImageView imageMainTab4;
    private IndexFragment indexFragment;
    private MyMusicFragment myMusicFragment;
    private MyAccountFragment myAccountFragment;
    private CommunityFragment communityFragment;

    private SongPlayingFragment songPlayingFragment;
    /**
     * 进入播放界面图标
     */
    private RadiusImageView toPlayingPageImageView;
    private ObjectAnimator playMusicAnim;

    private View view;
    private MainActivity mainActivity;
    private FragmentManager fManager;
    private int position = 1;

    /**
     * 用户信息
     */
    private JSONObject msg = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bottom_main,null);
        mainActivity = (MainActivity) getActivity();
        fManager = mainActivity.getManager();
        initView();
        initListener();
        initLogin();
        setMainTabClick(1);

        // 动态注册广播接收器
        MsgReceiver msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("UPDATE_MUSIC_PLAYING_FRAGMENT");
        Objects.requireNonNull(getActivity()).registerReceiver(msgReceiver, intentFilter);
        return view;
    }

    private void initView(){
        mainTab1 = view.findViewById(R.id.main_tab1);
        mainTab2 = view.findViewById(R.id.main_tab2);
        mainTab3 = view.findViewById(R.id.main_tab3);
        mainTab4 = view.findViewById(R.id.main_tab4);
        imageMainTab1 = view.findViewById(R.id.image_main_tab1);
        imageMainTab2 = view.findViewById(R.id.image_main_tab2);
        imageMainTab3 = view.findViewById(R.id.image_main_tab3);
        imageMainTab4 = view.findViewById(R.id.image_main_tab4);
        songPlayingFragment = mainActivity.getSongPlayingFragment();
        toPlayingPageImageView = view.findViewById(R.id.index_to_playing_image);
        playMusicAnim = ObjectAnimator.ofFloat(toPlayingPageImageView,"rotation",0f,360f);
        playMusicAnim.setDuration(10000);
        // 重复无数次
        playMusicAnim.setRepeatCount(-1);
        // 旋转不卡顿
        playMusicAnim.setInterpolator(new LinearInterpolator());
    }

    /**
     * 为底部导航栏及进入播放界面的图标设置监听器
     */
    private void initListener(){
        mainTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != 1){
                    position = 1;
                    setMainTabClick(position);
                }
            }
        });
        mainTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != 2){
                    position = 2;
                    setMainTabClick(position);
                }
            }
        });
        mainTab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != 3){
                    position = 3;
                    setMainTabClick(position);
                }
            }
        });
        mainTab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != 4){
                    position = 4;
                    setMainTabClick(position);
                }
            }
        });
        // 进入播放界面监听器
        toPlayingPageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fTransaction = fManager.beginTransaction();
                mainActivity.hideBottomView(fTransaction);
                mainActivity.hideTopView(fTransaction);
                if(songPlayingFragment == null){
                    songPlayingFragment = new SongPlayingFragment();
                }
                fTransaction.replace(R.id.content_panel,songPlayingFragment).addToBackStack(null).commit();
            }
        });
    }
    /**
     * 为底部导航栏设置监听器执行内容
     */
    private void setMainTabClick(int position){
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (position){
            case 1:
                // 主页
                imageMainTab1.setImageResource(R.drawable.music_logo1);
                if(indexFragment == null){
                    indexFragment = new IndexFragment();
                    fTransaction.add(R.id.content_panel,indexFragment).commit();
                } else {
                    fTransaction.show(indexFragment).commit();
                }
                break;

            case 2:
                // 跳转到我的
                imageMainTab2.setImageResource(R.drawable.logo_me_1);
                if(myMusicFragment == null){
                    myMusicFragment = new MyMusicFragment();
                    fTransaction.add(R.id.content_panel,myMusicFragment).commit();
                } else {
                    fTransaction.show(myMusicFragment).commit();
                }
                break;

            case 3:
                // 跳转到社区主界面
                imageMainTab3.setImageResource(R.drawable.logo_community_2);
                if(communityFragment == null) {
                    communityFragment = CommunityFragment.newInstance("102");
                    fTransaction.add(R.id.content_panel, communityFragment, "cf").commit();
                }else{
                    fTransaction.show(communityFragment).commit();
                }
                break;

            case 4:
                // 跳转到个人中心
                imageMainTab4.setImageResource(R.drawable.community_logo1);
                if(myAccountFragment == null){
                    myAccountFragment = new MyAccountFragment();
                    if(msg != null){
                        // 将用户信息传给账户界面
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", String.valueOf(msg));
                        myAccountFragment.setArguments(bundle);
                    }
                    fTransaction.add(R.id.content_panel,myAccountFragment).commit();
                } else {
                    fTransaction.show(myAccountFragment).commit();
                }
                break;

            default:
                break;
        }
    }

    public void hideAllFragment(FragmentTransaction fTransaction){
        try{
            if(indexFragment!=null) {
                imageMainTab1.setImageResource(R.drawable.music_logo2);
                fTransaction.hide(indexFragment);
            }
            if(myMusicFragment!=null) {
                imageMainTab2.setImageResource(R.drawable.logo_me_2);
                fTransaction.hide(myMusicFragment);
            }
            if(communityFragment != null){
                imageMainTab3.setImageResource(R.drawable.logo_community_1);
                fTransaction.hide(communityFragment);
            }
            if(myAccountFragment != null){
                imageMainTab4.setImageResource(R.drawable.community_logo2);
                fTransaction.hide(myAccountFragment);
            }
        } catch (Exception e){
            Log.d("TAG2",e.toString());
        }

    }

    public void changePlayMusicAnim(boolean isPlaying){
        if(isPlaying){
            playMusicAnim.start();
        } else {
            playMusicAnim.pause();
        }
    }

    /**
     * 广播接收器
     * @author len
     *
     */
    private class MsgReceiver extends BroadcastReceiver {
        private boolean isPlayingTemp = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            // 拿到bundle更新界面
            Bundle progressBundle = intent.getBundleExtra("progressBundle");
            if(progressBundle != null) {
                boolean isPlaying = progressBundle.getBoolean("isPlaying");
                if(isPlaying != isPlayingTemp) {
                    changePlayMusicAnim(isPlaying);
                    isPlayingTemp = isPlaying;
                }

            }
        }
    }

    /**
     * 自动登录操作
     */
    private void initLogin(){
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        Map<String,String> map = (Map<String, String>) sharedPreferences.getAll();
        Set<String> set = map.keySet();
        for(String k : set){
            String value = map.get(k);
            try {
                JSONObject jsonObject = new JSONObject(value);
                // 自动登录
                if(jsonObject.getBoolean("isLogin")){
                    UserLoginCheck userLoginCheck = new UserLoginCheck(mainActivity,BottomMainFragment.this,
                            jsonObject.getString("id"),jsonObject.getString("password"));
                    userLoginCheck.checkUser();
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 服务器响应后的处理
     * @param msg
     */
    public void dealLoginResult(JSONObject msg){
        String errorMessage = null;
        try {
            errorMessage = msg.getString("errorMessage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(errorMessage == null){
            Log.d("TEST",String.valueOf(msg));
            this.msg = msg;
        }
    }
}
