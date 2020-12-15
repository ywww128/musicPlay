package com.example.musicplayer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;


/**
 * @author ywww
 * @date 2020-12-15 0:02
 */
public class RegisterFragment extends Fragment {
    private View view;
    private EditText usernameView;
    private EditText passwordView;
    private EditText passwordConfirmView;
    private TextView backView;
    private RoundButton registerButton;
    private MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register,null);
        usernameView = view.findViewById(R.id.username_register_text);
        passwordView = view.findViewById(R.id.password_register_text);
        passwordConfirmView = view.findViewById(R.id.confirm_password_register_text);
        backView = view.findViewById(R.id.back_to_login_view);
        registerButton = view.findViewById(R.id.register_button);
        mainActivity = (MainActivity) getActivity();
        initListener();
        return view;
    }

    private void initListener(){
        // 返回登录界面监听
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getManager().popBackStack();
            }
        });

        // 点击注册监听器(暂用，后续需要服务器）
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(usernameView.getText());
                String password = String.valueOf(passwordView.getText());
                String confirm_password = String.valueOf(passwordConfirmView.getText());
                JSONObject jsonObject = new JSONObject();
                if(username != "" && password != ""){
                    // 验证两次输入的密码是否一致
                    if(!password.equals(confirm_password)){
                        Toast.makeText(getActivity(),"密码不一致",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("user",Context.MODE_PRIVATE);
                    Map<String,String> map = (Map<String, String>) sharedPreferences.getAll();
                    Set<String> set = map.keySet();
                    // 遍历查看账号是否已经注册
                    for(String k : set){
                        if(k.equals(username)){
                            Toast.makeText(getActivity(),"账号已存在",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    try {
                        jsonObject.put("username",username);
                        jsonObject.put("password",password);
                        jsonObject.put("isLogin",false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.putString(username,String.valueOf(jsonObject));
                    editor.commit();
                    Toast.makeText(getActivity(),"注册成功",Toast.LENGTH_SHORT).show();
                    mainActivity.getManager().popBackStack();
                } else {
                    Toast.makeText(getActivity(),"账号密码不为空",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
