package com.example.musicplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_top_search,null);
       searchButton = view.findViewById(R.id.top_search_button);
       searchEditView = view.findViewById(R.id.top_search_edit);
       backView = view.findViewById(R.id.search_back_view);
       mainActivity = (MainActivity) getActivity();
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
    }
}
