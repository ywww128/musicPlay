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
import com.example.musicplayer.volley.UserLoginCheck;
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
    private RoundButton logoutButton;
    private MainActivity mainActivity;
    private FragmentManager fManager;
    private LoginFragment loginFragment;
    /**
     * 用来判断是否要进行自动登录操作
     */
    private String id = null;
    private boolean isLogin = false;
    private String msgString = null;
    private JSONObject msg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_my_account,null);
            loginTextView = view.findViewById(R.id.login_text_view);
            logoutButton = view.findViewById(R.id.my_account_logout_button);
            logoutButton.setVisibility(View.GONE);
            mainActivity = (MainActivity) getActivity();
            fManager = mainActivity.getManager();
            initListener();
        }
        // 自动登录或登陆界面传的bundle
        Bundle bundle = getArguments();
        if(bundle != null){
            // 自动登录或登陆界面登录成功
            msgString = bundle.getString("msg");
            try {
                msg = new JSONObject(msgString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // 已经退出登录或者自动登录失败
            msgString = null;
        }
        // 登录信息展示在页面,已经展示便不再展示
        if(msgString != null && !isLogin){
            try {
                changeToLogin(msg.getString("userName"));
                id = msg.getString("userId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            isLogin = true;
            loginTextView.setEnabled(false);
        }
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
                loginFragment = new LoginFragment(MyAccountFragment.this);
                fTransaction.replace(R.id.content_panel,loginFragment).addToBackStack(null).commit();
            }
        });

        // 登录/退出登录按钮监听
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("user",Context.MODE_PRIVATE);
                String value = null;
                // 获取要退出登录用户在SharePreferences里的信息
                value = sharedPreferences.getString(id,"");
                // 退出登录操作(将sharePreferences里的isLogin置为false,重启就不会自动登录)
                if(!"".equals(value)){
                    try {
                        JSONObject jsonObject = new JSONObject(value);
                        jsonObject.put("isLogin",false);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(id,String.valueOf(jsonObject));
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                loginTextView.setText("还未登录，点击登录");
                isLogin = false;
                MyAccountFragment.this.setArguments(null);
                loginTextView.setEnabled(true);
                logoutButton.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 登录成功后对展示信息进行修改
     * @param username
     */
    public void changeToLogin(String username){
        loginTextView.setText(username);
        logoutButton.setVisibility(View.VISIBLE);
    }

}
