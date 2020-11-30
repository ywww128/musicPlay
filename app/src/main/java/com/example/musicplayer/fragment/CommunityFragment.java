package com.example.musicplayer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.adapter.CommunityAdapter;
import com.example.musicplayer.adapter.CommunityAdapter.Callback;
import com.example.musicplayer.bean.CommunityItemBean;
import com.example.musicplayer.utils.DataUtil;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author czc
 */
public class CommunityFragment extends Fragment implements OnItemClickListener, Callback {

    private static final String USERNAME = "username";
    private String username;

    private RadiusImageView publish;
    private ListView listView;

    private SmartRefreshLayout smartRefreshLayout;

    private List<CommunityItemBean> list = new ArrayList<>();


    public CommunityFragment() { }

    /**
     * 将用户的用户名保存起来
     * @param username 用户名
     * @return 返回一个CommunityFragment类，并将参数保存在类中
     */
    public static CommunityFragment newInstance(String username){
        CommunityFragment communityFragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        communityFragment.setArguments(args);
        return communityFragment;
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
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        //初始化控件
        publish = view.findViewById(R.id.publish);
        listView = view.findViewById(R.id.list1);
        smartRefreshLayout = view.findViewById(R.id.refreshLayout);

        init();
        return view;
    }

    /**
     * 将数据通过SimpleAdapter封装到ListView中
     */
    private void init() {
        new DataUtil();
        initData();
        //设置SmartRefreshLayout的header样式
        smartRefreshLayout.setRefreshHeader(new TaurusHeader(getActivity()));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                Toast.makeText(getActivity(), "刷新", Toast.LENGTH_SHORT).show();
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityFragment communityFragment = (CommunityFragment) getActivity().getSupportFragmentManager().findFragmentByTag("cf");
                getActivity().getSupportFragmentManager().beginTransaction().hide(communityFragment).commit();
                //跳转到PublishFragment,并将参数username传递给PublishFragment，设置tag为pf
                PublishFragment publishFragment = (PublishFragment) getActivity().getSupportFragmentManager().findFragmentByTag("pf");
                if(publishFragment == null){
                    publishFragment = PublishFragment.newInstance(username);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_panel, publishFragment, "pf").commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().show(publishFragment).commit();
                }
                Toast.makeText(getActivity(), "点击了publish按钮", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 处理ListView中item中的button点击事件
     * @param v view
     */
    @Override
    public void click(View v) {
        String[] tags = ((String)v.getTag()).split(",");
        switch (tags[1]){
            case "like":
                Log.i("Community-like","position:"+tags[0]);

                break;
            case "comment":
                showInputDialog(tags[0]);
                Log.i("Community-comment","position:"+tags[0]);
                break;
            default:
                break;
        }
        Toast.makeText(getActivity(), "ListView中Item的button被点击，位置是----->"+v.getTag(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 处理ListView中item点击事件
     * @param parent parent
     * @param view item的view
     * @param position item的位置
     * @param id item的id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "ListView中Item被点击，位置是----->"+position, Toast.LENGTH_SHORT).show();
    }

    /**
     * 带输入框的对话框
     */
    private void showInputDialog(String position) {
        new MaterialDialog.Builder(getContext())
                .title(R.string.comment)
                .inputType(
                        InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.hint_comment), "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                })
                .inputRange(1,50)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String info = dialog.getInputEditText().getText().toString();
                        //点击确认按钮触发事件
                        //将评论写回
                        writeComment(info, position);
                        Toast.makeText(getActivity(), "你的评论:"+info, Toast.LENGTH_SHORT).show();
                        //刷新fragment中的数据
                        initData();
                        }
                })
                .cancelable(false)
                .show();
    }

    /**
     * 将发表的评论写回数据库
     * @param content 内容
     * @param position 对应动态的位置，可以通过position获取对应动态的信息
     */
    private void writeComment(String content, String position){
        String comment = position+" "+username+":"+content;
        DataUtil.comment_community.add(comment);
        Log.i("comment",DataUtil.comment_community.toString());
    }

    public void initData(){
        //清空ListView中的数据
        listView.setAdapter(null);
        list.clear();
        //每条动态的评论内容
        //数据库部分完成后替换为从数据库中读取数据
        Log.i("Avatar", String.valueOf(R.drawable.dog1));
        Log.i("Avatar", String.valueOf(R.drawable.dog2));
        Log.i("Avatar", String.valueOf(R.drawable.keqing));
        for (int i = 0; i < DataUtil.situation.size(); i++) {
            SimpleAdapter commentAdapter = null;
            List<Map<String, Object>> itemCommunity = new ArrayList<>();
            //每条动态的信息，下标0为id，1为头像，2为时间，3为昵称，4为内容,5为图片
            String[] situationInfo = DataUtil.situation.get(i).split(",");

            List<String> situationList = Arrays.asList(situationInfo);
            //每条评论的信息，下标0为id，1为内容，查找每条动态下的评论
            for(int k = 0; k < DataUtil.comment_community.size(); k++) {
                String commentInfo = DataUtil.comment_community.get(k);
                //评论id与动态id相等
                if(commentInfo.split(" ")[0].equals(situationInfo[0])) {
                    List<String> comment = Arrays.asList(commentInfo.split(" ")[1].split(","));
                    for(int j = 0; j < comment.size(); j++){
                        Map<String, Object> item = new HashMap<>(16);
                        item.put("comment_name", comment.get(j).split(":")[0]);
                        item.put("comment_content", comment.get(j).split(":")[1]);
                        itemCommunity.add(item);
                    }
                }
            }
            commentAdapter = new SimpleAdapter(getActivity(), itemCommunity, R.layout.comment
                    , new String[]{"comment_name", "comment_content"}
                    , new int[]{R.id.comment_name, R.id.comment_content});
            list.add(new CommunityItemBean(Integer.parseInt(situationInfo[1]), situationInfo[2], situationInfo[3]
                    , situationInfo[4], Integer.parseInt(situationInfo[5]), commentAdapter));
        }

        listView.setAdapter(new CommunityAdapter(getActivity(), list, this));
        listView.setOnItemClickListener(this);
    }
}