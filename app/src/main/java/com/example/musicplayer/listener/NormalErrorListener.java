package com.example.musicplayer.listener;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * @author czc
 */
public class NormalErrorListener implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("TAG", error.getMessage(), error);
    }
}
