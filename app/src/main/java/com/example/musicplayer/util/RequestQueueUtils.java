package com.example.musicplayer.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * @author czc
 */
public class RequestQueueUtils {
    private static RequestQueue requestQueue;

    /**
     * 单例创建RequestQueue并获取
     * @param context 上下文
     * @return RequestQueue对象
     */
    public static RequestQueue getRequestQueue(Context context){
        if(requestQueue == null){
            synchronized (RequestQueue.class){
                if(requestQueue == null){
                    requestQueue = Volley.newRequestQueue(context);
                }
            }
        }
        return requestQueue;
    }
}
