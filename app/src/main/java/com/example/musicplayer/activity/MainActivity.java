package com.example.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.musicplayer.R;
import com.example.musicplayer.fragment.BottomMainFragment;
import com.example.musicplayer.fragment.TopMainFragment;

/**
 * @author ywww
 * 主Activity
 */
public class MainActivity extends AppCompatActivity {

    private FragmentManager fManager;
    private TopMainFragment topMainFragment;
    private BottomMainFragment bottomMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fManager = getSupportFragmentManager();
        topMainFragment = new TopMainFragment();
        bottomMainFragment = new BottomMainFragment();
        FragmentTransaction fragmentTransaction = fManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_top_main,topMainFragment);
        fragmentTransaction.add(R.id.fragment_bottom_main,bottomMainFragment).commit();
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


}