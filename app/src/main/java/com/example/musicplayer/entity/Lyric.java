package com.example.musicplayer.entity;

/**
 * @author ywww
 * @date 2020-11-22 16:04
 * 歌词类
 */
public class Lyric {
    public int version;
    public String lyric;

    @Override
    public String toString() {
        return "Lyric{" +
                "version=" + version +
                ", lyric='" + lyric + '\'' +
                '}';
    }
}
