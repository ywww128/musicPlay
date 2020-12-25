package com.example.musicplayer.volley;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicplayer.fragment.RegisterFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ywww
 * @date 2020-12-22 0:04
 */
public class UserRegister extends Application {

    private Context context;
    private Fragment fragment;
    private JSONObject msg;
    private RequestQueue mQueue;
    private String url = "http://116.62.109.242:9988/user/insert";
    public UserRegister(Context context, Fragment fragment, JSONObject msg){
        this.context = context;
        this.fragment = fragment;
        this.msg = msg;
        Log.d("msg",String.valueOf(msg));
        initRequestQueue();
    }

    private void initRequestQueue(){
        this.mQueue = Volley.newRequestQueue(context);
    }

    public void register(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,new StringListener(),new StringError()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>(100);
                map.put("msg",String.valueOf(msg));
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    private class StringListener implements Response.Listener<String> {

        @Override
        public void onResponse(String response) {
            RegisterFragment registerFragment = (RegisterFragment) fragment;
            registerFragment.dealRegisterResult(Integer.parseInt(response));
        }
    }

    private class StringError implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("TAG",error.toString());
        }
    }
}
