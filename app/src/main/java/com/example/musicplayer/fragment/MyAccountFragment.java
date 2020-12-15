package com.example.musicplayer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * @author ywww
 * @date 2020-11-17 23:33
 */
public class MyAccountFragment extends Fragment {
    private View view;
    private static final String LOGIN_BUTTON_TEXT = "登录";
    private static final String LOGOUT_BUTTON_TEXT = "退出登录";
    private TextView loginTextView;
    private RoundButton loginButton;
    private MainActivity mainActivity;
    private FragmentManager fManager;
    private LoginFragment loginFragment;
    private String username;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_account,null);
        loginTextView = view.findViewById(R.id.login_text_view);
        loginButton = view.findViewById(R.id.my_account_login_button);
        mainActivity = (MainActivity) getActivity();
        fManager = mainActivity.getManager();
        initListener();
        initLogin();
        return view;
    }

    /**
     * 设置监听器
     */
    private void initListener(){
        // 点击登录监听器
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fTransaction = fManager.beginTransaction();
                mainActivity.hideTopView(fTransaction);
                mainActivity.hideBottomView(fTransaction);
                loginFragment = new LoginFragment();
                fTransaction.replace(R.id.content_panel,loginFragment).addToBackStack(null).commit();
            }
        });

        // 登录/退出登录按钮监听
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断当前状态为登录还是非登录
                if(LOGIN_BUTTON_TEXT.equals(loginButton.getText())){
                    loginTextView.performClick();
                } else {
                    SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("user",Context.MODE_PRIVATE);
                    String value = sharedPreferences.getString(username,"");
                    // 退出登录操作
                    if(!value.equals("")){
                        try {
                            JSONObject jsonObject = new JSONObject(value);
                            jsonObject.put("isLogin",false);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(username,String.valueOf(jsonObject));
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    loginButton.setText(LOGIN_BUTTON_TEXT);
                    loginTextView.setText("还未登录，点击登录");
                }
            }
        });
    }

    /**
     * 初始化登录(暂用，后续需要服务器验证)
     */
    private void initLogin(){
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        Map<String,String> map = (Map<String, String>) sharedPreferences.getAll();
        Set<String> set = map.keySet();
        for(String k : set){
            String value = map.get(k);
            try {
                JSONObject jsonObject = new JSONObject(value);
                if(jsonObject.getBoolean("isLogin")){
                    username = k;
                    loginTextView.setText(username);
                    loginButton.setText(LOGOUT_BUTTON_TEXT);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
