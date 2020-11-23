package com.example.musicplayer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

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

    private ListView listView;
    private TitleBar titleBar;
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

        init(view);
        return view;
    }

    /**
     * 将数据通过SimpleAdapter封装到ListView中
     */
    private void init(View view) {
        listView = view.findViewById(R.id.list1);
        titleBar = view.findViewById(R.id.tb_community);
        //每条动态的评论内容
        new DataUtil();
        //数据库部分完成后替换为从数据库中读取数据

        for (int i = 0; i < DataUtil.situation.size(); i++) {
            SimpleAdapter commentAdapter = null;
            List<Map<String, Object>> itemCommunity = new ArrayList<>();
            //每条动态的信息，下标0为id，1为头像，2为时间，3为昵称，4为内容
            String[] situationInfo = DataUtil.situation.get(i).split(" ");
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
            list.add(new CommunityItemBean(Integer.parseInt(situationInfo[1]), situationInfo[2], situationInfo[3], situationInfo[4]
                    , commentAdapter));
        }


        listView.setAdapter(new CommunityAdapter(getActivity(), list, this));
        listView.setOnItemClickListener(this);

        //设置标题栏左边图标监听器和右边图标及右边图标监听器
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "点击了返回按钮", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        //设置标题栏右边图标并设置监听器，点击后跳转到写动态界面
        }).addAction(new TitleBar.ImageAction(R.drawable.publish) {
            @Override
            public void performAction(View view) {
                //跳转到PublishFragment,并将参数username传递给PublishFragment，设置tag为pf
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, PublishFragment.newInstance(username), "pf")
                             .addToBackStack(null).commit();
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
                        Toast.makeText(getActivity(), "你的评论:"+dialog.getInputEditText().getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //点击确认按钮触发事件
                        //将评论写回
                        writeComment(dialog.getInputEditText().getText().toString(), position);
                        Toast.makeText(getActivity(), "你的评论:"+dialog.getInputEditText().getText().toString(), Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container
                                , CommunityFragment.newInstance(username), "cf").addToBackStack(null).commit();
                    }
                })
                .cancelable(true)
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
        Log.i("comment",comment);
    }

}