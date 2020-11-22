package com.example.musicplayer.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.utils.DataUtil;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 写歌曲评论界面
 * A simple {@link Fragment} subclass.
 * Use the {@link #newInstance} factory method to
 * create an instance of this fragment.
 * @author czc
 */
public class PublishSongCommentFragment extends Fragment {
    private static final String USERNAME = "username";
    private static final String SONGID = "songId";
    private String username;
    private String songId;

    private TitleBar titleBar;
    private MultiLineEditText multiLineEditText;

    public PublishSongCommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PublishSongCommentFragment.
     */
    public static PublishSongCommentFragment newInstance(String username, String songId) {
        PublishSongCommentFragment fragment = new PublishSongCommentFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        args.putString(SONGID, songId);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_publish_song_comment, container, false);
        init(view);
        return view;
    }
    private void init(View view){
        titleBar = view.findViewById(R.id.tb_publish_song_comment);
        multiLineEditText = view.findViewById(R.id.publish_song_comment);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container
                        , SongCommentFragment.newInstance(username, songId), null).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "点击了返回按钮", Toast.LENGTH_SHORT).show();
            }
            //设置标题栏右边按钮监听器,点击发布评论
        }).addAction(new TitleBar.ImageAction(R.drawable.confirm) {
            @Override
            public void performAction(View view) {
                writeCommentSong();
                Toast.makeText(getActivity(), "您输入了:"+multiLineEditText.getContentText(), Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container
                        , SongCommentFragment.newInstance(username, songId), null).addToBackStack(null).commit();
            }
        });
    }
    private void writeCommentSong(){
        String info = multiLineEditText.getContentText();
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
        String today = df.format(now);
        String comment = songId+" 2131230849 "+today+" "+username+" "+info;
        DataUtil.comment_song.add(comment);
    }
}