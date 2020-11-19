package com.example.musicplayer.bean;

import java.util.ArrayList;

/**
 * @author ywww
 * @date 2020-11-19 21:18
 * 歌曲信息
 */
public class Song {
    public int id;
    public String name;
    public ArrayList<Artist> artists;
    public Album album;
    public int duration;
    public int copyrightId;
    public int status;
    public ArrayList<String> alias;
    public int rtype;
    public int ftype;
    public int mvid;
    public int fee;
    public String rUrl;
    public int mark;

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artists=" + artists +
                ", album=" + album +
                ", duration=" + duration +
                ", copyrightId=" + copyrightId +
                ", status=" + status +
                ", alias=" + alias +
                ", rtype=" + rtype +
                ", ftype=" + ftype +
                ", mvid=" + mvid +
                ", fee=" + fee +
                ", rUrl='" + rUrl + '\'' +
                ", mark=" + mark +
                '}';
    }
}
