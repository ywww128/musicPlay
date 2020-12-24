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

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.volley.UserRegister;
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
    private EditText idView;
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
        idView = view.findViewById(R.id.id_register_text);
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
                clearFocus();
                mainActivity.getManager().popBackStack();
            }
        });

        // 点击注册监听器(暂用，后续需要服务器）
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                String id = String.valueOf(idView.getText());
                String username = String.valueOf(usernameView.getText());
                String password = String.valueOf(passwordView.getText());
                String confirm_password = String.valueOf(passwordConfirmView.getText());
                JSONObject msg = new JSONObject();
                if(username != "" && password != ""){
                    // 验证两次输入的密码是否一致
                    if(!password.equals(confirm_password)){
                        Toast.makeText(getActivity(),"密码不一致",Toast.LENGTH_SHORT).show();
                        passwordView.setText("");
                        passwordConfirmView.setText("");
                        return;
                    }
                    try {
                        msg.put("username",username);
                        msg.put("passwd",password);
                        msg.put("id",id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 进行服务器检验，如果符合要求即注册
                    UserRegister userRegister = new UserRegister(mainActivity,RegisterFragment.this,msg);
                    userRegister.register();
                } else {
                    Toast.makeText(getActivity(),"账号密码不为空",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void dealRegisterResult(int result){
        if(result == 0){
            Toast.makeText(getActivity(),"账号已存在",Toast.LENGTH_SHORT).show();
            idView.setText("");
        } else {
            Toast.makeText(getActivity(),"注册成功",Toast.LENGTH_SHORT).show();
            mainActivity.getManager().popBackStack();
        }
    }

    /**
     * 取消输入框焦点
     */
    private void clearFocus(){
        usernameView.clearFocus();
        idView.clearFocus();
        passwordView.clearFocus();
        passwordConfirmView.clearFocus();
        // 隐藏键盘
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
