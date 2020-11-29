package com.example.musicplayer.volley;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicplayer.bean.Song;
import com.example.musicplayer.fragment.SearchResultFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * @author ywww
 * @date 2020-11-19 19:31
 */
public class SongsMessageObtain extends Application {
    private RequestQueue mQueue;
    private Context context;
    private ArrayList<Song> songs = new ArrayList<>();
    private SearchResultFragment searchResultFragment;
    private String url = "http://116.62.109.242:3000/search?keywords=";
    public SongsMessageObtain(Context context, SearchResultFragment searchResultFragment,String keywords){
        this.context = context;
        this.searchResultFragment = searchResultFragment;
        initRequestQueue();
        url = url + keywords + "&limit=100";
    }

    /**
     * 成功获取后调用的监听器
     */
    private class JsonListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            Gson gson = new Gson();
            try {
                JSONObject allSongs = response.getJSONObject("result");
                JSONArray allSongsArray = allSongs.getJSONArray("songs");
                // 将json转化成Java对象，null值会被转成字符串
                for(int i=0;i<allSongsArray.length();i++){
                    songs.add(gson.fromJson(String.valueOf(allSongsArray.getJSONObject(i)),Song.class));
                }
                searchResultFragment.updateView(songs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 失败后调用的监听器
     */
    private class StrErrListener implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("TAG", error.getMessage(), error);
            searchResultFragment.updateView(songs);
        }
    }


    /**
     * 初始化
     */
    private void initRequestQueue() {
        mQueue = Volley.newRequestQueue(context);
    }

    /**
     * get请求
     */
    public void startGetJson() {
        // 默认情况下设成null为get方法,否则为post方法。
        JsonObjectRequest srReq = new JsonObjectRequest(url, null,
                new JsonListener(), new StrErrListener());
        // 控制是否缓存
        srReq.setShouldCache(true);
        mQueue.add(srReq);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

}

