package com.example.musicplayer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private Button searchButton;
    private EditText searchEditView;
    private ImageView backView;
    private MainActivity mainActivity;
    private SongsMessageObtain songsMessageObtain;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_top_search,null);
       searchButton = view.findViewById(R.id.top_search_button);
       searchEditView = view.findViewById(R.id.top_search_edit);
       backView = view.findViewById(R.id.search_back_view);
       mainActivity = (MainActivity) getActivity();
       Log.d("TAA","2");
       Bundle bundle = getArguments();
       String keywords = bundle.getString("keywords");
       searchEditView.setText(keywords);
       initView();
       return view;
    }
    public void initView(){
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
        // 进行歌曲信息获取操作
        songsMessageObtain = new SongsMessageObtain(mainActivity,
                mainActivity.getTopMainFragment().getSearchResultFragment(),keywords);
        songsMessageObtain.startGetJson();
    }

    public void changeEditViewText(String keywords){
        this.searchEditView.setText(keywords);
    }
}
