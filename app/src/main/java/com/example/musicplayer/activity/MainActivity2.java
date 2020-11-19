package com.example.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.musicplayer.R;
import com.example.musicplayer.fragment.CommunityFragment;

public class MainActivity2 extends FragmentActivity {
    private CommunityFragment communityFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
    }

    private void init(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        communityFragment = new CommunityFragment();
        fragmentTransaction.add(R.id.container, communityFragment);
        fragmentTransaction.commit();
    }
}