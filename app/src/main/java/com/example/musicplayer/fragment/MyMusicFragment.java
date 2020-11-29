package com.example.musicplayer.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.adapter.MyMusicAdapter;
import com.example.musicplayer.bean.LocalSong;
import com.example.musicplayer.bean.PlaySongData;
import com.example.musicplayer.util.SongResourceUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author ywww
 * @date 2020-11-16 21:31
 */
public class MyMusicFragment extends Fragment {
    private View view;
    private List<LocalSong> songList;
    private MainActivity mainActivity;
    private SongPlayingFragment songPlayingFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            Log.i("newone", "hello");
            view = inflater.inflate(R.layout.fragment_my_music,null);
            songList = SongResourceUtils.getSongList(getContext());
            mainActivity = (MainActivity) getActivity();
            initView();
        }
        return view;
    }

    private void initView(){
        Log.i("size", String.valueOf(songList.size()));
        RecyclerView recyclerView = view.findViewById(R.id.my_music_recyclerview);
        MyMusicAdapter myMusicAdapter = new MyMusicAdapter(songList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myMusicAdapter);
        myMusicAdapter.setItemClickListener(new MyMusicAdapter.OnItemClickListener() {
            @Override
            public void onSongClick(View view, int position) {
                PlaySongData playSongData = new PlaySongData();
                playSongData.setId((int) songList.get(position).getId());
                playSongData.setUrl(songList.get(position).getUrl());
                playSongData.setDuration((int) songList.get(position).getDuration());
                playSongData.setName(songList.get(position).getName());
                FragmentTransaction fTransaction = mainActivity.getManager().beginTransaction();
                mainActivity.hideBottomView(fTransaction);
                mainActivity.hideTopView(fTransaction);
                if(songPlayingFragment == null){
                    songPlayingFragment = new SongPlayingFragment();
                    fTransaction.add(R.id.content_panel,songPlayingFragment);
                } else {
                    fTransaction.show(songPlayingFragment);
                }
                songPlayingFragment.setNewSong(playSongData);
                fTransaction.addToBackStack(null).commit();
            }

            @Override
            public void onCollectClick(View view, int position) {

            }

            @Override
            public void onMoreClick(View view, int position) {

            }
        });
    }
}
