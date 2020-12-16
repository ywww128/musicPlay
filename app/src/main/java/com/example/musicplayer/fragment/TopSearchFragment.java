package com.example.musicplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.volley.SongsMessageObtain;

/**
 * @author ywww
 * @date 2020-11-24 16:13
 * 搜索界面的搜索框及后退按钮功能
 */
public class TopSearchFragment extends Fragment {
    private View view;
    private LinearLayout searchLayout;
    private Button searchButton;
    private EditText searchEditView;
    private ImageView backView;
    private MainActivity mainActivity;
    private SongsMessageObtain songsMessageObtain;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_top_search,null);
       searchLayout = view.findViewById(R.id.top_search_layout);
       searchButton = view.findViewById(R.id.top_search_button);
       searchEditView = view.findViewById(R.id.top_search_edit);
       backView = view.findViewById(R.id.search_back_view);
       mainActivity = (MainActivity) getActivity();
       // 首次创建获取值
       Bundle bundle = getArguments();
       String keywords = bundle.getString("keywords");
       searchEditView.setText(keywords);
       initView();
       return view;
    }
    public void initView(){
        // 返回监听器
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getManager().popBackStack();
            }
        });
        // 搜索框回车监听器
        searchEditView.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(KeyEvent.KEYCODE_ENTER == keyCode){
                    click();
                }
                return false;
            }
        });
        // 搜索按钮监听器
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
    }

    private void click(){
        String keywords = searchEditView.getText().toString();
        if(keywords.equals("")){
            Toast.makeText(mainActivity,"歌曲名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        // 搜索后隐藏键盘
        hideSoftInput();
        // 搜索后去除搜索框焦点
        clearEditFocus();

        // 显示等待界面
        mainActivity.getTopMainFragment().getSearchResultFragment().beginWait();
        // 进行歌曲信息获取操作
        songsMessageObtain = new SongsMessageObtain(mainActivity,
                mainActivity.getTopMainFragment().getSearchResultFragment(),keywords);
        songsMessageObtain.startGetJson();
    }

    /**
     * 非首次进入传值
     * @param keywords
     */
    public void changeEditViewText(String keywords){
        this.searchEditView.setText(keywords);
    }

    public String getText(){
        return String.valueOf(searchEditView.getText());
    }


    public void hideSoftInput(){
        // 隐藏键盘
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public void clearEditFocus(){
        searchEditView.clearFocus();
    }
}
