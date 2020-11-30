package com.example.musicplayer.bean;

/**
 * @author ywww
 * @date 2020-11-29 18:23
 */
public class LocalSong {
    private long id;
    private long album_id;
    /**
     * 歌名
     */
    private String name;
    /**
     * 歌手
     */
    private String artist_name;
    private long size;
    /**
     * 歌曲绝对路径
     */
    private String url;
    private int isMusic;
    /**
     * 歌曲时长
     */
    private long duration;
    /**
     * 专辑名
     */
    private String album;

    public long getId() {
        return id;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public String getName() {
        return name;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public int getIsMusic() {
        return isMusic;
    }

    public long getDuration() {
        return duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIsMusic(int isMusic) {
        this.isMusic = isMusic;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
