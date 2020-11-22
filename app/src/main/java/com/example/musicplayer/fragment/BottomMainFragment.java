package com.example.musicplayer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;

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
    private View view;
    private MainActivity mainActivity;
    private FragmentManager fManager;
    private int position = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bottom_main,null);
        initView();
        mainActivity = (MainActivity) getActivity();
        fManager = mainActivity.getManager();
        initListener();
        setMainTabClick(1);
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
    }

    private void initListener(){
        mainTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = 1;
                setMainTabClick(position);
            }
        });
        mainTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = 2;
                setMainTabClick(position);
            }
        });
        mainTab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = 3;
                setMainTabClick(position);
            }
        });
        mainTab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = 4;
                setMainTabClick(position);
            }
        });
    }
    /**
     * 为底部导航栏设置监听器,xml文件里使用了onclick标签
     */
    private void setMainTabClick(int position){
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (position){
            case 1:
                imageMainTab1.setImageResource(R.drawable.music_logo1);
                if(indexFragment == null){
                    indexFragment = new IndexFragment();
                    fTransaction.add(R.id.content_panel,indexFragment).commit();
                } else {
                    fTransaction.show(indexFragment).commit();
                }
                break;

            case 2:
                imageMainTab2.setImageResource(R.drawable.music_logo1);
                if(myMusicFragment == null){
                    myMusicFragment = new MyMusicFragment();
                    fTransaction.add(R.id.content_panel,myMusicFragment).commit();
                } else {
                    fTransaction.show(myMusicFragment).commit();
                }
                break;

            case 3:
                imageMainTab3.setImageResource(R.drawable.community_logo1);
                // 代写
                break;

            case 4:
                imageMainTab4.setImageResource(R.drawable.community_logo1);
                if(myAccountFragment == null){
                    myAccountFragment = new MyAccountFragment();
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
                imageMainTab2.setImageResource(R.drawable.music_logo2);
                fTransaction.hide(myMusicFragment);
            }
            if(true){
                imageMainTab3.setImageResource(R.drawable.community_logo2);
                // 代写
            }
            if(myAccountFragment != null){
                imageMainTab4.setImageResource(R.drawable.community_logo2);
                fTransaction.hide(myAccountFragment);
            }
        } catch (Exception e){
            Log.d("TAG2",e.toString());
        }

    }
}
