package com.example.musicplayer.bean;


/**
 * @author ywww
 * @date 2020-11-19 21:32
 * 专辑
 */
public class Album {
    public int id;
    public String name;
    public Artist artist;

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist=" + artist +
                ", publishTime=" + publishTime +
                ", size=" + size +
                ", copyrightId=" + copyrightId +
                ", status=" + status +
                ", picId=" + picId +
                ", mark=" + mark +
                '}';
    }

    public long publishTime;
    public int size;
    public int copyrightId;
    public int status;
    public long picId;
    public int mark;
}
