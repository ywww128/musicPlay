package com.example.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.musicplayer.R;
import com.example.musicplayer.fragment.CommunityFragment;
import com.example.musicplayer.fragment.SongCommentFragment;

/**
 * @author 14548
 */
public class MainActivity2 extends FragmentActivity {
    private CommunityFragment communityFragment;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
    }

    private void init(){
        String info = getIntent().getStringExtra("info");
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if("community".equals(info)) {
            fragmentTransaction.add(R.id.container, CommunityFragment.newInstance("zicai"), "cf");
        }else{
            fragmentTransaction.add(R.id.container, SongCommentFragment.newInstance("zicai","1"), "scf");
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fragmentManager.popBackStack();
    }
}