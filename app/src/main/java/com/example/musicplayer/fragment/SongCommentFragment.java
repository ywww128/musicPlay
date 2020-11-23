package com.example.musicplayer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.activity.MainActivity2;
import com.example.musicplayer.adapter.SongCommentAdapter;
import com.example.musicplayer.bean.CommunityItemBean;
import com.example.musicplayer.bean.SongCommentItemBean;
import com.example.musicplayer.utils.DataUtil;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 歌曲评论界面
 * @author czc
 */
public class SongCommentFragment extends Fragment implements SongCommentAdapter.CallBack {
    private static final String USERNAME = "username";
    private static final String SONGID = "id";
    private String username;
    private String songId = "1";

    private List<SongCommentItemBean> list = new ArrayList<>();
    private ListView listView;
    private TitleBar titleBar;

    public SongCommentFragment() {
        // Required empty public constructor
    }

    /**
     * 可以添加参数并绑定到fragment中
     * @return
     */
    public static SongCommentFragment newInstance(String username, String songId){
        SongCommentFragment songCommentFragment = new SongCommentFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        args.putString(SONGID, songId);
        songCommentFragment.setArguments(args);
        return songCommentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            username = getArguments().getString(USERNAME);
            songId = getArguments().getString(SONGID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_comment, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        listView = view.findViewById(R.id.comments_song);
        titleBar = view.findViewById(R.id.tb_song_comment);
        new DataUtil();

        for(int i = 0; i < DataUtil.comment_song.size(); i++){
            String line = DataUtil.comment_song.get(i);
            String[] info = line.split(" ");
            if(info[0].equals(songId)) {
                SongCommentItemBean item = new SongCommentItemBean(Integer.parseInt(info[1]), info[3]
                        , info[2], info[4]);
                list.add(item);
            }
        }

        listView.setAdapter(new SongCommentAdapter(getActivity(), list, this));

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //标题栏左边按钮的监听器
                Toast.makeText(getActivity(), "SongCommentFragement--点击了返回按钮", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }).addAction(new TitleBar.ImageAction(R.drawable.publish) {
            @Override
            public void performAction(View view) {
                //点击标题栏右边按钮跳转到评论页面
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container
                        , PublishSongCommentFragment.newInstance(username, songId), null).addToBackStack(null).commit();
            }
        });
    }

    /**
     * 点击点赞按钮触发
     * @param v
     */
    @Override
    public void click(View v) {
        String tag = (String) v.getTag();
        Toast.makeText(getActivity(), "被点击按钮位置："+tag, Toast.LENGTH_SHORT).show();
    }
}