package com.example.musicplayer.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author ywww
 * @date 2020-11-19 21:30
 * 歌手
 */
public class Artist implements Serializable {
    public int id;
    public String name;
    public String picUrl;
    public ArrayList<String> alias;
    public int albumSIze;
    public long picId;
    public String img1v1Url;
    public int img1v1;
    public String trans;

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", alias=" + alias +
                ", albumSIze=" + albumSIze +
                ", picId=" + picId +
                ", img1v1Url='" + img1v1Url + '\'' +
                ", img1v1=" + img1v1 +
                ", trans='" + trans + '\'' +
                '}';
    }
}
