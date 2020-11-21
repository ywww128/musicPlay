package com.example.musicplayer.valley;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Queue;

/**
 * @author ywww
 * @date 2020-11-20 0:12
 * 获取音乐链接
 */
public class SongObtain extends Application {
    private Context context;
    private RequestQueue mQueue;
    private String url = "http://116.62.109.242:3000/song/url?id=";
    public SongObtain(Context context, RequestQueue mQueue,int id){
        this.context = context;
        this.mQueue = mQueue;
        this.url = this.url + id;
        initRequestQueue();
    }

    private void initRequestQueue() {
        this.mQueue = Volley.newRequestQueue(context);
    }


}
