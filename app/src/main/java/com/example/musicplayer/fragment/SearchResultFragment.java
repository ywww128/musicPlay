package com.example.musicplayer.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.adapter.SearchResultAdapter;
import com.example.musicplayer.bean.PlaySongData;
import com.example.musicplayer.bean.Song;
import com.example.musicplayer.volley.SongObtain;
import com.example.musicplayer.volley.SongsMessageObtain;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.util.ArrayList;

/**
 * @author ywww
 * @date 2020-11-20 0:46
 * 搜索结果展示界面
 */
public class SearchResultFragment extends Fragment {
    /**
     * 要展示的歌曲信息
     */
    private ArrayList<Song> songs;
    private View view;
    private TopSearchFragment topSearchFragment;
    /**
     * 放recyclerView的layout
     */
    private FrameLayout showSongsLayout;
    /**
     * 等待界面
     */
    private View waitContent;
    /**
     * 等待界面图片旋转将要使用
     */
    private RadiusImageView waitImage;
    private ObjectAnimator waitAnim;
    /**
     * recyclerView的xml文件读取使用
     */
    private View recyclerViewContent;
    private RecyclerView recyclerView;
    private PlaySongData playSongData;
    /**
     * recyclerView刷新
     */
    private SwipeRefreshLayout refreshLayout;
    private MainActivity mainActivity;
    private SongObtain songObtain;
    private SongsMessageObtain songsMessageObtain;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_search_result,null);
            mainActivity = (MainActivity) getActivity();
            showSongsLayout = view.findViewById(R.id.show_songs_content);
            waitContent = inflater.inflate(R.layout.wait_content,null);
            waitImage = waitContent.findViewById(R.id.wait_image);
            // 旋转动画
            waitAnim = ObjectAnimator.ofFloat(waitImage,"rotation",0f,360f);
            waitAnim.setDuration(5000);
            // 重复无数次
            waitAnim.setRepeatCount(-1);
            // 去除停顿
            waitAnim.setInterpolator(new LinearInterpolator());
            recyclerViewContent = inflater.inflate(R.layout.recylerview_search,null);
            refreshLayout = recyclerViewContent.findViewById(R.id.search_swipe_refresh_layout);
            initRefreshLayout();
            // 设置搜索界面的搜索框
            topSearchFragment = new TopSearchFragment();
            MainActivity mainActivity = (MainActivity) getActivity();
            FragmentTransaction fTransaction = mainActivity.getManager().beginTransaction();
            fTransaction.add(R.id.top_search_content,topSearchFragment).commit();
            // 第一次进入搜索界面的传值
            topSearchFragment.setArguments(getArguments());
        } else {
            // 后来进入搜索页面的传值
            Bundle bundle = getArguments();
            topSearchFragment.changeEditViewText(bundle.getString("keywords"));
            // 从主要搜索进入,删除上一个界面的recylerView
            showSongsLayout.removeAllViews();
            recyclerView = null;
        }
        // 加入等待界面
        showSongsLayout.addView(waitContent);
        waitAnim.start();
        return view;
    }

    /**
     * 更新歌曲展示列表
     * @param songs 歌曲信息
     */
    public void updateView(final ArrayList<Song> songs){
        this.songs = songs;
        // 数据输出查看
        for(int i=0;i<songs.size();i++){
            Log.d("TAG"+i,songs.get(i).name+songs.get(i).artists.get(0).name);
        }
        recyclerView = recyclerViewContent.findViewById(R.id.search_result_view);
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(songs);
        // 布局管理器，参数解释：上下文对象,方向,是否倒序
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchResultAdapter);
        // 设监听器
        searchResultAdapter.setItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            // 播放歌曲
            @Override
            public void onSongClick(View view, int position) {
                playSongData = new PlaySongData();
                playSongData.setId(songs.get(position).id);
                playSongData.setName(songs.get(position).name);
                playSongData.setDuration(songs.get(position).duration);
                playSongData.setArtists(songs.get(position).artists);
                songObtain = new SongObtain(getContext(), playSongData);
                songObtain.startGetJson();
            }

            // 收藏歌曲
            @Override
            public void onCollectClick(View view, int position) {
                Toast.makeText(getContext(),"你点击了收藏"+position,Toast.LENGTH_SHORT).show();
            }

            // 更多设置
            @Override
            public void onMoreClick(View view, int position) {
                Toast.makeText(getContext(),"你点击了更多"+position,Toast.LENGTH_SHORT).show();
            }

        });
        // 刷新结束,刷新不需要等待界面切换
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        } else {
            waitAnim.end();
            // 将刷新的布局删掉
            showSongsLayout.removeAllViews();
            showSongsLayout.addView(recyclerViewContent);
        }
    }

    /**
     * 进入加载动画
     */
    public void beginWait(){
        showSongsLayout.removeAllViews();
        showSongsLayout.addView(waitContent);
        waitAnim.start();
    }

    /**
     * 设置刷新监听器
     */
    private void initRefreshLayout(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 进行歌曲信息获取操作
                songsMessageObtain = new SongsMessageObtain(mainActivity,
                        SearchResultFragment.this,topSearchFragment.getText());
                songsMessageObtain.startGetJson();
            }
        });
    }

    /**
     * 进入播放界面时要去除焦点和键盘
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        topSearchFragment.hideSoftInput();
        topSearchFragment.clearEditFocus();
    }
}
