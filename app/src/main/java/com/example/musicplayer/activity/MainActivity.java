package com.example.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.fragment.IndexFragment;
import com.example.musicplayer.fragment.MyAccountFragment;
import com.example.musicplayer.fragment.MyMusicFragment;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

/**
 * @author ywww
 * 主Activity
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 导航栏的4个tab
     */
    private LinearLayout mainTab1;
    private LinearLayout mainTab2;
    private LinearLayout mainTab3;
    private LinearLayout mainTab4;
    private FragmentManager fManager;
    /**
     * 导航栏的4个tab对应的ImageView
     */
    private ImageView imageMainTab1;
    private ImageView imageMainTab2;
    private ImageView imageMainTab3;
    private ImageView imageMainTab4;
    private Button searchButton;
    private IndexFragment indexFragment;
    private MyMusicFragment myMusicFragment;
    private MyAccountFragment myAccountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fManager = getSupportFragmentManager();
        initSearchView();
        setMainTabClick(mainTab1);
    }

    public void initView(){
        mainTab1 = findViewById(R.id.main_tab1);
        mainTab2 = findViewById(R.id.main_tab2);
        mainTab3 = findViewById(R.id.main_tab3);
        mainTab4 = findViewById(R.id.main_tab4);
        imageMainTab1 = findViewById(R.id.image_main_tab1);
        imageMainTab2 = findViewById(R.id.image_main_tab2);
        imageMainTab3 = findViewById(R.id.image_main_tab3);
        imageMainTab4 = findViewById(R.id.image_main_tab4);
        searchButton = findViewById(R.id.main_search_button);
    }

    /**
     * 为底部导航栏设置监听器,xml文件里使用了onclick标签
     */
    public void setMainTabClick(View view){
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (view.getId()){
            case R.id.main_tab1:
                imageMainTab1.setImageResource(R.mipmap.music_logo1);
                if(indexFragment == null){
                    indexFragment = new IndexFragment();
                    fTransaction.add(R.id.content_panel,indexFragment).commit();
                } else {
                    fTransaction.show(indexFragment).commit();
                }
                break;

            case R.id.main_tab2:
                imageMainTab2.setImageResource(R.mipmap.music_logo1);
                if(myMusicFragment == null){
                    myMusicFragment = new MyMusicFragment();
                    fTransaction.add(R.id.content_panel,myMusicFragment).commit();
                } else {
                    fTransaction.show(myMusicFragment).commit();
                }
                break;

            case R.id.main_tab3:
                imageMainTab3.setImageResource(R.mipmap.community_logo1);
                // 代写
                break;

            case R.id.main_tab4:
                imageMainTab4.setImageResource(R.mipmap.community_logo1);
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

    private void hideAllFragment(FragmentTransaction fTransaction){
        if(indexFragment!=null) {
            imageMainTab1.setImageResource(R.mipmap.music_logo2);
            fTransaction.hide(indexFragment);
        }
        if(myMusicFragment!=null) {
            imageMainTab2.setImageResource(R.mipmap.music_logo2);
            fTransaction.hide(myMusicFragment);
        }
        if(true){
            imageMainTab3.setImageResource(R.mipmap.community_logo2);
            // 代写
        }
        if(myAccountFragment != null){
            imageMainTab4.setImageResource(R.mipmap.community_logo2);
            fTransaction.hide(myAccountFragment);
        }
    }

    private void initSearchView(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = SearchFragment.newInstance();
                searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
                    @Override
                    public void OnSearchClick(String keyword) {
                        //这里处理逻辑
                        Toast.makeText(MainActivity.this, keyword, Toast.LENGTH_SHORT).show();
                    }
                });
                searchFragment.showFragment(getSupportFragmentManager(),SearchFragment.TAG);
            }
        });
    }

}