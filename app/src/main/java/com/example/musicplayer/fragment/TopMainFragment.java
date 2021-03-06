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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.bean.PlaySongData;
import com.example.musicplayer.volley.SongsMessageObtain;

/**
 * @author ywww
 * @date 2020-11-20 19:35
 * 主页搜索框与进入播放界面功能
 */
public class TopMainFragment extends Fragment{

    private View view;
    private Button searchButton;
    private EditText searchEdit;
    private ImageView toMoreFunction;
    private SongsMessageObtain songsMessageObtain;

    private SearchResultFragment searchResultFragment;
    private MainActivity mainActivity;
    private FragmentManager fManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top_main,null);
        searchButton = view.findViewById(R.id.main_search_button);
        searchEdit = view.findViewById(R.id.main_search_view);
        toMoreFunction = view.findViewById(R.id.to_more_function_view);
        mainActivity = (MainActivity) getActivity();
        fManager = mainActivity.getManager();
        initSearchView();
        return view;
    }

    /**
     * 搜索歌曲功能
     */
    public void initSearchView(){
        // 搜索框回车监听器
        searchEdit.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN){
                    click();
                }
                return false;
            }
        });
        // 搜索按钮点击监听器
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
        // 打开播放界面的view的监听器
        toMoreFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void click() {
        String keywords = searchEdit.getText().toString();
        if("".equals(keywords)){
            Toast.makeText(mainActivity,"歌曲名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        FragmentTransaction fTransaction = fManager.beginTransaction();
        mainActivity.hideBottomView(fTransaction);
        mainActivity.hideTopView(fTransaction);
        if(searchResultFragment == null){
            searchResultFragment = new SearchResultFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("keywords",keywords);
        searchResultFragment.setArguments(bundle);
        fTransaction.replace(R.id.content_panel,searchResultFragment).addToBackStack(null).commit();
        searchEdit.setText("");

        // 进行歌曲信息获取操作
        songsMessageObtain = new SongsMessageObtain(mainActivity,searchResultFragment,keywords);
        songsMessageObtain.startGetJson();
    }

    public void hideSearchResultFragment(FragmentTransaction fragmentTransaction) {
        fragmentTransaction.hide(searchResultFragment);
    }

    public SearchResultFragment getSearchResultFragment() {
        return searchResultFragment;
    }

    /**
     * 解决回退后键盘未隐藏以及光标出现的问题
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        // 隐藏键盘
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
