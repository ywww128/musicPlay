package com.example.musicplayer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.adapter.LoginListAdapter;
import com.example.musicplayer.volley.UserLoginCheck;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ywww
 * @date 2020-12-14 0:30
 */
public class LoginFragment extends Fragment {
    private View view;
    private MyAccountFragment myAccountFragment;
    private EditText idView;
    private EditText passwordView;
    private TextView backView;
    private RoundButton loginButton;
    private TextView toRegisterView;
    private MainActivity mainActivity;
    private FragmentManager fManager;
    private View recyclerViewContent;
    private RecyclerView recyclerView;
    private PopupWindow popupWindow = null;
    private ImageView showHistoryIdButton;
    private String id;
    private String password;
    public  LoginFragment(){}
    public LoginFragment(MyAccountFragment myAccountFragment){
        this.myAccountFragment = myAccountFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login,null);
        idView = view.findViewById(R.id.id_login_text);
        passwordView = view.findViewById(R.id.password_login_text);
        backView = view.findViewById(R.id.cancel_login_view);
        loginButton = view.findViewById(R.id.login_button);
        toRegisterView = view.findViewById(R.id.to_register_view);
        showHistoryIdButton = view.findViewById(R.id.show_history_id_button);
        recyclerViewContent = inflater.inflate(R.layout.recyclerview_login_history,null);
        recyclerView = recyclerViewContent.findViewById(R.id.login_history_list);
        mainActivity = (MainActivity) getActivity();
        fManager = mainActivity.getManager();
        initListener();
        initRecyclerView();
        return view;
    }

    /**
     * 设监听器
     */
    private void initListener(){
        showHistoryIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                } else {
                    showPopWindow();
                }
            }
        });
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
                clearFocus();
                FragmentTransaction fTransaction = fManager.beginTransaction();
                fTransaction.hide(LoginFragment.this);
                fTransaction.add(R.id.content_panel,new RegisterFragment()).addToBackStack(null).commit();
            }
        });

        // 点击登录按钮监听器进行服务器验证
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = String.valueOf(idView.getText());
                password = String.valueOf(passwordView.getText());
                // 判断密码和账号是否有为空的情况
                if(!"".equals(id) && !"".equals(password)){
                    // 传入服务器进行登录操作
                    UserLoginCheck userLoginCheck = new UserLoginCheck(mainActivity,LoginFragment.this,id,password);
                    userLoginCheck.checkUser();
                } else {
                    Toast.makeText(getActivity(),"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                }
                clearFocus();
                passwordView.setText("");
            }
        });
    }

    private void showPopWindow(){
        popupWindow = new PopupWindow(recyclerViewContent, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setWidth(idView.getWidth()-30);
        popupWindow.showAsDropDown(idView,10,15);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showHistoryIdButton.setImageResource(R.drawable.pull_down_logo);
            }
        });
        showHistoryIdButton.setImageResource(R.drawable.pull_up_logo);
    }

    private void  initRecyclerView(){
        List<String> list = new ArrayList<>();
        // 从SharedPreferences里读出数据展示
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("user",Context.MODE_PRIVATE);
        Map<String,String> map = (Map<String, String>) sharedPreferences.getAll();
        Set<String> set = map.keySet();
        for(String s:set){
            list.add(s);
        }
        LoginListAdapter listAdapter = new LoginListAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new LoginListAdapter.OnItemClickListener() {
            @Override
            public void setOnClick(View view, int position) {
                TextView textView = (TextView) view;
                idView.setText(textView.getText());
                try {
                    String psw = new JSONObject(String.valueOf(map.get(textView.getText()))).getString("password");
                    passwordView.setText(psw);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 服务器响应后的处理
     * @param msg 服务器返回信息，登录失败会返回errorMessage，否则为用户信息
     */
    public void dealLoginResult(JSONObject msg){
        String errorMessage = null;
        try {
            errorMessage = msg.getString("errorMessage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 判断是否返回errorMessage
        if(errorMessage == null){
            SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
            Map<String,String> map = (Map<String, String>) sharedPreferences.getAll();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // 判断sharedPreferences里有没有此用户信息
            if(map.containsKey(id)){
                String value = map.get(id);
                try {
                    JSONObject jsonObject = new JSONObject(value);
                    // 修改为已登录状态，此状态只允许一个账号为true
                    jsonObject.put("isLogin",true);
                    editor.putString(id,String.valueOf(jsonObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    // 不存在则新建添加
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",String.valueOf(msg.getInt("userId")));
                    jsonObject.put("password",msg.getString("userPasswd"));
                    jsonObject.put("username",msg.getString("userName"));
                    jsonObject.put("isLogin",true);
                    editor.putString(id,String.valueOf(jsonObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            editor.apply();
            Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();
            mainActivity.getBottomMainFragment().setMsg(msg);
            // 传用户信息到账户界面进行展示
            Bundle bundle = new Bundle();
            bundle.putString("msg",String.valueOf(msg));
            myAccountFragment.setArguments(bundle);
            backView.performClick();
        } else {
            Log.d("ERRORMESSAGE",errorMessage);
            Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 清空注册前的输入
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            idView.setText("");
            passwordView.setText("");
        }
    }

    private void clearFocus(){
        idView.clearFocus();
        passwordView.clearFocus();
        // 隐藏键盘
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
