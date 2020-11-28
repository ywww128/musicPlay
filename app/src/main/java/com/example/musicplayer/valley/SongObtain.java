package com.example.musicplayer.valley;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicplayer.bean.ConcreteSong;
import com.example.musicplayer.bean.PlaySongData;
import com.example.musicplayer.bean.Song;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Queue;

/**
 * @author ywww
 * @date 2020-11-20 0:12
 * 获取音乐链接
 */
public class SongObtain extends Application {
    private PlaySongData playSongData;
    private Context context;
    private RequestQueue mQueue;
    private ConcreteSong concreteSong;
    private String url = "http://116.62.109.242:3000/song/url?id=";
    public SongObtain(Context context, PlaySongData playSongData){
        this.context = context;
        this.url = this.url + playSongData.getId();
        this.playSongData = playSongData;
        initRequestQueue();
    }

    public ConcreteSong getConcreteSong() {
        return concreteSong;
    }

    /**
     * 成功获取后调用的监听器
     */
    private class JsonListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            Gson gson = new Gson();
            try {
                JSONArray concreteJSONArray = response.getJSONArray("data");
                JSONObject concreteSongJSON = concreteJSONArray.getJSONObject(0);
                // 下列为测试代码
                Log.d("TAAAAN",String.valueOf(concreteSongJSON));
                concreteSong = gson.fromJson(String.valueOf(concreteSongJSON),ConcreteSong.class);
                Log.d("TAD",concreteSong.toString());
//                MediaPlayer mediaPlayer = new MediaPlayer();
//                try {
//                    mediaPlayer.setDataSource(concreteSong.url);
//                    mediaPlayer.prepareAsync();
//                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        //异步准备监听
//                        @Override
//                        public void onPrepared(MediaPlayer mediaPlayer) {
//                            mediaPlayer.start();//播放
//                        }
//                    });
//                }catch (Exception e){
//                }
                // 获取歌词的操作
                playSongData.setUrl(concreteSong.url);
                SongWordsObtain songWordsObtain = new SongWordsObtain(context, playSongData);
                songWordsObtain.startGetJson();
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
        }
    }

    private void initRequestQueue() {
        this.mQueue = Volley.newRequestQueue(context);
    }

    /**
     * get请求
     */
    public void startGetJson() {
        // 默认情况下设成null为get方法,否则为post方法。
        JsonObjectRequest srReq = new JsonObjectRequest(url, null,
                new SongObtain.JsonListener(), new SongObtain.StrErrListener());
        // 控制是否缓存
        srReq.setShouldCache(false);
        mQueue.add(srReq);
    }

}
