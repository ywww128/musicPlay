package com.example.musicplayer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.SearchResultAdapter;
import com.example.musicplayer.bean.Song;

import java.util.ArrayList;

/**
 * @author ywww
 * @date 2020-11-20 0:46
 */
public class SearchResultFragment extends Fragment {
    /**
     * 要展示的歌曲信息
     */
    private ArrayList<Song> songs;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_result,null);
        Bundle bundle = getArguments();
        songs = (ArrayList<Song>) bundle.getSerializable("songs");
        for(int i=0;i<songs.size();i++){
            Log.d("TAG"+i,songs.get(i).name+songs.get(i).artists.get(0).name);
        }
        initView();
        return view;
    }
    private void initView(){
        RecyclerView recyclerView = view.findViewById(R.id.search_result_view);
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(songs);
        // 布局管理器，参数解释：上下文对象,方向,是否倒序
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchResultAdapter);
        // 设监听器
        searchResultAdapter.setItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onSongClick(View view, int position) {
                Toast.makeText(getContext(),"你点击了Song"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCollectClick(View view, int position) {
                Toast.makeText(getContext(),"你点击了collect"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMoreClick(View view, int position) {
                Toast.makeText(getContext(),"你点击了more"+position,Toast.LENGTH_SHORT).show();
            }

        });
    }


}
