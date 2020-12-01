package com.example.musicplayer.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.utils.DataUtil;
import com.xuexiang.xui.utils.SnackbarUtils;
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

    /**
     * 初始化控件
     * @param view 包含控件的视图
     */
    private void init(View view){
        titleBar = view.findViewById(R.id.tb_publish);
        editText = view.findViewById(R.id.publish_text);

        //设置标题栏左边按钮监听器，点击返回社区主界面
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment publishFragment = (PublishFragment) getActivity().getSupportFragmentManager().findFragmentByTag("pf");
                CommunityFragment communityFragment = (CommunityFragment) getActivity().getSupportFragmentManager().findFragmentByTag("cf");
                getActivity().getSupportFragmentManager().beginTransaction().hide(publishFragment).commit();
                getActivity().getSupportFragmentManager().beginTransaction().show(communityFragment).commit();
                Toast.makeText(getActivity(), "点击了返回按钮", Toast.LENGTH_SHORT).show();
            }
            //设置标题栏右边按钮监听器,点击发布动态
        }).addAction(new TitleBar.ImageAction(R.drawable.confirm) {
            @Override
            public void performAction(View view) {
                String info = editText.getContentText();
                Toast.makeText(getActivity(), "您输入了:"+info, Toast.LENGTH_SHORT).show();
                //判断输入书否为空,内容为空跳出提示信息
                if(info.equals("")){
                    //设置内容在顶部显示
                    SnackbarUtils.Short(view, "发布动态内容不能为空").gravityFrameLayout(Gravity.TOP)
                            .messageCenter().warning().show();
                    Toast.makeText(getActivity(), "发布动态内容不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    writeSituation();
                    PublishFragment publishFragment = (PublishFragment) getActivity().getSupportFragmentManager().findFragmentByTag("pf");
                    CommunityFragment communityFragment = (CommunityFragment) getActivity().getSupportFragmentManager().findFragmentByTag("cf");
                    getActivity().getSupportFragmentManager().beginTransaction().hide(publishFragment).commit();
                    getActivity().getSupportFragmentManager().beginTransaction().show(communityFragment).commit();
                    communityFragment.initData();
                }
            }
        });
    }

    /**
     * 将用户发布的内容写回
     */
    private void writeSituation(){
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
        String info = "5,2131230853,"+df.format(now)+","+username+","+editText.getContentText() + ",0";
        Log.i("username",username);
        DataUtil.situation.add(info);
        Log.i("publishFragment",DataUtil.situation.toString());
    }


}