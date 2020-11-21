package com.example.musicplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.valley.SongsMessageObtain;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

/**
 * @author ywww
 * @date 2020-11-20 19:35
 */
public class TopMainFragment extends Fragment {

    private Button searchButton;
    private SongsMessageObtain songsMessageObtain;

    private SearchResultFragment searchResultFragment;
    private MainActivity mainActivity;
    private FragmentManager fManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_main,null);
        searchButton = view.findViewById(R.id.main_search_button);
        mainActivity = (MainActivity) getActivity();
        fManager = mainActivity.getManager();
        initSearchView();
        return view;
    }

    /**
     * 搜索歌曲功能
     */
    public void initSearchView(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SearchFragment searchFragment = SearchFragment.newInstance();
                searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
                    @Override
                    public void OnSearchClick(String keyword) {
                        // 进行歌曲信息获取操作
                        songsMessageObtain = new SongsMessageObtain(mainActivity,keyword);
                        songsMessageObtain.startGetJson();
                        // 传入数据到SearchResultFragment
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("songs",songsMessageObtain.getSongs());
                        // 待进行多线程操作
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FragmentTransaction fTransaction = fManager.beginTransaction();
                        mainActivity.hideBottomView(fTransaction);
                        mainActivity.hideTopView(fTransaction);
                        //mainActivity.getBottomMainFragment().hideAllFragment(fTransaction);
                        if(searchResultFragment != null){
                            fTransaction.remove(searchResultFragment);
                        }
                        searchResultFragment = new SearchResultFragment();
                        searchResultFragment.setArguments(bundle);
                        //fTransaction.replace(R.id.fragment_top_main,null);
                        fTransaction.replace(R.id.main_layout,searchResultFragment).addToBackStack(null).commit();
                    }
                });
                searchFragment.showFragment(fManager,SearchFragment.TAG);
            }
        });
    }
}
