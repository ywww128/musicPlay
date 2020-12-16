package com.example.musicplayer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author ywww
 * @date 2020-12-14 0:30
 */
public class LoginFragment extends Fragment {
    private View view;
    private EditText usernameView;
    private EditText passwordView;
    private TextView backView;
    private RoundButton loginButton;
    private TextView toRegisterView;
    private MainActivity mainActivity;
    private FragmentManager fManager;
    private boolean isFromRegister = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login,null);
        usernameView = view.findViewById(R.id.username_login_text);
        passwordView = view.findViewById(R.id.password_login_text);
        backView = view.findViewById(R.id.cancel_login_view);
        loginButton = view.findViewById(R.id.login_button);
        toRegisterView = view.findViewById(R.id.to_register_view);
        mainActivity = (MainActivity) getActivity();
        fManager = mainActivity.getManager();
        initListener();
        return view;
    }

    /**
     * 设监听器
     */
    private void initListener(){
        // 退出登录界面监听
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fManager.popBackStack();
            }
        });

        // 进入注册界面监听
        toRegisterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromRegister = true;
                FragmentTransaction fTransaction = fManager.beginTransaction();
                fTransaction.hide(LoginFragment.this);
                fTransaction.add(R.id.content_panel,new RegisterFragment()).addToBackStack(null).commit();
            }
        });

        // 点击登录按钮监听器(暂用，后续需要服务器）
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(usernameView.getText());
                String password = String.valueOf(passwordView.getText());
                if(username != null && password != null){
                    SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                    Map<String,String> map = (Map<String, String>) sharedPreferences.getAll();
                    // 判断账号是否存在
                    if(map.containsKey(username)){
                        // 取出json数据
                        String value = map.get(username);
                        try {
                            JSONObject jsonObject = new JSONObject(value);
                            if(jsonObject.getString("password").equals(password)){
                                Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();
                                // 修改为已登录状态，此状态只允许一个账号为true
                                jsonObject.put("isLogin",true);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username,String.valueOf(jsonObject));
                                editor.commit();
                                // 登录成功返回
                                fManager.popBackStack();
                            } else {
                                Toast.makeText(getActivity(),"密码错误",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(),"该账号不存在",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 清空注册前的输入
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            usernameView.setText("");
            passwordView.setText("");
        }
        // 隐藏键盘
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
