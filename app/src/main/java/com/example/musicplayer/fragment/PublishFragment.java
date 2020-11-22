package com.example.musicplayer.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
 * 发表动态fragment，对应fragment_publish
 * @author czc
 */
public class PublishFragment extends Fragment {
    private static final String USERNAME = "username";

    private TitleBar titleBar;
    private MultiLineEditText editText;
    private String username;
    public PublishFragment() {
        // Required empty public constructor
    }


    public static PublishFragment newInstance(String username){
        PublishFragment publishFragment = new PublishFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        publishFragment.setArguments(args);
        return publishFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            username = getArguments().getString(USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        init(view);
        return view;
    }
    private void init(View view){
        titleBar = view.findViewById(R.id.tb_publish);
        editText = view.findViewById(R.id.publish_text);
        //设置标题栏左边按钮监听器，点击返回社区主界面
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, CommunityFragment.newInstance(username), "cf")
                        .addToBackStack(null).commit();
                Toast.makeText(getActivity(), "点击了返回按钮", Toast.LENGTH_SHORT).show();
            }
            //设置标题栏右边按钮监听器,点击发布动态
        }).addAction(new TitleBar.ImageAction(R.drawable.confirm) {
            @Override
            public void performAction(View view) {
                writeSituation();
                Toast.makeText(getActivity(), "您输入了:"+editText.getContentText(), Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, CommunityFragment.newInstance(username), "cf")
                        .addToBackStack(null).commit();
            }
        });
    }
    private void writeSituation(){
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
        String info = "11 2131230849 "+df.format(now)+" "+username+" "+editText.getContentText();
        Log.i("username",username);
        DataUtil.situation.add(info);
        Log.i("publishFragment",DataUtil.situation.toString());
    }

}