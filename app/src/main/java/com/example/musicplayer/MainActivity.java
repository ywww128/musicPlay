package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author ywww
 * 主Activity
 */
public class MainActivity extends AppCompatActivity {

    private LinearLayout main_tab1;
    private LinearLayout main_tab2;
    private LinearLayout main_tab3;
    private LinearLayout main_tab4;
    private FragmentManager fManager;
    private IndexFragment indexFragment;
    private  MyMusicFragment myMusicFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fManager = getSupportFragmentManager();
    }

    public void initView(){
        main_tab1 = findViewById(R.id.main_tab1);
        main_tab2 = findViewById(R.id.main_tab2);
        main_tab3 = findViewById(R.id.main_tab3);
        main_tab4 = findViewById(R.id.main_tab4);
    }

    /**
     * 为底部导航栏设置监听器
     */
    public void setMainTabClick(View view){
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (view.getId()){
            case R.id.main_tab1:
                if(indexFragment == null){
                    indexFragment = new IndexFragment();
                    fTransaction.add(R.id.content_panel,indexFragment).commit();
                } else {
                    fTransaction.hide(myMusicFragment);
                    fTransaction.show(indexFragment).commit();
                }
                break;

            case R.id.main_tab2:
                if(myMusicFragment == null){
                    myMusicFragment = new MyMusicFragment();
                    fTransaction.add(R.id.content_panel,myMusicFragment).commit();
                } else {
                    fTransaction.hide(indexFragment);
                    fTransaction.show(myMusicFragment).commit();
                }
                break;
        }
    }

    private void hideAllFragment(FragmentTransaction fTransaction){
        if(indexFragment!=null) {
            fTransaction.hide(indexFragment);
        }
        if(myMusicFragment!=null) {
            fTransaction.hide(myMusicFragment);
        }
    }
}