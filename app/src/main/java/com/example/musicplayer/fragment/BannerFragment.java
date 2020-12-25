package com.example.musicplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

/**
 * @author ywww
 * @date 2020-11-17 22:32
 */
public class BannerFragment extends Fragment implements OnBannerListener {
    private Banner banner;
    private ArrayList<String> listPath;
    private ArrayList<String> listTitle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner,null);
        banner = view.findViewById(R.id.image_banner);
        initImageBanner();
        return view;
    }

    /**
     * 初始化轮播图
     */
    public void initImageBanner(){
        //放图片地址的集合
        listPath = new ArrayList<>();
        //放标题的集合
        listTitle = new ArrayList<>();

        listPath.add("http://116.62.109.242/img/banner1.png");
        listPath.add("http://116.62.109.242/img/banner2.jpg");
        listPath.add("http://116.62.109.242/img/banner3.jpg");
        listPath.add("http://116.62.109.242/img/banner4.jpg");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(listPath);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(listTitle);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
        banner.setOnBannerListener(this);
        //必须最后调用的方法，启动轮播图。
        banner.start();
    }

    /**
     * 图片点击事件
     * @param position 第position张图片
     */
    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第"+position+"张轮播图");
    }


    /**
     * 图片加载器
     */
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}
