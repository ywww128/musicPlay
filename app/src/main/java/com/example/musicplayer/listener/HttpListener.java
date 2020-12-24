package com.example.musicplayer.listener;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.musicplayer.bean.Post;
import com.example.musicplayer.util.DataUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 14548
 */
public class HttpListener {

    public Response.Listener<String> getAllPostListener(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type postListType = new TypeToken<ArrayList<Post>>(){}.getType();
                List<Post> posts = gson.fromJson(response, postListType);
                DataUtils.setPosts(posts);
            }
        };
        return listener;
    }
    public Response.ErrorListener errorListener(){
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HttpListener", error.getMessage(), error);
            }
        };
        return errorListener;
    }

}
