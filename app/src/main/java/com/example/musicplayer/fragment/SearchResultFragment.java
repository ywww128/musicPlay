package com.example.musicplayer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.adapter.SearchResultAdapter;
import com.example.musicplayer.bean.PlaySongData;
import com.example.musicplayer.bean.Song;
import com.example.musicplayer.volley.SongObtain;

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
    private LinearLayout linearLayout;
    private TopSearchFragment topSearchFragment;
    private SongObtain songObtain;
    private View waitContent;
    private View recyclerViewContent;
    private PlaySongData playSongData;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_search_result,null);
            linearLayout = view.findViewById(R.id.search_page_layout);
            waitContent = inflater.inflate(R.layout.wait_content,null);
            recyclerViewContent = inflater.inflate(R.layout.recylerview_search,null);
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
        }
        return view;
    }

    /**
     * 更新歌曲展示列表
     * @param songs 歌曲信息
     */
    public void updateView(final ArrayList<Song> songs){
        // 删除上一个界面的recylerView
        if(recyclerViewContent != null){
            linearLayout.removeView(recyclerViewContent);
        }
        this.songs = songs;
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
        // 将刷新的布局删掉
        linearLayout.removeView(waitContent);
        linearLayout.addView(recyclerViewContent);
    }

}
