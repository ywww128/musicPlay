package com.example.musicplayer.volley;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicplayer.fragment.BottomMainFragment;
import com.example.musicplayer.fragment.LoginFragment;
import com.example.musicplayer.fragment.MyAccountFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 检验登录的账号密码
 * @author ywww
 * @date 2020-12-20 15:31
 */
public class UserLoginCheck extends Application {
    private RequestQueue mQueue;
    private Context context;
    private final String url = "http://10.0.2.2:9988/user/login";
    private Fragment fragment;
    private String id;
    private String password;

    public UserLoginCheck(Context context, Fragment fragment, String id, String password){
        this.context = context;
        this.fragment = fragment;
        this.id = id;
        this.password = password;
        initRequestQueue();
    }

    private void initRequestQueue(){
        this.mQueue = Volley.newRequestQueue(context);
    }

    /**
     * 检验登录
     */
    public void checkUser(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,new StringListener(),new ErrorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>(100);
                map.put("userId",id);
                map.put("userPasswd",password);
                return map;
            }

            // 解决返回字符串乱码问题
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String info = new String(response.data,"UTF-8");
                    return Response.success(info, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        mQueue.add(stringRequest);
    }

    private class StringListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            JSONObject msg = null;
            try {
                msg = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("TAG",response);
            // 判断是从哪个fragment要实现登录操作
            if(fragment.getClass() == LoginFragment.class){
                LoginFragment loginFragment = (LoginFragment) fragment;
                loginFragment.dealLoginResult(msg);
            } else if(fragment.getClass() == BottomMainFragment.class){
                BottomMainFragment bottomMainFragment = (BottomMainFragment) fragment;
                bottomMainFragment.dealLoginResult(msg);
            }
        }
    }

    private class ErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("TAG",error.toString());
        }
    }
}
