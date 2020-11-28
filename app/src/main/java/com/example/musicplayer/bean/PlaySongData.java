package com.example.musicplayer.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author ywww
 * @date 2020-11-22 20:51
 * 简要歌曲信息实体类
 */
public class PlaySongData implements Serializable {
    private int id;
    private String name;
    private String lrc;
    private ArrayList<Artist> artists;
    private int duration;
    private String url;

    public PlaySongData() {}

    public PlaySongData(int id, String name, String lrc, ArrayList<Artist> artists, int duration, String url) {
        this.id = id;
        this.name = name;
        this.lrc = lrc;
        this.artists = artists;
        this.duration = duration;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

