package com.example.musicplayer.fragment;

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
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.CommunityAdapter;
import com.example.musicplayer.adapter.CommunityAdapter.Callback;
import com.example.musicplayer.bean.CommunityItemBean;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author czc
 */
public class CommunityFragment extends Fragment implements OnItemClickListener, Callback {

    private ListView listView;
    private TitleBar titleBar;

    public CommunityFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    private void init(View view){
        listView = view.findViewById(R.id.list1);
        titleBar = view.findViewById(R.id.tb_community);
        //数据库部分完成后替换为从数据库中读取数据
        List<CommunityItemBean> list = new ArrayList<>();
        list.add(new CommunityItemBean(R.drawable.dog1,"今天20:20","111","aaaaaa"));
        list.add(new CommunityItemBean(R.drawable.dog2,"今天20:20","222","aaaaaa"));
        list.add(new CommunityItemBean(R.drawable.keqing,"今天20:20","333","aaaaaa"));
        list.add(new CommunityItemBean(R.drawable.dog1,"今天20:20","444","aaaaaa"));
        list.add(new CommunityItemBean(R.drawable.dog1,"今天20:20","555","aaaaaa"));
        list.add(new CommunityItemBean(R.drawable.dog2,"今天20:20","666","aaaaaa"));
        list.add(new CommunityItemBean(R.drawable.dog1,"今天20:20","777","aaaaaa"));
        list.add(new CommunityItemBean(R.drawable.keqing,"今天20:20","888","aaaaaa"));
        list.add(new CommunityItemBean(R.drawable.dog1,"今天20:20","999","aaaaaa"));

        listView.setAdapter(new CommunityAdapter(getActivity(), list, this));
        listView.setOnItemClickListener(this);

        //设置标题栏左边图标监听器和右边图标及右边图标监听器
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "点击了返回按钮", Toast.LENGTH_SHORT).show();
            }
        //设置标题栏右边图标并设置监听器，点击后跳转到写动态界面
        }).addAction(new TitleBar.ImageAction(R.drawable.publish) {
            @Override
            public void performAction(View view) {
                //跳转到PublishFragment
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new PublishFragment(), null)
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
                showInputDialog();
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
    private void showInputDialog() {
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
                        Toast.makeText(getActivity(), "你的评论:"+dialog.getInputEditText().getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .cancelable(true)
                .show();
    }
}