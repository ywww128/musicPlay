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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.valley.SongsMessageObtain;

/**
 * @author ywww
 * @date 2020-11-20 19:35
 */
public class TopMainFragment extends Fragment {

    private View view;
    private Button searchButton;
    private EditText searchEdit;
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
        mainActivity = (MainActivity) getActivity();
        fManager = mainActivity.getManager();
        initSearchView();
        return view;
    }

    /**
     * 搜索歌曲功能
     */
    public void initSearchView(){
        searchEdit.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(KeyEvent.KEYCODE_ENTER == keyCode){
                    click(searchButton);
                }
                return false;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });
    }

    private void click(View v) {
        String keywords = searchEdit.getText().toString();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        mainActivity.hideBottomView(fTransaction);
        mainActivity.hideTopView(fTransaction);
        if(searchResultFragment == null){
            searchResultFragment = new SearchResultFragment();
        }
        fTransaction.replace(R.id.content_panel,searchResultFragment).addToBackStack(null).commit();
        // 隐藏键盘
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

        // 进行歌曲信息获取操作
        songsMessageObtain = new SongsMessageObtain(mainActivity,searchResultFragment,keywords);
        songsMessageObtain.startGetJson();
    }

    public void hideSearchResultFragment(FragmentTransaction fragmentTransaction) {
        fragmentTransaction.hide(searchResultFragment);
    }

}
