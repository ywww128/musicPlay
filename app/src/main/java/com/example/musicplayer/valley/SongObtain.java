package com.example.musicplayer.valley;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;

/**
 * @author ywww
 * @date 2020-11-20 0:12
 */
public class SongObtain extends Application {
    private Context context;
    private RequestQueue mQueue;
    private String url;
    public SongObtain(Context context, RequestQueue mQueue,String url){
        this.context = context;
        this.mQueue=mQueue;
        this.url=url;
    }
}
